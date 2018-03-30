package com.huazong.app.huazong.base;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.huazong.app.huazong.R;

/**
 * Created by Administrator on 2017/3/16.
 */

public class BarUtil {
    public static void topBar(final Activity activity,String title){
        TextView topTitle = (TextView) activity.findViewById(R.id.topTitle);
        topTitle.setText(title);
        activity.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }
}
