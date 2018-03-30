package com.jhobor.ddc.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.ddc.R;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.utils.ErrorUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class BindCardActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView topArrow;
    TextView topTitle, getCode;
    EditText realName, belong, cardNo, region, branch, code;
    Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_card);

        initView();
        handleEvt();
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);
        ok.setOnClickListener(this);
        getCode.setOnClickListener(this);
    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        topTitle = (TextView) findViewById(R.id.topTitle);
        getCode = (TextView) findViewById(R.id.getCode);
        realName = (EditText) findViewById(R.id.realName);
        belong = (EditText) findViewById(R.id.belong);
        cardNo = (EditText) findViewById(R.id.cardNo);
        region = (EditText) findViewById(R.id.region);
        branch = (EditText) findViewById(R.id.branch);
        code = (EditText) findViewById(R.id.code);
        ok = (Button) findViewById(R.id.ok);

        topTitle.setText("绑定银行卡");
    }

    @Override
    public void onClick(View v) {
        if (v == topArrow) {
            finish();
        } else if (v == ok) {
            final String realNameText = realName.getText().toString();
            final String bankNameText = belong.getText().toString();
            final String cardNoText = cardNo.getText().toString();
            final String regionText = region.getText().toString();
            final String branchText = branch.getText().toString();
            final String codeText = code.getText().toString();
            if (realNameText.trim().isEmpty()) {
                Toast.makeText(this, "持卡人不可留空哦", Toast.LENGTH_SHORT).show();
                realName.requestFocus();
            } else if (bankNameText.trim().isEmpty()) {
                Toast.makeText(this, "银行不可留空哦", Toast.LENGTH_SHORT).show();
                belong.requestFocus();
            } else if (cardNoText.trim().isEmpty()) {
                Toast.makeText(this, "卡号不可留空哦", Toast.LENGTH_SHORT).show();
                cardNo.requestFocus();
            } else if (regionText.trim().isEmpty()) {
                Toast.makeText(this, "所属省市不可留空哦", Toast.LENGTH_SHORT).show();
                region.requestFocus();
            } else if (branchText.trim().isEmpty()) {
                Toast.makeText(this, "所属支行不可留空哦", Toast.LENGTH_SHORT).show();
                branch.requestFocus();
            }/*else if (codeText.trim().isEmpty()){
                Toast.makeText(this,"验证码不可留空哦",Toast.LENGTH_SHORT).show();
                realName.requestFocus();
            }*/ else {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("贴心小提示")
                        .setMessage("确认信息无误？信息不正确会导致提现不成功")
                        .setPositiveButton("正确", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String uuid = (String) BaseApplication.dataMap.get("token");
                                BaseApplication.iService.bindCard(uuid, realNameText, bankNameText, cardNoText, regionText, branchText).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                                    @Override
                                    public void parse(String data) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(data);
                                            int isLogin = jsonObject.getInt("isLogin");
                                            if (isLogin == 1) {
                                                int msg = jsonObject.getInt("msg");
                                                if (msg == 1) {
                                                    Toast.makeText(getBaseContext(), "绑定成功", Toast.LENGTH_LONG).show();
                                                } else {
                                                    Toast.makeText(getBaseContext(), "绑定失败，请稍后再试", Toast.LENGTH_LONG).show();
                                                }
                                                finish();
                                            } else {
                                                Toast.makeText(getBaseContext(), "未登录，绑定失败", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (JSONException e) {
                                            ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                                        }
                                    }
                                }));
                            }
                        }).setNegativeButton("再检查一遍", null).create();
                dialog.show();

            }

        } else if (v == getCode) {

        }
    }
}
