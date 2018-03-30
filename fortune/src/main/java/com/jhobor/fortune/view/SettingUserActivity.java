package com.jhobor.fortune.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.fortune.LoginActivity;
import com.jhobor.fortune.MainActivity;
import com.jhobor.fortune.ModifyPassActivity;
import com.jhobor.fortune.R;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.utils.BarUtil;
import com.jhobor.fortune.utils.DataCleanManager;
import com.jhobor.fortune.utils.HideIMEUtil;

/**
 * Author     Qijing
 * Created by YQJ on 2018/3/27.
 * Description:
 */
public class SettingUserActivity extends AppCompatActivity {

    private LinearLayout mChanged_psd;
    private LinearLayout mAuuount_again;
    private LinearLayout mClear_cache;
    private LinearLayout mGetout;
    private TextView mCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_user);
        HideIMEUtil.wrap(this);
        BarUtil.topBar(this, "设置");
        BarUtil.topBarRight(this, false, " ");
        initView();
        initClick();
    }

    private void initView() {
        mChanged_psd = (LinearLayout) findViewById(R.id.changed_psd);
        mAuuount_again = (LinearLayout) findViewById(R.id.auuount_again);
        mClear_cache = (LinearLayout) findViewById(R.id.clear_cache);
        mGetout = (LinearLayout) findViewById(R.id.getout);
        mCache = (TextView) findViewById(R.id.nu_cache);
        try {
            String totalCacheSize = DataCleanManager.getTotalCacheSize(this);
            mCache.setText(totalCacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initClick() {
        mChanged_psd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingUserActivity.this, ModifyPassActivity.class));
            }
        });
        mAuuount_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingUserActivity.this, LoginActivity.class));
            }
        });
        mClear_cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCleanManager.clearAllCache(SettingUserActivity.this);
                mCache.setText("0K");
                Toast.makeText(SettingUserActivity.this, "清楚成功", Toast.LENGTH_SHORT).show();
            }
        });
        mGetout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLogin();
            }
        });
    }

    private void goLogin() {
        Intent intent = new Intent(SettingUserActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        BaseApplication.prefs.edit().clear().commit();
        startActivity(intent);
        finish();
    }
}
