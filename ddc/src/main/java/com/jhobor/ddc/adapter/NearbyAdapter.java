package com.jhobor.ddc.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jhobor.ddc.R;
import com.jhobor.ddc.activity.StoreActivity;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.entity.Store;
import com.jhobor.ddc.utils.ConvertUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/1/17.
 */

public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.ViewHolder> {
    Context context;
    private List<Store> datas;

    public NearbyAdapter(List<Store> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    public void setDatas(List<Store> datas) {
        this.datas = datas;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_shopping, viewGroup, false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final Store store = datas.get(position);
        viewHolder.storeName.setText(store.getName());
        viewHolder.storeAddr.setText(store.getAddr());
        viewHolder.distance.setText(ConvertUtil.formatDistance(store.getDistance()));
        Glide.with(context)
                .load(store.getPicture())
                .error(R.mipmap.load_img_fail)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(viewHolder.storePic);
        viewHolder.itemShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StoreActivity.class);
                intent.putExtra("storeId", store.getId());
                context.startActivity(intent);
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
        ImageView storePic;
        TextView storeName, storeAddr, distance;
        LinearLayout itemShopping;

        public ViewHolder(View view) {
            super(view);
            storeName = (TextView) view.findViewById(R.id.storeName);
            storeAddr = (TextView) view.findViewById(R.id.storeAddr);
            distance = (TextView) view.findViewById(R.id.distance);
            storePic = (ImageView) view.findViewById(R.id.storePic);
            itemShopping = (LinearLayout) view.findViewById(R.id.itemShopping);
        }
    }
}

