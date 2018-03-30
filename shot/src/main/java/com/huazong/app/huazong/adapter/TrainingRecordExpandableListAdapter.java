package com.huazong.app.huazong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.huazong.app.huazong.R;
import com.huazong.app.huazong.entity.TrainingRecord;
import com.huazong.app.huazong.entity.TrainingRecordGroup;

import java.util.List;

/**
 * Created by Administrator on 2016/7/14.
 */
public class TrainingRecordExpandableListAdapter extends BaseExpandableListAdapter {
    LayoutInflater inflater;
    private List<TrainingRecordGroup> trainingRecordGroups;

    public TrainingRecordExpandableListAdapter(Context context, List<TrainingRecordGroup> trainingRecordGroups) {
        this.trainingRecordGroups = trainingRecordGroups;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return trainingRecordGroups.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return trainingRecordGroups.get(i).getRecordList().size();
    }

    @Override
    public Object getGroup(int i) {
        return trainingRecordGroups.get(i).getGroup();
    }

    @Override
    public Object getChild(int i, int i1) {
        return trainingRecordGroups.get(i).getRecordList().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return 1;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        DateViewHolder dateViewHolder;
        if (view == null) {
            dateViewHolder = new DateViewHolder();
            view = inflater.inflate(R.layout.item_training_date, viewGroup, false);
            dateViewHolder.trainingDate = (TextView) view.findViewById(R.id.trainingDate);
            view.setTag(dateViewHolder);
        } else {
            dateViewHolder = (DateViewHolder) view.getTag();
        }
        System.out.println(trainingRecordGroups.get(i).getGroup());
        dateViewHolder.trainingDate.setText(trainingRecordGroups.get(i).getGroup());
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        DetailViewHolder detailViewHolder;
        if (view == null) {
            detailViewHolder = new DetailViewHolder();
            view = inflater.inflate(R.layout.item_training_detail, viewGroup, false);
            detailViewHolder.time = (TextView) view.findViewById(R.id.time);
            detailViewHolder.depth = (TextView) view.findViewById(R.id.depth);
            detailViewHolder.scores = (TextView) view.findViewById(R.id.scores);
            view.setTag(detailViewHolder);
        } else {
            detailViewHolder = (DetailViewHolder) view.getTag();
        }
        TrainingRecord trainingRecord = (TrainingRecord) getChild(i, i1);
        detailViewHolder.time.setText(trainingRecord.getTime());
        String depth = "第" + trainingRecord.getDepth() + "关";
        detailViewHolder.depth.setText(depth);
        String scores = trainingRecord.getScores() + "分";
        detailViewHolder.scores.setText(scores);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    static class DetailViewHolder {
        TextView time, depth, scores;
    }

    static class DateViewHolder {
        TextView trainingDate;
    }

}
