package com.jhobor.ddc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jhobor.ddc.R;
import com.jhobor.ddc.entity.Category;

import java.util.List;

/**
 * Created by Administrator on 2017/1/5.
 */

public class StoreCategoryBaseAdapter extends BaseAdapter {
    private List<Category> categoryList;
    private Context context;
    private LayoutInflater inflater;
    private int pos;

    public StoreCategoryBaseAdapter(List<Category> categoryNameList, Context context) {
        this.categoryList = categoryNameList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setPos(int pos) {
        this.pos = pos;
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
            convertView = inflater.inflate(R.layout.item_store_category, parent, false);
            viewHolder.itemName = (TextView) convertView.findViewById(R.id.itemName);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Category category = categoryList.get(position);
        if (position == pos) {
            convertView.setBackgroundResource(R.color.redPink);
        } else {
            convertView.setBackgroundResource(R.color.white);
        }

        viewHolder.itemName.setText(category.getName());
        return convertView;
    }

    private static class ViewHolder {
        TextView itemName;
    }
}
