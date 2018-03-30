package com.jhobor.ddc.entity;

/**
 * Created by Administrator on 2017/2/10.
 * 用户信息表
 */

public class UserInfo {
    private int id;
    private String uuid;
    private String name;
    private String account;
    private String gravatar;
    private float balance;

    public UserInfo() {
    }

    public UserInfo(int id, String name, String account, String gravatar, float balance) {
        this.id = id;
        this.name = name;
        this.account = account;
        this.gravatar = gravatar;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getGravatar() {
        return gravatar;
    }

    public void setGravatar(String gravatar) {
        this.gravatar = gravatar;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", account='" + account + '\'' +
                ", gravatar='" + gravatar + '\'' +
                ", balance=" + balance +
                '}';
    }
}
