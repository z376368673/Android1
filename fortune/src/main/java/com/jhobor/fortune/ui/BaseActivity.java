package com.jhobor.fortune.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.jhobor.fortune.R;
import com.jhobor.fortune.dialog.LoadingDialog;

/**
 * Created by zh on 2018/5/8.
 */

public class BaseActivity  extends AppCompatActivity implements View.OnClickListener {

    Activity act;
    Context context;
    public TextView titleView ;
    public View back;
    public  LoadingDialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act =this;
        context = this;
        dialog = new LoadingDialog(this);
    }

    public void initTitle(){
        titleView = (TextView) findViewById(R.id.topTitle);
        back =  findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void startAct(Class cls){
        Intent intent = new Intent(context,cls);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {

    }
}
