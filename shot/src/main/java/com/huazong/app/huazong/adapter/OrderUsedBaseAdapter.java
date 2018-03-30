package com.huazong.app.huazong.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huazong.app.huazong.MapActivity;
import com.huazong.app.huazong.R;
import com.huazong.app.huazong.base.BaseApplication;
import com.huazong.app.huazong.entity.Order;

import java.util.List;


public class OrderUsedBaseAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
    private List<Order> orderList;

    public OrderUsedBaseAdapter(Activity activity, LayoutInflater inflater, List<Order> orderList) {
        this.inflater = inflater;
        this.context = activity;
        this.orderList = orderList;
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_order_used, viewGroup, false);
            holder.storePic = (ImageView) view.findViewById(R.id.storePic);
            holder.location = (ImageView) view.findViewById(R.id.orderPass);
            holder.storeName = (TextView) view.findViewById(R.id.storeName);
            holder.orderDate = (TextView) view.findViewById(R.id.orderDate);
            holder.orderTime = (TextView) view.findViewById(R.id.orderTime);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final Order order = orderList.get(i);
        Glide.with(context)
                .load(order.getPicture())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .error(BaseApplication.GLIDE_ERROR)
                .into(holder.storePic);
        holder.storeName.setText(order.getName());
        holder.orderDate.setText(order.getDate());
        holder.orderTime.setText(order.getTime());
        holder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MapActivity.class);
                intent.putExtra("selfLng", BaseApplication.here.longitude);
                intent.putExtra("selfLat", BaseApplication.here.latitude);
                intent.putExtra("destLng", order.getLatLng().longitude);
                intent.putExtra("destLat", order.getLatLng().latitude);
                context.startActivity(intent);
            }
        });

        return view;
    }

    static class ViewHolder {
        ImageView storePic, location;
        TextView storeName, orderDate, orderTime;
    }
}
