package com.jhobor.fortune.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jhobor.fortune.R;
import com.jhobor.fortune.adapter.BankListAdapter;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.entity.BankBean;
import com.jhobor.fortune.utils.BarUtil;
import com.jhobor.fortune.utils.HideIMEUtil;
import com.jhobor.fortune.oldui.AddBankCardActivity;

import java.util.List;

/**
 * Author     Qijing
 * Created by YQJ on 2018/3/27.
 * Description:
 */
public class MyAccountActivity extends AppCompatActivity implements BarUtil.RightClick,
        View.OnClickListener, AdapterView.OnItemLongClickListener {


    private ListView listView;
    private TextView mAdd;
    Context context;
    ProgressDialog progressDialog;
    BankListAdapter bankListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        HideIMEUtil.wrap(this);
        BarUtil.topBar(this, "账户信息");
        //BarUtil.topBarRight(this, true, "添加");
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemLongClickListener(this);
        context = this;
        mAdd = (TextView) findViewById(R.id.add);
        BarUtil.setmClick(this);
        mAdd.setOnClickListener(this);
        bankListAdapter = new BankListAdapter(context);
        listView.setAdapter(bankListAdapter);
        progressDialog = ProgressDialog.show(this, "添加银行卡", "提交信息...");
        myBankList();
    }

    @Override
    public void onResume() {
        super.onResume();
        myBankList();
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

    private void myBankList() {

        String token = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.myBankList(token)
                .enqueue(new RetrofitCallback(context, new RetrofitCallback.DataParser() {
                    @Override
                    public void parse(String data) {
                        progressDialog.dismiss();
                        JSONObject jsonObject = JSON.parseObject(data);
                        int msg = jsonObject.getIntValue("msg");
                        if (msg == 1) {
                            List<BankBean> bankBeanList = JSON.parseArray(jsonObject.getString("bcList"), BankBean.class);
                            if (bankBeanList.size() > 0) {
                                bankListAdapter.clear();
                                bankListAdapter.addAll(bankBeanList);
                                bankListAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(context, jsonObject.getString("errorInfo"), Toast.LENGTH_SHORT).show();
                        }
                    }
                }));
    }

    private void delBank(final BankBean bankBean) {
        progressDialog = ProgressDialog.show(this, "删除银行卡", "正在删除...");
        String token = BaseApplication.prefs.getString("token", "");
        BaseApplication.iService.delBank(token, bankBean.getId())
                .enqueue(new RetrofitCallback(context, new RetrofitCallback.DataParser() {
                    @Override
                    public void parse(String data) {
                        progressDialog.dismiss();
                        JSONObject jsonObject = JSON.parseObject(data);
                        int msg = jsonObject.getIntValue("msg");
                        if (msg == 1) {
                            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                            bankListAdapter.remove(bankBean);
                            bankListAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, jsonObject.getString("errorInfo"), Toast.LENGTH_SHORT).show();
                        }
                    }
                }));
    }


    private void showNormalDialog(final BankBean bankBean) {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(context);
        normalDialog.setIcon(R.mipmap.logout);
        normalDialog.setTitle("提示");
        normalDialog.setMessage("您确认要删除此银行卡吗？");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delBank(bankBean);
                        dialog.dismiss();
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        // 显示
        normalDialog.show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        BankBean bankBean = (BankBean) adapterView.getAdapter().getItem(i);
        showNormalDialog(bankBean);
        return true;
    }
}
