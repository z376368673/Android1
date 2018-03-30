package com.huazong.app.huazong.base;

import android.app.Application;
import android.content.SharedPreferences;

import com.baidu.mapapi.model.LatLng;
import com.huazong.app.huazong.R;
import com.huazong.app.huazong.interfaces.IService;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.utils.Log;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import retrofit2.Retrofit;


public class BaseApplication extends Application {
    public static final String BASE_URL = "http://www.ir-touch.com/";
    public static final String BASE_URL_HOST2 = "http://www.huazconnect.com/";
    public static final int GLIDE_PLACE_HOLDER = R.mipmap.ic_empty;
    public static final int GLIDE_ERROR = R.mipmap.ic_error;
    public static final String APP_ID = "wx10099e56ba3222b8";
    public static SharedPreferences prefs;
    public static IService iService;
    public static UMShareAPI umShareAPI;
    public static Map<String,Object> dataMap = new HashMap<>();
    public static LatLng here;

    @Override
    public void onCreate() {
        super.onCreate();

        prefs = getSharedPreferences("mActivity",MODE_PRIVATE);
        initRetrofit();

        JPushInterface.setDebugMode(false);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush

        PlatformConfig.setWeixin("wx10099e56ba3222b8","7cb34bdebecf91e2eb11c42b8ae3ff27");
        PlatformConfig.setQQZone("1105528692","33u8hE4ZQx1lFz4i");
        Log.LOG = false;
        umShareAPI = UMShareAPI.get(this);
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .build();
        iService = retrofit.create(IService.class);
    }
}
