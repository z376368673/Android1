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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jhobor.zzb.R;
import com.jhobor.zzb.entity.Product;

import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/7/15.
 */

public class MyBrandProductsAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<Product> mDatas;

    public MyBrandProductsAdapter(Context context, List<Product> datas){
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

    public class ViewHolder implements View.OnClickListener {
        ImageView productImg;
        TextView name,type;
        public TextView add,share,delete;
        public LinearLayout actionBox;
        ViewHolder(View view){
            productImg = (ImageView) view.findViewById(R.id.productImg);
            type = (TextView) view.findViewById(R.id.type);
            name = (TextView) view.findViewById(R.id.name);
            add = (TextView) view.findViewById(R.id.add);
            share = (TextView) view.findViewById(R.id.share);
            delete = (TextView) view.findViewById(R.id.delete);
            actionBox = (LinearLayout) view.findViewById(R.id.actionBox);
            actionBox.setVisibility(View.GONE);
            add.setOnClickListener(this);
            share.setOnClickListener(this);
            delete.setOnClickListener(this);
        }
        void stuff(Product item, int position){
            add.setTag(position);
            share.setTag(position);
            delete.setTag(position);
            if (position>0) {
                productImg.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(mContext)
                        .load(item.getImages().get(0))
                        .into(productImg);
            }else {
                productImg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                productImg.setImageResource(R.mipmap.my_brand_add_product);
            }
            name.setText(item.getTitle());
            type.setText(item.getType());
        }

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            actionBox.setVisibility(View.GONE);
            if (v== add){
                Log.i(">>","copy");
                showPhotoPicker();
            }else if (v==share){
                Log.i(">>","share");
                showSharePanel();
            }else if (v==delete){
                Log.i(">>","delete");
                showConfirmDialog("确定删除此商品？");
            }
        }

        private void showPhotoPicker() {
            View view = inflater.inflate(R.layout.dialog_photo_picker, null);
            TextView gallery = (TextView) view.findViewById(R.id.gallery);
            TextView takePhoto = (TextView) view.findViewById(R.id.takePhoto);
            final AlertDialog dialog = new AlertDialog.Builder(mContext)
                    .setView(view)
                    .create();
            gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                }
            });
            takePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                }
            });
            dialog.show();
            dialog.getWindow().setLayout(500,ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private void showConfirmDialog(String mes) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_confirm,null);
        TextView msg = (TextView) view.findViewById(R.id.msg);
        Button yes = (Button) view.findViewById(R.id.yes);
        Button no = (Button) view.findViewById(R.id.no);
        msg.setText(mes);
        final AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setView(view)
                .create();
        dialog.show();
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private class MyClickListener implements View.OnClickListener {
        BottomSheetDialog dialog;

        MyClickListener(BottomSheetDialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void onClick(View v) {
            if (v.getId()==R.id.wechatCircle){
                Toast.makeText(mContext,"分享成功",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }else if (v.getId()==R.id.wechat){
                Toast.makeText(mContext,"分享成功",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }else if (v.getId()==R.id.qq){
                Toast.makeText(mContext,"分享成功",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }
    }
    private void showSharePanel(){
        BottomSheetDialog dialog = new BottomSheetDialog(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_share_bottom_sheet,null);
        dialog.setContentView(view);
        dialog.show();
        MyClickListener clickListener = new MyClickListener(dialog);
        view.findViewById(R.id.wechatCircle).setOnClickListener(clickListener);
        view.findViewById(R.id.wechat).setOnClickListener(clickListener);
        view.findViewById(R.id.qq).setOnClickListener(clickListener);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_my_brand_product,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        Product item = mDatas.get(position);
        holder.stuff(item,position);
        return convertView;
    }
}
