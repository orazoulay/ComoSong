package com.sapps.songprocess.data;

import androidx.recyclerview.widget.RecyclerView;

import com.sapps.songprocess.utils.Parser;

import org.json.JSONObject;

public class User {
    private String userName;
    private String name;
    private String password;
    private String id;
    private String uid;

public User(){
    super();
}

public static User createUserFromResponse(JSONObject response){
    User user = new User();
    JSONObject userJson = Parser.jsonParse(response,"user",new JSONObject());
    user.setUserName(Parser.jsonParseString(userJson, "username", ""));
    user.setName(Parser.jsonParseString(userJson, "name", ""));
    user.setPassword(Parser.jsonParseString(userJson, "password", ""));
    user.setUid(Parser.jsonParseString(userJson, "uid", ""));
    user.setId(Parser.jsonParseString(userJson, "_id", ""));



    return user;
}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
