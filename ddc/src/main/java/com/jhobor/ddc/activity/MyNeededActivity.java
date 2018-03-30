package com.jhobor.ddc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jhobor.ddc.R;
import com.jhobor.ddc.adapter.MyNeededBaseAdapter;
import com.jhobor.ddc.entity.Needed;

import java.util.ArrayList;
import java.util.List;

public class MyNeededActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView topArrow;
    TextView topTitle, done;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_needed);

        initView();
        handleEvt();
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);
        done.setOnClickListener(this);
    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        topTitle = (TextView) findViewById(R.id.topTitle);
        done = (TextView) findViewById(R.id.done);
        listView = (ListView) findViewById(R.id.listView);

        topTitle.setText("我的需求");
        done.setText("发布");
        done.setVisibility(View.VISIBLE);
        List<Needed> neededList = getNeededData();
        listView.setAdapter(new MyNeededBaseAdapter(neededList, this));
    }

    private List<Needed> getNeededData() {
        List<Needed> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            list.add(new Needed(i + 1, "产品名称" + i, i + 1, "柔软，贴身，舒服，时尚，大方", "深圳市宝安区公明镇民生大道上辇村口明婕大厦1201", "2017-01-16"));
        }
        return list;
    }

    @Override
    public void onClick(View v) {
        if (v == topArrow) {
            finish();
        } else if (v == done) {
            Intent intent = new Intent(this, PublishDemandActivity.class);
            startActivity(intent);
        }
    }
}
