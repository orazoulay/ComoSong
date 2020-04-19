package com.sapps.songprocess.data;

import com.sapps.songprocess.utils.Parser;

import org.json.JSONObject;

import java.util.List;

public class SongSubtitles {
    private List<Line> lines;

    public static SongSubtitles createUserFromResponse(JSONObject response) {
        SongSubtitles songSubtitles = new SongSubtitles();
//        JSONObject userJson = Parser.jsonParse(response, "user", new JSONObject());
        songSubtitles.setLines(Parser.createList(response, new Line()));

        return songSubtitles;

    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }
}
