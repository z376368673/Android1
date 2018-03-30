package com.jhobor.zzb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhobor.zzb.base.BaseActivity;

public class UpdateInfoActivity extends BaseActivity implements View.OnClickListener {
    ImageView back;
    TextView save;
    EditText userName;
    FrameLayout avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);

        back = (ImageView) findViewById(R.id.back);
        save = (TextView) findViewById(R.id.save);
        userName = (EditText) findViewById(R.id.userName);
        avatar = (FrameLayout) findViewById(R.id.avatar);

        back.setOnClickListener(this);
        save.setOnClickListener(this);
        avatar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==back){
            setResult(RESULT_CANCELED);
            finish();
        }else if (v==avatar){

        }else if (v==save){
            setResult(RESULT_OK);
            finish();
        }
    }
}
