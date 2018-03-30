package com.jhobor.ddc.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jhobor.ddc.R;
import com.jhobor.ddc.activity.MapNavActivity;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.entity.Store;
import com.jhobor.ddc.utils.ConvertUtil;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Administrator on 2017/1/4.
 */

public class StoreBaseAdapter extends BaseAdapter {
    private List<Store> storeList;
    private LayoutInflater inflater;
    private Context context;

    public StoreBaseAdapter(List<Store> storeList, Context context) {
        this.storeList = storeList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return storeList.size();
    }

    @Override
    public Object getItem(int position) {
        return storeList.get(position);
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
            convertView = inflater.inflate(R.layout.item_home_store, parent, false);
            viewHolder.storePic = (ImageView) convertView.findViewById(R.id.storePic);
            viewHolder.storeName = (TextView) convertView.findViewById(R.id.storeName);
            viewHolder.distance = (TextView) convertView.findViewById(R.id.distance);
            viewHolder.type = (TextView) convertView.findViewById(R.id.type);
            viewHolder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Store store = storeList.get(position);
        Glide.with(context)
                .load(store.getPicture())
                .error(R.mipmap.load_img_fail)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .bitmapTransform(new RoundedCornersTransformation(context,20,0))
                .into(viewHolder.storePic);
        viewHolder.storeName.setText(store.getName());
        viewHolder.ratingBar.setRating(store.getScores());
        viewHolder.type.setText(store.getType());
        viewHolder.distance.setText(ConvertUtil.formatDistance(store.getDistance()));
        viewHolder.distance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MapNavActivity.class);
                intent.putExtra("lat", store.getLat());
                intent.putExtra("lng", store.getLng());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        ImageView storePic;
        TextView storeName, distance, type;
        RatingBar ratingBar;
    }
}
