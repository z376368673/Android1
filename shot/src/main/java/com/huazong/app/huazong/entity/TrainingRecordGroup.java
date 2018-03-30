package com.huazong.app.huazong.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/7/27.
 */
public class TrainingRecordGroup {
    private String group;
    private List<TrainingRecord> recordList;

    public TrainingRecordGroup() {
    }

    public TrainingRecordGroup(String group, List<TrainingRecord> recordList) {
        this.group = group;
        this.recordList = recordList;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<TrainingRecord> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<TrainingRecord> recordList) {
        this.recordList = recordList;
    }

}
