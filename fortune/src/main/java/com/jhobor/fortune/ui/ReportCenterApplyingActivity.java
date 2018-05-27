package com.jhobor.fortune.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jhobor.fortune.R;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.dialog.LoadingDialog;
import com.jhobor.fortune.utils.ErrorUtil;
import com.vincent.filepicker.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.BindViews;

/**
 *
 * 服务中心审核中
 *
 * Created by zh on 2018/5/8.
 *
 */

public class ReportCenterApplyingActivity extends BaseActivity {


    LinearLayout layout_status;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_report_conter_applying);
        initTitle();
        titleView.setText("服务中心");
        layout_status = (LinearLayout) findViewById(R.id.layout_status);
        isBillCenter();
    }

    //查询状态
    private void isBillCenter() {
        dialog.show();
        String token = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.isBillCenter(token).enqueue(new RetrofitCallback(context, new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int msg = jsonObject.getInt("msg");
                    if (msg == 1) {
                        //status 判断是不是服务中心 状态0.不是，1.是，2审核中
                        int status = jsonObject.getInt("status");
                           switch (status){
                               case 0:
                                   layout_status.setVisibility(View.INVISIBLE);
                                   goCenter(ReportCenterApplyActivity.class);
                                   break;
                               case 1:
                                   layout_status.setVisibility(View.INVISIBLE);
                                   goCenter(ReportCenterActivity.class);
                                   break;
                               case 2:
                                   layout_status.setVisibility(View.VISIBLE);
                                   break;
                           }

                    }else {
                        String errorInfo = jsonObject.getString("errorInfo");
                        ToastUtil.getInstance(act).showToast(errorInfo);
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(context, e);
                }finally {
                    dialog.dismiss();
                }
            }
        }));
    }


    private void goCenter(Class cls){
        Intent intent = new Intent(this,cls);
        startActivity(intent);
        finish();
    }


}
