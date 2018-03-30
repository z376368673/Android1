package com.jhobor.ddc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jhobor.ddc.R;
import com.jhobor.ddc.adapter.ShippingAddrBaseAdapter;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.entity.Addr;
import com.jhobor.ddc.utils.ErrorUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShippingAddrActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView topArrow;
    TextView topTitle, done;
    ListView listView;

    List<Addr> addrArrayList;
    ShippingAddrBaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_addr);

        initView();
        handleEvt();
        getShippingAddrData();
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);
        done.setOnClickListener(this);
    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        topTitle = (TextView) findViewById(R.id.topTitle);
        done = (TextView) findViewById(R.id.done);
        listView = (ListView) findViewById(R.id.listView);

        topTitle.setText("收货地址");
        done.setText("新增");
        done.setVisibility(View.VISIBLE);
    }

    private void getShippingAddrData() {
        String uuid = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.myAddr(uuid).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int isLogin = jsonObject.getInt("isLogin");
                    if (isLogin == 1) {
                        JSONArray addrList = jsonObject.getJSONArray("addrList");
                        addrArrayList = new ArrayList<Addr>();
                        for (int i = 0; i < addrList.length(); i++) {
                            JSONArray jsonArray = addrList.getJSONArray(i);
                            int id = jsonArray.getInt(0);
                            String userName = jsonArray.getString(1);
                            String phone = jsonArray.getString(2);
                            String addr = jsonArray.getString(3);
                            int state = jsonArray.getInt(4);
                            addrArrayList.add(new Addr(id, userName, phone, addr, state));
                        }
                        adapter = new ShippingAddrBaseAdapter(addrArrayList, ShippingAddrActivity.this);
                        listView.setAdapter(adapter);
                    }
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
        } else if (v == done) {
            Intent intent = new Intent(this, EditShippingAddrActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        Object object = BaseApplication.dataMap.get("addrArrayList");
        if (object != null) {
            addrArrayList = (List<Addr>) object;
            adapter = new ShippingAddrBaseAdapter(addrArrayList, this);
            listView.setAdapter(adapter);
            BaseApplication.dataMap.remove("addrArrayList");
        }
        super.onResume();
    }
}
