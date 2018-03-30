package com.jhobor.ddc.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.ddc.R;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.utils.ErrorUtil;
import com.jhobor.ddc.utils.MD5Util;
import com.jhobor.ddc.utils.VerifyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    EditText phone, password, code;
    TextView getCode;
    Button register;

    int flag = 0;
    int time = 60;
    String sms = "";
    String regPhone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
        handleEvt();
    }

    private void handleEvt() {
        getCode.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    private void initView() {
        phone = (EditText) findViewById(R.id.phone);
        password = (EditText) findViewById(R.id.password);
        code = (EditText) findViewById(R.id.code);
        getCode = (TextView) findViewById(R.id.getCode);
        register = (Button) findViewById(R.id.register);
    }

    @Override
    public void onClick(View v) {
        if (v == getCode) {
            if (flag == 1) {
                Toast.makeText(this, "请稍候再重新获取", Toast.LENGTH_SHORT).show();
                return;
            }
            flag = 1;
            String phoneText = phone.getText().toString();
            if (!VerifyUtil.isMobile(phoneText)) {
                Toast.makeText(this, "手机号码不正确", Toast.LENGTH_SHORT).show();
                phone.requestFocus();
                flag = 0;
                return;
            }
            BaseApplication.iService.getRegisterVerifyCode(phoneText).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                @Override
                public void parse(String data) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        int msg = jsonObject.getInt("msg");

                        if (msg == 3) {
                            sms = jsonObject.getString("sms");
                            regPhone = jsonObject.getString("mobile");
                        } else {
                            Toast.makeText(RegisterActivity.this, "这个手机号码已经注册过了", Toast.LENGTH_SHORT).show();
                            time = -1;
                        }
                    } catch (JSONException e) {
                        ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                    }
                }
            }));
            final Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (time > 0) {
                        getCode.setText(String.format(Locale.CHINA, "│ 剩余 %d s", time));
                        --time;
                        handler.postDelayed(this, 1000);
                    } else {
                        getCode.setText("│ 获取验证码");
                        if (time < 0) {
                            flag = 0;
                        } else {
                            flag = 2;
                        }
                        time = 60;
                    }
                }
            };
            handler.post(runnable);
        } else if (v == register) {
            if (flag == 0) {
                Toast.makeText(this, "请获取验证码", Toast.LENGTH_SHORT).show();
                return;
            }
            String phoneNo = phone.getText().toString();
            String codeText = code.getText().toString();
            String pwd = password.getText().toString();
            if (!VerifyUtil.isMobile(phoneNo)) {
                Toast.makeText(this, "手机号码不正确", Toast.LENGTH_SHORT).show();
                phone.requestFocus();
            } else if (!phoneNo.equals(regPhone)) {
                Toast.makeText(this, "该手机号码与获取验证码的手机号不一致", Toast.LENGTH_SHORT).show();
                phone.requestFocus();
            } else if (!codeText.equals(sms)) {
                Toast.makeText(this, "验证码不正确", Toast.LENGTH_SHORT).show();
                code.requestFocus();
            } else if (pwd.length() < 6) {
                Toast.makeText(this, "密码长度应该大于等于6", Toast.LENGTH_SHORT).show();
                password.requestFocus();
            } else {
                pwd = MD5Util.encode(pwd);
                Log.i("**********>>", phoneNo + " ; " + pwd);
                BaseApplication.iService.register(phoneNo, pwd).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                    @Override
                    public void parse(String data) {
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            int msg = jsonObject.getInt("msg");
                            if (msg == 3) {
                                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "注册失败，该手机号码已经注册过了", Toast.LENGTH_SHORT).show();
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
