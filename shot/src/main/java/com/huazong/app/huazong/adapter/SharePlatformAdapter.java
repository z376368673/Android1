package com.huazong.app.huazong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huazong.app.huazong.R;
import com.huazong.app.huazong.base.BaseApplication;

/**
 * Created by Administrator on 2017/5/9.
 */

public class SharePlatformAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
    private String[] mPlatforms;
    private int[] mPlatformIcons;

    public SharePlatformAdapter(Context context,String[] platforms,int[] platformIcons){
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        mPlatformIcons = platformIcons;
        mPlatforms = platforms;
    }

    @Override
    public int getCount() {
        return mPlatforms.length;
    }

    @Override
    public Object getItem(int position) {
        return mPlatforms[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            convertView = inflater.inflate(R.layout.item_dialog_share,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.platformIcon);
            viewHolder.platform = (TextView) convertView.findViewById(R.id.platformText);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.platform.setText(mPlatforms[position]);
        Glide.with(context)
                .load(mPlatformIcons[position])
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .error(BaseApplication.GLIDE_ERROR)
                .into(viewHolder.icon);
        return convertView;
    }


    static class ViewHolder{
        ImageView icon;
        TextView platform;
    }
}
