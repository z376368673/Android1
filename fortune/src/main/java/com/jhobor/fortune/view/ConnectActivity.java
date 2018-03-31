package com.jhobor.fortune.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jhobor.fortune.R;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.utils.BarUtil;
import com.jhobor.fortune.utils.HideIMEUtil;

/**
 * Author     Qijing
 * Created by YQJ on 2018/3/27.
 * Description:
 */
public class ConnectActivity extends AppCompatActivity{

    TextView tv_v;
    TextView connect_tv_content;
    TextView connect_tv_tel;
    TextView connect_tv_qq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        HideIMEUtil.wrap(this);
        BarUtil.topBar(this, "联系我们");
        BarUtil.topBarRight(this, false, " ");
        tv_v = (TextView) findViewById(R.id.tv_v);
        connect_tv_content = (TextView) findViewById(R.id.connect_tv_content);
        connect_tv_tel = (TextView) findViewById(R.id.connect_tv_tel);
        connect_tv_qq = (TextView) findViewById(R.id.connect_tv_qq);
        getData();
    }
    //aboutWe/list

    public void getData() {
        String uuid = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.aboutWe(uuid).enqueue(new RetrofitCallback(getApplicationContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                JSONObject jsonObject = JSON.parseObject(data);
                int msg = jsonObject.getInteger("msg");
                if (msg==1){
                   // Toast.makeText(FankuiActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    jsonObject = jsonObject.getJSONObject("aboutWe");

                    String version = jsonObject.getString("version");
                    String content = jsonObject.getString("content");
                    String phone = jsonObject.getString("phone");
                    String qq = jsonObject.getString("qq");

                    tv_v.setText(R.string.app_name+": "+version);
                    connect_tv_content.setText(content);
                    connect_tv_tel.setText("客服电话："+phone);
                    connect_tv_qq.setText("客服电话："+qq);

                }else {
                    String error = jsonObject.getString("errorInfo");
                    Toast.makeText(ConnectActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        }));
    }

}
