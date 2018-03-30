package com.jhobor.fortune;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.utils.BarUtil;
import com.jhobor.fortune.utils.CheckUtil;
import com.jhobor.fortune.utils.ErrorUtil;
import com.jhobor.fortune.utils.HideIMEUtil;
import com.jhobor.fortune.utils.TextUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class FillBindAccountInfoActivity extends AppCompatActivity implements View.OnClickListener {
    EditText bank, holder, unionPayAccount, aliPayAccount, aliName, wechatPayAccount, wechatName;
    LinearLayout unionBox, aliBox, wechatBox;
    Button save;

    int type;

    private int mBankId;
    private int mALiId;
    private int mWeChatId;
    private int mFinalId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_bind_account_info);
        HideIMEUtil.wrap(this);

        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        unionBox = (LinearLayout) findViewById(R.id.unionBox);
        aliBox = (LinearLayout) findViewById(R.id.aliBox);
        wechatBox = (LinearLayout) findViewById(R.id.wechatBox);
        save = (Button) findViewById(R.id.save);
        String title = "银联卡信息";
        if (type == ReceiptWayActivity.TYPE_BANK) {
            aliBox.setVisibility(View.GONE);
            wechatBox.setVisibility(View.GONE);
            bank = (EditText) findViewById(R.id.bank);
            holder = (EditText) findViewById(R.id.holder);
            unionPayAccount = (EditText) findViewById(R.id.unionPayAccount);
            Intent intent1 = getIntent();
            String mBankName = intent1.getStringExtra("mBankName");
            if (mBankName !=null){
                unionPayAccount.setText(mBankName);
            }

        } else if (type == ReceiptWayActivity.TYPE_ALI) {
            unionBox.setVisibility(View.GONE);
            wechatBox.setVisibility(View.GONE);
            Intent intent1 = getIntent();
            String mALiName = intent1.getStringExtra("mALiName");
            aliPayAccount = (EditText) findViewById(R.id.aliPayAccount);
            if (mALiName !=null){
                aliPayAccount.setText(mALiName);
            }
            aliName = (EditText) findViewById(R.id.aliName);
            title = "支付宝信息";
        } else if (type == ReceiptWayActivity.TYPE_WECHAT) {
            aliBox.setVisibility(View.GONE);
            unionBox.setVisibility(View.GONE);
            wechatPayAccount = (EditText) findViewById(R.id.wechatPayAccount);
            wechatName = (EditText) findViewById(R.id.wechatName);

            Intent intent1 = getIntent();
            String mWeChatName = intent1.getStringExtra("mWeChatName");
            if (mWeChatName !=null){
                wechatPayAccount.setText(mWeChatName);
            }
            title = "微信信息";
        }
        BarUtil.topBar(this, title);
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == save) {
            if (type == ReceiptWayActivity.TYPE_BANK) {
                Intent intent = getIntent();
                 mBankId = intent.getIntExtra("mBankId",0);
                submitBankInfo();
            } else if (type == ReceiptWayActivity.TYPE_ALI) {
                Intent intent = getIntent();
                 mALiId = intent.getIntExtra("mALiId",0);
                submitAliInfo();
            } else if (type == ReceiptWayActivity.TYPE_WECHAT) {
                Intent intent = getIntent();
                 mWeChatId = intent.getIntExtra("mWeChatId",0);
                submitWechatInfo();
            }
        }
    }

    private void submitWechatInfo() {
        mFinalId = mWeChatId;
        Object[] objectArr = TextUtil.arrange(wechatPayAccount, wechatName);
        String[] contentArr = (String[]) objectArr[0];
        boolean[] itemChecks = {
                CheckUtil.isMobile(contentArr[0]) || CheckUtil.isEmail(contentArr[0]),
                !contentArr[1].isEmpty(),
        };
        String[] itemTips = {
                "微信帐号应为手机号码或邮箱地址",
                "微信昵称不可为空",
        };
        int i = CheckUtil.checkAll(itemChecks);
        Log.i(">>", String.valueOf(i));
        if (i < itemChecks.length) {
            Toast.makeText(this, itemTips[i], Toast.LENGTH_SHORT).show();
            EditText[] editTextArr = (EditText[]) objectArr[1];
            editTextArr[i].requestFocus();
        } else {
            submitInfo("微信", contentArr[1], contentArr[0], "微信");
        }
    }

    private void submitAliInfo() {
        mFinalId = mALiId;
        Object[] objectArr = TextUtil.arrange(aliPayAccount, aliName);
        String[] contentArr = (String[]) objectArr[0];
        boolean[] itemChecks = {
                CheckUtil.isMobile(contentArr[0]) || CheckUtil.isEmail(contentArr[0]),
                CheckUtil.isName(contentArr[1]),
        };
        String[] itemTips = {
                "支付宝帐号应为手机号码或邮箱地址",
                "支付宝绑定的姓名不正确",
        };
        int i = CheckUtil.checkAll(itemChecks);
        if (i < itemChecks.length) {
            Toast.makeText(this, itemTips[i], Toast.LENGTH_SHORT).show();
            EditText[] editTextArr = (EditText[]) objectArr[1];
            editTextArr[i].requestFocus();
        } else {
            submitInfo("支付宝", contentArr[1], contentArr[0], "支付宝");
        }
    }

    private void submitBankInfo() {
        mFinalId = mBankId;
        Object[] objectArr = TextUtil.arrange(bank, holder, unionPayAccount);
        String[] contentArr = (String[]) objectArr[0];
        boolean[] itemChecks = {
                CheckUtil.isName(contentArr[0]) && contentArr[0].length() > 3,
                CheckUtil.isName(contentArr[1]),
                CheckUtil.isBankCardNo(contentArr[2]),
        };
        String[] itemTips = {
                "银行名称不正确",
                "户主姓名不正确",
                "银行卡卡号为16位或19位数字",
        };
        int i = CheckUtil.checkAll(itemChecks);
        if (i < itemChecks.length) {
            Toast.makeText(this, itemTips[i], Toast.LENGTH_SHORT).show();
            EditText[] editTextArr = (EditText[]) objectArr[1];
            editTextArr[i].requestFocus();
        } else {
            submitInfo(contentArr[0], contentArr[1], contentArr[2], "银行");
        }
    }

    private void submitInfo(String bankName, String name, String account, String typeName) {
        String token = (String) BaseApplication.dataMap.get("token");
        if (mFinalId == 0){
        BaseApplication.iService.addAccountInfo(token, bankName, name, account, typeName).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int isLogin = jsonObject.getInt("isLogin");
                    if (isLogin == 1) {
                        int msg = jsonObject.getInt("msg");
                        if (msg == 1) {
                            Toast.makeText(FillBindAccountInfoActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                            BaseApplication.dataMap.put("reload", true);
                            finish();
                        }
                    } else {
                        Toast.makeText(FillBindAccountInfoActivity.this, "账户信息失效，无法获取数据", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(FillBindAccountInfoActivity.this, e);
                }
            }
        }));
    }else {
            BaseApplication.iService.changedAccountInfo(mFinalId+"",token, bankName, name, account, typeName).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                @Override
                public void parse(String data) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        int isLogin = jsonObject.getInt("isLogin");
                        if (isLogin == 1) {
                            int msg = jsonObject.getInt("msg");
                            if (msg == 1) {
                                Toast.makeText(FillBindAccountInfoActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                                BaseApplication.dataMap.put("reload", true);
                                finish();
                            }
                        } else {
                            Toast.makeText(FillBindAccountInfoActivity.this, "账户信息失效，无法获取数据", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        ErrorUtil.retrofitResponseParseFail(FillBindAccountInfoActivity.this, e);
                    }
                }
            }));
        }
    }
}
