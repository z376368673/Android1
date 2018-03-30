package com.jhobor.ddc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jhobor.ddc.R;
import com.jhobor.ddc.entity.Needed;

import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/1/5.
 */

public class MyNeededBaseAdapter extends BaseAdapter {
    private List<Needed> neededList;
    private Context context;
    private LayoutInflater inflater;

    public MyNeededBaseAdapter(List<Needed> neededList, Context context) {
        this.neededList = neededList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return neededList.size();
    }

    @Override
    public Object getItem(int position) {
        return neededList.get(position);
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
            convertView = inflater.inflate(R.layout.item_needed, parent, false);
            viewHolder.goodsName = (TextView) convertView.findViewById(R.id.goodsName);
            viewHolder.count = (TextView) convertView.findViewById(R.id.count);
            viewHolder.demand = (TextView) convertView.findViewById(R.id.demand);
            viewHolder.toAddr = (TextView) convertView.findViewById(R.id.toAddr);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Needed needed = neededList.get(position);
        //BaseApplication.display(msg.getPicture(),msgPic);
        viewHolder.goodsName.setText(needed.getGoodsName());
        viewHolder.count.setText(String.valueOf(needed.getCount()));
        viewHolder.demand.setText(String.format(Locale.CHINA, "产品要求：%s", needed.getDemand()));
        viewHolder.toAddr.setText(String.format(Locale.CHINA, "收货地址：%s", needed.getToAddr()));
        viewHolder.date.setText(String.format(Locale.CHINA, "发布日期：%s", needed.getDate()));
        return convertView;
    }

    private static class ViewHolder {
        TextView goodsName, count, demand, toAddr, date;
    }
}
