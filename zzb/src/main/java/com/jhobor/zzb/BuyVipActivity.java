package com.jhobor.zzb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.zzb.base.BaseWithHeaderActivity;
import com.jhobor.zzb.utils.CodeUtil;
import com.jhobor.zzb.utils.VerifyUtil;

public class BuyVipActivity extends BaseWithHeaderActivity implements View.OnClickListener {
    EditText phoneNum,code,pwd;
    TextView getCode,handBook;
    Button buy;
    CheckBox agree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fillContent(R.layout.activity_buy_vip);
        hideRight();

        phoneNum = (EditText) findViewById(R.id.phoneNum);
        code = (EditText) findViewById(R.id.code);
        pwd = (EditText) findViewById(R.id.pwd);
        getCode = (TextView) findViewById(R.id.getCode);
        handBook = (TextView) findViewById(R.id.handBook);
        buy = (Button) findViewById(R.id.buy);
        agree = (CheckBox) findViewById(R.id.agree);

        getCode.setOnClickListener(this);
        buy.setOnClickListener(this);
        handBook.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==getCode){
            CodeUtil.getVerifyCode(getCode);
        }else if (v==buy){
            String strPhone = phoneNum.getText().toString().trim();
            String strCode = code.getText().toString().trim();
            String strPwd = pwd.getText().toString().trim();
            if (!VerifyUtil.isMobile(strPhone)){
                Toast.makeText(this,"手机号码格式不正确",Toast.LENGTH_LONG).show();
                phoneNum.requestFocus();
            }else if (!VerifyUtil.isValifyCode(strCode)){
                Toast.makeText(this,"验证码不正确",Toast.LENGTH_LONG).show();
                code.requestFocus();
            }else if (!VerifyUtil.isPass(strPwd)){
                Toast.makeText(this,"密码长度应为 6 到 18位",Toast.LENGTH_LONG).show();
                pwd.requestFocus();
            }else if (!agree.isChecked()){
                Toast.makeText(this,"购买会员需同意手册内容",Toast.LENGTH_LONG).show();
            }else {
                startActivity(new Intent(this,PayForVipActivity.class));
                finish();
            }
        }else if (v==handBook){

        }
    }
}
