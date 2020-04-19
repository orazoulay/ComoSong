package com.sapps.songprocess.data;

public class OpenSong {
    private String songName;
    private String songSenderName;

    public OpenSong(String openSong, String songSenderName) {
        this.songName = openSong;
        this.songSenderName = songSenderName;

    }

    public OpenSong(Song song) {
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongSenderName() {
        return songSenderName;
    }

    public void setSongSenderName(String songSenderName) {
        this.songSenderName = songSenderName;
    }

}
