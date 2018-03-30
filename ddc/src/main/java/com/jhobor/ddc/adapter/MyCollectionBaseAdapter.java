package com.jhobor.ddc.adapter;

import android.content.Context;
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
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.entity.Collection;

import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/1/5.
 */

public class MyCollectionBaseAdapter extends BaseAdapter {
    private List<Collection> collectionList;
    private Context context;
    private LayoutInflater inflater;

    public MyCollectionBaseAdapter(List<Collection> collectionList, Context context) {
        this.collectionList = collectionList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return collectionList.size();
    }

    @Override
    public Object getItem(int position) {
        return collectionList.get(position);
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
            convertView = inflater.inflate(R.layout.item_my_collection, parent, false);
            viewHolder.storePic = (ImageView) convertView.findViewById(R.id.storePic);
            viewHolder.storeName = (TextView) convertView.findViewById(R.id.storeName);
            viewHolder.scores = (TextView) convertView.findViewById(R.id.scores);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Collection collection = collectionList.get(position);
        Glide.with(context)
                .load(collection.getStorePic())
                .error(R.mipmap.load_img_fail)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(viewHolder.storePic);
        viewHolder.storeName.setText(collection.getStoreName());
        viewHolder.scores.setText(String.format(Locale.CHINA, "%.1f", collection.getScores()));
        viewHolder.date.setText(collection.getDate());
        viewHolder.ratingBar.setRating(collection.getScores());

        return convertView;
    }

    private static class ViewHolder {
        ImageView storePic;
        TextView storeName, scores, date;
        RatingBar ratingBar;
    }
}
