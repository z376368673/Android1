package com.jhobor.ddc.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.ddc.R;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.entity.Delivery;
import com.jhobor.ddc.utils.ErrorUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/1/5.
 */

public class RushOrdersBaseAdapter extends BaseAdapter {
    private List<Delivery> deliveryList;
    private Context context;
    private LayoutInflater inflater;

    public RushOrdersBaseAdapter(List<Delivery> deliveryList, Context context) {
        this.deliveryList = deliveryList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return deliveryList.size();
    }

    @Override
    public Object getItem(int position) {
        return deliveryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_rush_orders, parent, false);
            viewHolder.call = (ImageView) convertView.findViewById(R.id.call);
            viewHolder.storeName = (TextView) convertView.findViewById(R.id.storeName);
            viewHolder.task = (TextView) convertView.findViewById(R.id.task);
            viewHolder.money = (TextView) convertView.findViewById(R.id.money);
            viewHolder.fromAddr = (TextView) convertView.findViewById(R.id.fromAddr);
            viewHolder.toAddr = (TextView) convertView.findViewById(R.id.toAddr);
            viewHolder.reputationScores = (TextView) convertView.findViewById(R.id.reputationScore);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.phoneTitle = (TextView) convertView.findViewById(R.id.phoneTitle);
            viewHolder.phone = (TextView) convertView.findViewById(R.id.phone);
            viewHolder.rush = (Button) convertView.findViewById(R.id.rush);
            viewHolder.phoneTitle.setVisibility(View.GONE);
            viewHolder.phone.setVisibility(View.GONE);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Delivery delivery = deliveryList.get(position);
        viewHolder.storeName.setText(delivery.getStoreName());
        viewHolder.task.setText(delivery.getTask());
        viewHolder.money.setText(String.format(Locale.CHINA, "￥%.1f", delivery.getMoney()));
        viewHolder.fromAddr.setText(delivery.getFromAddr());
        viewHolder.toAddr.setText(delivery.getToAddr());
        viewHolder.reputationScores.setText(String.format(Locale.CHINA, "%d分", delivery.getReputationScores()));
        viewHolder.time.setText(delivery.getMinTime());
        viewHolder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + delivery.getStorePhone()));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "拨号权限已被限制，请在安全中心允许本应用打电话", Toast.LENGTH_SHORT).show();
                } else {
                    context.startActivity(intent);
                }
            }
        });
        viewHolder.rush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Object token = BaseApplication.dataMap.get("token");
                if (token == null) {
                    Toast.makeText(context, "你还没登录，不能抢哦", Toast.LENGTH_SHORT).show();
                } else {
                    BaseApplication.iService.rush((String) token, delivery.getId()).enqueue(new RetrofitCallback(context, new RetrofitCallback.DataParser() {
                        @Override
                        public void parse(String data) {
                            try {
                                JSONObject jsonObject = new JSONObject(data);
                                int msg = jsonObject.getInt("msg");
                                Button button = (Button) v;
                                button.setEnabled(false);
                                if (msg == 1) {
                                    Toast.makeText(context, "抢到啦，运气真好！", Toast.LENGTH_SHORT).show();
                                    button.setText("已抢到");
                                } else if (msg == 0) {
                                    Toast.makeText(context, "没有实名认证，还不能抢", Toast.LENGTH_SHORT).show();
                                    button.setText("未实名");
                                } else {
                                    Toast.makeText(context, "已被别人抢去了，下次请早~", Toast.LENGTH_SHORT).show();
                                    button.setText("被抢了");
                                }
                            } catch (JSONException e) {
                                ErrorUtil.retrofitResponseParseFail(context, e);
                            }
                        }
                    }));
                }
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        ImageView call;
        TextView storeName, task, money, fromAddr, toAddr, reputationScores, time, phoneTitle, phone;
        Button rush;
    }
}
