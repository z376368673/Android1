package com.jhobor.ddc.entity;

/**
 * Created by Administrator on 2017/1/5.
 * 我的 模块中的设置项
 */

public class MeItem {
    private int resId;
    private String name;

    public MeItem() {
    }

    public MeItem(int resId, String name) {
        this.resId = resId;
        this.name = name;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
