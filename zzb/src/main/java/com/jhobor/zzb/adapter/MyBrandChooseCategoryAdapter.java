package com.jhobor.zzb.adapter;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jhobor.zzb.R;
import com.jhobor.zzb.entity.Category;
import com.jhobor.zzb.entity.Product;

import java.util.List;

/**
 * Created by Administrator on 2017/7/15.
 */

public class MyBrandChooseCategoryAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<Category> mDatas;

    public MyBrandChooseCategoryAdapter(Context context, List<Category> datas){
        mContext = context;
        inflater = LayoutInflater.from(context);
        mDatas = datas;
    }
    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        CheckBox name;
        ViewHolder(View view){
            name = (CheckBox) view.findViewById(R.id.name);
        }
        void stuff(Category item, int position){
            name.setText(item.getName());
        }

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_my_brand_choose_category,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        Category item = mDatas.get(position);
        holder.stuff(item,position);
        return convertView;
    }
}
