package com.jhobor.zzb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jhobor.zzb.base.BaseWithHeaderActivity;
import com.jhobor.zzb.utils.CodeUtil;
import com.jhobor.zzb.utils.VerifyUtil;

public class ChangeAccount2Activity extends BaseWithHeaderActivity implements View.OnClickListener {
    TextView getCode,originMobile;
    EditText phoneNum;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fillContent(R.layout.activity_change_account);
        hideRight();

        getCode = (TextView) findViewById(R.id.getCode);
        originMobile = (TextView) findViewById(R.id.originMobile);
        phoneNum = (EditText) findViewById(R.id.phoneNum);
        next = (Button) findViewById(R.id.next);

        originMobile.setText("新手机号");
        phoneNum.setHint("在此输入新的手机号码");
        next.setText("提交");
        getCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==getCode){
            CodeUtil.getVerifyCode(getCode);
        }else if (v==next){
            String strPhone = phoneNum.getText().toString();
            if (VerifyUtil.isMobile(strPhone)){

            }
        }
    }
}
