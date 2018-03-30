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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class StreamSendFragment extends Fragment implements View.OnClickListener {
    View view;
    EditText deliveryNo;
    Button ok;

    int ordersId;

    public StreamSendFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        ordersId = arguments.getInt("ordersId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initView(inflater, container);
        handleEvt();
        return view;
    }

    private void handleEvt() {
        ok.setOnClickListener(this);
    }

    private void initView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_stream_send, container, false);
        deliveryNo = (EditText) view.findViewById(R.id.deliveryNo);
        ok = (Button) view.findViewById(R.id.ok);

    }

    @Override
    public void onClick(View v) {
        if (v == ok) {
            final String streamNo = deliveryNo.getText().toString().trim();
            if (streamNo.length() < 6) {
                Toast.makeText(getContext(), "物流单号不正确", Toast.LENGTH_SHORT).show();
                deliveryNo.requestFocus();
            } else {
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle("确认")
                        .setMessage(String.format(Locale.CHINA, "物流单号【%s】正确无误吗？一旦确定就不可以更改。", streamNo))
                        .setNegativeButton("再看看", null)
                        .setPositiveButton("没问题", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String uuid = (String) BaseApplication.dataMap.get("token");
                                BaseApplication.iService.othersStream(uuid, ordersId, streamNo).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
                                    @Override
                                    public void parse(String data) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(data);
                                            int isLogin = jsonObject.getInt("isLogin");
                                            if (isLogin == 1) {
                                                int msg = jsonObject.getInt("msg");
                                                if (msg == 1) {
                                                    getContext().sendBroadcast(new Intent("refreshStoreOrders"));
                                                    getActivity().finish();
                                                }
                                            }
                                        } catch (JSONException e) {
                                            ErrorUtil.retrofitResponseParseFail(getContext(), e);
                                        }
                                    }
                                }));
                            }
                        }).create();
                dialog.show();

            }
        }
    }
}
