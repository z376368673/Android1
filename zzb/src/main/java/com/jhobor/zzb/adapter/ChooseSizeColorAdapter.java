package com.jhobor.zzb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jhobor.zzb.R;
import com.jhobor.zzb.entity.SizeColor;

import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/7/15.
 */

public class ChooseSizeColorAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<SizeColor> mDatas;

    public ChooseSizeColorAdapter(Context context, List<SizeColor> datas){
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

    public class ViewHolder{
        TextView sizeColor;
        ViewHolder(View view){
            sizeColor = (TextView) view.findViewById(R.id.sizeColor);
        }
        void stuff(final SizeColor item){
            sizeColor.setText(String.format(Locale.CHINA,"%s %s", item.getSize(),item.getColor()));
            if (item.isChecked()){
                sizeColor.setBackgroundResource(R.drawable.round_2_purple_bg);
            }else {
                sizeColor.setBackgroundResource(R.drawable.round_3_dark_gray_border);
            }
            sizeColor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.setChecked(!item.isChecked());
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_choose_size_color,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        SizeColor item = mDatas.get(position);
        holder.stuff(item);
        return convertView;
    }
}
