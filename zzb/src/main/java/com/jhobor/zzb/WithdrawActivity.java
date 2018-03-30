package com.jhobor.zzb;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jhobor.zzb.adapter.WithdrawRecommendRecordAdapter;
import com.jhobor.zzb.base.BaseActivity;
import com.jhobor.zzb.entity.RecommendMember;

import java.util.ArrayList;

public class WithdrawActivity extends BaseActivity implements View.OnClickListener {
    TextView back;
    ListView recommendRecord;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        back = (TextView) findViewById(R.id.back);
        recommendRecord = (ListView) findViewById(R.id.recommendRecord);
        next = (Button) findViewById(R.id.next);
        ArrayList<RecommendMember> recommendMembers = new ArrayList<>();
        recommendMembers.add(new RecommendMember(1,"180****3109",3));
        recommendMembers.add(new RecommendMember(2,"180****3109",3));
        WithdrawRecommendRecordAdapter adapter = new WithdrawRecommendRecordAdapter(this, recommendMembers);
        recommendRecord.setAdapter(adapter);
        back.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==back){
            finish();
        }else if (v==next){
            showWithdrawResult();
        }
    }

    private void showWithdrawResult() {
        View view = getLayoutInflater().inflate(R.layout.dialog_my_brand_withdraw_result,null);
        TextView msg = (TextView) view.findViewById(R.id.msg);
        TextView tips = (TextView) view.findViewById(R.id.tips);
        ImageView close = (ImageView) view.findViewById(R.id.close);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setView(view)
                .create();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
