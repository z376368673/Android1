package com.jhobor.fortune.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jhobor.fortune.R;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.utils.BarUtil;
import com.jhobor.fortune.utils.HideIMEUtil;

import java.net.URLEncoder;

/**
 * Author     Qijing
 * Created by YQJ on 2018/3/27.
 * Description:
 */
public class FankuiActivity extends AppCompatActivity {

    EditText fankui_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fankui);
        HideIMEUtil.wrap(this);
        BarUtil.topBar(this, "用户反馈");
        BarUtil.topBarRight(this, false, " ");
        fankui_et = (EditText) findViewById(R.id.fankui_et);
    }

    public void postData(View view) {
        String fankui = fankui_et.getText().toString().trim();
        if (TextUtils.isEmpty(fankui)) {
            Toast.makeText(FankuiActivity.this, "请输入您的意见", Toast.LENGTH_SHORT).show();
            return;
        }
        String encode = URLEncoder.encode(fankui);
        String encode1 = URLEncoder.encode(URLEncoder.encode(encode));

        String uuid = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.feedback(uuid, encode1).enqueue(new RetrofitCallback(getApplicationContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                JSONObject jsonObject = JSON.parseObject(data);
                int msg = jsonObject.getInteger("msg");
                if (msg == 1) {
                    Toast.makeText(FankuiActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    String error = jsonObject.getString("errorInfo");
                    Toast.makeText(FankuiActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        }));
    }


}
