package com.jhobor.fortune;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseActivity extends AppCompatActivity implements View.OnClickListener {
    Button helpPlatform,financePlatform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        helpPlatform = (Button) findViewById(R.id.helpPlatform);
        financePlatform = (Button) findViewById(R.id.financePlatform);
        helpPlatform.setOnClickListener(this);
        financePlatform.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==helpPlatform){
            startActivity(new Intent(this,TradeDetailsActivity.class));
        }else if (v==financePlatform){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }
}
