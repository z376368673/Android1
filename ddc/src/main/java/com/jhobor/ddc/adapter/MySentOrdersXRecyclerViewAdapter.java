package com.jhobor.ddc.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.ddc.R;
import com.jhobor.ddc.entity.Delivery;

import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/1/17.
 */

public class MySentOrdersXRecyclerViewAdapter extends RecyclerView.Adapter<MySentOrdersXRecyclerViewAdapter.ViewHolder> {
    private List<Delivery> datas;
    private Context context;

    public MySentOrdersXRecyclerViewAdapter(List<Delivery> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_rush_orders, viewGroup, false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final Delivery delivery = datas.get(position);

        viewHolder.storeName.setText(delivery.getStoreName());
        viewHolder.task.setText(delivery.getTask());
        viewHolder.money.setText(String.format(Locale.CHINA, "￥%.1f", delivery.getMoney()));
        viewHolder.fromAddr.setText(delivery.getFromAddr());
        viewHolder.toAddr.setText(delivery.getToAddr());
        viewHolder.reputationScore.setText(String.format(Locale.CHINA, "%d分", delivery.getReputationScores()));
        viewHolder.phone.setText(delivery.getEmployerPhone());
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
        viewHolder.rush.setText("呼叫雇主");
        viewHolder.rush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + delivery.getEmployerPhone()));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "拨号权限已被限制，请在安全中心允许本应用打电话", Toast.LENGTH_SHORT).show();
                } else {
                    context.startActivity(intent);
                }
            }
        });
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView call;
        TextView storeName, task, money, fromAddr, toAddr, reputationScore, phone, time;
        Button rush;

        public ViewHolder(View view) {
            super(view);
            call = (ImageView) view.findViewById(R.id.call);
            storeName = (TextView) view.findViewById(R.id.storeName);
            task = (TextView) view.findViewById(R.id.task);
            money = (TextView) view.findViewById(R.id.money);
            fromAddr = (TextView) view.findViewById(R.id.fromAddr);
            toAddr = (TextView) view.findViewById(R.id.toAddr);
            reputationScore = (TextView) view.findViewById(R.id.reputationScore);
            phone = (TextView) view.findViewById(R.id.phone);
            time = (TextView) view.findViewById(R.id.time);
            rush = (Button) view.findViewById(R.id.rush);
        }
    }
}

