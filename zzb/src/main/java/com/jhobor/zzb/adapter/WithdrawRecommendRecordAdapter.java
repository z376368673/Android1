package com.jhobor.zzb.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jhobor.zzb.R;
import com.jhobor.zzb.entity.Product;
import com.jhobor.zzb.entity.RecommendMember;

import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/7/15.
 */

public class WithdrawRecommendRecordAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<RecommendMember> mDatas;

    public WithdrawRecommendRecordAdapter(Context context, List<RecommendMember> datas){
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
        TextView member,money;
        ViewHolder(View view){
            member = (TextView) view.findViewById(R.id.member);
            money = (TextView) view.findViewById(R.id.money);
        }
        void stuff(RecommendMember item){
            member.setText(String.format(Locale.CHINA,"%s", item.getMobile()));
            money.setText(String.format(Locale.CHINA,"%d", item.getReward()));
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_withdraw_recommend_record,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        RecommendMember item = mDatas.get(position);
        holder.stuff(item);
        return convertView;
    }
}
