package com.jhobor.fortune.base;

import android.app.Application;
import android.content.SharedPreferences;

import com.jhobor.fortune.interfaces.IService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;

/**
 * Created by Administrator on 2017/4/5.
 */

public class BaseApplication extends Application {
    //public static final String BASE_URL = "http://123.1.180.243/fortuneUsb/";// http://119.23.231.252/fortune/   http://192.168.0.101:8080/fortuneUsb/
    public static final String BASE_URL = "http://192.168.1.14:8080/rentalcarUsb/";//    http://192.168.0.101:8080/fortuneUsb/
    //public static final String BASE_URL = "http://tz.1yuanpf.com/rentalcarUsb/";//    http://192.168.0.101:8080/fortuneUsb/

    public static Map<String, Object> dataMap = new HashMap<>();
    public static SharedPreferences prefs;
    public static ArrayList infoList;
    public static IService iService;
    private static BaseApplication application;
    @Override
    public void onCreate() {
        super.onCreate();
        prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        infoList = new ArrayList();
        initRetrofit();
        dataMap.put("reload", false);
        application = this;
    }
    public static BaseApplication getAppContext(){
        return   application;
    }
    private void initRetrofit() {
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .build();
        iService = retrofit.create(IService.class);
    }
}
