package com.jhobor.ddc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.ddc.R;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.entity.UserInfo;
import com.jhobor.ddc.utils.ErrorUtil;
import com.jhobor.ddc.utils.MD5Util;
import com.jhobor.ddc.utils.VerifyUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText phone, password;
    CheckBox remember;
    TextView forget, register;
    Button login;

    String backCls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();
        backCls = intent.getStringExtra("backCls");
        initView();
        handleEvt();
    }

    private void handleEvt() {
        forget.setOnClickListener(this);
        register.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    private void initView() {
        phone = (EditText) findViewById(R.id.phone);
        password = (EditText) findViewById(R.id.password);
        remember = (CheckBox) findViewById(R.id.remember);
        forget = (TextView) findViewById(R.id.forget);
        register = (TextView) findViewById(R.id.register);
        login = (Button) findViewById(R.id.login);
    }

    @Override
    public void onClick(View v) {
        if (v == login) {
            String phoneNo = phone.getText().toString().trim();
            String pwd = password.getText().toString();
            if (!VerifyUtil.isMobile(phoneNo)) {
                Toast.makeText(this, "手机号码为空 或 密码为空", Toast.LENGTH_SHORT).show();
                phone.requestFocus();
            } else if (pwd.length() < 6) {
                Toast.makeText(this, "密码长度应该大于等于6", Toast.LENGTH_SHORT).show();
                password.requestFocus();
            } else {
                pwd = MD5Util.encode(pwd);
                BaseApplication.iService.login(phoneNo, pwd).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                    @Override
                    public void parse(String data) {
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            int msg = jsonObject.getInt("msg");
                            if (msg == 1) {
                                JSONObject obj = jsonObject.getJSONObject("userInfo");
                                int id = obj.getInt("id");
                                String name = obj.getString("name");
                                String gravatar = BaseApplication.BASE_URL + obj.getString("gravatar");
                                String mobile = obj.getString("mobile");
                                double balance = obj.getDouble("balance");
                                int status = obj.getInt("status");
                                int optionCity = obj.getInt("optionCity");
                                int type = obj.getInt("type");
                                String uuid = jsonObject.getString("uuid");
                                int isStore = jsonObject.getInt("isStore");
                                int isSend = jsonObject.getInt("isSend");
                                UserInfo userInfo = new UserInfo(id, name, mobile, gravatar, (float) balance);
                                BaseApplication.dataMap.put("token", uuid);
                                BaseApplication.dataMap.put("hasStore", isStore == 1);
                                BaseApplication.dataMap.put("hasAuth", isSend == 1);
                                BaseApplication.dataMap.put("userInfo", userInfo);
                                if (remember.isChecked()) {
                                    BaseApplication.prefs.edit().putString("token", uuid).apply();
                                }
                                if (backCls == null) {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                } else {
                                    goBack(false);
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "帐号和密码不匹配", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                        }
                    }
                }));
            }
        } else if (v == forget) {
            startActivity(new Intent(this, ForgetPwdActivity.class));
        } else if (v == register) {
            startActivity(new Intent(this, RegisterActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        goBack(true);
    }

    private void goBack(boolean isBack) {
        try {
            Intent intent = new Intent(this, Class.forName(backCls));
            intent.putExtra("isBack", isBack);
            setResult(RESULT_OK, intent);
            finish();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
