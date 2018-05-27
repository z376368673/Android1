package com.jhobor.fortune.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.jhobor.fortune.R;
import com.jhobor.fortune.adapter.ReportRecordAdapter;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.entity.BillRecordBean;
import com.jhobor.fortune.utils.ErrorUtil;
import java.util.List;

/**
 *
 * 服务积分增长记录
 *
 * Created by zh on 2018/5/8.
 *
 */

public class ReportRecordActivity extends BaseActivity {

    private RecyclerView recyclerview;
    private TextView tv_text1;

    ReportRecordAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_report_add_record);
        initTitle();
        titleView.setText("服务积分增长记录");
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        tv_text1 = (TextView) findViewById(R.id.tv_text1);
        getData();
    }

    private void getData() {
        dialog.show();
        String token = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.billIntegralRecord(token).enqueue(new RetrofitCallback(context, new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = JSON.parseObject(data);
                    int msg = jsonObject.getIntValue("msg");
                    String str = jsonObject.getString("billGroupInfoRecordList");
                    String enrolmentTime = jsonObject.getString("enrolmentTime");
                    enrolmentTime = enrolmentTime.substring(0,enrolmentTime.lastIndexOf(" "));
                    if (msg == 1) {
                        //格式化描述 把获取的日期挤进去
                        enrolmentTime = String.format(tv_text1.getText().toString(),enrolmentTime);
                        tv_text1.setText(enrolmentTime);
                        List<BillRecordBean> list = JSON.parseArray(str,BillRecordBean.class);
                        adapter = new ReportRecordAdapter(R.layout.item_report_record,list);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(act);
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerview.setLayoutManager(layoutManager);
                        recyclerview.setAdapter(adapter);

                    } else {
                        data =  jsonObject.getString("errorInfo");
                        Toast.makeText(context, data, Toast.LENGTH_SHORT).show();
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
