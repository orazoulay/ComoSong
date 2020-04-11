package com.sapps.songprocess.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sapps.songprocess.R;
import com.sapps.songprocess.fragments.MainFragment;
import com.sapps.songprocess.requests.VolleyMultipartRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_VIDEO_CAPTURE = 1;
    private String upload_URL = "http://10.100.102.7:3001/fileupload";
    private RequestQueue rQueue;
    private ArrayList<HashMap<String, String>> arraylist;
    String url = "http://10.100.102.7:3001/fileupload";

    private OnVideoReturnListener onVideoReturnListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendPostRequest();
        Fragment fragment = new MainFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flContainer,fragment);
        transaction.commit();




    }








    public void dispatchTakeVideoIntent(OnVideoReturnListener onVideoReturnListener , Context context) {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        this.onVideoReturnListener = onVideoReturnListener;
        if (takeVideoIntent.resolveActivity(context.getPackageManager()) != null) {
            takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 100);
            takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 8);
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = intent.getData();
            File videoFile = new File(videoUri.getPath());
//            this.videoURI = videoUri;

            uploadPDF("test",videoUri);
//            onVideoReturnListener.onVideoReturn(videoUri);

//            videoView.setVideoURI(videoUri);
//            videoView.start();
        }
    }



        public interface OnVideoReturnListener{
            void onVideoReturn(Uri videoUri);
        }

        public void sendPostRequest() {
            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
            String url = "http://10.100.102.7:3001/api/getInformation";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


                @Override
                public void onResponse(String response) {
                    Log.d("response",response);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("res_error","here");

                }
            });

            stringRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 10000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 10000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }

            });

            queue.add(stringRequest);

        }
        private void sendGetRequest(){
            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
            String url ="http://10.0.2.2:3001/api/";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("response",response);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("res_error",error.getMessage());

                }
            });
            queue.add(stringRequest);
        }

    private void uploadPDF(final String pdfname, Uri pdffile){

        InputStream iStream = null;
        try {

            iStream = getContentResolver().openInputStream(pdffile);
            final byte[] inputData = getBytes(iStream);

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            Log.d("ressssssoo",new String(response.data));
                            rQueue.getCache().clear();
                            try {
                                JSONObject jsonObject = new JSONObject(new String(response.data));
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                jsonObject.toString().replace("\\\\","");

                                if (jsonObject.getString("status").equals("true")) {
                                    Log.d("come::: >>>  ","yessssss");
                                    arraylist = new ArrayList<HashMap<String, String>>();
                                    JSONArray dataArray = jsonObject.getJSONArray("data");


                                    for (int i = 0; i < dataArray.length(); i++) {
                                        JSONObject dataobj = dataArray.getJSONObject(i);
                                        url = dataobj.optString("pathToFile");
//                                        tv.setText(url);
                                    }


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {

                /*
                 * If you want to add more parameters with the image
                 * you can do it here
                 * here we have only one parameter with the image
                 * which is tags
                 * */
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                     params.put("tags", "ccccc");
                    return params;
                }

                /*
                 *pass files using below method
                 * */
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();

                    params.put("filename", new DataPart(pdfname ,inputData,"video/*"));
                    return params;
                }
            };


            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            rQueue = Volley.newRequestQueue(MainActivity.this);
            rQueue.add(volleyMultipartRequest);



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

}
