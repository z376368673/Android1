package com.jhobor.ddc.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jhobor.ddc.R;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.entity.Store;
import com.jhobor.ddc.utils.ErrorUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreInfoFragment extends Fragment implements View.OnClickListener {
    View view;
    TextView period, storeAddr, tel, report;

    int storeId;
    Store store;
    int count;//优惠券数量

    public StoreInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initView(inflater, container);
        if (store == null) {
            getStoreInfo();
        } else {
            showData();
        }

        return view;
    }

    private void getStoreInfo() {
        storeId = (int) BaseApplication.dataMap.get("storeId");
        BaseApplication.iService.storeInfo(storeId).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String address = jsonObject.getString("address");
                    String mobile = jsonObject.getString("mobile");
                    String openDate = jsonObject.getString("openDate");
                    count = jsonObject.getInt("count");
                    store = new Store();
                    store.setAddr(address);
                    store.setPhone(mobile);
                    store.setOperatingTime(openDate);
                    showData();
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getContext(), e);
                }
            }
        }));
    }

    private void showData() {
        period.setText(store.getOperatingTime());
        storeAddr.setText(store.getAddr());
        tel.setText(store.getPhone());
    }

    private void initView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_store_info, container, false);
        period = (TextView) view.findViewById(R.id.period);
        storeAddr = (TextView) view.findViewById(R.id.storeAddr);
        tel = (TextView) view.findViewById(R.id.tel);
        report = (TextView) view.findViewById(R.id.report);
    }

    @Override
    public void onClick(View v) {
        if (v == report) {

        }
    }
}
