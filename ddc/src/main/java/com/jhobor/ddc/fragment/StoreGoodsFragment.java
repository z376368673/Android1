package com.jhobor.ddc.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.ddc.R;
import com.jhobor.ddc.activity.PayActivity;
import com.jhobor.ddc.adapter.StoreCategoryBaseAdapter;
import com.jhobor.ddc.adapter.StoreGoodsBaseAdapter;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.entity.Category;
import com.jhobor.ddc.entity.Goods;
import com.jhobor.ddc.entity.Pager;
import com.jhobor.ddc.entity.ShopCar;
import com.jhobor.ddc.greendao.ShopCarDao;
import com.jhobor.ddc.utils.ErrorUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * 待解决 购物车产品数量
 */
public class StoreGoodsFragment extends Fragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener, View.OnClickListener {
    View view;
    ListView categoryListView, goodsListView;
    ImageView shoppingCar;
    TextView count, money, pay;

    int storeId = 0;
    int curPos = 0;
    String storeName;
    List<Category> categoryList;
    SparseArray<List<Goods>> goodsSparseArray = new SparseArray<>();
    List<Pager> pagerList;
    StoreGoodsBaseAdapter goodsBaseAdapter;
    StoreCategoryBaseAdapter categoryBaseAdapter;

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            categoryList = intent.getParcelableArrayListExtra("categoryList");
            List<Goods> goodsList = intent.getParcelableArrayListExtra("goodsList");
            storeId = intent.getIntExtra("storeId", 0);
            storeName = intent.getStringExtra("storeName");

            ShopCarDao shopCarDao = BaseApplication.dbService.getShopCarDao();
            List<ShopCar> shopCarList = shopCarDao.queryBuilder().where(ShopCarDao.Properties.StoreId.eq(storeId)).list();
            int sum = 0;
            float amount = 0f;
            for (ShopCar shopCar : shopCarList) {
                sum += shopCar.getCount();
                amount += shopCar.getCount() * shopCar.getGoodsPrice();
                for (Goods goods : goodsList) {
                    if (goods.getId() == shopCar.getGoodsId()) {
                        goods.setCount(shopCar.getCount());
                        break;
                    }
                }
            }
            BaseApplication.dataMap.put("sum", sum);
            BaseApplication.dataMap.put("amount", amount);

            categoryBaseAdapter = new StoreCategoryBaseAdapter(categoryList, getContext());
            goodsBaseAdapter = new StoreGoodsBaseAdapter(goodsList, getActivity(), shoppingCar, count, money, storeId, storeName);
            categoryBaseAdapter.setPos(curPos);
            categoryListView.setAdapter(categoryBaseAdapter);
            goodsListView.setAdapter(goodsBaseAdapter);
            updateViewDatas();
            if (categoryList.size() > 0) {
                goodsSparseArray.append(categoryList.get(0).getId(), goodsList);
                pagerList = new ArrayList<>(categoryList.size());
                int len = categoryList.size();
                for (int i = 0; i < len; i++) {
                    pagerList.add(new Pager(10));
                }
            }
        }
    };

    public StoreGoodsFragment() {
        // Required empty public constructor
    }

    private void updateViewDatas() {
        int sum = (int) BaseApplication.dataMap.get("sum");
        float amount = (float) BaseApplication.dataMap.get("amount");
        count.setText(String.valueOf(sum));
        money.setText(String.format(Locale.CHINA, "%.2f元", amount));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getContext().registerReceiver(receiver, new IntentFilter("showCategoryAndGoods"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initView(inflater, container);
        handleEvt();
        return view;
    }

    private void handleEvt() {
        categoryListView.setOnItemClickListener(this);
        categoryListView.setOnScrollListener(this);
        pay.setOnClickListener(this);
    }

    private void initView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_store_goods, container, false);
        categoryListView = (ListView) view.findViewById(R.id.categoryListView);
        goodsListView = (ListView) view.findViewById(R.id.goodsListView);
        shoppingCar = (ImageView) view.findViewById(R.id.shoppingCar);
        count = (TextView) view.findViewById(R.id.count);
        money = (TextView) view.findViewById(R.id.money);
        pay = (TextView) view.findViewById(R.id.pay);

        if (categoryBaseAdapter != null) {
            categoryListView.setAdapter(categoryBaseAdapter);
        }
        if (goodsBaseAdapter != null) {
            Log.i(">>", "setAdapter");
            goodsBaseAdapter.setShopCar(shoppingCar);
            goodsBaseAdapter.setCountTextView(count);
            goodsListView.setAdapter(goodsBaseAdapter);
            updateViewDatas();
        }
    }

    private void getGoodsData() {
        BaseApplication.iService.storeCategoryGoods(storeId, categoryList.get(curPos).getId(), pagerList.get(curPos).getPage()).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray goodsList = jsonObject.getJSONArray("goodsList");
                    int len = goodsList.length();
                    List<Goods> goodses = new ArrayList<Goods>(len);
                    ShopCarDao shopCarDao = BaseApplication.dbService.getShopCarDao();
                    List<ShopCar> shopCarList = shopCarDao.loadAll();

                    for (int j = 0; j < len; j++) {
                        JSONArray jsonArray = goodsList.getJSONArray(j);
                        int gid = jsonArray.getInt(0);
                        String gname = jsonArray.getString(1);
                        double price = jsonArray.getDouble(2);
                        String gpicture = BaseApplication.BASE_URL + jsonArray.getString(3);
                        int stock = jsonArray.getInt(4);
                        int salesVolume = jsonArray.getInt(5);
                        Goods goods = new Goods(gid, gname, gpicture, (float) price, salesVolume, stock);
                        for (ShopCar shopCar : shopCarList) {
                            if (gid == shopCar.getGoodsId()) {
                                goods.setCount(shopCar.getCount());
                                break;
                            }
                        }
                        goodses.add(goods);
                    }
                    if (len < pagerList.get(curPos).getPageLen()) {
                        pagerList.get(curPos).setNoMoreData(true);
                    }
                    goodsSparseArray.append(categoryList.get(curPos).getId(), goodses);
                    goodsBaseAdapter.setGoodsList(goodsSparseArray.get(categoryList.get(curPos).getId()));
                    goodsBaseAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getContext(), e);
                }
            }
        }));
    }

    @Override
    public void onDestroy() {
        getContext().unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (curPos == position)
            return;
        curPos = position;
        categoryBaseAdapter.setPos(curPos);
        categoryBaseAdapter.notifyDataSetChanged();
        List<Goods> goodses = goodsSparseArray.get(categoryList.get(curPos).getId(), null);
        if (goodses == null) {
            getGoodsData();
        } else {
            goodsBaseAdapter.setGoodsList(goodses);
            goodsBaseAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        int position = goodsListView.getLastVisiblePosition();
        if (position == goodsSparseArray.get(categoryList.get(curPos).getId()).size() - 1 && !pagerList.get(curPos).isNoMoreData()) {
            pagerList.get(curPos).setPage(pagerList.get(curPos).getPage() + 1);
            getGoodsData();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onClick(View v) {
        if (v == pay) {
            int sum = (int) BaseApplication.dataMap.get("sum");
            if (sum > 0) {
                Intent intent = new Intent(getContext(), PayActivity.class);
                intent.putExtra("storeId", storeId);
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "购物车是空的", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
