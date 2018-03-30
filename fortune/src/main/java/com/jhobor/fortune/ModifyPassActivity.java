package com.jhobor.fortune;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.utils.BarUtil;
import com.jhobor.fortune.utils.CheckUtil;
import com.jhobor.fortune.utils.ErrorUtil;
import com.jhobor.fortune.utils.HideIMEUtil;
import com.jhobor.fortune.utils.MD5Util;
import com.jhobor.fortune.utils.TextUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class ModifyPassActivity extends AppCompatActivity implements View.OnClickListener {
    EditText pass, newPass, confirmPass;
    Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pass);
        HideIMEUtil.wrap(this);

        pass = (EditText) findViewById(R.id.pass);
        newPass = (EditText) findViewById(R.id.newPass);
        confirmPass = (EditText) findViewById(R.id.confirmPass);
        ok = (Button) findViewById(R.id.ok);

        BarUtil.topBar(this, "修改密码");
        ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == ok) {
            Object[] objectArr = TextUtil.arrange(pass, newPass, confirmPass);
            String[] contentArr = (String[]) objectArr[0];
            boolean[] itemChecks = {
                    CheckUtil.isPass(contentArr[0]),
                    CheckUtil.isPass(contentArr[1]) && !contentArr[1].equals(contentArr[0]),
                    CheckUtil.isSame(contentArr[1], contentArr[2]),
            };
            String[] itemTips = {
                    "密码长度为6-18",
                    "新密码长度为6-18，且不和旧密码相同",
                    "新密码和确认密码不一致",
            };
            int i = CheckUtil.checkAll(itemChecks);
            if (i < itemChecks.length) {
                Toast.makeText(this, itemTips[i], Toast.LENGTH_SHORT).show();
                EditText[] editTextArr = (EditText[]) objectArr[1];
                editTextArr[i].requestFocus();
            } else {
                String token = (String) BaseApplication.dataMap.get("token");
                String strPass = MD5Util.encode(contentArr[0]);
                String strNewPass = MD5Util.encode(contentArr[1]);
                BaseApplication.iService.updatePass(token, strNewPass, strPass).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                    @Override
                    public void parse(String data) {
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            int isLogin = jsonObject.getInt("isLogin");
                            if (isLogin == 1) {
                                int msg = jsonObject.getInt("msg");
                                if (msg == 1) {
                                    BaseApplication.prefs.edit().remove("token").apply();
                                    Toast.makeText(ModifyPassActivity.this, "密码修改成功，下次需要重新登录", Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(ModifyPassActivity.this, "密码修改失败，可能旧密码错误", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(ModifyPassActivity.this, "账户信息失效，无法获取数据", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            ErrorUtil.retrofitResponseParseFail(ModifyPassActivity.this, e);
                        }
                    }
                }));
            }
        }
    }
}
