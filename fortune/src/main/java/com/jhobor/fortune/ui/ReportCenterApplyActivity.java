package com.jhobor.fortune.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.jhobor.fortune.R;
import com.jhobor.fortune.adapter.LowerAdapter;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.entity.LowerLevel;
import com.jhobor.fortune.utils.ErrorUtil;
import com.vincent.filepicker.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 *
 * 申请服务中心
 *
 * Created by zh on 2018/5/8.
 *
 */

public class ReportCenterApplyActivity extends BaseActivity {


    // Content View Elements
    TextView tv_membersNum;
    TextView tv_moneyNum;
    TextView tv_applyReport;
    TextView tv_applyReportText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_report_conter_apply);
        initTitle();
        titleView.setText("服务中心");
        initView();
        getData();
    }

    private void initView() {
        tv_membersNum = (TextView) findViewById(R.id.tv_membersNum);
        tv_moneyNum = (TextView) findViewById(R.id.tv_moneyNum);
        tv_applyReport = (TextView) findViewById(R.id.tv_applyReport);
        tv_applyReportText = (TextView) findViewById(R.id.tv_applyReportText);

        tv_applyReport.setOnClickListener(this);
        tv_applyReport.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        if (view==tv_applyReport){
            apply();
        }
    }

    //获取数据
    private void getData() {
        dialog.show();
        String token = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.applyChecked(token).enqueue(new RetrofitCallback(context, new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int msg = jsonObject.getInt("msg");
                    if (msg == 1) {

                        int countDirectNop = jsonObject.getInt("countDirectNop");
                        int countDirectMoney = jsonObject.getInt("countDirectMoney");
                        tv_membersNum.setText(countDirectNop+"人");
                        tv_moneyNum.setText(countDirectMoney+"元");
                        if (countDirectNop>=10||countDirectMoney>=20000){
                            //如果达到申请条件 则更换按钮背景
                            tv_applyReport.setBackgroundResource(R.drawable.submmit_shape);
                            tv_applyReport.setEnabled(true);
                            tv_applyReportText.setVisibility(View.INVISIBLE);
                        }else {
                            tv_applyReportText.setVisibility(View.VISIBLE);
                            String str =  tv_applyReportText.getText().toString();
                            str = String.format(str,10-countDirectNop,20000-countDirectMoney);
                            tv_applyReportText.setText(str);
                            tv_applyReport.setEnabled(false);
                        }
                        //差8人/1000元可成为服务中心
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

    //申请
    private void apply() {
        dialog.show();
        String token = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.applyAdd(token).enqueue(new RetrofitCallback(context, new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int msg = jsonObject.getInt("msg");
                    if (msg == 1) {
                        ToastUtil.getInstance(act).showToast("申请成功，等待审核中...");
                        Intent intent = new Intent(act,ReportCenterApplyingActivity.class);
                        startActivity(intent);
                        finish();
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



}
