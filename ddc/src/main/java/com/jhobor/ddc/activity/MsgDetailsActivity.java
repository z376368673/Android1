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

public class MsgDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView topArrow;
    TextView topTitle, title, date, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_details);

        Intent intent = getIntent();
        int msgId = intent.getIntExtra("msgId", 0);
        initView();
        handleEvt();
        getMsgDetailsData(msgId);
    }

    private void getMsgDetailsData(int msgId) {
        BaseApplication.iService.msgDetails(msgId).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONObject info = jsonObject.getJSONObject("info");
                    int id = info.getInt("id");
                    String msgTitle = info.getString("title");
                    String msgContent = info.getString("content");
                    String infoDate = info.getString("infoDate");
                    int status = info.getInt("status");
                    title.setText(msgTitle);
                    date.setText(infoDate);
                    content.setText(msgContent);

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
        title = (TextView) findViewById(R.id.title);
        date = (TextView) findViewById(R.id.date);
        content = (TextView) findViewById(R.id.content);

        topTitle.setText("消息");
    }

    @Override
    public void onClick(View v) {
        if (v == topArrow) {
            finish();
        }
    }
}
