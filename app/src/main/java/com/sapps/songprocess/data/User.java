package com.sapps.songprocess.data;

import androidx.recyclerview.widget.RecyclerView;

import com.sapps.songprocess.utils.Parser;

import org.json.JSONObject;

import java.util.List;

public class User {
    private String userName;
    private String name;
    private String password;
    private String id;
    private String uid;
    private String filePath;
    private String openSong;
    private String sendSongId;
    private String sendSongName;
    private List<String> usersId;
    private List<Line> songSubtitle;


    public User() {
        super();
    }

    public static User createUserFromResponse(JSONObject response) {
        User user = new User();
        JSONObject userJson = Parser.jsonParse(response, "user", new JSONObject());
        user.setUserName(Parser.jsonParseString(userJson, "username", ""));
        user.setName(Parser.jsonParseString(userJson, "name", ""));
        user.setPassword(Parser.jsonParseString(userJson, "password", ""));
        user.setUid(Parser.jsonParseString(userJson, "uid", ""));
        user.setId(Parser.jsonParseString(userJson, "_id", ""));
        user.setFilePath(Parser.jsonParseString(userJson, "filePath", ""));
        user.setOpenSong(Parser.jsonParseString(userJson, "openSong", ""));
        user.setSendSongId(Parser.jsonParseString(userJson, "sendSongId", ""));
        user.setSendSongName(Parser.jsonParseString(userJson, "sendSongName", ""));
        JSONObject subtitleJson = Parser.jsonParse(userJson, "songSubtitle", new JSONObject());
        user.setSongSubtitle(Parser.createList(subtitleJson, new Line()));

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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getOpenSong() {
        return openSong;
    }

    public void setOpenSong(String openSong) {
        this.openSong = openSong;
    }

    public String getSendSongId() {
        return sendSongId;
    }

    public void setSendSongId(String sendSongId) {
        this.sendSongId = sendSongId;
    }

    public List<String> getUsersId() {
        return usersId;
    }

    public void setUsersId(List<String> usersId) {
        this.usersId = usersId;
    }

    public String getSendSongName() {
        return sendSongName;
    }

    public void setSendSongName(String sendSongName) {
        this.sendSongName = sendSongName;
    }

    public List<Line> getSongSubtitle() {
        return songSubtitle;
    }

    public void setSongSubtitle(List<Line> songSubtitle) {
        this.songSubtitle = songSubtitle;
    }
}
