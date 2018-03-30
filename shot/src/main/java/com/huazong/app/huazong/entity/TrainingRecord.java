package com.huazong.app.huazong.entity;

/**
 * Created by Administrator on 2016/7/14.
 */
public class TrainingRecord {
    private String date; // 日期
    private String time; // 时间
    private int depth; // 关数
    private int scores; // 分数

    public TrainingRecord() {
    }

    public TrainingRecord(String date, String time, int depth, int scores) {
        this.date = date;
        this.time = time;
        this.depth = depth;
        this.scores = scores;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getScores() {
        return scores;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "TrainingRecord{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", depth=" + depth +
                ", scores=" + scores +
                '}';
    }
}
