package com.jhobor.ddc.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.jhobor.ddc.R;
import com.jhobor.ddc.adapter.StoreCommentsBaseAdapter;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.entity.Comment;
import com.jhobor.ddc.entity.Pager;
import com.jhobor.ddc.utils.ErrorUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreCommentFragment extends Fragment implements AbsListView.OnScrollListener {
    View view;
    TextView holisticScoring, reputationScore, brief;
    ListView listView;

    int storeId;
    Pager pager;
    int creditScore;
    int level;
    List<Comment> commentList = new ArrayList<>();
    StoreCommentsBaseAdapter adapter;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            storeId = intent.getIntExtra("storeId", 0);
            getCommentData();
        }
    };

    public StoreCommentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getContext().registerReceiver(receiver, new IntentFilter("showStoreComments"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initView(inflater, container);
        handleEvt();
        if (adapter != null) {
            holisticScoring.setText(String.valueOf(level));
            reputationScore.setText(String.valueOf(creditScore));
            listView.setAdapter(adapter);
        }

        return view;
    }

    private void handleEvt() {
        listView.setOnScrollListener(this);
    }

    private void initView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_store_comment, container, false);
        holisticScoring = (TextView) view.findViewById(R.id.holisticScoring);
        reputationScore = (TextView) view.findViewById(R.id.reputationScore);
        brief = (TextView) view.findViewById(R.id.brief);
        listView = (ListView) view.findViewById(R.id.listView);

    }

    private void getCommentData() {
        BaseApplication.iService.storeComments(storeId).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    creditScore = jsonObject.getInt("creditScore");
                    level = jsonObject.getInt("level");
                    holisticScoring.setText(String.valueOf(level));
                    reputationScore.setText(String.valueOf(creditScore));
                    parseJson(jsonObject);
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getContext(), e);
                }
            }
        }));
    }

    private void parseJson(JSONObject jsonObject) throws JSONException {
        JSONArray evaluateList = jsonObject.getJSONArray("evaluateList");
        int len = evaluateList.length();
        for (int i = 0; i < len; i++) {
            JSONObject jo = evaluateList.getJSONObject(i);
            int id = jo.getInt("id");
            double score = jo.getDouble("score");
            String content = jo.getString("content");
            String evaDate = jo.getString("evaDate");
            JSONObject userInfo = jo.getJSONObject("userInfo");
            JSONArray evaPictures = jo.getJSONArray("evaPictures");
            int uid = userInfo.getInt("id");
            String userName = userInfo.getString("name");
            String gravatar = BaseApplication.BASE_URL + userInfo.getString("gravatar");
            int size = evaPictures.length();
            List<String> pictureList = new ArrayList<String>(size);
            for (int j = 0; j < size; j++) {
                String commentPic = BaseApplication.BASE_URL + evaPictures.getString(j);
                pictureList.add(commentPic);
            }
            // int id, int userId, String userName, String userPic, String content, String time, List<String> pictureList
            commentList.add(new Comment(id, uid, userName, gravatar, content, evaDate, pictureList));
        }
        if (adapter == null) {
            adapter = new StoreCommentsBaseAdapter(commentList, getContext());
            listView.setAdapter(adapter);
            pager = new Pager(10);
        } else {
            //adapter.setCommentList(commentList);
            adapter.notifyDataSetChanged();
        }
        if (len < pager.getPageLen()) {
            pager.setNoMoreData(true);
        }
    }

    @Override
    public void onDestroy() {
        getContext().unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        int position = listView.getLastVisiblePosition();
        if (position == commentList.size() - 1 && !pager.isNoMoreData()) {
            pager.setPage(pager.getPage() + 1);
            getPageComments();
        }
    }

    private void getPageComments() {
        BaseApplication.iService.storePageComments(storeId, pager.getPage()).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    parseJson(jsonObject);
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getContext(), e);
                }
            }
        }));
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
