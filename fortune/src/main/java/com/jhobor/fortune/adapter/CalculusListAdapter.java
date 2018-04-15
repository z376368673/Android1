package com.jhobor.fortune.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jhobor.fortune.R;
import com.jhobor.fortune.entity.CalculusBean;
import com.jhobor.fortune.entity.RecordBean;

/**
 * Created by 37636 on 2018/3/31.
 */

public class CalculusListAdapter extends ArrayAdapter<CalculusBean> {
    Context context;

    public CalculusListAdapter(@NonNull Context context) {
        super(context, 0);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_record, parent, false);
            holder = new ViewHolder();
            holder.tv_integral = (TextView) convertView.findViewById(R.id.tv_integral);
            holder.tv_1 = (TextView) convertView.findViewById(R.id.tv_1);
            holder.tv_2 = (TextView) convertView.findViewById(R.id.tv_2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_integral.setText(getItem(position).getCalculus());
        holder.tv_1.setText(getItem(position).getCalculusExplain());
        holder.tv_2.setText(getItem(position).getCreateDate());
        return convertView;
    }

    static class ViewHolder {
        TextView tv_integral;
        TextView tv_1;
        TextView tv_2;
    }

}
