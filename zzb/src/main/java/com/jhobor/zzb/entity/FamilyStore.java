package com.jhobor.zzb.entity;

/**
 * Created by Administrator on 2017/7/21.
 */

public class FamilyStore {
    private int id;
    private String province;
    private String city;
    private String block;
    private String street;
    private String addr;
    private int numberOfSales;

    public FamilyStore(int id, String province, String city, String block, String street, String addr, int numberOfSales) {
        this.id = id;
        this.province = province;
        this.city = city;
        this.block = block;
        this.street = street;
        this.addr = addr;
        this.numberOfSales = numberOfSales;
    }

    public FamilyStore() {
    }

    public int getNumberOfSales() {
        return numberOfSales;
    }

    public void setNumberOfSales(int numberOfSales) {
        this.numberOfSales = numberOfSales;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
}
