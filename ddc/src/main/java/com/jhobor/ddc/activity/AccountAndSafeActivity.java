package com.jhobor.ddc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.jhobor.ddc.R;

public class AccountAndSafeActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView topArrow;
    TextView topTitle, account, phone;
    TableRow modifyPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_and_safe);

        initView();
        handleEvt();
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);
        modifyPwd.setOnClickListener(this);
    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        topTitle = (TextView) findViewById(R.id.topTitle);
        account = (TextView) findViewById(R.id.account);
        phone = (TextView) findViewById(R.id.phone);
        modifyPwd = (TableRow) findViewById(R.id.modifyPwd);

        topTitle.setText("帐号和安全");
    }

    @Override
    public void onClick(View v) {
        if (v == topArrow) {
            finish();
        } else if (v == modifyPwd) {
            Intent intent = new Intent(this, ModifyPwdActivity.class);
            startActivity(intent);
        }
    }
}
