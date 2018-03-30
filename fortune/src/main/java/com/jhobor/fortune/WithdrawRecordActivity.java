package com.jhobor.fortune;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jhobor.fortune.adapter.WithdrawAdapter;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.entity.Pager;
import com.jhobor.fortune.entity.Withdraw;
import com.jhobor.fortune.utils.BarUtil;
import com.jhobor.fortune.utils.ErrorUtil;
import com.jhobor.fortune.utils.HideIMEUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WithdrawRecordActivity extends AppCompatActivity implements BaseQuickAdapter.RequestLoadMoreListener {
    RecyclerView recyclerView;
    List<Withdraw> withdrawList = new ArrayList<>();
    Pager pager;
    WithdrawAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_record);
        HideIMEUtil.wrap(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        BarUtil.topBar(this, "提现记录");
        pager = new Pager(15);
        getData();
    }

    private void setData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        String token = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.withdrawRecord(token, pager.getPage()).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray withdrawArr = jsonObject.getJSONArray("withdrawList");
                    int length = withdrawArr.length();
                    for (int i = 0; i < length; i++) {
                        JSONArray jsonArray = withdrawArr.getJSONArray(i);
                        String date = jsonArray.getString(0);
                        int money = (int) jsonArray.getDouble(1);
                        int state = jsonArray.getInt(2);
                        String way = jsonArray.getString(3);
                        String status = "未处理";
                        if (state == 1) {
                            status = "已完成";
                        }
                        withdrawList.add(new Withdraw(0, date, money, state, status, way));
                    }
                    if (adapter == null) {
                        adapter = new WithdrawAdapter(R.layout.item_withdraw_record, withdrawList);
                        adapter.setEnableLoadMore(true);
                        adapter.setOnLoadMoreListener(WithdrawRecordActivity.this, recyclerView);
                        setData();
                    } else {
                        adapter.notifyDataSetChanged();
                        adapter.loadMoreComplete();
                        Toast.makeText(WithdrawRecordActivity.this, "加载完成", Toast.LENGTH_SHORT).show();
                    }
                    if (length < pager.getPageCount()) {
                        adapter.loadMoreEnd();
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(WithdrawRecordActivity.this, e);
                }
            }
        }));
    }

    @Override
    public void onLoadMoreRequested() {
        pager.next();
        getData();
    }
}
