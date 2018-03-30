package com.jhobor.ddc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jhobor.ddc.R;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.entity.Orders;

import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/1/5.
 */

public class BuyGoodsBaseAdapter extends BaseAdapter {
    private List<Orders> ordersList;
    private Context context;
    private LayoutInflater inflater;

    public BuyGoodsBaseAdapter(List<Orders> ordersList, Context context) {
        this.ordersList = ordersList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return ordersList.size();
    }

    @Override
    public Object getItem(int position) {
        return ordersList.get(position);
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
            convertView = inflater.inflate(R.layout.item_buy_goods, parent, false);
            viewHolder.checked = (ImageView) convertView.findViewById(R.id.checked);
            viewHolder.goodsPic = (ImageView) convertView.findViewById(R.id.goodsPic);
            viewHolder.storeName = (TextView) convertView.findViewById(R.id.storeName);
            viewHolder.ordersTime = (TextView) convertView.findViewById(R.id.ordersTime);
            viewHolder.goodsName = (TextView) convertView.findViewById(R.id.goodsName);
            viewHolder.price = (TextView) convertView.findViewById(R.id.price);
            viewHolder.count = (TextView) convertView.findViewById(R.id.count);
            viewHolder.sum = (TextView) convertView.findViewById(R.id.sum);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Orders orders = ordersList.get(position);

        viewHolder.storeName.setText(String.format(Locale.CHINA, "┃%s", orders.getStoreName()));
        viewHolder.ordersTime.setText(orders.getOrdersTime());
        viewHolder.checked.setVisibility(orders.isChecked() ? View.VISIBLE : View.INVISIBLE);
        Glide.with(context)
                .load(orders.getGoodsPic())
                .error(R.mipmap.load_img_fail)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(viewHolder.goodsPic);
        viewHolder.goodsName.setText(orders.getGoodsName());
        viewHolder.price.setText(String.format(Locale.CHINA, "￥%.2f", orders.getPrice()));
        viewHolder.count.setText(String.valueOf(orders.getCount()));
        viewHolder.sum.setText(String.format(Locale.CHINA, "合计：￥%.2f", orders.getMoney()));
        return convertView;
    }

    private static class ViewHolder {
        ImageView checked, goodsPic;
        TextView storeName, ordersTime, goodsName, price, count, sum;
    }
}
