package com.huazong.app.huazong.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.huazong.app.huazong.fragment.OrderFragment;

import java.util.List;

/**
 * Created by Administrator on 2016/7/22.
 */
public class OrdersFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<OrderFragment> allOrderFragmentList;

    public OrdersFragmentPagerAdapter(FragmentManager fm, List<OrderFragment> allOrderFragmentList) {
        super(fm);
        this.allOrderFragmentList = allOrderFragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return allOrderFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return allOrderFragmentList.size();
    }
}
