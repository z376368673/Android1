package com.jhobor.zzb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.zzb.base.BaseActivity;
import com.jhobor.zzb.base.BaseWithHeaderActivity;
import com.jhobor.zzb.utils.CodeUtil;
import com.jhobor.zzb.utils.VerifyUtil;

public class ChangeAccountActivity extends BaseWithHeaderActivity implements View.OnClickListener {
    TextView getCode;
    EditText phoneNum;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fillContent(R.layout.activity_change_account);
        hideRight();
        showTitle("变更帐号");

        getCode = (TextView) findViewById(R.id.getCode);
        phoneNum = (EditText) findViewById(R.id.phoneNum);
        next = (Button) findViewById(R.id.next);

        getCode.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==next){
            String strPhone = phoneNum.getText().toString();
            if (VerifyUtil.isMobile(strPhone)){
                startActivity(new Intent(this,ChangeAccount2Activity.class));
            }else {
                Toast.makeText(this,"手机未验证通过",Toast.LENGTH_SHORT).show();
            }
        }else if (v==getCode){
            CodeUtil.getVerifyCode(getCode);
        }
    }
}
