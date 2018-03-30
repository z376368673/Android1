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
import com.jhobor.ddc.entity.Product;
import com.jhobor.ddc.utils.ConvertUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/1/5.
 */

public class FarmingBaseAdapter extends BaseAdapter {
    private List<Product> productList;
    private Context context;
    private LayoutInflater inflater;

    public FarmingBaseAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
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
            convertView = inflater.inflate(R.layout.item_farming, parent, false);
            viewHolder.storePic = (ImageView) convertView.findViewById(R.id.storePic);
            viewHolder.storeName = (TextView) convertView.findViewById(R.id.storeName);
            viewHolder.distance = (TextView) convertView.findViewById(R.id.distance);
            viewHolder.storeAddr = (TextView) convertView.findViewById(R.id.storeAddr);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Product product = productList.get(position);
        Glide.with(context)
                .load(product.getStorePic())
                .error(R.mipmap.load_img_fail)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(viewHolder.storePic);
        viewHolder.storeName.setText(product.getStoreName());
        viewHolder.distance.setText(ConvertUtil.formatDistance(product.getDistance()));
        viewHolder.storeAddr.setText(product.getStoreAddr());

        return convertView;
    }

    private static class ViewHolder {
        ImageView storePic;
        TextView storeName, distance, storeAddr;
    }
}
