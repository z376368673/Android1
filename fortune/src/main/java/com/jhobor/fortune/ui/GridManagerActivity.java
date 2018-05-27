package com.jhobor.fortune.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jhobor.fortune.R;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.utils.BarUtil;
import com.jhobor.fortune.utils.HideIMEUtil;
import com.jhobor.fortune.oldui.AddMoneyActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author     Qijing
 * Created by YQJ on 2018/3/27.
 * Description:
 */
public class GridManagerActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mAccount_jf_tv, account_wjf_tv,account_bd_tv;
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
        account_bd_tv = (TextView) findViewById(R.id.account_bd_tv);
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

    double integral, calculus,billIntegral;

    private void getData() {
        String uuid = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.getmyReport(uuid).enqueue(new RetrofitCallback(getApplicationContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    integral = jsonObject.getDouble("integral");
                    calculus = jsonObject.getDouble("calculus");
                    billIntegral = jsonObject.getDouble("billIntegral");

                    mAccount_jf_tv.setText(integral + "");
                    account_wjf_tv.setText(calculus + "");
                    account_bd_tv.setText(billIntegral + "");
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

    /**
     * 选择积分类型
     *
     */
    public  void choiseType(final View view) {
        View views = LayoutInflater.from(this).inflate(R.layout.dialog_chose, null, false);
        final TextView tv_text1 = (TextView) views.findViewById(R.id.tv_text1);
        final TextView tv_text2 = (TextView) views.findViewById(R.id.tv_text2);
        final TextView tv_text3 = (TextView) views.findViewById(R.id.tv_text3);

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setView(views)
                .setCancelable(true);
        final android.support.v7.app.AlertDialog dialog = builder.create();
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.show();
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (view == mWithdraw) {
                    if (v==tv_text1){
                        Intent intent2 = new Intent(context, AccountWithdrawActivity.class);
                        intent2.putExtra("JF",integral);
                        intent2.putExtra("TYPE",1);//租车积分
                        startActivity(intent2);
                    }else if (v==tv_text2){
                        Intent intent2 = new Intent(context, AccountWithdrawActivity.class);
                        intent2.putExtra("JF",calculus);
                        intent2.putExtra("TYPE",2);//电商积分
                        startActivity(intent2);
                    }else {
                        Intent intent2 = new Intent(context, AccountWithdrawActivity.class);
                        intent2.putExtra("JF",billIntegral);
                        intent2.putExtra("TYPE",3);//服务积分
                        startActivity(intent2);
                    }

                } else if (view == mZr) {
                    if (v==tv_text1){
                        Intent intent2 = new Intent(context, IntegralTransActivity.class);
                        intent2.putExtra("JF",integral);
                        intent2.putExtra("TYPE",1);//租车积分
                        startActivity(intent2);
                    }else if (v==tv_text2){
                        Intent intent2 = new Intent(context, IntegralTransActivity.class);
                        intent2.putExtra("JF",calculus);
                        intent2.putExtra("TYPE",2);//电商积分
                        startActivity(intent2);
                    }else {
                        Intent intent2 = new Intent(context, IntegralTransActivity.class);
                        intent2.putExtra("JF",billIntegral);
                        intent2.putExtra("TYPE",3);//服务积分
                        startActivity(intent2);
                    }

                }
            }
        };
        tv_text1.setOnClickListener(onClickListener);
        tv_text2.setOnClickListener(onClickListener);
        tv_text3.setOnClickListener(onClickListener);
    }
}
