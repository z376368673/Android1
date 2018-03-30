package com.jhobor.zzb.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jhobor.zzb.R;
import com.jhobor.zzb.entity.EnterpriseStore;
import com.jhobor.zzb.entity.FamilyStore;

import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/7/15.
 */

public class EnterpriseStoreAdapter extends BaseAdapter implements View.OnClickListener {
    private Context mContext;
    private LayoutInflater inflater;
    private List<EnterpriseStore> mDatas;

    public EnterpriseStoreAdapter(Context context, List<EnterpriseStore> datas) {
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
        ImageView shopIcon, lock;
        TextView name, call, company;

        ViewHolder(View view) {
            shopIcon = (ImageView) view.findViewById(R.id.shopIcon);
            lock = (ImageView) view.findViewById(R.id.lock);
            name = (TextView) view.findViewById(R.id.name);
            call = (TextView) view.findViewById(R.id.call);
            company = (TextView) view.findViewById(R.id.company);
            call.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
            call.setOnClickListener(EnterpriseStoreAdapter.this);
            lock.setOnClickListener(EnterpriseStoreAdapter.this);
        }
        void stuff(EnterpriseStore item,int pos){
            call.setTag(pos);
            lock.setTag(pos);
            if (!item.getStoreIcon().isEmpty()){
                Glide.with(mContext)
                        .load(item.getStoreIcon())
                        .into(shopIcon);
            }
            name.setText(item.getName());
            company.setText(item.getCompany());
            if (item.isLocked()){
                lock.setImageResource(R.mipmap.lock);
            }else {
                lock.setImageResource(R.mipmap.unlock);
            }
        }

    }
    @Override
    public void onClick(View v) {
        int pos = (int) v.getTag();
        if (v.getId()==R.id.call){
            EnterpriseStore enterpriseStore = mDatas.get(pos);
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Toast.makeText(mContext,"你未授予本应用【打电话】权限，开启后才能打电话",Toast.LENGTH_LONG).show();
                return;
            }
            mContext.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + enterpriseStore.getPhoneNum())));
        }else if (v.getId()==R.id.lock){
            int count = getCount();
            for (int i = 0;i<count;i++){
                EnterpriseStore store = mDatas.get(i);
                if (i==pos){
                    store.setLocked(true);
                }else {
                    store.setLocked(false);
                }
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_enterprise_suit,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        EnterpriseStore item = mDatas.get(position);
        holder.stuff(item,position);
        return convertView;
    }
}
