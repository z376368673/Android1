package com.jhobor.fortune.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.fortune.R;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.dialog.LoadingDialog;
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
    double integral = 0;
    int type = 1;

    LoadingDialog dialog ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);
        HideIMEUtil.wrap(this);
        dialog = new LoadingDialog(this);
        integral = getIntent().getDoubleExtra("JF",0);
        type = getIntent().getIntExtra("TYPE",1);
        mMoney = (EditText) findViewById(R.id.with_draw_et);
        mAccount = (EditText) findViewById(R.id.with_draw_account);
        mPsd = (EditText) findViewById(R.id.with_draw_psd);
        mSubbmit = (TextView) findViewById(R.id.with_draw_submmit);
        mSubbmit.setOnClickListener(this);
        switch (type){
            case 1:
                BarUtil.topBar(this, "业绩积分转让");
                break;
            case 2:
                BarUtil.topBar(this, "电商积分转让");
                break;
            case 3:
                BarUtil.topBar(this, "服务积分转让");
                break;
        }
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
            if (Integer.parseInt(trim)>integral) {
                Toast.makeText(this, "积分不足,无法转让", Toast.LENGTH_SHORT).show();
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
            dialog.show();
            switch (type){
                case 1:
                    integralTransfer(psd,account,trim);
                    break;
                case 2:
                    calculusTransfer(psd,account,trim);
                    break;
                case 3:
                    reportTransfer(psd,account,trim);
                    break;
            }
        }
    }

    //租车积分转让
    private void integralTransfer(String psd,String account,String trim){
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
                }finally {
                    dialog.dismiss();
                }
            }
        }));
    }

    //商城积分转让
    private void calculusTransfer(String psd,String account,String trim){
        String uuid = (String) BaseApplication.dataMap.get("token");
        BigDecimal b = new BigDecimal(trim);
        b = b.setScale(2, BigDecimal.ROUND_DOWN);
        BaseApplication.iService.calculusTransfer(uuid, psd, account, b).enqueue(new RetrofitCallback(this, new RetrofitCallback.DataParser() {
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
                }finally {
                    dialog.dismiss();
                }
            }
        }));
    }
    //服务积分转让
    private void reportTransfer(String psd,String account,String trim){
        String uuid = (String) BaseApplication.dataMap.get("token");
        BigDecimal b = new BigDecimal(trim);
        b = b.setScale(2, BigDecimal.ROUND_DOWN);
        BaseApplication.iService.reportTransfer(uuid, psd, account, b).enqueue(new RetrofitCallback(this, new RetrofitCallback.DataParser() {
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
                }finally {
                    dialog.dismiss();
                }
            }
        }));
    }
}

