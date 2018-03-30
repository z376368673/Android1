package com.jhobor.ddc.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jhobor.ddc.R;
import com.jhobor.ddc.activity.PublishGoodsActivity;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.entity.Goods;
import com.jhobor.ddc.utils.ErrorUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/1/5.
 */

public class GoodsManageBaseAdapter extends BaseAdapter {
    private List<Goods> goodsList;
    private Context context;
    private LayoutInflater inflater;

    public GoodsManageBaseAdapter(List<Goods> goodsList, Context context) {
        this.goodsList = goodsList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return goodsList.size();
    }

    @Override
    public Object getItem(int position) {
        return goodsList.get(position);
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
            convertView = inflater.inflate(R.layout.item_goods_manage, parent, false);
            viewHolder.goodsPic = (ImageView) convertView.findViewById(R.id.goodsPic);
            viewHolder.goodsName = (TextView) convertView.findViewById(R.id.goodsName);
            viewHolder.price = (TextView) convertView.findViewById(R.id.price);
            viewHolder.stock = (TextView) convertView.findViewById(R.id.stock);
            viewHolder.edit = (Button) convertView.findViewById(R.id.edit);
            viewHolder.delete = (Button) convertView.findViewById(R.id.delete);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Goods goods = goodsList.get(position);
        Glide.with(context)
                .load(goods.getPicture())
                .error(R.mipmap.load_img_fail)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(viewHolder.goodsPic);
        viewHolder.goodsName.setText(goods.getName());
        viewHolder.price.setText(String.format(Locale.CANADA, "￥%.2f", goods.getPrice()));
        viewHolder.stock.setText(String.format(Locale.CANADA, "库存：%d", goods.getStock()));
        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PublishGoodsActivity.class);
                intent.putExtra("goodsId", goods.getId());
                context.startActivity(intent);
            }
        });
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("删除")
                        .setMessage(String.format(Locale.CANADA, "确定要删除产品【%s】", goods.getName()))
                        .setNegativeButton("取消", null)
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String uuid = (String) BaseApplication.dataMap.get("token");
                                BaseApplication.iService.delStoreGoods(uuid, goods.getId()).enqueue(new RetrofitCallback(context, new RetrofitCallback.DataParser() {
                                    @Override
                                    public void parse(String data) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(data);
                                            int msg = jsonObject.getInt("msg");
                                            if (msg == 1) {
                                                goodsList.remove(position);
                                                notifyDataSetChanged();
                                                Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            ErrorUtil.retrofitResponseParseFail(context, e);
                                        }
                                    }
                                }));
                            }
                        }).create();
                dialog.show();
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        ImageView goodsPic;
        TextView goodsName, price, stock;
        Button edit, delete;
    }
}
