package com.jhobor.ddc.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jhobor.ddc.R;
import com.jhobor.ddc.activity.SendingDetails2Activity;
import com.jhobor.ddc.activity.SendingDetailsActivity;
import com.jhobor.ddc.adapter.OrdersBaseAdapter;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.entity.Orders;
import com.jhobor.ddc.utils.ErrorUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017/1/4.
 */

public class OrdersItemFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    View view;
    ListView listView;

    List<Orders> ordersList;
    String status;

    public OrdersItemFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        status = bundle.getString("status", "");
        ordersList = bundle.getParcelableArrayList("ordersList");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initView(inflater, container);

        return view;
    }

    private void initView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_item_orders, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        OrdersBaseAdapter ordersBaseAdapter = new OrdersBaseAdapter(ordersList, getContext());
        listView.setAdapter(ordersBaseAdapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Orders orders = ordersList.get(position);
        if (orders.getState() == 2) {
            Intent intent;
            if (orders.getType() == 0) {
                intent = new Intent(getContext(), SendingDetailsActivity.class);
            } else {
                intent = new Intent(getContext(), SendingDetails2Activity.class);
            }
            intent.putExtra("ordersId", orders.getId());
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "没有物流信息", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        int state = ordersList.get(position).getState();
        if (state!=0){
            Toast.makeText(getContext(),"不是待付款订单，不可删除",Toast.LENGTH_SHORT).show();
            return true;
        }
        MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                .title("删除")
                .content("确定要删除这个订单吗？")
                .positiveText("删除")
                .positiveColor(ContextCompat.getColor(getContext(), R.color.redPress))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String uuid = (String) BaseApplication.dataMap.get("token");
                        long ordersId = ordersList.get(position).getId();
                        BaseApplication.iService.delOrders(uuid,ordersId).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
                            @Override
                            public void parse(String data) {
                                try {
                                    JSONObject jsonObject = new JSONObject(data);
                                    int isLogin = jsonObject.getInt("isLogin");
                                    if (isLogin==1) {
                                        int msg = jsonObject.getInt("msg");
                                        if (msg==1) {
                                            Intent intent = new Intent("reloadOrders");
                                            getContext().sendBroadcast(intent);
                                            Toast.makeText(getContext(),"删除成功",Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(getContext(),"删除失败，请稍候再试",Toast.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        Toast.makeText(getContext(),"未登录，操作失败",Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    ErrorUtil.retrofitResponseParseFail(getContext(),e);
                                }
                            }
                        }));
                    }
                })
                .negativeText("取消")
                .negativeColor(ContextCompat.getColor(getContext(), R.color.blackGray))
                .show();


        return true;
    }
}
