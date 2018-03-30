package com.jhobor.fortune;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
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

public class BalanceDetailsActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    TextView unmatch, uncompleted, completed;
    ViewPager viewPager;

    SparseArray<List<Booding>> boodingArr = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_details);
        HideIMEUtil.wrap(this);

        unmatch = (TextView) findViewById(R.id.unmatch);
        uncompleted = (TextView) findViewById(R.id.uncompleted);
        completed = (TextView) findViewById(R.id.completed);
        viewPager = (ViewPager) findViewById(R.id.viewPage);
        BarUtil.topBar(this, "静态钱包明细");

        unmatch.setOnClickListener(this);
        uncompleted.setOnClickListener(this);
        completed.setOnClickListener(this);
        getData();
    }

    private void getData() {
        String token = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.balanceRecord(token, 0).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
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
                            String name = jo.getString("name");
                            String phone = jo.getString("phone");
                            int timePoor = jo.getInt("timePoor");
                            Booding booding = new Booding(id, phone, name, (int) money, date, tag, status);
                            //将待打款，待收款归为未完成
                            if (status == 2)
                                status = 1;
                            int key = boodingArr.indexOfKey(status);
                            if (key < 0) {
                                List<Booding> list = new ArrayList<Booding>();
                                list.add(booding);
                                boodingArr.append(status, list);
                            } else {
                                 boodingArr.get(status).add(booding);
                            }
                        }
                        List<Fragment> fragmentList = new ArrayList<Fragment>(3);
                        BalancePageFragment unmatchFragment = new BalancePageFragment();
                        Bundle unmatchBundle = new Bundle();
                        unmatchBundle.putString("whatPage","static");
                        unmatchBundle.putParcelableArrayList("boodingList", (ArrayList<? extends Parcelable>) boodingArr.get(0));
                        unmatchFragment.setArguments(unmatchBundle);
                        fragmentList.add(unmatchFragment);

                        BalancePageFragment uncompletedFragment = new BalancePageFragment();
                        Bundle uncompletedBundle = new Bundle();
                        uncompletedBundle.putString("whatPage","static");
                        uncompletedBundle.putParcelableArrayList("boodingList", (ArrayList<? extends Parcelable>) boodingArr.get(1));
                        uncompletedFragment.setArguments(uncompletedBundle);
                        fragmentList.add(uncompletedFragment);

                        BalancePageFragment completedFragment = new BalancePageFragment();
                        Bundle completedBundle = new Bundle();
                        completedBundle.putString("whatPage","static");
                        completedBundle.putParcelableArrayList("boodingList",
                                (ArrayList<? extends Parcelable>) boodingArr.get(3));
                        completedFragment.setArguments(completedBundle);
                        fragmentList.add(completedFragment);
                        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(), fragmentList));
                        viewPager.addOnPageChangeListener(BalanceDetailsActivity.this);
                        unmatch.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.tabActived));
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
        if (v == unmatch) {
            viewPager.setCurrentItem(0);
        } else if (v == uncompleted) {
            viewPager.setCurrentItem(1);
        } else if (v == completed) {
            viewPager.setCurrentItem(2);
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
            unmatch.setTextColor(activedColor);
            uncompleted.setTextColor(color);
            completed.setTextColor(color);
        } else if (position == 1) {
            unmatch.setTextColor(color);
            completed.setTextColor(color);
            uncompleted.setTextColor(activedColor);
        } else {
            unmatch.setTextColor(color);
            uncompleted.setTextColor(color);
            completed.setTextColor(activedColor);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
