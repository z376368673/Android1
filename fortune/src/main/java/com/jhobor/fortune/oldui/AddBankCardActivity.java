package com.jhobor.fortune.oldui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
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

/**
 * Author     Qijing
 * Created by YQJ on 2018/3/30.
 * Description:
 */
public class AddBankCardActivity extends AppCompatActivity {

    private EditText with_draw_et;
    private EditText with_draw_sfz;
    private EditText with_draw_account;
    private EditText with_draw_name_sub;
    private TextView with_draw_name;//请输入银行卡类型
    private TextView with_draw_submmit;

    Context context;
    String userName;//持卡人名字
    String identityCard;//身份证号
    String bankName; //银行名称
    String bankNo;//银行卡号
    String subbranch ;//银行所属支行
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bandcard);
        context = this;
        HideIMEUtil.wrap(this);
        BarUtil.topBar(this, "添加银行卡");

        with_draw_et = (EditText) findViewById(R.id.with_draw_et);
        with_draw_sfz = (EditText) findViewById(R.id.with_draw_sfz);
        with_draw_account = (EditText) findViewById(R.id.with_draw_account);
        with_draw_name_sub = (EditText) findViewById(R.id.with_draw_name_sub);
        with_draw_name = (TextView) findViewById(R.id.with_draw_name);
        with_draw_submmit = (TextView) findViewById(R.id.with_draw_submmit);
    }

    /**
     * 校验填写的信息
     *
     * @param view
     */
    public void submit(View view) {

        userName = with_draw_et.getText().toString().trim();
        identityCard = with_draw_sfz.getText().toString().trim();
        bankName = with_draw_name.getText().toString().trim();
        bankNo = with_draw_account.getText().toString().trim();
        subbranch = with_draw_name_sub.getText().toString().trim();

        boolean[] itemChecks = {
                CheckUtil.isName(userName),
                CheckUtil.identityCard(identityCard),
                CheckUtil.isName(bankName),
                CheckUtil.isBankCardNo(bankNo),
                !TextUtils.isEmpty(subbranch),
        };
        String[] itemTips = {
                "户主姓名不正确",
                "身份证为18位数字",
                "银行名称不正确",
                "银行卡卡号为16位或19位数字",
                "支行名称能为空"
        };
        int i = CheckUtil.checkAll(itemChecks);
        if (i < itemChecks.length) {
            Toast.makeText(this, itemTips[i], Toast.LENGTH_SHORT).show();
        } else {
            progressDialog = ProgressDialog.show(this, "添加银行卡", "提交信息...");
            postData();
        }
    }

    /***
     * 提交银行卡信息
     */
    private void postData() {
        String token = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.addBank(token, userName,identityCard, bankName, bankNo,subbranch)
                .enqueue(new RetrofitCallback(context, new RetrofitCallback.DataParser() {
                    @Override
                    public void parse(String data) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            int msg = jsonObject.getInt("msg");
                            if (msg == 1) {
                                Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(context, "添加失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            ErrorUtil.retrofitResponseParseFail(context, e);
                        }
                    }
                }));
    }

    /**
     * 选择银行
     *
     * @param view
     */
    public void dialogChoice(View view) {
        final String items[] = {"中国工商银行", "招商银行", "中国农业银行", "中国建设银行",
                "中国银行", "中国民生银行", "中国光大银行", "中信银行", "交通银行", "兴业银行", "上海浦东发展银行"
                , "中国人民银行", "华夏银行", "深圳发展银行", "广东发展银行", "国家开发银行", "北京银行", "中国邮政储蓄银行", "上海银行"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this, 3);
        builder.setTitle("请选择银行");
        builder.setIcon(R.mipmap.ic_bank);
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        with_draw_name.setText(items[which]);
                    }
                });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

}
