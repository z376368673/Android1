package com.jhobor.fortune.utils;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/4/5.
 */

public class CheckUtil {

    public static int checkAll(boolean[] judges) {
        int len = judges.length;
        for (int i = 0; i < len; i++) {
            if (!judges[i]) {
                return i;
            }
        }
        return len;
    }

    public static boolean isMobile(String mobile) {
        return Pattern.compile("^0?(13[0-9]|15[012356789]|17[013678]|18[0-9]|14[57])[0-9]{8}$").matcher(mobile).matches();
    }

    public static boolean isValifyCode(String code) {
        return Pattern.compile("^\\d{4,6}$").matcher(code).matches();
    }

    public static boolean identityCard(String pass) {
        return Pattern.compile("^(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)").matcher(pass).matches();
    }
    public static boolean isPass(String pass) {
        return Pattern.compile("^\\w{6,18}$").matcher(pass).matches();
    }

    public static boolean isSame(String content1, String content2) {
        return content1.equals(content2);
    }

    public static boolean isName(String realName) {
        return Pattern.compile("^[a-zA-Z·]{2,}|[\\u4E00-\\u9FA5]{2,}$").matcher(realName).matches();
    }

    public static boolean isBankCardNo(String number) {
        return Pattern.compile("^\\d{16}|\\d{19}$").matcher(number).matches();
    }

    public static boolean isEmail(String email) {
        return Pattern.compile("^[A-Za-z\\d]+([-_.][A-Za-z\\d]+)*@[A-Za-z\\d]+([-_.]?[A-Za-z\\d]+)*[.][A-Za-z\\d]{2,5}$").matcher(email).matches();
    }
}
