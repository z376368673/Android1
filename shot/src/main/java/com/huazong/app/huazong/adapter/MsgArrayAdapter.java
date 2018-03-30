package com.huazong.app.huazong.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.huazong.app.huazong.R;
import com.huazong.app.huazong.entity.PushMessage;

import java.util.List;

/**
 * Created by Administrator on 2017/5/9.
 */

public class MsgArrayAdapter  extends ArrayAdapter<PushMessage> {
    private int resource;
    LayoutInflater inflater;

    public MsgArrayAdapter(Context context, int resource, List<PushMessage> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(resource, parent, false);
            holder.msgTitle = (TextView) convertView.findViewById(R.id.msgTitle);
            holder.msgContent = (TextView) convertView.findViewById(R.id.msgContent);
            holder.msgTime = (TextView) convertView.findViewById(R.id.msgTime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PushMessage item = getItem(position);
        holder.msgTitle.setText(item.getTitle());
        holder.msgContent.setText(item.getContent());
        holder.msgTime.setText(item.getDate());

        return convertView;
    }

    class ViewHolder {
        TextView msgTitle, msgContent, msgTime;
    }
}
