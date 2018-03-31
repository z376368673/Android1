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
import com.jhobor.fortune.entity.BankBean;

/**
 * Created by 37636 on 2018/3/31.
 */

public class BankListAdapter extends ArrayAdapter<BankBean> {
    Context context;

    public BankListAdapter(@NonNull Context context) {
        super(context, 0);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_bank, parent, false);
            holder = new ViewHolder();
            holder.tv_bank_name = (TextView) convertView.findViewById(R.id.tv_bank_name);
            holder.tv_bank_no = (TextView) convertView.findViewById(R.id.tv_bank_no);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_bank_name.setText(getItem(position).getBankName());
        holder.tv_bank_no.setText(getItem(position).getBankNo());
        return convertView;
    }

    static class ViewHolder {
        TextView tv_bank_name;
        TextView tv_bank_no;
    }

}
