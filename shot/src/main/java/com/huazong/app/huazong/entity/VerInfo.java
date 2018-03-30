package com.huazong.app.huazong.entity;

/**
 * Created by Administrator on 2016/7/28.
 */
public class VerInfo {
    private String verInfo;
    private String date;
    private String time;

    public VerInfo() {
    }

    public VerInfo(String verInfo, String date, String time) {
        this.verInfo = verInfo;
        this.date = date;
        this.time = time;
    }

    public String getVerInfo() {
        return verInfo;
    }

    public void setVerInfo(String verInfo) {
        this.verInfo = verInfo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "VerInfo{" +
                "verInfo='" + verInfo + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
