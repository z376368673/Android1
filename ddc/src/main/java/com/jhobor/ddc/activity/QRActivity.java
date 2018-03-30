package com.jhobor.ddc.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhobor.ddc.R;

public class QRActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView topArrow, userPic, qrCode;
    TextView topTitle, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        initView();
        handleEvt();
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);
    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        userPic = (ImageView) findViewById(R.id.userPic);
        qrCode = (ImageView) findViewById(R.id.qrCode);
        topTitle = (TextView) findViewById(R.id.topTitle);
        userName = (TextView) findViewById(R.id.userName);

        topTitle.setText("我的二维码");
    }

    @Override
    public void onClick(View v) {
        if (v == topArrow) {
            finish();
        }
    }
}
