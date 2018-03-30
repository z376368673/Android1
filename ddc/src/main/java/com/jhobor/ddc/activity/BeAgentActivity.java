package com.jhobor.ddc.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.ddc.R;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.entity.UserInfo;
import com.jhobor.ddc.utils.ErrorUtil;
import com.jhobor.ddc.utils.VerifyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BeAgentActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView topArrow;
    TextView topTitle, getCode;
    EditText userName, phone, addr, code;
    Button ok;
    Spinner type;

    List<String> items;
    int flag = 0;
    int time = 60;
    String sms = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_be_agent);

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
        userName = (EditText) findViewById(R.id.userName);
        phone = (EditText) findViewById(R.id.phone);
        addr = (EditText) findViewById(R.id.addr);
        code = (EditText) findViewById(R.id.code);
        ok = (Button) findViewById(R.id.ok);
        type = (Spinner) findViewById(R.id.type);

        topTitle.setText("成为代理商");
        UserInfo userInfo = (UserInfo) BaseApplication.dataMap.get("userInfo");
        phone.setText(userInfo.getAccount());
        phone.setFocusable(false);
        items = getItemData();
        type.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items));
    }

    private List<String> getItemData() {
        List<String> list = new ArrayList<>();
        list.add("农产品省级代理");
        list.add("农产品镇级代理");
        list.add("附近店铺省级代理");
        list.add("附近店铺镇级代理");
        return list;
    }

    @Override
    public void onClick(View v) {
        if (v == topArrow) {
            finish();
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
                            Toast.makeText(BeAgentActivity.this, "这个手机号码还未注册", Toast.LENGTH_SHORT).show();
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
            String userNameText = userName.getText().toString().trim();
            String phoneText = phone.getText().toString();
            String addrText = addr.getText().toString().trim();
            String codeText = code.getText().toString();
            if (userNameText.isEmpty()) {
                Toast.makeText(this, "名字不可为空！", Toast.LENGTH_SHORT).show();
                userName.requestFocus();
            } else if (!VerifyUtil.isMobile(phoneText)) {
                Toast.makeText(this, "手机号码格式不正确！", Toast.LENGTH_SHORT).show();
                phone.requestFocus();
            } else if (!codeText.equals(sms) || codeText.isEmpty()) {
                Toast.makeText(this, "验证码不正确！", Toast.LENGTH_SHORT).show();
                code.requestFocus();
            } else if (addrText.length() < 12) {
                Toast.makeText(this, "地址请具体到省、市、区、街道", Toast.LENGTH_SHORT).show();
                addr.requestFocus();
            } else {
                int position = type.getSelectedItemPosition();
                String s = items.get(position);
                String uuid = (String) BaseApplication.dataMap.get("token");
                BaseApplication.iService.applyForStore(uuid, userNameText, phoneText, s, addrText).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                    @Override
                    public void parse(String data) {
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            int msg = jsonObject.getInt("msg");
                            int isLogin = jsonObject.getInt("isLogin");
                            if (msg == 1) {
                                Toast.makeText(BeAgentActivity.this, "已提交申请，请静待客服人员联系", Toast.LENGTH_LONG).show();
                                finish();
                            } else if (msg == 0) {
                                Toast.makeText(BeAgentActivity.this, "已申请过了，不可重复申请", Toast.LENGTH_LONG).show();
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
