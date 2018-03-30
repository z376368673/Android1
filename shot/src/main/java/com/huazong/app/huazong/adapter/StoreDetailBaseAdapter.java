package com.huazong.app.huazong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.huazong.app.huazong.R;
import com.huazong.app.huazong.entity.PassState;

import java.util.List;

/**
 * Created by Administrator on 2016/7/11.
 */
public class StoreDetailBaseAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    List<PassState> allPassStates;

    public StoreDetailBaseAdapter(Context context, List<PassState> allPassStates) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.allPassStates = allPassStates;
    }

    public void setAllPassStates(List<PassState> allPassStates) {
        this.allPassStates = allPassStates;
    }

    @Override
    public int getCount() {
        return allPassStates.size();
    }

    @Override
    public Object getItem(int i) {
        return allPassStates.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_store_detail_pass, parent, false);
            holder.pass = (ImageView) convertView.findViewById(R.id.pass);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PassState passState = allPassStates.get(position);
        if (passState.getState() == 2) {
            holder.pass.setImageResource(R.mipmap.seat_sold);
        } else if (passState.getState() == 1) {
            holder.pass.setImageResource(R.mipmap.seat_chosen);
        } else {
            holder.pass.setImageResource(R.mipmap.seat_unchosen);
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView pass;
    }
}
