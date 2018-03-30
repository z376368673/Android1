package com.huazong.app.huazong.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.huazong.app.huazong.R;
import com.huazong.app.huazong.adapter.OrderUnusedBaseAdapter;
import com.huazong.app.huazong.adapter.OrderUsedBaseAdapter;
import com.huazong.app.huazong.entity.Order;

import java.util.List;

/**
 * Created by Administrator on 2016/7/22.
 */
public class OrderFragment extends Fragment {
    ListView storesView;
    int useStatus;
    private List<Order> orderList;
    private int depth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        useStatus = arguments.getInt("useStatus");
        orderList = arguments.getParcelableArrayList("orders");
        depth = arguments.getInt("depth");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        storesView = (ListView) view.findViewById(R.id.storesView);

        if (useStatus == 0) {
            OrderUnusedBaseAdapter adapter = new OrderUnusedBaseAdapter(getActivity(), inflater, orderList, depth);
            storesView.setAdapter(adapter);
        } else {
            OrderUsedBaseAdapter adapter = new OrderUsedBaseAdapter(getActivity(), inflater, orderList);
            storesView.setAdapter(adapter);
        }

        return view;
    }


}
