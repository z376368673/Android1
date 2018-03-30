package com.jhobor.fortune.entity;

/**
 * Created by Administrator on 2017/5/2.
 */

public class Interest {
    private int id;
    private float money;
    private String date;
    private String rate;



    private String interestMoney;

    public Interest() {
    }

    public Interest(int id, float money, String date, String rate,String interestMoney) {
        this.id = id;
        this.money = money;
        this.date = date;
        this.rate = rate;
        this.interestMoney = interestMoney;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getInterestMoney() {
        return interestMoney;
    }

    public void setInterestMoney(String interestMoney) {
        this.interestMoney = interestMoney;
    }
}
