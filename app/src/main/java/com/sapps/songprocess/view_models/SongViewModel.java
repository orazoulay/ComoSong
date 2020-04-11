package com.sapps.songprocess.view_models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SongViewModel extends ViewModel {
    private  String TAG = this.getClass().getSimpleName();

    private MutableLiveData<String> mSongRunTime ;
    String songRunTime;

    public MutableLiveData<String> getmSongRunTime(){
        if(mSongRunTime == null){
            mSongRunTime = new MutableLiveData<String>();
        }
        return  mSongRunTime;
    }



    public String getSongRunTime() {
        return songRunTime;
    }
    public void setSongRunTime(String songRunTime) {
        this.songRunTime = songRunTime;
    }


}
