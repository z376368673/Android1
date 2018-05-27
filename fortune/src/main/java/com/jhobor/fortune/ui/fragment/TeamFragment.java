package com.jhobor.fortune.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jhobor.fortune.R;
import com.jhobor.fortune.adapter.LowerAdapter;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.dialog.LoadingDialog;
import com.jhobor.fortune.entity.LowerLevel;
import com.jhobor.fortune.ui.TeamSubordinateActivity;
import com.jhobor.fortune.utils.ErrorUtil;
import com.jhobor.fortune.utils.TabUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeamFragment extends Fragment implements View.OnClickListener {
    View view;
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

    public TeamFragment() {
        // Required empty public constructor

    }
    LoadingDialog dialog ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_team, container, false);
        dialog = new LoadingDialog(getContext());
        oneBox = (RelativeLayout) view.findViewById(R.id.oneBox);
        twoBox = (RelativeLayout) view.findViewById(R.id.twoBox);
        oneText = (TextView) view.findViewById(R.id.oneText);
        oneLevel = (TextView) view.findViewById(R.id.oneLevel);
        twoText = (TextView) view.findViewById(R.id.twoText);
        twoLevel = (TextView) view.findViewById(R.id.twoLevel);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView1 = (RecyclerView) view.findViewById(R.id.recyclerView1);
        arrow1 = (ImageView) view.findViewById(R.id.down_one);
        arrow2 = (ImageView) view.findViewById(R.id.down_two);
        ll1 = (LinearLayout) view.findViewById(R.id.team_ll1);
        ll2 = (LinearLayout) view.findViewById(R.id.team_ll2);
        one_ll = (LinearLayout) view.findViewById(R.id.one_ll);
        one_ll.setTag(1);
        two_ll = (LinearLayout) view.findViewById(R.id.two_ll);
        two_ll.setTag(0);
        p1 = (TextView) view.findViewById(R.id.people_1);
        p2 = (TextView) view.findViewById(R.id.people_2);

        if (lowerList.size() == 0) {
            getData();
        } else {
            setData();
        }
        initView();
        return view;
    }

    /**
     *  初始化view
     */
    private void initView(){
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                LowerLevel lowerLevel = (LowerLevel) adapter.getData().get(position);
                Log.e("LowerLevel",""+lowerLevel.getMobile());
                Intent intent = new Intent(getContext(), TeamSubordinateActivity.class);
                intent.putExtra("name",lowerLevel.getName());
                intent.putExtra("phone",lowerLevel.getMobile());
                startActivity(intent);
            }
        });

        recyclerView1.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                LowerLevel lowerLevel = (LowerLevel) adapter.getData().get(position);
                Log.e("LowerLevel1",""+lowerLevel.getMobile());
                Intent intent = new Intent(getContext(), TeamSubordinateActivity.class);
                intent.putExtra("name",lowerLevel.getName());
                intent.putExtra("phone",lowerLevel.getMobile());
                startActivity(intent);
            }
        });
    }

    private void setData() {
        if (tabUtil != null){
            tabUtil = new TabUtil(ContextCompat.getColor(getActivity(), R.color.tabNormal), ContextCompat.getColor(getContext(), R.color.tabActived), new TextView[][]{{oneLevel, oneText}, {twoLevel, twoText}});
            tabUtil.change(tab);
        }

        //oneLevel.setText(String.format(Locale.CHINA, "%d人", childrenCount));
        //twoLevel.setText(String.format(Locale.CHINA, "%d人", grandsonCount));
        p1.setText(String.format(Locale.CHINA, "%d人", childrenCount));
        p2.setText(String.format(Locale.CHINA, "%d人", grandsonCount));



        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(lowerAdapter);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.setAdapter(lowerAdapter1);

        oneBox.setOnClickListener(this);
        twoBox.setOnClickListener(this);

    }

    private void getData() {
        dialog.show();
        String token = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.myLower(token).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
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
                        Toast.makeText(getContext(), "账户信息失效，无法获取数据", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getContext(), e);
                }finally {
                    dialog.dismiss();
                }
            }
        }));
    }

    @Override
    public void onClick(View v) {
        if (v == oneBox){
            int tag = (int) one_ll.getTag();
            if (tag==0){
                one_ll.setTag(1);
                one_ll.setVisibility(View.VISIBLE);
                arrow1.setImageResource(R.mipmap.down);
            }else {
                one_ll.setTag(0);
                one_ll.setVisibility(View.GONE);
                arrow1.setImageResource(R.mipmap.up);
            }
        }else if (v == twoBox){
            int tag = (int) two_ll.getTag();
            if (tag==0){
                two_ll.setTag(1);
                two_ll.setVisibility(View.VISIBLE);
                arrow2.setImageResource(R.mipmap.down);
            }else {
                two_ll.setTag(0);
                two_ll.setVisibility(View.GONE);
                arrow2.setImageResource(R.mipmap.up);
            }
        }
        /*if (v == oneBox) {
            if (tab != 0) {
                tab = 0;
                tabUtil.change(tab);
                lowerAdapter.setNewData(lowerList.get(tab));
//                lowerAdapter.notifyDataSetChanged();
            }
        } else if (v == twoBox) {
            if (tab != 1) {
                tab = 1;
                tabUtil.change(tab);
                lowerAdapter.setNewData(lowerList.get(tab));
//                lowerAdapter.notifyDataSetChanged();
            }
        }*/
//        if (v == oneBox) {
//            if (isAFloded) {
//                //不折叠
//                ll1.setVisibility(view.VISIBLE);
//                arrow1.setImageResource(R.drawable.up);
//                isAFloded = !isAFloded;
//            }else if(!isAFloded){
//                //折叠
//                ll1.setVisibility(view.GONE);
//                arrow1.setImageResource(R.drawable.down);
//                isAFloded = !isAFloded;
//            }
//        } else if (v == twoBox) {
//            if (isBFloded) {
//                ll2.setVisibility(view.VISIBLE);
//                arrow2.setImageResource(R.drawable.up);
//                isBFloded = !isBFloded;
//            }else if(!isBFloded){
//                ll2.setVisibility(view.GONE);
//                arrow2.setImageResource(R.drawable.down);
//                isBFloded = !isBFloded;
//            }
//        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getData();//聚焦时重新获取一遍数据
    }
}
