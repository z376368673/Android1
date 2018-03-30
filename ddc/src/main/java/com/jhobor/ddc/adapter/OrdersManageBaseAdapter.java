package com.jhobor.ddc.adapter;

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
import com.jhobor.ddc.R;
import com.jhobor.ddc.activity.ArrangeDeliveryActivity;
import com.jhobor.ddc.activity.RushSendDetailsActivity;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.entity.Orders;

import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/1/4.
 */

public class OrdersManageBaseAdapter extends BaseAdapter {
    private List<Orders> ordersList;
    private LayoutInflater inflater;
    private Context context;

    public OrdersManageBaseAdapter(List<Orders> ordersList, Context context) {
        this.ordersList = ordersList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setOrdersList(List<Orders> ordersList) {
        this.ordersList = ordersList;
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
            convertView = inflater.inflate(R.layout.item_orders_manage, parent, false);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.userName);
            viewHolder.goodsPic = (ImageView) convertView.findViewById(R.id.goodsPic);
            viewHolder.state = (TextView) convertView.findViewById(R.id.state);
            viewHolder.goodsName = (TextView) convertView.findViewById(R.id.goodsName);
            viewHolder.price = (TextView) convertView.findViewById(R.id.price);
            viewHolder.count = (TextView) convertView.findViewById(R.id.count);
            viewHolder.ordersNo = (TextView) convertView.findViewById(R.id.ordersNo);
            viewHolder.ordersTime = (TextView) convertView.findViewById(R.id.ordersTime);
            viewHolder.operation = (TextView) convertView.findViewById(R.id.operation);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Orders orders = ordersList.get(position);
        Glide.with(context)
                .load(orders.getGoodsPic())
                .error(R.mipmap.load_img_fail)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(viewHolder.goodsPic);
        viewHolder.userName.setText(orders.getUserName());
        final int state = orders.getState();
        String stateText = "";
        final int type = orders.getType();
        if (state == 0) {
            stateText = "待付款";
        } else if (state == 1) {
            if (type == 2) {
                stateText = "待发货(抢单中)";
            } else {
                stateText = "待发货";
            }
        } else if (state == 2) {
            stateText = "待收货";
        } else if (state == 3) {
            stateText = "已完成";
        } else if (state == 4) {
            stateText = "已评论";
        }
        viewHolder.state.setText(stateText);
        viewHolder.goodsName.setText(orders.getGoodsName());
        viewHolder.price.setText(String.format(Locale.CHINA, "￥%.2f", orders.getPrice()));
        viewHolder.count.setText(String.format(Locale.CHINA, "x%d", orders.getCount()));
        viewHolder.ordersNo.setText(String.format(Locale.CHINA, "订单编号：%s", orders.getOrdersNo()));
        viewHolder.ordersTime.setText(String.format(Locale.CHINA, "订单时间：%s", orders.getOrdersTime()));
        String str = "";
        if (1 == state) {
            if (type == 2) {
                str = "待接单";
            } else {
                str = "发货";
            }
        }
        if (str.isEmpty()) {
            viewHolder.operation.setVisibility(View.GONE);
        } else {
            viewHolder.operation.setText(str);
            viewHolder.operation.setVisibility(View.VISIBLE);
            viewHolder.operation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (1 == state) {
                        if (type == 2) {
                            Intent intent = new Intent(context, RushSendDetailsActivity.class);
                            intent.putExtra("ordersId", orders.getId());
                            context.startActivity(intent);
                        } else {
                            Intent intent = new Intent(context, ArrangeDeliveryActivity.class);
                            intent.putExtra("ordersId", orders.getId());
                            context.startActivity(intent);
                        }
                    }
                }
            });
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView state, goodsName, price, count, ordersNo, ordersTime, operation, userName;
        ImageView goodsPic;
    }
}
