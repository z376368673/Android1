package com.huazong.app.huazong;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huazong.app.huazong.adapter.TrainingRecordExpandableListAdapter;
import com.huazong.app.huazong.base.BarUtil;
import com.huazong.app.huazong.base.BaseApplication;
import com.huazong.app.huazong.base.RetrofitCallback;
import com.huazong.app.huazong.entity.TrainingRecord;
import com.huazong.app.huazong.entity.TrainingRecordGroup;
import com.huazong.app.huazong.utils.ErrorUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TrainingRecordActivity extends AppCompatActivity implements ExpandableListView.OnGroupClickListener {
    ExpandableListView recordsView;

    boolean[] flagGroupExpand;
    TrainingRecordExpandableListAdapter adapter;
    private List<TrainingRecordGroup> trainingRecordGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_training_record);

        Intent intent = getIntent();
        BarUtil.topBar(this,intent.getStringExtra("title"));
        recordsView = (ExpandableListView) findViewById(R.id.recordsView);

        recordsView.setOnGroupClickListener(this);
        getData();
    }

    private void getData() {
        String openid = (String) BaseApplication.dataMap.get("openid");
        int arrowOrGun = (int) BaseApplication.dataMap.get("arrowOrGun");
        BaseApplication.iService.trainRecordList(openid,arrowOrGun).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray data = jsonObject.getJSONArray("data");
                    int length = data.length();
                    if (length > 0) {
                        flagGroupExpand = new boolean[length];
                    } else {
                        flagGroupExpand = new boolean[1];
                    }
                    trainingRecordGroups = new ArrayList<>();
                    String tmp = "";
                    for (int i = 0; i < length; i++) {
                        flagGroupExpand[i] = false;
                        JSONObject object = data.getJSONObject(i);
                        int id = object.getInt("id");
                        String date = object.getString("date");
                        String time = object.getString("time");
                        int pass = object.getInt("pass");
                        int score = object.getInt("score");
                        TrainingRecord tr = new TrainingRecord(date, time, pass, score);
                        if (tmp.equals("") || !tmp.equals(date)) {
                            List<TrainingRecord> recordList = new ArrayList<>();
                            recordList.add(tr);
                            TrainingRecordGroup group = new TrainingRecordGroup(date, recordList);
                            trainingRecordGroups.add(group);
                            tmp = date;
                        } else {
                            trainingRecordGroups.get(trainingRecordGroups.size() - 1).getRecordList().add(tr);
                        }
                    }
                    adapter = new TrainingRecordExpandableListAdapter(getBaseContext(), trainingRecordGroups);
                    recordsView.setAdapter(adapter);
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getBaseContext(),e);
                }
            }
        }));
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View view, int groupPosition, long id) {
        TextView tv = (TextView) ((LinearLayout) view).getChildAt(0);
        if (!flagGroupExpand[groupPosition]) {
            tv.setBackgroundResource(R.mipmap.train_group_expand);
            flagGroupExpand[groupPosition] = true;
        } else {
            tv.setBackgroundResource(R.mipmap.train_group_collapse);
            flagGroupExpand[groupPosition] = false;
        }
        return false;
    }
}
