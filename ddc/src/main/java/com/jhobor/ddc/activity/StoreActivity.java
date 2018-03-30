package com.jhobor.ddc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jhobor.ddc.R;
import com.jhobor.ddc.adapter.MyFragmentPagerAdapter;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.entity.Category;
import com.jhobor.ddc.entity.Goods;
import com.jhobor.ddc.fragment.StoreCommentFragment;
import com.jhobor.ddc.fragment.StoreGoodsFragment;
import com.jhobor.ddc.fragment.StoreInfoFragment;
import com.jhobor.ddc.utils.ErrorUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StoreActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    ImageView topArrow, collection, storePic, goodsPic, commentsPic, infoPic;
    TextView storeName, goodsText, commentsText, infoText;
    LinearLayout goods, comments, info;
    ViewPager viewPager;

    List<ImageView> tabImgList = new ArrayList<>();
    List<TextView> tabTextList = new ArrayList<>();
    List<LinearLayout> tabBoxList = new ArrayList<>();
    List<Integer> tabNormalIconList = new ArrayList<>();
    List<Integer> tabActivedIconList = new ArrayList<>();
    int curPos = 0;
    int storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        Intent intent = getIntent();
        storeId = intent.getIntExtra("storeId", 0);
        initView();
        handleEvt();
        getStoreInfoAndGoods(storeId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean reload = (boolean) BaseApplication.dataMap.get("reload");
        if (reload){
            getStoreInfoAndGoods(storeId);
        }
    }

    private void getStoreInfoAndGoods(final int storeId) {
        BaseApplication.iService.storeGoodsAndInfo(storeId).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String name = jsonObject.getString("name");
                    String picture = BaseApplication.BASE_URL + jsonObject.getString("picture");
                    int id = jsonObject.getInt("id");
                    JSONArray categoryList = jsonObject.getJSONArray("categoryList");
                    JSONArray goodsList = jsonObject.getJSONArray("goodsList");
                    int len = categoryList.length();
                    List<Category> categories = new ArrayList<Category>(len);
                    for (int i = 0; i < len; i++) {
                        JSONArray jsonArray = categoryList.getJSONArray(i);
                        int cid = jsonArray.getInt(0);
                        String cname = jsonArray.getString(1);
                        categories.add(new Category(cid, cname));
                    }
                    len = goodsList.length();
                    List<Goods> goodses = new ArrayList<Goods>(len);
                    for (int j = 0; j < len; j++) {
                        JSONArray jsonArray = goodsList.getJSONArray(j);
                        int gid = jsonArray.getInt(0);
                        String gname = jsonArray.getString(1);
                        double price = jsonArray.getDouble(2);
                        String gpicture = BaseApplication.BASE_URL + jsonArray.getString(3);
                        int stock = jsonArray.getInt(4);
                        int salesVolume = jsonArray.getInt(5);
                        goodses.add(new Goods(gid, gname, gpicture, (float) price, salesVolume, stock));
                    }
                    BaseApplication.dataMap.put("storeId", id);
                    Glide.with(getApplicationContext())
                            .load(picture)
                            .error(R.mipmap.load_img_fail)
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .into(storePic);
                    storeName.setText(name);
                    Intent intent = new Intent("showCategoryAndGoods");
                    intent.putExtra("storeId", id);
                    intent.putExtra("storeName", name);
                    intent.putParcelableArrayListExtra("categoryList", (ArrayList<? extends Parcelable>) categories);
                    intent.putParcelableArrayListExtra("goodsList", (ArrayList<? extends Parcelable>) goodses);
                    sendBroadcast(intent);
                    Intent intent2 = new Intent("showStoreComments");
                    intent2.putExtra("storeId", id);
                    sendBroadcast(intent2);

                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                }
            }
        }));
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);
        collection.setOnClickListener(this);
        goods.setOnClickListener(this);
        comments.setOnClickListener(this);
        info.setOnClickListener(this);
        viewPager.addOnPageChangeListener(this);
    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        collection = (ImageView) findViewById(R.id.collection);
        goodsPic = (ImageView) findViewById(R.id.goodsPic);
        commentsPic = (ImageView) findViewById(R.id.commentsPic);
        infoPic = (ImageView) findViewById(R.id.infoPic);
        storePic = (ImageView) findViewById(R.id.storePic);
        storeName = (TextView) findViewById(R.id.storeName);
        goodsText = (TextView) findViewById(R.id.goodsText);
        commentsText = (TextView) findViewById(R.id.commentsText);
        infoText = (TextView) findViewById(R.id.infoText);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        goods = (LinearLayout) findViewById(R.id.goods);
        comments = (LinearLayout) findViewById(R.id.comments);
        info = (LinearLayout) findViewById(R.id.info);

        addTabs();
        List<Fragment> fragmentList = getFragmentData();
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
    }

    private void addTabs() {
        tabImgList.add(goodsPic);
        tabImgList.add(commentsPic);
        tabImgList.add(infoPic);

        tabTextList.add(goodsText);
        tabTextList.add(commentsText);
        tabTextList.add(infoText);

        tabBoxList.add(goods);
        tabBoxList.add(comments);
        tabBoxList.add(info);

        tabNormalIconList.add(R.mipmap.store_goods_tab_normal);
        tabNormalIconList.add(R.mipmap.store_comments_tab_normal);
        tabNormalIconList.add(R.mipmap.store_info_tab_normal);
        tabActivedIconList.add(R.mipmap.store_goods_tab_press);
        tabActivedIconList.add(R.mipmap.store_comments_tab_press);
        tabActivedIconList.add(R.mipmap.store_info_tab_press);
    }

    private List<Fragment> getFragmentData() {
        List<Fragment> fragmentList = new ArrayList<>();
        StoreGoodsFragment storeGoodsFragment = new StoreGoodsFragment();
        StoreCommentFragment storeCommentFragment = new StoreCommentFragment();
        StoreInfoFragment storeInfoFragment = new StoreInfoFragment();
        fragmentList.add(storeGoodsFragment);
        fragmentList.add(storeCommentFragment);
        fragmentList.add(storeInfoFragment);
        return fragmentList;
    }

    @Override
    public void onClick(View v) {
        if (v == topArrow) {
            finish();
        } else if (v == collection) {

        } else if (v == goods) {
            viewPager.setCurrentItem(0);
            //changeTab(0);
        } else if (v == comments) {
            viewPager.setCurrentItem(1);
            //changeTab(1);
        } else if (v == info) {
            viewPager.setCurrentItem(2);
            //changeTab(2);
        }
    }

    private void changeTab(int newPos) {
        if (newPos != curPos) {
            int white = ContextCompat.getColor(this, R.color.white);
            tabBoxList.get(curPos).setBackgroundColor(white);
            tabBoxList.get(newPos).setBackgroundColor(ContextCompat.getColor(this, R.color.redTheme));

            tabTextList.get(curPos).setTextColor(ContextCompat.getColor(this, R.color.textGray));
            tabTextList.get(newPos).setTextColor(white);

            tabImgList.get(curPos).setImageResource(tabNormalIconList.get(curPos));
            tabImgList.get(newPos).setImageResource(tabActivedIconList.get(newPos));
            curPos = newPos;
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        changeTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
