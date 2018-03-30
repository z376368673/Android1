package com.huazong.app.huazong;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.huazong.app.huazong.base.BaseApplication;
import com.huazong.app.huazong.entity.Global;
import com.huazong.app.huazong.entity.Navigation;
import com.huazong.app.huazong.fragment.HomeFragment;
import com.huazong.app.huazong.fragment.MeFragment;
import com.huazong.app.huazong.fragment.SellEquipmentFragment;
import com.huazong.app.huazong.fragment.StoreFragment;
import com.huazong.app.huazong.utils.DisplayUtil;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    static final int tabCount = 5;
    Context context = this;
    FragmentTabHost mTabHost;
    Navigation[] navs;
    LinearLayout[] tabView;
    //一秒内按返回键两次退出程序
    long exitTime = 0,diffTime = 0;
    static boolean canBack = false;
    LocationClient mLocationClient = null;
    BDLocationListener myListener = new MyLocationListener();
    int distanceX,distanceY;
    int[] scale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        initView();
        setAlias();
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener( myListener );
        initLocation();
        mLocationClient.start();     //开始定位
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(false);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(false);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(false);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(true);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            switch (location.getLocType()){
                case BDLocation.TypeGpsLocation:// GPS定位结果
                case BDLocation.TypeNetWorkLocation:// 网络定位结果
                case BDLocation.TypeOffLineLocation:// 离线定位结果
                    BaseApplication.here = new LatLng(location.getLatitude(),location.getLongitude());
                    break;
                case BDLocation.TypeServerError:// 服务端网络定位失败
                case BDLocation.TypeNetWorkException:// 网络不同导致定位失败，请检查网络是否通畅
                case BDLocation.TypeCriteriaException:// 无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机
                    BaseApplication.here = new LatLng(0,0);
                    Toast.makeText(context,"定位失败，您与商铺的距离将有较大误差",Toast.LENGTH_LONG).show();
                    break;
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                diffTime = SystemClock.elapsedRealtime();
                distanceX = (int) event.getRawX();
                distanceY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int left =(int)event.getRawX();
                int top =(int)event.getRawY();
                int halfWidth = v.getWidth()/2;
                int halfHeight = v.getHeight()/2;
                if(left - halfWidth<0){
                    left = 0;
                }else if (left>scale[0]-halfWidth){
                    left = scale[0]-v.getWidth();
                }else {
                    left -= halfWidth;
                }

                if(top - halfHeight < 0){
                    top = 0;
                }else if (top>scale[1]-halfHeight){
                    top = scale[1]-v.getHeight();
                }else {
                    top -= halfHeight;
                }
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
                params.leftMargin = left;
                params.topMargin = top;
                v.setLayoutParams(params);
                v.postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                diffTime = SystemClock.elapsedRealtime() - diffTime;
                distanceX = (int) event.getRawX() - distanceX;
                distanceY = (int) event.getRawY() - distanceY;
                if (diffTime > 200 || Math.sqrt(Math.pow(distanceX,2)+Math.pow(distanceY,2))>v.getWidth()/2){
                    return true;
                }
                break;
        }
        return false;
    }

    private void initView() {
        navs = new Navigation[tabCount];
        navs[0] = new Navigation(R.mipmap.nav_home_normal, R.mipmap.nav_home_selected, "首页", HomeFragment.class);
        navs[1] = new Navigation(R.mipmap.nav_bbs_normal, R.mipmap.nav_bbs_selected, "社区", SellEquipmentFragment.class);
        navs[2] = new Navigation(R.mipmap.nav_store_normal, R.mipmap.nav_store_selected, "基地", StoreFragment.class);
        navs[3] = new Navigation(R.mipmap.nav_equipment_normal, R.mipmap.nav_equipment_selected, "装备", SellEquipmentFragment.class);
        navs[4] = new Navigation(R.mipmap.nav_me_normal, R.mipmap.nav_me_selected, "我的", MeFragment.class);
        ImageView unuseOrders = (ImageView) findViewById(R.id.unuseOrders);
        unuseOrders.setOnTouchListener(this);
        unuseOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,OrdersActivity.class));
            }
        });
        scale = DisplayUtil.getScreenScale(this);
        tabView = new LinearLayout[tabCount];
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.real_content);
        mTabHost.getTabWidget().setDividerDrawable(null); // 去掉分割线
        for (int i = 0; i < tabCount; i++) {
            tabView[i] = (LinearLayout) getTabView(i);
            // Tab按钮添加文字和图片
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(navs[i].getName()).setIndicator(tabView[i]);
            // 添加Fragment
            Bundle args = new Bundle();
            if ("社区".equals(navs[i].getName())){
                args.putString("url","https://shequ.yunzhijia.com/thirdapp/forum/network/57e88a91e4b07556d59440e3");
            }else if ("装备".equals(navs[i].getName())){
                args.putString("url","https://design-pie.taobao.com");
            }
            mTabHost.addTab(tabSpec, navs[i].getCls(), args);
        }
        // 初始化 tab选中
//        mTabHost.setCurrentTabByTag(mFragmentTags[0]);
        final int colorChecked = ContextCompat.getColor(context, R.color.nav_text_checked);
        final int colorUnChecked = ContextCompat.getColor(context, R.color.nav_text_unchecked);
        ((ImageView) tabView[0].getChildAt(0)).setImageResource(navs[0].getSelectedPic());
        ((TextView) tabView[0].getChildAt(1)).setTextColor(colorChecked);
        // 设置Tab按钮的背景
        mTabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.mipmap.nav_tab_check);
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                int currentTab = mTabHost.getCurrentTab();
                canBack = currentTab == 1 || currentTab == 3;
                for (int i = 0; i < tabCount; i++) {
                    ImageView iv = (ImageView) tabView[i].getChildAt(0);
                    TextView tv = (TextView) tabView[i].getChildAt(1);
                    if (i == currentTab) {
                        iv.setImageResource(navs[i].getSelectedPic());
                        tv.setTextColor(colorChecked);
                        mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.mipmap.nav_tab_check);
                    } else {
                        iv.setImageResource(navs[i].getNormalPic());
                        tv.setTextColor(colorUnChecked);
                        mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.mipmap.nav_tab_uncheck);
                    }
                }
            }
        });
    }

    // 获得图片资源
    private View getTabView(int index) {
        View view = getLayoutInflater().inflate(R.layout.tab_content, null,false);
        ImageView tabImg = (ImageView) view.findViewById(R.id.tab_image);
        TextView tabTxt = (TextView) view.findViewById(R.id.tab_txt);
        tabImg.setImageResource(navs[index].getNormalPic());
        tabTxt.setText(navs[index].getName());
        return view;
    }

    // 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
    private void setAlias() {
        String jPushAlias = BaseApplication.prefs.getString("JPushAlias", "");
        if ("".equals(jPushAlias)||!Global.getOpenid().equals(jPushAlias)) {
            mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, Global.getOpenid()));
        }
    }

    private static final int MSG_SET_ALIAS = 1001;
    private static final String TAG = "JPushSetAlias";
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    BaseApplication.prefs.edit().putString("JPushAlias",alias).apply();
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
        }
    };

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.i(TAG, "Set alias in scrollHandler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!canBack) {
                if ((System.currentTimeMillis() - exitTime) > 1000) {
                    Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    finish();
                    System.exit(0);
                }
            }else {
                Intent intent = new Intent("goBack");
                sendBroadcast(intent);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        mLocationClient.unRegisterLocationListener(myListener);
        super.onDestroy();
    }
}
