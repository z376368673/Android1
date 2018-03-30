package com.jhobor.zzb;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.jhobor.zzb.adapter.FamilyStoreAdapter;
import com.jhobor.zzb.base.BaseActivity;
import com.jhobor.zzb.entity.FamilyStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GetMaterialActivity extends BaseActivity implements View.OnClickListener {
    ImageView back,share;
    TextView target,myCard;
    Spinner province,city;
    ListView listView;
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_material);

        back = (ImageView) findViewById(R.id.back);
        share = (ImageView) findViewById(R.id.share);
        target = (TextView) findViewById(R.id.target);
        myCard = (TextView) findViewById(R.id.myCard);
        province = (Spinner) findViewById(R.id.province);
        city = (Spinner) findViewById(R.id.city);
        listView = (ListView) findViewById(R.id.listView);
        send = (Button) findViewById(R.id.send);

        share.setVisibility(View.INVISIBLE);
        myCard.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        back.setOnClickListener(this);
        myCard.setOnClickListener(this);
        send.setOnClickListener(this);
        String[] provinceNames = {
                "广东省",
                "广西",
                "湖南省",
                "江西省",
                "福建省",
                "浙江省",
                "安徽省",
                "湖北省",
                "重庆",
                "贵州省",
                "云南省",
                "四川省",
                "陕西省",
                "河南省",
                "江苏省",
                "上海",
                "山东省",
                "河北省",
                "山西省",
                "宁夏",
                "甘肃省",
                "青海省",
                "内蒙古",
                "北京",
                "辽宁省",
                "吉林省",
                "黑龙江省",
                "新疆",
                "西藏",
        };
        String[] cityNames = {"罗定市","深圳市","广州市","上海市","北京市"};
        List<Map<String,Object>> provinceData = new ArrayList<>();
        for (String str : provinceNames) {
            Map<String, Object> map = new HashMap<>();
            map.put("province", str);
            provinceData.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), provinceData, R.layout.item_spinner_place, new String[]{"province"}, new int[]{R.id.place});
        province.setAdapter(simpleAdapter);
        List<Map<String,Object>> cityData = new ArrayList<>();
        for (String str : cityNames) {
            Map<String, Object> map = new HashMap<>();
            map.put("city", str);
            cityData.add(map);
        }
        SimpleAdapter simpleAdapter2 = new SimpleAdapter(getBaseContext(), cityData, R.layout.item_spinner_place, new String[]{"city"}, new int[]{R.id.place});
        city.setAdapter(simpleAdapter2);
        List<FamilyStore> familyStoreList = new ArrayList<>();
        familyStoreList.add(new FamilyStore(1,"广东","深圳","光明新区","福成街道","家装馆建材市场11号铺",2));
        familyStoreList.add(new FamilyStore(2,"广东","深圳","光明新区","福成街道","家装馆建材市场11号铺",3));
        familyStoreList.add(new FamilyStore(3,"广东","深圳","光明新区","福成街道","家装馆建材市场11号铺",2));
        familyStoreList.add(new FamilyStore(4,"广东","深圳","光明新区","福成街道","家装馆建材市场11号铺",5));
        FamilyStoreAdapter familyStoreAdapter = new FamilyStoreAdapter(this, familyStoreList);
        listView.setAdapter(familyStoreAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v==back){
            finish();
        }else if (v==send){
            LinearLayout container = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_round_grid, null, false);
            View itemView = getLayoutInflater().inflate(R.layout.item_dialog_property, null, false);
            TextView key = (TextView) itemView.findViewById(R.id.key);
            TextView value = (TextView) itemView.findViewById(R.id.value);
            key.setText("联系方式");
            value.setText(String.format(Locale.CHINA,"QQ：%s","1434129383"));
            container.addView(itemView);
            final AlertDialog dialog = new AlertDialog.Builder(this)
                    .setView(container)
                    .setCancelable(false)
                    .show();
            container.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }else if (v==myCard){
            startActivity(new Intent(this,VCardActivity.class));
        }
    }
}
