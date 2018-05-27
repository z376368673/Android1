package com.jhobor.fortune.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.jhobor.fortune.R;
import com.jhobor.fortune.adapter.TeamRecordAdapter;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.dialog.LoadingDialog;
import com.jhobor.fortune.entity.TeamRecord;
import com.jhobor.fortune.utils.ErrorUtil;
import com.vincent.filepicker.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeamRecordFragment extends BaseFragment {

    public static int TYPE = 0 ; // 0，增资记录    1，未投资记录

    View view;

    private TextView tv_text1;
    private TextView tv_text2;
    private TextView tv_text3;


    private RecyclerView recyclerview;

    TeamRecordAdapter adapter;
    LoadingDialog dialog;
    public TeamRecordFragment() {
        // Required empty public constructor
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_team_record, container, false);
        dialog = new LoadingDialog(getContext());
        bindViews(view);
        return view;
    }
    private void bindViews(View view) {
        tv_text1 = (TextView) view.findViewById(R.id.tv_text1);
        tv_text2 = (TextView) view.findViewById(R.id.tv_text2);
        tv_text3 = (TextView) view.findViewById(R.id.tv_text3);

        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        if (TYPE == 0) {
            tv_text1.setText("旗下服务中心账号");
            tv_text2.setText("增资金额(元)");
            tv_text3.setText("增资日期");
            groupInvestRecord();
        }else {
            tv_text1.setText("账号");
            tv_text2.setText("分属服务中心");
            tv_text3.setText("注册日期");
            noInvestRecord();
        }
    }

    /**
     * 团队投资明细-增资记录
     */
    private void groupInvestRecord() {
        dialog.show();
        String token = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.groupInvestRecord(token).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                   JSONObject jsonObject = new JSONObject(data);
                    int msg = jsonObject.getInt("msg");
                    data = jsonObject.getString("childCapitalRecord");
                    if (msg == 1) {
                        List<TeamRecord> list = JSON.parseArray(data,TeamRecord.class);
                        adapter = new TeamRecordAdapter(R.layout.item_team_record,list);
                        adapter.setType(1);
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
                }finally {
                    dialog.dismiss();
                }
            }
        }));
    }


    /**
     * 团队投资明细-未投资记录
     */
    private void noInvestRecord() {
        dialog.show();
        String token = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.noInvestRecord(token).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int msg = jsonObject.getInt("msg");
                    data = jsonObject.getString("noInvestRecordList");
                    if (msg == 1) {
                        List<TeamRecord> list = JSON.parseArray(data,TeamRecord.class);
                        adapter = new TeamRecordAdapter(R.layout.item_team_record,list);
                        adapter.setType(2);
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
                }finally {
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
