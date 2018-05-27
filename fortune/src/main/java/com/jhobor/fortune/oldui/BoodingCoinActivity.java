package com.jhobor.fortune.oldui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.fortune.R;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.utils.BarUtil;
import com.jhobor.fortune.utils.CheckUtil;
import com.jhobor.fortune.utils.ErrorUtil;
import com.jhobor.fortune.utils.HideIMEUtil;
import com.jhobor.fortune.utils.TextUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class BoodingCoinActivity extends AppCompatActivity implements View.OnClickListener {
    TextView boodingCoin;
    EditText account, count;
    Button ok;

    int iboodingcoin;
    int icount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booding_coin);
        HideIMEUtil.wrap(this);

        boodingCoin = (TextView) findViewById(R.id.boodingCoin);
        account = (EditText) findViewById(R.id.account);
        count = (EditText) findViewById(R.id.count);
        ok = (Button) findViewById(R.id.ok);

        BarUtil.topBar(this, "排单币转让");
        getData();
        ok.setOnClickListener(this);
    }

    private void getData() {
        String token = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.myCoin(token).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int isLogin = jsonObject.getInt("isLogin");
                    if (isLogin == 1) {
                        iboodingcoin = jsonObject.getInt("boodingcoin");
                        boodingCoin.setText(String.valueOf(iboodingcoin));
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
        Object[] objectArr = TextUtil.arrange(account, count);
        final String[] contentArr = (String[]) objectArr[0];
        icount = -1;
        if (!contentArr[1].isEmpty()) {
            icount = Integer.parseInt(contentArr[1]);
        }
        boolean[] itemChecks = {
                CheckUtil.isMobile(contentArr[0]),
                icount > 0 && icount <= iboodingcoin
        };
        String[] itemTips = {
                "不是合法的账号",
                "转让数量不可少于1，且不大于" + iboodingcoin
        };
        int i = CheckUtil.checkAll(itemChecks);
        if (i < itemChecks.length) {
            Toast.makeText(this, itemTips[i], Toast.LENGTH_SHORT).show();
            EditText[] editTextArr = (EditText[]) objectArr[1];
            editTextArr[i].requestFocus();
        } else {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("确认")
                    .setMessage("确定转让吗？")
                    .setNegativeButton("取消",null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            assignment(contentArr[0], icount);
                        }
                    }).create();
            dialog.show();
        }
    }

    private void assignment(String mobile, int icount) {
        String token = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.transferCoin(token, icount, mobile).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int isLogin = jsonObject.getInt("isLogin");
                    if (isLogin == 1) {
                        int msg = jsonObject.getInt("msg");
                        if (msg == 1) {
                            Toast.makeText(getBaseContext(), "转让成功", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(getBaseContext(), "转让失败，可能是【对方账号】不存在或已被封号", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getBaseContext(), "你未登录，提交失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                }
            }
        }));
    }
}
