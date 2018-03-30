package com.jhobor.ddc.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

public class StoreCommentsBaseAdapter extends BaseAdapter {
    private List<Comment> commentList;
    private Context context;
    private LayoutInflater inflater;

    public StoreCommentsBaseAdapter(List<Comment> commentList, Context context) {
        this.commentList = commentList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentList.get(position);
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
            convertView = inflater.inflate(R.layout.item_store_comment, parent, false);
            viewHolder.userPic = (ImageView) convertView.findViewById(R.id.userPic);
            viewHolder.takePhoto1 = (ImageView) convertView.findViewById(R.id.takePhoto1);
            viewHolder.takePhoto2 = (ImageView) convertView.findViewById(R.id.takePhoto2);
            viewHolder.takePhoto3 = (ImageView) convertView.findViewById(R.id.takePhoto3);
            viewHolder.takePhoto4 = (ImageView) convertView.findViewById(R.id.takePhoto4);
            viewHolder.takePhoto5 = (ImageView) convertView.findViewById(R.id.takePhoto5);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.userName);
            viewHolder.content = (TextView) convertView.findViewById(R.id.content);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.commentsPictureBox = (GridLayout) convertView.findViewById(R.id.commentsPictureBox);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Comment comment = commentList.get(position);
        Glide.with(context)
                .load(comment.getUserPic())
                .error(R.mipmap.load_img_fail)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(viewHolder.userPic);
        viewHolder.userName.setText(comment.getUserName());
        viewHolder.content.setText(comment.getContent());
        viewHolder.time.setText(comment.getTime());
        int i = 0;
        if (comment.getPictureList() != null && comment.getPictureList().size() > 0) {
            int len = comment.getPictureList().size();
            for (; i < len; i++) {
                Log.i(">>", String.valueOf(i));
                if (i == 0) {
                    viewHolder.takePhoto1.setVisibility(View.VISIBLE);
                    Glide.with(context)
                            .load(comment.getPictureList().get(i))
                            .error(R.mipmap.load_img_fail)
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .into(viewHolder.takePhoto1);
                } else if (i == 1) {
                    viewHolder.takePhoto2.setVisibility(View.VISIBLE);
                    Glide.with(context)
                            .load(comment.getPictureList().get(i))
                            .error(R.mipmap.load_img_fail)
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .into(viewHolder.takePhoto2);
                } else if (i == 2) {
                    viewHolder.takePhoto3.setVisibility(View.VISIBLE);
                    Glide.with(context)
                            .load(comment.getPictureList().get(i))
                            .error(R.mipmap.load_img_fail)
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .into(viewHolder.takePhoto3);
                } else if (i == 3) {
                    viewHolder.takePhoto4.setVisibility(View.VISIBLE);
                    Glide.with(context)
                            .load(comment.getPictureList().get(i))
                            .error(R.mipmap.load_img_fail)
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .into(viewHolder.takePhoto4);
                } else if (i == 4) {
                    viewHolder.takePhoto5.setVisibility(View.VISIBLE);
                    Glide.with(context)
                            .load(comment.getPictureList().get(i))
                            .error(R.mipmap.load_img_fail)
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .into(viewHolder.takePhoto5);
                }
            }
        }
        for (; i < 5; i++) {
            if (i == 0) {
                viewHolder.takePhoto1.setVisibility(View.GONE);
            } else if (i == 1) {
                viewHolder.takePhoto2.setVisibility(View.GONE);
            } else if (i == 2) {
                viewHolder.takePhoto3.setVisibility(View.GONE);
            } else if (i == 3) {
                viewHolder.takePhoto4.setVisibility(View.GONE);
            } else if (i == 4) {
                viewHolder.takePhoto5.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    private static class ViewHolder {
        ImageView userPic, takePhoto1, takePhoto2, takePhoto3, takePhoto4, takePhoto5;
        TextView userName, content, time;
        GridLayout commentsPictureBox;
    }
}
