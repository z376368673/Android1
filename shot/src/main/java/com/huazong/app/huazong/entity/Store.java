package com.huazong.app.huazong.entity;

/**
 * Created by Administrator on 2016/7/6.
 */
public class Store implements Comparable<Store> {
    private int id;
    private String name;
    private String address;
    private String picture;
    private String desc;
    private double lng;
    private double lat;
    private double distance;

    public Store() {
    }

    public Store(int id, String name, String address, String picture, String desc, double lng, double lat) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.picture = picture;
        this.desc = desc;
        this.lng = lng;
        this.lat = lat;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    @Override
    public int compareTo(Store store) {
        return this.distance > store.getDistance() ? 1 : -1;
    }

    @Override
    public String toString() {
        return "Store{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", picture='" + picture + '\'' +
                ", desc='" + desc + '\'' +
                ", lng=" + lng +
                ", lat=" + lat +
                ", distance=" + distance +
                '}';
    }
}
