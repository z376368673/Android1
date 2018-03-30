package com.jhobor.ddc.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jhobor.ddc.R;
import com.jhobor.ddc.adapter.MyCollectionBaseAdapter;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.entity.Collection;
import com.jhobor.ddc.utils.ErrorUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyCollectionActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView topArrow;
    TextView topTitle;
    ListView listView;

    List<Collection> collectionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);

        initView();
        handleEvt();
        getCollectionData();
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);
    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        topTitle = (TextView) findViewById(R.id.topTitle);
        listView = (ListView) findViewById(R.id.listView);

        topTitle.setText("我的收藏");

    }

    private void getCollectionData() {
        String uuid = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.myCollection(uuid, 0).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int isLogin = jsonObject.getInt("isLogin");
                    JSONArray collectList = jsonObject.getJSONArray("collectList");
                    collectionList = new ArrayList<Collection>();
                    for (int i = 0; i < collectList.length(); i++) {
                        JSONArray jsonArray = collectList.getJSONArray(i);
                        int id = jsonArray.getInt(0);
                        String storeName = jsonArray.getString(1);
                        String storePic = BaseApplication.BASE_URL + jsonArray.getString(2);
                        double scores = jsonArray.getDouble(3);
                        String time = jsonArray.getString(4);
                        int storeState = jsonArray.getInt(5);
                        //int id, int storeId, String storeName, int userId, float scores, String date
                        collectionList.add(new Collection(id, 0, storeName, 0, (float) scores, time, storePic));
                    }
                    listView.setAdapter(new MyCollectionBaseAdapter(collectionList, MyCollectionActivity.this));

                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                }
            }
        }));
    }

    @Override
    public void onClick(View v) {
        if (v == topArrow) {
            finish();
        }
    }
}
