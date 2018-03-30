package com.jhobor.fortune.utils;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.jhobor.fortune.R;

/**
 * Created by Administrator on 2017/4/1.
 */

public class BarUtil {
    public static void topBar(final Activity activity, String title) {
        ((TextView) activity.findViewById(R.id.topTitle)).setText(title);
        activity.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    public static void topBarRight(final Activity activity, boolean b, String title) {
        ((TextView) activity.findViewById(R.id.topRight)).setText(title);
        if (b) {
            ((TextView) activity.findViewById(R.id.topRight)).setVisibility(View.VISIBLE);
        }
    }

    public static void rightClick(final Activity activity, boolean b, String title) {
        TextView tv = (TextView) activity.findViewById(R.id.topRight);
        ((TextView) activity.findViewById(R.id.topRight)).setText(title);
        if (b) {
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClick != null) {
                        mClick.click();
                    }
                }
            });
        }
    }

    public static void setmClick(RightClick mClick) {
        BarUtil.mClick = mClick;
    }

    public static RightClick mClick;

    public interface RightClick{
        void click();
    }
}
