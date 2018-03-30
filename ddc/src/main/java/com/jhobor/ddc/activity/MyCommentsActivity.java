package com.jhobor.ddc.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.jhobor.ddc.R;
import com.jhobor.ddc.adapter.MyCommentsBaseAdapter;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.entity.Comment;
import com.jhobor.ddc.utils.DisplayUtil;
import com.jhobor.ddc.utils.ErrorUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class MyCommentsActivity extends AppCompatActivity implements View.OnClickListener, XRecyclerView.LoadingListener {
    ImageView topArrow;
    TextView topTitle;
    XRecyclerView xRecyclerView;

    int page = 0;
    MyCommentsBaseAdapter adapter;
    List<Comment> commentList = new ArrayList<Comment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_comments);

        initView();
        getCommentData(page);
    }

    private void getCommentData(int page) {
        String uuid = (String) BaseApplication.dataMap.get("token");
        Call<ResponseBody> call = BaseApplication.iService.myComments(uuid, page);
        call.enqueue(new RetrofitCallback(this, new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    Log.i(">>", data);
                    JSONObject jsonObject = new JSONObject(data);
                    int isLogin = jsonObject.getInt("isLogin");
                    JSONArray evaluateList = jsonObject.getJSONArray("evaluateList");
                    int i = 0;
                    for (; i < evaluateList.length(); i++) {
                        JSONObject object = evaluateList.getJSONObject(i);
                        int id = object.getInt("id");
                        String content = object.getString("content");
                        String evaDate = object.getString("evaDate");
                        JSONObject userInfo = object.getJSONObject("userInfo");
                        int userId = userInfo.getInt("id");
                        String name = userInfo.getString("name");
                        String gravatar = BaseApplication.BASE_URL + userInfo.getString("gravatar");
                        JSONArray evaPictures = object.getJSONArray("evaPictures");
                        JSONObject store = object.getJSONObject("store");
                        String storeName = store.getString("name");
                        List<String> pictureList = new ArrayList<String>();
                        for (int j = 0; j < evaPictures.length(); j++) {
                            String pic = BaseApplication.BASE_URL + evaPictures.getString(j);
                            pictureList.add(pic);
                        }
                        commentList.add(new Comment(id, userId, name, gravatar, content, evaDate, pictureList, storeName));
                    }
                    Log.i("i>>", String.valueOf(i));
                    if (i == 0) {
                        xRecyclerView.setLoadingMoreEnabled(false);
                        Log.i("load more enable>>", String.valueOf(false));
                    }
                    if (adapter == null) {
                        adapter = new MyCommentsBaseAdapter(MyCommentsActivity.this, commentList, R.layout.item_my_comment);
                        xRecyclerView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                        Toast.makeText(MyCommentsActivity.this, "数据加载完成", Toast.LENGTH_SHORT).show();
                    }
                    xRecyclerView.loadMoreComplete();
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(MyCommentsActivity.this, e);
                }
            }
        }));

    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        topTitle = (TextView) findViewById(R.id.topTitle);
        xRecyclerView = (XRecyclerView) findViewById(R.id.xRecyclerView);

        topArrow.setOnClickListener(this);
        topTitle.setText("我的评价");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setLayoutManager(layoutManager);
        // 分割线
        new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        xRecyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration
                        .Builder(this)
                        .size(DisplayUtil.dip2px(this, 5))
                        .build()
        );
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setLoadingListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == topArrow) {
            finish();
        }
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        getCommentData(++page);
    }
}
