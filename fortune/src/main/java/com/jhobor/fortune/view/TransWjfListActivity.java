package com.jhobor.fortune.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jhobor.fortune.R;
import com.jhobor.fortune.adapter.AddMoneyListAdapter;
import com.jhobor.fortune.adapter.CalculusListAdapter;
import com.jhobor.fortune.adapter.RecordListAdapter;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.entity.CalculusBean;
import com.jhobor.fortune.entity.RecordBean;
import com.jhobor.fortune.utils.BarUtil;
import com.jhobor.fortune.utils.HideIMEUtil;

import java.util.List;

/**
 * Author     Qijing
 * Created by YQJ on 2018/3/27.
 * Description:
 */
public class TransWjfListActivity extends AppCompatActivity implements BarUtil.RightClick {

    private ListView listView;
    private TextView mAdd;
    Context context;
    CalculusListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_list);
        HideIMEUtil.wrap(this);
        int tag = getIntent().getIntExtra("Tag", 0);
        switch (tag) {
            case 1:
                BarUtil.topBar(this, "微积分提现记录");
                break;
            case 0:
                BarUtil.topBar(this, "微积分转让记录");
                break;
            default:
                break;
        }
        listView = (ListView) findViewById(R.id.listView);
        context = this;
        mAdd = (TextView) findViewById(R.id.add);
        BarUtil.setmClick(this);
        adapter = new CalculusListAdapter(context);
        listView.setAdapter(adapter);
        getIntegralList(tag);
    }

    /**
     * 获取积分记录
     *
     * @param tag
     */
    private void getIntegralList(int tag) {
        String uuid = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.getalculusRecordList(uuid, tag).enqueue(new RetrofitCallback(getApplicationContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                JSONObject jsonObject = JSON.parseObject(data);
                int msg = jsonObject.getIntValue("msg");
                if (msg == 1) {
                    List<CalculusBean> beanList = JSON.parseArray(jsonObject.getString("crList"), CalculusBean.class);
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

    @Override
    public void click() {

    }
}
