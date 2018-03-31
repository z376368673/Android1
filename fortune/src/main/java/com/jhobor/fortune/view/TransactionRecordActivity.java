package com.jhobor.fortune.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.fortune.LoginActivity;
import com.jhobor.fortune.ModifyPassActivity;
import com.jhobor.fortune.R;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.utils.BarUtil;
import com.jhobor.fortune.utils.DataCleanManager;
import com.jhobor.fortune.utils.HideIMEUtil;

/**
 * Author     Qijing
 * Created by YQJ on 2018/3/27.
 * Description:
 */
public class TransactionRecordActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout ll_zengzhi;
    private LinearLayout ll_fenhong;
    private LinearLayout ll_jfzengzhangd;
    private LinearLayout ll_jftixian;
    private LinearLayout ll_jfzhuangrang;


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
        ll_fenhong = (LinearLayout) findViewById(R.id.ll_fenhong);
        ll_jfzengzhangd = (LinearLayout) findViewById(R.id.ll_jfzengzhangd);
        ll_jftixian = (LinearLayout) findViewById(R.id.ll_jftixian);
        ll_jfzhuangrang = (LinearLayout) findViewById(R.id.ll_jfzhuangrang);
        ll_zengzhi.setOnClickListener(this);
        ll_fenhong.setOnClickListener(this);
        ll_jfzengzhangd.setOnClickListener(this);
        ll_jftixian.setOnClickListener(this);
        ll_jfzhuangrang.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this,TransListActivity.class);
        if (view ==ll_zengzhi){
            intent.putExtra("Tag",0);
            startActivity(intent);
        }else if (view ==ll_fenhong){
            intent.putExtra("Tag",0);
            startActivity(intent);
        }else if (view ==ll_jfzengzhangd){
            intent.putExtra("Tag",0);
            startActivity(intent);
        }else if (view ==ll_jftixian){
            intent.putExtra("Tag",1);
            startActivity(intent);
        }else if (view ==ll_jfzhuangrang){
            intent.putExtra("Tag",2);
            startActivity(intent);
        }
    }
}
