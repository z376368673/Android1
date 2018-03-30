package com.jhobor.zzb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.jhobor.zzb.adapter.StoreProductsAdapter;
import com.jhobor.zzb.base.BaseActivity;
import com.jhobor.zzb.entity.Product;
import com.jhobor.zzb.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

public class StoreProductsActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    ImageView back,share;
    GridView products;

    List<Product> productList = new ArrayList<>(4);
    StoreProductsAdapter adapter;
    int itemImgHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_products);

        back = (ImageView) findViewById(R.id.back);
        share = (ImageView) findViewById(R.id.share);
        products = (GridView) findViewById(R.id.products);

        back.setOnClickListener(this);
        List<String> list = new ArrayList<String>(1){{add("http://img15.3lian.com/2015/f2/147/d/74.jpg");}};
        productList.add(new Product(1,5200,"雪花白",list));
        productList.add(new Product(1,5200,"雪花白",list));
        productList.add(new Product(1,5200,"雪花白",list));
        productList.add(new Product(1,5200,"雪花白",list));
        products.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                products.getViewTreeObserver().removeOnPreDrawListener(this);
                int height = products.getHeight();
                itemImgHeight = height/2;
                Log.i("height>>", String.valueOf(height));
                Log.i("itemImgHeight>>", String.valueOf(itemImgHeight));
                adapter = new StoreProductsAdapter(getBaseContext(),productList,itemImgHeight);
                products.setAdapter(adapter);
                return true;
            }
        });
        products.setOnItemClickListener(this);
        products.setOnItemLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==back){
            finish();
        }else if (v==share){

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        StoreProductsAdapter.ViewHolder holder = (StoreProductsAdapter.ViewHolder) view.getTag();
        if(holder.collect.getVisibility()==View.VISIBLE){
            holder.collect.setVisibility(View.GONE);
        }else {
            startActivity(new Intent(this, HallActivity.class));
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        StoreProductsAdapter.ViewHolder holder = (StoreProductsAdapter.ViewHolder) view.getTag();
        if(holder.collect.getVisibility()!=View.VISIBLE){
            holder.collect.setVisibility(View.VISIBLE);
        }else {
            holder.collect.setVisibility(View.GONE);
        }
        return true;
    }
}
