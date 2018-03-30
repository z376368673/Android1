package com.jhobor.zzb.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.zzb.R;

/**
 * Created by Administrator on 2017/7/22.
 */

public class BaseWithHeaderActivity extends BaseActivity {
    protected ImageView back,share;
    protected TextView subject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        back = (ImageView)findViewById(R.id.back);
        share = (ImageView)findViewById(R.id.share);
        subject = (TextView)findViewById(R.id.subject);
        MyClickListener myClickListener = new MyClickListener();
        back.setOnClickListener(myClickListener);
        share.setOnClickListener(myClickListener);
    }

    protected void fillContent(int layoutResId){
        FrameLayout content = (FrameLayout) findViewById(R.id.content);
        View v = getLayoutInflater().inflate(layoutResId,null);
        content.addView(v);
    }

    protected void hideRight(){
        share.setVisibility(View.INVISIBLE);
    }

    protected void showTitle(String title){
        subject.setText(title);
        subject.setVisibility(View.VISIBLE);
    }

    private class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.i("onClick>>","BaseWithHeaderActivity onClick");
            if (v==back){
                finish();
            }else if (v==share){
                BaseDialog.showSharePanel(BaseWithHeaderActivity.this);
            }
        }
    }
}
