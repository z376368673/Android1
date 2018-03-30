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
import android.widget.ListView;

import com.jhobor.ddc.R;
import com.jhobor.ddc.adapter.TicketManageBaseAdapter;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.entity.Ticket;
import com.jhobor.ddc.utils.ErrorUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TicketMangeFragment extends Fragment {
    View view;
    ListView listView;

    int op = 0;
    List<Ticket> ticketList = new ArrayList<>();
    TicketManageBaseAdapter adapter;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getStoreTickets();
        }
    };

    public TicketMangeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getContext().registerReceiver(receiver, new IntentFilter("refreshTicketData"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initView(inflater, container);
        return view;
    }

    private void initView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_ticket_mange, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        getStoreTickets();

    }

    private void getStoreTickets() {
        String uuid = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.storeTickets(uuid).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int msg = jsonObject.getInt("msg");
                    if (msg == 1) {
                        JSONArray couponList = jsonObject.getJSONArray("couponList");
                        if (op == 0) {
                            ticketList.clear();
                        }
                        for (int i = 0; i < couponList.length(); i++) {
                            JSONArray jsonArray = couponList.getJSONArray(i);
                            int id = jsonArray.getInt(0);
                            double restrict = jsonArray.getDouble(1);
                            double privilege = jsonArray.getDouble(2);
                            int count = jsonArray.getInt(3);
                            String date = jsonArray.getString(4);
                            int state = jsonArray.getInt(5);
                            ticketList.add(new Ticket(id, (float) privilege, (float) restrict, date, state, count));
                        }
                        if (adapter == null) {
                            adapter = new TicketManageBaseAdapter(ticketList, getContext());
                            listView.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                    }

                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getContext(), e);
                }
            }
        }));
    }

    @Override
    public void onDestroy() {
        getContext().unregisterReceiver(receiver);
        super.onDestroy();
    }
}
