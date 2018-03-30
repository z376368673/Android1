package com.huazong.app.huazong.entity;

/**
 * Created by Administrator on 2016/7/30.
 */
public class MyInfo {
    private String picture;
    private String name;
    private int score;
    private String rankName;
    private int userId;

    public MyInfo() {
    }

    public MyInfo(String picture, String name, int score, String rankName, int userId) {
        this.picture = picture;
        this.name = name;
        this.score = score;
        this.rankName = rankName;
        this.userId = userId;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
