package com.huazong.app.huazong.entity;

/**
 * Created by Administrator on 2016/7/22.
 */
public class BeFriend {
    private int userId;
    private String name;
    private int score;
    private int depth;
    private String picture;

    public BeFriend() {
    }

    public BeFriend(int userId, String name, int score, int depth, String picture) {
        this.userId = userId;
        this.name = name;
        this.score = score;
        this.depth = depth;
        this.picture = picture;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "BeFriend{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", score=" + score +
                ", depth=" + depth +
                ", picture='" + picture + '\'' +
                '}';
    }
}
