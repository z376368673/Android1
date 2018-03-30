package com.jhobor.ddc.utils;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/1/4.
 * 转换和格式化类
 */

public class ConvertUtil {

    public static String formatDistance(float m) {
        String str;
        if (m > 1000) {
            DecimalFormat df = new DecimalFormat("#0.0km");
            str = df.format(m / 1000);
        } else {
            DecimalFormat df = new DecimalFormat("#0m");
            str = df.format(m);
        }
        return str;
    }
}
