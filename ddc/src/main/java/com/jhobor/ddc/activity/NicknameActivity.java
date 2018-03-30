package com.jhobor.ddc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.ddc.R;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.utils.ErrorUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class NicknameActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView topArrow;
    TextView topTitle, done;
    EditText userName;

    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname);

        initView();
        handleEvt();
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);
        done.setOnClickListener(this);
    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        topTitle = (TextView) findViewById(R.id.topTitle);
        done = (TextView) findViewById(R.id.done);
        userName = (EditText) findViewById(R.id.userName);

        topTitle.setText("昵称");
        done.setText("完成");
        done.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        String nickName = intent.getStringExtra("nickName");
        type = intent.getStringExtra("type");
        userName.setText(nickName);
    }

    @Override
    public void onClick(View v) {
        if (v == topArrow) {
            finish();
        } else if (v == done) {
            String s = userName.getText().toString();
            if (s.trim().isEmpty()) {
                Toast.makeText(this, "昵称不可以为空哦", Toast.LENGTH_SHORT).show();
            } else {
                String uuid = (String) BaseApplication.dataMap.get("token");
                if (type.equals("userName")) {
                    BaseApplication.iService.editNickname(uuid, s).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                        @Override
                        public void parse(String data) {
                            try {
                                JSONObject jsonObject = new JSONObject(data);
                                int msg = jsonObject.getInt("msg");
                                int isLogin = jsonObject.getInt("isLogin");
                                if (msg == 1 && isLogin == 1) {
                                    BaseApplication.dataMap.put("nickName", userName.getText().toString());
                                    Toast.makeText(NicknameActivity.this, "昵称修改成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            } catch (JSONException e) {
                                ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                            }
                        }
                    }));
                } else if (type.equals("storeName")) {
                    BaseApplication.iService.editStoreName(uuid, s).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                        @Override
                        public void parse(String data) {
                            try {
                                JSONObject jsonObject = new JSONObject(data);
                                int msg = jsonObject.getInt("msg");
                                if (msg == 1) {
                                    BaseApplication.dataMap.put("nickName", userName.getText().toString());
                                    Toast.makeText(NicknameActivity.this, "昵称修改成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            } catch (JSONException e) {
                                ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                            }
                        }
                    }));
                }
            }
        }
    }
}
