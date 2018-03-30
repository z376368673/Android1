package com.jhobor.fortune.entity;

/**
 * Created by Administrator on 2017/3/30.
 */

public class Finance {
    private int id;
    /**
     * 本金
     */
    private float capital;
    /**
     * 利息
     */
    private float interest;
    private float total;
    private String date;
    private int state;
    private String Status;

    public Finance() {
    }

    public Finance(int id, float capital, float interest, float total, String date, int state, String status) {
        this.id = id;
        this.capital = capital;
        this.interest = interest;
        this.total = total;
        this.date = date;
        this.state = state;
        Status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getCapital() {
        return capital;
    }

    public void setCapital(float capital) {
        this.capital = capital;
    }

    public float getInterest() {
        return interest;
    }

    public void setInterest(float interest) {
        this.interest = interest;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    @Override
    public String toString() {
        return "Finance{" +
                "id=" + id +
                ", capital=" + capital +
                ", interest=" + interest +
                ", total=" + total +
                ", date='" + date + '\'' +
                ", state=" + state +
                ", Status='" + Status + '\'' +
                '}';
    }
}
