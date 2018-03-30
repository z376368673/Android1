package com.jhobor.ddc.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.ddc.R;
import com.jhobor.ddc.adapter.BuyGoodsBaseAdapter;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.entity.Addr;
import com.jhobor.ddc.entity.Orders;
import com.jhobor.ddc.entity.ShopCar;
import com.jhobor.ddc.entity.Ticket;
import com.jhobor.ddc.greendao.ShopCarDao;
import com.jhobor.ddc.utils.ErrorUtil;
import com.jhobor.ddc.utils.IPUtil;
import com.jhobor.ddc.views.InlineListView;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PayActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    ImageView topArrow, checkAlipay, checkWechatPay;
    TextView topTitle, location, totalAmount, expressFee, privilege, money, pay, ticketTV;
    FrameLayout locateBox, ticketBox, expressBox, alipayBox, wechatPayBox;
    InlineListView listView;
    Addr addr;
    List<Orders> ordersList = new ArrayList<>();
    BuyGoodsBaseAdapter adapter;
    boolean reloadAddr = false;
    boolean resetTicket = false;
    float total = 0;
    int storeId;
    int discount = 0;
    int checkPos = 0;
    List<ImageView> checkIvs = new ArrayList<>(3);
    IWXAPI iwxapi;
    long lastMillis = 0;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        Intent intent = getIntent();
        storeId = intent.getIntExtra("storeId", 0);
        registerReceiver(receiver,new IntentFilter("finishPayActivity"));
        iwxapi = WXAPIFactory.createWXAPI(this, BaseApplication.APP_ID, false);
        iwxapi.registerApp(BaseApplication.APP_ID);
        initView();
        handleEvt();
        getDefaultAddr();
        if (storeId>0) {
            getShopCarGoods();
        }else {
            Orders orders = intent.getParcelableExtra("orders");
            orders.setChecked(true);
            ordersList.add(orders);
            total += orders.getMoney();
            adapter = new BuyGoodsBaseAdapter(ordersList, this);
            listView.setAdapter(adapter);
            totalAmount.setText(String.format(Locale.CHINA, "￥%.2f", total));
            money.setText(String.format(Locale.CHINA, "￥%.2f", total));
        }
    }

    private void getDefaultAddr() {
        String uuid = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.getDefaultAddr(uuid).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int isLogin = jsonObject.getInt("isLogin");
                    if (isLogin == 1) {
                        int id = jsonObject.getInt("id");
                        String userName = jsonObject.getString("name");
                        String phone = jsonObject.getString("mobile");
                        String address = jsonObject.getString("address");
                        addr = new Addr(id, userName, phone, address, 1);
                        Log.i(">>", addr.toString());
                        location.setText(String.format(Locale.CHINA, "%s  %s\n%s", userName, phone, address));
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                }
            }
        }));
    }

    private void getShopCarGoods() {
        List<ShopCar> shopCarList = BaseApplication.dbService.getShopCarDao().queryBuilder().where(ShopCarDao.Properties.StoreId.eq(storeId)).list();
        for (ShopCar shopCar : shopCarList) {
            Orders orders = new Orders(shopCar.getGoodsId(), shopCar.getStoreName(), shopCar.getGoodsName(), shopCar.getGoodsPic(), shopCar.getTime(), shopCar.getCount() * shopCar.getGoodsPrice(), shopCar.getCount(), shopCar.getGoodsPrice());
            orders.setGoodsId(shopCar.getGoodsId());
            orders.setChecked(true);
            total += orders.getMoney();
            ordersList.add(orders);
        }
        adapter = new BuyGoodsBaseAdapter(ordersList, this);
        listView.setAdapter(adapter);
        totalAmount.setText(String.format(Locale.CHINA, "￥%.2f", total));
        money.setText(String.format(Locale.CHINA, "￥%.2f", total));
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);
        locateBox.setOnClickListener(this);
        ticketBox.setOnClickListener(this);
        alipayBox.setOnClickListener(this);
        wechatPayBox.setOnClickListener(this);
        pay.setOnClickListener(this);
        listView.setOnItemClickListener(this);
    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        topTitle = (TextView) findViewById(R.id.topTitle);
        totalAmount = (TextView) findViewById(R.id.totalAmount);
        expressFee = (TextView) findViewById(R.id.expressFee);
        privilege = (TextView) findViewById(R.id.privilege);
        money = (TextView) findViewById(R.id.money);
        pay = (TextView) findViewById(R.id.pay);
        locateBox = (FrameLayout) findViewById(R.id.locateBox);
        location = (TextView) findViewById(R.id.location);
        ticketBox = (FrameLayout) findViewById(R.id.ticketBox);
        expressBox = (FrameLayout) findViewById(R.id.expressBox);
        alipayBox = (FrameLayout) findViewById(R.id.alipayBox);
        wechatPayBox = (FrameLayout) findViewById(R.id.wechatPayBox);
        listView = (InlineListView) findViewById(R.id.listView);
        ticketTV = (TextView) findViewById(R.id.ticket);
        checkAlipay = (ImageView) findViewById(R.id.checkAlipay);
        checkWechatPay = (ImageView) findViewById(R.id.checkWechatPay);

        topTitle.setText("确认支付");
        expressBox.setVisibility(View.GONE);
        checkIvs.add(checkWechatPay);
        checkIvs.add(checkAlipay);
    }

    @Override
    public void onClick(View v) {
        if (v == topArrow) {
            finish();
        } else if (v == locateBox) {
            Intent intent = new Intent(this, ShippingAddrActivity.class);
            startActivity(intent);
            reloadAddr = true;
        } else if (v == ticketBox) {
            if (total == 0) {
                Toast.makeText(this, "支付金额不能少于0元", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, TicketsActivity.class);
                intent.putExtra("total", total);
                intent.putExtra("storeId", storeId);
                startActivity(intent);
                resetTicket = true;
            }
        } else if (v == pay) {
            if (total == 0) {
                Toast.makeText(this, "支付金额不能少于0元", Toast.LENGTH_SHORT).show();
            } else if (addr == null) {
                Toast.makeText(this, "请先设置收货地址", Toast.LENGTH_SHORT).show();
            } else {
                if (System.currentTimeMillis()-lastMillis<6*1000) {
                    Toast.makeText(getBaseContext(),"操作太频繁，请稍候再试",Toast.LENGTH_SHORT).show();
                    return;
                }
                lastMillis = System.currentTimeMillis();
                BaseApplication.dataMap.put("ordersList",ordersList);
                BaseApplication.dataMap.put("reload",true);
                if (checkPos == 0) {
                    payWithWechat();
                } else {
                    payWithAli();
                }
            }
        } else if (v == wechatPayBox) {
            changeCheck(0);
        } else if (v == alipayBox) {
            changeCheck(1);
        }
    }

    private void payWithAli() {
        Toast.makeText(getBaseContext(),"暂不支持支付宝支付",Toast.LENGTH_SHORT).show();
    }

    private void payWithWechat() {
        if(iwxapi.getWXAppSupportAPI() < Build.PAY_SUPPORTED_SDK_INT){
            Toast.makeText(getBaseContext(),"你的微信版本过低，请先升级微信",Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject();
            String uuid = (String) BaseApplication.dataMap.get("token");
            jsonObject.put("uuid", uuid);
            Log.i(">>", addr.toString());
            jsonObject.put("addrId", addr.getId());
            jsonObject.put("ip", IPUtil.getIpAddress(this));
            JSONArray jsonArray = new JSONArray();
            for (Orders orders : ordersList) {
                if (orders.isChecked()) {
                    JSONObject jo = new JSONObject();
                    jo.put("goodsId", orders.getGoodsId());
                    jo.put("count", orders.getCount());
                    jsonArray.put(jo);
                }
            }
            jsonObject.put("goodsList", jsonArray);
            String json = jsonObject.toString();
            Log.i("json>>", json);
            BaseApplication.iService.startBuy(json).enqueue(new RetrofitCallback(this, new RetrofitCallback.DataParser() {
                @Override
                public void parse(String data) {
                    try {
                        JSONObject root = new JSONObject(data);
                        String packageValue = root.getString("packageValue");
                        String ordersNo = root.getString("ordersNo");
                        String appid = root.getString("appid");
                        String sign = root.getString("sign");
                        String partnerid = root.getString("partnerid");
                        String prepayid = root.getString("prepayid");
                        String noncestr = root.getString("noncestr");
                        String timestamp = root.getString("timestamp");
                        PayReq payReq = new PayReq();
                        payReq.appId = appid;
                        payReq.partnerId = partnerid;
                        payReq.prepayId= prepayid;
                        payReq.packageValue = packageValue;
                        payReq.nonceStr= noncestr;
                        payReq.timeStamp= timestamp;
                        payReq.sign= sign;
                        payReq.extData = ordersNo;
                        iwxapi.sendReq(payReq);
                    } catch (JSONException e) {
                        ErrorUtil.retrofitResponseParseFail(PayActivity.this, e);
                    }
                }
            }));
        } catch (JSONException e) {
            ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
        }
    }

    private void changeCheck(int pos) {
        if (pos != checkPos) {
            checkIvs.get(checkPos).setVisibility(View.INVISIBLE);
            checkIvs.get(pos).setVisibility(View.VISIBLE);
            checkPos = pos;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Orders orders = ordersList.get(position);
        boolean checked = orders.isChecked();
        orders.setChecked(!checked);
        adapter.notifyDataSetChanged();
        if (checked) {
            total -= orders.getMoney();
        } else {
            total += orders.getMoney();
        }
        totalAmount.setText(String.format(Locale.CHINA, "￥%.2f", total));
        money.setText(String.format(Locale.CHINA, "￥%.2f", total - discount));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (reloadAddr) {
            getDefaultAddr();
            reloadAddr = false;
        }
        if (resetTicket) {
            Ticket ticket = (Ticket) BaseApplication.dataMap.get("ticket");
            if (ticket==null) {
                resetTicket = false;
                return;
            }
            discount = (int) ticket.getMoney();
            ticketTV.setText(String.format(Locale.CHINA, "满 %d 优惠 %d", (int) ticket.getRestrictMoney(), discount));
            privilege.setText(String.format(Locale.CHINA, "￥%d", discount));
            money.setText(String.format(Locale.CHINA, "￥%.2f", total - discount));
            BaseApplication.dataMap.remove("ticket");
            resetTicket = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseApplication.dataMap.remove("ordersList");
        unregisterReceiver(receiver);
    }
}
