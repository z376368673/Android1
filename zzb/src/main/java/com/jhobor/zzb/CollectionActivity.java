package com.jhobor.zzb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhobor.zzb.adapter.CategoryAdapter;
import com.jhobor.zzb.base.BaseActivity;
import com.jhobor.zzb.entity.Category;
import com.jhobor.zzb.entity.CategoryListMap;
import com.jhobor.zzb.entity.Product;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CollectionActivity extends BaseActivity implements View.OnClickListener {
    ImageView close;
    RecyclerView categories;
    Button ok;

    boolean enable = false;
    List<CategoryListMap> allCategories = new ArrayList<>();
    CategoryAdapter categoryAdapter;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            enable = intent.getBooleanExtra("enable",false);
            ok.setText(enable?"确定":"请选择");
            ok.setEnabled(enable);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        close = (ImageView) findViewById(R.id.close);
        categories = (RecyclerView) findViewById(R.id.categories);
        categories.setLayoutManager(new LinearLayoutManager(this));
        ok = (Button) findViewById(R.id.ok);
        ok.setText("请选择");
        close.setOnClickListener(this);
        ok.setOnClickListener(this);

        registerReceiver(receiver,new IntentFilter("switchCollectButton"));
        initData();
    }

    private void initData() {
        Category c1 = Category.makeTop(1, "商品-天然石材-大理石");
        c1.setDefault(true);
        CategoryListMap categoryListMap = new CategoryListMap(c1);
        allCategories.add(categoryListMap);

        Category c2 = Category.makeTop(2, "背景墙");
        CategoryListMap categoryListMap2 = new CategoryListMap(c2);
        categoryListMap2.addChild(c2.makeChild(20,"瓷砖背景墙"));
        categoryListMap2.addChild(c2.makeChild(21,"硅藻泥背景墙"));
        categoryListMap2.addChild(c2.makeChild(22,"xxx1背景墙"));
        categoryListMap2.addChild(c2.makeChild(23,"xxx2背景墙"));
        categoryListMap2.addChild(c2.makeChild(24,"xxx3背景墙"));
        categoryListMap2.addChild(c2.makeChild(25,"xxx4背景墙"));
        categoryListMap2.addChild(c2.makeChild(26,"+"));
        allCategories.add(categoryListMap2);

        Category c3 = Category.makeTop(3, "木地板");
        CategoryListMap categoryListMap3 = new CategoryListMap(c3);
        categoryListMap3.addChild(c3.makeChild(30,"瓷砖木地板"));
        categoryListMap3.addChild(c3.makeChild(31,"硅藻泥木地板"));
        categoryListMap3.addChild(c3.makeChild(32,"xxx1木地板"));
        categoryListMap3.addChild(c3.makeChild(33,"xxx2木地板"));
        categoryListMap3.addChild(c3.makeChild(34,"xxx3木地板"));
        categoryListMap3.addChild(c3.makeChild(35,"xxx4木地板"));
        categoryListMap3.addChild(c3.makeChild(36,"+"));
        allCategories.add(categoryListMap3);

        Category c4 = Category.makeTop(4,"+");
        CategoryListMap categoryListMap4 = new CategoryListMap(c4);
        allCategories.add(categoryListMap4);

        categoryAdapter = new CategoryAdapter(this,allCategories);
        categories.setAdapter(categoryAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v==close){
            setResult(RESULT_CANCELED);
            finish();
        }else if (v==ok){
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
