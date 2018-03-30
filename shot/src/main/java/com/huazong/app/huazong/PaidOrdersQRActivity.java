package com.huazong.app.huazong;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ListView;

import com.huazong.app.huazong.adapter.OrderUnusedBaseAdapter;
import com.huazong.app.huazong.base.BarUtil;
import com.huazong.app.huazong.base.BaseApplication;
import com.huazong.app.huazong.base.RetrofitCallback;
import com.huazong.app.huazong.entity.Global;
import com.huazong.app.huazong.entity.Order;
import com.huazong.app.huazong.utils.ErrorUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PaidOrdersQRActivity extends AppCompatActivity {
    ListView storesView;

    private int depth;
    ArrayList<Order> unuseOrders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_paid_orders_qr);

        storesView = (ListView) findViewById(R.id.storesView);
        BarUtil.topBar(this,"已订购订单");
        getData();
    }
    private void getData() {
        Intent intent = getIntent();
        String orderNo = intent.getStringExtra("orderNo");
        String openid = (String) BaseApplication.dataMap.get("openid");
        int arrowOrGun = (int) BaseApplication.dataMap.get("arrowOrGun");
        BaseApplication.iService.justBuyOrderList(openid,arrowOrGun,orderNo).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    depth = jsonObject.getInt("pass");
                    int userId = jsonObject.getInt("userId");
                    Global.setUserid(userId);
                    JSONArray renList = jsonObject.getJSONArray("rentedList");
                    unuseOrders = new ArrayList<>();
                    for (int i = 0; i < renList.length(); i++) {
                        JSONArray rend = renList.getJSONArray(i);
                        int id = rend.getInt(0);
                        int byway = rend.getInt(1);
                        String date = rend.getString(2);
                        String time = rend.getString(3);
                        int type = rend.getInt(4);
                        int storeId = rend.getInt(5);
                        String name = rend.getString(6);
                        Order order = new Order(id, name, false, date, time, null, byway, "", type, storeId);
                        unuseOrders.add(order);
                    }
                    OrderUnusedBaseAdapter adapter = new OrderUnusedBaseAdapter(getBaseContext(), getLayoutInflater(), unuseOrders, depth);
                    storesView.setAdapter(adapter);
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getBaseContext(),e);
                }
            }
        }));
    }
}
