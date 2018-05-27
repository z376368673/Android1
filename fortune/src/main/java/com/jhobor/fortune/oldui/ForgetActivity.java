package com.jhobor.fortune.oldui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.fortune.R;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.utils.BarUtil;
import com.jhobor.fortune.utils.CheckUtil;
import com.jhobor.fortune.utils.CodeUtil;
import com.jhobor.fortune.utils.ErrorUtil;
import com.jhobor.fortune.utils.HideIMEUtil;
import com.jhobor.fortune.utils.TextUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgetActivity extends AppCompatActivity implements View.OnClickListener {
    EditText mobile, code, pass, pass_again;
    TextView getCode;
    Button next;
    boolean isGetCode = false;
    String strMobile;
    String valifyCode;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        HideIMEUtil.wrap(this);

        mobile = (EditText) findViewById(R.id.mobile);
        code = (EditText) findViewById(R.id.code);
        pass = (EditText) findViewById(R.id.pass);
        pass_again = (EditText) findViewById(R.id.pass_again);
        getCode = (TextView) findViewById(R.id.getCode);
        next = (Button) findViewById(R.id.next);
        BarUtil.topBar(this, "找回密码");
        getCode.setOnClickListener(this);
        next.setOnClickListener(this);
        registerReceiver(receiver, new IntentFilter("destory"));
    }

    @Override
    public void onClick(View v) {
        if (v == next) {
            if (isGetCode) {
                Object[] objectArr = TextUtil.arrange(code, pass, pass_again);
                String[] contentArr = (String[]) objectArr[0];
                boolean[] itemChecks = {
                        CheckUtil.isValifyCode(contentArr[0]) && CheckUtil.isSame(contentArr[0], valifyCode),
                        CheckUtil.isPass(contentArr[1]),
                        CheckUtil.isPass(contentArr[2]) && CheckUtil.isSame(contentArr[2], contentArr[1])
                };
                String[] itemTips = {
                        "验证码不正确",
                        "请输入6-16位",
                        "两次密码不一样"
                };
                int i = CheckUtil.checkAll(itemChecks);
                if (i < itemChecks.length) {
                    Toast.makeText(this, itemTips[i], Toast.LENGTH_SHORT).show();
                    EditText[] editTextArr = (EditText[]) objectArr[1];
                    editTextArr[i].requestFocus();
                } else {
                    //BaseApplication.dataMap.put("mobile", strMobile);
                    //startActivity(new Intent(this, ResetActivity.class));
                    restPassWord(strMobile, contentArr[2]);
                }
            } else {
                Toast.makeText(this, "请先获取验证码", Toast.LENGTH_SHORT).show();
            }
        } else if (v == getCode) {
            strMobile = mobile.getText().toString();
            if (CheckUtil.isMobile(strMobile)) {
                if (!CodeUtil.isRun) {
                    BaseApplication.iService.otherVerify(strMobile).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                        @Override
                        public void parse(String data) {
                            try {
                                JSONObject jsonObject = new JSONObject(data);
                                int msg = jsonObject.getInt("msg");
                                if (msg == 1) {
                                    valifyCode = jsonObject.getString("sms");
                                } else {
                                    Toast.makeText(ForgetActivity.this, "该手机号码未在平台上注册", Toast.LENGTH_SHORT).show();
                                    CodeUtil.isRun = false;
                                }
                            } catch (JSONException e) {
                                ErrorUtil.retrofitResponseParseFail(ForgetActivity.this, e);
                            }
                            isGetCode = true;
                        }
                    }));
                }
                CodeUtil.verifyCode(getCode);
            } else {
                mobile.requestFocus();
                Toast.makeText(this, "手机号码不合法", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 重置密码
     *
     * @param mobile
     * @param pwd
     */
    public void restPassWord(String mobile, String pwd) {
        BaseApplication.iService.resetPass(mobile, pwd).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int msg = jsonObject.getInt("msg");
                    if (msg == 1) {
                        Toast.makeText(ForgetActivity.this, "重置密码成功", Toast.LENGTH_SHORT).show();
                        sendBroadcast(new Intent("destory"));
                        finish();
                    } else {
                        Toast.makeText(ForgetActivity.this, "重置密码失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(ForgetActivity.this, e);
                }
            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseApplication.dataMap.remove("mobile");
        unregisterReceiver(receiver);
    }
}
