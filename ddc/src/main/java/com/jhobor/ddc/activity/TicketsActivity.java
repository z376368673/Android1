package com.jhobor.ddc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jhobor.ddc.R;
import com.jhobor.ddc.adapter.TicketBaseAdapter;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.entity.Ticket;
import com.jhobor.ddc.utils.ErrorUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TicketsActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView topArrow;
    TextView topTitle;
    ListView listView;

    List<Ticket> ticketList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);

        Intent intent = getIntent();
        float total = intent.getFloatExtra("total", 0);
        int storeId = intent.getIntExtra("storeId", 0);
        initView();
        handleEvt();
        getTicketData(total, storeId);
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);
    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        topTitle = (TextView) findViewById(R.id.topTitle);
        listView = (ListView) findViewById(R.id.listView);

        topTitle.setText("我的券包");
    }

    private void getTicketData(final float total, final int storeId) {
        String uuid = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.myTickets(uuid).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    ticketList = new ArrayList<Ticket>();
                    JSONObject jsonObject = new JSONObject(data);
                    int isLogin = jsonObject.getInt("isLogin");
                    JSONArray ucList = jsonObject.getJSONArray("ucList");
                    for (int i = 0; i < ucList.length(); i++) {
                        JSONArray jsonArray = ucList.getJSONArray(i);
                        int storeId = jsonArray.getInt(0);
                        double restrictMoney = jsonArray.getDouble(1);
                        double money = jsonArray.getDouble(2);
                        String storeName = jsonArray.getString(3);
                        int status = jsonArray.getInt(4);
                        String time = jsonArray.getString(5);
                        ticketList.add(new Ticket(0, storeId, (float) money, (float) restrictMoney, time, "", storeName, status));
                    }

                    listView.setAdapter(new TicketBaseAdapter(ticketList, TicketsActivity.this, total, storeId));
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                }
            }
        }));
    }

    @Override
    public void onClick(View v) {
        if (v == topArrow) {
            finish();
        }
    }
}
