package com.jhobor.fortune.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.jhobor.fortune.R;
import com.jhobor.fortune.adapter.LowerAdapter;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.entity.LowerLevel;
import com.jhobor.fortune.utils.ErrorUtil;
import com.jhobor.fortune.utils.TabUtil;
import com.vincent.filepicker.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

/**
 *
 * 下属团队列表
 *
 * Created by zh on 2018/5/8.
 *
 */

public class TeamSubordinateActivity extends BaseActivity {


    RelativeLayout oneBox, twoBox;
    LinearLayout ll1,ll2,one_ll,two_ll;
    TextView oneLevel, twoLevel, oneText, twoText;
    RecyclerView recyclerView,recyclerView1;
    ImageView arrow1,arrow2;
    TextView p1,p2;
    boolean isAFloded = false; //表示不折叠
    boolean isBFloded = false;

    SparseArray<List<LowerLevel>> lowerList = new SparseArray<>();
    LowerAdapter lowerAdapter,lowerAdapter1;
    int tab = 0;
    TabUtil tabUtil;
    int childrenCount, grandsonCount;
    String title,phone;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_team_subordinate);
        initTitle();
        title  = getIntent().getStringExtra("name");
        phone  = getIntent().getStringExtra("phone");
        if (!TextUtils.isEmpty(title)){
            titleView.setText(title);
        }
//        if (!TextUtils.isEmpty(phone)){
//            ToastUtil.getInstance(act).showToast("该账号不存在");
//        }
        initView();
    }

    private void initView() {
        oneBox = (RelativeLayout)  findViewById(R.id.oneBox);
        twoBox = (RelativeLayout)  findViewById(R.id.twoBox);
        oneText = (TextView)  findViewById(R.id.oneText);
        oneLevel = (TextView)  findViewById(R.id.oneLevel);
        twoText = (TextView)  findViewById(R.id.twoText);
        twoLevel = (TextView)  findViewById(R.id.twoLevel);
        recyclerView = (RecyclerView)  findViewById(R.id.recyclerView);
        recyclerView1 = (RecyclerView)  findViewById(R.id.recyclerView1);
        arrow1 = (ImageView)  findViewById(R.id.down_one);
        arrow2 = (ImageView)  findViewById(R.id.down_two);
        ll1 = (LinearLayout)  findViewById(R.id.team_ll1);
        ll2 = (LinearLayout)  findViewById(R.id.team_ll2);
        one_ll = (LinearLayout)  findViewById(R.id.one_ll);
        one_ll.setTag(1);
        two_ll = (LinearLayout)  findViewById(R.id.two_ll);
        two_ll.setTag(0);
        p1 = (TextView)  findViewById(R.id.people_1);
        p2 = (TextView)  findViewById(R.id.people_2);

        if (lowerList.size() == 0) {
            getData();
        } else {
            setData();
        }
    }


    private void setData() {
        if (tabUtil != null){
            tabUtil = new TabUtil(ContextCompat.getColor( act, R.color.tabNormal), ContextCompat.getColor( act, R.color.tabActived), new TextView[][]{{oneLevel, oneText}, {twoLevel, twoText}});
            tabUtil.change(tab);
        }
        //oneLevel.setText(String.format(Locale.CHINA, "%d人", childrenCount));
        //twoLevel.setText(String.format(Locale.CHINA, "%d人", grandsonCount));
        p1.setText(String.format(Locale.CHINA, "%d人", childrenCount));
        p2.setText(String.format(Locale.CHINA, "%d人", grandsonCount));

        LinearLayoutManager layoutManager = new LinearLayoutManager( act);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(lowerAdapter);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager( act);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.setAdapter(lowerAdapter1);

        oneBox.setOnClickListener(this);
        twoBox.setOnClickListener(this);
    }

    private void getData() {
        dialog.show();
        String token = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.queryLowerLevel(token,phone).enqueue(new RetrofitCallback( act, new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int msg = jsonObject.getInt("msg");
                    if (msg == 1) {
                        lowerList.clear();
                        JSONArray childrenList = jsonObject.getJSONArray("childrenList");
                        JSONArray grandsonList = jsonObject.getJSONArray("grandsonList");
                        childrenCount = childrenList.length();
                        grandsonCount = grandsonList.length();

                        List<LowerLevel> list = JSON.parseArray(childrenList.toString(),LowerLevel.class);
                        lowerList.put(0, list);

                        List<LowerLevel> list2 =  JSON.parseArray(grandsonList.toString(),LowerLevel.class);
                        lowerList.put(1, list2);

                        List<LowerLevel> lowerLevels = lowerList.get(0);
                        if (lowerLevels.size() > 0){
                            ll1.setVisibility(View.VISIBLE);
                            isAFloded = false;
                            lowerAdapter = new LowerAdapter(R.layout.item_lower_level, lowerList.get(0));
                        }else {
                            ll1.setVisibility(View.GONE);
                            isAFloded = true;
                        }

                        List<LowerLevel> lowerLevels1 = lowerList.get(1);
                        if (lowerLevels1.size() > 0){
                            ll2.setVisibility(View.VISIBLE);
                            isBFloded = false;
                            lowerAdapter1 = new LowerAdapter(R.layout.item_lower_level, lowerList.get(1));
                        }else {
                            isBFloded = true;
                            ll2.setVisibility(View.GONE);
                        }
                        setData();
                    } else {
                        Toast.makeText( act, "账户信息失效，无法获取数据", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail( act, e);
                }finally {
                    dialog.dismiss();
                }
            }
        }));
    }

    @Override
    public void onClick(View v) {
        if (v == oneBox) {
            int tag = (int) one_ll.getTag();
            if (tag == 0) {
                one_ll.setTag(1);
                one_ll.setVisibility(View.VISIBLE);
                arrow1.setImageResource(R.mipmap.down);
            } else {
                one_ll.setTag(0);
                one_ll.setVisibility(View.GONE);
                arrow1.setImageResource(R.mipmap.up);
            }
        } else if (v == twoBox) {
            int tag = (int) two_ll.getTag();
            if (tag == 0) {
                two_ll.setTag(1);
                two_ll.setVisibility(View.VISIBLE);
                arrow2.setImageResource(R.mipmap.down);
            } else {
                two_ll.setTag(0);
                two_ll.setVisibility(View.GONE);
                arrow2.setImageResource(R.mipmap.up);
            }
        }
    }
}
