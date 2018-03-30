package com.jhobor.ddc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.jhobor.ddc.R;
import com.jhobor.ddc.base.BaseApplication;

public class SystemSettingActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView topArrow;
    TextView topTitle;
    TableRow accountAndSafe, knowUs, openShop, about;
    Button quit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_setting);

        initView();
        handleEvt();
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);
        accountAndSafe.setOnClickListener(this);
        knowUs.setOnClickListener(this);
        openShop.setOnClickListener(this);
        about.setOnClickListener(this);
        quit.setOnClickListener(this);
    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        topTitle = (TextView) findViewById(R.id.topTitle);
        accountAndSafe = (TableRow) findViewById(R.id.accountAndSafe);
        knowUs = (TableRow) findViewById(R.id.knowUs);
        openShop = (TableRow) findViewById(R.id.openShop);
        about = (TableRow) findViewById(R.id.about);
        quit = (Button) findViewById(R.id.quit);

        topTitle.setText("系统设置");
    }

    @Override
    public void onClick(View v) {
        if (v == topArrow) {
            finish();
        } else if (v == accountAndSafe) {
            Intent intent = new Intent(this, AccountAndSafeActivity.class);
            startActivity(intent);
        } else if (v == knowUs) {
            Intent intent = new Intent(this, KnowUsActivity.class);
            startActivity(intent);
        } else if (v == openShop) {

        } else if (v == about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        } else if (v == quit) {
            BaseApplication.dataMap.put("logout", true);
            finish();
        }
    }
}
