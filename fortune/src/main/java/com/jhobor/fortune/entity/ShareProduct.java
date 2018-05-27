package com.jhobor.fortune.entity;

/**
 * Created by zh on 2018/5/19.
 */

public class ShareProduct {


    /**
     * id : 6
     * title : 西瓜
     * createDate : 2018-05-16 14:26:03
     * price : 11
     * imgUrl : http://tz.1yuanpf.com/rentalcar/res/upload/1526451963199.jpg
     * telPhone : 10086
     */

    private int id;
    private String title;
    private String createDate;
    private int price;
    private String imgUrl;
    private String telPhone;

    public int getId() {
        return id;
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

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }
}
