package com.jhobor.fortune.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jhobor.fortune.R;
import com.jhobor.fortune.adapter.CalculusListAdapter;
import com.jhobor.fortune.adapter.RecordListAdapter;
import com.jhobor.fortune.adapter.ReportListAdapter;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.dialog.LoadingDialog;
import com.jhobor.fortune.entity.CalculusBean;
import com.jhobor.fortune.entity.RecordBean;
import com.jhobor.fortune.entity.ReportBean;
import com.vincent.filepicker.ToastUtil;


import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntegralRecordFragment extends BaseFragment {
    public static int INTEGRAL_TYPE = 0; // 0，租车积分  1，电商积分 2 服务积分
    public static int TYPE = 0; // 0，增长  1，提现 2转让

    View view;
    private ListView listView;
    LoadingDialog dialog;
    RecordListAdapter recordAdapter; // 租车
    CalculusListAdapter calculusListAdapter; //电商
    ReportListAdapter reportListAdapter; //服务

    public IntegralRecordFragment() {
        // Required empty public constructor
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_integral_record_list, container, false);
        bindViews(view);
        initDate();
        return view;
    }

    private void initDate() {
        dialog.show();
        if (INTEGRAL_TYPE == 0) {
            recordAdapter = new RecordListAdapter(getContext());
            listView.setAdapter(recordAdapter);
            getIntegralList(TYPE);
        } else if (INTEGRAL_TYPE == 1) {
            calculusListAdapter = new CalculusListAdapter(getContext());
            listView.setAdapter(calculusListAdapter);

            getalculusRecordList(TYPE);
        } else if (INTEGRAL_TYPE == 2) {
            reportListAdapter = new ReportListAdapter(getContext());
            listView.setAdapter(reportListAdapter);
            billIntegralRecord(TYPE);
        }

    }

    private void bindViews(View view) {
        dialog = new LoadingDialog(getContext());
        listView = (ListView) view.findViewById(R.id.listView);
    }

    /**
     * 获取租车积分记录
     *
     * @param tag
     */
    private void getIntegralList(int tag) {
        String uuid = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.getIntegralList(uuid, tag).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                 JSONObject jsonObject = JSON.parseObject(data);
                try {
                    int msg = jsonObject.getIntValue("msg");
                    if (msg == 1) {
                        List<RecordBean> beanList = JSON.parseArray(jsonObject.getString("irList"), RecordBean.class);
                        if (beanList.size() > 0) {
                            recordAdapter.clear();
                            recordAdapter.addAll(beanList);
                            recordAdapter.notifyDataSetChanged();
                        }else {
                            ToastUtil.getInstance(getContext()).showToast("暂无数据");
                        }
                    } else {
                        Toast.makeText(getContext(), jsonObject.getString("errorInfo"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                } finally {
                    dialog.dismiss();
                }
            }
        }));
    }

    /**
     * 获取电商积分记录
     *
     * @param tag
     */
    private void getalculusRecordList(int tag) {
        String uuid = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.getalculusRecordList(uuid, tag).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                     JSONObject jsonObject = JSON.parseObject(data);
                    int msg = jsonObject.getIntValue("msg");
                    if (msg == 1) {
                        List<CalculusBean> beanList = JSON.parseArray(jsonObject.getString("crList"), CalculusBean.class);
                        if (beanList.size() > 0) {
                            calculusListAdapter.clear();
                            calculusListAdapter.addAll(beanList);
                            calculusListAdapter.notifyDataSetChanged();
                        }else {
                            ToastUtil.getInstance(getContext()).showToast("暂无数据");
                        }
                    } else {
                        Toast.makeText(getContext(), jsonObject.getString("errorInfo"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                } finally {
                    dialog.dismiss();
                }
            }
        }));
    }

    /**
     * 获取服务积分记录
     *
     * @param tag
     */
    private void billIntegralRecord(int tag) {
        String uuid = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.billIntegralRecord(uuid, tag).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                     JSONObject jsonObject = JSON.parseObject(data);
                    int msg = jsonObject.getIntValue("msg");
                    if (msg == 1) {
                        List<ReportBean> beanList = JSON.parseArray(jsonObject.getString("irList"), ReportBean.class);
                        if (beanList.size() > 0) {
                            reportListAdapter.clear();
                            reportListAdapter.addAll(beanList);
                            reportListAdapter.notifyDataSetChanged();
                        }else {
                            ToastUtil.getInstance(getContext()).showToast("暂无数据");
                        }
                    } else {
                        Toast.makeText(getContext(), jsonObject.getString("errorInfo"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

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
