package com.jhobor.fortune.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jhobor.fortune.R;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.ui.fragment.ReportCenterFragment;
import com.jhobor.fortune.ui.fragment.TeamFragment;
import com.jhobor.fortune.utils.ErrorUtil;
import com.vincent.filepicker.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 服务中心
 *
 * Created by zh on 2018/5/8.
 *
 */

public class ReportCenterActivity extends BaseActivity {


    private TextView tv_countNum;
    private TextView tv_newNum;
    private TextView tv_countjf;
    private TextView tv_newjf;
    private LinearLayout layout_reportRecord;
    private LinearLayout layout_teamDetailed;
    private TextView tv_reportCenter;
    private View tv_reportCenter_line;
    private TextView tv_unreportCenter;
    private View tv_unreportCenter_line;
    private FrameLayout frameLayout;

    List<Fragment> fragmentList = new ArrayList<Fragment>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_report_conter);
        initTitle();
        titleView.setText("服务中心");
        bindViews();
        initFragment();
        getData();
    }

    //查询状态
    private void getData() {
        dialog.show();
        String token = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.billCenterHome(token).enqueue(new RetrofitCallback(context, new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int msg = jsonObject.getInt("msg");
                    if (msg == 1) {
                        String countChild = jsonObject.getString("countChild");
                        String nowDateChild = jsonObject.getString("nowDateChild");
                        String countBillIntegral = jsonObject.getString("countBillIntegral");
                        String nowDateBillIntegral = jsonObject.getString("nowDateBillIntegral");
                        tv_countNum.setText(countChild+"人");
                        tv_newNum.setText(nowDateChild+"人");
                        tv_countjf.setText(countBillIntegral);
                        tv_newjf.setText(nowDateBillIntegral);
                    }else {
                        String errorInfo = jsonObject.getString("errorInfo");
                        ToastUtil.getInstance(act).showToast(errorInfo);
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(context, e);
                }finally {
                    dialog.dismiss();
                }
            }
        }));
    }

    private void bindViews() {
        tv_countNum = (TextView) findViewById(R.id.tv_countNum);
        tv_newNum = (TextView) findViewById(R.id.tv_newNum);
        tv_countjf = (TextView) findViewById(R.id.tv_countjf);
        tv_newjf = (TextView) findViewById(R.id.tv_newjf);
        layout_reportRecord = (LinearLayout) findViewById(R.id.layout_reportRecord);
        layout_teamDetailed = (LinearLayout) findViewById(R.id.layout_teamDetailed);
        tv_reportCenter = (TextView) findViewById(R.id.tv_reportCenter);
        tv_reportCenter_line = (View) findViewById(R.id.tv_reportCenter_line);
        tv_unreportCenter = (TextView) findViewById(R.id.tv_unreportCenter);
        tv_unreportCenter_line = (View) findViewById(R.id.tv_unreportCenter_line);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);

        tv_reportCenter.setOnClickListener(this);
        tv_unreportCenter.setOnClickListener(this);
        layout_reportRecord.setOnClickListener(this);
        layout_teamDetailed.setOnClickListener(this);
    }
    private void initFragment() {
        ReportCenterFragment projectNFragment = new ReportCenterFragment();
        ReportCenterFragment projectPFragment = new ReportCenterFragment();
        fragmentList.add(projectNFragment);
        fragmentList.add(projectPFragment);

        ReportCenterFragment.TYPE=0;
        selectTab(0);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (tv_reportCenter==view){
            ReportCenterFragment.TYPE=0;
            selectTab(0);
        }else if (tv_unreportCenter==view){
            ReportCenterFragment.TYPE=1;
            selectTab(1);
        }else if (layout_reportRecord==view){
            //服务积分记录
            startAct(ReportRecordActivity.class);
        }else if (layout_teamDetailed==view){
            //团队投资明细
            startAct(TeamDetailedActivity.class);
        }
    }

    private void selectTab(int indexOfChild) {
        Fragment fragment;
        if (indexOfChild==0){
            tv_reportCenter.setTextColor(getResources().getColor(R.color.blue_5e));
            tv_reportCenter_line.setBackgroundColor(getResources().getColor(R.color.blue_5e));
            tv_unreportCenter.setTextColor(getResources().getColor(R.color.black_66));
            tv_unreportCenter_line.setBackgroundColor(getResources().getColor(R.color.white));
            fragment = fragmentList.get(0);
        }else {
            tv_reportCenter.setTextColor(getResources().getColor(R.color.black_66));
            tv_reportCenter_line.setBackgroundColor(getResources().getColor(R.color.white));
            tv_unreportCenter.setTextColor(getResources().getColor(R.color.blue_5e));
            tv_unreportCenter_line.setBackgroundColor(getResources().getColor(R.color.blue_5e));
            fragment = fragmentList.get(1);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }

}
