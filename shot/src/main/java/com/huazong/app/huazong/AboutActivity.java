package com.huazong.app.huazong;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.huazong.app.huazong.base.BarUtil;
import com.huazong.app.huazong.base.BaseApplication;
import com.huazong.app.huazong.base.RetrofitCallback;
import com.huazong.app.huazong.utils.ErrorUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {
    TextView summary,state;
    Button aboutVer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_about);

        aboutVer = (Button) findViewById(R.id.aboutVer);
        summary = (TextView) findViewById(R.id.summary);
        state = (TextView) findViewById(R.id.state);
        TextView copyright = (TextView) findViewById(R.id.copyrignt);

        BarUtil.topBar(this,getIntent().getStringExtra("title"));
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        int nextYear = calendar.get(Calendar.YEAR) + 1;
        String c = "Copyright © 2016-"+nextYear+"\n深圳市印象科技有限公司";
        copyright.setText(c);
        aboutVer.setOnClickListener(this);
        getData();
    }

    private void getData() {
        BaseApplication.iService.about().enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray aboutList = jsonObject.getJSONArray("aboutList");
                    JSONObject object = aboutList.getJSONObject(0);
                    String content = object.getString("content");
                    String idpa = object.getString("idpa");
                    String ipsa = object.getString("ipsa");
                    String contact = object.getString("contact");
                    String body = String.format(Locale.CHINA, "        %s", content);
                    String footer = String.format(Locale.CHINA, "注：%s\n%s\n%s", idpa, ipsa, contact);
                    summary.setText(body);
                    state.setText(footer);
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getBaseContext(),e);
                }
            }
        }));
    }

    @Override
    public void onClick(View view) {
        if (view == aboutVer) {
            Intent intent = new Intent(getBaseContext(), VersionActivity.class);
            startActivity(intent);
        }
    }
}
