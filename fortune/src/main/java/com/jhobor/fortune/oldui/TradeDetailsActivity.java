package com.jhobor.fortune.oldui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jhobor.fortune.R;
import com.jhobor.fortune.adapter.TradeHelpAdapter;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.entity.Booding;
import com.jhobor.fortune.utils.ErrorUtil;
import com.jhobor.fortune.utils.HideIMEUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TradeDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView help, getHelp, back, header;
    LinearLayout walletsBox, freezeBox;
    RecyclerView recyclerView;
    TextView balance, freezeMoney;
    FrameLayout tradeRecord, boodingCoin;

    Double bonusMoney,congealMoney,staticMoney;
    List<Booding> boodingList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_details);
        HideIMEUtil.wrap(this);

        help = (ImageView) findViewById(R.id.help);
        getHelp = (ImageView) findViewById(R.id.getHelp);
        back = (ImageView) findViewById(R.id.back);
        header = (ImageView) findViewById(R.id.header);
        walletsBox = (LinearLayout) findViewById(R.id.walletsBox);
        freezeBox = (LinearLayout) findViewById(R.id.freezeBox);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        balance = (TextView) findViewById(R.id.balance);
        freezeMoney = (TextView) findViewById(R.id.freezeMoney);
        tradeRecord = (FrameLayout) findViewById(R.id.tradeRecord);
        boodingCoin = (FrameLayout) findViewById(R.id.boodingCoin);
        back.setOnClickListener(this);
        help.setOnClickListener(this);
        getHelp.setOnClickListener(this);
        walletsBox.setOnClickListener(this);
        freezeBox.setOnClickListener(this);
        tradeRecord.setOnClickListener(this);
        boodingCoin.setOnClickListener(this);
        header.setOnClickListener(this);
        getData();
    }

    private void getData() {
        String token = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.tradeDetails(token).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
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
                        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(TradeDetailsActivity.this);
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
                                    Intent intent = new Intent(TradeDetailsActivity.this, BoodingDetailsActivity.class);
                                    intent.putExtra("helpId", id);
                                    intent.putExtra("type", type);
                                    startActivity(intent);
                                }
                            }
                        });
                    } else {
                        Toast.makeText(TradeDetailsActivity.this, "账户信息失效，无法获取数据", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(TradeDetailsActivity.this, e);
                }
            }
        }));
    }

    @Override
    public void onClick(View v) {
        if (help == v) {
            startActivity(new Intent(this, GiveHandActivity.class));
        } else if (v == getHelp) {
            Intent intent = new Intent(this, GetHandActivity.class);
            intent.putExtra("staticMoney", staticMoney);
            intent.putExtra("bonusMoney", bonusMoney);
            startActivity(intent);
        } else if (walletsBox == v) {
            startActivity(new Intent(this, BalanceDetailsActivity.class));
        } else if (v == freezeBox) {
            startActivity(new Intent(this, FreezeDetailsActivity.class));
        } else if (v == tradeRecord) {
            startActivity(new Intent(this, TradeRecordHelpActivity.class));
        } else if (v == boodingCoin) {
            startActivity(new Intent(this, BoodingCoinActivity.class));
        } else if (v == back) {
            finish();
        }else if (v==header){
            getData();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean b = (boolean) BaseApplication.dataMap.get("reload");
        if (b) {
            getData();
            BaseApplication.dataMap.put("reload", false);
        }
    }
}
