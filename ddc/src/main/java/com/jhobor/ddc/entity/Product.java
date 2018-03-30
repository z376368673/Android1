package com.jhobor.ddc.entity;

/**
 * Created by Administrator on 2017/1/9.
 * 物品，农产品展示
 */

public class Product {
    private int id;
    private int storeId;
    private String storeName;
    private String goodsName;
    private float distance;
    private String storeAddr;
    private String goodsPicture;
    private String storePic;

    public Product() {
    }

    public Product(int id, int storeId, String storeName, float distance, String storeAddr, String storePic, String goodsPicture) {
        this.id = id;
        this.storeId = storeId;
        this.storeName = storeName;
        this.distance = distance;
        this.storeAddr = storeAddr;
        this.storePic = storePic;
        this.goodsPicture = goodsPicture;
    }

    public String getGoodsPicture() {
        return goodsPicture;
    }

    public void setGoodsPicture(String goodsPicture) {
        this.goodsPicture = goodsPicture;
    }

    public String getStorePic() {
        return storePic;
    }

    public void setStorePic(String storePic) {
        this.storePic = storePic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
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

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getStoreAddr() {
        return storeAddr;
    }

    public void setStoreAddr(String storeAddr) {
        this.storeAddr = storeAddr;
    }
}
