package com.jhobor.fortune.oldui;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.fortune.R;
import com.jhobor.fortune.adapter.MyViewPagerAdapter;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.entity.Booding;
import com.jhobor.fortune.fragments.BalancePageFragment;
import com.jhobor.fortune.utils.BarUtil;
import com.jhobor.fortune.utils.ErrorUtil;
import com.jhobor.fortune.utils.HideIMEUtil;
import com.jhobor.fortune.utils.TabUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TradeRecordHelpActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    TextView help, getHelp, prize;
    ViewPager viewPager;

    List<Booding> boodingList = new ArrayList<>();
    TabUtil tabUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_record_help);
        HideIMEUtil.wrap(this);

        help = (TextView) findViewById(R.id.help);
        getHelp = (TextView) findViewById(R.id.getHelp);
        prize = (TextView) findViewById(R.id.prize);
        viewPager = (ViewPager) findViewById(R.id.viewPage);
        BarUtil.topBar(this, "帮助记录");
        int normalColor = ContextCompat.getColor(this, R.color.tabNormal);
        int activedColor = ContextCompat.getColor(this, R.color.tabActived);
        tabUtil = new TabUtil(normalColor,activedColor,new TextView[][]{{help},{getHelp},{prize}});
        help.setOnClickListener(this);
        getHelp.setOnClickListener(this);
        prize.setOnClickListener(this);
        getData();
    }

    private void getData() {
        String token = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.tradeRecordHelp(token, 1).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
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
                            String ledName = jo.getString("ledName");
                            String ledPhone = jo.getString("ledPhone");
                            String phone = jo.getString("phone");
                            String name = jo.getString("name");
                            int timePoor = jo.getInt("timePoor");
                            Booding booding = new Booding(id, phone, name, (int) money, date, tag, status, timePoor, ledName, ledPhone);
                            boodingList.add(booding);
                        }
                        List<Fragment> fragmentList = new ArrayList<Fragment>(2);
                        BalancePageFragment helpFragment = new BalancePageFragment();
                        Bundle helpBundle = new Bundle();
                        helpBundle.putParcelableArrayList("boodingList", (ArrayList<? extends Parcelable>) boodingList);
                        helpBundle.putString("whatPage", "help");
                        helpFragment.setArguments(helpBundle);
                        fragmentList.add(helpFragment);

                        BalancePageFragment getHelpFragment = new BalancePageFragment();
                        Bundle getHelpBundle = new Bundle();
                        getHelpBundle.putString("whatPage", "getHelp");
                        getHelpFragment.setArguments(getHelpBundle);
                        fragmentList.add(getHelpFragment);

                        BalancePageFragment prizeFragment = new BalancePageFragment();
                        Bundle prizeBundle = new Bundle();
                        prizeBundle.putString("whatPage", "prize");
                        prizeFragment.setArguments(prizeBundle);
                        fragmentList.add(prizeFragment);

                        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(), fragmentList));
                        viewPager.addOnPageChangeListener(TradeRecordHelpActivity.this);
                        help.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.tabActived));
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
        if (v == help) {
            viewPager.setCurrentItem(0);
        } else if (v == getHelp) {
            viewPager.setCurrentItem(1);
        }else if (v==prize){
            viewPager.setCurrentItem(2);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tabUtil.change(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
