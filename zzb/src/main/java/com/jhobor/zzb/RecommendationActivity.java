package com.jhobor.zzb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.zzb.base.BaseWithHeaderActivity;

public class RecommendationActivity extends BaseWithHeaderActivity implements View.OnClickListener {
    TextView testimonial,rewardStandard,rewardMoney,myReward,myRewardMoney,withdraw;
    ImageView qrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fillContent(R.layout.activity_recommendation);

        testimonial = (TextView) findViewById(R.id.testimonial);
        rewardStandard = (TextView) findViewById(R.id.rewardStandard);
        rewardMoney = (TextView) findViewById(R.id.rewardMoney);
        myReward = (TextView) findViewById(R.id.myReward);
        myRewardMoney = (TextView) findViewById(R.id.myRewardMoney);
        withdraw = (TextView) findViewById(R.id.withdraw);
        qrCode = (ImageView) findViewById(R.id.qrCode);

        testimonial.setOnClickListener(this);
        qrCode.setOnClickListener(this);
        withdraw.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==qrCode){
            startActivity(new Intent(this,QrCodeActivity.class));
        }else if (v==withdraw){
            try {
                String str = myRewardMoney.getText().toString();
                int i = str.indexOf("元");
                String substring = str.substring(0, i);
                int money = Integer.parseInt(substring);
                if (money>0){
                    startActivity(new Intent(this,WithdrawActivity.class));
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this,"不可提现",Toast.LENGTH_SHORT).show();
            }
        }else if (v==testimonial){
            startActivity(new Intent(this,TypeTestimonialActivity.class));
        }
    }
}
