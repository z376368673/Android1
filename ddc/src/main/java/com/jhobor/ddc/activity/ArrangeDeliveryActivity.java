package com.jhobor.ddc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhobor.ddc.R;
import com.jhobor.ddc.adapter.MyFragmentPagerAdapter;
import com.jhobor.ddc.fragment.RushSendFragment;
import com.jhobor.ddc.fragment.StreamSendFragment;

import java.util.ArrayList;
import java.util.List;

public class ArrangeDeliveryActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    ImageView topArrow, streamCursor, rushCursor;
    TextView topTitle, streamSend, rushSend, done;
    ViewPager viewPager;

    List<TextView> tabList = new ArrayList<>();
    List<View> cursorList = new ArrayList<>();
    int curPos = 0;
    int ordersId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrange_delivery);

        Intent intent = getIntent();
        ordersId = intent.getIntExtra("ordersId", 0);
        initView();
        handleEvt();
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);
        done.setOnClickListener(this);
        streamSend.setOnClickListener(this);
        rushSend.setOnClickListener(this);
        viewPager.addOnPageChangeListener(this);
    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        topTitle = (TextView) findViewById(R.id.topTitle);
        done = (TextView) findViewById(R.id.done);
        streamSend = (TextView) findViewById(R.id.streamSend);
        rushSend = (TextView) findViewById(R.id.rushSend);
        streamCursor = (ImageView) findViewById(R.id.streamCursor);
        rushCursor = (ImageView) findViewById(R.id.rushCursor);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        addTabs();
        topTitle.setText("安排发货");
        done.setText("自配送");
        done.setVisibility(View.VISIBLE);
        List<Fragment> fragmentList = getFragmentData();
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
    }

    private void addTabs() {
        tabList.add(streamSend);
        tabList.add(rushSend);

        cursorList.add(streamCursor);
        cursorList.add(rushCursor);
    }

    private List<Fragment> getFragmentData() {
        List<Fragment> list = new ArrayList<>();

        Bundle bundle = new Bundle();
        bundle.putInt("ordersId", ordersId);
        StreamSendFragment streamSendFragment = new StreamSendFragment();
        streamSendFragment.setArguments(bundle);
        list.add(streamSendFragment);

        RushSendFragment rushSendFragment = new RushSendFragment();
        rushSendFragment.setArguments(bundle);
        list.add(rushSendFragment);

        return list;
    }

    @Override
    public void onClick(View v) {
        if (v == topArrow) {
            finish();
        } else if (v == streamSend) {
            changeTab(0);
            viewPager.setCurrentItem(0);
        } else if (v == rushSend) {
            changeTab(1);
            viewPager.setCurrentItem(1);
        } else if (v == done) {

        }
    }

    private void changeTab(int newPos) {
        if (newPos != curPos) {
            int red = ContextCompat.getColor(this, R.color.redTheme);
            tabList.get(newPos).setTextColor(red);
            cursorList.get(newPos).setVisibility(View.VISIBLE);

            int black = ContextCompat.getColor(this, R.color.black);
            tabList.get(curPos).setTextColor(black);
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
