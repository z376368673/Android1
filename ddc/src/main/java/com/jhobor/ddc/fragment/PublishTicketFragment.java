package com.jhobor.ddc.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.jhobor.ddc.R;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.utils.ErrorUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class PublishTicketFragment extends Fragment implements View.OnClickListener {
    EditText restriction, privilege, count;
    ImageButton ok;
    View view;

    public PublishTicketFragment() {
        // Required empty public constructor
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
        view = inflater.inflate(R.layout.fragment_publish_ticket, container, false);
        restriction = (EditText) view.findViewById(R.id.restriction);
        privilege = (EditText) view.findViewById(R.id.privilege);
        count = (EditText) view.findViewById(R.id.count);
        ok = (ImageButton) view.findViewById(R.id.ok);
    }

    @Override
    public void onClick(View v) {
        if (v == ok) {
            String restrict = restriction.getText().toString().trim();
            String privilegeText = privilege.getText().toString().trim();
            String countText = count.getText().toString().trim();
            int ii = Integer.parseInt(restrict);
            int jj = Integer.parseInt(privilegeText);

            if (restrict.isEmpty()) {
                Toast.makeText(getContext(), "请设置限制满多少可使用优惠券", Toast.LENGTH_SHORT).show();
                restriction.requestFocus();
            } else if (privilegeText.isEmpty()) {
                Toast.makeText(getContext(), "请设置优惠金额", Toast.LENGTH_SHORT).show();
                privilege.requestFocus();
            } else if (privilegeText.length() > 4) {
                Toast.makeText(getContext(), "优惠金额不能大于9999", Toast.LENGTH_SHORT).show();
                privilege.requestFocus();
            } else if (ii <= jj) {
                Toast.makeText(getContext(), "优惠金额不能大于限制使用金额", Toast.LENGTH_SHORT).show();
                privilege.requestFocus();
            } else if (countText.isEmpty()) {
                Toast.makeText(getContext(), "请设置发行优惠券的数量", Toast.LENGTH_SHORT).show();
                count.requestFocus();
            } else if (countText.length() > 4) {
                Toast.makeText(getContext(), "优惠券数量不能大于9999", Toast.LENGTH_SHORT).show();
                count.requestFocus();
            } else {
                String uuid = (String) BaseApplication.dataMap.get("token");
                BaseApplication.iService.publishTicket(uuid, restrict, privilegeText, countText).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
                    @Override
                    public void parse(String data) {
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            int msg = jsonObject.getInt("msg");
                            if (msg == 1) {
                                restriction.setText("");
                                privilege.setText("");
                                count.setText("");
                                getContext().sendBroadcast(new Intent("refreshTicketData"));
                                Toast.makeText(getContext(), "优惠券发布成功", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            ErrorUtil.retrofitResponseParseFail(getContext(), e);
                        }
                    }
                }));
            }
        }
    }
}
