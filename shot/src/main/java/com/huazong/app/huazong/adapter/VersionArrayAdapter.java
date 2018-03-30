package com.huazong.app.huazong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.huazong.app.huazong.R;
import com.huazong.app.huazong.entity.VerInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class VersionArrayAdapter extends ArrayAdapter<VerInfo> {
    LayoutInflater inflater;
    int resource;

    public VersionArrayAdapter(Context context, int resource, List<VerInfo> objects) {
        super(context, resource, objects);
        this.inflater = LayoutInflater.from(context);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(resource, parent, false);
            holder.versionMsg = (TextView) convertView.findViewById(R.id.versionMsg);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        VerInfo item = getItem(position);
        holder.versionMsg.setText(item.getVerInfo());
        holder.date.setText(item.getDate());
        holder.time.setText(item.getTime());

        return convertView;
    }

    static class ViewHolder {
        TextView versionMsg, date, time;
    }
}
