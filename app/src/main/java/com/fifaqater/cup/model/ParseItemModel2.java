package com.fifaqater.cup.model;

public class ParseItemModel2 {
    private String match;
    private String league;
    private String day;
    private String time;

    public ParseItemModel2(String match, String league, String day, String time) {
        this.match = match;
        this.league = league;
        this.day = day;
        this.time = time;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
