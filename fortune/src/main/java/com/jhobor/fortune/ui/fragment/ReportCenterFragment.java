package com.jhobor.fortune.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.jhobor.fortune.R;
import com.jhobor.fortune.adapter.LowerAdapter;
import com.jhobor.fortune.adapter.ReportCenterAdapter;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.dialog.LoadingDialog;
import com.jhobor.fortune.entity.ChildCenterBean;
import com.jhobor.fortune.entity.LowerLevel;
import com.jhobor.fortune.utils.ErrorUtil;
import com.jhobor.fortune.utils.TabUtil;
import com.vincent.filepicker.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportCenterFragment extends BaseFragment {

    public static int TYPE = 0; // 0，旗下服务中心    1，非旗下服务中心

    View view;

    private TextView tv_account;
    private TextView tv_increasing_people;
    private TextView tv_report_reward;
    private TextView tv_countNum;

    private RecyclerView recyclerview;
    LoadingDialog dialog;
    ReportCenterAdapter adapter;

    public ReportCenterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_report_center, container, false);
        dialog = new LoadingDialog(getContext());
        bindViews(view);
        return view;
    }

    private void bindViews(View view) {
        tv_account = (TextView) view.findViewById(R.id.tv_account);
        tv_increasing_people = (TextView) view.findViewById(R.id.tv_increasing_people);
        tv_report_reward = (TextView) view.findViewById(R.id.tv_report_reward);
        tv_countNum = (TextView) view.findViewById(R.id.tv_countNum);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        if (TYPE == 0) {
            tv_account.setText("旗下服务中心账号");
            childBillCenter();
        } else {
            tv_account.setText("非服务中心");
            notBillCenter();
        }

    }

    //旗下服务中心
    private void childBillCenter() {
        dialog.show();
        String token = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.childBillCenter(token).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int msg = jsonObject.getInt("msg");
                    data = jsonObject.getString("childBillCenter");
                    if (msg == 1) {
                        List<ChildCenterBean> list = JSON.parseArray(data, ChildCenterBean.class);
                        if (list == null || list.size() == 0) return;
                        adapter = new ReportCenterAdapter(R.layout.item_report, list);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerview.setLayoutManager(layoutManager);
                        recyclerview.setAdapter(adapter);
                    } else {
                        String errorInfo = jsonObject.getString("errorInfo");
                        ToastUtil.getInstance(getContext()).showToast(errorInfo);
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getContext(), e);
                } finally {
                    dialog.dismiss();
                }
            }
        }));
    }

    //非服务中心
    private void notBillCenter() {
        dialog.show();
        String token = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.notBillCenter(token).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int msg = jsonObject.getInt("msg");
                    if (msg == 1) {
                        ChildCenterBean childCenterBean = JSON.parseObject(data, ChildCenterBean.class);
                        List<ChildCenterBean> list = new ArrayList<>();
                        list.add(childCenterBean);
                        adapter = new ReportCenterAdapter(R.layout.item_report, list);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerview.setLayoutManager(layoutManager);
                        recyclerview.setAdapter(adapter);
                    } else {
                        String errorInfo = jsonObject.getString("errorInfo");
                        ToastUtil.getInstance(getContext()).showToast(errorInfo);
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getContext(), e);
                } finally {
                    dialog.dismiss();
                }
            }
        }));
    }


    @Override
    public void onResume() {
        super.onResume();
    }
}
