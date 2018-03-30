package com.huazong.app.huazong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huazong.app.huazong.R;
import com.huazong.app.huazong.entity.PhotoInfoList;
import com.huazong.app.huazong.view.MyGridView;

import java.util.List;

/**
 * Created by Administrator on 2017/5/9.
 */

public class ShareListAdapter  extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
    List<PhotoInfoList> photoList;

    public ShareListAdapter(Context context,List<PhotoInfoList> photoList) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.photoList = photoList;
    }

    @Override
    public int getCount() {
        return photoList.size();
    }

    @Override
    public Object getItem(int position) {
        return photoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            // 获取行布局，对行布局中的控件进行设置赋值
            view = inflater.inflate(R.layout.item_share, parent, false);
            holder.date = (TextView) view.findViewById(R.id.date);
            holder.photoGridView = (MyGridView) view.findViewById(R.id.photoGridView);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        PhotoInfoList photoInfo = photoList.get(position);
        holder.date.setText(photoInfo.getDate());
        holder.photoGridView.setAdapter(new PhotoGridViewAdapter(context, photoInfo.getPhotoInfos()));

        return view;
    }

    static class ViewHolder {
        TextView date;
        MyGridView photoGridView;
    }

}
