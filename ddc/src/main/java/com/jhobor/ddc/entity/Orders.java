package com.jhobor.ddc.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/1/4.
 * 商品订单类
 */

public class Orders implements Parcelable {
    public static final Creator<Orders> CREATOR = new Creator<Orders>() {
        @Override
        public Orders createFromParcel(Parcel in) {
            return new Orders(in);
        }

        @Override
        public Orders[] newArray(int size) {
            return new Orders[size];
        }
    };
    private long id;
    private int storeId;
    private String storeName;
    private int goodsId;
    private String goodsName;
    /**
     * 0.待付款 1待发货 2待收货 3.已完成 4.已评论
     */
    private int state;
    private String goodsPic;
    private String ordersTime;
    private float money;
    private String userName;
    private int userId;
    private int count;
    private float price;
    private String ordersNo;
    /**
     * 0.商家安排人员发货  1.使用快递/物流发货
     */
    private int type;
    private boolean isChecked;

    public Orders() {
    }

    public Orders(long id, String storeName, String goodsName, String goodsPic, String ordersTime, float money, int count, float price) {
        this.id = id;
        this.storeName = storeName;
        this.goodsName = goodsName;
        this.goodsPic = goodsPic;
        this.ordersTime = ordersTime;
        this.money = money;
        this.count = count;
        this.price = price;
    }

    public Orders(long id, String storeName, String goodsName, int state, String goodsPic, String ordersTime, float money, int type) {
        this.id = id;
        this.storeName = storeName;
        this.goodsName = goodsName;
        this.state = state;
        this.goodsPic = goodsPic;
        this.ordersTime = ordersTime;
        this.money = money;
        this.type = type;
    }

    public Orders(long id, String goodsName, int state, String goodsPic, String ordersTime, String userName, int count, float price, String ordersNo, int type) {
        this.id = id;
        this.goodsName = goodsName;
        this.state = state;
        this.goodsPic = goodsPic;
        this.ordersTime = ordersTime;
        this.userName = userName;
        this.count = count;
        this.price = price;
        this.ordersNo = ordersNo;
        this.type = type;
    }

    protected Orders(Parcel in) {
        id = in.readLong();
        storeId = in.readInt();
        storeName = in.readString();
        goodsId = in.readInt();
        goodsName = in.readString();
        state = in.readInt();
        goodsPic = in.readString();
        ordersTime = in.readString();
        money = in.readFloat();
        userName = in.readString();
        userId = in.readInt();
        count = in.readInt();
        price = in.readFloat();
        ordersNo = in.readString();
        type = in.readInt();
        isChecked = in.readByte() != 0;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getGoodsPic() {
        return goodsPic;
    }

    public void setGoodsPic(String goodsPic) {
        this.goodsPic = goodsPic;
    }

    public String getOrdersTime() {
        return ordersTime;
    }

    public void setOrdersTime(String ordersTime) {
        this.ordersTime = ordersTime;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getOrdersNo() {
        return ordersNo;
    }

    public void setOrdersNo(String ordersNo) {
        this.ordersNo = ordersNo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(id);
        dest.writeInt(storeId);
        dest.writeString(storeName);
        dest.writeInt(goodsId);
        dest.writeString(goodsName);
        dest.writeInt(state);
        dest.writeString(goodsPic);
        dest.writeString(ordersTime);
        dest.writeFloat(money);
        dest.writeString(userName);
        dest.writeInt(userId);
        dest.writeInt(count);
        dest.writeFloat(price);
        dest.writeString(ordersNo);
        dest.writeInt(type);
        dest.writeByte((byte) (isChecked ? 1 : 0));
    }


}
