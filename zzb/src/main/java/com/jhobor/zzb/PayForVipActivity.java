package com.jhobor.zzb;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.zzb.base.BaseWithHeaderActivity;
import com.jhobor.zzb.base.BaseDialog;

public class PayForVipActivity extends BaseWithHeaderActivity implements View.OnClickListener{
    TextView feeOfYear,expires;
    CheckBox wechatPay;
    Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fillContent(R.layout.activity_pay_for_vip);
        hideRight();

        feeOfYear = (TextView) findViewById(R.id.feeOfYear);
        expires = (TextView) findViewById(R.id.expires);
        wechatPay = (CheckBox) findViewById(R.id.wechatPay);
        ok = (Button) findViewById(R.id.ok);

        ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==ok){
            if (wechatPay.isChecked()){
                BaseDialog.showInfo(this,"已为您开通会员！");
            }else {
                Toast.makeText(this,"请选择支付方式",Toast.LENGTH_LONG).show();
            }
        }
    }
}
