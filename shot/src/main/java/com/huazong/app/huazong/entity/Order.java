package com.huazong.app.huazong.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.baidu.mapapi.model.LatLng;

public class Order implements Parcelable {

    private int id;
    private String name;
    private boolean used;
    private String date;
    private String time;
    private LatLng latLng;
    private int byway;
    private String picture;
    private int type;
    private int storeId;

    public Order() {
    }

    public Order(int id, String name, boolean used, String date, String time, LatLng latLng, int byway, String picture, int type, int storeId) {
        this.id = id;
        this.name = name;
        this.used = used;
        this.date = date;
        this.time = time;
        this.latLng = latLng;
        this.byway = byway;
        this.picture = picture;
        this.type = type;
        this.storeId = storeId;
    }

    protected Order(Parcel in) {
        id = in.readInt();
        name = in.readString();
        used = in.readByte() != 0;
        date = in.readString();
        time = in.readString();
        latLng = in.readParcelable(LatLng.class.getClassLoader());
        byway = in.readInt();
        picture = in.readString();
        type = in.readInt();
        storeId = in.readInt();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
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

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public int getByway() {
        return byway;
    }

    public void setByway(int byway) {
        this.byway = byway;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeByte((byte) (used ? 1 : 0));
        dest.writeString(date);
        dest.writeString(time);
        dest.writeParcelable(latLng, flags);
        dest.writeInt(byway);
        dest.writeString(picture);
        dest.writeInt(type);
        dest.writeInt(storeId);
    }
}
