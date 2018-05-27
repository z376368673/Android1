package com.jhobor.fortune.oldui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jhobor.fortune.R;
import com.jhobor.fortune.adapter.AddMoneyListAdapter;
import com.jhobor.fortune.adapter.BankListAdapter;
import com.jhobor.fortune.adapter.RecordListAdapter;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.entity.AddMoneyBean;
import com.jhobor.fortune.entity.BankBean;
import com.jhobor.fortune.entity.RecordBean;
import com.jhobor.fortune.utils.BarUtil;
import com.jhobor.fortune.utils.HideIMEUtil;

import org.json.JSONException;

import java.util.List;

/**
 * Author     Qijing
 * Created by YQJ on 2018/3/27.
 * Description:
 */
public class TransListActivity extends AppCompatActivity implements BarUtil.RightClick {


    private ListView listView;
    private TextView mAdd;
    Context context;
    RecordListAdapter adapter;
    AddMoneyListAdapter adapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_list);
        HideIMEUtil.wrap(this);
        int tag = getIntent().getIntExtra("Tag", 0);
        int Type = getIntent().getIntExtra("Type", 0);
        if (Type == 0) {
            switch (tag) {
                case 0:
                    BarUtil.topBar(this, "增资记录");
                    break;
                case 1:
                    BarUtil.topBar(this, "日分红记录");
                    break;
                default:
                    break;
            }
        } else {
            switch (tag) {
                case 0:
                    BarUtil.topBar(this, "积分增长记录");
                    break;
                case 1:
                    BarUtil.topBar(this, "积分提现记录");
                    break;
                case 2:
                    BarUtil.topBar(this, "积分转让记录");
                    break;
                default:
                    break;
            }
        }
        listView = (ListView) findViewById(R.id.listView);
        context = this;
        mAdd = (TextView) findViewById(R.id.add);
        BarUtil.setmClick(this);
        adapter = new RecordListAdapter(context);
        adapter1 = new AddMoneyListAdapter(context);
        listView.setAdapter(adapter);
        if (Type == 0){
            listView.setAdapter(adapter1);
            getMoneyList(tag);
        }else{
            listView.setAdapter(adapter);
            getIntegralList(tag);
        }

    }

    /**
     * 获取积分记录
     *
     * @param tag
     */
    private void getIntegralList(int tag) {
        String uuid = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.getIntegralList(uuid, tag).enqueue(new RetrofitCallback(getApplicationContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                JSONObject jsonObject = JSON.parseObject(data);
                int msg = jsonObject.getIntValue("msg");
                if (msg == 1) {
                    List<RecordBean> beanList = JSON.parseArray(jsonObject.getString("irList"), RecordBean.class);
                    if (beanList.size() > 0) {
                        adapter.clear();
                        adapter.addAll(beanList);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(context, jsonObject.getString("errorInfo"), Toast.LENGTH_SHORT).show();
                }
            }
        }));
    }


    /**
     * 获取金额记录
     *
     * @param tag
     */
    private void getMoneyList(int tag) {
        String uuid = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.getRecordlList(uuid, tag).enqueue(new RetrofitCallback(getApplicationContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                JSONObject jsonObject = JSON.parseObject(data);
                int msg = jsonObject.getIntValue("msg");
                if (msg == 1) {
                    List<AddMoneyBean> beanList = JSON.parseArray(jsonObject.getString("arList"), AddMoneyBean.class);
                    if (beanList!=null&&beanList.size() > 0) {
                        adapter1.clear();
                        adapter1.addAll(beanList);
                        adapter1.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(context, jsonObject.getString("errorInfo"), Toast.LENGTH_SHORT).show();
                }
            }
        }));
    }

    @Override
    public void click() {

    }
}
