package com.huazong.app.huazong.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.huazong.app.huazong.R;

import java.util.List;
import java.util.Locale;

public class TimeArrayAdapter extends ArrayAdapter<String> {
    private int resource;
    LayoutInflater inflater;
    private List<Double> discountList;

    public TimeArrayAdapter(Context context, int resource, String[] objects, List<Double> discountList) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(context);
        this.resource = resource;
        this.discountList = discountList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(resource, parent, false);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.discount = (TextView) convertView.findViewById(R.id.discount);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String item = getItem(position);
        holder.time.setText(item);
        Double discount = discountList.get(position);
        String dis;
        if (discount<1||discount>=10) {
            dis = "";
        }else {
            dis = String.format(Locale.CHINA, "%.1f折", discount);
        }
        holder.discount.setText(dis);
        return convertView;
    }

    static class ViewHolder {
        TextView time,discount;
    }
}
