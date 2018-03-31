package com.jhobor.fortune.entity;

/**
 * Created by 37636 on 2018/3/31.
 */

public class BankBean {

    private int id;  //1,
    private int userInfoId;  //1
    private String name;  //"华众",
    private String bankNo;  //"123456789123456789",
    private String bankName;  //"中国银行",


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(int userInfoId) {
        this.userInfoId = userInfoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
