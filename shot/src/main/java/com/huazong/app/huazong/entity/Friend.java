package com.huazong.app.huazong.entity;

import android.util.Log;

/**
 * Created by Administrator on 2016/7/22.
 */
public class Friend {
    private int userId;
    private String openId;
    private String picture;
    private String name;
    private boolean checked;
    private int score;
    private int depth;

    public Friend() {
    }

    public Friend(int userId, String openId, String picture, String name, boolean checked) {
        this.userId = userId;
        this.openId = openId;
        this.picture = picture;
        this.name = name;
        this.checked = checked;
    }

    public Friend(int userId, String picture, String name, int score, int depth, boolean checked) {
        this.userId = userId;
        this.picture = picture;
        this.name = name;
        this.score = score;
        this.depth = depth;
        this.checked = checked;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
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

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "userId=" + userId +
                ", openId=" + openId +
                ", picture='" + picture + '\'' +
                ", name='" + name + '\'' +
                ", checked=" + checked +
                '}';
    }

    @Override
    public int hashCode() {
        return userId;
    }

    @Override
    public boolean equals(Object obj) {
        boolean b = false;
        if (obj != null && obj instanceof Friend){
            b = this.hashCode() == obj.hashCode();
        }
        return b;
    }
}
