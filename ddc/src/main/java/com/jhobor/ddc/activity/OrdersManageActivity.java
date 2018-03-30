package com.jhobor.ddc.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhobor.ddc.R;
import com.jhobor.ddc.adapter.MyFragmentPagerAdapter;
import com.jhobor.ddc.fragment.OrdersManageItemFragment;

import java.util.ArrayList;
import java.util.List;

public class OrdersManageActivity extends AppCompatActivity implements View.OnClickListener, OnPageChangeListener {
    ImageView topArrow, allCursor, need2sendCursor, need2receiveCursor, completedCursor;
    TextView topTitle, all, need2send, need2receive, completed;
    ViewPager viewPager;

    List<View> cursorList = new ArrayList<>();
    List<TextView> tabList = new ArrayList<>();
    int curPos = 0;
    List<Fragment> fragmentList = new ArrayList<>();
    MyFragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_manage);

        initView();
        handleEvt();
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);
        all.setOnClickListener(this);
        need2send.setOnClickListener(this);
        need2receive.setOnClickListener(this);
        completed.setOnClickListener(this);
        viewPager.addOnPageChangeListener(this);
    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        topTitle = (TextView) findViewById(R.id.topTitle);
        all = (TextView) findViewById(R.id.all);
        need2send = (TextView) findViewById(R.id.need2send);
        need2receive = (TextView) findViewById(R.id.need2receive);
        completed = (TextView) findViewById(R.id.completed);
        allCursor = (ImageView) findViewById(R.id.allCursor);
        need2sendCursor = (ImageView) findViewById(R.id.sendCursor);
        need2receiveCursor = (ImageView) findViewById(R.id.receiveCursor);
        completedCursor = (ImageView) findViewById(R.id.completedCursor);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        topTitle.setText("订单管理");
        addTabs();
        updateData();

        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
    }

    private void updateData() {
        fragmentList.clear();
        Log.i(">>", String.valueOf(fragmentList.size()));
        for (int i = 0; i < 4; i++) {
            Bundle bundle = new Bundle();
            bundle.putInt("status", i);
            OrdersManageItemFragment fragment = new OrdersManageItemFragment();
            fragment.setArguments(bundle);
            fragmentList.add(fragment);
        }
        Log.i(">>", String.valueOf(fragmentList.size()));
    }

    private void addTabs() {
        tabList.add(all);
        tabList.add(need2send);
        tabList.add(need2receive);
        tabList.add(completed);

        cursorList.add(allCursor);
        cursorList.add(need2sendCursor);
        cursorList.add(need2receiveCursor);
        cursorList.add(completedCursor);
    }

    @Override
    public void onClick(View v) {
        if (v == topArrow) {
            finish();
        } else if (v == all) {
            changeTab(0);
            viewPager.setCurrentItem(0);
        } else if (v == need2send) {
            changeTab(1);
            viewPager.setCurrentItem(1);
        } else if (v == need2receive) {
            changeTab(2);
            viewPager.setCurrentItem(2);
        } else if (v == completed) {
            changeTab(3);
            viewPager.setCurrentItem(3);
        }
    }

    private void changeTab(int newPos) {
        if (newPos != curPos) {
            int red = ContextCompat.getColor(this, R.color.redTheme);
            int gray = ContextCompat.getColor(this, R.color.textGray);
            tabList.get(newPos).setTextColor(red);
            tabList.get(curPos).setTextColor(gray);

            cursorList.get(newPos).setVisibility(View.VISIBLE);
            cursorList.get(curPos).setVisibility(View.INVISIBLE);
            curPos = newPos;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        changeTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
