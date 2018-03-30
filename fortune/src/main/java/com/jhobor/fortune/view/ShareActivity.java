package com.jhobor.fortune.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.fortune.R;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.utils.BarUtil;
import com.jhobor.fortune.utils.HideIMEUtil;
import com.jhobor.fortune.utils.RQcode;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author     Qijing
 * Created by YQJ on 2018/3/27.
 * Description:
 */
public class ShareActivity extends AppCompatActivity {

    private ImageView mShare_share;
    private ImageView mShare_iv;
    private TextView mSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        HideIMEUtil.wrap(this);
        BarUtil.topBar(this, "分享");
        BarUtil.topBarRight(this, false, " ");
        mShare_share = (ImageView) findViewById(R.id.share_share);
        mShare_iv = (ImageView) findViewById(R.id.share_iv);
        mSave = (TextView) findViewById(R.id.share_submmit);
        getData();
    }

    private void getData() {
        String uuid = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.share(uuid).enqueue(new RetrofitCallback(this, new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int msg = jsonObject.optInt("msg");
                    if (msg == 1) {
                        String qrCode = jsonObject.optString("qrCode");
                        Bitmap rQcode = RQcode.getRQcode(qrCode);
                        mShare_iv.setImageBitmap(rQcode);
                    } else {
                        Toast.makeText(ShareActivity.this, "二维码获取失败", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));
    }


}
