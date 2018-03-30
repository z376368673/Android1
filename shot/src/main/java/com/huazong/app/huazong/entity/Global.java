package com.huazong.app.huazong.entity;

/**
 * Created by Administrator on 2016/8/1.
 */
public class Global {
    private static int userid;      // 用户ID
    private static String openid;   // 第三方ID
    private static int prop;        // 武器类别
    private static String userName;
    private static String userPicture;

    public static String getUserPicture() {
        return userPicture;
    }

    public static void setUserPicture(String userPicture) {
        Global.userPicture = userPicture;
    }

    public static int getUserid() {
        return userid;
    }

    public static void setUserid(int userid) {
        Global.userid = userid;
    }

    public static String getOpenid() {
        return openid;
    }

    public static void setOpenid(String openid) {
        Global.openid = openid;
    }

    public static int getProp() {
        return prop;
    }

    public static void setProp(int prop) {
        Global.prop = prop;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        Global.userName = userName;
    }
}
