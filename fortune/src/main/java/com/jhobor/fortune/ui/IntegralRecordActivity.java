package com.jhobor.fortune.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.jhobor.fortune.R;
import com.jhobor.fortune.ui.fragment.IntegralRecordFragment;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 积分记录详情
 *
 * Created by zh on 2018/5/8.
 *
 */

public class IntegralRecordActivity extends BaseActivity {


    private TextView tv_text1;
    private View tv_text1_line;
    private TextView tv_text2;
    private View tv_text2_line;
    private TextView tv_text3;
    private View tv_text3_line;

    List<Fragment> fragmentList = new ArrayList<Fragment>();
    int type = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_integral_record_detailed);
        initTitle();
        type = getIntent().getIntExtra("Tag",0);
        switch (type){
            case 0:
                titleView.setText("租车业绩积分记录");
                break;
            case 1:
                titleView.setText("电商积分记录");
                break;
            case 2:
                titleView.setText("服务中心积分记录");
                break;
        }

        initView();
        initFragment();
    }

    private void initView() {
        tv_text1 = (TextView) findViewById(R.id.tv_text1);
        tv_text1_line = findViewById(R.id.tv_text1_line);
        tv_text2 = (TextView) findViewById(R.id.tv_text2);
        tv_text2_line = findViewById(R.id.tv_text2_line);
        tv_text3 = (TextView) findViewById(R.id.tv_text3);
        tv_text3_line = findViewById(R.id.tv_text3_line);

        tv_text1.setOnClickListener(this);
        tv_text2.setOnClickListener(this);
        tv_text3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (tv_text1==view){
            IntegralRecordFragment.TYPE=0;
            selectTab(0);
        }else if (tv_text2==view){
            IntegralRecordFragment.TYPE=1;
            selectTab(1);
        }else if (tv_text3==view){
            IntegralRecordFragment.TYPE=2;
            selectTab(2);
        }
    }

    private void initFragment() {
        IntegralRecordFragment.INTEGRAL_TYPE= type;
        IntegralRecordFragment fragment1 = new IntegralRecordFragment();
        IntegralRecordFragment fragment2 = new IntegralRecordFragment();
        IntegralRecordFragment fragment3 = new IntegralRecordFragment();
        fragmentList.add(fragment1);
        fragmentList.add(fragment2);
        fragmentList.add(fragment3);
        IntegralRecordFragment.TYPE=0;
        selectTab(0);
    }


    private void selectTab(int indexOfChild) {
        Fragment fragment;
        if (indexOfChild==0){
            tv_text1.setTextColor(getResources().getColor(R.color.blue_5e));
            tv_text1_line.setBackgroundColor(getResources().getColor(R.color.blue_5e));
            tv_text2.setTextColor(getResources().getColor(R.color.black_66));
            tv_text2_line.setBackgroundColor(getResources().getColor(R.color.white));
            tv_text3.setTextColor(getResources().getColor(R.color.black_66));
            tv_text3_line.setBackgroundColor(getResources().getColor(R.color.white));
            fragment = fragmentList.get(0);
        }else if (indexOfChild==1){
            tv_text1.setTextColor(getResources().getColor(R.color.black_66));
            tv_text1_line.setBackgroundColor(getResources().getColor(R.color.white));
            tv_text2.setTextColor(getResources().getColor(R.color.blue_5e));
            tv_text2_line.setBackgroundColor(getResources().getColor(R.color.blue_5e));
            tv_text3.setTextColor(getResources().getColor(R.color.black_66));
            tv_text3_line.setBackgroundColor(getResources().getColor(R.color.white));
            fragment = fragmentList.get(1);
        }else {
            tv_text1.setTextColor(getResources().getColor(R.color.black_66));
            tv_text1_line.setBackgroundColor(getResources().getColor(R.color.white));
            tv_text2.setTextColor(getResources().getColor(R.color.black_66));
            tv_text2_line.setBackgroundColor(getResources().getColor(R.color.white));
            tv_text3.setTextColor(getResources().getColor(R.color.blue_5e));
            tv_text3_line.setBackgroundColor(getResources().getColor(R.color.blue_5e));
            fragment = fragmentList.get(2);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }
}
