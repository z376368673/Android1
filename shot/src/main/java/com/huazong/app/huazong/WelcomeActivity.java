package com.huazong.app.huazong;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huazong.app.huazong.base.BaseApplication;
import com.huazong.app.huazong.base.RetrofitCallback;
import com.huazong.app.huazong.utils.ErrorUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WelcomeActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    ViewPager viewPager;
    int idx = 0;
    int[] imageIds = new int[]{R.drawable.a, R.drawable.b, R.drawable.c};
    int[] randomIds = new int[]{R.drawable.show1,R.drawable.show2,R.drawable.show3};
    List<String> imageUrls = new ArrayList<>(3);
    List<ImageView> toShowList = new ArrayList<>(4);;
    boolean flag = false;
    boolean isFirstSetup = false;
    private Handler handler = new Handler();
    MyRunnable myRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);

        viewPager = (ViewPager) findViewById(R.id.vp);
        viewPager.addOnPageChangeListener(this);

        isFirstSetup = BaseApplication.prefs.getInt("setupTimes", 0)==0;
        if (isFirstSetup){
            for (int id : imageIds) {
                ImageView iv = new ImageView(getBaseContext());
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                iv.setImageResource(id);
                toShowList.add(iv);
            }
            BaseApplication.prefs.edit().putInt("setupTimes",1).apply();
            viewPager.setAdapter(new MyPagerAdapter());
            handler.post(new MyRunnable());
        }else {
            loadData();
        }
    }

    private void loadData() {
        BaseApplication.iService.getAd().enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray data = jsonObject.getJSONArray("data");
                    int length = data.length();
                    for (int i = 0; i < length+1; i++) {
                        ImageView iv = new ImageView(getBaseContext());
                        iv.setScaleType(ImageView.ScaleType.FIT_XY);
                        if (i>0){
                            JSONObject object = data.getJSONObject(i-1);
                            String url = BaseApplication.BASE_URL_HOST2 + object.getString("url");
                            imageUrls.add(url);
                        }else {
                            iv.setImageResource(randomIds[new Random().nextInt(imageIds.length)]);
                        }
                        toShowList.add(iv);
                    }
                    viewPager.setAdapter(new MyPagerAdapter());
                    myRunnable = new MyRunnable();
                    handler.post(myRunnable);
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getBaseContext(),e);
                }
            }
        }));
    }

    private class MyRunnable implements Runnable {
        @Override
        public void run() {
            if (idx < toShowList.size()) {
                if (idx == 0) {
                    ++idx;
                    handler.postDelayed(this, 2000);
                } else {
                    viewPager.setCurrentItem(idx);
                }
            } else {
                jump();
            }
        }
    }

    private void jump() {
        if (!flag) {
            flag = true;
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        idx = position + 1;
        handler.removeCallbacks(myRunnable);
        handler.postDelayed(myRunnable, 3000);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (idx == toShowList.size()&&state==1){
            jump();
        }
    }

    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return toShowList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (!isFirstSetup&&position<imageUrls.size()) {
                Glide.with(getBaseContext())
                        .load(imageUrls.get(position))
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .placeholder(imageIds[position/imageIds.length])
                        .error(BaseApplication.GLIDE_PLACE_HOLDER)
                        .into(toShowList.get(position+1));
            }
            ImageView view = toShowList.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
