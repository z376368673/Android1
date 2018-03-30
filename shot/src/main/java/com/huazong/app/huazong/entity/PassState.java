package com.huazong.app.huazong.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/7/11.
 */
public class PassState implements Parcelable {
    public static final Parcelable.Creator<PassState> CREATOR = new Parcelable.Creator<PassState>() {

        @Override
        public PassState createFromParcel(Parcel parcel) {
            return new PassState(parcel);
        }

        @Override
        public PassState[] newArray(int i) {
            return new PassState[i];
        }
    };
    private String time;
    private int pass;
    private int state; // 0-未选择，1-已选择，2-已售出
    private String openid;

    public PassState(String time, int pass, int state, String openid) {
        this.time = time;
        this.pass = pass;
        this.state = state;
        this.openid = openid;
    }

    public PassState(String time, int pass, String openid) {
        this.time = time;
        this.pass = pass;
        this.openid = openid;
    }

    public PassState() {
    }

    private PassState(Parcel in) {
        time = in.readString();
        pass = in.readInt();
        state = in.readInt();
        openid = in.readString();
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPass() {
        return pass;
    }

    public void setPass(int pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "PassState{" +
                "time='" + time + '\'' +
                ", pass=" + pass +
                ", state=" + state +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(time);
        parcel.writeInt(pass);
        parcel.writeInt(state);
        parcel.writeString(openid);
    }
}
