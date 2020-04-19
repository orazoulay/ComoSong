package com.sapps.songprocess.fragments;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.VideoView;

import androidx.annotation.Nullable;

import com.sapps.songprocess.R;
import com.sapps.songprocess.activities.MainActivity;
import com.sapps.songprocess.data.OpenSong;
import com.sapps.songprocess.requests.AuthenticationRequests;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

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
    private OpenSong openSong;
    private long songStarttime;
    private long songEndtime;
    private Uri vidUri;
    private OnContinueProcess onContinueProcess;


    private MainActivity.OnVideoReturnListener onVideoReturnListener = new MainActivity.OnVideoReturnListener() {
        @Override
        public void onVideoReturn(Uri videoUri) {
//            resetChronometer();
            videoView.setVideoURI(videoUri);
            vidUri = videoUri;
            videoView.setVisibility(View.VISIBLE);
            videoView.start();
        }


    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public SongFragment(OpenSong song) {
        this.openSong = song;
    }

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
//        initSong();

    }

    protected void initListeners() {
        btnSendToServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onContinueProcess != null) {
                    onContinueProcess.onSendToServer(vidUri);
                }
            }
        });
        ibPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChronometer(v);

            }
        });

        openCamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onContinueProcess != null) {
                    onContinueProcess.onContinue(onVideoReturnListener);
                }
            }
        });

    }


    public void startChronometer(View v) {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        playSong();

    }


    private void initSong() {
        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.shevet);
        mediaPlayer.setLooping(true); // Set looping
        mediaPlayer.setVolume(100, 100);
        this.songStarttime = preperTimes(app().getUserAccountManager().getUser().getSongSubtitle().get(0).getStart());
        this.songEndtime = preperTimes(app().getUserAccountManager().getUser().getSongSubtitle().get(0).getEnd());
        seekTo(songStarttime);

    }

    private long preperTimes(String sTime) {
        String[] parts = sTime.split(Pattern.quote(":"));
        long min = TimeUnit.MINUTES.toMillis(Long.parseLong(parts[0]));
        long sec = TimeUnit.SECONDS.toMillis(Long.parseLong(parts[1]));
        if (parts.length > 2) {
            long mili = Long.parseLong(parts[2]);
            sec += mili;
        }

        long time = min + sec;

        return time;

    }

    private void playSong() {
        initSong();
        mediaPlayer.start();
        ibPlayer.setEnabled(false);
        CountDownTimer timer = new CountDownTimer(songEndtime - songStarttime, 1000) {


            @Override
            public void onTick(long millisUntilFinished) {
                // Nothing to do
            }

            @Override
            public void onFinish() {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    chronometer.stop();
                    ibPlayer.setEnabled(true);

                }
            }
        };
        timer.start();
    }


    private void stopSong() {
        mediaPlayer.stop();
    }

    private void seekTo(long value) {
        if (value > (long) Integer.MAX_VALUE) {
            // x is too big to convert, throw an exception or something useful
        } else {
            value = ((int) value);
        }
        mediaPlayer.seekTo((int) value);
    }

    public void setOnContinueProcess(OnContinueProcess onContinueProcess) {
        this.onContinueProcess = onContinueProcess;
    }

    public interface OnContinueProcess {
        void onContinue(MainActivity.OnVideoReturnListener onVideoReturnListener);

        void onSendToServer(Uri uri);
    }
}
