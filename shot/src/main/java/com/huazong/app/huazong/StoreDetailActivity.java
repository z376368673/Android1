package com.huazong.app.huazong;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.huazong.app.huazong.adapter.StoreDetailBaseAdapter;
import com.huazong.app.huazong.adapter.TimeArrayAdapter;
import com.huazong.app.huazong.base.BarUtil;
import com.huazong.app.huazong.base.BaseActivity;
import com.huazong.app.huazong.base.BaseApplication;
import com.huazong.app.huazong.base.RetrofitCallback;
import com.huazong.app.huazong.entity.Global;
import com.huazong.app.huazong.entity.PassState;
import com.huazong.app.huazong.utils.DateUtil;
import com.huazong.app.huazong.utils.ErrorUtil;
import com.huazong.app.huazong.utils.IpUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class StoreDetailActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, View.OnTouchListener, DatePickerDialog.OnDateSetListener {
    static final int GRANT_PERMISSION = 100;
    static final int CHOOSE_PAY_WAY = 101;
    TextView selectDateView, priceTextView,moneyTextView;
    ListView timeView;
    GridView passStateView;
    LinearLayout whichPassView;
    Button submit;
    HorizontalScrollView hsvContent;
    HorizontalScrollView hsvColumnTitle;
    ImageView increase, decrease;

    TimeArrayAdapter timeArrayAdapter;
    StoreDetailBaseAdapter storeDetailBaseAdapter;
    String[] times = {"09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};
    Calendar lastCal, thisCal;
    IWXAPI iwxapi;
    long lastMillis = 0;
    boolean needToJudge = true,isPlaceListNull = true,jump = false;
    private double money,totalMoney;
    private int storeId, passNum;
    private String ordersNo,submitStoreData;
    private String storeName, selectedDate;
    private List<PassState> allPassStates;
    private List<Double> priceList, discountList;
    Handler scrollHandler = new Handler();
    private ScrollRunnable scrollViewHorizontalRunnable, gridViewVerticalRunnable;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int code = intent.getIntExtra("code", -1);
            Log.i("receiver code>>", String.valueOf(code));
            if (code == 0){
                jump = true;
                getData();
            }else {
                //totalMoney = 0;
                cancelOrder();
            }
        }
    };
    Handler msgHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Toast.makeText(getBaseContext(), (CharSequence) msg.obj,Toast.LENGTH_SHORT).show();
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_store_detail);

        iwxapi = WXAPIFactory.createWXAPI(this, BaseApplication.APP_ID);
        Intent intent = getIntent();
        storeId = intent.getIntExtra("storeId", -1);
        storeName = intent.getStringExtra("storeName");
        selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date());

        initView();
        getData();
        registerReceiver(receiver,new IntentFilter("wechatPayCallback"));
    }

    private void getData() {
        BaseApplication.iService.storeDetails(storeId,selectedDate).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                handleData(data);
                if (isPlaceListNull) {
                    initPlaceAdapter();
                    isPlaceListNull = false;
                    priceTextView.setText(String.format(Locale.CHINA,"%.1f元/小时",money));
                } else {
                    storeDetailBaseAdapter.setAllPassStates(allPassStates);
                    storeDetailBaseAdapter.notifyDataSetChanged();
                }
                totalMoney = 0;
                moneyTextView.setText("");
                if (jump){
                    Intent intent = new Intent(getBaseContext(), PaidOrdersQRActivity.class);
                    intent.putExtra("orderNo",ordersNo);
                    startActivity(intent);
                    jump = false;
                }
            }
        }));
    }

    private void handleData(String data) {
        List<PassState> passStatesList;
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject price = jsonObject.getJSONObject("price");
            money = jsonObject.getDouble("money");
            priceList = new ArrayList<>(15);
            discountList = new ArrayList<>(15);
            discountList.add(price.getDouble("nine"));
            discountList.add(price.getDouble("ten"));
            discountList.add(price.getDouble("eleven"));
            discountList.add(price.getDouble("twelve"));
            discountList.add(price.getDouble("thirteen"));
            discountList.add(price.getDouble("fourteen"));
            discountList.add(price.getDouble("fifteen"));
            discountList.add(price.getDouble("sixteen"));
            discountList.add(price.getDouble("seventeen"));
            discountList.add(price.getDouble("eighteen"));
            discountList.add(price.getDouble("nineteen"));
            discountList.add(price.getDouble("twenty"));
            discountList.add(price.getDouble("twentyOne"));
            discountList.add(price.getDouble("twentyTwo"));
            discountList.add(price.getDouble("twentyThree"));
            for (double d: discountList){
                priceList.add(money*d/10);
            }
            passNum = jsonObject.getInt("byway");
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            int count = jsonArray.length();
            passStatesList = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String time = object.getString("time");
                int pass = object.getInt("byway");
                String openid = object.getString("openid");
                if (i>0) {
                    PassState prev = passStatesList.get(passStatesList.size() - 1);
                    if (prev.getPass() != pass || !prev.getTime().equals(time)) {
                        PassState passState = new PassState(time, pass, openid);
                        passStatesList.add(passState);
                        Log.i(">>>", passState.toString());
                    }
                }else {
                    PassState passState = new PassState(time, pass, openid);
                    passStatesList.add(passState);
                    Log.i(">>>", passState.toString());
                }
            }
            int k = 0;
            int size = passStatesList.size();
            if (allPassStates == null) {
                allPassStates = new ArrayList<>(passNum * times.length);
            } else {
                allPassStates.clear();
            }
            if (size > 0) {
                for (String time : times) {
                    for (int j = 0; j < passNum; j++) {
                        PassState passState = new PassState(time, j + 1, "");
                        if (k < size && time.equals(passStatesList.get(k).getTime()) && (j + 1) == passStatesList.get(k).getPass()) {
                            passState.setState(2);
                            passState.setOpenid(passStatesList.get(k).getOpenid());
                            k++;
                        } else {
                            passState.setState(0);
                        }
                        allPassStates.add(passState);
                    }
                }
            } else {
                for (String time : times) {
                    for (int j = 0; j < passNum; j++) {
                        PassState passState = new PassState(time, j + 1, "");
                        allPassStates.add(passState);
                    }
                }
            }
            Log.i(">>>", String.valueOf(allPassStates.size()));
        } catch (JSONException e) {
            ErrorUtil.retrofitResponseParseFail(getBaseContext(),e);
        }
    }

    /*
     * 初始化视图
     */
    private void initView() {
        selectDateView = (TextView) findViewById(R.id.selectDate);
        timeView = (ListView) findViewById(R.id.timeView);
        passStateView = (GridView) findViewById(R.id.passStateView);
        whichPassView = (LinearLayout) findViewById(R.id.whichPassView);
        submit = (Button) findViewById(R.id.submit);
        hsvContent = (HorizontalScrollView) findViewById(R.id.hsvContent);
        hsvColumnTitle = (HorizontalScrollView) findViewById(R.id.hsvColumnTitle);
        increase = (ImageView) findViewById(R.id.increase);
        decrease = (ImageView) findViewById(R.id.decrease);
        priceTextView = (TextView) findViewById(R.id.price);
        moneyTextView = (TextView) findViewById(R.id.money);

        BarUtil.topBar(this,storeName);
        selectDateView.setText(selectedDate);
        selectDateView.setOnClickListener(this);
        increase.setOnClickListener(this);
        decrease.setOnClickListener(this);
        submit.setOnClickListener(this);

        passStateView.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        passStateView.setOnItemClickListener(this);

        hsvContent.setOnTouchListener(this);
        hsvColumnTitle.setOnTouchListener(this);
        passStateView.setOnTouchListener(this);
        timeView.setOnTouchListener(this);
    }

    void initPlaceAdapter() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int itemWidth = dm.widthPixels / 6;
        int totalWidth = itemWidth * passNum;
        passStateView.setColumnWidth(itemWidth);
        passStateView.setNumColumns(passNum);
        ViewGroup.LayoutParams layoutParams1 = passStateView.getLayoutParams();
        layoutParams1.width = totalWidth;
        layoutParams1.height = ViewGroup.LayoutParams.MATCH_PARENT;
        ViewGroup.LayoutParams layoutParams2 = whichPassView.getLayoutParams();
        layoutParams2.width = totalWidth;
        genTitles(itemWidth, passNum);

        timeArrayAdapter = new TimeArrayAdapter(this, R.layout.item_store_detail_time, times, discountList);
        timeView.setAdapter(timeArrayAdapter);
        storeDetailBaseAdapter = new StoreDetailBaseAdapter(this, allPassStates);
        passStateView.setAdapter(storeDetailBaseAdapter);
    }

    /*
     * 生成场地号
     */
    private void genTitles(int itemWidth, int passNum) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(itemWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        for (int i = 0; i < passNum; i++) {
            TextView tv = new TextView(this);
            tv.setLayoutParams(params);
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            tv.setTextColor(ContextCompat.getColor(this, R.color.second_text_color));
            tv.setTextSize(10);
            String title = "场地" + (i + 1);
            tv.setText(title);
            whichPassView.addView(tv);
        }
    }

    public void showMsg(String msg) {
        Message message = new Message();
        message.obj = msg;
        msgHandler.sendMessage(message);
    }

    /*
     * ListView跟随GridView滚动
     */
    public void followPosition() {
        int firstVisiblePosition = passStateView.getFirstVisiblePosition();
        // GridView需要这一步，ListView则不需要，因为GridView一行有多个孩子，ListView一行只有一个孩子
        if (firstVisiblePosition > 0) {
            firstVisiblePosition /= passNum;
        }
        // GridView的第一个item，即使是把这个item滑动到不可见状态
        View firstChild = passStateView.getChildAt(0);
        int top = (firstChild == null) ? 0 : firstChild.getTop();
        timeView.setSelectionFromTop(firstVisiblePosition, top);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSE_PAY_WAY) {
            if (resultCode == RESULT_OK) {
                int way = data.getIntExtra("way", 0);
                Log.i("way>>", String.valueOf(way));
                lastMillis = System.currentTimeMillis();
                if (way == ChoosePayWayActivity.PAY_WITH_WE_CHAT) {
                    // 微信支付
                    payWithWechat();
                } else if (way == ChoosePayWayActivity.PAY_WITH_ALIPAY) {
                    // 支付宝支付
                    payWithAli();
                } else if (way == ChoosePayWayActivity.PAY_WITH_MEMBER_WALLET) {
                    payWithWallet();
                } else {
                    showMsg("不合法的支付方式-" + resultCode);
                }
            } else {
                showMsg("取消支付");
            }
        }
    }

    private void payWithWallet() {
        BaseApplication.iService.payWithWallet(submitStoreData).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                if (data.startsWith("支付成功")){
                    ordersNo = data.substring(4);
                    showMsg("支付成功");
                    jump = true;
                    getData();
                }else {
                    showMsg(data);
                }
            }
        }));
    }

    private void payWithAli() {
        BaseApplication.iService.payWithAli(submitStoreData).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(final String data) {
                if ("sb".equals(data)){
                    showMsg("箭道购买失败，您选择的部分箭道可能已被购买，请刷新后再试");
                }else {
                    // 必须异步调用
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(StoreDetailActivity.this);
                            String result = alipay.pay(data, true);

                            Log.i(">>>", data);
                            Log.i(">>>", result);
                            boolean flag = false;

                            Pattern pattern = Pattern.compile("resultStatus=\\{(\\d+)\\}");
                            Matcher matcher = pattern.matcher(result);
                            String resultStatus = "0000";
                            if (matcher.find()) {
                                resultStatus = matcher.group(1);
                                Log.i("group>>>", resultStatus);
                                if ("9000".equals(resultStatus)) {
                                    Pattern patternResult = Pattern.compile("success=\"(\\w+)\"");
                                    Matcher matcherResult = patternResult.matcher(result);
                                    if (matcherResult.find()) {
                                        String group1 = matcherResult.group(1);
                                        Log.i("group>>>", group1);
                                        if ("true".equals(group1)) {
                                            flag = true;
                                        }
                                    }
                                }
                            }
                            Matcher matcher1 = Pattern.compile("out_trade_no=\"(\\w+)\"").matcher(data);
                            ordersNo = "";
                            if (matcher1.find()) {
                                ordersNo = matcher1.group(1);
                            }
                            Log.i("tradeNo>>>", ordersNo);
                            if (flag) {
                                showMsg("支付成功");
                                jump = true;
                            } else {
                                showMsg("支付失败 - " + resultStatus);
                                cancelOrder();
                            }
                            getData();
                        }
                    }).start();
                }
            }
        }));
    }

    @Override
    public void onClick(View v) {
        if (v == selectDateView) {
            lastCal = DateUtil.string2Calendar(selectedDate);
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    this,
                    lastCal.get(Calendar.YEAR),
                    lastCal.get(Calendar.MONTH),
                    lastCal.get(Calendar.DAY_OF_MONTH)
            );
            dpd.setMinDate(Calendar.getInstance(Locale.CHINA));
            Calendar calendar = Calendar.getInstance(Locale.CHINA);
            calendar.add(Calendar.YEAR,1);
            dpd.setMaxDate(calendar);
            dpd.setThemeDark(true);
            dpd.setAccentColor(ContextCompat.getColor(getBaseContext(),R.color.orange));
            dpd.show(getFragmentManager(),"datePicker");
        } else if (v == submit) {
            long millis = System.currentTimeMillis();
            if (millis-lastMillis<5*1000) {
                showMsg("操作太频繁，请稍候再试");
                return;
            }
            lastMillis = millis;
            String[] perms = {
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };

            if (!EasyPermissions.hasPermissions(getBaseContext(),perms)){
                EasyPermissions.requestPermissions(this,"需要获得一些权限才能继续...",GRANT_PERMISSION,perms);
                return;
            }
            List<PassState> list = new ArrayList<>();
            for (int i = 0; i < allPassStates.size(); i++) {
                PassState passState = allPassStates.get(i);
                if (passState.getState() == 1) {
                    list.add(passState);
                }
            }
            int size = list.size();
            if (size == 0) {
                showMsg("您未选择任何箭道");
                return;
            }
            JSONObject obj = new JSONObject();
            try {
                obj.put("storeId", storeId);
                obj.put("selectedDate", selectedDate);
                obj.put("number", size);
                String openid = (String) BaseApplication.dataMap.get("openid");
                obj.put("openid", openid);
                int arrowOrGun = (int) BaseApplication.dataMap.get("arrowOrGun");
                obj.put("type", arrowOrGun);
                obj.put("tag",1);
                obj.put("ip", IpUtil.getIpAddress(StoreDetailActivity.this));
                JSONArray arr = new JSONArray();
                for (int j = 0; j < size; j++) {
                    JSONObject obj2 = new JSONObject();
                    PassState passState = list.get(j);
                    obj2.put("time", passState.getTime());
                    obj2.put("pass", passState.getPass());
                    arr.put(obj2);
                }
                BigDecimal b = new BigDecimal(totalMoney);
                double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                obj.put("totalMoney", f1);
                obj.put("data", arr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            submitStoreData = obj.toString();
            Log.i(">>>", submitStoreData);
            Intent intent = new Intent(StoreDetailActivity.this, ChoosePayWayActivity.class);
            startActivityForResult(intent, CHOOSE_PAY_WAY);
        } else if (v == increase) {
            thisCal = DateUtil.string2Calendar(selectedDate);
            thisCal.add(Calendar.DATE, 1);
            selectedDate = DateUtil.Calendar2String(thisCal);
            selectDateView.setText(selectedDate);
            lastCal = thisCal;
            needToJudge = false;
            getData();
        } else if (v == decrease) {
            thisCal = DateUtil.string2Calendar(selectedDate);
            thisCal.add(Calendar.DATE, -1);
            Calendar now = Calendar.getInstance(Locale.CHINA);
            boolean inOneDay = DateUtil.inOneDay(now, thisCal);
            Log.i("compare>>>", String.valueOf(thisCal.compareTo(now)));
            if (thisCal.compareTo(now)>=0|| inOneDay) {
                needToJudge = inOneDay;
                selectedDate = DateUtil.Calendar2String(thisCal);
                selectDateView.setText(selectedDate);
                lastCal = thisCal;
                getData();
            } else {
                showMsg("只能选择今天或以后的日期");
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PassState passState = allPassStates.get(position);
        if (needToJudge) {
            Calendar now = Calendar.getInstance(Locale.CHINA);
            Calendar selectTime = Calendar.getInstance(Locale.CHINA);
            String time = passState.getTime();
            String[] split = time.split(":");
            Log.i(">>>", split[0]);
            int hour = Integer.parseInt(split[0]);
            selectTime.set(Calendar.HOUR_OF_DAY, hour);
            now.add(Calendar.MINUTE, 30);
            if (selectTime.before(now)) {
                showMsg("这个时间点已过期，请选择后面的时间点");
                return;
            }
        }

        int lineNumber = position / passNum;
        if (passState.getState() == 0) {
            for (int j = lineNumber * passNum; j < (lineNumber + 1) * passNum; j++) {
                if (allPassStates.get(j).getState() == 1 || (allPassStates.get(j).getState() == 2 && allPassStates.get(j).getOpenid().equals(Global.getOpenid()))) {
                    showMsg("同一时间点只能购买一个箭道");
                    return;
                }
            }
            passState.setState(1);
            totalMoney += priceList.get(lineNumber);
        } else if (passState.getState() == 1) {
            passState.setState(0);
            totalMoney -= priceList.get(lineNumber);
        }
        storeDetailBaseAdapter.notifyDataSetChanged();
        String format = new DecimalFormat("合计：#0.00元").format(totalMoney);
        moneyTextView.setText(format);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (MotionEvent.ACTION_MOVE == event.getAction()) {
            if (v == passStateView) {
                scrollHandler.removeCallbacks(gridViewVerticalRunnable);

                followPosition();
            } else if (v == hsvContent) {
                scrollHandler.removeCallbacks(scrollViewHorizontalRunnable);

                int scrollX = v.getScrollX();
                hsvColumnTitle.scrollTo(scrollX, 0);
            } else if (v == hsvColumnTitle) {
                return true;
            } else if (v == timeView) {
                return true;
            }
        } else if (MotionEvent.ACTION_UP == event.getAction()) {
            if (v == hsvContent) {
                scrollViewHorizontalRunnable = new ScrollRunnable(hsvContent, hsvColumnTitle, 0);
                scrollHandler.post(scrollViewHorizontalRunnable);
            } else if (v == passStateView) {
                gridViewVerticalRunnable = new ScrollRunnable(passStateView, timeView, 1);
                scrollHandler.post(gridViewVerticalRunnable);
            }

        }
        return false;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        thisCal = Calendar.getInstance(Locale.CHINA);
        thisCal.set(Calendar.YEAR, year);
        thisCal.set(Calendar.MONTH, monthOfYear);
        thisCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        Calendar now = Calendar.getInstance(Locale.CHINA);
        Log.i("compare>>>", String.valueOf(thisCal.compareTo(now)));
        // 只有选择的日期在今天或今天之后才刷新数据
        boolean inOneDay = DateUtil.inOneDay(now, thisCal);
        if (thisCal.compareTo(now)>=0||inOneDay) {
            if (!DateUtil.inOneDay(thisCal,lastCal)) {
                needToJudge = inOneDay;
                selectedDate = DateUtil.Calendar2String(thisCal);
                selectDateView.setText(selectedDate);
                lastCal = thisCal;
                getData();
            }
        }
    }

    private class ScrollRunnable implements Runnable {
        View v, v2;
        int orientation;
        int lastPos = -1;

        ScrollRunnable(View v, View v2, int orientation) {
            this.v = v;
            this.v2 = v2;
            this.orientation = orientation;
        }

        @Override
        public void run() {
            if (orientation == 0) {
                int scrollX = v.getScrollX();
                if (scrollX!=lastPos) {
                    v2.scrollTo(scrollX, 0);
                    scrollHandler.postDelayed(this, 10);
                }
            } else if (orientation == 1) {
                followPosition();
                View firstChild = passStateView.getChildAt(0);
                int top = (firstChild == null) ? 0 : firstChild.getTop();
                if (top!=lastPos){
                    scrollHandler.postDelayed(this, 10);
                    lastPos = top;
                }
            }
        }
    }

    private void payWithWechat() {
        BaseApplication.iService.payWithWechat(submitStoreData).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                if ("sb".equals(data)){
                    showMsg("箭道购买失败，您选择的部分箭道可能已被购买，请刷新后再试");
                    getData();
                }else {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        ordersNo = jsonObject.getString("ordersNo");
                        PayReq req = new PayReq();
                        req.appId			= jsonObject.getString("appid");
                        req.partnerId		= jsonObject.getString("partnerid");
                        req.prepayId		= jsonObject.getString("prepayid");
                        req.packageValue	= jsonObject.getString("packageValue");
                        req.nonceStr		= jsonObject.getString("noncestr");
                        req.timeStamp		= jsonObject.getString("timestamp");
                        req.sign			= jsonObject.getString("sign");
                        iwxapi.sendReq(req);
                        lastMillis = System.currentTimeMillis();
                    }catch(JSONException e){
                        Log.e("PAY_GET", "异常："+e.getMessage());
                        showMsg("服务器返回数据格式错误");
                        cancelOrder();
                    }
                }
            }
        }));
    }

    void cancelOrder(){
        if (ordersNo != null&&!ordersNo.isEmpty()) {
            BaseApplication.iService.cancelOrder(ordersNo).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                @Override
                public void parse(String data) {

                }
            }));
        }else {
            Log.e(">>","取消订单失败");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, new EasyPermissions.PermissionCallbacks() {
            @Override
            public void onPermissionsGranted(int requestCode, List<String> perms) {
            }

            @Override
            public void onPermissionsDenied(int requestCode, List<String> perms) {
                new AppSettingsDialog.Builder(StoreDetailActivity.this)
                        .setTitle("需要的权限")
                        .setRationale("需要【网络】【存储】权限才能继续下一步...")
                        .build()
                        .show();
            }

            @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
