package com.jhobor.ddc.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jhobor.ddc.R;
import com.jhobor.ddc.adapter.GoodsCategoryBaseAdapter;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.utils.ErrorUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GoodsCategoryActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener {
    ImageView topArrow;
    TextView topTitle;
    EditText category;
    Button ok;
    GridView gridView;

    List<String> categoryArrayList = new ArrayList<>();
    List<Integer> categoryIds = new ArrayList<>();
    BaseAdapter baseAdapter;
    String uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_category);

        initView();
        handleEvt();
        getCategorysData();
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);
        ok.setOnClickListener(this);
        gridView.setOnItemLongClickListener(this);
    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        topTitle = (TextView) findViewById(R.id.topTitle);
        category = (EditText) findViewById(R.id.category);
        ok = (Button) findViewById(R.id.ok);
        gridView = (GridView) findViewById(R.id.gridView);

        topTitle.setText("产品分类");

    }

    private void getCategorysData() {
        uuid = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.storeCategorys(uuid).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int msg = jsonObject.getInt("msg");
                    if (msg == 1) {
                        JSONArray categoryList = jsonObject.getJSONArray("categoryList");
                        for (int i = 0; i < categoryList.length(); i++) {
                            JSONArray jsonArray = categoryList.getJSONArray(i);
                            int id = jsonArray.getInt(0);
                            String name = jsonArray.getString(1);
                            categoryArrayList.add(name);
                            categoryIds.add(id);
                        }
                        baseAdapter = new GoodsCategoryBaseAdapter(categoryArrayList, GoodsCategoryActivity.this);
                        gridView.setAdapter(baseAdapter);
                    }
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
        } else if (v == ok) {
            final String s = category.getText().toString().trim();
            if (!s.isEmpty()) {
                for (int i = 0; i < categoryArrayList.size(); i++) {
                    if (s.equals(categoryArrayList.get(i))) {
                        Toast.makeText(this, String.format(Locale.CHINA, "分类【%s】已经存在，不用重复添加", s), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                BaseApplication.iService.addStoreCategory(uuid, s).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                    @Override
                    public void parse(String data) {
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            int msg = jsonObject.getInt("msg");
                            if (msg == 1) {
                                int categoryId = jsonObject.getInt("categoryId");
                                categoryIds.add(categoryId);
                                categoryArrayList.add(s);
                                baseAdapter.notifyDataSetChanged();
                                category.setText("");
                                category.clearFocus();
                                Toast.makeText(GoodsCategoryActivity.this, "分类添加成功", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                        }
                    }
                }));

            } else {
                Toast.makeText(this, "无效产品分类", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("删除")
                .content(String.format(Locale.CHINA, "确认要删除【%s】？", categoryArrayList.get(position)))
                .positiveText("删除")
                .positiveColor(ContextCompat.getColor(getBaseContext(), R.color.redPress))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        BaseApplication.iService.delCategory(uuid, categoryIds.get(position)).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                            @Override
                            public void parse(String data) {
                                try {
                                    JSONObject jsonObject = new JSONObject(data);
                                    int msg = jsonObject.getInt("msg");
                                    if (msg == 1) {
                                        Toast.makeText(GoodsCategoryActivity.this, String.format(Locale.CHINA, "已删除分类【%s】", categoryArrayList.get(position)), Toast.LENGTH_SHORT).show();
                                        categoryIds.remove(position);
                                        categoryArrayList.remove(position);
                                        baseAdapter.notifyDataSetChanged();
                                    }
                                } catch (JSONException e) {
                                    ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                                }
                            }
                        }));
                    }
                })
                .negativeText("取消")
                .negativeColor(ContextCompat.getColor(getBaseContext(), R.color.blackGray))
                .show();

        return false;
    }
}
