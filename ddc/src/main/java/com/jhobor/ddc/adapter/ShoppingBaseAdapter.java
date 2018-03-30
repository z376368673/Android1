package com.jhobor.ddc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhobor.ddc.R;
import com.jhobor.ddc.entity.Store;
import com.jhobor.ddc.utils.ConvertUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/1/5.
 */

public class ShoppingBaseAdapter extends BaseAdapter {
    private List<Store> storeList;
    private Context context;
    private LayoutInflater inflater;

    public ShoppingBaseAdapter(List<Store> storeList, Context context) {
        this.storeList = storeList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return storeList.size();
    }

    @Override
    public Object getItem(int position) {
        return storeList.get(position);
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
            convertView = inflater.inflate(R.layout.item_shopping, parent, false);
            viewHolder.storePic = (ImageView) convertView.findViewById(R.id.storePic);
            viewHolder.storeName = (TextView) convertView.findViewById(R.id.storeName);
            viewHolder.storeAddr = (TextView) convertView.findViewById(R.id.storeAddr);
            viewHolder.distance = (TextView) convertView.findViewById(R.id.distance);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Store store = storeList.get(position);
        //BaseApplication.display(store.getPicture(),viewHolder.storePic);
        viewHolder.storeName.setText(store.getName());
        viewHolder.storeAddr.setText(store.getAddr());
        viewHolder.distance.setText(ConvertUtil.formatDistance(store.getDistance()));
        return convertView;
    }

    private static class ViewHolder {
        ImageView storePic;
        TextView storeName, storeAddr, distance;
    }
}
