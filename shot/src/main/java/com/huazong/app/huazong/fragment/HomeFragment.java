package com.huazong.app.huazong.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.huazong.app.huazong.R;
import com.huazong.app.huazong.base.BaseApplication;
import com.huazong.app.huazong.base.RetrofitCallback;
import com.huazong.app.huazong.utils.ErrorUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private static final int ARROW = 0;
    private static final int GUN = 1;
    View view;
    ImageView useArrow, useGun, pass;
    int[] depths;
    int arrowOrGun;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrowOrGun = BaseApplication.prefs.getInt("arrowOrGun", ARROW);
        BaseApplication.dataMap.put("arrowOrGun", arrowOrGun);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        useArrow = (ImageView) view.findViewById(R.id.useArrow);
        useGun = (ImageView) view.findViewById(R.id.useGun);
        pass = (ImageView) view.findViewById(R.id.pass);
        useArrow.setOnClickListener(this);
        useGun.setOnClickListener(this);
        if (arrowOrGun == ARROW) {
            useArrow.setImageResource(R.mipmap.home_arrow_checked);
        } else {
            useGun.setImageResource(R.mipmap.home_gun_checked);
        }
        getData();

        return view;
    }

    private void getData() {
        String openid = (String) BaseApplication.dataMap.get("openid");
        BaseApplication.iService.getHomeData(openid).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    depths = new int[jsonObject.length()];
                    depths[0] = jsonObject.getInt("pass0");
                    depths[1] = jsonObject.getInt("pass1");
                    int index = arrowOrGun ==ARROW?depths[0]:depths[1];
                    if (index>=43){
                        index = 43;
                        Toast.makeText(getContext(), "恭喜通关", Toast.LENGTH_SHORT).show();
                    }else if (index<0){
                        index = 0;
                    }
                    int id = getResources().getIdentifier("pass_" + index, "mipmap", getContext().getPackageName());
                    pass.setImageResource(id);
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getContext(),e);
                }
            }
        }));
    }

    @Override
    public void onClick(View view) {
        if (view == useArrow) {
            useArrow.setImageResource(R.mipmap.home_arrow_checked);
            useGun.setImageResource(R.mipmap.home_gun_unchecked);
            if (arrowOrGun != ARROW) {
                arrowOrGun = ARROW;
                int id = getResources().getIdentifier("pass_" + depths[0], "mipmap", getContext().getPackageName());
                pass.setImageResource(id);
                BaseApplication.dataMap.put("arrowOrGun", arrowOrGun);
                BaseApplication.prefs.edit().putInt("arrowOrGun", ARROW).apply();
            }
        } else if (view == useGun) {
            useGun.setImageResource(R.mipmap.home_gun_checked);
            useArrow.setImageResource(R.mipmap.home_arrow_unchecked);
            if (arrowOrGun != GUN) {
                arrowOrGun = GUN;
                int id = getResources().getIdentifier("pass_" + depths[1], "mipmap", getContext().getPackageName());
                pass.setImageResource(id);
                BaseApplication.dataMap.put("arrowOrGun", arrowOrGun);
                BaseApplication.prefs.edit().putInt("prop", GUN).apply();
            }
        }
    }

}
