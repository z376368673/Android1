package com.jhobor.fortune.utils;

import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by Administrator on 2017/4/1.
 */

public class CodeUtil {
    public static boolean isRun = false;
    private static int seconds = 60;

    public static void verifyCode(final TextView getCode) {
        if (!isRun) {
            isRun = true;
            final Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (seconds > 0 && isRun) {
                        getCode.setText(String.format(Locale.CHINA, "剩余 %d s", seconds));
                        --seconds;
                        handler.postDelayed(this, 1000);
                    } else {
                        seconds = 60;
                        getCode.setText("获取验证码");
                        isRun = false;
                    }
                }
            });
        } else {
            Toast.makeText(getCode.getContext(), String.format(Locale.CHINA, "操作太频繁，%d 秒后可使用", seconds), Toast.LENGTH_SHORT).show();
        }
    }
}
