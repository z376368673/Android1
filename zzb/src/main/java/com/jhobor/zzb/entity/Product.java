package com.jhobor.zzb.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/7/14.
 */

public class Product {
    private int id;
    private String title;
    private List<String> images;// 展示图
    private String dateTime;
    private int clicks;// 点击量
    private int place;// 地点
    private int price;// 多少钱每平方米
    private String type;// 类型
    private int storeId;// 店铺id

    public Product() {
    }

    public Product(int id, String title, List<String> images, String dateTime, int clicks, int place) {
        this.id = id;
        this.title = title;
        this.images = images;
        this.dateTime = dateTime;
        this.clicks = clicks;
        this.place = place;
    }

    public Product(int storeId,int price, String type, List<String> images) {
        this.storeId = storeId;
        this.price = price;
        this.type = type;
        this.images = images;
    }
    public Product(int storeId,String title, String type, List<String> images) {
        this.storeId = storeId;
        this.title = title;
        this.type = type;
        this.images = images;
    }

    public int getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }
}
