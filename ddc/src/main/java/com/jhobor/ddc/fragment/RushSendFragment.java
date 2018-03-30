package com.jhobor.ddc.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jhobor.ddc.R;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.utils.ErrorUtil;
import com.jhobor.ddc.utils.VerifyUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class RushSendFragment extends Fragment implements View.OnClickListener {
    View view;
    EditText task, reputationScore, fee, scale, minTime;
    Button ok;
    int ordersId;
    int deliveryId;
    boolean showDetails = false;

    public RushSendFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        ordersId = arguments.getInt("ordersId");
        String sd = arguments.getString("showDetails");
        showDetails = sd != null && sd.equals("Y");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initView(inflater, container);
        handleEvt();
        if (showDetails) {
            getRushDetails();
        }

        return view;
    }

    private void getRushDetails() {
        BaseApplication.iService.rushDetails(ordersId).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int score = jsonObject.getInt("score");
                    deliveryId = jsonObject.getInt("deliveryId");
                    double money = jsonObject.getDouble("money");
                    String sendDate = jsonObject.getString("sendDate");
                    double weight = jsonObject.getDouble("weight");
                    String title = jsonObject.getString("title");
                    task.setText(title);
                    reputationScore.setText(String.valueOf(score));
                    fee.setText(String.valueOf(money));
                    scale.setText(String.valueOf(weight));
                    minTime.setText(sendDate);
                    task.setFocusable(false);
                    reputationScore.setFocusable(false);
                    fee.setFocusable(false);
                    scale.setFocusable(false);
                    minTime.setFocusable(false);
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getContext(), e);
                }
            }
        }));
    }

    private void handleEvt() {
        ok.setOnClickListener(this);
    }

    private void initView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_rush_send, container, false);
        task = (EditText) view.findViewById(R.id.task);
        reputationScore = (EditText) view.findViewById(R.id.reputationScore);
        fee = (EditText) view.findViewById(R.id.fee);
        scale = (EditText) view.findViewById(R.id.scale);
        minTime = (EditText) view.findViewById(R.id.minTime);
        ok = (Button) view.findViewById(R.id.ok);

        if (showDetails) {
            ok.setText("删除");
        }
    }

    @Override
    public void onClick(View v) {
        if (v == ok) {
            if (showDetails) {
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle("删除")
                        .setMessage("删除该订单可以重新安排第三方物流派送或店铺自己派送")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String uuid = (String) BaseApplication.dataMap.get("token");
                                BaseApplication.iService.rushOrdersDelete(uuid, deliveryId).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
                                    @Override
                                    public void parse(String data) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(data);
                                            int msg = jsonObject.getInt("msg");
                                            if (msg == 1) {
                                                getContext().sendBroadcast(new Intent("refreshStoreOrders"));
                                                Toast.makeText(getContext(), "订单删除成功", Toast.LENGTH_SHORT).show();
                                                getActivity().finish();
                                            } else if (msg == 0) {
                                                Toast.makeText(getContext(), "订单不存在", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            ErrorUtil.retrofitResponseParseFail(getContext(), e);
                                        }
                                    }
                                }));
                            }
                        }).create();
                dialog.show();
            } else {
                final String taskText = task.getText().toString().trim();
                final String reputationScoreText = reputationScore.getText().toString().trim();
                final String feeText = fee.getText().toString().trim();
                final String scaleText = scale.getText().toString().trim();
                final String minTimeText = minTime.getText().toString().trim();
                if (taskText.length() < 3) {
                    Toast.makeText(getContext(), "标题不够具体", Toast.LENGTH_SHORT).show();
                    task.requestFocus();
                } else if (reputationScoreText.isEmpty()) {
                    Toast.makeText(getContext(), "信誉分不可为空", Toast.LENGTH_SHORT).show();
                    reputationScore.requestFocus();
                } else if (feeText.isEmpty()) {
                    Toast.makeText(getContext(), "费用不可为空", Toast.LENGTH_SHORT).show();
                    fee.requestFocus();
                } else if (scaleText.isEmpty()) {
                    Toast.makeText(getContext(), "重量不可为空", Toast.LENGTH_SHORT).show();
                    scale.requestFocus();
                } else if (!VerifyUtil.isDateTime(minTimeText)) {
                    Toast.makeText(getContext(), "请按格式【2017-03-02 11:43】输入最晚送达时间", Toast.LENGTH_SHORT).show();
                    minTime.requestFocus();
                } else {
                    AlertDialog dialog = new AlertDialog.Builder(getContext())
                            .setTitle("确认")
                            .setMessage("填写的订单信息正确无误吗？一旦确定就不可以更改。")
                            .setNegativeButton("再看看", null)
                            .setPositiveButton("没问题", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String uuid = (String) BaseApplication.dataMap.get("token");
                                            BaseApplication.iService.rushStream(uuid, ordersId, taskText, reputationScoreText, feeText, scaleText, minTimeText).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
                                                @Override
                                                public void parse(String data) {
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(data);
                                                        int isLogin = jsonObject.getInt("isLogin");
                                                        if (isLogin == 1) {
                                                            int msg = jsonObject.getInt("msg");
                                                            if (msg == 1) {
                                                                getContext().sendBroadcast(new Intent("refreshStoreOrders"));
                                                                Toast.makeText(getContext(), "发布成功", Toast.LENGTH_SHORT).show();
                                                                getActivity().finish();
                                                            } else if (msg == 2) {
                                                                Toast.makeText(getContext(), "你的帐户余额不足", Toast.LENGTH_SHORT).show();
                                                            } else if (msg == 3) {
                                                                Toast.makeText(getContext(), "该订单已经发布过了", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    } catch (JSONException e) {
                                                        ErrorUtil.retrofitResponseParseFail(getContext(), e);
                                                    }
                                                }
                                            }));
                                        }
                                    }
                            ).create();
                    dialog.show();
                }
            }
        }
    }
}
