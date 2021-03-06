package com.jhobor.ddc.entity;

/**
 * Created by Administrator on 2017/2/24.
 * 模块类
 */

public class Module {
    int id;
    private String name;
    private String picture;

    public Module() {
    }

    public Module(int id, String name, String picture) {
        this.id = id;
        this.name = name;
        this.picture = picture;
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

    @Override
    public String toString() {
        return "Module{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                '}';
    }
}
