package com.jhobor.ddc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhobor.ddc.R;
import com.jhobor.ddc.entity.Msg;

import java.util.List;

/**
 * Created by Administrator on 2017/1/5.
 */

public class MsgBaseAdapter extends BaseAdapter {
    private List<Msg> msgList;
    private Context context;
    private LayoutInflater inflater;

    public MsgBaseAdapter(List<Msg> msgList, Context context) {
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
            convertView = inflater.inflate(R.layout.item_msg, parent, false);
            viewHolder.msgPic = (ImageView) convertView.findViewById(R.id.msgPic);
            viewHolder.title = (TextView) convertView.findViewById(R.id.msgTitle);
            viewHolder.brief = (TextView) convertView.findViewById(R.id.brief);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Msg msg = msgList.get(position);
        //BaseApplication.display(msg.getPicture(),msgPic);
        viewHolder.title.setText(msg.getTitle());
        viewHolder.brief.setText(msg.getContent());
        viewHolder.date.setText(msg.getDate());
        return convertView;
    }

    private static class ViewHolder {
        ImageView msgPic;
        TextView title, brief, date;
    }
}
