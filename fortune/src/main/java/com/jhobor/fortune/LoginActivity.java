package com.jhobor.fortune;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.utils.CheckUtil;
import com.jhobor.fortune.utils.ErrorUtil;
import com.jhobor.fortune.utils.HideIMEUtil;
import com.jhobor.fortune.utils.PrefUtils;
import com.jhobor.fortune.utils.TextUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText mobile, userPass;
    ImageView watch;
    TextView reg, forget;
    Button login;

    boolean isHide = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        HideIMEUtil.wrap(this);


        mobile = (EditText) findViewById(R.id.mobile);
        userPass = (EditText) findViewById(R.id.userPass);
        watch = (ImageView) findViewById(R.id.watch);
        reg = (TextView) findViewById(R.id.reg);
        forget = (TextView) findViewById(R.id.forget);
        login = (Button) findViewById(R.id.login);

        watch.setOnClickListener(this);
        reg.setOnClickListener(this);
        forget.setOnClickListener(this);
        login.setOnClickListener(this);
        setDefaultInfo();


        String token = BaseApplication.prefs.getString("token", "");
        if (token.isEmpty()) {
            String m = BaseApplication.prefs.getString("phone", "");
            String p = BaseApplication.prefs.getString("pws", "");
            if (!m.isEmpty() && !p.isEmpty()) {
                login(m, p);
            }
        } else {
//            BaseApplication.dataMap.put("token", token);
//            startActivity(new Intent(this, MainActivity.class));
//            finish();
        }
    }

    private void setDefaultInfo() {

        String m = BaseApplication.prefs.getString("phone", "");
        String p = BaseApplication.prefs.getString("pws", "");
        if (!TextUtils.isEmpty(m) && !TextUtils.isEmpty(p)) {
            mobile.setText(m);
            userPass.setText(p);
        }
//        if (BaseApplication.infoList != null && BaseApplication.infoList.size() != 0) {
//            String acc = (String) BaseApplication.infoList.get(0);
//            String psd = (String) BaseApplication.infoList.get(1);
//            if (acc != null && !acc.equals("phone")) {
//                mobile.setText(acc);
//            }
//
//            if (psd != null && !psd.equals("psd")) {
//                userPass.setText(psd);
//            }
//        }

    }

    @Override
    public void onClick(View v) {
        if (v == login) {
            Object[] objectArr = TextUtil.arrange(mobile, userPass);
            String[] contentArr = (String[]) objectArr[0];
            boolean[] itemChecks = {
                    CheckUtil.isMobile(contentArr[0]),
                    CheckUtil.isPass(contentArr[1]),
            };
            String[] itemTips = {
                    "手机号码格式不正确",
                    "密码长度为6-18",

            };
            int i = CheckUtil.checkAll(itemChecks);
            if (i < itemChecks.length) {
                Toast.makeText(this, itemTips[i], Toast.LENGTH_SHORT).show();
                EditText[] editTextArr = (EditText[]) objectArr[1];
                editTextArr[i].requestFocus();
            } else {
                BaseApplication.infoList.clear();
                BaseApplication.infoList.add(contentArr[0]);
                BaseApplication.infoList.add(contentArr[1]);
                //login(contentArr[0], MD5Util.encode(contentArr[1]));
                //原来要加密，现在不用
                login(contentArr[0], contentArr[1]);
            }
        } else if (v == watch) {
            if (isHide) {
                userPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                watch.setImageResource(R.mipmap.show);
            } else {
                userPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                watch.setImageResource(R.mipmap.hide);
            }
            isHide = !isHide;
            userPass.postInvalidate();
            //切换后将EditText光标置于末尾
            CharSequence charSequence = userPass.getText();
            if (charSequence != null) {
                Spannable spanText = (Spannable) charSequence;
                Selection.setSelection(spanText, charSequence.length());
            }
        } else if (v == reg) {
            startActivity(new Intent(this, RegActivity.class));
        } else if (v == forget) {
            startActivity(new Intent(this, ForgetActivity.class));
        }
    }

    private void login(final String strMobile, final String strPass) {
        final ProgressDialog progressDialog = ProgressDialog.show(this, "登录", "登录中...");
        BaseApplication.iService.login(strMobile, strPass).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {

            private int mMsg;

            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    //int isActivation = jsonObject.getInt("isActivation");
                    mMsg = jsonObject.getInt("msg");
                    progressDialog.dismiss();
                    if (mMsg == 0) {
                        String errorInfo = jsonObject.getString("errorInfo");
                        if (!TextUtils.isEmpty(errorInfo))
                            Toast.makeText(LoginActivity.this, errorInfo, Toast.LENGTH_LONG).show();
                        mobile.setText(strMobile);
                    } else if (mMsg == 1) {
                        BaseApplication.infoList.clear();
                        BaseApplication.infoList.add(mobile.getText().toString().trim());
                        BaseApplication.infoList.add(userPass.getText().toString().trim());

                        String uuid = jsonObject.getString("uuid");
                        BaseApplication.prefs.edit()
                                .putString("phone", strMobile)
                                .putString("pws", strPass)
                                .putString("token", uuid)
                                .apply();

                        PrefUtils.putString(LoginActivity.this, "phone", strMobile);

                        BaseApplication.dataMap.put("token", uuid);
                        //startActivity(new Intent(LoginActivity.this, ChooseActivity.class));
                        //原来有个利息互助平台，现在跳转到主页
                        goMain();
                    } else if (mMsg == 2) {
                        String errorInfo = jsonObject.getString("errorInfo");
                        Toast.makeText(LoginActivity.this, errorInfo, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    String errorMsg = e.toString();
                    if (errorMsg.equals("org.json.JSONException: No value for isActivation")
                            && mMsg == 0) {
                        Toast.makeText(LoginActivity.this, "帐号或密码错误", Toast.LENGTH_LONG).show();
                    } else {
                        ErrorUtil.retrofitResponseParseFail(LoginActivity.this, e);
                    }
                    progressDialog.dismiss();
                }
            }
        }));
    }

    private void goMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }

}
