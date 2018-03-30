package com.jhobor.zzb;

import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.jhobor.zzb.base.BaseActivity;
import com.jhobor.zzb.fragments.HomeSuitFragment;
import com.jhobor.zzb.fragments.MerchantFragment;
import com.jhobor.zzb.fragments.ShopFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopActivity extends BaseActivity implements View.OnClickListener {
    ImageView back,share,shopIcon;
    TextView addr,phoneNum,homeSuit,enterpriseSuit,merchant,shop;
    Spinner homeBlock,enterpriseBlock,merchantBlock;
    FrameLayout frameLayout;

    FragmentManager fragmentManager;
    List<TextView> tvs = new ArrayList<>(4);
    int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        back = (ImageView) findViewById(R.id.back);
        share = (ImageView) findViewById(R.id.share);
        shopIcon = (ImageView) findViewById(R.id.shopIcon);
        addr = (TextView) findViewById(R.id.addr);
        phoneNum = (TextView) findViewById(R.id.phoneNum);
        homeSuit = (TextView) findViewById(R.id.homeSuit);
        enterpriseSuit = (TextView) findViewById(R.id.enterpriseSuit);
        merchant = (TextView) findViewById(R.id.merchant);
        shop = (TextView) findViewById(R.id.shop);
        homeBlock = (Spinner) findViewById(R.id.homeBlock);
        enterpriseBlock = (Spinner) findViewById(R.id.enterpriseBlock);
        merchantBlock = (Spinner) findViewById(R.id.merchantBlock);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);

        share.setVisibility(View.INVISIBLE);
        back.setOnClickListener(this);
        homeSuit.setOnClickListener(this);
        enterpriseSuit.setOnClickListener(this);
        merchant.setOnClickListener(this);
        shop.setOnClickListener(this);
        tvs.add(homeSuit);
        tvs.add(enterpriseSuit);
        tvs.add(merchant);
        tvs.add(shop);
        String[] addrNames = {"区域","罗定","深圳","广州","上海","北京"};
        List<Map<String,Object>> dataTable = new ArrayList<>();
        for (String addrName : addrNames) {
            Map<String, Object> map = new HashMap<>();
            map.put("place", addrName);
            dataTable.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, dataTable, R.layout.item_spinner_place, new String[]{"place"}, new int[]{R.id.place});
        homeBlock.setAdapter(simpleAdapter);
        enterpriseBlock.setAdapter(simpleAdapter);
        merchantBlock.setAdapter(simpleAdapter);

        fragmentManager = getSupportFragmentManager();
        HomeSuitFragment homeSuitFragment = new HomeSuitFragment();
        Bundle bundle = new Bundle();
        bundle.putString("what","homeSuit");
        homeSuitFragment.setArguments(bundle);
        fragmentManager.beginTransaction().add(R.id.frameLayout,homeSuitFragment).commitAllowingStateLoss();

    }

    @Override
    public void onClick(View v) {
        if (v==back){
            finish();
        }else if (v==homeSuit){
            tab(0);
            HomeSuitFragment homeSuitFragment = new HomeSuitFragment();
            Bundle bundle = new Bundle();
            bundle.putString("what","homeSuit");
            homeSuitFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.frameLayout,homeSuitFragment).commitAllowingStateLoss();
        }else if (v==enterpriseSuit){
            tab(1);
            HomeSuitFragment homeSuitFragment = new HomeSuitFragment();
            Bundle bundle = new Bundle();
            bundle.putString("what","enterpriseSuit");
            homeSuitFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.frameLayout,homeSuitFragment).commitAllowingStateLoss();
        }else if (v==merchant){
            tab(2);
            MerchantFragment merchantFragment = new MerchantFragment();
            fragmentManager.beginTransaction().replace(R.id.frameLayout,merchantFragment).commitAllowingStateLoss();
        }else if (v==shop){
            tab(3);
            ShopFragment shopFragment = new ShopFragment();
            fragmentManager.beginTransaction().replace(R.id.frameLayout,shopFragment).commitAllowingStateLoss();
        }
    }

    void tab(int newPos){
        if (newPos!=pos) {
            int size = tvs.size();
            int purple = ContextCompat.getColor(this, R.color.purple);
            int grayDark = ContextCompat.getColor(this, R.color.grayDark);
            for (int i = 0;i< size;i++){
                if (i==newPos) {
                    tvs.get(i).setTextColor(purple);
                }else {
                    tvs.get(i).setTextColor(grayDark);
                }
            }
            pos = newPos;
        }
    }
}
