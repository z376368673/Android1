package com.jhobor.ddc.entity;

/**
 * Created by Administrator on 2017/1/12.
 * 票券
 */
public class Ticket {
    private int id;
    private float money;//优惠金额
    private float restrictMoney;//限制金额，即消费达到这个金额才可使用优惠券
    private String startDate;//有效期的起始时间
    private String endDate;//有效期的终止时间
    private int storeId;
    private String storeName;//发出该优惠券的店铺，使用优惠券也要在该店铺
    private int state;//状态 0.未使用  1.已使用
    private int count;// 数量，店铺通过此属性查看发行了多少票券，剩余多少

    public Ticket() {
    }

    public Ticket(int id, float money, float restrictMoney, int count) {
        this.id = id;
        this.money = money;
        this.restrictMoney = restrictMoney;
        this.count = count;
    }

    public Ticket(int id, float money, float restrictMoney, String startDate, int state, int count) {
        this.id = id;
        this.money = money;
        this.restrictMoney = restrictMoney;
        this.startDate = startDate;
        this.state = state;
        this.count = count;
    }

    public Ticket(int id, int storeId, float money, float restrictMoney, String startDate, String endDate, String storeName, int state) {
        this.id = id;
        this.storeId = storeId;
        this.money = money;
        this.restrictMoney = restrictMoney;
        this.startDate = startDate;
        this.endDate = endDate;
        this.storeName = storeName;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public float getRestrictMoney() {
        return restrictMoney;
    }

    public void setRestrictMoney(float restrictMoney) {
        this.restrictMoney = restrictMoney;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
