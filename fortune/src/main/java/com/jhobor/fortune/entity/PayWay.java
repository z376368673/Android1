package com.jhobor.fortune.entity;

/**
 * Created by Administrator on 2017/4/6.
 */

public class PayWay {
    private int id;
    private String bankName;
    private String userName;
    private String account;
    private String type;

    public PayWay() {
    }

    public PayWay(int id, String bankName, String userName, String account, String type) {
        this.id = id;
        this.bankName = bankName;
        this.userName = userName;
        this.account = account;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
