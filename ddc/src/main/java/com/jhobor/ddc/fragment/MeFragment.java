package com.jhobor.ddc.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jhobor.ddc.R;
import com.jhobor.ddc.activity.Apply4SenderActivity;
import com.jhobor.ddc.activity.BeAgentActivity;
import com.jhobor.ddc.activity.MyCollectionActivity;
import com.jhobor.ddc.activity.MyCommentsActivity;
import com.jhobor.ddc.activity.MyNeededActivity;
import com.jhobor.ddc.activity.MySentOrdersActivity;
import com.jhobor.ddc.activity.ProfileActivity;
import com.jhobor.ddc.activity.QRActivity;
import com.jhobor.ddc.activity.ShippingAddrActivity;
import com.jhobor.ddc.activity.SystemSettingActivity;
import com.jhobor.ddc.activity.TicketsActivity;
import com.jhobor.ddc.activity.WalletsActivity;
import com.jhobor.ddc.adapter.MeBaseAdapter;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.entity.MeItem;
import com.jhobor.ddc.entity.UserInfo;
import com.jhobor.ddc.utils.ErrorUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MeFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    View view;
    ImageView userPic, qrCode, enter;
    TextView userName, account;
    LinearLayout ticket, wallets;
    ListView listView;

    boolean plan2modify = false;
    String uuid;
    int applyStatus;
    List<MeItem> meItemList;
    MeBaseAdapter adapter;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            applyStatus = intent.getIntExtra("applyStatus", 5);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getContext().registerReceiver(receiver, new IntentFilter("changeApplySenderStatus"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initView(inflater, container);
        handleEvt();
        if (applyStatus == 0) {
            getSomeData();
        }

        return view;
    }

    private void getSomeData() {
        uuid = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.hasApply4sender(uuid).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    //4.未申请     5.未通过    6.已通过  （返回dispatchList集合）
                    applyStatus = jsonObject.getInt("msg");
                    if (applyStatus != 6) {
                        meItemList.get(3).setName("申请派单员");
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getContext(), e);
                }
            }
        }));
    }

    private void handleEvt() {
        qrCode.setOnClickListener(this);
        enter.setOnClickListener(this);
        ticket.setOnClickListener(this);
        wallets.setOnClickListener(this);
        listView.setOnItemClickListener(this);
    }

    private void initView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_me, container, false);
        userPic = (ImageView) view.findViewById(R.id.userPic);
        qrCode = (ImageView) view.findViewById(R.id.qrCode);
        enter = (ImageView) view.findViewById(R.id.enter);
        userName = (TextView) view.findViewById(R.id.userName);
        account = (TextView) view.findViewById(R.id.account);
        ticket = (LinearLayout) view.findViewById(R.id.ticket);
        wallets = (LinearLayout) view.findViewById(R.id.wallets);
        listView = (ListView) view.findViewById(R.id.listView);

        UserInfo userInfo = (UserInfo) BaseApplication.dataMap.get("userInfo");
        userName.setText(userInfo.getName());
        account.setText(userInfo.getAccount());
        Glide.with(getContext())
                .load(userInfo.getGravatar())
                .error(R.mipmap.load_img_fail)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(userPic);
        meItemList = getItemData();
        adapter = new MeBaseAdapter(meItemList, getContext());
        listView.setAdapter(adapter);
    }

    private List<MeItem> getItemData() {
        List<MeItem> meItemList = new ArrayList<>();
        meItemList.add(new MeItem(R.mipmap.me_needs, "我的需求"));
        meItemList.add(new MeItem(R.mipmap.me_comments, "我的评价"));
        meItemList.add(new MeItem(R.mipmap.me_agent, "成为代理商"));
        meItemList.add(new MeItem(R.mipmap.me_sender, "我的派送单"));
        meItemList.add(new MeItem(R.mipmap.me_collections, "我的收藏"));
        meItemList.add(new MeItem(R.mipmap.me_addr, "收货地址"));
        meItemList.add(new MeItem(R.mipmap.me_settings, "设置"));
        return meItemList;
    }

    @Override
    public void onClick(View v) {
        if (v == qrCode) {
            Intent intent = new Intent(getContext(), QRActivity.class);
            startActivity(intent);
        } else if (v == enter) {
            plan2modify = true;
            Intent intent = new Intent(getContext(), ProfileActivity.class);
            startActivity(intent);
        } else if (v == ticket) {
            Intent intent = new Intent(getContext(), TicketsActivity.class);
            startActivity(intent);
        } else if (v == wallets) {
            Intent intent = new Intent(getContext(), WalletsActivity.class);
            startActivity(intent);
        } else if (v == userPic) {

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            Intent intent = new Intent(getContext(), MyNeededActivity.class);
            startActivity(intent);
        } else if (position == 1) {
            Intent intent = new Intent(getContext(), MyCommentsActivity.class);
            startActivity(intent);
        } else if (position == 2) {
            Intent intent = new Intent(getContext(), BeAgentActivity.class);
            startActivity(intent);
        } else if (position == 3) {
            if (applyStatus == 4 || applyStatus == 5) {
                Intent intent = new Intent(getContext(), Apply4SenderActivity.class);
                startActivity(intent);
            } else if (applyStatus == 7) {
                Toast.makeText(getContext(), "正在审核中，请耐心等待...", Toast.LENGTH_SHORT).show();
            } else if (applyStatus == 6) {
                Intent intent = new Intent(getContext(), MySentOrdersActivity.class);
                startActivity(intent);
            }
        } else if (position == 4) {
            Intent intent = new Intent(getContext(), MyCollectionActivity.class);
            startActivity(intent);
        } else if (position == 5) {
            Intent intent = new Intent(getContext(), ShippingAddrActivity.class);
            startActivity(intent);
        } else if (position == 6) {
            Intent intent = new Intent(getContext(), SystemSettingActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        if (plan2modify) {
            UserInfo userInfo = (UserInfo) BaseApplication.dataMap.get("userInfo");
            Glide.with(getContext())
                    .load(userInfo.getGravatar())
                    .error(R.mipmap.load_img_fail)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(userPic);
            userName.setText(userInfo.getName());
            plan2modify = false;
        }
        super.onResume();
    }

    @Override
    public void onDestroy() {
        getContext().unregisterReceiver(receiver);
        super.onDestroy();
    }
}
