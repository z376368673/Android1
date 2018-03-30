package com.jhobor.ddc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhobor.ddc.R;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.utils.ErrorUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class SendingActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView topArrow;
    TextView topTitle, goodsName, count, money, receiver, phone, payTime, toAddr, ordersNo, streamNo, sender;

    int ordersId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sending);

        Intent intent = getIntent();
        ordersId = intent.getIntExtra("ordersId", 0);
        initView();
        handleEvt();
        getOrdersDetails();
    }

    private void getOrdersDetails() {
        String uuid = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.ordersDetails(uuid, ordersId).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String address = jsonObject.getString("address");
                    double totalMoney = jsonObject.getDouble("money");
                    String ordersNum = jsonObject.getString("ordersNo");
                    int num = jsonObject.getInt("count");
                    String mobile = jsonObject.getString("mobile");
                    String name = jsonObject.getString("name");
                    String ordersDate = jsonObject.getString("ordersDate");
                    int tag = jsonObject.getInt("tag");
                    String gName = jsonObject.getString("goodsName");
                    if (tag == 1) {
                        /*第三方物流*/
                        String streamNum = jsonObject.getString("streamNo");//物流编号
                        String streamName = jsonObject.getString("streamName");//物流名称，这里为空字符串
                        streamNo.setText(String.format(Locale.CHINA, "物流编号：%s", streamNum));
                        sender.setVisibility(View.INVISIBLE);
                    } else if (tag == 2) {
                        /*抢单派送*/
                        String streamNum = jsonObject.getString("streamNo");//派送员手机号码
                        String streamName = jsonObject.getString("streamName");//派送员姓名
                        streamNo.setText(String.format(Locale.CHINA, "派送员手机：%s", streamNum));
                        sender.setText(String.format(Locale.CHINA, "派送员：%s", streamName));
                    } else if (tag == 3) {
                        /*店家自配送*/
                        String streamNum = jsonObject.getString("streamNo");//店家手机号码
                        String streamName = jsonObject.getString("streamName");//店家名称
                        streamNo.setText(String.format(Locale.CHINA, "店家手机：%s", streamNum));
                        sender.setText(String.format(Locale.CHINA, "店家：%s", streamName));
                    } else {
                        streamNo.setText("未派送");
                        sender.setVisibility(View.INVISIBLE);
                    }
                    goodsName.setText(gName);
                    count.setText(String.format(Locale.CHINA, "x %d", num));
                    money.setText(String.format(Locale.CHINA, "￥%.2f", totalMoney));
                    receiver.setText(String.format(Locale.CHINA, "收货人：%s", name));
                    phone.setText(String.format(Locale.CHINA, "电    话：%s", mobile));
                    payTime.setText(String.format(Locale.CHINA, "付款时间：%s", ordersDate));
                    toAddr.setText(String.format(Locale.CHINA, "收货地址：%s", address));
                    ordersNo.setText(String.format(Locale.CHINA, "订单编号：%s", ordersNum));
                    receiver.setText(String.format(Locale.CHINA, "收货人：%s", name));

                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                }
            }
        }));
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);
    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        topTitle = (TextView) findViewById(R.id.topTitle);
        goodsName = (TextView) findViewById(R.id.goodsName);
        count = (TextView) findViewById(R.id.count);
        money = (TextView) findViewById(R.id.money);
        receiver = (TextView) findViewById(R.id.receiver);
        phone = (TextView) findViewById(R.id.phone);
        payTime = (TextView) findViewById(R.id.payTime);
        toAddr = (TextView) findViewById(R.id.toAddr);
        ordersNo = (TextView) findViewById(R.id.ordersNo);
        streamNo = (TextView) findViewById(R.id.streamNo);
        sender = (TextView) findViewById(R.id.sender);

        topTitle.setText("订单详情");
    }

    @Override
    public void onClick(View v) {
        if (v == topArrow) {
            finish();
        }
    }
}
