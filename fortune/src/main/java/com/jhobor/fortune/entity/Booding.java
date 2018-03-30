package com.jhobor.fortune.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/4/28.
 */

public class Booding implements Parcelable {
    public static final Creator<Booding> CREATOR = new Creator<Booding>() {
        @Override
        public Booding createFromParcel(Parcel in) {
            return new Booding(in);
        }

        @Override
        public Booding[] newArray(int size) {
            return new Booding[size];
        }
    };
    private int id;
    private String orderNo;
    private String phone;
    private String participator;
    private float money;
    private String time;
    private int type;// 1-提供帮助 0-得到帮助
    private int state;// 0-排队中 1-等待打款 2-等待确认 3-已完成
    private int countdown;// 倒计时 秒数
    private String parentName;
    private String parentPhone;

    public Booding() {
    }

    public Booding(int id, String orderNo, String participator, float money, String time, int type, int state, int countdown) {
        this.id = id;
        this.orderNo = orderNo;
        this.participator = participator;
        this.money = money;
        this.time = time;
        this.type = type;
        this.state = state;
        this.countdown = countdown;
    }

    public Booding(int id, String phone, String participator, float money, String time, int type, int state) {
        this.id = id;
        this.phone = phone;
        this.participator = participator;
        this.money = money;
        this.time = time;
        this.type = type;
        this.state = state;
    }

    public Booding(int id, String phone, String participator, float money, String time, int type, int state, int countdown, String parentName, String parentPhone) {
        this.id = id;
        this.phone = phone;
        this.participator = participator;
        this.money = money;
        this.time = time;
        this.type = type;
        this.state = state;
        this.countdown = countdown;
        this.parentName = parentName;
        this.parentPhone = parentPhone;
    }

    protected Booding(Parcel in) {
        id = in.readInt();
        orderNo = in.readString();
        phone = in.readString();
        participator = in.readString();
        money = in.readFloat();
        time = in.readString();
        type = in.readInt();
        state = in.readInt();
        countdown = in.readInt();
        parentName = in.readString();
        parentPhone = in.readString();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentPhone() {
        return parentPhone;
    }

    public void setParentPhone(String parentPhone) {
        this.parentPhone = parentPhone;
    }

    public int getCountdown() {
        return countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getParticipator() {
        return participator;
    }

    public void setParticipator(String participator) {
        this.participator = participator;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Booding{" +
                "id=" + id +
                ", orderNo='" + orderNo + '\'' +
                ", phone='" + phone + '\'' +
                ", participator='" + participator + '\'' +
                ", money=" + money +
                ", time='" + time + '\'' +
                ", type=" + type +
                ", state=" + state +
                ", countdown=" + countdown +
                ", parentName='" + parentName + '\'' +
                ", parentPhone='" + parentPhone + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeString(orderNo);
        dest.writeString(phone);
        dest.writeString(participator);
        dest.writeFloat(money);
        dest.writeString(time);
        dest.writeInt(type);
        dest.writeInt(state);
        dest.writeInt(countdown);
        dest.writeString(parentName);
        dest.writeString(parentPhone);
    }

}
