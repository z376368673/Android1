package com.jhobor.ddc.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.jhobor.ddc.R;
import com.jhobor.ddc.adapter.MySentOrdersXRecyclerViewAdapter;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.entity.Delivery;
import com.jhobor.ddc.utils.ErrorUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MySentOrdersActivity extends AppCompatActivity implements View.OnClickListener, XRecyclerView.LoadingListener {
    ImageView topArrow;
    TextView topTitle;
    XRecyclerView xRecyclerView;

    List<Delivery> deliveryList = new ArrayList<>();
    MySentOrdersXRecyclerViewAdapter adapter;
    int state = 0;// 0.刷新数据  1.加载更多数据
    int page = 0;//第几页
    int pageLen = 10;//每页数据量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sent_orders);

        initView();
        handleEvt();
        getDeliveryData(page);
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);
        xRecyclerView.setLoadingListener(this);
    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        topTitle = (TextView) findViewById(R.id.topTitle);
        xRecyclerView = (XRecyclerView) findViewById(R.id.xRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setLayoutManager(layoutManager);
        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.x_recycler_view_divider);
        xRecyclerView.addItemDecoration(xRecyclerView.new DividerItemDecoration(dividerDrawable));
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setArrowImageView(R.mipmap.iconfont_downgrey);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setPullRefreshEnabled(true);

        topTitle.setText("我的派送单");
    }

    private void getDeliveryData(int page) {
        String uuid = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.myDelivery(uuid, page).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    if (state == 0) {
                        deliveryList.clear();
                    }
                    JSONObject jsonObject = new JSONObject(data);
                    int isLogin = jsonObject.getInt("isLogin");
                    if (isLogin == 1) {
                        JSONArray dispatchList = jsonObject.getJSONArray("dispatchList");
                        int i = 0;
                        for (; i < dispatchList.length(); i++) {
                            JSONArray jsonArray = dispatchList.getJSONArray(i);
                            String storeName = jsonArray.getString(0);
                            String storePhone = jsonArray.getString(1);
                            String title = jsonArray.getString(2);
                            double money = jsonArray.getDouble(3);
                            String fromAddr = jsonArray.getString(4);
                            String toAddr = jsonArray.getString(5);
                            int score = jsonArray.getInt(6);
                            String employer = jsonArray.getString(7);
                            String employerPhone = jsonArray.getString(8);
                            String time = jsonArray.getString(9);
                            // int id, int storeId, String storeName, String storePhone, String task, float money, String fromAddr, String toAddr, int reputationScores, String minTime,String employerPhone
                            deliveryList.add(new Delivery(0, 0, storeName, storePhone, title, (float) money, fromAddr, toAddr, score, time, employerPhone));
                        }
                        if (adapter == null) {
                            adapter = new MySentOrdersXRecyclerViewAdapter(deliveryList, MySentOrdersActivity.this);
                            xRecyclerView.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                            String content;
                            if (state == 0) {
                                content = "刷新完成";
                                xRecyclerView.refreshComplete();
                            } else {
                                content = "加载完成";
                                if (i < pageLen) {
                                    xRecyclerView.setNoMore(true);
                                } else {
                                    xRecyclerView.loadMoreComplete();
                                }
                            }
                            Toast.makeText(MySentOrdersActivity.this, content, Toast.LENGTH_SHORT).show();

                        }
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
        }
    }

    @Override
    public void onRefresh() {
        state = 0;
        page = 0;
        getDeliveryData(page);
    }

    @Override
    public void onLoadMore() {
        state = 1;
        ++page;
        getDeliveryData(page);
        Log.i("onLoadMore page>>", String.valueOf(page));
    }
}
