package com.jhobor.fortune.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jhobor.fortune.R;
import com.jhobor.fortune.adapter.BalanceDetailsAdapter;
import com.jhobor.fortune.adapter.HelpAdapter;
import com.jhobor.fortune.adapter.InterestAdapter;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.entity.Booding;
import com.jhobor.fortune.entity.Interest;
import com.jhobor.fortune.entity.Pager;
import com.jhobor.fortune.utils.ErrorUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class BalancePageFragment extends Fragment {
    View view;
    RecyclerView recyclerView;

    List<Booding> boodingList;
    String whatPage;
    Pager pager;
    InterestAdapter adapter;
    List<Interest> list;

    public BalancePageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        boodingList = arguments.getParcelableArrayList("boodingList");
        whatPage = arguments.getString("whatPage", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frament_recycler_view, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        if ("static".equals(whatPage)||"freeze".equals(whatPage)) {
            // 静态钱包明细、冻结钱包排单记录
            recyclerView.setAdapter(new BalanceDetailsAdapter(R.layout.item_balance, boodingList));
        } else if ("interest".equals(whatPage)) {
            // 冻结钱包利息记录
            pager = new Pager(10);
            list = new ArrayList<>();
            getInterestData();
        } else if ("help".equals(whatPage)) {
            // 提供帮助交易记录
            recyclerView.setAdapter(new HelpAdapter(R.layout.item_help, boodingList));
        } else if ("getHelp".equals(whatPage)) {
            // 得到帮助交易记录
            boodingList = new ArrayList<>();
            getAcceptHelpData();
        } else if ("prize".equals(whatPage)){
            boodingList = new ArrayList<>();
            getPrizeData();
        }

        return view;
    }

    private void getPrizeData() {
        String token = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.tradeRecordHelp(token,2).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int isLogin = jsonObject.getInt("isLogin");
                    if (isLogin == 1) {
                        JSONArray helpList = jsonObject.getJSONArray("helpList");
                        int length = helpList.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject jo = helpList.getJSONObject(i);
                            int id = jo.getInt("id");
                            double money = jo.getDouble("money");
                            //int tag = jo.getInt("tag");
                            int tag = 5;
                            String date = jo.getString("date");
                            int status = jo.getInt("status");
                            String ledName = jo.getString("ledName");
                            String ledPhone = jo.getString("ledPhone");
                            String phone = jo.getString("phone");
                            String name = jo.getString("name");
                            int timePoor = jo.getInt("timePoor");
                            Booding booding = new Booding(id, phone, name, (int) money, date, tag, status, timePoor, ledName, ledPhone);
                            boodingList.add(booding);
                        }
                        //recyclerView.setAdapter(new HelpAdapter(R.layout.item_help, boodingList));
                        recyclerView.setAdapter(new HelpAdapter(R.layout.item_help_prise, boodingList));
                    } else {
                        Toast.makeText(getContext(), "你未登录，获取数据失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getContext(), e);
                }
            }
        }));
    }

    private void getAcceptHelpData() {
        String token = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.tradeRecordHelp(token, 0).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int isLogin = jsonObject.getInt("isLogin");
                    if (isLogin == 1) {
                        JSONArray helpList = jsonObject.getJSONArray("helpList");
                        int length = helpList.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject jo = helpList.getJSONObject(i);
                            int id = jo.getInt("id");
                            double money = jo.getDouble("money");
                            int tag = jo.getInt("tag");
                            String date = jo.getString("date");
                            int status = jo.getInt("status");
                            String ledName = jo.getString("ledName");
                            String ledPhone = jo.getString("ledPhone");
                            String phone = jo.getString("phone");
                            String name = jo.getString("name");
                            int timePoor = jo.getInt("timePoor");
                            Booding booding = new Booding(id, phone, name, (int) money, date, tag, status, timePoor, ledName, ledPhone);
                            boodingList.add(booding);
                        }
                        recyclerView.setAdapter(new HelpAdapter(R.layout.item_help, boodingList));
                    } else {
                        Toast.makeText(getContext(), "你未登录，获取数据失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getContext(), e);
                }
            }
        }));
    }

    private void getInterestData() {
        String token = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.myInterestInfo(token, pager.getPage()).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int isLogin = jsonObject.getInt("isLogin");
                    if (isLogin == 1) {
                        JSONArray iiList = jsonObject.getJSONArray("iiList");
                        int length = iiList.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject jo = iiList.getJSONObject(i);
                            int id = jo.getInt("id");
                            double money = jo.getDouble("money");
                            String date = jo.getString("date");
                            String recordExplain = jo.getString("recordExplain");
                            String interestMoney = jo.getString("interestMoney");
                            list.add(new Interest(id, (float) money, date, recordExplain,interestMoney));
                        }
                        if (adapter == null) {
                            adapter = new InterestAdapter(R.layout.item_interest, list);
                            recyclerView.setAdapter(adapter);
                            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                    super.onScrollStateChanged(recyclerView, newState);
                                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                                    int itemCount = layoutManager.getItemCount();
                                    if (lastVisibleItemPosition+1==itemCount && newState!=0 && !pager.isNoMoreData() && !pager.isLoading()){
                                        pager.next();
                                        pager.setLoading(true);
                                        getInterestData();
                                    }
                                    Log.i("--->>", String.format(Locale.CHINA,"lastPosition:%d , itemCount:%d , newState:%d , page:%d",lastVisibleItemPosition,itemCount,newState,pager.getPage()));
                                }

                                @Override
                                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                    super.onScrolled(recyclerView, dx, dy);
                                }
                            });
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                        pager.setLoading(false);
                        if (length<pager.getPageCount()){
                            pager.setNoMoreData(true);
                        }
                    } else {
                        Toast.makeText(getContext(), "账户信息失效，无法获取数据", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getContext(), e);
                }
            }
        }));
    }

}
