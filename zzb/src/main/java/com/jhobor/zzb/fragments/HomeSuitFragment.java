package com.jhobor.zzb.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jhobor.zzb.R;
import com.jhobor.zzb.adapter.EnterpriseStoreAdapter;
import com.jhobor.zzb.adapter.FamilyStoreAdapter;
import com.jhobor.zzb.entity.EnterpriseStore;
import com.jhobor.zzb.entity.FamilyStore;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeSuitFragment extends Fragment {
    ListView listView;
    String what;

    public HomeSuitFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        what = arguments.getString("what");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_suit, container, false);
        listView = (ListView) view.findViewById(R.id.listView);

        if (what.equals("homeSuit")){
            List<FamilyStore> familyStoreList = new ArrayList<>();
            familyStoreList.add(new FamilyStore(1,"广东","深圳","光明新区","福成街道","家装馆建材市场11号铺",2));
            familyStoreList.add(new FamilyStore(2,"广东","深圳","光明新区","福成街道","家装馆建材市场11号铺",3));
            familyStoreList.add(new FamilyStore(3,"广东","深圳","光明新区","福成街道","家装馆建材市场11号铺",2));
            familyStoreList.add(new FamilyStore(4,"广东","深圳","光明新区","福成街道","家装馆建材市场11号铺",5));
            FamilyStoreAdapter familyStoreAdapter = new FamilyStoreAdapter(getContext(), familyStoreList);
            listView.setAdapter(familyStoreAdapter);
        }else if (what.equals("enterpriseSuit")){
            List<EnterpriseStore> enterpriseStoreList = new ArrayList<>();
            enterpriseStoreList.add(new EnterpriseStore(1,"","apple","18098983109","深圳公司",true));
            enterpriseStoreList.add(new EnterpriseStore(2,"","banana","18098983109","广州公司",false));
            enterpriseStoreList.add(new EnterpriseStore(3,"","cat","18098983109","惠州公司",false));
            enterpriseStoreList.add(new EnterpriseStore(4,"","dog","18098983109","上海公司",false));
            EnterpriseStoreAdapter enterpriseStoreAdapter = new EnterpriseStoreAdapter(getContext(),enterpriseStoreList);
            listView.setAdapter(enterpriseStoreAdapter);
        }

        return view;
    }

}
