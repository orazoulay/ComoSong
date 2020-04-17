package com.sapps.songprocess.requests;

import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.sapps.songprocess.Application;
import com.sapps.songprocess.data.GeneralData;
import com.sapps.songprocess.data.User;
import com.sapps.songprocess.utils.Parser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseRequest extends Request<JSONObject> {
    private static final String url = "http://10.100.102.6:3001";
    private static final String middleWare = "/api/";
    private HashMap params;
    private Map<String, String> mHeaders;
    private Response.Listener<NetworkResponse> mListener;
    private Response.ErrorListener mErrorListener;
    private final String twoHyphens = "--";
    private final String lineEnd = "\r\n";
    private final String boundary = "apiclient-" + System.currentTimeMillis();


    protected Application app() {
        return Application.getApp();
    }

    public BaseRequest(int method, String url,
                       Response.Listener<NetworkResponse> listener,
                       Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;

    }

    @Override
    public String getBodyContentType() {
        if (AuthenticationRequests.isUploadingData) {
            return "multipart/form-data;boundary=" + boundary;
        } else {
            return super.getBodyContentType();
        }
    }

    public BaseRequest(int method, String url, @Nullable Response.ErrorListener listener) {
        super(method, url, listener);
    }


    public BaseRequest(String method, HashMap params) {
        this(Method.POST, url + middleWare + method, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        this.params = params;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return (mHeaders != null) ? mHeaders : super.getHeaders();
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        String requestType = Parser.jsonParseString(response, "request", "");
        switch (requestType) {
            case "login":
                JSONObject loginJson = Parser.jsonParse(response, "data", new JSONObject());
                User user = User.createUserFromResponse(loginJson);
                app().getUserAccountManager().setUser(user);
                app().getUserAccountManager().pushMainFragment();
                Toast.makeText(app().getCurrectActivity(), "Login Request Successfully", Toast.LENGTH_SHORT).show();
                break;
            case "getUsers":
                GeneralData generalData = GeneralData.getInstance();
                JSONObject getUsersJson = Parser.jsonParse(response, "data", new JSONObject());
                JSONObject usersJson = Parser.jsonParse(getUsersJson, "users", new JSONObject());
                List<User> users = Parser.createList(usersJson, new User());
                generalData.setUsers(users);
                app().getUserAccountManager().pushChooseSongPropertiesFragment(users);
                break;


        }

    }


    public responseTypeEnum parseResponseType() {
        return responseTypeEnum.LOGIN;
    }

    public enum responseTypeEnum {
        LOGIN
    }


    @Override
    public byte[] getBody() throws AuthFailureError {
        if (!AuthenticationRequests.isUploadingData) {
            return super.getBody();
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);

            try {
                // populate text payload
                Map<String, String> params = getParams();
                if (params != null && params.size() > 0) {
                    textParse(dos, params, getParamsEncoding());
                }

                // populate data byte payload
                Map<String, BaseRequest.DataPart> data = getByteData();
                if (data != null && data.size() > 0) {
                    dataParse(dos, data);
                }

                // close multipart form data after text and file data
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                return bos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void textParse(DataOutputStream dataOutputStream, Map<String, String> params, String encoding) throws IOException {
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                buildTextPart(dataOutputStream, entry.getKey(), entry.getValue());
            }
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + encoding, uee);
        }
    }

    private void buildTextPart(DataOutputStream dataOutputStream, String parameterName, String parameterValue) throws IOException {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + parameterName + "\"" + lineEnd);
        dataOutputStream.writeBytes(lineEnd);
        dataOutputStream.writeBytes(parameterValue + lineEnd);
    }

    private void dataParse(DataOutputStream dataOutputStream, Map<String, BaseRequest.DataPart> data) throws IOException {
        for (Map.Entry<String, BaseRequest.DataPart> entry : data.entrySet()) {
            buildDataPart(dataOutputStream, entry.getValue(), entry.getKey());
        }
    }

    protected Map<String, BaseRequest.DataPart> getByteData() throws AuthFailureError {
        return null;
    }

    private void buildDataPart(DataOutputStream dataOutputStream, DataPart dataFile, String inputName) throws IOException {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" +
                inputName + "\"; filename=\"" + dataFile.getFileName() + "\"" + lineEnd);
        if (dataFile.getType() != null && !dataFile.getType().trim().isEmpty()) {
            dataOutputStream.writeBytes("Content-Type: " + dataFile.getType() + lineEnd);
        }
        dataOutputStream.writeBytes(lineEnd);

        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(dataFile.getContent());
        int bytesAvailable = fileInputStream.available();

        int maxBufferSize = 1024 * 1024;
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];

        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {
            dataOutputStream.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }

        dataOutputStream.writeBytes(lineEnd);
    }


    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


    public class DataPart {
        private String fileName;
        private byte[] content;
        private String type;

        public DataPart() {
        }

        public DataPart(String name, byte[] data, String type) {
            fileName = name;
            content = data;
        }

        String getFileName() {
            return fileName;
        }

        byte[] getContent() {
            return content;
        }

        String getType() {
            return type;
        }

    }

}
