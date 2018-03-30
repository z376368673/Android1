package com.jhobor.fortune.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jhobor.fortune.R;
import com.jhobor.fortune.utils.BarUtil;
import com.jhobor.fortune.utils.HideIMEUtil;

/**
 * Author     Qijing
 * Created by YQJ on 2018/3/27.
 * Description:
 */
public class MyAccountActivity extends AppCompatActivity implements BarUtil.RightClick, View.OnClickListener {


    private RecyclerView mRvAccount;
    private TextView mAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        HideIMEUtil.wrap(this);
        BarUtil.topBar(this, "账户信息");
        //BarUtil.topBarRight(this, true, "添加");
        mRvAccount = (RecyclerView) findViewById(R.id.account_rv);

        mAdd = (TextView) findViewById(R.id.add);
        BarUtil.setmClick(this);
        mAdd.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void click() {

    }

    @Override
    public void onClick(View v) {
        if (v == mAdd) {
            Intent intent = new Intent(this, AddBankCardActivity.class);
            startActivity(intent);
        }
    }
}
