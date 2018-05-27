package com.jhobor.fortune.oldui;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

/**
 * Author     Qijing
 * Created by YQJ on 2018/3/30.
 * Description:
 */
public class AddMoneyActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mAdd;
    private TextView mSubbmit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);
        HideIMEUtil.wrap(this);
        BarUtil.topBar(this, "增资");
        mAdd = (EditText) findViewById(R.id.add_et);
        mSubbmit = (TextView) findViewById(R.id.add_submmit);
        mSubbmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mSubbmit) {
            String trim = mAdd.getText().toString().trim();

            if (trim.isEmpty()) {
                Toast.makeText(AddMoneyActivity.this, "输入金额不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            int aDouble = Integer.valueOf(trim);
            if (aDouble < 2000) {
                Toast.makeText(AddMoneyActivity.this, "输入金额必须是2000元以上", Toast.LENGTH_SHORT).show();
                return;
            }
            if (aDouble % 1000 != 0) {
                Toast.makeText(AddMoneyActivity.this, "输入金额必须是1000的整数倍", Toast.LENGTH_SHORT).show();
                return;
            }


            String uuid = (String) BaseApplication.dataMap.get("token");

            BigDecimal b = new BigDecimal(trim);
            b = b.setScale(2, BigDecimal.ROUND_DOWN);
            double dou = b.doubleValue();
            BaseApplication.iService.addMoney(uuid, dou).enqueue(new RetrofitCallback(this, new RetrofitCallback.DataParser() {
                @Override
                public void parse(String data) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        int msg = (int) jsonObject.opt("msg");
                        if (msg == 1) {
                            Toast.makeText(AddMoneyActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                            mAdd.setText("");
                            finish();
                        } else {
                            String errorInfo = jsonObject.getString("errorInfo");
                            Toast.makeText(AddMoneyActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }));

        }
    }
}
