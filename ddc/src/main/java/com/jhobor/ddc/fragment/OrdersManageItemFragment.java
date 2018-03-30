package com.jhobor.ddc.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.jhobor.ddc.R;
import com.jhobor.ddc.activity.SendingActivity;
import com.jhobor.ddc.adapter.OrdersManageBaseAdapter;
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
 * Created by Administrator on 2017/1/4.
 * 订单管理分页
 */

public class OrdersManageItemFragment extends Fragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {
    View view;
    ListView listView;

    int status;
    List<Orders> ordersList = new ArrayList<>();
    int page = 0;
    int op = 0;
    int pageLen = 4;
    boolean noMoreData = false;
    OrdersManageBaseAdapter adapter;
    boolean need2refresh = false;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            need2refresh = true;
        }
    };

    public OrdersManageItemFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        status = arguments.getInt("status");

        getOrdersData();
        getContext().registerReceiver(receiver, new IntentFilter("refreshStoreOrders"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initView(inflater, container);
        handleEvt();
        if (adapter != null) {
            listView.setAdapter(adapter);
        }

        return view;
    }

    private void handleEvt() {
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(this);
    }

    private void initView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_item_orders, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), SendingActivity.class);
        intent.putExtra("ordersId", ordersList.get(position).getId());
        startActivity(intent);
    }


    private void getOrdersData() {
        String uuid = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.storeOrdersManage(uuid, page, status).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int isLogin = jsonObject.getInt("isLogin");
                    if (isLogin == 1) {
                        int msg = jsonObject.getInt("msg");
                        if (msg == 1) {
                            if (op == 0) {
                                ordersList.clear();
                                noMoreData = false;
                            }
                            JSONArray storeList = jsonObject.getJSONArray("storeList");
                            int i = 0;
                            for (; i < storeList.length(); i++) {
                                JSONArray jsonArray = storeList.getJSONArray(i);
                                int id = jsonArray.getInt(0);
                                String goodsName = jsonArray.getString(1);
                                int ordersStatus = jsonArray.getInt(2);
                                double price = jsonArray.getDouble(3);
                                String picture = BaseApplication.BASE_URL + jsonArray.getString(4);
                                int count = jsonArray.getInt(5);
                                String ordersNo = jsonArray.getString(6);
                                String dateTime = jsonArray.getString(7);
                                int type = jsonArray.getInt(8);//发货方式  0-未发 1-第三方物流 2-抢单
                                String userName = jsonArray.getString(9);

                                //int id, String goodsName, int state, String goodsPic, String ordersTime, int count, float price, String ordersNo, int type
                                ordersList.add(new Orders(id, goodsName, ordersStatus, picture, dateTime, userName, count, (float) price, ordersNo, type));
                            }
                            if (i < pageLen) {
                                noMoreData = true;
                            }
                            if (adapter == null) {
                                adapter = new OrdersManageBaseAdapter(ordersList, getContext());
                                listView.setAdapter(adapter);
                            } else {

                                adapter.notifyDataSetChanged();
                                String content;
                                if (op == 0) {
                                    content = "刷新完成";
                                } else {
                                    content = "加载完成";
                                }
                                Toast.makeText(getContext(), content, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getContext(), e);
                }
            }
        }));

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        int position = listView.getLastVisiblePosition();
        if (position == ordersList.size() - 1 && !noMoreData) {
            op = 1;
            ++page;
            getOrdersData();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onResume() {
        if (need2refresh) {
            op = 0;
            page = 0;
            getOrdersData();
            need2refresh = false;
        }
        super.onResume();
    }

    @Override
    public void onDestroy() {
        getContext().unregisterReceiver(receiver);
        super.onDestroy();
    }
}
