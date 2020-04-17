package com.sapps.songprocess.fragments;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.VideoView;

import com.sapps.songprocess.R;
import com.sapps.songprocess.activities.MainActivity;

public class SongFragment extends ComoSongFragment {
    private Chronometer chronometer;
    private MediaPlayer mediaPlayer;
    private long pauseOffset;
    private boolean running;
    private VideoView videoView;
    private Button openCamBtn;
    private Button btnSendToServer;
    private ImageButton ibPlayer;
    long timeWhenStopped = 0;


    private MainActivity.OnVideoReturnListener onVideoReturnListener = new MainActivity.OnVideoReturnListener() {
        @Override
        public void onVideoReturn(Uri videoUri) {
            resetChronometer();
            videoView.setVideoURI(videoUri);
            videoView.setVisibility(View.VISIBLE);
            videoView.start();
        }

        ;
    };

    @Override
    protected int initLayout() {
        return R.layout.fragment_song;
    }

    protected void initViews(View view) {
        openCamBtn = view.findViewById(R.id.openCamBtn);
        videoView = view.findViewById(R.id.videoView);
        ibPlayer = view.findViewById(R.id.ibPlayer);
        chronometer = view.findViewById(R.id.chronometer);
        btnSendToServer = view.findViewById(R.id.btnSendToServer);
        chronometer.setFormat("זמן: %s");
        chronometer.setBase(SystemClock.elapsedRealtime());
        initSong();

    }

    protected void initListeners() {
        btnSendToServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).sendPostRequest();
            }
        });
        ibPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChronometer(v);

            }
        });
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if ((SystemClock.elapsedRealtime() - chronometer.getBase()) >= mediaPlayer.getDuration()) {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    Toast.makeText(getActivity(), "Bing!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        openCamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSong();
                ((MainActivity) getActivity()).dispatchTakeVideoIntent(onVideoReturnListener, getContext());


            }
        });

    }


    public void startChronometer(View v) {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
            chronometer.start();
            playSong();
            running = true;
        } else {
            pauseChronometer(v);
        }
    }

    public void pauseChronometer(View v) {
        if (running) {
            timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
            chronometer.stop();
            running = false;
            pauseSong();
        }
    }

    public void resetChronometer() {
       stopSong();
        chronometer.setBase(SystemClock.elapsedRealtime());
         timeWhenStopped = 0;
    }


    private void initSong() {
        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.angels);
        mediaPlayer.setLooping(true); // Set looping
        mediaPlayer.setVolume(100, 100);
    }

    private void playSong() {
        mediaPlayer.start();
    }

    private void pauseSong() {
        mediaPlayer.pause();
    }

    private void stopSong() {
        mediaPlayer.stop();
    }

    private void seekTo(int value) {
        mediaPlayer.seekTo(value);
    }


}
