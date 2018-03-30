package com.jhobor.zzb.entity;

/**
 * Created by Administrator on 2017/7/22.
 */

public class EnterpriseStore {
    private int id;
    private String storeIcon;
    private String name;
    private String phoneNum;
    private String company;
    private boolean locked;

    public EnterpriseStore(int id, String storeIcon, String name, String phoneNum, String company, boolean locked) {
        this.id = id;
        this.storeIcon = storeIcon;
        this.name = name;
        this.phoneNum = phoneNum;
        this.company = company;
        this.locked = locked;
    }

    public EnterpriseStore() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStoreIcon() {
        return storeIcon;
    }

    public void setStoreIcon(String storeIcon) {
        this.storeIcon = storeIcon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
