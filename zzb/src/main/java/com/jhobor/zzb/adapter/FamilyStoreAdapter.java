package com.jhobor.zzb.adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jhobor.zzb.R;
import com.jhobor.zzb.entity.EnterpriseStore;
import com.jhobor.zzb.entity.FamilyStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/15.
 */

public class FamilyStoreAdapter extends BaseAdapter implements View.OnClickListener {
    private Context mContext;
    private LayoutInflater inflater;
    private List<FamilyStore> mDatas;

    private AlertDialog dialog;
    EnterpriseStoreAdapter adapter;

    public FamilyStoreAdapter(Context context, List<FamilyStore> datas){
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
    public void onClick(View v) {
        if (v.getId()==R.id.salesman){
            int pos = (int) v.getTag();
            if (mDatas.get(pos).getNumberOfSales()<1)
                return;
            if (dialog==null){
                LinearLayout dialogContainer = (LinearLayout) inflater.inflate(R.layout.dialog_round_grid,null,false);
                dialog = new AlertDialog.Builder(mContext).setView(dialogContainer).create();
                dialogContainer.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                ListView listView = new ListView(mContext);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                listView.setLayoutParams(params);
                dialogContainer.addView(listView);
                List<EnterpriseStore> enterpriseStoreList = new ArrayList<>();
                enterpriseStoreList.add(new EnterpriseStore(1,"","apple","18098983109","深圳公司",true));
                enterpriseStoreList.add(new EnterpriseStore(2,"","banana","18098983109","广州公司",false));
                enterpriseStoreList.add(new EnterpriseStore(3,"","cat","18098983109","惠州公司",false));
                enterpriseStoreList.add(new EnterpriseStore(4,"","dog","18098983109","上海公司",false));
                adapter = new EnterpriseStoreAdapter(mContext,enterpriseStoreList);
                listView.setAdapter(adapter);
            }else {
                // 重新请求数据，刷新适配器

                adapter.notifyDataSetChanged();
            }
            dialog.show();
        }
    }

    public class ViewHolder{
        TextView addr,salesman;
        ViewHolder(View view){
            addr = (TextView) view.findViewById(R.id.addr);
            salesman = (TextView) view.findViewById(R.id.salesman);
            salesman.setOnClickListener(FamilyStoreAdapter.this);
        }
        void stuff(FamilyStore item, int position){
            salesman.setTag(position);
            addr.setText(String.format(Locale.CHINA,"%s/%s/%s/%s/%S", item.getAddr(),item.getStreet(),item.getBlock(),item.getCity(),item.getProvince()));
            salesman.setText(String.format(Locale.CHINA,"业务员(%d)", item.getNumberOfSales()));
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_get_material,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        FamilyStore item = mDatas.get(position);
        holder.stuff(item,position);
        return convertView;
    }
}
