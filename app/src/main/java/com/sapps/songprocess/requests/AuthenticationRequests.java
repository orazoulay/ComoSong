package com.sapps.songprocess.requests;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.sapps.songprocess.Application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationRequests {
    private static final String loginMethod = "login";
    private static final String uploadVideoMethod = "uploadSong";
    private static final String getUsersMethod = "getUsers";
    private static RequestQueue rQueue;
    public static boolean isUploadingData = false;

    protected static Application app() {
        return Application.getApp();
    }

    public static void sendLoginRequest(Context context, String userName, String password) {
        isUploadingData = false;
        Toast.makeText(app().getCurrectActivity(), "Login Request Process", Toast.LENGTH_SHORT).show();
        rQueue = Volley.newRequestQueue(context);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("username", userName);
        hashMap.put("password", password);
        BaseRequest baseRequest = new BaseRequest(loginMethod, hashMap);
        rQueue.add(baseRequest);
    }

    public static void sendGetUsersRequest(Context context) {
        isUploadingData = false;
        Toast.makeText(app().getCurrectActivity(), "Get Users Request Process", Toast.LENGTH_SHORT).show();
        rQueue = Volley.newRequestQueue(context);
        HashMap<String, String> hashMap = new HashMap<>();
        BaseRequest baseRequest = new BaseRequest(getUsersMethod, hashMap);
        rQueue.add(baseRequest);
    }


    public static void sendUploadVideoRequest(Context context, final String songName, Uri songFile) {
        isUploadingData = true;
        InputStream iStream = null;
        try {
            iStream = app().getContentResolver().openInputStream(songFile);
            final byte[] inputData = BaseRequest.getBytes(iStream);

            Toast.makeText(app().getCurrectActivity(), "UploadVideo Request Process", Toast.LENGTH_SHORT).show();
            rQueue = Volley.newRequestQueue(context);
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("fileName", songName);

            BaseRequest baseRequest = new BaseRequest(uploadVideoMethod, hashMap) {
                @Override
                protected Map<String, BaseRequest.DataPart> getByteData() throws AuthFailureError {
                    Map<String, BaseRequest.DataPart> params = new HashMap<>();
                    params.put("name", new BaseRequest.DataPart(songName, inputData, "video/*"));
                    return params;
                }
            };

            rQueue.add(baseRequest);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
