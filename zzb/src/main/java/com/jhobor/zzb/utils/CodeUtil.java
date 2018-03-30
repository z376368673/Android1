package com.jhobor.zzb.utils;

import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by Administrator on 2017/7/24.
 */

public class CodeUtil {
    public static boolean isRunning = false;
    public static int seconds = 60;

    public static void getVerifyCode(final TextView tv){
        if (!isRunning){
            isRunning = true;
            Toast.makeText(tv.getContext(),"已发送验证码到你填写的手机",Toast.LENGTH_LONG).show();
            final Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    --seconds;
                    if (seconds>0){
                        tv.setText(String.format(Locale.CHINA,"%ds 后可操作",seconds));
                        handler.postDelayed(this,1000);
                    }else {
                        tv.setText("获取验证码");
                        seconds = 60;
                        isRunning = false;
                    }
                }
            });
        }else {
            tv.setText(String.format(Locale.CHINA,"%ds 后可操作",seconds));
            Toast.makeText(tv.getContext(),String.format(Locale.CHINA,"%ds 后可操作",seconds),Toast.LENGTH_SHORT).show();
        }
    }
}
