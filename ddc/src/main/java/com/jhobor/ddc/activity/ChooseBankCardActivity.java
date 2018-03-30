package com.jhobor.ddc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jhobor.ddc.R;
import com.jhobor.ddc.adapter.ChooseBankCardBaseAdapter;
import com.jhobor.ddc.entity.BankCard;

import java.util.ArrayList;
import java.util.List;

public class ChooseBankCardActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    ImageView topArrow;
    TextView topTitle, done;
    ListView listView;

    List<BankCard> bankCardList;
    ChooseBankCardBaseAdapter adapter;
    int curPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_bank_card);

        initView();
        handleEvt();
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);
        done.setOnClickListener(this);
        listView.setOnItemClickListener(this);
    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        topTitle = (TextView) findViewById(R.id.topTitle);
        done = (TextView) findViewById(R.id.done);
        listView = (ListView) findViewById(R.id.listView);

        topTitle.setText("选择银行卡");
        done.setText("添加");
        done.setVisibility(View.VISIBLE);
        bankCardList = getBankCardData();
        adapter = new ChooseBankCardBaseAdapter(bankCardList, this);
        listView.setAdapter(adapter);
    }

    private List<BankCard> getBankCardData() {
        List<BankCard> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            list.add(new BankCard(i + 1, i + 1, "中国银行", "光明支行", 0, "123456789123456789"));
        }
        return list;
    }

    @Override
    public void onClick(View v) {
        if (v == topArrow) {
            finish();
        } else if (v == done) {
            Intent intent = new Intent(this, BindCardActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BankCard bankCard = bankCardList.get(position);
        if (bankCard.getChosen() == 0) {
            if (curPos != -1) {
                bankCardList.get(curPos).setChosen(0);
            }
            bankCard.setChosen(1);
            curPos = position;
            adapter.notifyDataSetChanged();
        }
    }
}
