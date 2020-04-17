package com.sapps.songprocess.data;

import java.util.ArrayList;
import java.util.List;

public class GeneralData {

    public static GeneralData generalData;
    public static List <User> users = new ArrayList<>();

    public static GeneralData getInstance(){
        if(generalData == null){
            generalData = new GeneralData();
        }

        return generalData;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
