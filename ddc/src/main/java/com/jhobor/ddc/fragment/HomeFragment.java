package com.jhobor.ddc.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jhobor.ddc.R;
import com.jhobor.ddc.activity.FarmingActivity;
import com.jhobor.ddc.activity.MapActivity;
import com.jhobor.ddc.activity.RushOrdersActivity;
import com.jhobor.ddc.activity.SellerActivity;
import com.jhobor.ddc.activity.ShoppingNearbyActivity;
import com.jhobor.ddc.activity.StoreActivity;
import com.jhobor.ddc.adapter.MyPagerAdapter;
import com.jhobor.ddc.adapter.StoreBaseAdapter;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.entity.Module;
import com.jhobor.ddc.entity.Store;
import com.jhobor.ddc.utils.ErrorUtil;
import com.jhobor.ddc.utils.GlideImageLoader;
import com.youth.banner.Banner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    View view;
    Banner banner;
    ViewPager viewPager;
    ListView listView;
    TextView location, shopping, hotel, entertainment, service, buyer, seller, food, takeAway, rushOrders, farming;
    ImageView logo;
    LinearLayout locateBox,recommend;
    ScrollView scrollView;

    boolean hasData = false;
    List<Store> storeList = new ArrayList<>();
    List<View> viewList = new ArrayList<>();
    List<String> images = new ArrayList<>();
    List<Module> moduleArrayList = new ArrayList<>();
    AMapLocation aMapLocation;
    StoreBaseAdapter storeBaseAdapter;
    MyPagerAdapter pagerAdapter;
    LayoutInflater inflater;

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            aMapLocation = (AMapLocation) BaseApplication.dataMap.get("aMapLocation");
            if (aMapLocation.getErrorCode() == 0) {
                if (moduleArrayList.size()==0) {
                    getHomeData();
                }
                location.setText(aMapLocation.getAddress());
            } else {
                location.setText("错误码:" + aMapLocation.getErrorCode() + "。" + aMapLocation.getErrorInfo());
            }
        }
    };

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        view = inflater.inflate(R.layout.fragment_home, container, false);
        banner = (Banner) view.findViewById(R.id.banner);
        location = (TextView) view.findViewById(R.id.location);
        shopping = (TextView) view.findViewById(R.id.shopping);
        hotel = (TextView) view.findViewById(R.id.hotel);
        entertainment = (TextView) view.findViewById(R.id.entertainment);
        service = (TextView) view.findViewById(R.id.service);
        buyer = (TextView) view.findViewById(R.id.buyer);
        seller = (TextView) view.findViewById(R.id.seller);
        food = (TextView) view.findViewById(R.id.food);
        takeAway = (TextView) view.findViewById(R.id.takeAway);
        rushOrders = (TextView) view.findViewById(R.id.rushOrders);
        farming = (TextView) view.findViewById(R.id.farming);
        locateBox = (LinearLayout) view.findViewById(R.id.locateBox);
        recommend = (LinearLayout) view.findViewById(R.id.recommend);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        listView = (ListView) view.findViewById(R.id.listView);
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        shopping.setOnClickListener(this);
        hotel.setOnClickListener(this);
        entertainment.setOnClickListener(this);
        service.setOnClickListener(this);
        buyer.setOnClickListener(this);
        seller.setOnClickListener(this);
        food.setOnClickListener(this);
        takeAway.setOnClickListener(this);
        rushOrders.setOnClickListener(this);
        farming.setOnClickListener(this);
        locateBox.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        if (aMapLocation != null) {
            location.setText(aMapLocation.getAddress());
        }
        if (hasData) {
            showView();
        }/*else {
            getHomeData();
        }*/
        getContext().registerReceiver(receiver, new IntentFilter("showLocationData"));

        return view;
    }

    private void showView() {
        /*图片轮播*/
        banner.setImageLoader(new GlideImageLoader())
                .setImages(images)
                .start();
        initModuleData();
        if (viewList.size()==0){
            recommend.setVisibility(View.GONE);
        }else {
            if (pagerAdapter == null) {
                pagerAdapter = new MyPagerAdapter(viewList);
            }
            viewPager.setAdapter(pagerAdapter);
        }
        if (storeBaseAdapter == null) {
            storeBaseAdapter = new StoreBaseAdapter(storeList, getContext());
        }
        listView.setAdapter(storeBaseAdapter);
    }

    private void initModuleData() {
        shopping.setText(moduleArrayList.get(0).getName());
        hotel.setText(moduleArrayList.get(1).getName());
        entertainment.setText(moduleArrayList.get(2).getName());
        service.setText(moduleArrayList.get(3).getName());
        food.setText(moduleArrayList.get(4).getName());
        takeAway.setText(moduleArrayList.get(5).getName());
        rushOrders.setText(moduleArrayList.get(6).getName());
        farming.setText(moduleArrayList.get(7).getName());
        buyer.setText(moduleArrayList.get(8).getName());
        seller.setText(moduleArrayList.get(9).getName());
    }

    private void getHomeData() {
        BaseApplication.iService.getHomeData(aMapLocation.getLongitude(), aMapLocation.getLatitude()).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray moduleList = jsonObject.getJSONArray("moduleList");
                    JSONArray recommStoreList = jsonObject.getJSONArray("recommStoreList");
                    JSONArray storeTypeList = jsonObject.getJSONArray("storeTypeList");
                    JSONArray pictureList = jsonObject.getJSONArray("pictureList");

                    images.clear();
                    for (int i = 0; i < pictureList.length(); i++) {
                        JSONObject object = pictureList.getJSONObject(i);
                        int id = object.getInt("id");
                        String picture = BaseApplication.BASE_URL + object.getString("picture");
                        String picOrder = object.getString("picOrder");
                        images.add(picture);
                    }

                    /*首页模块数据*/
                    moduleArrayList.clear();
                    for (int i = 0; i < moduleList.length(); i++) {
                        JSONObject jo = moduleList.getJSONObject(i);
                        int id = jo.getInt("id");
                        String name = jo.getString("name");
                        String picture = jo.getString("picture");
                        moduleArrayList.add(new Module(id, name, picture));
                    }
                    /*推荐店铺*/
                    int length = recommStoreList.length();
                    if (length==0){
                        recommend.setVisibility(View.GONE);
                    }else {
                        viewList.clear();
                        for (int i = 0; i < length; i++) {
                            JSONArray jsonArray = recommStoreList.getJSONArray(i);
                            final int id = jsonArray.getInt(0);
                            String storeName = jsonArray.getString(1);
                            String picture = BaseApplication.BASE_URL + jsonArray.getString(2);
                            View view = inflater.inflate(R.layout.item_menu, (ViewGroup) HomeFragment.this.view.findViewById(R.id.cardView));
                            ImageView imageView = (ImageView) view.findViewById(R.id.foodPic);
                            TextView textView = (TextView) view.findViewById(R.id.restaurant);
                            textView.setText(storeName);
                            Glide.with(getContext())
                                    .load(picture)
                                    .error(R.mipmap.load_img_fail)
                                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                    .bitmapTransform(new RoundedCornersTransformation(getContext(), 20, 0))
                                    .into(imageView);
                            viewList.add(view);
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(), StoreActivity.class);
                                    intent.putExtra("storeId", id);
                                    startActivity(intent);
                                }
                            });
                        }
                    }

                    /*附近店铺列表*/
                    storeList.clear();
                    for (int i = 0; i < storeTypeList.length(); i++) {
                        JSONArray jsonArray = storeTypeList.getJSONArray(i);
                        int id = jsonArray.getInt(0);
                        String storeName = jsonArray.getString(1);
                        String picture = BaseApplication.BASE_URL + jsonArray.getString(2);
                        double scores = jsonArray.getDouble(3);
                        double lat = jsonArray.getDouble(4);
                        double lng = jsonArray.getDouble(5);
                        String type = jsonArray.getString(6);
                        storeList.add(new Store(id, storeName, picture, (float) scores, type, (float) lng, (float) lat, AMapUtils.calculateLineDistance(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()), new LatLng(lat, lng))));
                    }
                    showView();
                    hasData = true;
                    banner.setFocusable(true);
                    banner.setFocusableInTouchMode(true);
                    banner.requestFocus();
                    banner.requestFocusFromTouch();

                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getContext(), e);
                }
            }
        }));
    }

    @Override
    public void onClick(View v) {
        if (moduleArrayList.size() == 0)
            return;
        if (v == shopping) {
            Intent intent = new Intent(getContext(), ShoppingNearbyActivity.class);
            intent.putExtra("thisTitle", moduleArrayList.get(0).getName());
            intent.putExtra("moduleId", moduleArrayList.get(0).getId());
            startActivity(intent);
        } else if (v == hotel) {
            Intent intent = new Intent(getContext(), ShoppingNearbyActivity.class);
            intent.putExtra("thisTitle", moduleArrayList.get(1).getName());
            intent.putExtra("moduleId", moduleArrayList.get(1).getId());
            startActivity(intent);
        } else if (v == entertainment) {
            Intent intent = new Intent(getContext(), ShoppingNearbyActivity.class);
            intent.putExtra("thisTitle", moduleArrayList.get(2).getName());
            intent.putExtra("moduleId", moduleArrayList.get(2).getId());
            startActivity(intent);
        } else if (v == service) {
            Intent intent = new Intent(getContext(), ShoppingNearbyActivity.class);
            intent.putExtra("thisTitle", moduleArrayList.get(3).getName());
            intent.putExtra("moduleId", moduleArrayList.get(3).getId());
            startActivity(intent);
        } else if (v == buyer) {

        } else if (v == seller) {
            Object hasStore = BaseApplication.dataMap.get("hasStore");
            if (hasStore == null) {
                Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
            } else {
                if ((boolean) hasStore) {
                    Intent intent = new Intent(getContext(), SellerActivity.class);
                    intent.putExtra("thisTitle", moduleArrayList.get(9).getName());
                    intent.putExtra("moduleId", moduleArrayList.get(9).getId());
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "你还不是卖家，请联系客服预约开店", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (v == food) {
            Intent intent = new Intent(getContext(), ShoppingNearbyActivity.class);
            intent.putExtra("thisTitle", moduleArrayList.get(4).getName());
            intent.putExtra("moduleId", moduleArrayList.get(4).getId());
            startActivity(intent);
        } else if (v == takeAway) {
            Intent intent = new Intent(getContext(), ShoppingNearbyActivity.class);
            intent.putExtra("thisTitle", moduleArrayList.get(5).getName());
            intent.putExtra("moduleId", moduleArrayList.get(5).getId());
            startActivity(intent);
        } else if (v == rushOrders) {
            Intent intent = new Intent(getContext(), RushOrdersActivity.class);
            intent.putExtra("thisTitle", moduleArrayList.get(6).getName());
            intent.putExtra("moduleId", moduleArrayList.get(6).getId());
            startActivity(intent);
        } else if (v == farming) {
            Intent intent = new Intent(getContext(), FarmingActivity.class);
            intent.putExtra("thisTitle", moduleArrayList.get(7).getName());
            intent.putExtra("moduleId", moduleArrayList.get(7).getId());
            startActivity(intent);
        } else if (v == locateBox) {
            Intent intent = new Intent(getContext(), MapActivity.class);

            startActivity(intent);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), StoreActivity.class);
        intent.putExtra("storeId", storeList.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        Object location = BaseApplication.dataMap.get("aMapLocation");
        if (aMapLocation == null && location != null){
            getContext().sendBroadcast(new Intent("showLocationData"));
        }
    }

    @Override
    public void onDestroy() {
        getContext().unregisterReceiver(receiver);
        super.onDestroy();
    }
}
