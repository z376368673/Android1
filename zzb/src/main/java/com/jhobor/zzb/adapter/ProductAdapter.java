package com.jhobor.zzb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jhobor.zzb.R;
import com.jhobor.zzb.entity.Product;

import java.util.List;

/**
 * Created by Administrator on 2017/7/14.
 */

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Product> mDatas;
    private Context mContext;

    public ProductAdapter(Context context,List<Product> datas){
        mContext = context;
        mDatas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        switch (viewType){
            case 1:
                View view1 = inflater.inflate(R.layout.item_home_1,parent,false);
                holder = new ViewHolder1(view1);
                break;
            case 2:
                View view2 = inflater.inflate(R.layout.item_home_2,parent,false);
                holder = new ViewHolder2(view2);
                break;
            case 3:
                View view3 = inflater.inflate(R.layout.item_home_3,parent,false);
                holder = new ViewHolder3(view3);
                break;
            default:
                holder = null;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        Product item = mDatas.get(position);
        switch (type){
            case 1:
                ((ViewHolder1)holder).stuff(item);
                break;
            case 2:
                ((ViewHolder2)holder).stuff(item);
                break;
            case 3:
                ((ViewHolder3)holder).stuff(item);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).getImages().size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHolder1 extends RecyclerView.ViewHolder{
        TextView title,dateTime,clicks;
        ImageView image1;
        ViewHolder1(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            dateTime = (TextView) view.findViewById(R.id.dateTime);
            clicks = (TextView) view.findViewById(R.id.clicks);
            image1 = (ImageView) view.findViewById(R.id.image1);
        }
        void stuff(Product product){
            title.setText(product.getTitle());
            dateTime.setText(product.getDateTime());
            clicks.setText(String.valueOf(product.getClicks()));
            Glide.with(mContext)
                    .load(product.getImages().get(0))
                    //.placeholder(R.mipmap.load_img)
                    .into(image1);
        }
    }
    class ViewHolder2 extends ViewHolder1{
        ImageView image2;
        ViewHolder2(View view){
            super(view);
            image2 = (ImageView) view.findViewById(R.id.image2);
        }
        void stuff(Product product){
            super.stuff(product);
            Glide.with(mContext)
                    .load(product.getImages().get(1))
                    .into(image2);
        }
    }
    class ViewHolder3 extends ViewHolder2{
        ImageView image3;
        ViewHolder3(View view){
            super(view);
            image3 = (ImageView) view.findViewById(R.id.image3);
        }
        void stuff(Product product){
            super.stuff(product);
            Glide.with(mContext)
                    .load(product.getImages().get(2))
                    .into(image3);
        }
    }
}
