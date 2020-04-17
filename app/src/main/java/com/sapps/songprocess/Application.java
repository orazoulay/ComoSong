package com.sapps.songprocess;

import android.content.Context;

import com.sapps.songprocess.activities.MainActivity;
import com.sapps.songprocess.managers.Manager;
import com.sapps.songprocess.managers.UserAccountManager;

import java.util.ArrayList;
import java.util.List;

public class Application extends android.app.Application {
    static Application app;
    private List<Manager> managers;



    private MainActivity currectActivity;

    public static Application getApp(){return app;}




    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        startManagers();
    }

    public List<Manager> getManagers(){
        if(managers == null){
            initManagers();
        }
        return managers;
    }

    private void initManagers() {
        managers = new ArrayList<>();
        managers.add(getUserAccountManager());
    }

    private void startManagers() {
        for (Manager manager: getManagers()){
            manager.init();
        }
    }
    public void resetMangers() {
        for (Manager manager: getManagers()){
            manager.reset();
        }
        initManagers();
    }

    public MainActivity getCurrectActivity() {
        return currectActivity;
    }

    public void setCurrectActivity(MainActivity currectActivity) {
        this.currectActivity = currectActivity;
    }

public UserAccountManager getUserAccountManager(){
        return UserAccountManager.getInstance(this);
}
}
