package com.huazong.app.huazong;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.huazong.app.huazong.base.BaseActivity;
import com.huazong.app.huazong.base.BaseApplication;
import com.huazong.app.huazong.base.RetrofitCallback;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    ProgressDialog progressDialog;
    private ImageView loginWithQq, loginWithWeChat;
    private boolean isProcessing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginWithQq = (ImageView) findViewById(R.id.loginWithQq);
        loginWithWeChat = (ImageView) findViewById(R.id.loginWithWeChat);
        loginWithQq.setOnClickListener(this);
        loginWithWeChat.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (isProcessing) {
            Toast.makeText(getBaseContext(), "正在登录...", Toast.LENGTH_SHORT).show();
            return;
        }
        isProcessing = true;
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("建立连接");
        progressDialog.setMessage("应用正在和第三方建立连接，请稍候...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        if (view == loginWithQq) {
            getInfo(SHARE_MEDIA.QQ);
        } else if (view == loginWithWeChat) {
            getInfo(SHARE_MEDIA.WEIXIN);
        }
    }

    private void getInfo(SHARE_MEDIA share_media) {
        BaseApplication.umShareAPI.getPlatformInfo(this, share_media, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) { }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                Set<Map.Entry<String, String>> entrySet = map.entrySet();
                for (Map.Entry<String, String> entry : entrySet) {
                    Log.i(">>",entry.getKey()+" : "+entry.getValue());
                }
                String openid = map.get("openid");
                String screen_name = map.get("screen_name");
                String profile_image_url = map.get("profile_image_url");
                BaseApplication.dataMap.put("openid", openid);
                BaseApplication.dataMap.put("nickname", screen_name);
                BaseApplication.dataMap.put("userPicture", profile_image_url);
                try {
                    JSONObject jsonData = new JSONObject();
                    jsonData.put("openid", openid);

                    JSONObject userInfo = new JSONObject();
                    userInfo.put("nickname", screen_name);
                    userInfo.put("figureurl_qq_2", profile_image_url);
                    String strJsonData = jsonData.toString();
                    String strUserInfo = userInfo.toString();
                    Log.i(">>",String.format(Locale.CHINA,"jsonData: %s\n userInfo: %s", strJsonData, strUserInfo));
                    BaseApplication.iService.login(strJsonData, strUserInfo,"qq").enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                        @Override
                        public void parse(String data) {
                            if (!data.isEmpty()){
                                showResult("登录成功");
                                jump();
                            }else {
                                showResult("登录失败");
                            }
                        }
                    }));
                } catch (JSONException e) {
                    showResult("登录失败");
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                showResult("获取用户资料失败");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                showResult("取消登录");
            }
        });
    }

    private void showResult(String result) {
        Toast.makeText(getBaseContext(),result,Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
        isProcessing = false;
    }

    private void jump() {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
