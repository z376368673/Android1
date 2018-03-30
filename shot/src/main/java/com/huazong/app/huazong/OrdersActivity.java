package com.huazong.app.huazong;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.huazong.app.huazong.adapter.OrdersFragmentPagerAdapter;
import com.huazong.app.huazong.base.BarUtil;
import com.huazong.app.huazong.base.BaseApplication;
import com.huazong.app.huazong.base.RetrofitCallback;
import com.huazong.app.huazong.entity.Order;
import com.huazong.app.huazong.fragment.OrderFragment;
import com.huazong.app.huazong.utils.ErrorUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {
    static final int UN_USED = 0;
    static final int USED = 1;
    TextView showUsed, showUnused;
    ViewPager storesContentPager;
    private List<Order> usedOrders;
    private List<Order> unuseOrders;
    private int depth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_orders);

        initView();
        getData();
    }

    private void getData() {
        String openid = (String) BaseApplication.dataMap.get("openid");
        int arrowOrGun = (int) BaseApplication.dataMap.get("arrowOrGun");
        BaseApplication.iService.orderList(openid,arrowOrGun).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    depth = jsonObject.getInt("pass");
                    JSONArray renList = jsonObject.getJSONArray("renList");
                    JSONArray stoList = jsonObject.getJSONArray("stoList");
                    usedOrders = new ArrayList<>();
                    unuseOrders = new ArrayList<>();
                    for (int i = 0; i < renList.length(); i++) {
                        JSONObject rend = renList.getJSONObject(i);
                        int id = rend.getInt("id");
                        int byway = rend.getInt("byway");
                        String date = rend.getString("date");
                        String time = rend.getString("time");
                        int type = rend.getInt("type");
                        boolean used = rend.getString("status").equals("已使用");
                        JSONObject sto = stoList.getJSONObject(i);
                        String name = sto.getString("name");
                        int storeId = sto.getInt("id");
                        String picture = BaseApplication.BASE_URL_HOST2 + sto.getString("picture");
                        double lng = sto.getDouble("lng");
                        double lat = sto.getDouble("lat");
                        Order order = new Order(id, name, used, date, time, new LatLng(lat,lng), byway, picture, type, storeId);
                        if (used) {
                            usedOrders.add(order);
                        } else {
                            unuseOrders.add(order);
                        }
                    }
                    initFragments();
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getBaseContext(),e);
                }
            }
        }));
    }

    private void initView() {
        showUnused = (TextView) findViewById(R.id.showUnused);
        showUsed = (TextView) findViewById(R.id.showUsed);

        BarUtil.topBar(this,"订单列表");
        storesContentPager = (ViewPager) findViewById(R.id.storesContentPager);
        showUsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storesContentPager.setCurrentItem(0);
            }
        });
        showUnused.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storesContentPager.setCurrentItem(1);
            }
        });
    }

    private void initFragments() {
        List<OrderFragment> allOrderFragmentList = new ArrayList<>(2);

        Bundle arg1 = new Bundle();
        arg1.putInt("useStatus", USED);
        arg1.putInt("depth", depth);
        arg1.putParcelableArrayList("orders", (ArrayList<? extends Order>) usedOrders);
        OrderFragment usedOrderFragment = new OrderFragment();
        usedOrderFragment.setArguments(arg1);
        allOrderFragmentList.add(usedOrderFragment);

        Bundle arg2 = new Bundle();
        arg2.putInt("useStatus", UN_USED);
        arg2.putInt("depth", depth);
        arg2.putParcelableArrayList("orders", (ArrayList<? extends Order>) unuseOrders);
        OrderFragment unuseOrderFragment = new OrderFragment();
        unuseOrderFragment.setArguments(arg2);
        allOrderFragmentList.add(unuseOrderFragment);

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        OrdersFragmentPagerAdapter fragmentPagerAdapter = new OrdersFragmentPagerAdapter(supportFragmentManager, allOrderFragmentList);
        storesContentPager.setAdapter(fragmentPagerAdapter);
        storesContentPager.addOnPageChangeListener(new OnFragmentPageChange());
        storesContentPager.setCurrentItem(1);
    }

    private class OnFragmentPageChange implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                showUsed.setTextColor(ContextCompat.getColor(OrdersActivity.this, R.color.green_text_color));
                showUnused.setTextColor(ContextCompat.getColor(OrdersActivity.this, R.color.second_text_color));
                ((LinearLayout) showUsed.getParent()).setBackgroundResource(R.mipmap.item_order_used);
            } else if (position == 1) {
                showUnused.setTextColor(ContextCompat.getColor(OrdersActivity.this, R.color.green_text_color));
                showUsed.setTextColor(ContextCompat.getColor(OrdersActivity.this, R.color.second_text_color));
                ((LinearLayout) showUsed.getParent()).setBackgroundResource(R.mipmap.item_order_unused);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {}
    }
}
