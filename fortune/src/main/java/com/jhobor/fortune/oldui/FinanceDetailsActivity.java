package com.jhobor.fortune.oldui;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
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

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class FinanceDetailsActivity extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {
    TextView capital, account, payee, payBank, payWay, customerService;
    ImageView call;

    int orderId;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_details);
        HideIMEUtil.wrap(this);

        capital = (TextView) findViewById(R.id.capital);
        account = (TextView) findViewById(R.id.account);
        payee = (TextView) findViewById(R.id.payee);
        payBank = (TextView) findViewById(R.id.payBank);
        payWay = (TextView) findViewById(R.id.payWay);
        customerService = (TextView) findViewById(R.id.customerService);
        call = (ImageView) findViewById(R.id.call);

        BarUtil.topBar(this, "平台收款信息");
        call.setOnClickListener(this);
        Intent intent = getIntent();
        orderId = intent.getIntExtra("orderId", 0);
        getData();
    }

    private void getData() {
        String token = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.fortuneDetails(token, orderId).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int isLogin = jsonObject.getInt("isLogin");
                    if (isLogin == 1) {
                        double money = jsonObject.getDouble("money");
                        JSONArray collection = jsonObject.getJSONArray("collection");
                        JSONObject obj = collection.getJSONObject(0);
                        String name = obj.getString("name");
                        String bankName = obj.getString("bankName");
                        String acct = obj.getString("account");
                        phone = obj.getString("phone");
                        capital.setText(String.valueOf((int) money));
                        account.setText(acct);
                        payee.setText(name);
                        payBank.setText(bankName);

                        customerService.setText(phone);
                    } else {
                        Toast.makeText(FinanceDetailsActivity.this, "未登录，获取数据失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(FinanceDetailsActivity.this, e);
                }
            }
        }));
    }

    @Override
    public void onClick(View v) {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CALL_PHONE)) {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone)));
        } else {
            EasyPermissions.requestPermissions(this, "请求授予打电话权限", 100, Manifest.permission.CALL_PHONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).setTitle("授权").setRationale("需要您授予电话权限给本应用").build().show();
        }
    }
}
