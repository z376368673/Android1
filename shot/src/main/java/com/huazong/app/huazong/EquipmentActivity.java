package com.huazong.app.huazong;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.huazong.app.huazong.adapter.EquipmentArrayAdapter;
import com.huazong.app.huazong.base.BarUtil;
import com.huazong.app.huazong.base.BaseApplication;
import com.huazong.app.huazong.base.RetrofitCallback;
import com.huazong.app.huazong.entity.Equipment;
import com.huazong.app.huazong.utils.ErrorUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EquipmentActivity extends AppCompatActivity {
    TextView topTitle;
    ListView equipmentsView;

    List<Equipment> equipments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_equipment);

        topTitle = (TextView) findViewById(R.id.topTitle);
        equipmentsView = (ListView) findViewById(R.id.equipmentsView);

        Intent intent = getIntent();
        BarUtil.topBar(this,intent.getStringExtra("title"));
        getData();
    }

    private void getData() {
        String openid = (String) BaseApplication.dataMap.get("openid");
        BaseApplication.iService.myEquipment(openid).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray data = jsonObject.getJSONArray("data");
                    int length = data.length();
                    if (length > 0) {
                        equipments = new ArrayList<>(length);
                    } else {
                        equipments = new ArrayList<>(1);
                    }
                    for (int i = 0; i < length; i++) {
                        JSONObject object = data.getJSONObject(i);
                        int id = object.getInt("id");
                        String outfitNo = object.getString("outfitNo");
                        String name = object.getString("name");
                        int count = object.getInt("count");
                        Equipment equipment = new Equipment(outfitNo, name, count);
                        equipments.add(equipment);
                    }
                    EquipmentArrayAdapter adapter = new EquipmentArrayAdapter(getBaseContext(), R.layout.item_equipment, equipments);
                    equipmentsView.setAdapter(adapter);
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getBaseContext(),e);
                }
            }
        }));
    }

}
