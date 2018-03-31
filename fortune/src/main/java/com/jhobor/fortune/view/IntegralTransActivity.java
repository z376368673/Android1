package com.jhobor.fortune.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.fortune.R;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.utils.BarUtil;
import com.jhobor.fortune.utils.HideIMEUtil;
import com.jhobor.fortune.utils.VerifyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

/**
 * Author     Qijing
 * Created by YQJ on 2018/3/30.
 * Description:
 */
public class IntegralTransActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText mMoney;
    private EditText mAccount;
    private EditText mPsd;
    private TextView mSubbmit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);
        HideIMEUtil.wrap(this);
        BarUtil.topBar(this, "积分转让");
        mMoney = (EditText) findViewById(R.id.with_draw_et);
        mAccount = (EditText) findViewById(R.id.with_draw_account);
        mPsd = (EditText) findViewById(R.id.with_draw_psd);
        mSubbmit = (TextView) findViewById(R.id.with_draw_submmit);
        mSubbmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mSubbmit) {
            String trim = mMoney.getText().toString().trim();
            String account = mAccount.getText().toString().trim();
            String psd = mPsd.getText().toString().trim();
            if (trim.isEmpty()) {
                Toast.makeText(this, "输入金额不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!VerifyUtil.isPhone(account)) {
                Toast.makeText(this, "账户手机号有误", Toast.LENGTH_SHORT).show();
                return;
            }
            if (psd.isEmpty()) {
                Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            String uuid = (String) BaseApplication.dataMap.get("token");

            BigDecimal b = new BigDecimal(trim);
            b = b.setScale(2, BigDecimal.ROUND_DOWN);
            BaseApplication.iService.integralTransfer(uuid, psd, account, b).enqueue(new RetrofitCallback(this, new RetrofitCallback.DataParser() {
                @Override
                public void parse(String data) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        int msg = (int) jsonObject.opt("msg");

                        if (msg == 1) {
                            Toast.makeText(IntegralTransActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(IntegralTransActivity.this, jsonObject.getString("errorInfo"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }));

        }
    }
}

