package com.jhobor.fortune.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.jhobor.fortune.R;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.dialog.LoadingDialog;
import com.jhobor.fortune.entity.BankBean;
import com.jhobor.fortune.utils.BarUtil;
import com.jhobor.fortune.utils.HideIMEUtil;
import com.vincent.filepicker.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Author     Qijing
 * Created by YQJ on 2018/3/30.
 * Description:
 */
public class AccountWithdrawActivity extends BaseActivity implements View.OnClickListener {


    private EditText mMoney;
    private TextView mAccount;
    private EditText mPsd;
    private TextView mSubbmit;

    int bankId = -1;

    Context context;
    List<BankBean> bankBeanList;


    double integral = 0;
    int type = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_withdraw);
        context = this;
        HideIMEUtil.wrap(this);
        integral = getIntent().getDoubleExtra("JF",0);
        type = getIntent().getIntExtra("TYPE",1);
        mMoney = (EditText) findViewById(R.id.with_draw_et);
        mAccount = (TextView) findViewById(R.id.with_draw_account);
        mPsd = (EditText) findViewById(R.id.with_draw_psd);
        mSubbmit = (TextView) findViewById(R.id.with_draw_submmit);
        mSubbmit.setOnClickListener(this);

        switch (type){
            case 1:
                BarUtil.topBar(this, "业绩积分提现");
                break;
            case 2:
                BarUtil.topBar(this, "电商积分提现");
                break;
            case 3:
                BarUtil.topBar(this, "服务积分提现");
                break;
        }

        bankBeanList = new ArrayList<>();
        myBankList();

    }

    @Override
    public void onClick(View v) {
        if (v == mAccount) {

        } else if (v == mSubbmit) {
            String trim = mMoney.getText().toString().trim();
            String account = mAccount.getText().toString().trim();
            String psd = mPsd.getText().toString().trim();
            if (trim.isEmpty()) {
                Toast.makeText(this, "输入金额不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Integer.valueOf(trim) < 500) {
                Toast.makeText(this, "提现金额不能少于500", Toast.LENGTH_SHORT).show();
                return;
            }

            if (Integer.valueOf(trim)>integral){
                Toast.makeText(this, "积分不足，不能提现", Toast.LENGTH_SHORT).show();
                return;
            }
            if (account.isEmpty() && bankId != -1) {
                Toast.makeText(this, "提现账户不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (psd.isEmpty()) {
                Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            dialog.show();
            switch (type){
                case 1:
                    withdrawMoney(psd,trim);
                    break;
                case 2:
                    myWithdrawMoney(psd,trim);
                    break;
                case 3:
                    ReportWithdrawMoney(psd,trim);
                    break;
            }

        }
    }
    //租车积分提现
    private void withdrawMoney(String psd,String trim){
        String uuid = (String) BaseApplication.dataMap.get("token");
        BigDecimal b = new BigDecimal(trim);
        b = b.setScale(2, BigDecimal.ROUND_DOWN);
        BaseApplication.iService.withdrawMoney(uuid, psd, bankId + "", b).enqueue(new RetrofitCallback(this, new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                dialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int msg = (int) jsonObject.opt("msg");
                    if (msg == 1) {
                        Toast.makeText(AccountWithdrawActivity.this, "提交成功,等待审核", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AccountWithdrawActivity.this, jsonObject.getString("errorInfo"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    dialog.dismiss();
                }
            }
        }));
    }

    //电商积分提现
    private void myWithdrawMoney(String psd,String trim){
        String uuid = (String) BaseApplication.dataMap.get("token");
        BigDecimal b = new BigDecimal(trim);
        b = b.setScale(2, BigDecimal.ROUND_DOWN);
        BaseApplication.iService.myWithdrawMoney(uuid, psd, bankId + "", b).enqueue(new RetrofitCallback(this, new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                dialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int msg = (int) jsonObject.opt("msg");
                    if (msg == 1) {
                        Toast.makeText(AccountWithdrawActivity.this, "提交成功,等待审核", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AccountWithdrawActivity.this, jsonObject.getString("errorInfo"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    dialog.dismiss();
                }
            }
        }));
    }

    //服务积分提现
    private void ReportWithdrawMoney(String psd,String trim){

        String uuid = (String) BaseApplication.dataMap.get("token");
        BigDecimal b = new BigDecimal(trim);
        b = b.setScale(2, BigDecimal.ROUND_DOWN);
        BaseApplication.iService.ReportWithdrawMoney(uuid, psd, bankId + "", b).enqueue(new RetrofitCallback(this, new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                dialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int msg = (int) jsonObject.opt("msg");
                    if (msg == 1) {
                        Toast.makeText(AccountWithdrawActivity.this, "提交成功,等待审核", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        String errorInfo = jsonObject.getString("errorInfo");
                        ToastUtil.getInstance(act).showToast(errorInfo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    dialog.dismiss();
                }
            }
        }));
    }


    private void myBankList() {
        String token = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.myBankList(token)
                .enqueue(new RetrofitCallback(context, new RetrofitCallback.DataParser() {
                    @Override
                    public void parse(String data) {
                        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(data);
                        int msg = jsonObject.getIntValue("msg");
                        if (msg == 1) {
                            bankBeanList = JSON.parseArray(jsonObject.getString("bcList"), BankBean.class);
                        } else {
                            Toast.makeText(context, jsonObject.getString("errorInfo"), Toast.LENGTH_SHORT).show();
                        }
                    }
                }));
    }

    /**
     * 选择银行
     *
     * @param view
     */
    public void dialogChoice(View view) {
        if (bankBeanList.size() == 0) {
            ToastUtil.getInstance(context).showToast("您必须先添加银行卡");
            return;
        }
        final int len = bankBeanList.size();
        final String items[] = new String[len];
        for (int i = 0; i < bankBeanList.size(); i++) {
            items[i] = bankBeanList.get(i).getBankName() +"(尾号"+ bankBeanList.get(i).getBankNo().substring(bankBeanList.get(i).getBankNo().length()-4)+")";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this, 3);
        builder.setTitle("请选择银行");
        builder.setIcon(R.mipmap.ic_bank);
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bankId = bankBeanList.get(which).getId();
                        mAccount.setText(bankBeanList.get(which).getBankNo());
                    }
                });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

}

