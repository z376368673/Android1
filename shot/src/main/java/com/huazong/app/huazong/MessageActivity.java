package com.huazong.app.huazong;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ListView;

import com.huazong.app.huazong.adapter.MsgArrayAdapter;
import com.huazong.app.huazong.base.BarUtil;
import com.huazong.app.huazong.base.BaseApplication;
import com.huazong.app.huazong.base.RetrofitCallback;
import com.huazong.app.huazong.entity.PushMessage;
import com.huazong.app.huazong.utils.ErrorUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    ListView msgListView;

    private List<PushMessage> msgList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_msg);

        msgListView = (ListView) findViewById(R.id.msgListView);
        BarUtil.topBar(this,getIntent().getStringExtra("title"));
        getData();
    }

    private void getData() {
        String openid = (String) BaseApplication.dataMap.get("openid");
        BaseApplication.iService.newsList(openid).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray newsList = jsonObject.getJSONArray("newsList");
                    JSONArray myNews = jsonObject.getJSONArray("myNews");
                    msgList = new ArrayList<>();
                    for (int i = 0; i < newsList.length(); i++) {
                        JSONObject obj = newsList.getJSONObject(i);
                        int id = obj.getInt("id");
                        String title = obj.getString("title");
                        String content = obj.getString("content");
                        String date = obj.getString("date");
                        PushMessage pm = new PushMessage(id, title, content, date);
                        msgList.add(pm);
                    }
                    for (int j = 0;j<myNews.length();j++){
                        JSONObject obj = myNews.getJSONObject(j);
                        int id = obj.getInt("id");
                        String title = obj.getString("title");
                        String content = obj.getString("content");
                        String date = obj.getString("date");
                        PushMessage pm = new PushMessage(id, title, content, date);
                        msgList.add(pm);
                    }
                    Collections.sort(msgList, new Comparator<PushMessage>() {
                        @Override
                        public int compare(PushMessage o1, PushMessage o2) {
                            return o1.getDate().compareTo(o2.getDate())>0?-1:1;
                        }
                    });
                    MsgArrayAdapter adapter = new MsgArrayAdapter(getBaseContext(), R.layout.item_msg, msgList);
                    msgListView.setAdapter(adapter);
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getBaseContext(),e);
                }
            }
        }));
    }
}
