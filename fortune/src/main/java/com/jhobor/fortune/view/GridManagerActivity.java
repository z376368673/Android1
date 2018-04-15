package com.jhobor.fortune.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.fortune.R;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.utils.BarUtil;
import com.jhobor.fortune.utils.HideIMEUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author     Qijing
 * Created by YQJ on 2018/3/27.
 * Description:
 */
public class GridManagerActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mAccount_jf_tv, account_wjf_tv;
    private TextView mAdd;
    private TextView mWithdraw;
    private TextView mZr;
    private RelativeLayout mRecord;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_manager);
        HideIMEUtil.wrap(this);
        BarUtil.topBar(this, "租车积分管理");
        context = this;
        mAccount_jf_tv = (TextView) findViewById(R.id.account_jf_tv);
        account_wjf_tv = (TextView) findViewById(R.id.account_wjf_tv);
        mAdd = (TextView) findViewById(R.id.account_tv_add);
        mWithdraw = (TextView) findViewById(R.id.account_tv_withdraw);
        mZr = (TextView) findViewById(R.id.account_tv_zr);
        mRecord = (RelativeLayout) findViewById(R.id.rl_trade_record);
        mAdd.setOnClickListener(this);
        mWithdraw.setOnClickListener(this);
        mZr.setOnClickListener(this);
        mRecord.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    double integral, calculus;

    private void getData() {
        String uuid = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.getmyReport(uuid).enqueue(new RetrofitCallback(getApplicationContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    integral = jsonObject.getDouble("integral");
                    calculus = jsonObject.getDouble("calculus");
                    mAccount_jf_tv.setText(integral + "");
                    account_wjf_tv.setText(calculus + "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));
    }


    @Override
    public void onClick(View v) {
        /*
        *  mAdd = (TextView) findViewById(R.id.account_tv_add);
        mWithdraw = (TextView) findViewById(R.id.account_tv_withdraw);
        mZr = (TextView) findViewById(R.id.account_tv_zr);
        * */

        if (v == mAdd) {
            Intent intent1 = new Intent(this, AddMoneyActivity.class);
            startActivity(intent1);
        } else if (v == mWithdraw) {
            choiseType(mWithdraw);
        } else if (v == mZr) {
            choiseType(mZr);
        } else if (v == mRecord) {
            Intent intent2 = new Intent(this, TransactionRecordActivity.class);
            startActivity(intent2);
        }
    }

    private void choiseType(final View view) {
        final String[] items = {"租车业绩积分", "租车微积分"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(context);
        listDialog.setTitle("请选择");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (view == mWithdraw) {
                    if (which==0){
                        Intent intent2 = new Intent(context, AccountWithdrawActivity.class);
                        intent2.putExtra("JF",integral);
                        startActivity(intent2);
                    }else {
                        Intent intent2 = new Intent(context, AccountWithdraw_wActivity.class);
                        intent2.putExtra("JF",calculus);
                        startActivity(intent2);
                    }

                } else if (view == mZr) {
                    if (which==0){
                        Intent intent2 = new Intent(context, IntegralTransActivity.class);
                        intent2.putExtra("JF",integral);
                        startActivity(intent2);
                    }else {
                        Intent intent2 = new Intent(context, IntegralTrans_wActivity.class);
                        intent2.putExtra("JF",calculus);
                        startActivity(intent2);
                    }

                }
            }
        });
        listDialog.show();
    }
}
