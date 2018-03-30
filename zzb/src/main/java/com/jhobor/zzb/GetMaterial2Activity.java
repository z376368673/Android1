package com.jhobor.zzb;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jhobor.zzb.base.BaseActivity;

import java.util.Locale;

public class GetMaterial2Activity extends BaseActivity implements View.OnClickListener {
    ImageView back,share;
    TextView target,way,myCard;
    Button send;
    LinearLayout tips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_material2);

        back = (ImageView) findViewById(R.id.back);
        share = (ImageView) findViewById(R.id.share);
        target = (TextView) findViewById(R.id.target);
        way = (TextView) findViewById(R.id.way);
        myCard = (TextView) findViewById(R.id.myCard);
        send = (Button) findViewById(R.id.send);
        tips = (LinearLayout) findViewById(R.id.tips);

        Intent intent = getIntent();
        boolean showTips = intent.getBooleanExtra("showTips", true);
        share.setVisibility(View.INVISIBLE);
        if (!showTips) {
            tips.setVisibility(View.INVISIBLE);
        }
        back.setOnClickListener(this);
        myCard.setOnClickListener(this);
        send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==back){
            finish();
        }else if (v==send){
            LinearLayout container = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_round_grid, null, false);
            View itemView = getLayoutInflater().inflate(R.layout.item_dialog_property, null, false);
            TextView key = (TextView) itemView.findViewById(R.id.key);
            TextView value = (TextView) itemView.findViewById(R.id.value);
            key.setText("联系方式");
            value.setText(String.format(Locale.CHINA,"QQ：%s","1434129383"));
            container.addView(itemView);
            final AlertDialog dialog = new AlertDialog.Builder(this)
                    .setView(container)
                    .setCancelable(false)
                    .show();
            container.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }else if (v==myCard){
            startActivity(new Intent(this,VCardActivity.class));
        }
    }
}
