package com.jhobor.fortune.oldui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jhobor.fortune.R;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.utils.BarUtil;
import com.jhobor.fortune.utils.ErrorUtil;
import com.jhobor.fortune.utils.HideIMEUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class GiveHandActivity extends AppCompatActivity implements View.OnClickListener {
    EditText money;
    Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_hand);
        HideIMEUtil.wrap(this);

        money = (EditText) findViewById(R.id.money);
        ok = (Button) findViewById(R.id.ok);
        BarUtil.topBar(this, "提供帮助");
        ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == ok) {
            BaseApplication.dataMap.put("reload", true);
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("请等待");
            dialog.show();
            String string = money.getText().toString();
            if (string.isEmpty()) {
                Toast.makeText(this, "提供帮助的金额不能为空", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                return;
            }
            int mon = Integer.parseInt(string);
            if (mon < 2000 || mon > 20000 || mon % 100 != 0) {
                Toast.makeText(this, "金额应在2000-50000之间，且是100的整数倍", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                return;
            }
            String token = (String) BaseApplication.dataMap.get("token");
            BaseApplication.iService.help(token, mon, 1, 0).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                @Override
                public void parse(String data) {
                    try {
                        dialog.dismiss();
                        JSONObject jsonObject = new JSONObject(data);
                        int isLogin = jsonObject.getInt("isLogin");
                        if (isLogin == 1) {
                            int msg = jsonObject.getInt("msg");
                            if (msg == 1) {
                                Toast.makeText(getBaseContext(), "提交成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (msg == 2){
                                Toast.makeText(getBaseContext(), "提交失败，帐号被冻结或正在提供帮助", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getBaseContext(), "提交失败，可能因为你的派单币不足", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getBaseContext(), "你未登录，不能提交", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        ErrorUtil.retrofitResponseParseFail(GiveHandActivity.this, e);
                    }
                }
            }));
        }
    }
}
