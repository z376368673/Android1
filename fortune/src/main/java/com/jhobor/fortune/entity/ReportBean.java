package com.jhobor.fortune.entity;

/**
 * Created by 37636 on 2018/3/31.
 */

public class ReportBean {
    private int id;
    private String billIntegral;//"微积分",
    private String billIntegralExplain;//:"说明",
    private String createDate;//:"时间",
    private int tag; //0.增长，1.提现，2转让
    private int userInfoId;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBillIntegral() {
        return billIntegral;
    }

    public void setBillIntegral(String billIntegral) {
        this.billIntegral = billIntegral;
    }

    public String getBillIntegralExplain() {
        return billIntegralExplain;
    }

    public void setBillIntegralExplain(String billIntegralExplain) {
        this.billIntegralExplain = billIntegralExplain;
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
}
