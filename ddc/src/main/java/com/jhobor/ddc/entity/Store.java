package com.jhobor.ddc.entity;

/**
 * Created by Administrator on 2017/1/4.
 * 店铺
 */

public class Store {
    private int id;
    private String name;
    private String picture;
    private float scores;
    private int reputationScores;
    private String type;
    private float lng;
    private float lat;
    private float distance;
    private String addr;
    private float balance;
    private String phone;
    private String operatingTime;//经营时间

    public Store() {
    }

    public Store(int id, String name, String picture, float scores, int reputationScores, float balance) {
        this.id = id;
        this.name = name;
        this.picture = picture;
        this.scores = scores;
        this.reputationScores = reputationScores;
        this.balance = balance;
    }

    public Store(int id, String name, String picture, String addr, float distance) {
        this.id = id;
        this.name = name;
        this.picture = picture;
        this.addr = addr;
        this.distance = distance;
    }

    public Store(int id, String name, String picture, float scores, String type, float lng, float lat, float distance) {
        this.id = id;
        this.name = name;
        this.picture = picture;
        this.scores = scores;
        this.type = type;
        this.lng = lng;
        this.lat = lat;
        this.distance = distance;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public int getReputationScores() {
        return reputationScores;
    }

    public void setReputationScores(int reputationScores) {
        this.reputationScores = reputationScores;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public float getScores() {
        return scores;
    }

    public void setScores(float scores) {
        this.scores = scores;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOperatingTime() {
        return operatingTime;
    }

    public void setOperatingTime(String operatingTime) {
        this.operatingTime = operatingTime;
    }
}
