package com.jhobor.ddc.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/1/14.
 * 地址
 */

public class Addr implements Parcelable {
    public static final Creator<Addr> CREATOR = new Creator<Addr>() {
        @Override
        public Addr createFromParcel(Parcel in) {
            return new Addr(in);
        }

        @Override
        public Addr[] newArray(int size) {
            return new Addr[size];
        }
    };
    private int id;
    private String realName;
    private String phone;
    private String address;
    private int isDefault;

    public Addr() {
    }

    public Addr(int id, String realName, String phone, String address, int isDefault) {
        this.id = id;
        this.realName = realName;
        this.phone = phone;
        this.address = address;
        this.isDefault = isDefault;
    }

    protected Addr(Parcel in) {
        id = in.readInt();
        realName = in.readString();
        phone = in.readString();
        address = in.readString();
        isDefault = in.readInt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(realName);
        dest.writeString(phone);
        dest.writeString(address);
        dest.writeInt(isDefault);
    }

    @Override
    public String toString() {
        return "Addr{" +
                "id=" + id +
                ", realName='" + realName + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", isDefault=" + isDefault +
                '}';
    }
}
