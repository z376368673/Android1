package com.jhobor.ddc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jhobor.ddc.R;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.entity.Comment;

import java.util.List;

/**
 * Created by Administrator on 2017/1/5.
 */

public class MyCommentsBaseAdapter extends RecyclerView.Adapter<MyCommentsBaseAdapter.ViewHolder> {
    Context context;
    private List<Comment> datas;
    private int resId;

    public MyCommentsBaseAdapter(Context context, List<Comment> datas, int resId) {
        this.datas = datas;
        this.context = context;
        this.resId = resId;
    }

    public void setDatas(List<Comment> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(resId, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Comment item = datas.get(position);
        Glide.with(context)
                .load(item.getUserPic())
                .error(R.mipmap.load_img_fail)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.userPic);
        holder.userName.setText(item.getUserName());
        holder.content.setText(item.getContent());
        holder.brief.setText(item.getGoodsInfo());
        holder.time.setText(item.getTime());
        List<String> pictureList = item.getPictureList();
        int size = pictureList.size();
        int childCount = holder.commentsPictureBox.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView child = (ImageView) holder.commentsPictureBox.getChildAt(i);
            if (i < size) {
                child.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(pictureList.get(i))
                        .error(R.mipmap.load_img_fail)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(child);
            } else {
                child.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userPic;
        TextView userName, content, brief, time;
        GridLayout commentsPictureBox;

        public ViewHolder(View view) {
            super(view);
            userPic = (ImageView) view.findViewById(R.id.userPic);
            userName = (TextView) view.findViewById(R.id.userName);
            content = (TextView) view.findViewById(R.id.content);
            brief = (TextView) view.findViewById(R.id.brief);
            time = (TextView) view.findViewById(R.id.time);
            commentsPictureBox = (GridLayout) view.findViewById(R.id.commentsPictureBox);
        }
    }
}
