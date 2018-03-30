package com.jhobor.zzb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.jhobor.zzb.adapter.SettingAdapter;
import com.jhobor.zzb.base.BaseApp;
import com.jhobor.zzb.base.BaseWithHeaderActivity;
import com.jhobor.zzb.entity.SettingItem;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends BaseWithHeaderActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    ListView listView;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fillContent(R.layout.activity_setting);
        hideRight();
        showTitle("设置");

        listView = (ListView) findViewById(R.id.listView);
        logout = (Button) findViewById(R.id.logout);

        logout.setOnClickListener(this);
        List<SettingItem> settingItemList = new ArrayList<>();
        settingItemList.add(new SettingItem("个人中心",""));
        settingItemList.add(new SettingItem("变更帐号",""));
        settingItemList.add(new SettingItem("投诉/维权",""));
        settingItemList.add(new SettingItem("清除缓存","20M"));
        settingItemList.add(new SettingItem("最新版本","V-1.2"));
        settingItemList.add(new SettingItem("品牌入驻",""));
        settingItemList.add(new SettingItem("联系我们",""));
        SettingAdapter adapter = new SettingAdapter(this, settingItemList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==logout){
            BaseApp.sp.edit().remove("token").apply();
            Toast.makeText(this,"已退出登录",Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position==0){
            startActivity(new Intent(this, UpdateInfoActivity.class));
        }else if (position==1){
            startActivity(new Intent(this,ChangeAccountActivity.class));
        }else if (position==2){
            startActivity(new Intent(this,MaintainActivity.class));
        }else if (position==5){
            startActivity(new Intent(this,SettledActivity.class));
        }else if (position==6){
            startActivity(new Intent(this,ContactUsActivity.class));
        }
    }
}
