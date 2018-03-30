package com.jhobor.zzb.entity;

/**
 * Created by Administrator on 2017/7/29.
 */

public class RecommendMember {
    private int id;
    private String mobile;
    private int reward;

    public RecommendMember(int id, String mobile, int reward) {
        this.id = id;
        this.mobile = mobile;
        this.reward = reward;
    }

    public RecommendMember() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }
}
