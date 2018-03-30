package com.huazong.app.huazong;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.huazong.app.huazong.base.BarUtil;
import com.huazong.app.huazong.base.BaseActivity;
import com.huazong.app.huazong.base.BaseApplication;
import com.huazong.app.huazong.base.RetrofitCallback;
import com.huazong.app.huazong.utils.ErrorUtil;
import com.huazong.app.huazong.utils.IpUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WalletActivity extends BaseActivity implements View.OnClickListener {
    static final int CHOOSE_PAY_WAY = 100;
    TextView money;
    EditText inputMoney;
    Button ok;

    IWXAPI iwxapi;
    int number;
    long lastMillis = 0;
    private String ordersNo;
    private  BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            cancelOrder();
        }
    };
    Handler msgHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Toast.makeText(getBaseContext(), (CharSequence) msg.obj,Toast.LENGTH_SHORT).show();
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_wallet);

        money = (TextView) findViewById(R.id.money);
        inputMoney = (EditText) findViewById(R.id.inputMoney);
        ok = (Button) findViewById(R.id.ok);

        iwxapi = WXAPIFactory.createWXAPI(this, BaseApplication.APP_ID);
        Intent intent = getIntent();
        BarUtil.topBar(this,intent.getStringExtra("title"));
        ok.setOnClickListener(this);
        registerReceiver(receiver,new IntentFilter("wechatPayCallback"));
        getData();
    }

    private void getData() {
        String openid = (String) BaseApplication.dataMap.get("openid");
        BaseApplication.iService.getBalance(openid).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                money.setText(String.format(Locale.CHINA,"￥%s",data));
            }
        }));
    }

    @Override
    public void onClick(View view) {
        if (view == ok) {
            long millis = System.currentTimeMillis();
            if (millis-lastMillis<5*1000){
                showMsg("操作太频繁，请稍候再试");
                return;
            }
            lastMillis = millis;
            String string = inputMoney.getText().toString();
            if (string.isEmpty()) {
                showMsg("请输入金额");
                return;
            }
            number = Integer.parseInt(string);
            if (number<10){
                showMsg("充值金额不能少于10");
                return;
            }
            Intent intent = new Intent(this, ChoosePayWayActivity.class);
            startActivityForResult(intent, CHOOSE_PAY_WAY);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSE_PAY_WAY) {
            if (resultCode == RESULT_OK) {
                int way = data.getIntExtra("way", 0);
                lastMillis = System.currentTimeMillis();
                if (way == ChoosePayWayActivity.PAY_WITH_WE_CHAT) {
                    // 微信支付
                    payWithWechat();
                } else if (way == ChoosePayWayActivity.PAY_WITH_ALIPAY) {
                    // 支付宝支付
                    payWithAli();
                } else if (way == ChoosePayWayActivity.PAY_WITH_MEMBER_WALLET) {
                    showMsg("不能用会员支付的方式充值");
                }

            } else {
                showMsg("取消充值");
            }
        }
    }

    private void payWithAli() {
        String openid = (String) BaseApplication.dataMap.get("openid");
        BaseApplication.iService.rechargeWithAli(openid,number).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(final String data) {
                if ("sb".equals(data)){
                    showMsg("充值失败，请稍候再试");
                }else {
                    // 必须异步调用
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(WalletActivity.this);
                            String result = alipay.pay(data, true);

                            boolean flag = false;
                            Pattern pattern = Pattern.compile("resultStatus=\\{(\\d+)\\}");
                            Matcher matcher = pattern.matcher(result);
                            String resultStatus = "0000";
                            if (matcher.find()) {
                                resultStatus = matcher.group(1);
                                if ("9000".equals(resultStatus)) {
                                    Pattern patternResult = Pattern.compile("success=\"(\\w+)\"");
                                    Matcher matcherResult = patternResult.matcher(result);
                                    if (matcherResult.find()) {
                                        String group1 = matcherResult.group(1);
                                        if ("true".equals(group1)) {
                                            flag = true;
                                        }
                                    }
                                }
                            }
                            Matcher matcher1 = Pattern.compile("out_trade_no=\"(\\w+)\"").matcher(data);
                            ordersNo = "";
                            if (matcher1.find()) {
                                ordersNo = matcher1.group(1);
                            }
                            if (flag) {
                                getData();
                                showMsg("充值成功");
                            } else {
                                cancelOrder();
                                showMsg("充值失败 - " + resultStatus);
                            }
                        }
                    }).start();
                }
            }
        }));
    }

    public void showMsg(String msg) {
        Message message = new Message();
        message.obj = msg;
        msgHandler.sendMessage(message);
    }

    private void payWithWechat() {
        String openid = (String) BaseApplication.dataMap.get("openid");
        BaseApplication.iService.rechargeWithWechat(openid,number,IpUtil.getIpAddress(getBaseContext())).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    ordersNo = jsonObject.getString("ordersNo");
                    PayReq req = new PayReq();
                    req.appId			= jsonObject.getString("appid");
                    req.partnerId		= jsonObject.getString("partnerid");
                    req.prepayId		= jsonObject.getString("prepayid");
                    req.packageValue	= jsonObject.getString("packageValue");
                    req.nonceStr		= jsonObject.getString("noncestr");
                    req.timeStamp		= jsonObject.getString("timestamp");
                    req.sign			= jsonObject.getString("sign");
                    iwxapi.sendReq(req);
                    lastMillis = System.currentTimeMillis();
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getBaseContext(),e);
                }
            }
        }));
    }

    void cancelOrder(){
        if (ordersNo != null&&!ordersNo.isEmpty()) {
            BaseApplication.iService.cancelOrder(ordersNo).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                @Override
                public void parse(String data) {

                }
            }));
        }else {
            Log.e(">>","取消订单失败");
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
