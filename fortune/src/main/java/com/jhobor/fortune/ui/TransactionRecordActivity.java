package com.jhobor.fortune.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.jhobor.fortune.R;
import com.jhobor.fortune.oldui.TransMoneyListActivity;
import com.jhobor.fortune.ui.IntegralRecordActivity;
import com.jhobor.fortune.utils.BarUtil;
import com.jhobor.fortune.utils.HideIMEUtil;

/**
 * 交易记录
 */
public class TransactionRecordActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout ll_zengzhi;
//    private LinearLayout ll_fenhong;
//    private LinearLayout ll_jfzengzhangd;
//    private LinearLayout ll_jftixian;
//    private LinearLayout ll_jfzhuangrang;
//    private LinearLayout ll_wjftixian;
//    private LinearLayout ll_wjfzhuangrang;

    private LinearLayout layout_zcjf,layout_dsjf,layout_fwjf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_record);
        HideIMEUtil.wrap(this);
        BarUtil.topBar(this, "交易记录");
        BarUtil.topBarRight(this, false, " ");
        initView();
    }

    private void initView() {
        ll_zengzhi = (LinearLayout) findViewById(R.id.ll_zengzhi);
//        ll_fenhong = (LinearLayout) findViewById(R.id.ll_fenhong);
//        ll_jfzengzhangd = (LinearLayout) findViewById(R.id.ll_jfzengzhangd);
//        ll_jftixian = (LinearLayout) findViewById(R.id.ll_jftixian);
//        ll_wjftixian = (LinearLayout) findViewById(R.id.ll_wjftixian);
//        ll_jfzhuangrang = (LinearLayout) findViewById(R.id.ll_jfzhuangrang);
//        ll_wjfzhuangrang = (LinearLayout) findViewById(R.id.ll_wjfzhuangrang);


        layout_zcjf = (LinearLayout) findViewById(R.id.layout_zcjf);
        layout_dsjf = (LinearLayout) findViewById(R.id.layout_dsjf);
        layout_fwjf = (LinearLayout) findViewById(R.id.layout_fwjf);

        layout_zcjf.setOnClickListener(this);
        layout_dsjf.setOnClickListener(this);
        layout_fwjf.setOnClickListener(this);


        ll_zengzhi.setOnClickListener(this);
//        ll_wjftixian.setOnClickListener(this);
//        ll_wjfzhuangrang.setOnClickListener(this);
//        ll_fenhong.setOnClickListener(this);
//        ll_jfzengzhangd.setOnClickListener(this);
//        ll_jftixian.setOnClickListener(this);
//        ll_jfzhuangrang.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;

        if (view == ll_zengzhi) {
            intent = new Intent(this, TransMoneyListActivity.class);
            intent.putExtra("Tag", 0);
            startActivity(intent);
        } else if (view == layout_zcjf) {
            intent = new Intent(this, IntegralRecordActivity.class);
            intent.putExtra("Tag", 0);
            startActivity(intent);
        }else if (view == layout_dsjf) {
            intent = new Intent(this, IntegralRecordActivity.class);
            intent.putExtra("Tag", 1);
            startActivity(intent);
        }else if (view == layout_fwjf) {
            intent = new Intent(this, IntegralRecordActivity.class);
            intent.putExtra("Tag", 2);
            startActivity(intent);
        }


//        else if (view == ll_fenhong) {
//            intent = new Intent(this, TransMoneyListActivity.class);
//            intent.putExtra("Tag", 1);
//            startActivity(intent);
//        } else if (view == ll_jfzengzhangd) {
//            intent = new Intent(this, TransJfListActivity.class);
//            intent.putExtra("Tag", 0);
//            startActivity(intent);
//        } else if (view == ll_jftixian) {
//            intent = new Intent(this, TransJfListActivity.class);
//            intent.putExtra("Tag", 1);
//            startActivity(intent);
//        } else if (view == ll_jfzhuangrang) {
//            intent = new Intent(this, TransJfListActivity.class);
//            intent.putExtra("Tag", 2);
//            startActivity(intent);
//        } else if (view == ll_wjftixian) {
//            intent = new Intent(this, TransWjfListActivity.class);
//            intent.putExtra("Tag", 1);
//            startActivity(intent);
//        } else if (view == ll_wjfzhuangrang) {
//            intent = new Intent(this, TransWjfListActivity.class);
//            intent.putExtra("Tag", 0);
//            startActivity(intent);
//        }
    }
}
