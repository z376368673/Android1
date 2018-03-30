package com.jhobor.fortune.entity;

/**
 * Created by Administrator on 2017/4/1.
 */

public class Withdraw {
    private int id;
    private String time;
    private float money;
    private int state;
    private String status;
    private String payWay;

    public Withdraw() {
    }

    public Withdraw(int id, String time, float money, int state, String status, String payWay) {
        this.id = id;
        this.time = time;
        this.money = money;
        this.state = state;
        this.status = status;
        this.payWay = payWay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }
}
