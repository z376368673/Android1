package com.jhobor.ddc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.ddc.R;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.entity.BankCard;
import com.jhobor.ddc.entity.UserInfo;
import com.jhobor.ddc.utils.ErrorUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class WalletsActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView topArrow;
    TextView money, charge, withdraw;
    LinearLayout bindCard;

    boolean need2refresh = false;
    BankCard myBankCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallets);

        initView();
        handleEvt();
        getBankCardInfo();
    }

    private void getBankCardInfo() {
        String uuid = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.bankCardDetails(uuid).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int isLogin = jsonObject.getInt("isLogin");
                    JSONObject bankCard = jsonObject.getJSONObject("bankCard");
                    int id = bankCard.getInt("id");
                    String name = bankCard.getString("name");
                    String bankName = bankCard.getString("bankName");
                    String bankNo = bankCard.getString("bankNo");
                    String city = bankCard.getString("city");
                    String branch = bankCard.getString("branch");
                    int userId = bankCard.getInt("userId");
                    myBankCard = new BankCard(id, userId, bankName, branch, 1, bankNo);
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                }
            }
        }));
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);
        charge.setOnClickListener(this);
        withdraw.setOnClickListener(this);
        bindCard.setOnClickListener(this);
    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        money = (TextView) findViewById(R.id.money);
        charge = (TextView) findViewById(R.id.charge);
        withdraw = (TextView) findViewById(R.id.withdraw);
        bindCard = (LinearLayout) findViewById(R.id.bindCard);
        UserInfo userInfo = (UserInfo) BaseApplication.dataMap.get("userInfo");
        money.setText(String.format(Locale.CHINA, "%.2f", userInfo.getBalance()));
    }

    @Override
    public void onClick(View v) {
        if (v == topArrow) {
            finish();
        } else if (v == charge) {
            need2refresh = true;
            Toast.makeText(WalletsActivity.this, "暂未开通充值功能", Toast.LENGTH_LONG).show();
        } else if (v == withdraw) {
            if (myBankCard != null) {
                need2refresh = true;
                Intent intent = new Intent(this, WithdrawActivity.class);
                intent.putExtra("bankCard", myBankCard);
                startActivity(intent);
            } else {
                Toast.makeText(WalletsActivity.this, "还没绑定储蓄卡，请先绑定...", Toast.LENGTH_LONG).show();
            }
        } else if (v == bindCard) {
            if (myBankCard == null) {
                Intent intent = new Intent(this, BindCardActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(WalletsActivity.this, String.format(Locale.CHINA, "你已绑定【%s】尾号为%s的银行卡了，不能再绑定其它银行卡", myBankCard.getBankName(), myBankCard.getCardNo().substring(myBankCard.getCardNo().length() - 4)), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onResume() {
        if (need2refresh) {
            UserInfo userInfo = (UserInfo) BaseApplication.dataMap.get("userInfo");
            money.setText(String.format(Locale.CHINA, "%.2f", userInfo.getBalance()));
            need2refresh = false;
        }
        super.onResume();
    }
}
