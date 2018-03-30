package com.jhobor.fortune;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.fortune.utils.BarUtil;
import com.jhobor.fortune.utils.HideIMEUtil;

public class ManagePropertyActivity extends AppCompatActivity implements View.OnClickListener {
    TextView money;
    Button ok;

    int capital;
    String[] items = {
            "投资金额1000(人民币)",
            "投资金额3000(人民币)",
            "投资金额5000(人民币)",
            "投资金额10000(人民币)",
            "投资金额30000(人民币)",
            "投资金额50000(人民币)",
            "投资金额100000(人民币)",
            "投资金额200000(人民币)"
    };
    int[] itemMoney = {
            1000,
            3000,
            5000,
            10000,
            30000,
            50000,
            100000,
            200000
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_property);
        HideIMEUtil.wrap(this);

        money = (TextView) findViewById(R.id.money);
        ok = (Button) findViewById(R.id.ok);

        BarUtil.topBar(this, "理财");
        money.setOnClickListener(this);
        ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this,"此功能暂未开放",Toast.LENGTH_SHORT).show();

        /*if (v == money) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.dialog_choose_capital, (ViewGroup) findViewById(R.id.dialog_choose_capital), false);
            ListView listView = (ListView) view.findViewById(R.id.listView);
            listView.setAdapter(new ChooseCapitalAdapter(this, items));
            final AlertDialog dialog = new AlertDialog.Builder(this)
                    .setView(view)
                    .create();
            dialog.setCancelable(true);
            dialog.show();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    dialog.dismiss();
                    capital = itemMoney[position];
                    money.setText(items[position].substring(4));
                }
            });
        } else if (v == ok) {
            if (capital == 0)
                return;
            String token = (String) BaseApplication.dataMap.get("token");
            BaseApplication.iService.manageProperty(token, capital).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                @Override
                public void parse(String data) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        jsonObject.getInt("isLogin");
                        int isLogin = jsonObject.getInt("isLogin");
                        if (isLogin == 1) {
                            Toast.makeText(ManagePropertyActivity.this, "提交成功，进入审核阶段", Toast.LENGTH_LONG).show();
                            BaseApplication.dataMap.put("reload", true);
                            finish();
                        } else {
                            Toast.makeText(ManagePropertyActivity.this, "未登录，获取数据失败", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        ErrorUtil.retrofitResponseParseFail(ManagePropertyActivity.this, e);
                    }
                }
            }));
        }*/
    }
}
