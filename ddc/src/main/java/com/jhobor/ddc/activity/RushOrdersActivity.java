package com.jhobor.ddc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.jhobor.ddc.R;
import com.jhobor.ddc.adapter.RushOrdersBaseAdapter;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.entity.Delivery;
import com.jhobor.ddc.utils.ErrorUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RushOrdersActivity extends AppCompatActivity implements View.OnClickListener, AbsListView.OnScrollListener {
    ImageView topArrow;
    TextView topTitle, done;
    ListView listView;

    AMapLocation aMapLocation;
    int page = 0;
    int pageLen = 5;// 分页获取店铺数据的每页数据量
    int op = 1;// 操作 0.刷新数据  1.加载更多数据
    List<Delivery> deliveryArrayList = new ArrayList<>();
    RushOrdersBaseAdapter adapter;
    boolean isNoMoreData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rush_orders);

        initView();
        handleEvt();
        getOrdersData();
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);
        done.setOnClickListener(this);
        listView.setOnScrollListener(this);
    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        topTitle = (TextView) findViewById(R.id.topTitle);
        done = (TextView) findViewById(R.id.done);
        listView = (ListView) findViewById(R.id.listView);

        Intent intent = getIntent();
        String title = intent.getStringExtra("thisTitle");
        topTitle.setText(title);
        done.setText("刷新");
        done.setVisibility(View.VISIBLE);
        aMapLocation = (AMapLocation) BaseApplication.dataMap.get("aMapLocation");
    }

    private void getOrdersData() {

        BaseApplication.iService.rushOrders(aMapLocation.getLatitude(), aMapLocation.getLongitude(), page).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray deliverieList = jsonObject.getJSONArray("deliverieList");
                    if (op == 0) {
                        deliveryArrayList.clear();
                    }
                    int i = 0;
                    for (; i < deliverieList.length(); i++) {
                        JSONArray jsonArray = deliverieList.getJSONArray(i);
                        int id = jsonArray.getInt(0);
                        String storeName = jsonArray.getString(1);
                        String storePhone = jsonArray.getString(2);
                        String title = jsonArray.getString(3);
                        double money = jsonArray.getDouble(4);
                        String fromAddr = jsonArray.getString(5);
                        String toAddr = jsonArray.getString(6);
                        int score = jsonArray.getInt(7);
                        String time = jsonArray.getString(8);
                        double lat = jsonArray.getDouble(9);
                        double lng = jsonArray.getDouble(10);

                        deliveryArrayList.add(new Delivery(id, 0, storeName, storePhone, title, (float) money, fromAddr, toAddr, score, time));
                    }
                    if (adapter == null) {
                        adapter = new RushOrdersBaseAdapter(deliveryArrayList, RushOrdersActivity.this);
                        listView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                        String content;
                        if (op == 0) {
                            content = "刷新完成";
                            isNoMoreData = false;
                        } else {
                            content = "加载完成";
                            if (i < pageLen) {
                                isNoMoreData = true;
                            }
                        }
                        Toast.makeText(getBaseContext(), content, Toast.LENGTH_SHORT).show();
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
            page = 0;
            op = 0;
            getOrdersData();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        int position = listView.getLastVisiblePosition();
        if (position == deliveryArrayList.size() - 1 && !isNoMoreData) {
            ++page;
            op = 1;
            getOrdersData();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
