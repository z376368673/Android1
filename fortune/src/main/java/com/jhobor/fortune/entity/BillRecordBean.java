package com.jhobor.fortune.entity;

/**
 * Created by Administrator on 2017/3/31.
 *
 * 服务积分增长记录
 *
 */

public class BillRecordBean {

    /**
     * id : 11
     * increaseNop : 0
     * headCount : 129
     * myBillAwardNop : 0
     * dateBalanceIntegral : 160.0
     * createDate : 2018/05/15
     * awardIntegral : 0.0
     * userInfoId : 1
     */

    private int id;
    private int increaseNop;    //团队增长人数
    private int headCount;      //团队总人数
    private int myBillAwardNop; //我的服务奖励人数
    private double dateBalanceIntegral; //当日结余积分
    private String createDate;          //日期
    private double awardIntegral;       //奖励积分
    private int userInfoId;             //用户外键

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIncreaseNop() {
        return increaseNop;
    }

    public void setIncreaseNop(int increaseNop) {
        this.increaseNop = increaseNop;
    }

    public int getHeadCount() {
        return headCount;
    }

    public void setHeadCount(int headCount) {
        this.headCount = headCount;
    }

    public int getMyBillAwardNop() {
        return myBillAwardNop;
    }

    public void setMyBillAwardNop(int myBillAwardNop) {
        this.myBillAwardNop = myBillAwardNop;
    }

    public double getDateBalanceIntegral() {
        return dateBalanceIntegral;
    }

    public void setDateBalanceIntegral(double dateBalanceIntegral) {
        this.dateBalanceIntegral = dateBalanceIntegral;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public double getAwardIntegral() {
        return awardIntegral;
    }

    public void setAwardIntegral(double awardIntegral) {
        this.awardIntegral = awardIntegral;
    }

    public int getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(int userInfoId) {
        this.userInfoId = userInfoId;
    }
}
