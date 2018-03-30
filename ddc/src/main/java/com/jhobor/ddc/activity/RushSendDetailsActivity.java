package com.jhobor.ddc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhobor.ddc.R;
import com.jhobor.ddc.fragment.RushSendFragment;

public class RushSendDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView topArrow;
    TextView topTitle;
    RushSendFragment fragment;

    int ordersId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rush_send_details);

        Intent intent = getIntent();
        ordersId = intent.getIntExtra("ordersId", 0);
        initView();
        handleEvt();
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);
    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        topTitle = (TextView) findViewById(R.id.topTitle);

        topTitle.setText("订单详情");
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        fragment = new RushSendFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("ordersId", ordersId);
        bundle.putString("showDetails", "Y");
        fragment.setArguments(bundle);
        transaction.add(R.id.container, fragment);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        if (v == topArrow) {
            finish();
        }
    }
}
