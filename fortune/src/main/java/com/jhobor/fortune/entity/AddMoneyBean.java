package com.jhobor.fortune.entity;

/**
 * Created by 37636 on 2018/3/31.
 */

public class AddMoneyBean {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getAmountExplain() {
        return amountExplain;
    }

    public void setAmountExplain(String amountExplain) {
        this.amountExplain = amountExplain;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(int userInfoId) {
        this.userInfoId = userInfoId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private int id;
    private String money;//"积分",
    private String amountExplain;//:"说明",
    private String createDate;//:"时间",
    private int tag; //0.增长，1.提现，2转让
    private int userInfoId;
    private int status;

}
