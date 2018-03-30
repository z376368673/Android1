package com.huazong.app.huazong.utils;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Administrator on 2016/7/26.
 */
public class DateUtil {

    public static Calendar string2Calendar(String yearMonthDay) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        String[] split = yearMonthDay.split("-");
        int year = Integer.parseInt(split[0]);
        int month = Integer.parseInt(split[1]) - 1;
        int date = Integer.parseInt(split[2]);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, date);
        return calendar;
    }


    public static String Calendar2String(Calendar calendar) {
        StringBuilder sb = new StringBuilder();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        sb.append(year);
        sb.append("-");
        if ((month + 1) < 10) {
            sb.append("0");
            sb.append(month + 1);
        } else {
            sb.append(month + 1);
        }
        sb.append("-");
        if (dayOfMonth < 10) {
            sb.append("0");
            sb.append(dayOfMonth);
        } else {
            sb.append(dayOfMonth);
        }
        return sb.toString();
    }

    public static boolean inOneDay(Calendar cal,Calendar another){
        return cal.get(Calendar.YEAR)==another.get(Calendar.YEAR)&&cal.get(Calendar.MONTH)==another.get(Calendar.MONTH)&&cal.get(Calendar.DAY_OF_MONTH)==another.get(Calendar.DAY_OF_MONTH);
    }
}
