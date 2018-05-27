package com.jhobor.fortune.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.fortune.R;
import com.jhobor.fortune.oldui.WalletsDetailsActivity;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.utils.ErrorUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class WalletsFragment extends Fragment implements View.OnClickListener {
    TextView total, selfCapital, selfBalance, selfFreeze, oneCapital, oneBalance, oneFreeze, twoCapital, twoBalance, twoFreeze;
    View view;

    float fTotal, fSelfCapital, fSelfBalance, fSelfFreeze, fOneCapital, fOneBalance, fOneFreeze, fTwoCapital, fTwoBalance, fTwoFreeze;
    boolean hasData = false;

    public WalletsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_wallets, container, false);
        total = (TextView) view.findViewById(R.id.total);
        selfCapital = (TextView) view.findViewById(R.id.selfCapital);
        selfBalance = (TextView) view.findViewById(R.id.selfBalance);
        selfFreeze = (TextView) view.findViewById(R.id.selfFreeze);
        oneCapital = (TextView) view.findViewById(R.id.oneCapital);
        oneBalance = (TextView) view.findViewById(R.id.oneBalance);
        oneFreeze = (TextView) view.findViewById(R.id.oneFreeze);
        twoCapital = (TextView) view.findViewById(R.id.twoCapital);
        twoBalance = (TextView) view.findViewById(R.id.twoBalance);
        twoFreeze = (TextView) view.findViewById(R.id.twoFreeze);
        if (!hasData) {
            getData();
        } else {
            setData();
        }
        total.setOnClickListener(this);

        return view;
    }

    private void getData() {
        String token = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.myInvest(token).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int isLogin = jsonObject.getInt("isLogin");
                    if (isLogin == 1) {
                        fTotal = (float) jsonObject.getDouble("totalMoney");
                        fSelfCapital = (float) jsonObject.getDouble("investTotalMoney");
                        fSelfBalance = (float) jsonObject.getDouble("gainMoney");
                        fSelfFreeze = (float) jsonObject.getDouble("freezeMoney");
                        fOneCapital = (float) jsonObject.getDouble("childTotalMoney");
                        fOneBalance = (float) jsonObject.getDouble("childUsableMoney");
                        fOneFreeze = (float) jsonObject.getDouble("childFreezeMoney");
                        fTwoCapital = (float) jsonObject.getDouble("granTotalMoney");
                        fTwoBalance = (float) jsonObject.getDouble("granUsableMoney");
                        fTwoFreeze = (float) jsonObject.getDouble("granFreezeMoney");
                        setData();
                        hasData = true;
                    } else {
                        Toast.makeText(getContext(), "账户信息失效，无法获取数据", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getContext(), e);
                }
            }
        }));
    }

    private void setData() {
        total.setText(String.format(Locale.CHINA, "%.2f", fTotal));
        selfCapital.setText(String.format(Locale.CHINA, "%.2f", fSelfCapital));
        selfBalance.setText(String.format(Locale.CHINA, "%.2f", fSelfBalance));
        selfFreeze.setText(String.format(Locale.CHINA, "%.2f", fSelfFreeze));
        oneCapital.setText(String.format(Locale.CHINA, "%.2f", fOneCapital));
        oneBalance.setText(String.format(Locale.CHINA, "%.2f", fOneBalance));
        oneFreeze.setText(String.format(Locale.CHINA, "%.2f", fOneFreeze));
        twoCapital.setText(String.format(Locale.CHINA, "%.2f", fTwoCapital));
        twoBalance.setText(String.format(Locale.CHINA, "%.2f", fTwoBalance));
        twoFreeze.setText(String.format(Locale.CHINA, "%.2f", fTwoFreeze));
    }

    @Override
    public void onClick(View v) {
        if (v == total) {
            startActivity(new Intent(getContext(), WalletsDetailsActivity.class));
        }
    }
}
