package com.jhobor.fortune.oldui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jhobor.fortune.R;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.utils.BarUtil;
import com.jhobor.fortune.utils.CheckUtil;
import com.jhobor.fortune.utils.ErrorUtil;
import com.jhobor.fortune.utils.HideIMEUtil;
import com.jhobor.fortune.utils.TextUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class ResetActivity extends AppCompatActivity implements View.OnClickListener {
    EditText pass, confirmPass;
    Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        HideIMEUtil.wrap(this);

        pass = (EditText) findViewById(R.id.pass);
        confirmPass = (EditText) findViewById(R.id.confirmPass);
        ok = (Button) findViewById(R.id.ok);

        BarUtil.topBar(this, "重置密码");
        ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == ok) {
            Object[] objectArr = TextUtil.arrange(pass, confirmPass);
            String[] contentArr = (String[]) objectArr[0];
            boolean[] itemChecks = {
                    CheckUtil.isPass(contentArr[0]),
                    CheckUtil.isSame(contentArr[0], contentArr[1]),
            };
            String[] itemTips = {
                    "密码长度为6-18",
                    "密码和确认密码不一致",
            };
            int i = CheckUtil.checkAll(itemChecks);
            if (i < itemChecks.length) {
                Toast.makeText(this, itemTips[i], Toast.LENGTH_SHORT).show();
                EditText[] editTextArr = (EditText[]) objectArr[1];
                editTextArr[i].requestFocus();
            } else {
                String mobile = (String) BaseApplication.dataMap.get("mobile");
                restPassWord(mobile,contentArr[0]);
            }
        }
    }

    public void restPassWord(String mobile,String pwd){
        BaseApplication.iService.resetPass(mobile, pwd).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int msg = jsonObject.getInt("msg");
                    if (msg == 1) {
                        Toast.makeText(ResetActivity.this, "重置密码成功", Toast.LENGTH_SHORT).show();
                        sendBroadcast(new Intent("destory"));
                        finish();
                    } else {
                        Toast.makeText(ResetActivity.this, "重置密码失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(ResetActivity.this, e);
                }
            }
        }));
    }

}
