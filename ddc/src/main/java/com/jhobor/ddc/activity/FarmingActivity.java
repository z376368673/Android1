package com.jhobor.ddc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.jhobor.ddc.R;
import com.jhobor.ddc.adapter.FarmingBaseAdapter;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.entity.Product;
import com.jhobor.ddc.utils.ErrorUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FarmingActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, AbsListView.OnScrollListener {
    ImageView topArrow, townCursor, bestCursor, popularCursor;
    TextView topTitle, best, popular;
    Spinner spinner;
    ListView listView;

    FarmingBaseAdapter adapter;
    int moduleId;
    List<ImageView> cursorList = new ArrayList<>();
    List<TextView> tabList = new ArrayList<>();
    Map<String, Integer> cityMap = new HashMap<>();
    List<String> cityNames = new ArrayList<>();
    int curPos = 1;
    int pageLen = 6;
    int op = 1;
    int curCity = 0;
    int tag = 0;// 0.好评优先  1.人气优先（销量排序）
    List<Integer> pages = new ArrayList<>();
    List<List<Product>> productArrayLists = new ArrayList<List<Product>>();
    List<Boolean> isNoMoreDataList = new ArrayList<>();
    AMapLocation aMapLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farming);

        initView();
        handleEvt();
        changeTab(0);
        getCityData();
        /*spinner初始化完数据后会触发选中事件onItemSelected，所以下面语句不用了*/
//        getProductData();
    }

    private void getCityData() {
        BaseApplication.iService.listCity().enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray cityList = jsonObject.getJSONArray("cityList");
                    cityMap.put("全国", 0);
                    cityNames.add("全国");
                    for (int i = 0; i < cityList.length(); i++) {
                        JSONObject jo = cityList.getJSONObject(i);
                        int id = jo.getInt("id");
                        String name = jo.getString("name");
                        cityMap.put(name, id);
                        cityNames.add(name);
                    }
                    spinner.setAdapter(new ArrayAdapter<>(FarmingActivity.this, android.R.layout.simple_spinner_dropdown_item, cityNames));
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                }
            }
        }));
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);
        best.setOnClickListener(this);
        popular.setOnClickListener(this);
        spinner.setOnItemSelectedListener(this);
        listView.setOnScrollListener(this);
    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        topTitle = (TextView) findViewById(R.id.topTitle);
        best = (TextView) findViewById(R.id.best);
        popular = (TextView) findViewById(R.id.popular);
        spinner = (Spinner) findViewById(R.id.spinner);
        townCursor = (ImageView) findViewById(R.id.townCursor);
        bestCursor = (ImageView) findViewById(R.id.bestCursor);
        popularCursor = (ImageView) findViewById(R.id.popularCursor);
        listView = (ListView) findViewById(R.id.listView);

        addTabs();
        Intent intent = getIntent();
        String thisTitle = intent.getStringExtra("thisTitle");
        moduleId = intent.getIntExtra("moduleId", 0);
        //moduleId = 1;
        topTitle.setText(thisTitle);
        aMapLocation = (AMapLocation) BaseApplication.dataMap.get("aMapLocation");

        for (int i = 0; i < 2; i++) {
            pages.add(0);
            isNoMoreDataList.add(false);
            productArrayLists.add(new ArrayList<Product>());
        }
    }

    private void getProductData() {
        BaseApplication.iService.getFarmingGoods(moduleId, pages.get(curPos), curCity, tag).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray storeList = jsonObject.getJSONArray("storeList");
                    if (op == 0) {
                        productArrayLists.get(curPos).clear();
                    }
                    int i = 0;
                    for (; i < storeList.length(); i++) {
                        JSONArray jsonArray = storeList.getJSONArray(i);
                        int storeId = jsonArray.getInt(0);
                        String storeName = jsonArray.getString(1);
                        double lat = jsonArray.getDouble(2);
                        double lng = jsonArray.getDouble(3);
                        double scores = jsonArray.getDouble(4);
                        String picture = BaseApplication.BASE_URL + jsonArray.getString(5);
                        String addr = jsonArray.getString(6);
                        productArrayLists.get(curPos).add(new Product(0, storeId, storeName, AMapUtils.calculateLineDistance(new LatLng(lat, lng), new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())), addr, picture, ""));
                    }
                    if (adapter == null) {
                        adapter = new FarmingBaseAdapter(productArrayLists.get(curPos), FarmingActivity.this);
                        listView.setAdapter(adapter);
                    } else {
                        String content;
                        if (op == 0) {
                            content = "刷新完成";
                            isNoMoreDataList.set(curPos, false);
                        } else {
                            content = "加载完成";
                        }
                        if (i < pageLen) {
                            isNoMoreDataList.set(curPos, true);
                        }
                        adapter.setProductList(productArrayLists.get(curPos));
                        adapter.notifyDataSetChanged();
                        Toast.makeText(FarmingActivity.this, content, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                }
            }
        }));
    }

    private void addTabs() {
        cursorList.add(bestCursor);
        cursorList.add(popularCursor);
        tabList.add(best);
        tabList.add(popular);
    }

    @Override
    public void onClick(View v) {
        if (v == topArrow) {
            finish();
        } else if (v == best) {
            changeTab(0);
        } else if (v == popular) {
            changeTab(1);
        }
    }

    private void changeTab(int newPos) {
        if (newPos != curPos) {
            cursorList.get(curPos).setVisibility(View.INVISIBLE);
            cursorList.get(newPos).setVisibility(View.VISIBLE);

            int red = ContextCompat.getColor(this, R.color.redTheme);
            tabList.get(newPos).setTextColor(red);
            int gray = ContextCompat.getColor(this, R.color.textGray);
            tabList.get(curPos).setTextColor(gray);

            if (newPos == 0) {
                tag = 0;
            } else if (newPos == 1) {
                tag = 1;
            }
            curPos = newPos;
            if (adapter != null) {
                if (productArrayLists.get(curPos).size() == 0) {
                    op = 0;
                    getProductData();
                } else {
                    adapter.setProductList(productArrayLists.get(curPos));
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String cityName = cityNames.get(position);
        curCity = cityMap.get(cityName);
        //选择城市后，重新设置请求的页面为第0页，这样切换选项卡就可以利用changeTab()方法中的判断条件刷新数据了
        for (int i = 0; i < pages.size(); i++) {
            pages.set(i, 0);
            isNoMoreDataList.set(i, false);
            if (curPos != i) {
                productArrayLists.get(i).clear();
            }
        }
        op = 0;
        getProductData();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        int position = listView.getLastVisiblePosition();
        if (position == productArrayLists.get(curPos).size() - 1 && !isNoMoreDataList.get(curPos)) {
            Integer integer = pages.get(curPos);
            pages.set(curPos, integer + 1);
            op = 1;
            getProductData();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
