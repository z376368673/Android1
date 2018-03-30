package com.jhobor.fortune.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jhobor.fortune.R;
import com.jhobor.fortune.utils.BarUtil;
import com.jhobor.fortune.utils.HideIMEUtil;

/**
 * Author     Qijing
 * Created by YQJ on 2018/3/27.
 * Description:
 */
public class ConnectActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        HideIMEUtil.wrap(this);
        BarUtil.topBar(this, "联系我们");
        BarUtil.topBarRight(this, false, " ");
    }
}
