package com.jhobor.fortune.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jhobor.fortune.oldui.BalanceDetailsActivity;
import com.jhobor.fortune.oldui.BoodingCoinActivity;
import com.jhobor.fortune.oldui.BoodingDetailsActivity;
import com.jhobor.fortune.oldui.FreezeDetailsActivity;
import com.jhobor.fortune.oldui.GetHandActivity;
import com.jhobor.fortune.oldui.GiveHandActivity;
import com.jhobor.fortune.R;
import com.jhobor.fortune.oldui.TradeRecordHelpActivity;
import com.jhobor.fortune.adapter.FinanceBaseQuickAdapter;
import com.jhobor.fortune.adapter.TradeHelpAdapter;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.entity.Booding;
import com.jhobor.fortune.entity.Finance;
import com.jhobor.fortune.utils.ErrorUtil;
import com.jhobor.fortune.oldui.FullyLinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class FortuneFragment extends Fragment implements BaseQuickAdapter.OnItemClickListener, View.OnClickListener {
    //FontTextView profit;
    //RecyclerView recyclerView;
    View view;
    LinearLayout tradeDetails;

    List<Finance> financeList = new ArrayList<>();
    FinanceBaseQuickAdapter quickAdapter;
    double profitMoney;

    public FortuneFragment() {
        // Required empty public constructor
    }

    ImageView help, getHelp, back, header;
    LinearLayout walletsBox, freezeBox, dynamic_box;
    RecyclerView recyclerView;
    TextView balance, freezeMoney,dynamicMoney;
    FrameLayout tradeRecord, boodingCoin;

    Double bonusMoney, congealMoney, staticMoney;
    List<Booding> boodingList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       /* Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fortune, container, false);
        profit = (FontTextView) view.findViewById(R.id.profit);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        tradeDetails = (LinearLayout) view.findViewById(R.id.tradeDetails);
        boolean reload = (boolean) BaseApplication.dataMap.get("reload");
        if (financeList.size() == 0 || reload) {
            getData();
        } else {
            setData();
        }
        tradeDetails.setOnClickListener(this);

        return view;*/
        view = inflater.inflate(R.layout.activity_trade_details, container, false);

        help = (ImageView) view.findViewById(R.id.help);
        getHelp = (ImageView) view.findViewById(R.id.getHelp);
        back = (ImageView) view.findViewById(R.id.back);
        header = (ImageView) view.findViewById(R.id.header);
        walletsBox = (LinearLayout) view.findViewById(R.id.walletsBox);
        freezeBox = (LinearLayout) view.findViewById(R.id.freezeBox);
        dynamic_box = (LinearLayout) view.findViewById(R.id.dynamic_box);


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        balance = (TextView) view.findViewById(R.id.balance);
        freezeMoney = (TextView) view.findViewById(R.id.freezeMoney);
        dynamicMoney = (TextView) view.findViewById(R.id.dynamicMoney); //动态钱包金额

        tradeRecord = (FrameLayout) view.findViewById(R.id.tradeRecord);
        boodingCoin = (FrameLayout) view.findViewById(R.id.boodingCoin);
        back.setOnClickListener(this);
        help.setOnClickListener(this);
        getHelp.setOnClickListener(this);
        walletsBox.setOnClickListener(this);
        freezeBox.setOnClickListener(this);
        tradeRecord.setOnClickListener(this);
        boodingCoin.setOnClickListener(this);
        header.setOnClickListener(this);
        dynamic_box.setOnClickListener(this);
        getData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        /*boolean reload = (boolean) BaseApplication.dataMap.get("reload");
        if (reload) {
            getData();
        }*/
        boolean b = (boolean) BaseApplication.dataMap.get("reload");
        if (b) {
            getData();
            BaseApplication.dataMap.put("reload", false);
        }
    }

    private void setData() {
        //profit.setText(String.format(Locale.CHINA, "我的盈亏额：%.2f", profitMoney)); 原本已经注释了的
       /* LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(quickAdapter);*/

    }

    private void getData() {
        /*String token = (String) BaseApplication.dataMap.get("token");
        Log.i("token>>", token);
        BaseApplication.iService.myFortune(token).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int isLogin = jsonObject.getInt("isLogin");
                    if (isLogin == 1) {
                        profitMoney = jsonObject.getDouble("profitMoney");
                        JSONArray orderList = jsonObject.getJSONArray("orderList");
                        int length = orderList.length();
                        financeList.clear();
                        for (int i = 0; i < length; i++) {
                            JSONArray jsonArray = orderList.getJSONArray(i);
                            int id = jsonArray.getInt(0);
                            double capital = jsonArray.getDouble(1);
                            double interest = jsonArray.getDouble(2);
                            double total = jsonArray.getDouble(3);
                            String date = jsonArray.getString(4);
                            int state = jsonArray.getInt(5);
                            String status = "未付款";
                            if (state == 1) {
                                status = "理财中";
                            }
                            financeList.add(new Finance(id, (float) capital, (float) interest, (float) total, date, state, status));
                        }
                        quickAdapter = new FinanceBaseQuickAdapter(R.layout.item_finance, financeList);
                        quickAdapter.setOnItemClickListener(FortuneFragment.this);
                        BaseApplication.dataMap.put("reload", false);
                        setData();
                    } else {
                        Toast.makeText(getContext(), "未登录，无法获取数据", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getContext(), e);
                }
            }
        }));*/
        String token = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.tradeDetails(token).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int isLogin = jsonObject.getInt("isLogin");
                    if (isLogin == 1) {
                        bonusMoney = jsonObject.getDouble("bonusMoney");
                        congealMoney = jsonObject.getDouble("congealMoney");
                        staticMoney = jsonObject.getDouble("staticMoney");
                        JSONArray helpList = jsonObject.getJSONArray("helpList");
                        int length = helpList.length();
                        boodingList.clear();
                        for (int i = 0; i < length; i++) {
                            JSONObject jo = helpList.getJSONObject(i);
                            int id = jo.getInt("id");
                            double money = jo.getDouble("money");
                            int tag = jo.getInt("tag");
                            String date = jo.getString("date");
                            int status = jo.getInt("status");
                            int timePoor = jo.getInt("timePoor");
                            boodingList.add(new Booding(id, String.valueOf(id), "", (float) money, date, tag, status, timePoor));
                        }
                        balance.setText(String.format(Locale.CHINA, "%.2f", staticMoney));
                        freezeMoney.setText(String.format(Locale.CHINA, "%.2f", congealMoney));
                        dynamicMoney.setText(String.format(Locale.CHINA, "%.2f", bonusMoney));


                        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(getActivity());
                        layoutManager.setOrientation(FullyLinearLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setNestedScrollingEnabled(false);
                        TradeHelpAdapter tradeHelpAdapter = new TradeHelpAdapter(R.layout.item_trade_help, boodingList);
                        recyclerView.setAdapter(tradeHelpAdapter);
                        tradeHelpAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                Booding booding = boodingList.get(position);
                                if (booding.getState() == 1 || booding.getState() == 2) {
                                    int id = booding.getId();
                                    int type = booding.getType();
                                    Intent intent = new Intent(getActivity(), BoodingDetailsActivity.class);
                                    intent.putExtra("helpId", id);
                                    intent.putExtra("type", type);
                                    startActivity(intent);
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), "账户信息失效，无法获取数据", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getActivity(), e);
                }
            }
        }));
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        /*Intent intent = new Intent(getContext(), FinanceDetailsActivity.class);
        intent.putExtra("orderId", financeList.get(position).getId());
        startActivity(intent);*/
    }

    @Override
    public void onClick(View v) {
        /*if (v == tradeDetails) {
            Log.i(">>","23444444444444444444444444444444");
            startActivity(new Intent(getContext(), TradeDetailsActivity.class));
        }*/
        if (help == v) {
            int isActivation_nu = BaseApplication.prefs.getInt("isActivation_nu", 1);
            if (0 == isActivation_nu) {
                Toast.makeText(getContext(), "账号未激活,请前往“个人”界面点击激活 ", Toast.LENGTH_LONG).show();
                return;
            }
            startActivity(new Intent(getActivity(), GiveHandActivity.class));
        } else if (v == getHelp) {
            Intent intent = new Intent(getActivity(), GetHandActivity.class);
            intent.putExtra("staticMoney", staticMoney);
            intent.putExtra("bonusMoney", bonusMoney);
            startActivity(intent);
        } else if (walletsBox == v) {   //静态钱包
            startActivity(new Intent(getActivity(), BalanceDetailsActivity.class));
        } else if (v == freezeBox) {    //冻结钱包
            startActivity(new Intent(getActivity(), FreezeDetailsActivity.class));
        } else if (v == tradeRecord) {
            startActivity(new Intent(getActivity(), TradeRecordHelpActivity.class));
        } else if (v == boodingCoin) {
            startActivity(new Intent(getActivity(), BoodingCoinActivity.class));
        } else if (v == back) {
            //finish();
        } else if (v == header) {
            getData();
        } else if (v == dynamic_box) { //动态钱包
            startActivity(new Intent(getActivity(), DynamicBoxActivity.class));
        }
    }

}
