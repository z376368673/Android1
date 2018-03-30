package com.huazong.app.huazong.entity;

/**
 * Created by Administrator on 2016/7/28.
 */
public class Equipment {
    private String No;
    private String name;
    private int number;

    public Equipment() {
    }

    public Equipment(String no, String name, int number) {
        No = no;
        this.name = name;
        this.number = number;
    }

    public String getNo() {
        return No;
    }

    public void setNo(String no) {
        No = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Equipment{" +
                "No='" + No + '\'' +
                ", name='" + name + '\'' +
                ", number=" + number +
                '}';
    }
}
