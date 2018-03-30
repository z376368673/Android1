package com.jhobor.zzb;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jhobor.zzb.base.BaseWithHeaderActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductDetailsActivity extends BaseWithHeaderActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    ViewPager viewPager;
    TextView indicator,showMore,clicks,title;

    List<String> imgs = new ArrayList<>(5);
    List<ImageView> imageViewList = new ArrayList<>(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fillContent(R.layout.activity_product_details);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        indicator = (TextView) findViewById(R.id.indicator);
        showMore = (TextView) findViewById(R.id.showMore);
        clicks = (TextView) findViewById(R.id.clicks);
        title = (TextView) findViewById(R.id.title);

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
        clicks.setText(String.valueOf(123));
        title.setText("建材建材建材建材建材建材建材建材建材建材建材建材建材建材建材建材建材");
        showMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.i("onClick>>","ProductDetailsActivity onClick");
        if (v==showMore){
            startActivity(new Intent(this,HallActivity.class));
        }
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

    private class ProductPagerAdapter extends PagerAdapter{

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
