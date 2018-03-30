package com.jhobor.zzb;

import android.os.Bundle;
import android.widget.TextView;

import com.jhobor.zzb.base.BaseWithHeaderActivity;

public class SettledActivity extends BaseWithHeaderActivity {
    TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fillContent(R.layout.activity_settled);
        hideRight();
        showTitle("品牌入驻");

        info = (TextView) findViewById(R.id.info);

    }
}
