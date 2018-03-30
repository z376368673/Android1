package com.jhobor.ddc.entity;

/**
 * Created by Administrator on 2017/1/16.
 * 需求
 */
public class Needed {
    private int id;
    private String goodsName;
    private int count;
    private String demand;
    private String toAddr;
    private String date;

    public Needed() {
    }

    public Needed(int id, String goodsName, int count, String demand, String toAddr, String date) {
        this.id = id;
        this.goodsName = goodsName;
        this.count = count;
        this.demand = demand;
        this.toAddr = toAddr;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDemand() {
        return demand;
    }

    public void setDemand(String demand) {
        this.demand = demand;
    }

    public String getToAddr() {
        return toAddr;
    }

    public void setToAddr(String toAddr) {
        this.toAddr = toAddr;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
