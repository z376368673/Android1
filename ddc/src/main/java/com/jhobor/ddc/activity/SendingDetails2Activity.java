package com.jhobor.ddc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhobor.ddc.R;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.utils.ErrorUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class SendingDetails2Activity extends AppCompatActivity implements View.OnClickListener {
    ImageView topArrow;
    TextView topTitle, streamNo;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sending_details2);

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
                    if (1 == type) {
                        String streamNoText = jsonObject.getString("streamNo");
                        streamNo.setText(streamNoText);
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                }
            }
        }));
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);

        webView.setWebViewClient(new WebViewClient());
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        webView.loadUrl("https://m.kuaidi100.com/");
    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        topTitle = (TextView) findViewById(R.id.topTitle);
        streamNo = (TextView) findViewById(R.id.streamNo);
        webView = (WebView) findViewById(R.id.webView);

        topTitle.setText("第三方物流");
        streamNo.setTextIsSelectable(true);
    }

    @Override
    public void onClick(View v) {
        if (v == topArrow) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
