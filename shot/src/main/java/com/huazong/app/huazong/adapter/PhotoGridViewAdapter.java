package com.huazong.app.huazong.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huazong.app.huazong.R;
import com.huazong.app.huazong.base.BaseApplication;
import com.huazong.app.huazong.entity.PhotoInfo;
import com.huazong.app.huazong.utils.DisplayUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/5/9.
 */

public class PhotoGridViewAdapter  extends BaseAdapter {
    LayoutInflater inflater;
    private List<PhotoInfo> photoInfos;
    Context context;

    public PhotoGridViewAdapter(Context context, List<PhotoInfo> photoInfos) {
        this.inflater = LayoutInflater.from(context);
        this.photoInfos = photoInfos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return photoInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return photoInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        final PhotoInfo photoInfo = (PhotoInfo) getItem(position);
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_share_photo, viewGroup, false);
            holder.photo = (ImageView) view.findViewById(R.id.photo);
            holder.conver = (ImageView) view.findViewById(R.id.conver);
            ViewGroup.LayoutParams layoutParams = holder.conver.getLayoutParams();
            ViewGroup.LayoutParams layoutParams2 = holder.photo.getLayoutParams();
            int[] scale = DisplayUtil.getScreenScale(context);
            layoutParams.height = layoutParams.width = (scale[0] - DisplayUtil.dip2px(context, 30)) / 3;
            layoutParams2.height = layoutParams2.width = layoutParams.width - DisplayUtil.dip2px(context, 10);
            holder.photo.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.conver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean check = photoInfo.isCheck();
                    if (check){
                        photoInfo.setCheck(false);
                    }else {
                        for (PhotoInfo info:photoInfos){
                            if (photoInfo!=info){
                                photoInfo.setCheck(false);
                            }
                        }
                        photoInfo.setCheck(true);
                        Intent intent = new Intent("showSharePanel");
                        intent.putExtra("photoInfo",photoInfo);
                        context.sendBroadcast(intent);
                    }
                    notifyDataSetChanged();
                }
            });
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        String path = photoInfo.getPath();
        Glide.with(context)
                .load(path)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .error(BaseApplication.GLIDE_ERROR)
                .into(holder.photo);
        Log.i(">>",path);
        if (photoInfo.isCheck()) {
            holder.conver.setBackgroundResource(R.mipmap.share_check);
        } else {
            holder.conver.setBackgroundColor(Color.TRANSPARENT);
        }
        return view;
    }

    class ViewHolder {
        ImageView photo, conver;
    }
}
