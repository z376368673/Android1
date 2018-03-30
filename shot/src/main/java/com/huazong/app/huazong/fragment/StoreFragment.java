package com.huazong.app.huazong.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.huazong.app.huazong.R;
import com.huazong.app.huazong.StoreDetailActivity;
import com.huazong.app.huazong.adapter.StoreBaseAdapter;
import com.huazong.app.huazong.base.BaseApplication;
import com.huazong.app.huazong.base.RetrofitCallback;
import com.huazong.app.huazong.entity.Store;
import com.huazong.app.huazong.utils.ErrorUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class StoreFragment extends Fragment implements AdapterView.OnItemClickListener {
    ListView storesView;
    View view;
    private StoreBaseAdapter storeBaseAdapter;
    private Store[] stores;

    public StoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getActivity().getApplication());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_store, container, false);

        initView();
        getData();
        return view;
    }

    private void initView() {
        storesView = (ListView) view.findViewById(R.id.storesView);
        storesView.setOnItemClickListener(this);
    }

    private void getData() {
        BaseApplication.iService.storeList().enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    int count = jsonArray.length();
                    stores = new Store[count];
                    for (int i = 0; i < count; i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        int id = object.getInt("id");
                        String name = object.getString("name");
                        String address = object.getString("address");
                        String urlString = object.getString("picture");
                        String picture = BaseApplication.BASE_URL_HOST2 + urlString;
                        String desc = object.getString("descript");
                        double lng = object.getDouble("lng");
                        double lat = object.getDouble("lat");
                        Store store = new Store(id, name, address, picture, desc, lng, lat);
                        double m = DistanceUtil.getDistance(BaseApplication.here,new LatLng(lat,lng));
                        store.setDistance(m);
                        stores[i] = store;
                    }
                    Arrays.sort(stores);
                    storeBaseAdapter = new StoreBaseAdapter(getContext(), stores);
                    storesView.setAdapter(storeBaseAdapter);
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getContext(),e);
                }
            }
        }));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int storeId = stores[position].getId();
        String storeName = stores[position].getName();
        Intent intent = new Intent(getContext(), StoreDetailActivity.class);
        intent.putExtra("storeId", storeId);
        intent.putExtra("storeName", storeName);
        startActivity(intent);
    }

}
