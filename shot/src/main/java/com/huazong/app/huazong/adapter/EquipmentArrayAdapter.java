package com.huazong.app.huazong.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.huazong.app.huazong.R;
import com.huazong.app.huazong.entity.Equipment;

import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2016/7/28.
 */
public class EquipmentArrayAdapter extends ArrayAdapter<Equipment> {

    private int resource;
    LayoutInflater inflater;

    public EquipmentArrayAdapter(Context context, int resource, List<Equipment> objects) {
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
            convertView = inflater.inflate(R.layout.item_equipment, parent, false);
            holder.propNo = (TextView) convertView.findViewById(R.id.propNo);
            holder.propName = (TextView) convertView.findViewById(R.id.propName);
            holder.propNumber = (TextView) convertView.findViewById(R.id.propNumber);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Equipment item = getItem(position);
        holder.propNo.setText(item.getNo());
        holder.propName.setText(item.getName());
        holder.propNumber.setText(String.format(Locale.CHINA,"%d件", item.getNumber()));

        return convertView;
    }

    static class ViewHolder {
        TextView propNo, propName, propNumber;
    }
}
