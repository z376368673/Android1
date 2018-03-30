package com.jhobor.zzb;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jhobor.zzb.base.BaseWithHeaderActivity;

public class MaintainActivity extends BaseWithHeaderActivity implements View.OnClickListener {
    EditText suggestion;
    Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fillContent(R.layout.activity_maintain);
        hideRight();
        showTitle("维护/投诉");

        suggestion = (EditText) findViewById(R.id.suggestion);
        ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==ok){
            View view = getLayoutInflater().inflate(R.layout.dialog_maintain_result,null);
            TextView state = (TextView) view.findViewById(R.id.state);
            TextView info = (TextView) view.findViewById(R.id.info);
            new AlertDialog.Builder(this)
                    .setView(view)
                    .create()
                    .show();
        }
    }
}
