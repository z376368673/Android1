package com.jhobor.zzb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhobor.zzb.base.BaseActivity;

public class SearchActivity extends BaseActivity implements View.OnClickListener {
    ImageView back;
    EditText keyword;
    TextView search;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        back = (ImageView) findViewById(R.id.back);
        keyword = (EditText) findViewById(R.id.keyword);
        search = (TextView) findViewById(R.id.search);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        back.setOnClickListener(this);
        search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==back){
            finish();
        }else if (v==search){
            String strKeyword = keyword.getText().toString().trim();

        }
    }
}
