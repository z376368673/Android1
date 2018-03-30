package com.jhobor.zzb.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.jhobor.zzb.ProductDetailsActivity;
import com.jhobor.zzb.R;
import com.jhobor.zzb.SearchActivity;
import com.jhobor.zzb.StoreProductsActivity;
import com.jhobor.zzb.adapter.ProductAdapter;
import com.jhobor.zzb.entity.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, OnRefreshListener, OnLoadMoreListener, OnItemClickListener {
    Spinner addr;
    TextView search;
    LRecyclerView lRecyclerView;

    List<Product> productList = new ArrayList<>();
    ProductAdapter adapter;
    LRecyclerViewAdapter lRecyclerViewAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        addr = (Spinner) view.findViewById(R.id.addr);
        search = (TextView) view.findViewById(R.id.search);
        lRecyclerView = (LRecyclerView) view.findViewById(R.id.lRecyclerView);
        lRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        lRecyclerView.setOnRefreshListener(this);
        lRecyclerView.setOnLoadMoreListener(this);
        initData();

        return view;
    }

    private void initData() {
        search.setOnClickListener(this);
        String[] addrNames = {"本地","罗定","深圳","广州","上海","北京"};
        List<Map<String,Object>> dataTable = new ArrayList<>();
        for (String addrName : addrNames) {
            Map<String, Object> map = new HashMap<>();
            map.put("place", addrName);
            dataTable.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(), dataTable, R.layout.item_spinner_place, new String[]{"place"}, new int[]{R.id.place});
        addr.setAdapter(simpleAdapter);
        addr.setOnItemSelectedListener(this);
        List<String> img1 = new ArrayList<String>();
        List<String> img2 = new ArrayList<String>();
        List<String> img3 = new ArrayList<String>();
        img1.add("http://preview.quanjing.com/mhrf005/mhrf-cpmh-82526f07z.jpg");
        img2.add("http://preview.quanjing.com/mhrf005/mhrf-cpmh-82526f07z.jpg");
        img2.add("http://preview.quanjing.com/mhrf005/mhrf-cpmh-82526f07z.jpg");
        img3.add("http://preview.quanjing.com/mhrf005/mhrf-cpmh-82526f07z.jpg");
        img3.add("http://preview.quanjing.com/mhrf005/mhrf-cpmh-82526f07z.jpg");
        img3.add("http://preview.quanjing.com/mhrf005/mhrf-cpmh-82526f07z.jpg");
        productList.add(new Product(1,"建材建材建材建材建材建材建材建材建材建材建材建材建材建材建材建材建材",img1,"07.14 16:28",125,0));
        productList.add(new Product(2,"建材建材建材建材建材建材建材建材建材建材建材建材建材建材建材建材建材",img2,"07.14 16:28",125,0));
        productList.add(new Product(3,"建材建材建材建材建材建材建材建材建材建材建材建材建材建材建材建材建材",img3,"07.14 16:28",125,0));
        adapter = new ProductAdapter(getContext(),productList);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        lRecyclerViewAdapter.setOnItemClickListener(this);
        lRecyclerView.setAdapter(lRecyclerViewAdapter);

    }

    @Override
    public void onClick(View v) {
        if (v==search){
            startActivity(new Intent(getContext(), SearchActivity.class));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onRefresh() {
        lRecyclerView.refreshComplete(1);
        lRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadMore() {
        lRecyclerView.refreshComplete(1);
    }

    @Override
    public void onItemClick(View view, int position) {
        if (position%2==0) {
            startActivity(new Intent(getContext(), ProductDetailsActivity.class));
        }else {
            startActivity(new Intent(getContext(), StoreProductsActivity.class));
        }
    }
}
