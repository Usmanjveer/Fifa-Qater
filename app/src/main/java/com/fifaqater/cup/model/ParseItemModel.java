package com.fifaqater.cup.model;

import java.util.ArrayList;
import java.util.List;

public class ParseItemModel {
    private String imageA;
    private String imageB;
    private String teama;
    private String teamb;
    private String league;
    private String time;
    private List<String> postLinkList;
    private List<String> titleLinkList;
    private String gif;

    public ParseItemModel(String imageA, String imageB, String teama, String teamb, String league, String time, List<String> postLinkList, List<String> titleLinkList, String gif) {
        this.imageA = imageA;
        this.imageB = imageB;
        this.teama = teama;
        this.teamb = teamb;
        this.league = league;
        this.time = time;
        this.postLinkList = postLinkList;
        this.titleLinkList = titleLinkList;
        this.gif = gif;
    }

    public String getImageA() {
        return imageA;
    }

    public void setImageA(String imageA) {
        this.imageA = imageA;
    }

    public String getImageB() {
        return imageB;
    }

    public void setImageB(String imageB) {
        this.imageB = imageB;
    }

    public String getTeama() {
        return teama;
    }

    public void setTeama(String teama) {
        this.teama = teama;
    }

    public String getTeamb() {
        return teamb;
    }

    public void setTeamb(String teamb) {
        this.teamb = teamb;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getPostLinkList() {
        return postLinkList;
    }

    public void setPostLinkList(List<String> postLinkList) {
        this.postLinkList = postLinkList;
    }

    public List<String> getTitleLinkList() {
        return titleLinkList;
    }

    public void setTitleLinkList(List<String> titleLinkList) {
        this.titleLinkList = titleLinkList;
    }

    public String getGif() {
        return gif;
    }

    public void setGif(String gif) {
        this.gif = gif;
    }
}
