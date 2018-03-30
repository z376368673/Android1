package com.jhobor.ddc.utils;

import java.security.MessageDigest;

/**
 * Created by Administrator on 2017/2/10.
 * 采用MD5加密解密
 */

public class MD5Util {
    /***
     * MD5加密 生成32位md5码
     */
    public static String encode(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuilder hexValue = new StringBuilder();
        for (byte md5Byte : md5Bytes) {
            int val = ((int) md5Byte) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();

    }

    /**
     * 加密解密算法 执行一次加密，两次解密
     */
    public static String decode(String inStr) {

        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        return new String(a);

    }

    // 测试主函数
    public static void main(String args[]) {
        String s = "jhobor-is-me.i will be a rich man!";
        System.out.println("原始：" + s);
        System.out.println("MD5后：" + encode(s));
        //System.out.println("加密的：" + decode(s));
        System.out.println("解密的：" + decode(decode(s)));

    }
}
