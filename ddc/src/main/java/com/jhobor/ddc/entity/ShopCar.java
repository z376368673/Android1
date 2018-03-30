package com.jhobor.ddc.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by Administrator on 2017/3/7.
 * 购物车，这个类和sqlite建立关系
 */

@Entity
public class ShopCar implements Parcelable {
    @Id(autoincrement = true)
    private Long id;
    @Unique
    private int goodsId;
    @NotNull
    private String goodsName;
    private float goodsPrice;
    private String goodsPic;
    private int count;
    @NotNull
    private int storeId;
    @NotNull
    private String storeName;
    private String time;

    @Generated(hash = 221043965)
    public ShopCar(Long id, int goodsId, @NotNull String goodsName, float goodsPrice, String goodsPic, int count, int storeId,
                   @NotNull String storeName, String time) {
        this.id = id;
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.goodsPrice = goodsPrice;
        this.goodsPic = goodsPic;
        this.count = count;
        this.storeId = storeId;
        this.storeName = storeName;
        this.time = time;
    }

    @Generated(hash = 1637372148)
    public ShopCar() {
    }

    public ShopCar(int goodsId, String goodsName, float goodsPrice, String goodsPic, int count, int storeId, String storeName, String time) {
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.goodsPrice = goodsPrice;
        this.goodsPic = goodsPic;
        this.count = count;
        this.storeId = storeId;
        this.storeName = storeName;
        this.time = time;
    }

    protected ShopCar(Parcel in) {
        goodsId = in.readInt();
        goodsName = in.readString();
        goodsPrice = in.readFloat();
        goodsPic = in.readString();
        count = in.readInt();
        storeId = in.readInt();
        storeName = in.readString();
        time = in.readString();
    }

    public static final Creator<ShopCar> CREATOR = new Creator<ShopCar>() {
        @Override
        public ShopCar createFromParcel(Parcel in) {
            return new ShopCar(in);
        }

        @Override
        public ShopCar[] newArray(int size) {
            return new ShopCar[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getGoodsId() {
        return this.goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return this.goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public float getGoodsPrice() {
        return this.goodsPrice;
    }

    public void setGoodsPrice(float goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ShopCar{" +
                "id=" + id +
                ", goodsId=" + goodsId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsPrice=" + goodsPrice +
                ", goodsPic='" + goodsPic + '\'' +
                ", count=" + count +
                ", time='" + time + '\'' +
                ", storeName='" + storeName + '\'' +
                '}';
    }

    public String getGoodsPic() {
        return this.goodsPic;
    }

    public void setGoodsPic(String goodsPic) {
        this.goodsPic = goodsPic;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStoreName() {
        return this.storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getStoreId() {
        return this.storeId;
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
        dest.writeInt(goodsId);
        dest.writeString(goodsName);
        dest.writeFloat(goodsPrice);
        dest.writeString(goodsPic);
        dest.writeInt(count);
        dest.writeInt(storeId);
        dest.writeString(storeName);
        dest.writeString(time);
    }
}
