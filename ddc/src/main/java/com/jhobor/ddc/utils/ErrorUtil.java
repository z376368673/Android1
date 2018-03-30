package com.jhobor.ddc.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/4/5.
 */

public class ErrorUtil {
    public static void retrofitGetDataFail(Context context, Throwable t) {
        String msg = t.getMessage() == null ? "获取数据出错，错误消息为：null" : t.getMessage();
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        t.printStackTrace();
    }

    public static void retrofitResponseParseFail(Context context, Exception e) {
        String msg = e.getMessage() == null ? "数据解析失败" : e.getMessage();
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        e.printStackTrace();
    }
}
