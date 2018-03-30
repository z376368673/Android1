package com.jhobor.ddc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jhobor.ddc.R;

import java.util.List;

/**
 * Created by Administrator on 2017/1/5.
 */

public class GoodsCategoryBaseAdapter extends BaseAdapter {
    private List<String> categoryList;
    private Context context;
    private LayoutInflater inflater;

    public GoodsCategoryBaseAdapter(List<String> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryList.get(position);
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
            convertView = inflater.inflate(R.layout.item_goods_category, parent, false);
            viewHolder.category = (TextView) convertView.findViewById(R.id.category);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String category = categoryList.get(position);
        viewHolder.category.setText(category);
        return convertView;
    }

    private static class ViewHolder {
        TextView category;
    }
}
