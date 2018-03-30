package com.huazong.app.huazong.entity;

/**
 * Created by Administrator on 2016/7/29.
 */
public class Rank {
    private String picture;
    private String name;
    private int depth;
    private int score;

    public Rank() {
    }

    public Rank(String picture, String name, int depth, int score) {
        this.picture = picture;
        this.name = name;
        this.depth = depth;
        this.score = score;
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

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Rank{" +
                "picture='" + picture + '\'' +
                ", name='" + name + '\'' +
                ", depth=" + depth +
                ", score=" + score +
                '}';
    }
}
