package com.jhobor.ddc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.ddc.R;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.entity.BankCard;
import com.jhobor.ddc.entity.Store;
import com.jhobor.ddc.entity.UserInfo;
import com.jhobor.ddc.utils.ErrorUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class WithdrawActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView topArrow, bankIcon;
    TextView topTitle, balance, takeAll, bankName, brief;
    EditText money;
    Button ok;
    View choose;

    BankCard bankCard;
    UserInfo userInfo;
    boolean isSeller = false;
    Store store;
    float curBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        Intent intent = getIntent();
        bankCard = intent.getParcelableExtra("bankCard");
        isSeller = intent.getBooleanExtra("isSeller", false);
        initView();
        handleEvt();
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);
        choose.setOnClickListener(this);
        takeAll.setOnClickListener(this);
        ok.setOnClickListener(this);
    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        bankIcon = (ImageView) findViewById(R.id.bankIcon);
        topTitle = (TextView) findViewById(R.id.topTitle);
        balance = (TextView) findViewById(R.id.balance);
        takeAll = (TextView) findViewById(R.id.takeAll);
        bankName = (TextView) findViewById(R.id.bankName);
        brief = (TextView) findViewById(R.id.brief);
        ok = (Button) findViewById(R.id.ok);
        choose = findViewById(R.id.choose);
        money = (EditText) findViewById(R.id.money);

        topTitle.setText("提现");
        bankName.setText(bankCard.getBankName());
        brief.setText(String.format(Locale.CHINA, "尾号%s储蓄卡", bankCard.getCardNo().substring(bankCard.getCardNo().length() - 4)));
        if (isSeller) {
            store = (Store) BaseApplication.dataMap.get("store");
            curBalance = store.getBalance();
        } else {
            userInfo = (UserInfo) BaseApplication.dataMap.get("userInfo");
            curBalance = userInfo.getBalance();
        }
        balance.setText(String.format(Locale.CHINA, "可用余额%.2f元", curBalance));
    }

    @Override
    public void onClick(View v) {
        if (v == topArrow) {
            finish();
        } else if (v == ok) {
            String s = money.getText().toString();
            if (!s.isEmpty()) {
                final int i = Integer.parseInt(s);
                if (i < 1) {
                    Toast.makeText(this, "最少提现1元", Toast.LENGTH_SHORT).show();
                } else if (i <= curBalance) {
                    String uuid = (String) BaseApplication.dataMap.get("token");
                    if (isSeller) {
                        BaseApplication.iService.storeWithdraw(uuid, i).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                            @Override
                            public void parse(String data) {
                                try {
                                    JSONObject jsonObject = new JSONObject(data);
                                    int isLogin = jsonObject.getInt("isLogin");
                                    int msg = jsonObject.getInt("msg");
                                    if (msg == 1) {
                                        store.setBalance(curBalance - i);
                                        Toast.makeText(WithdrawActivity.this, "成功提交提现申请，你的提款一会就能到账哦", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                } catch (JSONException e) {
                                    ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                                }
                            }
                        }));
                    } else {
                        BaseApplication.iService.withdraw(uuid, i).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                            @Override
                            public void parse(String data) {
                                try {
                                    JSONObject jsonObject = new JSONObject(data);
                                    int msg = jsonObject.getInt("msg");
                                    int isLogin = jsonObject.getInt("isLogin");
                                    if (msg == 1 && isLogin == 1) {
                                        double b = jsonObject.getDouble("balance");
                                        userInfo.setBalance((float) b);
                                        Toast.makeText(WithdrawActivity.this, "成功提交提现申请，你的提款一会就能到账哦", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                } catch (JSONException e) {
                                    ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                                }
                            }
                        }));
                    }
                } else {
                    Toast.makeText(this, "余额不足", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "还没输入提现金额", Toast.LENGTH_SHORT).show();
            }
        } else if (v == takeAll) {
            money.setText(String.valueOf((int) curBalance));
        } else if (v == choose) {
            /*Intent intent = new Intent(this,ChooseBankCardActivity.class);
            startActivity(intent);*/
        }
    }
}
