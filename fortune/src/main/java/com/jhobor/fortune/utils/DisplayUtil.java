package com.jhobor.fortune.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Administrator on 2017/4/2.
 */

public class DisplayUtil {
    public static int[] getScreenRect(Context context) {
        int[] pxs = new int[2];
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        pxs[0] = dm.widthPixels;
        pxs[1] = dm.heightPixels;
        return pxs;
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
