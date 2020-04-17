package com.sapps.songprocess.managers;

import com.sapps.songprocess.Application;

public abstract class Manager {
    protected Application application;
    protected boolean isInitialized;

    protected  Manager (Application application){
        this.application = application;
        init();
    }



    public void  init(){
        isInitialized = true;
    }
    public void reset(){};


}
