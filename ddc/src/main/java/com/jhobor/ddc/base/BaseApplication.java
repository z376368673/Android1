package com.jhobor.ddc.base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.jhobor.ddc.greendao.DBService;
import com.jhobor.ddc.interfaces.IService;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;

/**
 * Created by Administrator on 2016/12/24.
 * 自定义application
 */

public class BaseApplication extends Application {

    public static final String BASE_URL = "http://www.ttlshop.cn/Ttl/";//www.ttlshop.cn  192.168.1.101:8080
    public static final String APP_ID = "wx7a653e636b1c3af8";
    public static IService iService;
    public static Map<String, Object> dataMap = new HashMap<>();
    public static SharedPreferences prefs;
    public static DBService dbService;

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .build();
        iService = retrofit.create(IService.class);
        dbService = DBService.getInstance(this);
        dataMap.put("reload",false);
    }
}
