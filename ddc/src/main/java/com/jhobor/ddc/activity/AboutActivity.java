package com.jhobor.ddc.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhobor.ddc.R;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView topArrow;
    TextView topTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
        handleEvt();
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);
    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        topTitle = (TextView) findViewById(R.id.topTitle);

        topTitle.setText("关于");
    }

    @Override
    public void onClick(View v) {
        if (v == topArrow) {
            finish();
        }
    }
}
