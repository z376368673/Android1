package com.jhobor.zzb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.jhobor.zzb.base.BaseActivity;
import com.jhobor.zzb.fragments.HomeFragment;
import com.jhobor.zzb.fragments.MineFragment;
import com.jhobor.zzb.fragments.ZzbFragment;

import java.util.ArrayList;
import java.util.List;

import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageBottomTabLayout;
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;
import me.majiajie.pagerbottomtabstrip.item.NormalItemView;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;

public class MainActivity extends BaseActivity implements OnTabItemSelectedListener {
    PageBottomTabLayout tabStrip;

    List<Fragment> fragmentList = new ArrayList<>(3);
    FragmentManager fragmentManager;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int index = intent.getIntExtra("index", 0);
            Fragment fragment = null;
            if (index==0){
                fragment  = new HomeFragment();
            }else if (index==1){
                fragment  = new ZzbFragment();
            }else if (index==2){
                fragment  = new MineFragment();
            }
            fragmentList.set(index,fragment);
            fragmentManager.beginTransaction().replace(R.id.frameLayout,fragmentList.get(index)).commitAllowingStateLoss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabStrip = (PageBottomTabLayout) findViewById(R.id.tabStrip);new NormalItemView(this);
        registerReceiver(receiver,new IntentFilter("replaceFragment"));
        NavigationController navigationController = tabStrip.custom()
                .addItem(newItem(R.mipmap.home0,R.mipmap.home1,"首页"))
                .addItem(newItem(R.mipmap.zzb0,R.mipmap.zzb1,"正正帮"))
                .addItem(newItem(R.mipmap.mine0,R.mipmap.mine1,"我的"))
                .build();
        navigationController.addTabItemSelectedListener(this);
        fragmentManager = getSupportFragmentManager();
        HomeFragment homeFragment = new HomeFragment();
        ZzbFragment zzbFragment = new ZzbFragment();
        MineFragment mineFragment = new MineFragment();
        fragmentList.add(homeFragment);
        fragmentList.add(zzbFragment);
        fragmentList.add(mineFragment);
        fragmentManager.beginTransaction().add(R.id.frameLayout,homeFragment).commitAllowingStateLoss();
    }

    //创建一个Item
    private BaseTabItem newItem(int drawable, int checkedDrawable, String text){
        NormalItemView normalItemView = new NormalItemView(this);
        normalItemView.initialize(drawable,checkedDrawable,text);
        normalItemView.setTextDefaultColor(ContextCompat.getColor(this,R.color.gray));
        normalItemView.setTextCheckedColor(ContextCompat.getColor(this,R.color.purple));
        return normalItemView;
    }

    @Override
    public void onSelected(int index, int old) {
        fragmentManager.beginTransaction().replace(R.id.frameLayout,fragmentList.get(index)).commitAllowingStateLoss();
    }

    @Override
    public void onRepeat(int index) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
