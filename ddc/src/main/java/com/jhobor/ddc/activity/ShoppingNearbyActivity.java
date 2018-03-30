package com.jhobor.ddc.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.jhobor.ddc.R;
import com.jhobor.ddc.adapter.NearbyAdapter;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.entity.Store;
import com.jhobor.ddc.utils.ErrorUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShoppingNearbyActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, XRecyclerView.LoadingListener {
    TextView closest, best, popular, search, topTitle;
    ImageView topArrow, closestCursor, bestCursor, popularCursor;
    XRecyclerView xRecyclerView;

    NearbyAdapter adapter;
    List<List<Store>> storeArrayLists;
    List<View> tabBottomLineList = new ArrayList<>();
    List<TextView> tabTextViewList = new ArrayList<>();
    int curPos = 0;//第几个选项卡
    int moduleId;
    int tag = 1;// 获取店铺的参数：1.评分排序（好评）  2.销量排序（人气）
    int pageLen = 10;// 分页获取店铺数据的每页数据量
    int op = 1;// 操作 0.刷新数据  1.加载更多数据
    List<Integer> pages = new ArrayList<>();
    List<Boolean> noMoreDataList = new ArrayList<>();
    AMapLocation aMapLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_nearby);

        initView();
        handleEvt();
        getStoreData();
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);
        closest.setOnClickListener(this);
        best.setOnClickListener(this);
        popular.setOnClickListener(this);

        xRecyclerView.setLoadingListener(this);

    }

    private void initView() {
        closest = (TextView) findViewById(R.id.closest);
        best = (TextView) findViewById(R.id.best);
        popular = (TextView) findViewById(R.id.popular);
        search = (TextView) findViewById(R.id.search);
        topTitle = (TextView) findViewById(R.id.topTitle);
        topArrow = (ImageView) findViewById(R.id.topArrow);
        closestCursor = (ImageView) findViewById(R.id.closestCursor);
        bestCursor = (ImageView) findViewById(R.id.bestCursor);
        popularCursor = (ImageView) findViewById(R.id.popularCursor);
        xRecyclerView = (XRecyclerView) findViewById(R.id.xRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setLayoutManager(layoutManager);
        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.x_recycler_view_divider);
        xRecyclerView.addItemDecoration(xRecyclerView.new DividerItemDecoration(dividerDrawable));
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setArrowImageView(R.mipmap.iconfont_downgrey);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(true);

        addTabs();
        Intent intent = getIntent();
        String thisTitle = intent.getStringExtra("thisTitle");
        moduleId = intent.getIntExtra("moduleId", 0);
        topTitle.setText(thisTitle);
        aMapLocation = (AMapLocation) BaseApplication.dataMap.get("aMapLocation");
        storeArrayLists = new ArrayList<List<Store>>();
        for (int i = 0; i < 3; i++) {
            pages.add(0);
            noMoreDataList.add(false);
            storeArrayLists.add(new ArrayList<Store>());
        }

    }

    private void addTabs() {
        tabTextViewList.add(closest);
        tabTextViewList.add(best);
        tabTextViewList.add(popular);

        tabBottomLineList.add(closestCursor);
        tabBottomLineList.add(bestCursor);
        tabBottomLineList.add(popularCursor);
    }

    private void getStoreData() {
        final AMapLocation aMapLocation = (AMapLocation) BaseApplication.dataMap.get("aMapLocation");
        BaseApplication.iService.shopNearby(moduleId, aMapLocation.getLongitude(), aMapLocation.getLatitude(), pages.get(curPos), tag).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray storeList = jsonObject.getJSONArray("storeList");
                    if (op == 0) {
                        storeArrayLists.get(curPos).clear();
                    }
                    int i = 0;
                    for (; i < storeList.length(); i++) {
                        JSONArray jsonArray = storeList.getJSONArray(i);
                        int id = jsonArray.getInt(0);
                        String storeName = jsonArray.getString(1);
                        String picture = BaseApplication.BASE_URL + jsonArray.getString(2);
                        double scores = jsonArray.getDouble(3);
                        double lat = jsonArray.getDouble(4);
                        double lng = jsonArray.getDouble(5);
                        String addr = jsonArray.getString(6);
                        storeArrayLists.get(curPos).add(new Store(id, storeName, picture, addr, AMapUtils.calculateLineDistance(new LatLng(lat, lng), new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()))));
                    }

                    if (adapter == null) {
                        adapter = new NearbyAdapter(storeArrayLists.get(curPos), ShoppingNearbyActivity.this);
                        xRecyclerView.setAdapter(adapter);
                    } else {
                        String str;
                        if (op == 0) {
                            str = "刷新完成";
                            noMoreDataList.set(curPos, false);
                            xRecyclerView.refreshComplete();
                        } else {
                            str = "加载完成";
                            if (i < pageLen) {
                                xRecyclerView.setNoMore(true);
                                noMoreDataList.set(curPos, true);
                            } else {
                                xRecyclerView.loadMoreComplete();
                            }
                        }
                        adapter.setDatas(storeArrayLists.get(curPos));
                        adapter.notifyDataSetChanged();
                        Toast.makeText(ShoppingNearbyActivity.this, str, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                }
            }
        }));

    }

    @Override
    public void onClick(View v) {
        if (v == closest) {
            changeTab(0);
        } else if (v == best) {
            changeTab(1);
        } else if (v == popular) {
            changeTab(2);
        } else if (v == topArrow) {
            finish();
        }
    }

    private void changeTab(int newPos) {
        if (newPos != curPos) {
            tabTextViewList.get(curPos).setTextColor(ContextCompat.getColor(this, R.color.textGray));
            tabTextViewList.get(newPos).setTextColor(ContextCompat.getColor(this, R.color.redPink));

            tabBottomLineList.get(curPos).setVisibility(View.INVISIBLE);
            tabBottomLineList.get(newPos).setVisibility(View.VISIBLE);
            curPos = newPos;
            if (newPos == 1) {
                tag = 1;
            } else if (newPos == 2) {
                tag = 2;
            }
            if (storeArrayLists.get(curPos).size() == 0) {
                xRecyclerView.refresh();
            } else {
                xRecyclerView.setNoMore(noMoreDataList.get(curPos));
                adapter.setDatas(storeArrayLists.get(curPos));
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, StoreActivity.class);
        intent.putExtra("storeId", storeArrayLists.get(curPos).get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        pages.set(curPos, 0);
        op = 0;
        getStoreData();
    }

    @Override
    public void onLoadMore() {
        Integer integer = pages.get(curPos);
        pages.set(curPos, integer + 1);
        op = 1;
        getStoreData();
    }

}
