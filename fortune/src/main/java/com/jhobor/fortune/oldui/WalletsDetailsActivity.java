package com.jhobor.fortune.oldui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jhobor.fortune.R;
import com.jhobor.fortune.adapter.WalletsAdapter;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.entity.Income;
import com.jhobor.fortune.entity.Pager;
import com.jhobor.fortune.utils.BarUtil;
import com.jhobor.fortune.utils.ErrorUtil;
import com.jhobor.fortune.utils.HideIMEUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WalletsDetailsActivity extends AppCompatActivity implements BaseQuickAdapter.RequestLoadMoreListener {
    RecyclerView recyclerView;
    List<Income> incomeList = new ArrayList<>();
    Pager pager;
    WalletsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallets_details);
        HideIMEUtil.wrap(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        BarUtil.topBar(this, "钱包明细");
        if (adapter == null) {
            pager = new Pager(15);
            getData();
        } else {
            setData();
        }
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
        Log.i(">>", String.valueOf(pager.getPage()));
        BaseApplication.iService.walletsDetails(token, pager.getPage()).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int isLogin = jsonObject.getInt("isLogin");
                    if (isLogin == 1) {
                        JSONArray recordList = jsonObject.getJSONArray("recordList");
                        int length = recordList.length();
                        if (pager.getMode() == Pager.MODE_REFRESH)
                            incomeList.clear();
                        for (int i = 0; i < length; i++) {
                            JSONArray jsonArray = recordList.getJSONArray(i);
                            int id = jsonArray.getInt(0);
                            String date = jsonArray.getString(1);
                            float money = (float) jsonArray.getDouble(2);
                            String status = jsonArray.getString(3);
                            incomeList.add(new Income(id, date, money, 0, status));
                        }
                        if (adapter == null) {
                            adapter = new WalletsAdapter(R.layout.item_wallets, incomeList);
                            adapter.setEnableLoadMore(true);
                            adapter.setOnLoadMoreListener(WalletsDetailsActivity.this, recyclerView);
                            setData();
                        } else {
                            adapter.notifyDataSetChanged();
                            if (adapter.isLoading())
                                adapter.loadMoreComplete();
                            Toast.makeText(WalletsDetailsActivity.this, "加载完成", Toast.LENGTH_SHORT).show();
                        }
                        if (length < pager.getPageCount()) {
                            pager.setNoMoreData(true);
                            adapter.loadMoreEnd();
                        }
                    } else {
                        Toast.makeText(WalletsDetailsActivity.this, "账户信息失效，无法获取数据", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(WalletsDetailsActivity.this, e);
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
