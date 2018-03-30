package com.huazong.app.huazong.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/8/30.
 */
public class PhotoInfoList {
    private String date;
    private List<PhotoInfo> photoInfos;

    public PhotoInfoList() {
    }

    public PhotoInfoList(String date, List<PhotoInfo> photoInfos) {
        this.date = date;
        this.photoInfos = photoInfos;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<PhotoInfo> getPhotoInfos() {
        return photoInfos;
    }

    public void setPhotoInfos(List<PhotoInfo> photoInfos) {
        this.photoInfos = photoInfos;
    }
}
