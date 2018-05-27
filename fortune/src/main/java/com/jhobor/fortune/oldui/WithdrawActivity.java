package com.jhobor.fortune.oldui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.fortune.R;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.utils.BarUtil;
import com.jhobor.fortune.utils.ErrorUtil;
import com.jhobor.fortune.utils.HideIMEUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WithdrawActivity extends AppCompatActivity implements View.OnClickListener {
    EditText money;
    TextView total;
    Spinner receiptWay;
    Button ok;

    List<String> wayList = new ArrayList<>(3);
    List<Integer> wayIdList = new ArrayList<>(3);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        HideIMEUtil.wrap(this);

        money = (EditText) findViewById(R.id.money);
        total = (TextView) findViewById(R.id.total);
        receiptWay = (Spinner) findViewById(R.id.receiptWay);
        ok = (Button) findViewById(R.id.ok);

        BarUtil.topBar(this, "提现");
        total.setText(String.valueOf(500));
        ok.setOnClickListener(this);
        getBalance();
    }

    private void getBalance() {
        String token = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.myBalance(token).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int isLogin = jsonObject.getInt("isLogin");
                    if (isLogin == 1) {
                        double totalMoney = jsonObject.getDouble("totalMoney");
                        JSONArray optionModeList = jsonObject.getJSONArray("optionModeList");
                        int length = optionModeList.length();
                        for (int i = 0; i < length; i++) {
                            JSONArray jsonArray = optionModeList.getJSONArray(i);
                            int id = jsonArray.getInt(0);
                            String option = jsonArray.getString(1);
                            String account = jsonArray.getString(2);
                            if (option.equals("银行")) {
                                String prefix = account.substring(0, 4);
                                String suffix = account.substring(account.length() - 4);
                                option = String.format(Locale.CHINA, "%s (%s***%s)", option, prefix, suffix);
                            } else {
                                option = String.format(Locale.CHINA, "%s (%s)", option, account);
                            }
                            wayList.add(option);
                            wayIdList.add(id);
                        }
                        total.setText(String.format(Locale.CHINA, "%.2f", totalMoney));
                        receiptWay.setAdapter(new ArrayAdapter<String>(WithdrawActivity.this, android.R.layout.simple_spinner_dropdown_item, wayList));
                    } else {
                        Toast.makeText(WithdrawActivity.this, "账户信息失效，无法获取数据", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(WithdrawActivity.this, e);
                }
            }
        }));
    }

    @Override
    public void onClick(View v) {
        if (v == ok) {
            String strMoney = money.getText().toString();
            if (strMoney.isEmpty()) {
                Toast.makeText(WithdrawActivity.this, "请输入提现金额", Toast.LENGTH_SHORT).show();
            } else {
                int iMoney = Integer.parseInt(strMoney);
                if (iMoney < 100 || iMoney % 100 != 0) {
                    Toast.makeText(WithdrawActivity.this, "提现金额不少于100，且是100的整数倍", Toast.LENGTH_SHORT).show();
                } else if (wayIdList.size() == 0) {
                    Toast.makeText(WithdrawActivity.this, "收款方式还没完善，请从个人中心进入收款方式填写相关内容", Toast.LENGTH_LONG).show();
                } else {
                    String strTotal = total.getText().toString();
                    float fTotal = Float.parseFloat(strTotal);
                    long selectedItemId = receiptWay.getSelectedItemId();
                    Integer wayId = wayIdList.get((int) selectedItemId);
                    if (iMoney > fTotal) {
                        Toast.makeText(WithdrawActivity.this, "余额不足", Toast.LENGTH_SHORT).show();
                    } else {
                        applyWithdraw(iMoney, wayId);
                    }
                }
            }
        }
    }

    private void applyWithdraw(int iMoney, Integer wayId) {
        String token = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.withdraw(token, iMoney, wayId).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int isLogin = jsonObject.getInt("isLogin");
                    if (isLogin == 1) {
                        int msg = jsonObject.getInt("msg");
                        if (msg == 1) {
                            float totalMoney = (float) jsonObject.getDouble("totalMoney");
                            total.setText(String.format(Locale.CHINA, "%.2f", totalMoney));
                            Toast.makeText(WithdrawActivity.this, "申请提现成功，提现金额稍候会打到你的账户上", Toast.LENGTH_LONG).show();
                            money.setText("");
                        } else {
                            Toast.makeText(WithdrawActivity.this, "申请提现失败", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(WithdrawActivity.this, "账户信息失效，无法获取数据", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(WithdrawActivity.this, e);
                }
            }
        }));
    }
}
