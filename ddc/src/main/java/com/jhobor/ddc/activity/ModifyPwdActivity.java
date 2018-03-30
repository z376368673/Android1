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

public class ModifyPwdActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView topArrow;
    TextView topTitle, getCode;
    EditText phone, password, newPassword, rePwd, code;
    Button ok;

    int flag = 0;
    int time = 60;
    String sms = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);

        initView();
        handleEvt();
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);
        getCode.setOnClickListener(this);
        ok.setOnClickListener(this);
    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        topTitle = (TextView) findViewById(R.id.topTitle);
        getCode = (TextView) findViewById(R.id.getCode);
        phone = (EditText) findViewById(R.id.phone);
        password = (EditText) findViewById(R.id.password);
        newPassword = (EditText) findViewById(R.id.newPassword);
        rePwd = (EditText) findViewById(R.id.rePwd);
        code = (EditText) findViewById(R.id.code);
        ok = (Button) findViewById(R.id.ok);

        topTitle.setText("修改密码");
    }

    @Override
    public void onClick(View v) {
        if (v == topArrow) {
            finish();
        } else if (v == ok) {
            if (flag == 0) {
                Toast.makeText(this, "请获取验证码", Toast.LENGTH_SHORT).show();
                return;
            }
            String phoneText = phone.getText().toString();
            String curPwd = password.getText().toString();
            String newPwd = newPassword.getText().toString();
            String rePwdText = rePwd.getText().toString();
            String codeText = code.getText().toString();
            if (!VerifyUtil.isMobile(phoneText)) {
                Toast.makeText(this, "手机号码不正确", Toast.LENGTH_SHORT).show();
                phone.requestFocus();
            } else if (curPwd.length() < 6) {
                Toast.makeText(this, "密码不正确", Toast.LENGTH_SHORT).show();
                password.requestFocus();
            } else if (newPwd.length() < 6) {
                Toast.makeText(this, "新密码最少6位", Toast.LENGTH_SHORT).show();
                newPassword.requestFocus();
            } else if (!newPwd.equals(rePwdText)) {
                Toast.makeText(this, "确认密码与新密码不一致", Toast.LENGTH_SHORT).show();
                rePwd.requestFocus();
            } else if (!codeText.equals(sms) || codeText.isEmpty()) {
                Toast.makeText(this, "验证码不正确", Toast.LENGTH_SHORT).show();
                code.requestFocus();
            } else {
                BaseApplication.iService.updatePwd(phoneText, MD5Util.encode(curPwd), MD5Util.encode(newPwd)).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                    @Override
                    public void parse(String data) {
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            int msg = jsonObject.getInt("msg");
                            if (msg == 3) {
                                Toast.makeText(ModifyPwdActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (msg == 2) {
                                Toast.makeText(ModifyPwdActivity.this, "密码不正确", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                        }
                    }
                }));
            }
        } else if (v == getCode) {
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
                            Toast.makeText(ModifyPwdActivity.this, "这个手机号码还未注册", Toast.LENGTH_SHORT).show();
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
        }
    }
}
