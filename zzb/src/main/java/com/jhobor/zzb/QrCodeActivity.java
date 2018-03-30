package com.jhobor.zzb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jhobor.zzb.base.BaseActivity;

public class QrCodeActivity extends BaseActivity implements View.OnClickListener {
    ImageView close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        close = (ImageView) findViewById(R.id.close);
        close.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==close){
            finish();
        }
    }
}
