package com.jhobor.zzb;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jhobor.zzb.base.BaseWithHeaderActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductEditActivity extends BaseWithHeaderActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    ViewPager viewPager;
    TextView desc,indicator;

    List<String> imgs = new ArrayList<>(5);
    List<ImageView> imageViewList = new ArrayList<>(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fillContent(R.layout.activity_product_edit);
        share.setImageResource(R.mipmap.my_brand_menu);

        viewPager = (ViewPager)findViewById(R.id.viewPager);
        desc = (TextView)findViewById(R.id.desc);
        indicator = (TextView)findViewById(R.id.indicator);

        share.setOnClickListener(this);
        initData();
    }

    private void initData() {
        imgs.add("http://pic2116.ytqmx.com:82/2017/0713/40/1.jpg");
        imgs.add("http://pic2116.ytqmx.com:82/2017/0713/40/1.jpg");
        imgs.add("http://pic2116.ytqmx.com:82/2017/0713/40/1.jpg");
        imgs.add("http://pic2116.ytqmx.com:82/2017/0713/40/1.jpg");
        imgs.add("http://pic2116.ytqmx.com:82/2017/0713/40/1.jpg");
        for (String str:imgs){
            ImageView iv = new ImageView(getBaseContext());
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(getBaseContext())
                    .load(str)
                    .into(iv);
            imageViewList.add(iv);
        }
        viewPager.setAdapter(new ProductPagerAdapter());
        indicator.setText(String.format(Locale.CHINA,"%d/%d",1,imgs.size()));
        viewPager.addOnPageChangeListener(this);
        desc.setText("建材建材建材建材建材建材建材建材建材建材建材建材建材建材建材建材建材");
    }

    @Override
    public void onClick(View v) {
        if (v==share){
            showActionPopupWin();
        }
    }
    private void showActionPopupWin() {
        View view = getLayoutInflater().inflate(R.layout.popup_win_right_top, null, false);
        final PopupWindow actionPopupWin = new PopupWindow(this);
        actionPopupWin.setContentView(view);
        actionPopupWin.setFocusable(true);
        actionPopupWin.showAsDropDown(share);
        TextView actionCollect = (TextView) view.findViewById(R.id.actionCollect);
        TextView actionShare = (TextView) view.findViewById(R.id.actionShare);
        actionCollect.setText("修改描述");
        actionShare.setText("删除");
        actionCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(">>","collect");
                actionPopupWin.dismiss();
            }
        });
        actionShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(">>","share");
                actionPopupWin.dismiss();
            }
        });
        actionPopupWin.showAsDropDown(share);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        indicator.setText(String.format(Locale.CHINA,"%d/%d",position+1,imgs.size()));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class ProductPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imgs.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imageViewList.get(position));
            return imageViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViewList.get(position));
        }
    }
}
