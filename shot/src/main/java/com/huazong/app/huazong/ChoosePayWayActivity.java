package com.huazong.app.huazong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.huazong.app.huazong.base.BarUtil;

public class ChoosePayWayActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public static final int PAY_WITH_ALIPAY = 1;
    public static final int PAY_WITH_WE_CHAT = 2;
    public static final int PAY_WITH_MEMBER_WALLET = 3;

    ListView payway;

    String[] payTxtList = {"支付宝支付", "微信支付", "会员支付"};
    int[] payIconList = {R.mipmap.payway_ali, R.mipmap.payway_wechat, R.mipmap.payway_member};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_choose_pay_way);

        payway = (ListView) findViewById(R.id.payway);

        BarUtil.topBar(this,"选择支付方式");
        MyPayArrayAdapter adapter = new MyPayArrayAdapter(this, R.layout.item_pay_way, payTxtList);
        payway.addHeaderView(new ViewStub(this));
        payway.addFooterView(new ViewStub(this));
        payway.setAdapter(adapter);
        payway.setOnItemClickListener(this);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "请选择一种支付方式进行支付", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(ChoosePayWayActivity.this, StoreDetailActivity.class);
        // 因为listview加入了头部和尾部的分割线，所以position的值不是item所在的位置
        intent.putExtra("way", (int)id + 1);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private class MyPayArrayAdapter extends ArrayAdapter<String> {
        int resource;
        LayoutInflater inflater;

        MyPayArrayAdapter(Context context, int resource, String[] strings) {
            super(context, resource, strings);
            this.resource = resource;
            inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(resource, parent, false);
                holder.iv = (ImageView) convertView.findViewById(R.id.payIcon);
                holder.tv = (TextView) convertView.findViewById(R.id.payTxt);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv.setText(getItem(position));
            holder.iv.setImageResource(payIconList[position]);
            return convertView;
        }

        class ViewHolder {
            ImageView iv;
            TextView tv;
        }
    }
}
