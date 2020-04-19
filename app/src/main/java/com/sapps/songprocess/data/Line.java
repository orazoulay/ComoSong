package com.sapps.songprocess.data;

import com.sapps.songprocess.utils.Parser;

import org.json.JSONObject;

public class Line {


    private String number;
    private String start;
    private String end;
    private String words;


    public static Line createUserFromResponse(JSONObject response) {
        Line line = new Line();
        JSONObject lineJson = Parser.jsonParse(response, "lines", new JSONObject());
        line.setNumber(Parser.jsonParseString(lineJson, "number", ""));
        line.setStart(Parser.jsonParseString(lineJson, "start", ""));
        line.setEnd(Parser.jsonParseString(lineJson, "end", ""));
        line.setWords(Parser.jsonParseString(lineJson, "words", ""));

        return line;

    }
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }
}
