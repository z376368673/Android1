package com.huazong.app.huazong.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huazong.app.huazong.R;
import com.huazong.app.huazong.base.BaseApplication;
import com.huazong.app.huazong.entity.Rank;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


public class RankArrayAdapter extends ArrayAdapter<Rank> {
    LayoutInflater inflater;
    private int redColor,defaultColor;
    private int resource;

    public RankArrayAdapter(Context context, int resource, List<Rank> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.inflater = LayoutInflater.from(context);
        redColor = ContextCompat.getColor(context, R.color.red_text_color);
        defaultColor = ContextCompat.getColor(context, R.color.second_text_color);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(resource, parent, false);
            holder.rankNo = (TextView) convertView.findViewById(R.id.rankNo);
            holder.rankPic = (ImageView) convertView.findViewById(R.id.rankPic);
            holder.rankName = (TextView) convertView.findViewById(R.id.rankName);
            holder.rankDepth = (TextView) convertView.findViewById(R.id.rankDepth);
            holder.rankScore = (TextView) convertView.findViewById(R.id.rankScore);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Rank item = getItem(position);
        holder.rankNo.setText(String.valueOf(position + 1));
        if (position < 3) {
            holder.rankNo.setTextColor(redColor);
        }else {
            holder.rankNo.setTextColor(defaultColor);
        }
        Glide.with(getContext())
                .load(item.getPicture())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .error(BaseApplication.GLIDE_ERROR)
                .bitmapTransform(new CropCircleTransformation(getContext()))
                .into(holder.rankPic);
        holder.rankName.setText(item.getName());
        String depthStr = item.getDepth() + "关";
        holder.rankDepth.setText(depthStr);
        holder.rankScore.setText(String.valueOf(item.getScore()));

        return convertView;
    }

    static class ViewHolder {
        TextView rankNo, rankName, rankDepth, rankScore;
        ImageView rankPic;
    }
}
