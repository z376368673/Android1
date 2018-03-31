package com.jhobor.fortune.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.jhobor.fortune.LoginActivity;
import com.jhobor.fortune.R;
import com.jhobor.fortune.TradeRecordActivity;
import com.jhobor.fortune.WithdrawActivity;
import com.jhobor.fortune.WithdrawRecordActivity;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.utils.ErrorUtil;
import com.jhobor.fortune.utils.TextUtil;
import com.jhobor.fortune.view.ConnectActivity;
import com.jhobor.fortune.view.FankuiActivity;
import com.jhobor.fortune.view.GridManagerActivity;
import com.jhobor.fortune.view.MyAccountActivity;
import com.jhobor.fortune.view.SettingUserActivity;
import com.jhobor.fortune.view.ShareActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFragment extends Fragment implements View.OnClickListener {
    ImageView headImg, iv_isactivation;
    TextView userName, joinTime, mobile, refMobile,tv_isactivation,nu_pdb,nu_jhm;
    LinearLayout receiptWay, tradeRecord, withdrawRecord, modifyPass, logout, withdraw, about, pdb, jhm, help_record;
    View view;


    String strName, strMobile, strRegDate, strPhone,boodingCoin,activationCode;
    boolean hasData = false;

    public PersonalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_personal, container, false);
        headImg = (ImageView) view.findViewById(R.id.headImg);
        userName = (TextView) view.findViewById(R.id.userName);
        joinTime = (TextView) view.findViewById(R.id.joinTime);
        mobile = (TextView) view.findViewById(R.id.mobile);
        refMobile = (TextView) view.findViewById(R.id.refMobile);
        receiptWay = (LinearLayout) view.findViewById(R.id.receiptWay);
        tradeRecord = (LinearLayout) view.findViewById(R.id.tradeRecord);
        withdrawRecord = (LinearLayout) view.findViewById(R.id.withdrawRecord);
        modifyPass = (LinearLayout) view.findViewById(R.id.modifyPass);
        logout = (LinearLayout) view.findViewById(R.id.logout);
        withdraw = (LinearLayout) view.findViewById(R.id.withdraw);
        about = (LinearLayout) view.findViewById(R.id.about);
        pdb = (LinearLayout) view.findViewById(R.id.pdb);//排单币
        jhm = (LinearLayout) view.findViewById(R.id.jhm);//激活码
        help_record = (LinearLayout) view.findViewById(R.id.help_record);//帮助记录
        iv_isactivation = (ImageView) view.findViewById(R.id.iv_isactivation);
        tv_isactivation = (TextView) view.findViewById(R.id.tv_isactivation);
        nu_pdb = (TextView) view.findViewById(R.id.nu_pdb);
        nu_jhm = (TextView) view.findViewById(R.id.nu_jhm);

        receiptWay.setOnClickListener(this);
        tradeRecord.setOnClickListener(this);
        withdrawRecord.setOnClickListener(this);
        modifyPass.setOnClickListener(this);
        logout.setOnClickListener(this);
        withdraw.setOnClickListener(this);
        about.setOnClickListener(this);
        pdb.setOnClickListener(this);
        jhm.setOnClickListener(this);
        help_record.setOnClickListener(this);
        iv_isactivation.setOnClickListener(this);
        if (hasData) {
            setData();
        } else {
            getData();
        }

        int isActivation_nu = BaseApplication.prefs.getInt("isActivation_nu", 1);
        if (0 == isActivation_nu){
            iv_isactivation.setImageResource(R.mipmap.wjh);
            tv_isactivation.setText("(未激活)");
        }else {
            iv_isactivation.setImageResource(R.mipmap.jh);
            tv_isactivation.setVisibility(View.GONE);
        }


        return view;
    }

    private void getData() {
//        String token = (String) BaseApplication.dataMap.get("token");
//        BaseApplication.iService.mine(token).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
//            @Override
//            public void parse(String data) {
//                try {
//                    JSONObject jsonObject = new JSONObject(data);
//                    int isLogin = jsonObject.getInt("isLogin");
//                    if (isLogin == 1) {
//                        strName = jsonObject.getString("name");
//                        strMobile = jsonObject.getString("mobile");
//                        strRegDate = jsonObject.getString("regDate");
//                        boodingCoin = jsonObject.getInt("boodingCoin") + "";
//                        activationCode = jsonObject.getInt("activationCode") + "";
//                        boolean hasPhone = jsonObject.has("phone");
//                        if (hasPhone) {
//                            strPhone = jsonObject.getString("phone");
//                        }
//                        setData();
//                        hasData = true;
//                    } else {
//                        Toast.makeText(getContext(), "账户信息失效，无法获取数据", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    ErrorUtil.retrofitResponseParseFail(getContext(), e);
//                }
//            }
//        }));
    }

    private void setData() {
        //如果没有token 则取登陆界面，并且销毁此界面
        String token = BaseApplication.prefs.getString("token", "");
        if (TextUtils.isEmpty(token)){
            getActivity().finish();
        }else {
            userName.setText(strName);
            joinTime.setText("加入日期" + strRegDate);
            //mobile.setText(String.format("手机号码：%s", strMobile));
            String string = BaseApplication.prefs.getString("phone", "null");
            mobile.setText(String.format("手机号码：%s", string));
            refMobile.setText(String.format("推荐号码：%s", strPhone));
            nu_pdb.setText(boodingCoin);
            nu_jhm.setText(activationCode);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == receiptWay) {
            //startActivity(new Intent(getContext(), ReceiptWayActivity.class));
            startActivity(new Intent(getContext(), MyAccountActivity.class));
        } else if (v == tradeRecord) {
            startActivity(new Intent(getContext(), TradeRecordActivity.class));
        } else if (v == withdrawRecord) {
            startActivity(new Intent(getContext(), WithdrawRecordActivity.class));
        } else if (v == modifyPass) {
            // TODO: 2018/3/27
            //startActivity(new Intent(getContext(), ModifyPassActivity.class));
            startActivity(new Intent(getContext(), SettingUserActivity.class));
        } else if (v == logout) {
            new BottomDialog.Builder(getContext())
                    .setTitle("退出")
                    .setContent("确定要退出登录吗？")
                    .setNegativeText("取消")
                    .setNegativeTextColorResource(R.color.green)
                    .setPositiveText("退出")
                    .setPositiveBackgroundColorResource(R.color.red)
                    .onPositive(new BottomDialog.ButtonCallback() {
                        @Override
                        public void onClick(@NonNull BottomDialog bottomDialog) {
                            BaseApplication.prefs.edit().remove("token").remove("m").remove("p").remove("isActivation_nu").apply();
                            Toast.makeText(getContext(), "已退出登录", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                            startActivity(new Intent(getContext(), LoginActivity.class));
                        }
                    })
                    .show();
        } else if (v == withdraw) {
            startActivity(new Intent(getContext(), WithdrawActivity.class));
        } else if (v == about) {
            //startActivity(new Intent(getContext(), BrowserActivity.class));
            startActivity(new Intent(getContext(), ShareActivity.class));
        } else if (v == pdb) {
            //新调到报表管理页面
            startActivity(new Intent(getActivity(), GridManagerActivity.class));
            //startActivity(new Intent(getActivity(), BoodingCoinActivity.class));
        } else if (v == jhm) {
           /* Intent intent = new Intent(getActivity(), JiHuomaActivity.class);
            intent.putExtra("jhm", Integer.parseInt(activationCode));*/

            Intent intent = new Intent(getActivity(), FankuiActivity.class);
            startActivity(intent);
            // startActivity(new Intent(getActivity(), TradeRecordHelpActivity.class));
        } else if (v == help_record) {
            //startActivity(new Intent(getActivity(), TradeRecordHelpActivity.class));
            startActivity(new Intent(getActivity(), ConnectActivity.class));
        }else if (v == iv_isactivation) {
            int isActivation_nu = BaseApplication.prefs.getInt("isActivation_nu", 1);
            if (1 == isActivation_nu) {
                Toast.makeText(getContext(), "您已激活", Toast.LENGTH_SHORT).show();
            }else{
                String string = BaseApplication.prefs.getString("token", "null");
                BaseApplication.iService.account(string).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
                    @Override
                    public void parse(String data) {
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            int msg = jsonObject.getInt("msg");
                            if (1 == msg){
                                BaseApplication.prefs.edit().remove("isActivation_nu").apply();
                                BaseApplication.prefs.edit().putInt("isActivation_nu",1).apply();


                                Toast.makeText(getContext(), "激活成功", Toast.LENGTH_SHORT).show();
                                iv_isactivation.setImageResource(R.mipmap.jh);
                                tv_isactivation.setVisibility(View.GONE);
                            }else {
                                Toast.makeText(getContext(), "激活码不足", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
        setData();
    }
}
