package com.sapps.songprocess.activities;


import java.io.File;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;

import com.sapps.songprocess.R;

public class VideoScreen extends SurfaceView implements SurfaceHolder.Callback {

    private MediaRecorder recorder;
    private SurfaceHolder holder;
    public Context context;
    private Camera camera;
    public static String videoPath = Environment.getExternalStorageDirectory()
            .getPath() +"/YOUR_VIDEO.mp4";

    public VideoScreen(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public VideoScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VideoScreen(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @SuppressLint("NewApi")
    public void init() {
        try {
            recorder = new MediaRecorder();
            holder = getHolder();
            holder.addCallback(this);
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            camera = getCameraInstance();
            camera.setDisplayOrientation(90);
            camera.unlock();
            recorder.setCamera(camera);
            recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
            recorder.setOutputFile(videoPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    public void surfaceCreated(SurfaceHolder mHolder) {
        try {
            recorder.setPreviewDisplay(mHolder.getSurface());
            recorder.prepare();
            recorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopCapturingVideo() {
        try {
            recorder.stop();
            camera.lock();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(5)
    public void surfaceDestroyed(SurfaceHolder arg0) {
        if (recorder != null) {
            stopCapturingVideo();
            recorder.release();
            camera.lock();
            camera.release();
            recorder = null;
        }
    }

    private Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c;
    }
}