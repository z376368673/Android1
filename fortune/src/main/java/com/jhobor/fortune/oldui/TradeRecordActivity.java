package com.jhobor.fortune.oldui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.jhobor.fortune.R;
import com.jhobor.fortune.adapter.FinanceBaseQuickAdapter;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.entity.Finance;
import com.jhobor.fortune.utils.BarUtil;
import com.jhobor.fortune.utils.ErrorUtil;
import com.jhobor.fortune.utils.HideIMEUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TradeRecordActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Finance> financeList = new ArrayList<>();
    FinanceBaseQuickAdapter quickAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_record);
        HideIMEUtil.wrap(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        BarUtil.topBar(this, "交易记录");
        getData();
    }

    private void getData() {
        String token = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.tradeRecord(token).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int isLogin = jsonObject.getInt("isLogin");
                    if (isLogin == 0) {
                        Toast.makeText(TradeRecordActivity.this, "账户信息失效，无法获取数据", Toast.LENGTH_SHORT).show();
                    } else if (isLogin == 1) {
                        JSONArray orderList = jsonObject.getJSONArray("orderList");
                        int length = orderList.length();
                        for (int i = 0; i < length; i++) {
                            JSONArray jsonArray = orderList.getJSONArray(i);
                            int id = jsonArray.getInt(0);
                            double capital = jsonArray.getDouble(1);
                            double interest = jsonArray.getDouble(2);
                            double total = jsonArray.getDouble(3);
                            String date = jsonArray.getString(4);
                            int state = jsonArray.getInt(5);
                            String status = "未付款";
                            if (state == 1) {
                                status = "理财中";
                            }
                            financeList.add(new Finance(id, (float) capital, (float) interest, (float) total, date, state, status));
                        }
                        quickAdapter = new FinanceBaseQuickAdapter(R.layout.item_finance, financeList);
                        setData();
                    }

                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(TradeRecordActivity.this, e);
                }
            }
        }));
    }

    private void setData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(quickAdapter);
    }
}
