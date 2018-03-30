package com.jhobor.ddc.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/1/6.
 * 产品
 */
public class Goods implements Parcelable {
    public static final Creator<Goods> CREATOR = new Creator<Goods>() {
        @Override
        public Goods createFromParcel(Parcel in) {
            return new Goods(in);
        }

        @Override
        public Goods[] newArray(int size) {
            return new Goods[size];
        }
    };
    private int id;
    private String name;
    private String picture;
    private float price;
    private float wholesalePrice;//批发价
    private int wholesaleVolume;//批发量，批发达到这个量才能享有批发价
    private int salesVolume;//销量
    private int stock;//库存
    private int count;//用户选择的数量

    public Goods() {
    }

    public Goods(int id, String name, String picture, float price, int salesVolume, int stock) {
        this.id = id;
        this.name = name;
        this.picture = picture;
        this.price = price;
        this.salesVolume = salesVolume;
        this.stock = stock;
    }

    public Goods(int id, String name, String picture, float price, int salesVolume) {
        this.id = id;
        this.name = name;
        this.picture = picture;
        this.price = price;
        this.salesVolume = salesVolume;
    }

    protected Goods(Parcel in) {
        id = in.readInt();
        name = in.readString();
        picture = in.readString();
        price = in.readFloat();
        wholesalePrice = in.readFloat();
        wholesaleVolume = in.readInt();
        salesVolume = in.readInt();
        stock = in.readInt();
        count = in.readInt();
    }

    public int getWholesaleVolume() {
        return wholesaleVolume;
    }

    public void setWholesaleVolume(int wholesaleVolume) {
        this.wholesaleVolume = wholesaleVolume;
    }

    public float getWholesalePrice() {
        return wholesalePrice;
    }

    public void setWholesalePrice(float wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(int salesVolume) {
        this.salesVolume = salesVolume;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(picture);
        dest.writeFloat(price);
        dest.writeFloat(wholesalePrice);
        dest.writeInt(wholesaleVolume);
        dest.writeInt(salesVolume);
        dest.writeInt(stock);
        dest.writeInt(count);
    }
}
