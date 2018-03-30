package com.jhobor.fortune;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.entity.PayWay;
import com.jhobor.fortune.utils.BarUtil;
import com.jhobor.fortune.utils.ErrorUtil;
import com.jhobor.fortune.utils.HideIMEUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReceiptWayActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int TYPE_BANK = 0;
    public static final int TYPE_ALI = 1;
    public static final int TYPE_WECHAT = 2;
    LinearLayout unionBox, aliBox, wechatBox;
    TextView unionPayAccount, aliPayAccount, wechatPayAccount;
    SparseArray<PayWay> payWaySparseArray = new SparseArray<>(3);
    private int mBankId;
    private int mALiId;
    private int mWeChatId;
    private String mBankName;
    private String mALiName;
    private String mWeChatName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        HideIMEUtil.wrap(this);

        unionBox = (LinearLayout) findViewById(R.id.unionBox);
        aliBox = (LinearLayout) findViewById(R.id.aliBox);
        wechatBox = (LinearLayout) findViewById(R.id.wechatBox);
        unionPayAccount = (TextView) findViewById(R.id.unionPayAccount);
        aliPayAccount = (TextView) findViewById(R.id.aliPayAccount);
        wechatPayAccount = (TextView) findViewById(R.id.wechatPayAccount);

        BarUtil.topBar(this, "收款方式");
        unionBox.setOnClickListener(this);
        aliBox.setOnClickListener(this);
        wechatBox.setOnClickListener(this);
        getData();
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean reload = (boolean) BaseApplication.dataMap.get("reload");
        if (reload) {
            getData();
        }
    }

    private void getData() {
        String token = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.receiptWay(token).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int isLogin = jsonObject.getInt("isLogin");
                    if (isLogin == 1) {
                        JSONArray optionModeList = jsonObject.getJSONArray("optionModeList");
                        int length = optionModeList.length();
                        for (int i = 0; i < length; i++) {
                            JSONArray jsonArray = optionModeList.getJSONArray(i);
                            int id = jsonArray.getInt(0);
                            String userName = jsonArray.getString(1);
                            String account = jsonArray.getString(2);
                            String bankName = jsonArray.getString(3);
                            String type = jsonArray.getString(4);
                            if (type.equals("银行")) {
                                mBankId = jsonArray.getInt(0);
                                mBankName = account;
                                payWaySparseArray.put(TYPE_BANK, new PayWay(id, bankName, userName, account, type));
                            } else if (type.equals("支付宝")) {
                                mALiId = jsonArray.getInt(0);
                                mALiName = account;
                                payWaySparseArray.put(TYPE_ALI, new PayWay(id, bankName, userName, account, type));
                            } else if (type.equals("微信")) {
                                mWeChatId = jsonArray.getInt(0);
                                mWeChatName = account;
                                payWaySparseArray.put(TYPE_WECHAT, new PayWay(id, bankName, userName, account, type));
                            }
                        }

                        if (payWaySparseArray.get(TYPE_BANK) != null) {
                            unionPayAccount.setText(payWaySparseArray.get(TYPE_BANK).getAccount());
                        }
                        if (payWaySparseArray.get(TYPE_ALI) != null) {
                            aliPayAccount.setText(payWaySparseArray.get(TYPE_ALI).getAccount());
                        }
                        if (payWaySparseArray.get(TYPE_WECHAT) != null) {
                            wechatPayAccount.setText(payWaySparseArray.get(TYPE_WECHAT).getAccount());
                        }
                        BaseApplication.dataMap.put("reload", false);
                    } else {
                        Toast.makeText(ReceiptWayActivity.this, "账户信息失效，无法获取数据", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(ReceiptWayActivity.this, e);
                }
            }
        }));
    }

    @Override
    public void onClick(View v) {
        if (v == unionBox) {
            //if (payWaySparseArray.get(TYPE_BANK) == null) {
            Intent intent = new Intent(this, FillBindAccountInfoActivity.class);
            intent.putExtra("mBankId", mBankId);
            intent.putExtra("mBankName", mBankName);
            intent.putExtra("type", TYPE_BANK);
            startActivity(intent);
            // }
        } else if (v == aliBox) {
            //if (payWaySparseArray.get(TYPE_ALI) == null) {
            Intent intent = new Intent(this, FillBindAccountInfoActivity.class);
            intent.putExtra("type", TYPE_ALI);
            intent.putExtra("mALiId", mALiId);
            intent.putExtra("mALiName", mALiName);
            startActivity(intent);
            // }
        } else if (v == wechatBox) {
            //if (payWaySparseArray.get(TYPE_WECHAT) == null) {
            Intent intent = new Intent(this, FillBindAccountInfoActivity.class);
            intent.putExtra("type", TYPE_WECHAT);
            intent.putExtra("mWeChatId", mWeChatId);
            intent.putExtra("mWeChatName", mWeChatName);
            startActivity(intent);
            //}
        }
    }
}
