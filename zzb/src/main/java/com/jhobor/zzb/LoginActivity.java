package com.jhobor.zzb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.zzb.base.BaseActivity;
import com.jhobor.zzb.base.BaseApp;
import com.jhobor.zzb.utils.VerifyUtil;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    EditText phoneNum,pwd;
    Button login;
    TextView buyVIP,forget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneNum = (EditText) findViewById(R.id.phoneNum);
        pwd = (EditText) findViewById(R.id.pwd);
        login = (Button) findViewById(R.id.login);
        buyVIP = (TextView) findViewById(R.id.buyVIP);
        forget = (TextView) findViewById(R.id.forget);

        login.setOnClickListener(this);
        buyVIP.setOnClickListener(this);
        forget.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==login){
            String strPhone = phoneNum.getText().toString().trim();
            String strPwd = pwd.getText().toString().trim();
            if (!VerifyUtil.isMobile(strPhone)){
                Toast.makeText(this,"手机号码格式不正确",Toast.LENGTH_LONG).show();
                phoneNum.requestFocus();
            }else if (!VerifyUtil.isPass(strPwd)){
                Toast.makeText(this,"密码长度应为 6 到 18位",Toast.LENGTH_LONG).show();
                pwd.requestFocus();
            }else {
                BaseApp.sp.edit().putString("token","123456").apply();
                setResult(RESULT_OK);
                finish();
            }
        }else if (v==buyVIP){
            startActivity(new Intent(this,BuyVipActivity.class));
        }else if (v==forget){
            startActivity(new Intent(this,ResetPwdActivity.class));
        }
    }
}
