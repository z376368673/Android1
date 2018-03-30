package com.jhobor.ddc.entity;

/**
 * Created by Administrator on 2017/1/9.
 * 物流配送类
 * 待解决：一个配送单里包含多个产品
 */

public class Delivery {
    private int id;
    private int storeId;
    private String storeName;
    private String storePhone;
    private String task;
    private float money;
    private String fromAddr;
    private String toAddr;
    private int reputationScores;
    private String minTime;
    private String employerPhone;

    public Delivery() {
    }

    public Delivery(int id, int storeId, String storeName, String storePhone, String task, float money, String fromAddr, String toAddr, int reputationScores, String minTime) {
        this.id = id;
        this.storeId = storeId;
        this.storeName = storeName;
        this.storePhone = storePhone;
        this.task = task;
        this.money = money;
        this.fromAddr = fromAddr;
        this.toAddr = toAddr;
        this.reputationScores = reputationScores;
        this.minTime = minTime;
    }

    public Delivery(int id, int storeId, String storeName, String storePhone, String task, float money, String fromAddr, String toAddr, int reputationScores, String minTime, String employerPhone) {
        this.id = id;
        this.storeId = storeId;
        this.storeName = storeName;
        this.storePhone = storePhone;
        this.task = task;
        this.money = money;
        this.fromAddr = fromAddr;
        this.toAddr = toAddr;
        this.reputationScores = reputationScores;
        this.minTime = minTime;
        this.employerPhone = employerPhone;
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

    public String getStorePhone() {
        return storePhone;
    }

    public void setStorePhone(String storePhone) {
        this.storePhone = storePhone;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getFromAddr() {
        return fromAddr;
    }

    public void setFromAddr(String fromAddr) {
        this.fromAddr = fromAddr;
    }

    public String getToAddr() {
        return toAddr;
    }

    public void setToAddr(String toAddr) {
        this.toAddr = toAddr;
    }

    public int getReputationScores() {
        return reputationScores;
    }

    public void setReputationScores(int reputationScores) {
        this.reputationScores = reputationScores;
    }

    public String getMinTime() {
        return minTime;
    }

    public void setMinTime(String minTime) {
        this.minTime = minTime;
    }

    public String getEmployerPhone() {
        return employerPhone;
    }

    public void setEmployerPhone(String employerPhone) {
        this.employerPhone = employerPhone;
    }
}
