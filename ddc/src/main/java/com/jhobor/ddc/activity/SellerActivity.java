package com.jhobor.ddc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jhobor.ddc.R;
import com.jhobor.ddc.adapter.MeBaseAdapter;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.entity.BankCard;
import com.jhobor.ddc.entity.MeItem;
import com.jhobor.ddc.entity.Store;
import com.jhobor.ddc.utils.ErrorUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellerActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    ImageView storePic, qrCode, enter;
    TextView storeName, reputationScore, baseScores, balance;
    ListView listView;

    boolean need2refresh = false;
    boolean withdraw = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        initView();
        handleEvt();
        Store store = (Store) BaseApplication.dataMap.get("store");
        if (store == null) {
            getStoreInfo();
        } else {
            showDataInView(store);
        }
    }

    private void getStoreInfo() {
        String uuid = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.myStore(uuid).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.body() == null) {
                        Toast.makeText(SellerActivity.this, "获取服务器数据失败", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                    String string = response.body().string();
                    Log.i(">>", string);
                    JSONObject jsonObject = new JSONObject(string);
                    int isLogin = jsonObject.getInt("isLogin");
                    if (isLogin != 1) {
                        Toast.makeText(SellerActivity.this, "需要登录", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int msg = jsonObject.getInt("msg");
                    if (msg != 1) {
                        Toast.makeText(SellerActivity.this, "店铺不存在", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int creditScore = jsonObject.getInt("creditScore");
                    double money = jsonObject.getDouble("balance");
                    double level = jsonObject.getDouble("level");
                    String name = jsonObject.getString("name");
                    String picture = BaseApplication.BASE_URL + jsonObject.getString("picture");
                    Store store = new Store(0, name, picture, (float) level, creditScore, (float) money);
                    BaseApplication.dataMap.put("store", store);

                    showDataInView(store);
                } catch (IOException | JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String msg = t.getMessage() == null ? "错误信息：null" : t.getMessage();
                Log.e("Retrofit Err>>", msg);
                Toast.makeText(SellerActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDataInView(Store store) {
        Glide.with(getApplicationContext())
                .load(store.getPicture())
                .error(R.mipmap.load_img_fail)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(storePic);
        storeName.setText(store.getName());
        balance.setText(String.format(Locale.CHINA, "余额：￥%.2f", store.getBalance()));
        reputationScore.setText(String.valueOf(store.getReputationScores()));
        baseScores.setText(String.format(Locale.CHINA, "%.1f", store.getScores()));
    }

    private void handleEvt() {
        qrCode.setOnClickListener(this);
        enter.setOnClickListener(this);
        listView.setOnItemClickListener(this);
    }

    private void initView() {
        storePic = (ImageView) findViewById(R.id.storePic);
        qrCode = (ImageView) findViewById(R.id.qrCode);
        enter = (ImageView) findViewById(R.id.enter);
        storeName = (TextView) findViewById(R.id.storeName);
        reputationScore = (TextView) findViewById(R.id.reputationScore);
        baseScores = (TextView) findViewById(R.id.baseScores);
        balance = (TextView) findViewById(R.id.balance);
        listView = (ListView) findViewById(R.id.listView);

        List<MeItem> meItemList = getItemData();
        listView.setAdapter(new MeBaseAdapter(meItemList, this));
    }

    private List<MeItem> getItemData() {
        List<MeItem> list = new ArrayList<>();
        list.add(new MeItem(R.mipmap.seller_goods_category, "产品分类"));
        list.add(new MeItem(R.mipmap.seller_publish_goods, "发布产品"));
        list.add(new MeItem(R.mipmap.seller_goods_manage, "产品管理"));
        list.add(new MeItem(R.mipmap.seller_orders_manage, "订单管理"));
        list.add(new MeItem(R.mipmap.seller_ticket, "优惠券管理"));
        list.add(new MeItem(R.mipmap.seller_balance, "提现"));
        return list;
    }

    @Override
    public void onClick(View v) {
        if (v == qrCode) {
            Intent intent = new Intent(this, StoreQrCodeActivity.class);
            startActivity(intent);
        } else if (v == enter) {
            need2refresh = true;
            Intent intent = new Intent(this, StoreInfoActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;
        switch (position) {
            case 0:
                intent = new Intent(this, GoodsCategoryActivity.class);
                startActivity(intent);
                break;
            case 1:
                intent = new Intent(this, PublishGoodsActivity.class);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(this, GoodsManageActivity.class);
                startActivity(intent);
                break;
            case 3:
                intent = new Intent(this, OrdersManageActivity.class);
                startActivity(intent);
                break;
            case 4:
                intent = new Intent(this, TicketManageActivity.class);
                startActivity(intent);
                break;
            case 5:
                if (withdraw)
                    return;
                withdraw = true;
                String uuid = (String) BaseApplication.dataMap.get("token");
                BaseApplication.iService.getStoreBalance(uuid).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                    @Override
                    public void parse(String data) {
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            int msg = jsonObject.getInt("msg");
                            if (msg == 1) {
                                String receiptAccount = jsonObject.getString("receiptAccount");
                                double balance = jsonObject.getDouble("balance");
                                String receiptBank = jsonObject.getString("receiptBank");
                                Intent intent = new Intent(SellerActivity.this, WithdrawActivity.class);
                                intent.putExtra("isSeller", true);
                                intent.putExtra("bankCard", new BankCard(0, 0, receiptBank, "", 0, receiptAccount));
                                Store store = (Store) BaseApplication.dataMap.get("store");
                                store.setBalance((float) balance);
                                startActivity(intent);
                                need2refresh = true;
                                withdraw = false;
                            }

                        } catch (JSONException e) {
                            ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                        }
                    }
                }));
                /*intent = new Intent(this,GoodsCategoryActivity.class);
                startActivity(intent);*/
                break;
        }
    }

    @Override
    protected void onResume() {
        if (need2refresh) {
            Store store = (Store) BaseApplication.dataMap.get("store");
            showDataInView(store);
            need2refresh = false;
        }
        super.onResume();
    }
}
