package com.jhobor.ddc.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.ddc.R;
import com.jhobor.ddc.adapter.GoodsManageBaseAdapter;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.entity.Goods;
import com.jhobor.ddc.entity.Pager;
import com.jhobor.ddc.utils.ErrorUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoodsManageActivity extends AppCompatActivity implements View.OnClickListener, AbsListView.OnScrollListener {
    ImageView topArrow;
    TextView topTitle;
    ListView listView;

    Pager pager;
    int categoryId = 0;
    Map<String, Integer> categoryMap = new HashMap<>();
    List<Goods> goodsArrayList = new ArrayList<>();
    GoodsManageBaseAdapter adapter;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            pager.setPage(0);
            pager.setOp(0);
            getGoodsData();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_manage);

        initView();
        handleEvt();
        pager = new Pager(5);
        getGoodsData();
        registerReceiver(receiver, new IntentFilter("refreshGoodsList"));
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);
        listView.setOnScrollListener(this);
    }

    private void getGoodsData() {
        String uuid = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.storeGoods(uuid, pager.getPage(), categoryId).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int msg = jsonObject.getInt("msg");
                    if (msg == 1) {
                        if (pager.getOp() == 0) {
                            goodsArrayList.clear();
                            pager.setNoMoreData(false);
                        }
                        JSONArray categoryList = jsonObject.getJSONArray("categoryList");
                        JSONArray goodsList = jsonObject.getJSONArray("goodsList");
                        for (int i = 0; i < categoryList.length(); i++) {
                            JSONArray jsonArray = categoryList.getJSONArray(i);
                            int categoryId = jsonArray.getInt(0);
                            String categoryName = jsonArray.getString(1);
                            categoryMap.put(categoryName, categoryId);
                        }
                        int j = 0;
                        for (; j < goodsList.length(); j++) {
                            JSONArray jsonArray = goodsList.getJSONArray(j);
                            int goodsId = jsonArray.getInt(0);
                            String goodsName = jsonArray.getString(1);
                            double price = jsonArray.getDouble(2);
                            String picture = BaseApplication.BASE_URL + jsonArray.getString(3);
                            int count = jsonArray.getInt(4);
                            goodsArrayList.add(new Goods(goodsId, goodsName, picture, (float) price, 0, count));
                        }
                        if (adapter == null) {
                            adapter = new GoodsManageBaseAdapter(goodsArrayList, GoodsManageActivity.this);
                            listView.setAdapter(adapter);
                        } else {
                            String content = "加载完成";
                            if (pager.getOp() == 0) {
                                content = "刷新完成";
                            }
                            adapter.notifyDataSetChanged();
                            Toast.makeText(GoodsManageActivity.this, content, Toast.LENGTH_SHORT).show();
                        }
                        if (j < pager.getPageLen()) {
                            pager.setNoMoreData(true);
                        }
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                }
            }
        }));
    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        topTitle = (TextView) findViewById(R.id.topTitle);
        listView = (ListView) findViewById(R.id.listView);

        topTitle.setText("产品管理");
    }

    @Override
    public void onClick(View v) {
        if (v == topArrow) {
            finish();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        int position = listView.getLastVisiblePosition();
        if (position == goodsArrayList.size() - 1 && !pager.isNoMoreData()) {
            pager.setPage(pager.getPage() + 1);
            pager.setOp(1);
            getGoodsData();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
