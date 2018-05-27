package com.jhobor.fortune.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jhobor.fortune.R;
import com.jhobor.fortune.ui.fragment.ReportCenterFragment;
import com.jhobor.fortune.ui.fragment.TeamRecordFragment;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 团队投资明细
 *
 * Created by zh on 2018/5/8.
 *
 */

public class TeamDetailedActivity extends BaseActivity {


    private TextView tv_addRecord;
    private View tv_addRecord_line;
    private TextView tv_unAddRecord;
    private View tv_unAddRecord_line;

    List<Fragment> fragmentList = new ArrayList<Fragment>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_team_detailed);
        initTitle();
        titleView.setText("团队投资明细");
        initView();
        initFragment();
    }

    private void initView() {
        tv_addRecord = (TextView) findViewById(R.id.tv_addRecord);
        tv_addRecord_line = findViewById(R.id.tv_addRecord_line);
        tv_unAddRecord = (TextView) findViewById(R.id.tv_unAddRecord);
        tv_unAddRecord_line = findViewById(R.id.tv_unAddRecord_line);

        tv_addRecord.setOnClickListener(this);
        tv_unAddRecord.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (tv_addRecord==view){
            TeamRecordFragment.TYPE=0;
            selectTab(0);
        }else if (tv_unAddRecord==view){
            TeamRecordFragment.TYPE=1;
            selectTab(1);
        }
    }

    private void initFragment() {
        TeamRecordFragment projectNFragment = new TeamRecordFragment();
        TeamRecordFragment projectPFragment = new TeamRecordFragment();
        fragmentList.add(projectNFragment);
        fragmentList.add(projectPFragment);
        TeamRecordFragment.TYPE=0;
        selectTab(0);
    }


    private void selectTab(int indexOfChild) {
        Fragment fragment;
        if (indexOfChild==0){
            tv_addRecord.setTextColor(getResources().getColor(R.color.blue_5e));
            tv_addRecord_line.setBackgroundColor(getResources().getColor(R.color.blue_5e));
            tv_unAddRecord.setTextColor(getResources().getColor(R.color.black_66));
            tv_unAddRecord_line.setBackgroundColor(getResources().getColor(R.color.white));
            fragment = fragmentList.get(0);
        }else {
            tv_addRecord.setTextColor(getResources().getColor(R.color.black_66));
            tv_addRecord_line.setBackgroundColor(getResources().getColor(R.color.white));
            tv_unAddRecord.setTextColor(getResources().getColor(R.color.blue_5e));
            tv_unAddRecord_line.setBackgroundColor(getResources().getColor(R.color.blue_5e));
            fragment = fragmentList.get(1);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }
}
