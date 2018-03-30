package com.huazong.app.huazong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huazong.app.huazong.R;

public class MeArrayAdapter extends ArrayAdapter<String> {
    LayoutInflater inflater;
    int resource;
    int[] settingItemIcons;

    public MeArrayAdapter(Context context, int resource, String[] objects, int[] settingItemIcons) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(context);
        this.resource = resource;
        this.settingItemIcons = settingItemIcons;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(resource, parent, false);
            holder.itemIcon = (ImageView) convertView.findViewById(R.id.itemIcon);
            holder.itemSubject = (TextView) convertView.findViewById(R.id.itemSubject);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String item = getItem(position);
        holder.itemIcon.setImageResource(settingItemIcons[position]);
        holder.itemSubject.setText(item);
        return convertView;
    }

    static class ViewHolder {
        ImageView itemIcon;
        TextView itemSubject;
    }
}
