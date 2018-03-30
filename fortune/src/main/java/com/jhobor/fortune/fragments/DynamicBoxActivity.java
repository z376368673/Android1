package com.jhobor.fortune.fragments;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.jhobor.fortune.R;
import com.jhobor.fortune.adapter.DynamicAdapter;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.entity.Booding;
import com.jhobor.fortune.utils.BarUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Author     Qijing
 * Created by YQJ on 2017/10/16.
 * Description:
 */
public class DynamicBoxActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView dynamic_rv;
    private ArrayList<Booding> mList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic);
        BarUtil.topBar(this, "奖金钱包");
        dynamic_rv = (RecyclerView)findViewById(R.id.dynamic_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dynamic_rv.setLayoutManager(layoutManager);
        getData();
    }

    private void getData() {
        mList = new ArrayList<Booding>();
        String uuid = BaseApplication.prefs.getString("token", "null");
        BaseApplication.iService.dynmic(uuid).enqueue(new RetrofitCallback(getApplicationContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int isLogin = jsonObject.getInt("isLogin");
                    if (isLogin == 1){
                        JSONArray brList = jsonObject.getJSONArray("brList");
                        int length = brList.length();
                        for (int i = 0; i < length; i++) {
                            JSONArray jsonArray = brList.getJSONArray(i);
                            String name = (String) jsonArray.get(0);
                            String trader = (String) jsonArray.get(1);
                            double money = (double) jsonArray.get(2);
                            String phone = (String) jsonArray.get(3);
                            String date = (String) jsonArray.get(4);

                            Booding b = new Booding();


                            b.setParentName(name);
                            b.setOrderNo(trader);
                            b.setMoney((float) money);
                            b.setParentPhone(phone);
                            b.setParticipator(date);
                            mList.add(b);
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "无法获取数据", Toast.LENGTH_SHORT).show();
                    }


                    dynamic_rv.setAdapter(new DynamicAdapter(R.layout.item_dynamic, mList));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));


    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View view) {

    }


}
