package com.jhobor.ddc.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhobor.ddc.R;
import com.jhobor.ddc.adapter.MyFragmentPagerAdapter;
import com.jhobor.ddc.fragment.PublishTicketFragment;
import com.jhobor.ddc.fragment.TicketMangeFragment;

import java.util.ArrayList;
import java.util.List;

public class TicketManageActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    ImageView topArrow;
    View publishCursor, manageCursor;
    TextView topTitle, publishText, manageText;
    FrameLayout publishTab, manageTab;
    ViewPager viewPager;

    int num = 3;
    int curPos = 0;
    List<TextView> tabTextList = new ArrayList<>(num);
    List<View> cursorList = new ArrayList<>(num);
    List<Fragment> fragmentList = new ArrayList<>(num);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_manage);

        initView();
        handleEvt();
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);
        publishTab.setOnClickListener(this);
        manageTab.setOnClickListener(this);
        viewPager.addOnPageChangeListener(this);
    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        topTitle = (TextView) findViewById(R.id.topTitle);
        publishText = (TextView) findViewById(R.id.publishText);
        manageText = (TextView) findViewById(R.id.manageText);
        publishCursor = findViewById(R.id.publishCursor);
        manageCursor = findViewById(R.id.manageCursor);
        publishTab = (FrameLayout) findViewById(R.id.publishTab);
        manageTab = (FrameLayout) findViewById(R.id.manageTab);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        topTitle.setText("优惠券");
        initTabs();
        initFragments();
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
    }

    private void initFragments() {
        fragmentList.add(new PublishTicketFragment());
        fragmentList.add(new TicketMangeFragment());
    }

    private void initTabs() {
        tabTextList.add(publishText);
        tabTextList.add(manageText);

        cursorList.add(publishCursor);
        cursorList.add(manageCursor);
    }


    @Override
    public void onClick(View v) {
        if (v == topArrow) {
            finish();
        } else if (v == publishTab) {
            viewPager.setCurrentItem(0);
        } else if (v == manageTab) {
            viewPager.setCurrentItem(1);
        }
    }

    private void changeTab(int newPos) {
        if (newPos != curPos) {
            int blackGray = ContextCompat.getColor(this, R.color.blackGray);
            int redTheme = ContextCompat.getColor(this, R.color.redTheme);
            tabTextList.get(curPos).setTextColor(blackGray);
            cursorList.get(curPos).setVisibility(View.INVISIBLE);

            tabTextList.get(newPos).setTextColor(redTheme);
            cursorList.get(newPos).setVisibility(View.VISIBLE);
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
