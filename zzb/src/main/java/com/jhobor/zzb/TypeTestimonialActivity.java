package com.jhobor.zzb;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhobor.zzb.base.BaseActivity;

import java.util.Locale;

public class TypeTestimonialActivity extends BaseActivity implements View.OnClickListener {
    ImageView back;
    TextView subject,save, tips;
    EditText testimonial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_testimonial);

        back = (ImageView) findViewById(R.id.back);
        subject = (TextView) findViewById(R.id.subject);
        save = (TextView) findViewById(R.id.save);
        tips = (TextView) findViewById(R.id.tips);
        testimonial = (EditText) findViewById(R.id.testimonial);

        back.setOnClickListener(this);
        save.setOnClickListener(this);
        testimonial.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tips.setText(String.format(Locale.CHINA,"%d/100", s.toString().length()));
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v==back){
            finish();
        }else if (v==save){
            String str = testimonial.getText().toString();

            finish();
        }
    }
}
