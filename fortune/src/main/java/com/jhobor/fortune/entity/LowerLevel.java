package com.jhobor.fortune.entity;

/**
 * Created by Administrator on 2017/3/31.
 */

public class LowerLevel {
    private int id;
    private String name;
    private String mobile;
    private String joinTime;
    private float capital;
    private float profit;

    public LowerLevel() {
    }

    public LowerLevel(String mobile, float capital) {
        this.mobile = mobile;
        this.capital = capital;
    }

    public LowerLevel(int id, String name, String joinTime, float capital, float profit) {
        this.id = id;
        this.name = name;
        this.joinTime = joinTime;
        this.capital = capital;
        this.profit = profit;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(String joinTime) {
        this.joinTime = joinTime;
    }

    public float getCapital() {
        return capital;
    }

    public void setCapital(float capital) {
        this.capital = capital;
    }

    public float getProfit() {
        return profit;
    }

    public void setProfit(float profit) {
        this.profit = profit;
    }

    @Override
    public String toString() {
        return "LowerLevel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", joinTime='" + joinTime + '\'' +
                ", capital=" + capital +
                ", profit=" + profit +
                '}';
    }
}
