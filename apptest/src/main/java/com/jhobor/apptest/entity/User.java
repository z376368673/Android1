package com.jhobor.apptest.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Arrays;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/4/8.
 */

@Entity
public class User {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String name;
    private byte[] img;


    @Generated(hash = 291859051)
    public User(Long id, @NotNull String name, byte[] img) {
        this.id = id;
        this.name = name;
        this.img = img;
    }

    @Generated(hash = 586692638)
    public User() {
    }


    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", img=" + Arrays.toString(img) +
                '}';
    }
}
