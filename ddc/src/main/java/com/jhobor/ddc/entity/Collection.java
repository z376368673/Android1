package com.jhobor.ddc.entity;

/**
 * Created by Administrator on 2017/1/14.
 * 收藏
 */

public class Collection {
    private int id;
    private int storeId;
    private String storeName;
    private int userId;
    private float scores;
    private String date;
    private String storePic;

    public Collection() {
    }

    public Collection(int id, int storeId, String storeName, int userId, float scores, String date, String storePic) {
        this.id = id;
        this.storeId = storeId;
        this.storeName = storeName;
        this.userId = userId;
        this.scores = scores;
        this.date = date;
        this.storePic = storePic;
    }

    public String getStorePic() {
        return storePic;
    }

    public void setStorePic(String storePic) {
        this.storePic = storePic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public float getScores() {
        return scores;
    }

    public void setScores(float scores) {
        this.scores = scores;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
