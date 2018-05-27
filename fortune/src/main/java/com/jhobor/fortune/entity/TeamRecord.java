package com.jhobor.fortune.entity;

/**
 * Created by zh on 2018/5/17.
 */

public class TeamRecord {

    private int id;
    private String mobile;      //账号
    private int money;
    private String createDate;
    private int capital;        //投资本金
    private String billMobile;  //分属服务中心

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public int getCapital() {
        return capital;
    }

    public void setCapital(int capital) {
        this.capital = capital;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBillMobile() {
        return billMobile;
    }

    public void setBillMobile(String billMobile) {
        this.billMobile = billMobile;
    }
}
