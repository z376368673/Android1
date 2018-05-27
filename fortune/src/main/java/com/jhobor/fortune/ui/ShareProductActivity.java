package com.jhobor.fortune.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.jhobor.fortune.R;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.entity.ShareProduct;
import com.jhobor.fortune.utils.BarUtil;
import com.jhobor.fortune.utils.BitmapUtlis;
import com.jhobor.fortune.utils.HideIMEUtil;
import com.jhobor.fortune.view.ImagePopWindow;
import com.vincent.filepicker.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Author     分享 产品页
 * Created by YQJ on 2018/5/20.
 * Description:
 */
public class ShareProductActivity extends BaseActivity implements AdapterView.OnItemClickListener{
    MyAdapter adapter;
    private GridView gridView;
    private ImageView mShare_share;

    private TextView tv_phone;
    private TextView tv_yaoqing;
    Context context;

    ImagePopWindow imagePopWindow ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_product);
        context = this;
        HideIMEUtil.wrap(this);
        BarUtil.topBar(this, "分享");
        BarUtil.topBarRight(this, false, " ");
        mShare_share = (ImageView) findViewById(R.id.share_share);
        mShare_share.setOnClickListener(this);
        gridView = (GridView) findViewById(R.id.gridView);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_yaoqing = (TextView) findViewById(R.id.tv_yaoqing);
        //tv_yaoqing.setOnClickListener(this);
        adapter = new MyAdapter(context);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
        imagePopWindow = new ImagePopWindow(this);
        getData();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view==mShare_share) {
            Intent intent1 = new Intent(Intent.ACTION_SEND);
            intent1.putExtra(Intent.EXTRA_TEXT, "通众分享：\n"+BaseApplication.BASE_URL+"product/product.jsp");
            intent1.setType("text/plain");
            startActivity(Intent.createChooser(intent1, "分享"));
        }

    }
    private void getData() {
        dialog.show();
        String uuid = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.shareProduct(uuid).enqueue(new RetrofitCallback(this, new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int msg = jsonObject.optInt("msg");
                    if (msg == 1) {
                        List<ShareProduct> shareProductList = JSON.parseArray(jsonObject.getString("productList"),ShareProduct.class);
                        if (shareProductList!=null&&shareProductList.size()>0)
                        tv_phone.setText("联系方式:"+shareProductList.get(0).getTelPhone());
                        adapter.clear();
                        adapter.addAll(shareProductList);
                        adapter.notifyDataSetChanged();
                    } else {
                        data =  jsonObject.getString("errorInfo");
                        Toast.makeText(ShareProductActivity.this, data, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    dialog.dismiss();
                }
            }
        }));
    }

    /**
     * 邀请好友
     * @param view
     */
    public void yaoQing(View view) {
        startAct(ShareActivity.class);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (imagePopWindow.isShowing()){
            imagePopWindow.dismiss();
            return;
        }
        List list  = new ArrayList();
        ShareProduct path  = (ShareProduct) parent.getAdapter().getItem(position);
        list.add(path.getImgUrl());
        imagePopWindow.setData(list);
        imagePopWindow.setCurrentItem(0);
        imagePopWindow.showPopupWindow(mShare_share);
    }


    private class MyAdapter extends ArrayAdapter<ShareProduct>{

        public MyAdapter(@NonNull Context context) {
            super(context, 0);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView==null)
            convertView =   View.inflate(act,R.layout.activity_share_product_item,null);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_product);
            TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            TextView tv_price = (TextView) convertView.findViewById(R.id.tv_price);

            ShareProduct product = getItem(position);
            tv_name.setText(product.getTitle());
            tv_price.setText(String.format(tv_price.getText().toString(),product.getPrice()));
            Glide.with(context).load(product.getImgUrl()).into(imageView);
            return convertView;
        }
    }

}
