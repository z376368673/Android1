package com.huazong.app.huazong;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.huazong.app.huazong.adapter.RankArrayAdapter;
import com.huazong.app.huazong.base.BarUtil;
import com.huazong.app.huazong.base.BaseApplication;
import com.huazong.app.huazong.base.RetrofitCallback;
import com.huazong.app.huazong.entity.Rank;
import com.huazong.app.huazong.utils.ErrorUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RankActivity extends AppCompatActivity implements View.OnClickListener {
    TextView rankInFriends, rankInWorld;
    ListView rankView;

    List<Rank> rankWorldList, rankFriendList;
    RankArrayAdapter worldRankAdapter, friendRankAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_rank);

        rankView = (ListView) findViewById(R.id.rankView);
        rankInFriends = (TextView) findViewById(R.id.rankInFriends);
        rankInWorld = (TextView) findViewById(R.id.rankInWorld);

        rankInFriends.setOnClickListener(this);
        rankInWorld.setOnClickListener(this);
        Intent intent = getIntent();
        BarUtil.topBar(this,intent.getStringExtra("title"));
        getData();
    }

    private void getData() {
        String openid = (String) BaseApplication.dataMap.get("openid");
        int arrowOrGun = (int) BaseApplication.dataMap.get("arrowOrGun");
        BaseApplication.iService.rankList(openid,arrowOrGun).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray ranklist = jsonObject.getJSONArray("ranklist");
                    JSONArray friendList = jsonObject.getJSONArray("friendList");
                    JSONArray myRanklist = jsonObject.getJSONArray("myRanklist");
                    int length = ranklist.length();
                    rankWorldList = new ArrayList<>();
                    rankFriendList = new ArrayList<>();
                    for (int i = 0; i < length; i++) {
                        JSONArray jsonArray = ranklist.getJSONArray(i);
                        int score = jsonArray.getInt(0);
                        int depth = jsonArray.getInt(1);
                        String name = jsonArray.getString(2);
                        String picture = jsonArray.getString(3);
                        Rank rank = new Rank(picture, name, depth, score);
                        rankWorldList.add(rank);
                    }
                    for (int i = 0; i < friendList.length(); i++) {
                        JSONArray jsonArray = friendList.getJSONArray(i);
                        int score = jsonArray.getInt(0);
                        int depth = jsonArray.getInt(1);
                        String name = jsonArray.getString(2);
                        String picture = jsonArray.getString(3);
                        Rank rank = new Rank(picture, name, depth, score);
                        rankFriendList.add(rank);
                    }
                    int score = myRanklist.getInt(0);
                    int depth = myRanklist.getInt(1);
                    String name = myRanklist.getString(2);
                    String picture = myRanklist.getString(3);
                    Rank rank = new Rank(picture, name, depth, score);

                    boolean flag = false;
                    for (int i = 0; i < rankFriendList.size(); i++) {
                        if (score >= rankFriendList.get(i).getScore()) {
                            rankFriendList.add(i, rank);
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        rankFriendList.add(rank);
                    }
                    worldRankAdapter = new RankArrayAdapter(getBaseContext(), R.layout.item_rank, rankWorldList);
                    friendRankAdapter = new RankArrayAdapter(getBaseContext(), R.layout.item_rank, rankFriendList);
                    rankView.setAdapter(worldRankAdapter);
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getBaseContext(),e);
                }
            }
        }));
    }

    @Override
    public void onClick(View view) {
        if (view == rankInFriends) {
            rankInFriends.setBackgroundResource(R.mipmap.rank_type);
            rankInFriends.setTextColor(ContextCompat.getColor(this, R.color.green_text_color));
            rankInWorld.setTextColor(ContextCompat.getColor(this, R.color.second_text_color));
            rankInWorld.setBackgroundColor(Color.TRANSPARENT);
            rankView.setAdapter(friendRankAdapter);
        } else if (view == rankInWorld) {
            rankInWorld.setBackgroundResource(R.mipmap.rank_type);
            rankInWorld.setTextColor(ContextCompat.getColor(this, R.color.green_text_color));
            rankInFriends.setTextColor(ContextCompat.getColor(this, R.color.second_text_color));
            rankInFriends.setBackgroundColor(Color.TRANSPARENT);
            rankView.setAdapter(worldRankAdapter);
        }
    }
}
