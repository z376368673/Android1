package com.jhobor.ddc.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jhobor.ddc.R;
import com.jhobor.ddc.adapter.MyReputationBaseAdapter;
import com.jhobor.ddc.entity.Msg;

import java.util.ArrayList;
import java.util.List;

public class MyReputationActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView topArrow;
    TextView topTitle;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reputation);

        initView();
        handleEvt();
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);

    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        topTitle = (TextView) findViewById(R.id.topTitle);
        listView = (ListView) findViewById(R.id.listView);

        topTitle.setText("我的信誉");
        List<Msg> msgList = getReputationMsgData();
        listView.setAdapter(new MyReputationBaseAdapter(msgList, this));
    }

    private List<Msg> getReputationMsgData() {
        List<Msg> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            if (i % 3 == 0) {
                list.add(new Msg(i + 1, "-5", "未按时完成派送订单，客户投诉", "2017-01-13"));
            } else {
                list.add(new Msg(i + 1, "+5", "按时完成派送订单，客户好评", "2017-01-13"));
            }
        }
        return list;
    }

    @Override
    public void onClick(View v) {
        if (v == topArrow) {
            finish();
        }
    }
}
