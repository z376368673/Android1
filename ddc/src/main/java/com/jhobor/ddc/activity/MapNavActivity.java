package com.jhobor.ddc.activity;

import android.content.Intent;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.NaviLatLng;
import com.jhobor.ddc.R;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.BaseNaviActivity;

public class MapNavActivity extends BaseNaviActivity {

    float lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_nav);

        Intent intent = getIntent();
        lat = intent.getFloatExtra("lat", 0);
        lng = intent.getFloatExtra("lng", 0);

        //获取 AMapNaviView 实例
        mAMapNaviView = (AMapNaviView) findViewById(R.id.naviView);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);
        //设置模拟导航的行车速度
        mAMapNavi.setEmulatorNaviSpeed(75);

    }

    @Override
    public void onInitNaviSuccess() {
        super.onInitNaviSuccess();
        AMapLocation aMapLocation = (AMapLocation) BaseApplication.dataMap.get("aMapLocation");
        mAMapNavi.calculateRideRoute(new NaviLatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()), new NaviLatLng(lat, lng));
    }

    @Override
    public void onCalculateRouteSuccess() {
        super.onCalculateRouteSuccess();
        mAMapNavi.startNavi(NaviType.GPS);
    }
}
