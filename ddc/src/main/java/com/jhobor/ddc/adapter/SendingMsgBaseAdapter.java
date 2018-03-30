package com.jhobor.ddc.adapter;

import android.content.Context;
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

public class SendingMsgBaseAdapter extends BaseAdapter {
    private List<Msg> msgList;
    private Context context;
    private LayoutInflater inflater;

    public SendingMsgBaseAdapter(List<Msg> msgList, Context context) {
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
            convertView = inflater.inflate(R.layout.item_sending_msg, parent, false);
            viewHolder.state = (TextView) convertView.findViewById(R.id.state);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.content = (TextView) convertView.findViewById(R.id.content);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Msg msg = msgList.get(position);
        //BaseApplication.display(msg.getPicture(),msgPic);
        viewHolder.state.setText(msg.getTitle());
        viewHolder.time.setText(msg.getDate());
        viewHolder.content.setText(msg.getContent());
        return convertView;
    }

    private static class ViewHolder {
        TextView state, time, content;
    }
}
