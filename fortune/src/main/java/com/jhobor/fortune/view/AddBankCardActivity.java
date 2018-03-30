package com.jhobor.fortune.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jhobor.fortune.R;
import com.jhobor.fortune.utils.BarUtil;
import com.jhobor.fortune.utils.HideIMEUtil;

/**
 * Author     Qijing
 * Created by YQJ on 2018/3/30.
 * Description:
 */
public class AddBankCardActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bandcard);

        HideIMEUtil.wrap(this);
        BarUtil.topBar(this, "添加银行卡");
    }
}
