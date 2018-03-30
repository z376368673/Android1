package com.huazong.app.huazong.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huazong.app.huazong.R;
import com.huazong.app.huazong.entity.Global;
import com.huazong.app.huazong.entity.Order;
import com.huazong.app.huazong.utils.QRUtil;

import java.util.List;


public class OrderUnusedBaseAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
    private List<Order> orderList;
    private int depth;

    public OrderUnusedBaseAdapter(Context context, LayoutInflater inflater, List<Order> orderList, int depth) {
        this.inflater = inflater;
        this.context = context;
        this.orderList = orderList;
        this.depth = depth;
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int i) {
        return orderList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    static class ViewHolder {
        ImageView QRcode;
        TextView storeName, orderDate, orderTime, orderPass,type;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_order_unused, viewGroup, false);
            holder.QRcode = (ImageView) view.findViewById(R.id.QRcode);
            holder.storeName = (TextView) view.findViewById(R.id.storeName);
            holder.orderDate = (TextView) view.findViewById(R.id.orderDate);
            holder.orderTime = (TextView) view.findViewById(R.id.orderTime);
            holder.orderPass = (TextView) view.findViewById(R.id.orderPass);
            holder.type = (TextView) view.findViewById(R.id.type);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final Order order = orderList.get(i);
        holder.storeName.setText(order.getName());
        holder.orderDate.setText(order.getDate());
        holder.orderTime.setText(order.getTime());
        holder.orderPass.setText("场地"+order.getByway());
        holder.type.setText(order.getType()==0?"射箭":"射击");
        holder.QRcode.setImageBitmap(
                QRUtil.createBitmap(
                    "orderId:" + order.getId()
                    + ",accounts:" + Global.getUserid()
                    + ",field:" + order.getByway()
                    + ",timeSlot:" + order.getDate().replace("-","")+order.getTime()
                    + ",category:" + order.getType()
                    + ",number:" + depth
                    + ",storeId:"+order.getStoreId()
                )
        );

        return view;
    }

}
