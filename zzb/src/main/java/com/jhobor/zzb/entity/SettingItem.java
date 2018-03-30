package com.jhobor.zzb.entity;

/**
 * Created by Administrator on 2017/7/31.
 */

public class SettingItem {
    private String name;
    private String info;

    public SettingItem(String name, String info) {
        this.name = name;
        this.info = info;
    }

    public SettingItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
