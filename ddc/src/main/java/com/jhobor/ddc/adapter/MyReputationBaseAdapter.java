package com.jhobor.ddc.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jhobor.ddc.R;
import com.jhobor.ddc.entity.Msg;

import java.util.List;

/**
 * Created by Administrator on 2017/1/5.
 */

public class MyReputationBaseAdapter extends BaseAdapter {
    private List<Msg> msgList;
    private Context context;
    private LayoutInflater inflater;

    public MyReputationBaseAdapter(List<Msg> msgList, Context context) {
        this.msgList = msgList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return msgList.size();
    }

    @Override
    public Object getItem(int position) {
        return msgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_my_reputation, parent, false);
            viewHolder.content = (TextView) convertView.findViewById(R.id.content);
            viewHolder.scores = (TextView) convertView.findViewById(R.id.scores);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Msg msg = msgList.get(position);
        if (msg.getTitle().charAt(0) == '-') {
            viewHolder.scores.setTextColor(ContextCompat.getColor(context, R.color.green));
        } else {
            viewHolder.scores.setTextColor(ContextCompat.getColor(context, R.color.redTheme));
        }
        viewHolder.scores.setText(msg.getTitle());
        viewHolder.content.setText(msg.getContent());
        viewHolder.date.setText(msg.getDate());
        return convertView;
    }

    private static class ViewHolder {
        TextView content, scores, date;
    }
}
