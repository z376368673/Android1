package com.jhobor.fortune;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class GetHandActivity extends AppCompatActivity implements View.OnClickListener {
    TextView balance,prize;
    ImageView useStatic,usePrize;
    EditText money;
    Button ok;
    //Spinner receiptWay;

    private static final int STATIC_WALLET = 0;
    private static final int PRIZE_WALLET = 1;

    int toWhere;//提现到哪里，0-静态钱包，1-奖金钱包
    List<String> wayList = new ArrayList<>(3);
    List<Integer> wayIdList = new ArrayList<>(3);
    double staticMoney,bonusMoney;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_hand);
        HideIMEUtil.wrap(this);

        balance = (TextView) findViewById(R.id.balance);
        prize = (TextView) findViewById(R.id.prize);
        useStatic = (ImageView) findViewById(R.id.useStatic);
        usePrize = (ImageView) findViewById(R.id.usePrize);
        money = (EditText) findViewById(R.id.money);
        ok = (Button) findViewById(R.id.ok);
        //receiptWay = (Spinner) findViewById(receiptWay);
        BarUtil.topBar(this, "得到帮助");

        Intent intent = getIntent();
        staticMoney = intent.getDoubleExtra("staticMoney",0);
        bonusMoney = intent.getDoubleExtra("bonusMoney",0);
        balance.setText(String.format(Locale.CHINA,"%.1f",staticMoney));
        prize.setText(String.format(Locale.CHINA,"%.1f",bonusMoney));
        usePrize.setOnClickListener(this);
        useStatic.setOnClickListener(this);
        ok.setOnClickListener(this);
        getData();
    }

    private void getData() {
        String token = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.myOm(token).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int isLogin = jsonObject.getInt("isLogin");
                    if (isLogin == 1) {
                        JSONArray omList = jsonObject.getJSONArray("omList");

                        int length = omList.length();

                        for (int i = 0;i<length;i++){
                            JSONArray jsonArray = omList.getJSONArray(i);
                            int id = jsonArray.getInt(0);
                            String way = jsonArray.getString(1);
                            String number = jsonArray.getString(2);
                            wayIdList.add(id);
                           /* String str = "提现到银行卡";
                            if (way.equals("微信")){
                                str = "提现到微信";
                            }else if (way.equals("支付宝")){
                                str = "提现到支付宝";
                            }
                            wayList.add(str);*/
                        }
                        //本来有显示收款方式的，注销掉了
                        // receiptWay.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, wayList));
                    } else {
                        Toast.makeText(getBaseContext(), "你未登录，获取数据失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                }
            }
        }));
    }

    @Override
    public void onClick(View v) {
        if (v==useStatic){
            if (toWhere == PRIZE_WALLET){
                useStatic.setImageResource(R.mipmap.wallet_checked);
                usePrize.setImageResource(R.mipmap.wallet_unchecked);
                toWhere = STATIC_WALLET;
            }
        }else if (v==usePrize){
            if (toWhere==STATIC_WALLET){
                useStatic.setImageResource(R.mipmap.wallet_unchecked);
                usePrize.setImageResource(R.mipmap.wallet_checked);
                toWhere = PRIZE_WALLET;
            }
        }else if (v == ok) {
            BaseApplication.dataMap.put("reload", true);
            String string = money.getText().toString();
            if (string.isEmpty()) {
                Toast.makeText(this, "得到帮助的金额不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            int mon = Integer.parseInt(string);
            if (mon < 500 || mon > 50000 || mon % 100 != 0) {
                Toast.makeText(this, "金额应在500-50000之间，且是100的整数倍", Toast.LENGTH_SHORT).show();
                return;
            }
            if (wayIdList.size()==0){
                //Toast.makeText(this, "请先添加收款（提现）方式", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "您未绑定收款（提现）方式，请前往个人中心绑定", Toast.LENGTH_SHORT).show();
                return;
            }
            if (toWhere==STATIC_WALLET&&mon>staticMoney){
                Toast.makeText(this, "得到帮助的金额不能大于静态钱包", Toast.LENGTH_SHORT).show();
                return;
            }else if (toWhere==PRIZE_WALLET&&mon>bonusMoney){
                Toast.makeText(this, "得到帮助的金额不能大于奖金钱包", Toast.LENGTH_SHORT).show();
                return;
            }
            //long selectedItemId = receiptWay.getSelectedItemId();
            //int omId = wayIdList.get((int) selectedItemId);
            int omId = wayIdList.get(0);
            String token = (String) BaseApplication.dataMap.get("token");
            if (toWhere==STATIC_WALLET){
                withdrawFromStatic(token,mon,omId);
            }else if (toWhere==PRIZE_WALLET){
                withdrawFromPrize(token,mon,omId);
            }
        }
    }

    private void withdrawFromPrize(String token, int mon, int omId) {
        BaseApplication.iService.withdrawFromPrize(token, mon, omId).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int isLogin = jsonObject.getInt("isLogin");
                    if (isLogin == 1) {
                        int msg = jsonObject.getInt("msg");
                        if (msg == 1) {
                            Toast.makeText(getBaseContext(), "提交成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getBaseContext(), "提交失败，余额不足或已正在提现", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getBaseContext(), "你未登录，不能提交", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                }
            }
        }));
    }

    private void withdrawFromStatic(String token, int mon, int omId) {
        BaseApplication.iService.help(token, mon, 0, omId).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int isLogin = jsonObject.getInt("isLogin");
                    if (isLogin == 1) {
                        int msg = jsonObject.getInt("msg");
                        if (msg == 1) {
                            Toast.makeText(getBaseContext(), "提交成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getBaseContext(), "提交失败，余额不足或已正在提现", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getBaseContext(), "你未登录，不能提交", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                }
            }
        }));
    }
}
