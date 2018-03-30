package com.jhobor.ddc.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jhobor.ddc.R;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.entity.Store;
import com.jhobor.ddc.utils.ErrorUtil;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class StoreInfoActivity extends TakePhotoActivity implements View.OnClickListener {
    ImageView topArrow, storePic;
    TextView topTitle, storeName;
    LinearLayout storePicBox, storeNameBox, accountBox;
    PopupWindow pop;

    Store store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info);

        initView();
        handleEvt();
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);
        storePicBox.setOnClickListener(this);
        storeNameBox.setOnClickListener(this);
    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        storePic = (ImageView) findViewById(R.id.storePic);
        topTitle = (TextView) findViewById(R.id.topTitle);
        storeName = (TextView) findViewById(R.id.storeName);
        storePicBox = (LinearLayout) findViewById(R.id.storePicBox);
        storeNameBox = (LinearLayout) findViewById(R.id.storeNameBox);
        accountBox = (LinearLayout) findViewById(R.id.accountBox);

        topTitle.setText("店铺信息");
        accountBox.setVisibility(View.GONE);
        store = (Store) BaseApplication.dataMap.get("store");
        storeName.setText(store.getName());
        Glide.with(getApplicationContext())
                .load(store.getPicture())
                .error(R.mipmap.load_img_fail)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(storePic);
    }

    @Override
    public void onClick(View v) {
        if (v == topArrow) {
            finish();
        } else if (v == storePicBox) {
            popAndSet();
        } else if (v == storeNameBox) {
            Intent intent = new Intent(this, NicknameActivity.class);
            intent.putExtra("nickName", store.getName());
            intent.putExtra("type", "storeName");
            startActivity(intent);
        } else if (v.getId() == R.id.useGallery) {
            TakePhoto takePhoto = getTakePhoto();
            takePhoto.onPickFromGallery();
            pop.dismiss();
        } else if (v.getId() == R.id.useCamera) {
            TakePhoto takePhoto = getTakePhoto();
            Uri uri = Uri.fromFile(new File(getCacheDir(), "temp.jpg"));
            takePhoto.onPickFromCapture(uri);
            pop.dismiss();
        } else if (v.getId() == R.id.popWin) {
            pop.dismiss();
        }
    }

    @Override
    protected void onResume() {
        Object nickName = BaseApplication.dataMap.get("nickName");
        if (nickName != null) {
            storeName.setText((CharSequence) nickName);
            store.setName((String) nickName);
            BaseApplication.dataMap.remove("nickName");
        }
        super.onResume();
    }

    private void popAndSet() {
        FrameLayout layout = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.select_pictures_pop_window, (ViewGroup) findViewById(R.id.popWin));
        layout.findViewById(R.id.useGallery).setOnClickListener(this);
        layout.findViewById(R.id.useCamera).setOnClickListener(this);
        layout.findViewById(R.id.popWin).setOnClickListener(this);
        pop = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        pop.showAtLocation(findViewById(R.id.activity_store_info), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    public void takeSuccess(TResult result) {
        TImage image = result.getImage();
        String originalPath = image.getOriginalPath();
        Log.i(">>", originalPath);
        storePic.setImageURI(Uri.parse(originalPath));

        String uuid = (String) BaseApplication.dataMap.get("token");
        File file = new File(originalPath);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("uuid", uuid)
                .addFormDataPart("storePic", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        List<MultipartBody.Part> parts = builder.build().parts();
        BaseApplication.iService.uploadStorePicture(parts).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int msg = jsonObject.getInt("msg");
                    if (msg == 1) {
                        String picture = BaseApplication.BASE_URL + jsonObject.getString("storePic");
                        store.setPicture(picture);
                        Toast.makeText(StoreInfoActivity.this, "图片上传成功", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                }
            }
        }));

        super.takeSuccess(result);
    }
}
