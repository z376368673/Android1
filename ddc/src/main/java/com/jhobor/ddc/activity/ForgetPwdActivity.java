package com.jhobor.ddc.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class ForgetPwdActivity extends AppCompatActivity implements View.OnClickListener {
    EditText phone, code, password, rePwd;
    TextView getCode, topTitle;
    Button ok;
    ImageView topArrow;

    int flag = 0;
    int time = 60;
    String sms = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);

        initView();
        handleEvt();
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);
        getCode.setOnClickListener(this);
        ok.setOnClickListener(this);
    }

    private void initView() {
        phone = (EditText) findViewById(R.id.phone);
        code = (EditText) findViewById(R.id.code);
        password = (EditText) findViewById(R.id.password);
        rePwd = (EditText) findViewById(R.id.rePwd);
        getCode = (TextView) findViewById(R.id.getCode);
        topTitle = (TextView) findViewById(R.id.topTitle);
        ok = (Button) findViewById(R.id.ok);
        topArrow = (ImageView) findViewById(R.id.topArrow);

        topTitle.setText("忘记密码");
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
            BaseApplication.iService.getVerifyCode(phoneText).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                @Override
                public void parse(String data) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        int msg = jsonObject.getInt("msg");

                        if (msg != 1) {
                            Toast.makeText(ForgetPwdActivity.this, "这个手机号码还未注册", Toast.LENGTH_SHORT).show();
                            time = -1;
                        } else {
                            sms = jsonObject.getString("sms");
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

        } else if (v == ok) {
            if (flag == 0) {
                Toast.makeText(this, "请获取验证码", Toast.LENGTH_SHORT).show();
                return;
            }
            String phoneText = phone.getText().toString();
            String pwdText = password.getText().toString();
            String rePwdText = rePwd.getText().toString();
            String codeText = code.getText().toString();
            if (!VerifyUtil.isMobile(phoneText)) {
                Toast.makeText(this, "手机号码不正确", Toast.LENGTH_SHORT).show();
                phone.requestFocus();
            } else if (!codeText.equals(sms) || codeText.isEmpty()) {
                Toast.makeText(this, "验证码不正确", Toast.LENGTH_SHORT).show();
                code.requestFocus();
            } else if (pwdText.length() < 6) {
                Toast.makeText(this, "新密码最少6位", Toast.LENGTH_SHORT).show();
                password.requestFocus();
            } else if (!pwdText.equals(rePwdText)) {
                Toast.makeText(this, "确认密码与新密码不一致", Toast.LENGTH_SHORT).show();
                rePwd.requestFocus();
            } else {
                BaseApplication.iService.resetPwd(phoneText, MD5Util.encode(pwdText)).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                    @Override
                    public void parse(String data) {
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            int msg = jsonObject.getInt("msg");
                            if (msg == 1) {
                                Toast.makeText(ForgetPwdActivity.this, "重置成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (msg == 0) {
                                Toast.makeText(ForgetPwdActivity.this, "重置失败，可能该手机号未注册", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                        }
                    }
                }));
            }
        } else if (v == topArrow) {
            finish();
        }
    }
}

