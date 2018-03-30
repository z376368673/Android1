package com.jhobor.ddc.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.ddc.R;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.utils.ErrorUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class SendingDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView topArrow, callSender;
    TextView topTitle, sender, ordersNo, receiver, phone, payTime, sendTime, minTime, toAddr;
    Button report;

    String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sending_details);

        Intent intent = getIntent();
        int ordersId = intent.getIntExtra("ordersId", 0);
        initView();
        handleEvt();
        getOrdersStream(ordersId);
    }

    private void getOrdersStream(int ordersId) {
        String uuid = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.ordersStream(uuid, ordersId).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int type = jsonObject.getInt("type");
                    if (0 == type) {
                        String date = jsonObject.getString("date");
                        String sendDate = jsonObject.getString("sendDate");
                        String senderName = jsonObject.getString("name");
                        mobile = jsonObject.getString("mobile");
                        String ordersDate = jsonObject.getString("ordersDate");
                        String harvestName = jsonObject.getString("harvestName");
                        String harvestPhone = jsonObject.getString("harvestPhone");
                        String harvestAddr = jsonObject.getString("harvestAddr");
                        String ordersNoText = jsonObject.getString("ordersNo");
                        sender.setText(senderName);
                        receiver.setText(harvestName);
                        phone.setText(harvestPhone);
                        toAddr.setText(harvestAddr);
                        payTime.setText(ordersDate);
                        sendTime.setText(sendDate);
                        minTime.setText(date);
                        ordersNo.setText(ordersNoText);
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                }
            }
        }));
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);
        callSender.setOnClickListener(this);
        report.setOnClickListener(this);
    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        callSender = (ImageView) findViewById(R.id.callSender);
        topTitle = (TextView) findViewById(R.id.topTitle);
        sender = (TextView) findViewById(R.id.sender);
        ordersNo = (TextView) findViewById(R.id.ordersNo);
        receiver = (TextView) findViewById(R.id.receiver);
        phone = (TextView) findViewById(R.id.phone);
        payTime = (TextView) findViewById(R.id.payTime);
        sendTime = (TextView) findViewById(R.id.sendTime);
        minTime = (TextView) findViewById(R.id.minTime);
        toAddr = (TextView) findViewById(R.id.toAddr);
        report = (Button) findViewById(R.id.report);

        topTitle.setText("物流详情");
    }

    @Override
    public void onClick(View v) {
        if (v == callSender) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "拨号权限被限制了，请在安全中心打开本应用的拨号权限", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(intent);
        } else if (v == topArrow) {
            finish();
        } else if (v == report) {

        }
    }
}
