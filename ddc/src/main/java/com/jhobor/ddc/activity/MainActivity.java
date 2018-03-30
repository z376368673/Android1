package com.jhobor.ddc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.jhobor.ddc.R;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.BaseLocateActivity;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.entity.ShopCar;
import com.jhobor.ddc.entity.UserInfo;
import com.jhobor.ddc.fragment.HomeFragment;
import com.jhobor.ddc.fragment.MeFragment;
import com.jhobor.ddc.fragment.MsgFragment;
import com.jhobor.ddc.fragment.OrdersFragment;
import com.jhobor.ddc.greendao.ShopCarDao;
import com.jhobor.ddc.utils.ErrorUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseLocateActivity implements BottomNavigationBar.OnTabSelectedListener {
    BottomNavigationBar bottomNavigationBar;

    List<Fragment> fragmentList;
    UserInfo userInfo;
    long millis = 0;
    int lastIndex = 0;
    int curIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        handleEvt();
        initUserInfo();
        asynShopCarData();
    }

    private void asynShopCarData() {
        final ShopCarDao shopCarDao = BaseApplication.dbService.getShopCarDao();
        final List<ShopCar> shopCarList = shopCarDao.loadAll();
        if (shopCarList != null) {
            int size = shopCarList.size();

            if (size == 0)
                return;
            int[] goodsIds = new int[size];
            for (int i = 0; i < size; i++) {
                goodsIds[i] = shopCarList.get(i).getGoodsId();
            }

            BaseApplication.iService.batchGoodsInfo(goodsIds).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                @Override
                public void parse(String data) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        JSONArray goodsList = jsonObject.getJSONArray("goodsList");
                        int length = goodsList.length();
                        for (int i = 0; i < length; i++) {
                            JSONArray jsonArray = goodsList.getJSONArray(i);
                            int gid = jsonArray.getInt(0);
                            double price = jsonArray.getDouble(1);
                            String gname = jsonArray.getString(2);
                            String picture = BaseApplication.BASE_URL + jsonArray.getString(3);
                            String sname = jsonArray.getString(4);
                            int len = shopCarList.size();
                            for (int j = 0; j < len; j++) {
                                ShopCar shopCar = shopCarList.get(j);
                                if (shopCar.getGoodsId() == gid) {
                                    shopCar.setGoodsName(gname);
                                    shopCar.setGoodsPrice((float) price);
                                    shopCar.setGoodsPic(picture);
                                    shopCar.setStoreName(sname);
                                    shopCarDao.update(shopCar);
                                    shopCarList.remove(j);
                                    break;
                                }
                            }
                        }
                        for (ShopCar shopCar : shopCarList) {
                            //ShopCar sc = shopCarDao.queryBuilder().where(ShopCarDao.Properties.GoodsId.eq(shopCar.getId())).unique();
                            shopCarDao.delete(shopCar);
                        }
                    } catch (JSONException e) {
                        ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                    }
                }
            }));
        }
    }

    private void initUserInfo() {
        userInfo = (UserInfo) BaseApplication.dataMap.get("userInfo");
        if (userInfo == null) {
            String uuid = BaseApplication.prefs.getString("token", "");
            Log.i("uuid ->>", uuid);
            if (!uuid.isEmpty()) {
                BaseApplication.dataMap.put("token", uuid);
                BaseApplication.iService.mine(uuid).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                    @Override
                    public void parse(String data) {
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            int isLogin = jsonObject.getInt("isLogin");
                            if (isLogin == 1) {
                                JSONObject obj = jsonObject.getJSONObject("userInfo");
                                int id = obj.getInt("id");
                                String name = obj.getString("name");
                                String gravatar = BaseApplication.BASE_URL + obj.getString("gravatar");
                                String mobile = obj.getString("mobile");
                                double balance = obj.getDouble("balance");
                                int status = obj.getInt("status");
                                int optionCity = obj.getInt("optionCity");
                                int isStore = jsonObject.getInt("isStore");
                                int isSend = jsonObject.getInt("isSend");
                                UserInfo userInfo = new UserInfo(id, name, mobile, gravatar, (float) balance);
                                BaseApplication.dataMap.put("userInfo", userInfo);
                                BaseApplication.dataMap.put("hasStore", isStore == 1);// 是否有店铺
                                BaseApplication.dataMap.put("hasAuth", isSend == 1); // 是否实名认证（认证后可以抢单和派送）
                            }
                        } catch (JSONException e) {
                            ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                        }
                    }
                }));
            }
        }
    }

    private void toLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("backCls", this.getClass().getName());
        startActivityForResult(intent, 101);
    }

    private void handleEvt() {
        bottomNavigationBar.setTabSelectedListener(this);

    }

    private void initView() {
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottomNavigationBar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.setActiveColor(R.color.redTheme);
        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.nav_home_normal, "首页"))
                .addItem(new BottomNavigationItem(R.mipmap.nav_order_normal, "订单"))
                .addItem(new BottomNavigationItem(R.mipmap.nav_msg_normal, "消息"))
                .addItem(new BottomNavigationItem(R.mipmap.nav_me_normal, "我"))
                .setFirstSelectedPosition(0)
                .initialise();

        fragmentList = getFragments();
        showFragment(0);
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>(4);
        HomeFragment homeFragment = new HomeFragment();
        OrdersFragment ordersFragment = new OrdersFragment();
        MsgFragment msgFragment = new MsgFragment();
        MeFragment meFragment = new MeFragment();
        fragments.add(homeFragment);
        fragments.add(ordersFragment);
        fragments.add(msgFragment);
        fragments.add(meFragment);
        return fragments;
    }

    private void showFragment(int pos) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content, fragmentList.get(pos));
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onTabSelected(int position) {
        if (fragmentList != null && position < fragmentList.size()) {
            curIndex = position;
            if (position > 0 && userInfo == null && BaseApplication.dataMap.get("userInfo") == null) {
                toLogin();
            } else {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fragmentList.get(position);

                if (fragment.isAdded()) {
                    ft.replace(R.id.content, fragment);
                } else {
                    ft.add(R.id.content, fragment);
                }
                ft.commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onTabUnselected(int position) {
        if (fragmentList != null && position < fragmentList.size()) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment fragment = fragmentList.get(position);
            ft.remove(fragment);
            ft.commitAllowingStateLoss();
            lastIndex = position;
        }
    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public void onBackPressed() {
        if (millis == 0 || SystemClock.elapsedRealtime() - millis > 1000) {
            millis = SystemClock.elapsedRealtime();
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101 && resultCode == RESULT_OK) {
            boolean isBack = data.getBooleanExtra("isBack", true);
            if (isBack) {
                bottomNavigationBar.selectTab(lastIndex);
            } else {
                showFragment(curIndex);
            }
        }
    }

    @Override
    protected void onResume() {
        Object logout = BaseApplication.dataMap.get("logout");
        if (logout != null) {
            Log.i("onResume>>", String.valueOf(logout));
            if ((boolean) logout) {
                userInfo = null;
                /*BaseApplication.dataMap.remove("userInfo");
                BaseApplication.dataMap.remove("token");
                BaseApplication.dataMap.remove("logout");*/
                BaseApplication.dataMap.clear();
                BaseApplication.prefs.edit().remove("token").apply();
                bottomNavigationBar.selectTab(0);
            }
        }
        super.onResume();
    }
}
