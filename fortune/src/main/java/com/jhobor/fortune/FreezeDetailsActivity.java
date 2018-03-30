package com.jhobor.fortune;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.fortune.adapter.MyViewPagerAdapter;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.entity.Booding;
import com.jhobor.fortune.fragments.BalancePageFragment;
import com.jhobor.fortune.utils.BarUtil;
import com.jhobor.fortune.utils.ErrorUtil;
import com.jhobor.fortune.utils.HideIMEUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FreezeDetailsActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    TextView orderRecord, interestRecord;
    ViewPager viewPager;

    List<Booding> boodingList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freeze_details);
        HideIMEUtil.wrap(this);

        orderRecord = (TextView) findViewById(R.id.orderRecord);
        interestRecord = (TextView) findViewById(R.id.interestRecord);
        viewPager = (ViewPager) findViewById(R.id.viewPage);
        BarUtil.topBar(this, "冻结钱包明细");
        orderRecord.setOnClickListener(this);
        interestRecord.setOnClickListener(this);
        getData();
    }

    private void getData() {
        String token = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.balanceRecord(token, 1).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int isLogin = jsonObject.getInt("isLogin");
                    if (isLogin == 1) {
                        JSONArray helpList = jsonObject.getJSONArray("helpList");
                        int length = helpList.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject jo = helpList.getJSONObject(i);
                            int id = jo.getInt("id");
                            double money = jo.getDouble("money");
                            int tag = jo.getInt("tag");
                            String date = jo.getString("date");
                            int status = jo.getInt("status");
                            int timePoor = jo.getInt("timePoor");
                            Booding booding = new Booding(id, "***", "***", (int) money, date, tag, status);
                            boodingList.add(booding);
                        }
                        List<Fragment> fragmentList = new ArrayList<Fragment>(2);
                        BalancePageFragment freezeFragment = new BalancePageFragment();
                        Bundle freezeBundle = new Bundle();
                        freezeBundle.putString("whatPage","freeze");
                        freezeBundle.putParcelableArrayList("boodingList", (ArrayList<? extends Parcelable>) boodingList);
                        freezeFragment.setArguments(freezeBundle);
                        fragmentList.add(freezeFragment);

                        BalancePageFragment interestFragment = new BalancePageFragment();
                        Bundle interestBundle = new Bundle();
                        interestBundle.putString("whatPage", "interest");
                        interestFragment.setArguments(interestBundle);
                        fragmentList.add(interestFragment);

                        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(), fragmentList));
                        viewPager.addOnPageChangeListener(FreezeDetailsActivity.this);
                        orderRecord.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.tabActived));
                    } else {
                        Toast.makeText(getBaseContext(), "你未登录，获取数据失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                }
            }
        }));
    }

    @Override
    public void onClick(View v) {
        if (v == orderRecord) {
            viewPager.setCurrentItem(0);
        } else if (v == interestRecord) {
            viewPager.setCurrentItem(1);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int color = ContextCompat.getColor(getBaseContext(), R.color.tabNormal);
        int activedColor = ContextCompat.getColor(getBaseContext(), R.color.tabActived);
        if (position == 0) {
            orderRecord.setTextColor(activedColor);
            interestRecord.setTextColor(color);
        } else {
            orderRecord.setTextColor(color);
            interestRecord.setTextColor(activedColor);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
