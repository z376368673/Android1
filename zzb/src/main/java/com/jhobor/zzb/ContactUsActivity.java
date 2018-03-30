package com.jhobor.zzb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.jhobor.zzb.base.BaseWithHeaderActivity;

public class ContactUsActivity extends BaseWithHeaderActivity {
    TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fillContent(R.layout.activity_contact_us);
        hideRight();
        showTitle("联系我们");

        info = (TextView) findViewById(R.id.info);
    }
}
