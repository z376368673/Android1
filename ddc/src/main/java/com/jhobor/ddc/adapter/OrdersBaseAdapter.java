package com.jhobor.ddc.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jhobor.ddc.R;
import com.jhobor.ddc.activity.CommentActivity;
import com.jhobor.ddc.activity.PayActivity;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.entity.Orders;
import com.jhobor.ddc.utils.ErrorUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Administrator on 2017/1/4.
 */

public class OrdersBaseAdapter extends BaseAdapter {
    private List<Orders> ordersList;
    private LayoutInflater inflater;
    private Context context;

    public OrdersBaseAdapter(List<Orders> ordersList, Context context) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_orders, parent, false);
            viewHolder.goodsPic = (ImageView) convertView.findViewById(R.id.goodsPic);
            viewHolder.storeName = (TextView) convertView.findViewById(R.id.storeName);
            viewHolder.state = (TextView) convertView.findViewById(R.id.state);
            viewHolder.goodsName = (TextView) convertView.findViewById(R.id.goodsName);
            viewHolder.ordersTime = (TextView) convertView.findViewById(R.id.ordersTime);
            viewHolder.money = (TextView) convertView.findViewById(R.id.money);
            viewHolder.comment = (TextView) convertView.findViewById(R.id.comment);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Orders orders = ordersList.get(position);
        Glide.with(context)
                .load(orders.getGoodsPic())
                .error(R.mipmap.load_img_fail)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .bitmapTransform(new RoundedCornersTransformation(context,20,0))
                .into(viewHolder.goodsPic);
        viewHolder.storeName.setText(orders.getStoreName());
        String orderStatus = "未知状态";
        String btnName = "";
        int state = orders.getState();

        if (state == 0) {
            orderStatus = "待付款";
            btnName = "去付款";
        } else if (state == 1) {
            orderStatus = "待发货";
            btnName = "";
        } else if (state == 2) {
            orderStatus = "待收货";
            btnName = "确认收货";
        } else if (state == 3) {
            orderStatus = "已完成";
            btnName = "去评论";
        } else if (state == 4) {
            orderStatus = "已评论";
            btnName = "";
        }
        Log.i(">>",String.format(Locale.CHINA,"position:%d  state:%d  btnName:%s",position,state,btnName));
        viewHolder.state.setText(orderStatus);
        viewHolder.goodsName.setText(orders.getGoodsName());
        viewHolder.ordersTime.setText(orders.getOrdersTime());
        viewHolder.money.setText(String.valueOf(orders.getMoney()));
        viewHolder.comment.setText(btnName);

        if (btnName.isEmpty()) {
            viewHolder.comment.setVisibility(View.INVISIBLE);
        } else if (state == 0) {
            viewHolder.comment.setVisibility(View.VISIBLE);
            viewHolder.comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PayActivity.class);
                    intent.putExtra("orders",orders);
                    context.startActivity(intent);
                }
            });
        } else if (state == 2) {
            viewHolder.comment.setVisibility(View.VISIBLE);
            viewHolder.comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MaterialDialog dialog = new MaterialDialog.Builder(context)
                            .title("重要")
                            .content("确认收货吗？请确保自己确实收到货物!!")
                            .positiveText("确认收货")
                            .positiveColor(ContextCompat.getColor(context, R.color.redPress))
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    String uuid = (String) BaseApplication.dataMap.get("token");
                                    BaseApplication.iService.confirmReceived(uuid, orders.getId()).enqueue(new RetrofitCallback(context, new RetrofitCallback.DataParser() {
                                        @Override
                                        public void parse(String data) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(data);
                                                int isLogin = jsonObject.getInt("isLogin");
                                                if (isLogin==1) {
                                                    int msg = jsonObject.getInt("msg");
                                                    if (msg == 1) {
                                                        Intent intent = new Intent("reloadOrders");
                                                        context.sendBroadcast(intent);
                                                    }else {
                                                        Toast.makeText(context,"确认失败，请稍候再试",Toast.LENGTH_SHORT).show();
                                                    }
                                                }else {
                                                    Toast.makeText(context,"未登录，请先登录再操作",Toast.LENGTH_SHORT).show();
                                                }

                                            } catch (JSONException e) {
                                                ErrorUtil.retrofitResponseParseFail(context, e);
                                            }
                                        }
                                    }));
                                }
                            })
                            .negativeText("取消")
                            .negativeColor(ContextCompat.getColor(context, R.color.blackGray))
                            .show();
                }
            });

        } else if (state == 3) {
            viewHolder.comment.setVisibility(View.VISIBLE);
            viewHolder.comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CommentActivity.class);
                    intent.putExtra("ordersId", orders.getId());
                    intent.putExtra("goodsPic", orders.getGoodsPic());
                    intent.putExtra("pos", position);
                    context.startActivity(intent);
                }
            });
        }

        return convertView;
    }

    private class ViewHolder {
        ImageView goodsPic;
        TextView storeName, state, goodsName, ordersTime, money, comment;
    }
}
