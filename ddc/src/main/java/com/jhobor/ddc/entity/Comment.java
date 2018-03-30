package com.jhobor.ddc.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/1/7.
 * 评论
 */

public class Comment {
    private int id;
    private int userId;
    private String userName;
    private String userPic;
    private String content;
    private String time;
    private List<String> pictureList;
    private String goodsInfo;

    public Comment() {
    }

    public Comment(int id, int userId, String userName, String userPic, String content, String time, List<String> pictureList) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.userPic = userPic;
        this.content = content;
        this.time = time;
        this.pictureList = pictureList;
    }

    public Comment(int id, int userId, String userName, String userPic, String content, String time, List<String> pictureList, String goodsInfo) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.userPic = userPic;
        this.content = content;
        this.time = time;
        this.pictureList = pictureList;
        this.goodsInfo = goodsInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getPictureList() {
        return pictureList;
    }

    public void setPictureList(List<String> pictureList) {
        this.pictureList = pictureList;
    }

    public String getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(String goodsInfo) {
        this.goodsInfo = goodsInfo;
    }
}
