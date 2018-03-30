package com.jhobor.fortune;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.utils.BarUtil;
import com.jhobor.fortune.utils.CheckUtil;
import com.jhobor.fortune.utils.CodeUtil;
import com.jhobor.fortune.utils.ErrorUtil;
import com.jhobor.fortune.utils.HideIMEUtil;
import com.jhobor.fortune.utils.MD5Util;
import com.jhobor.fortune.utils.TextUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class RegActivity extends AppCompatActivity implements View.OnClickListener {
    EditText mobile, code, pass, confirmPass, realName, refMobile;
    TextView getCode;
    Button reg, toLogin;

    boolean isGetCode = false;
    String strMobile, valifyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        HideIMEUtil.wrap(this);

        mobile = (EditText) findViewById(R.id.mobile);
        code = (EditText) findViewById(R.id.code);
        pass = (EditText) findViewById(R.id.pass);
        confirmPass = (EditText) findViewById(R.id.confirmPass);
        realName = (EditText) findViewById(R.id.realName);
        refMobile = (EditText) findViewById(R.id.refMobile);
        getCode = (TextView) findViewById(R.id.getCode);
        reg = (Button) findViewById(R.id.reg);
        toLogin = (Button) findViewById(R.id.toLogin);

        BarUtil.topBar(this, "注册");
        getCode.setOnClickListener(this);
        reg.setOnClickListener(this);
        toLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == getCode) {
            strMobile = mobile.getText().toString();
            if (CheckUtil.isMobile(strMobile)) {
                if (!CodeUtil.isRun) {
                    BaseApplication.iService.registerVerify(strMobile).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                        @Override
                        public void parse(String data) {
                            try {
                                JSONObject jsonObject = new JSONObject(data);
                                int msg = jsonObject.getInt("msg");
                                if (msg == 1) {
                                    valifyCode = jsonObject.getString("sms");
                                } else if (msg == 0) {
                                    Toast.makeText(RegActivity.this, "该手机号码已注册", Toast.LENGTH_SHORT).show();
                                    CodeUtil.isRun = false;
                                } else {
                                    Toast.makeText(RegActivity.this, "推荐手机号码还未注册，不能作为推荐号码", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                ErrorUtil.retrofitResponseParseFail(RegActivity.this, e);
                            }
                        }
                    }));
                    isGetCode = true;
                }
                CodeUtil.verifyCode(getCode);
            } else {
                mobile.requestFocus();
                Toast.makeText(this, "手机号码不合法", Toast.LENGTH_SHORT).show();
            }
        } else if (v == reg) {
            if (isGetCode) {
                Object[] objectArr = TextUtil.arrange(mobile, code, pass, realName, confirmPass, refMobile);
                String[] contentArr = (String[]) objectArr[0];
                boolean[] itemChecks = {
                        CheckUtil.isMobile(contentArr[0]) && CheckUtil.isSame(contentArr[0], strMobile),
                        CheckUtil.isValifyCode(contentArr[1]) && CheckUtil.isSame(contentArr[1], valifyCode),
                        CheckUtil.isPass(contentArr[2]),
                        CheckUtil.isSame(contentArr[2], contentArr[4]),
                        //CheckUtil.isName(contentArr[4]),
                        CheckUtil.isMobile(contentArr[5]) || contentArr[5].isEmpty()
                };
                String[] itemTips = {
                        "手机号码不正确",
                        "验证码不正确",
                        "密码长度为6-18",
                        "密码和确认密码不一致",
                        "名字格式不正确",
                        "推荐人手机号码格式不正确",
                };
                int i = CheckUtil.checkAll(itemChecks);
                if (i < itemChecks.length) {
                    Toast.makeText(this, itemTips[i], Toast.LENGTH_SHORT).show();
                    EditText[] editTextArr = (EditText[]) objectArr[1];
                    editTextArr[i].requestFocus();
                } else {
                    BaseApplication.iService.register(contentArr[0], contentArr[2], /*contentArr[4],*/
                            contentArr[5]).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                        @Override
                        public void parse(String data) {
                            try {
                                JSONObject jsonObject = new JSONObject(data);
                                int msg = jsonObject.getInt("msg");
                                if (msg == 1) {
                                    Toast.makeText(RegActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else if (msg == 0) {
                                    Toast.makeText(RegActivity.this, "注册失败，注册手机号码已存在", Toast.LENGTH_SHORT).show();
                                } else if (msg == 2) {
                                    Toast.makeText(RegActivity.this, "注册失败，推荐人手机号码不存在", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                ErrorUtil.retrofitResponseParseFail(RegActivity.this, e);
                            }
                        }
                    }));
                }
            } else {
                Toast.makeText(this, "请先获取验证码", Toast.LENGTH_SHORT).show();
            }
        } else if (v == toLogin) {
            finish();
        }
    }
}
