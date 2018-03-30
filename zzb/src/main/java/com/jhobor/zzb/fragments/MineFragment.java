package com.jhobor.zzb.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jhobor.zzb.BuyVipActivity;
import com.jhobor.zzb.CollectionPackageActivity;
import com.jhobor.zzb.LoginActivity;
import com.jhobor.zzb.MyBrandActivity;
import com.jhobor.zzb.R;
import com.jhobor.zzb.RecommendationActivity;
import com.jhobor.zzb.SettingActivity;
import com.jhobor.zzb.VCardActivity;
import com.jhobor.zzb.base.BaseApp;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment implements View.OnClickListener {
    ImageView avatar;
    TextView login,buyVIP,collection,brand,myCard,recommendation,declaration;
    LinearLayout setting;

    private static final int REQUEST_CODE_LOGIN = 100;
    private static final int REQUEST_CODE_LOGOUT = 101;

    public MineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        avatar = (ImageView) view.findViewById(R.id.avatar);
        collection = (TextView) view.findViewById(R.id.collection);
        brand = (TextView) view.findViewById(R.id.brand);
        myCard = (TextView) view.findViewById(R.id.myCard);
        recommendation = (TextView) view.findViewById(R.id.recommendation);
        declaration = (TextView) view.findViewById(R.id.declaration);
        setting = (LinearLayout) view.findViewById(R.id.setting);

        String token = BaseApp.sp.getString("token", "");
        if (token.isEmpty()){
            login = (TextView) view.findViewById(R.id.login);
            buyVIP = (TextView) view.findViewById(R.id.buyVIP);
            login.setOnClickListener(this);
            buyVIP.setOnClickListener(this);
        }else {
            showUserInfo();
        }
        collection.setOnClickListener(this);
        brand.setOnClickListener(this);
        myCard.setOnClickListener(this);
        recommendation.setOnClickListener(this);
        setting.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v==login){
            startActivityForResult(new Intent(getContext(), LoginActivity.class),REQUEST_CODE_LOGIN);
        }else if (v==buyVIP){
            startActivity(new Intent(getContext(), BuyVipActivity.class));
        }else if (v==collection){
            startActivity(new Intent(getContext(), CollectionPackageActivity.class));
        }else if (v==brand){
            startActivity(new Intent(getContext(), MyBrandActivity.class));
        }else if (v==myCard){
            startActivity(new Intent(getContext(), VCardActivity.class));
        }else if (v==recommendation){
            startActivity(new Intent(getContext(), RecommendationActivity.class));
        }else if (v==setting){
            startActivityForResult(new Intent(getContext(), SettingActivity.class),REQUEST_CODE_LOGOUT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE_LOGIN&&resultCode== Activity.RESULT_OK){
            showUserInfo();
        }else if (requestCode==REQUEST_CODE_LOGOUT&&resultCode== Activity.RESULT_OK){
            Intent intent = new Intent("replaceFragment");
            intent.putExtra("index",2);
            getContext().sendBroadcast(intent);
        }
    }

    private void showUserInfo() {
        UserInfoFragment userInfoFragment = new UserInfoFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.userInfoBox,userInfoFragment).commitAllowingStateLoss();
    }
}
