package com.jhobor.ddc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhobor.ddc.R;
import com.jhobor.ddc.entity.MeItem;

import java.util.List;

/**
 * Created by Administrator on 2017/1/5.
 */

public class MeBaseAdapter extends BaseAdapter {
    private List<MeItem> meItemList;
    private Context context;
    private LayoutInflater inflater;

    public MeBaseAdapter(List<MeItem> meItemList, Context context) {
        this.meItemList = meItemList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return meItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return meItemList.get(position);
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
            convertView = inflater.inflate(R.layout.item_me, parent, false);
            viewHolder.itemPic = (ImageView) convertView.findViewById(R.id.itemPic);
            viewHolder.itemName = (TextView) convertView.findViewById(R.id.itemName);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MeItem meItem = meItemList.get(position);
        //BaseApplication.display(msg.getPicture(),msgPic);

        viewHolder.itemPic.setImageResource(meItem.getResId());
        viewHolder.itemName.setText(meItem.getName());
        return convertView;
    }

    private static class ViewHolder {
        ImageView itemPic;
        TextView itemName;
    }
}
