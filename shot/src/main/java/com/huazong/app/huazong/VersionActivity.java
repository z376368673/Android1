package com.huazong.app.huazong;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ListView;

import com.huazong.app.huazong.adapter.VersionArrayAdapter;
import com.huazong.app.huazong.base.BarUtil;
import com.huazong.app.huazong.entity.VerInfo;

import java.util.ArrayList;
import java.util.List;

public class VersionActivity extends AppCompatActivity {
    ListView versionInfoView;

    List<VerInfo> verList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_version);

        versionInfoView = (ListView) findViewById(R.id.versionInfoView);
        BarUtil.topBar(this,"系统版本");

        verList = new ArrayList<>();
        verList.add(new VerInfo("【战术射击训练营】1.0 应用发布啦！", "2016-08-15", "18:30:00"));
        verList.add(new VerInfo("应用更新到2.0啦！大幅优化性能，提升用户体验", "2017-07-20", "18:30:00"));
        VersionArrayAdapter adapter = new VersionArrayAdapter(this, R.layout.item_version, verList);
        versionInfoView.setAdapter(adapter);
    }
}
