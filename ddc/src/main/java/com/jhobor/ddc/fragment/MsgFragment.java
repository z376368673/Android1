package com.jhobor.ddc.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jhobor.ddc.R;
import com.jhobor.ddc.activity.MsgDetailsActivity;
import com.jhobor.ddc.adapter.MsgBaseAdapter;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.entity.Msg;
import com.jhobor.ddc.utils.ErrorUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MsgFragment extends Fragment implements AdapterView.OnItemClickListener {
    View view;
    ListView listView;
    ImageView topArrow;
    TextView topTitle;

    List<Msg> msgList;

    public MsgFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initView(inflater, container);
        handleEvt();
        if (msgList == null) {
            getMsgData();
        } else {
            listView.setAdapter(new MsgBaseAdapter(msgList, getContext()));
        }

        return view;
    }

    private void handleEvt() {
        listView.setOnItemClickListener(this);
    }

    private void initView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_msg, container, false);
        topArrow = (ImageView) view.findViewById(R.id.topArrow);
        topTitle = (TextView) view.findViewById(R.id.topTitle);
        listView = (ListView) view.findViewById(R.id.listView);

        topArrow.setVisibility(View.INVISIBLE);
        topTitle.setText("消息");
        //listView.setAdapter(new MsgBaseAdapter(msgList, getContext()));
    }

    private void getMsgData() {
        String uuid = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.myMsg(uuid, 0).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int isLogin = jsonObject.getInt("isLogin");
                    JSONArray ordersList = jsonObject.getJSONArray("infoList");
                    msgList = new ArrayList<Msg>();
                    for (int i = 0; i < ordersList.length(); i++) {
                        JSONArray jsonArray = ordersList.getJSONArray(i);
                        int id = jsonArray.getInt(0);
                        String title = jsonArray.getString(1);
                        String content = jsonArray.getString(2);
                        String time = jsonArray.getString(3);
                        msgList.add(new Msg(id, title, content, time));
                    }
                    listView.setAdapter(new MsgBaseAdapter(msgList, getContext()));
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getContext(), e);
                }
            }
        }));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), MsgDetailsActivity.class);
        int msgId = msgList.get(position).getId();
        intent.putExtra("msgId", msgId);
        startActivity(intent);
    }
}
