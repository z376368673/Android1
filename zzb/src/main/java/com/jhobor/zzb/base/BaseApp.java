package com.jhobor.zzb.base;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/7/14.
 */

public class BaseApp extends Application {
    public static SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();

        sp = getSharedPreferences("prefs",MODE_PRIVATE);
    }
}
