package com.huazong.app.huazong.adapter;

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
import com.huazong.app.huazong.entity.Store;

import java.text.DecimalFormat;


public class StoreBaseAdapter extends BaseAdapter {
    private Store[] stores;
    Context context;
    LayoutInflater inflater;

    public StoreBaseAdapter(Context context, Store[] stores) {
        this.context = context;
        this.stores = stores;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return stores.length;
    }

    @Override
    public Object getItem(int i) {
        return stores[i];
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
            view = inflater.inflate(R.layout.item_store, viewGroup, false);
            holder.storePic = (ImageView) view.findViewById(R.id.storePic);
            holder.location = (ImageView) view.findViewById(R.id.orderPass);
            holder.storeName = (TextView) view.findViewById(R.id.storeName);
            holder.desc = (TextView) view.findViewById(R.id.descript);
            holder.distance = (TextView) view.findViewById(R.id.distance);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Store store = stores[i];
        Glide.with(context)
                .load(store.getPicture())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .error(BaseApplication.GLIDE_ERROR)
                .into(holder.storePic);
        holder.storeName.setText(store.getName());
        holder.desc.setText(store.getDesc());
        holder.distance.setText(exchange(store.getDistance()));
        holder.location.setOnClickListener(new LocationListener(store.getLng(), store.getLat()));
        return view;
    }

    private String exchange(double distance) {
        String result;
        if (distance > 1000) {
            DecimalFormat df = new DecimalFormat("#0.0 Km");
            result = df.format(distance / 1000);
        } else {
            DecimalFormat df = new DecimalFormat("#0 m");
            result = df.format(distance);
        }
        return result;
    }

    static class ViewHolder {
        ImageView storePic, location;
        TextView storeName, desc, distance;
    }

    private class LocationListener implements View.OnClickListener {
        double lng, lat;

        LocationListener(double lng, double lat) {
            this.lat = lat;
            this.lng = lng;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, MapActivity.class);
            intent.putExtra("destLng", lng);
            intent.putExtra("destLat", lat);
            context.startActivity(intent);
        }
    }
}
