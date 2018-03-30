package com.jhobor.ddc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.ddc.R;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.entity.Addr;
import com.jhobor.ddc.utils.ErrorUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EditShippingAddrActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    ImageView topArrow;
    TextView topTitle, done;
    EditText realName, location, extra, phone;
    RadioGroup gender;
    RadioButton man, woman;

    String genderText;
    Addr addr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shipping_addr);

        Intent intent = getIntent();
        addr = intent.getParcelableExtra("addr");
        initView();
        handleEvt();
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);
        done.setOnClickListener(this);
        gender.setOnCheckedChangeListener(this);
    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        topTitle = (TextView) findViewById(R.id.topTitle);
        done = (TextView) findViewById(R.id.done);
        location = (EditText) findViewById(R.id.location);
        realName = (EditText) findViewById(R.id.realName);
        extra = (EditText) findViewById(R.id.extra);
        phone = (EditText) findViewById(R.id.phone);
        gender = (RadioGroup) findViewById(R.id.gender);
        man = (RadioButton) findViewById(R.id.man);
        woman = (RadioButton) findViewById(R.id.woman);

        topTitle.setText("收货地址");
        done.setText("保存");
        done.setVisibility(View.VISIBLE);
        if (addr != null) {
            realName.setText(addr.getRealName());
            location.setText(addr.getAddress());
            phone.setText(addr.getPhone());
        }
    }

    @Override
    public void onClick(View v) {
        if (v == topArrow) {
            finish();
        } else if (v == done) {
            String realNameText = realName.getText().toString().trim();
            String locationText = location.getText().toString().trim();
            String extraText = extra.getText().toString().trim();
            String phoneText = phone.getText().toString();
            if (realNameText.length() < 2) {
                Toast.makeText(this, "收货人姓名不具体", Toast.LENGTH_SHORT).show();
                realName.requestFocus();
            } else if (locationText.length() < 5) {
                Toast.makeText(this, "收货地址不够详细", Toast.LENGTH_SHORT).show();
                location.requestFocus();
            } else if (extraText.length() < 5) {
                Toast.makeText(this, "补充地址不够详细", Toast.LENGTH_SHORT).show();
                extra.requestFocus();
            } else if (phoneText.length() != 11 || !phoneText.startsWith("1")) {
                Toast.makeText(this, "手机号码不正确", Toast.LENGTH_SHORT).show();
                phone.requestFocus();
            } else {
                String uuid = (String) BaseApplication.dataMap.get("token");
                if (addr == null) {
                    BaseApplication.iService.addAddr(uuid, realNameText, phoneText, locationText + extraText).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                        @Override
                        public void parse(String data) {
                            try {
                                JSONObject jsonObject = new JSONObject(data);
                                int isLogin = jsonObject.getInt("isLogin");
                                if (isLogin == 1) {
                                    int msg = jsonObject.getInt("msg");
                                    if (msg==1) {
                                        JSONArray addrList = jsonObject.getJSONArray("addrList");
                                        List<Addr> addrArrayList = new ArrayList<Addr>();
                                        for (int i = 0; i < addrList.length(); i++) {
                                            JSONArray jsonArray = addrList.getJSONArray(i);
                                            int id = jsonArray.getInt(0);
                                            String userName = jsonArray.getString(1);
                                            String phone = jsonArray.getString(2);
                                            String addr = jsonArray.getString(3);
                                            int state = jsonArray.getInt(4);
                                            addrArrayList.add(new Addr(id, userName, phone, addr, state));
                                        }
                                        BaseApplication.dataMap.put("addrArrayList", addrArrayList);
                                        Toast.makeText(getBaseContext(), "添加成功", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }else {
                                        Toast.makeText(getBaseContext(), "添加失败，请稍候再试", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(getBaseContext(), "未登录，请登录后在操作", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                            }
                        }
                    }));
                } else {
                    BaseApplication.iService.editAddr(uuid, addr.getId(), realNameText, phoneText, locationText + extraText).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                        @Override
                        public void parse(String data) {
                            try {
                                JSONObject jsonObject = new JSONObject(data);
                                int msg = jsonObject.getInt("msg");
                                int isLogin = jsonObject.getInt("isLogin");
                                if (msg == 1 && isLogin == 1) {
                                    JSONArray addrList = jsonObject.getJSONArray("addrList");
                                    List<Addr> addrArrayList = new ArrayList<Addr>();
                                    for (int i = 0; i < addrList.length(); i++) {
                                        JSONArray jsonArray = addrList.getJSONArray(i);
                                        int id = jsonArray.getInt(0);
                                        String userName = jsonArray.getString(1);
                                        String phone = jsonArray.getString(2);
                                        String addr = jsonArray.getString(3);
                                        int state = jsonArray.getInt(4);
                                        addrArrayList.add(new Addr(id, userName, phone, addr, state));
                                    }
                                    BaseApplication.dataMap.put("addrArrayList", addrArrayList);
                                    Toast.makeText(EditShippingAddrActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.man) {
            genderText = man.getText().toString();
        } else if (checkedId == R.id.woman) {
            genderText = woman.getText().toString();
        }
    }
}
