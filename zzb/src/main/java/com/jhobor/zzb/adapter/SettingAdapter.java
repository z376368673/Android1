package com.jhobor.zzb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jhobor.zzb.R;
import com.jhobor.zzb.entity.SettingItem;

import java.util.List;

/**
 * Created by Administrator on 2017/7/15.
 */

public class SettingAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<SettingItem> mDatas;

    public SettingAdapter(Context context, List<SettingItem> datas){
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

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).getInfo().isEmpty()?1:2;
    }

    private class ViewHolder1{
        TextView name;
        ViewHolder1(View view){
            name = (TextView) view.findViewById(R.id.name);
        }
        void stuff(final SettingItem item){
            name.setText(item.getName());
        }
    }
    private class ViewHolder2{
        TextView name,info;
        ViewHolder2(View view){
            name = (TextView) view.findViewById(R.id.name);
            info = (TextView) view.findViewById(R.id.info);
        }
        void stuff(final SettingItem item){
            name.setText(item.getName());
            info.setText(item.getInfo());
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder1 holder1;
        ViewHolder2 holder2;
        SettingItem item = mDatas.get(position);
        int viewType = getItemViewType(position);
        switch (viewType){
            case 1:
                if (convertView == null){
                    convertView = inflater.inflate(R.layout.item_setting_enter,parent,false);
                    holder1 = new ViewHolder1(convertView);
                    convertView.setTag(holder1);
                }else {
                    holder1 = (ViewHolder1) convertView.getTag();
                }
                holder1.stuff(item);
                break;
            case 2:
                if (convertView == null){
                    convertView = inflater.inflate(R.layout.item_setting_info,parent,false);
                    holder2 = new ViewHolder2(convertView);
                    convertView.setTag(holder2);
                }else {
                    holder2 = (ViewHolder2) convertView.getTag();
                }
                holder2.stuff(item);
                break;
        }
        return convertView;
    }
}
