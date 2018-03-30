package com.jhobor.ddc.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jhobor.ddc.R;
import com.jhobor.ddc.adapter.SendingMsgBaseAdapter;
import com.jhobor.ddc.entity.Msg;

import java.util.ArrayList;
import java.util.List;

public class SendingMsgActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView topArrow;
    TextView topTitle;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sending_msg);

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

        topTitle.setText("订单详情");
        List<Msg> msgList = getMsgData();
        listView.setAdapter(new SendingMsgBaseAdapter(msgList, this));
    }

    private List<Msg> getMsgData() {
        List<Msg> msgList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            msgList.add(new Msg(i + 1, "订单提交成功" + i, "单号：24854548978945134，请耐心等待商家确认", "2017-01-10 14:36:24"));
        }
        return msgList;
    }

    @Override
    public void onClick(View v) {
        if (v == topArrow) {
            finish();
        }
    }
}
