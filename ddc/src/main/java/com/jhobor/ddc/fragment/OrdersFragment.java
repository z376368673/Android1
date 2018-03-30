package com.jhobor.ddc.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jhobor.ddc.R;
import com.jhobor.ddc.adapter.MyFragmentPagerAdapter;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.entity.Orders;
import com.jhobor.ddc.utils.ErrorUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
    View view;
    TextView sending, arrived;
    ViewPager viewPager;

    List<Orders> ordersReceivedList = new ArrayList<>();
    List<Orders> ordersSendingList = new ArrayList<>();
    List<Fragment> fragmentList = new ArrayList<>();
    int curIndex = 0;
    boolean reload = false;
    MyFragmentPagerAdapter adapter;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getMyOrdersData();
        }
    };

    public OrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getContext().registerReceiver(receiver,new IntentFilter("reloadOrders"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initView(inflater, container);
        if (ordersReceivedList.size() == 0 || ordersSendingList.size() == 0) {
            getMyOrdersData();
        } else {
            updateAdapter();
        }

        return view;
    }

    private void updateAdapter() {
        if (reload) {
            fragmentList.clear();
            Bundle bundleSending = new Bundle();
            bundleSending.putString("status", "sending");
            bundleSending.putParcelableArrayList("ordersList", (ArrayList<? extends Parcelable>) ordersSendingList);
            OrdersItemFragment ordersSendingFragment = new OrdersItemFragment();
            ordersSendingFragment.setArguments(bundleSending);
            fragmentList.add(ordersSendingFragment);

            Bundle bundleReceived = new Bundle();
            bundleReceived.putString("status", "received");
            bundleReceived.putParcelableArrayList("ordersList", (ArrayList<? extends Parcelable>) ordersReceivedList);
            OrdersItemFragment ordersArrivedFragment = new OrdersItemFragment();
            ordersArrivedFragment.setArguments(bundleReceived);
            fragmentList.add(ordersArrivedFragment);
            reload = false;
        }
        /**
         * fragment 中嵌套 fragment ，要使用getChildFragmentManager()，不然第二次进入这个fragment是显示不了数据(fragment不被重绘)
         * 这里的第一层fragment是ordersFragment，第二层fragment是viewPager里面的两个OrdersItemFragment(派送和到店)
         */
        adapter = new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);

        if (curIndex != viewPager.getCurrentItem()) {
            viewPager.setCurrentItem(curIndex);
        }else if (curIndex==0){
            activeLeft();
        }else if (curIndex==1){
            activeRight();
        }
    }

    private void getMyOrdersData() {
        reload = true;
        String uuid = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.myOrders(uuid).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    parseJson(data);
                    updateAdapter();
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getContext(), e);
                }
            }
        }));
    }

    private void parseJson(String string) throws JSONException {
        JSONObject jsonObject = new JSONObject(string);
        int isLogin = jsonObject.getInt("isLogin");
        JSONArray ordersList = jsonObject.getJSONArray("ordersList");
        ordersReceivedList.clear();
        ordersSendingList.clear();
        for (int i = 0; i < ordersList.length(); i++) {
            JSONArray jsonArray = ordersList.getJSONArray(i);
            int ordersId = jsonArray.getInt(0);
            double money = jsonArray.getDouble(1);
            int num = jsonArray.getInt(2);
            String ordersTime = jsonArray.getString(3);
            int status = jsonArray.getInt(4);
            int goodsId = jsonArray.getInt(5);
            String goodsName = jsonArray.getString(6);
            String goodsPic = BaseApplication.BASE_URL + jsonArray.getString(7);
            int storeId = jsonArray.getInt(8);
            String storeName = jsonArray.getString(9);
            int type = jsonArray.getInt(10);
            // int id, String storeName, String goodsName, String state, String goodsPic, String ordersTime, float money
            Orders orders = new Orders(ordersId, storeName, goodsName, status, goodsPic, ordersTime, (float) money, type);
            orders.setGoodsId(goodsId);
            orders.setCount(num);
            if (status == 3 || status == 4) {
                ordersReceivedList.add(orders);
            } else {
                ordersSendingList.add(orders);
            }
        }
    }

    private void initView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_orders, container, false);
        sending = (TextView) view.findViewById(R.id.sending);
        arrived = (TextView) view.findViewById(R.id.arrived);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        sending.setOnClickListener(this);
        arrived.setOnClickListener(this);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == sending) {
            //activeLeft();
            viewPager.setCurrentItem(0);
        } else if (v == arrived) {
            //activeRight();
            viewPager.setCurrentItem(1);
        }
    }

    private void activeRight() {
        arrived.setTextColor(ContextCompat.getColor(getContext(), R.color.redTheme));
        arrived.setBackgroundResource(R.drawable.orders_tab_right_press);
        sending.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        sending.setBackgroundResource(R.drawable.orders_tab_left_normal);
    }

    private void activeLeft() {
        sending.setTextColor(ContextCompat.getColor(getContext(), R.color.redTheme));
        sending.setBackgroundResource(R.drawable.orders_tab_left_press);
        arrived.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        arrived.setBackgroundResource(R.drawable.orders_tab_right_normal);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            activeLeft();
        } else {
            activeRight();
        }
        curIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onResume() {
        super.onResume();
        boolean reload = (boolean) BaseApplication.dataMap.get("reload");
        if (reload){
            getMyOrdersData();
            BaseApplication.dataMap.put("reload",false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(receiver);
    }
}
