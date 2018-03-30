package com.jhobor.zzb.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jhobor.zzb.R;
import com.jhobor.zzb.entity.Product;
import com.jhobor.zzb.utils.DisplayUtil;

import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/7/15.
 */

public class StoreProductsAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<Product> mDatas;
    private int mItemImgHeight;

    public StoreProductsAdapter(Context context, List<Product> datas, int itemImgHeight){
        mContext = context;
        inflater = LayoutInflater.from(context);
        mDatas = datas;
        mItemImgHeight = itemImgHeight;
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
        ImageView productImg;
        TextView price,type;
        public TextView collect;
        ViewHolder(View view){
            productImg = (ImageView) view.findViewById(R.id.productImg);
            price = (TextView) view.findViewById(R.id.price);
            type = (TextView) view.findViewById(R.id.type);
            collect = (TextView) view.findViewById(R.id.collect);
            collect.setVisibility(View.GONE);
            collect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    collect.setVisibility(View.GONE);
                    Log.i(">>","item collect");
                }
            });
        }
        void stuff(Product item){
            Glide.with(mContext)
                    .load(item.getImages().get(0))
                    .into(productImg);
            price.setText(String.format(Locale.CHINA,"￥%d M2", item.getPrice()));
            type.setText(item.getType());
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_product,parent,false);
            convertView.getLayoutParams().height = mItemImgHeight;
            Log.i("mItemImgHeight>>", String.valueOf(mItemImgHeight));
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        Product item = mDatas.get(position);
        holder.stuff(item);
        return convertView;
    }
}
