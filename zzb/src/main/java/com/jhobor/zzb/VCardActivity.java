package com.jhobor.zzb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.jhobor.zzb.base.BaseActivity;

public class VCardActivity extends BaseActivity implements View.OnClickListener {
    ImageView back,share,cardPicture;
    EditText company,name,job,phoneNum,email,addr;
    Spinner province,city,block;
    Button update,save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vcard);

        back = (ImageView) findViewById(R.id.back);
        share = (ImageView) findViewById(R.id.share);
        cardPicture = (ImageView) findViewById(R.id.cardPicture);
        company = (EditText) findViewById(R.id.company);
        name = (EditText) findViewById(R.id.name);
        job = (EditText) findViewById(R.id.job);
        phoneNum = (EditText) findViewById(R.id.phoneNum);
        email = (EditText) findViewById(R.id.email);
        addr = (EditText) findViewById(R.id.addr);
        province = (Spinner) findViewById(R.id.province);
        city = (Spinner) findViewById(R.id.city);
        block = (Spinner) findViewById(R.id.block);
        update = (Button) findViewById(R.id.update);
        save = (Button) findViewById(R.id.save);

        share.setVisibility(View.INVISIBLE);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==back){
            finish();
        }else if (v==cardPicture){

        }else if (v==update){

        }else if (v==save){

        }
    }
}
