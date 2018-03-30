package com.jhobor.ddc.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jhobor.ddc.R;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.utils.ErrorUtil;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PublishGoodsActivity extends TakePhotoActivity implements View.OnClickListener {
    ImageView topArrow, smallPhoto, takePhoto1, takePhoto2, takePhoto3, takePhoto4;
    TextView topTitle;
    EditText goodsName, retailPrice, wholesalePrice, wholesaleVolume, stock, description;
    Spinner category;
    Button ok;
    PopupWindow pop;

    List<String> categoryArrayList = new ArrayList<>();
    List<Integer> categoryIds = new ArrayList<>();
    String uuid;
    List<String> pictures = new ArrayList<>(5);
    List<Integer> pictureIds = new ArrayList<>(5);
    int curIndex = -1;
    ImageView curImageView;
    int goodsId;
    boolean isEdit = false;
    boolean refreshGoodsList = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_goods);

        Intent intent = getIntent();
        goodsId = intent.getIntExtra("goodsId", 0);
        isEdit = goodsId > 0;
        uuid = (String) BaseApplication.dataMap.get("token");
        initView();
        handleEvt();
        if (isEdit) {
            getGoodsDetails();
        } else {
            getCategorysData();
        }
    }

    private void getGoodsDetails() {
        BaseApplication.iService.goodsDetails(uuid, goodsId).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int msg = jsonObject.getInt("msg");
                    if (msg == 1) {
                        String showPicture = BaseApplication.BASE_URL + jsonObject.getString("showPicture");
                        pictures.add(showPicture);
                        pictureIds.add(0);
                        double price = jsonObject.getDouble("price");
                        String name = jsonObject.getString("name");
                        JSONArray gpList = jsonObject.getJSONArray("gpList");
                        JSONArray categoryList = jsonObject.getJSONArray("categoryList");
                        int wholesaleCount = jsonObject.getInt("wholesaleCount");
                        int sk = jsonObject.getInt("stock");
                        int categoryId = jsonObject.getInt("categoryId");
                        double wPirce = jsonObject.getDouble("wholesalePirce");
                        for (int i = 0; i < gpList.length(); i++) {
                            JSONArray jsonArray = gpList.getJSONArray(i);
                            int pid = jsonArray.getInt(0);
                            String pic = BaseApplication.BASE_URL + jsonArray.getString(1);
                            pictureIds.add(pid);
                            pictures.add(pic);
                            if (i == 0) {
                                Glide.with(getApplicationContext())
                                        .load(pic)
                                        .error(R.mipmap.load_img_fail)
                                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                        .into(takePhoto1);
                            } else if (i == 1) {
                                Glide.with(getApplicationContext())
                                        .load(pic)
                                        .error(R.mipmap.load_img_fail)
                                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                        .into(takePhoto2);
                            } else if (i == 2) {
                                Glide.with(getApplicationContext())
                                        .load(pic)
                                        .error(R.mipmap.load_img_fail)
                                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                        .into(takePhoto3);
                            } else if (i == 3) {
                                Glide.with(getApplicationContext())
                                        .load(pic)
                                        .error(R.mipmap.load_img_fail)
                                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                        .into(takePhoto4);
                            }
                        }
                        int pos = 0;
                        for (int j = 0; j < categoryList.length(); j++) {
                            JSONArray jsonArray = categoryList.getJSONArray(j);
                            int cid = jsonArray.getInt(0);
                            String categoryName = jsonArray.getString(1);
                            categoryArrayList.add(categoryName);
                            categoryIds.add(cid);
                            if (cid == categoryId) {
                                pos = j;
                            }
                        }
                        goodsName.setText(name);
                        category.setAdapter(new ArrayAdapter<>(PublishGoodsActivity.this, android.R.layout.simple_spinner_dropdown_item, categoryArrayList));
                        category.setSelection(pos);
                        retailPrice.setText(String.valueOf(price));
                        wholesalePrice.setText(String.valueOf(wPirce));
                        wholesaleVolume.setText(String.valueOf(wholesaleCount));
                        stock.setText(String.valueOf(sk));
                        Glide.with(getApplicationContext())
                                .load(showPicture)
                                .error(R.mipmap.load_img_fail)
                                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                .into(smallPhoto);
                    }

                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                }
            }
        }));
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);
        smallPhoto.setOnClickListener(this);
        takePhoto1.setOnClickListener(this);
        takePhoto2.setOnClickListener(this);
        takePhoto3.setOnClickListener(this);
        takePhoto4.setOnClickListener(this);
        ok.setOnClickListener(this);
    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        topTitle = (TextView) findViewById(R.id.topTitle);
        goodsName = (EditText) findViewById(R.id.goodsName);
        retailPrice = (EditText) findViewById(R.id.retailPrice);
        wholesalePrice = (EditText) findViewById(R.id.wholesalePrice);
        wholesaleVolume = (EditText) findViewById(R.id.wholesaleVolume);
        stock = (EditText) findViewById(R.id.stock);
        description = (EditText) findViewById(R.id.description);
        smallPhoto = (ImageView) findViewById(R.id.smallPhoto);
        takePhoto1 = (ImageView) findViewById(R.id.takePhoto1);
        takePhoto2 = (ImageView) findViewById(R.id.takePhoto2);
        takePhoto3 = (ImageView) findViewById(R.id.takePhoto3);
        takePhoto4 = (ImageView) findViewById(R.id.takePhoto4);
        category = (Spinner) findViewById(R.id.category);
        ok = (Button) findViewById(R.id.ok);

        if (isEdit) {
            ok.setText("修改");
            topTitle.setText("编辑产品");
        } else {
            topTitle.setText("发布产品");
        }
    }

    private void getCategorysData() {
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
                        category.setAdapter(new ArrayAdapter<>(PublishGoodsActivity.this, android.R.layout.simple_spinner_dropdown_item, categoryArrayList));
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
        } else if (v == smallPhoto) {
            popAndSet(v, 0);
        } else if (v == takePhoto1) {
            popAndSet(v, 1);
        } else if (v == takePhoto2) {
            popAndSet(v, 2);
        } else if (v == takePhoto3) {
            popAndSet(v, 3);
        } else if (v == takePhoto4) {
            popAndSet(v, 4);
        } else if (v == ok) {
            String goodsNameText = goodsName.getText().toString().trim();
            String retailPriceText = retailPrice.getText().toString().trim();
            String wholesalePriceText = wholesalePrice.getText().toString().trim();
            String wholesaleVolumeText = wholesaleVolume.getText().toString().trim();
            String stockText = stock.getText().toString().trim();
//            String descriptionText = description.getText().toString().trim();
            if (goodsNameText.isEmpty()) {
                Toast.makeText(this, "商品名称不可为空哦", Toast.LENGTH_SHORT).show();
                goodsName.requestFocus();
            } else if (retailPriceText.isEmpty()) {
                Toast.makeText(this, "零售价不正确", Toast.LENGTH_SHORT).show();
                retailPrice.requestFocus();
            } else if (wholesalePriceText.isEmpty()) {
                Toast.makeText(this, "批发价不正确", Toast.LENGTH_SHORT).show();
                wholesalePrice.requestFocus();
            } else if (wholesaleVolumeText.isEmpty()) {
                Toast.makeText(this, "批发数量不正确", Toast.LENGTH_SHORT).show();
                wholesaleVolume.requestFocus();
            } else if (stockText.isEmpty()) {
                Toast.makeText(this, "库存量不正确", Toast.LENGTH_SHORT).show();
                stock.requestFocus();
            }/*else if (descriptionText.isEmpty()){
                Toast.makeText(this,"商品描述不可为空哦",Toast.LENGTH_SHORT).show();
                description.requestFocus();
            }*/ else if (pictures.size() < 2) {
                Toast.makeText(this, "至少上传一张商品小图和一张商品图片", Toast.LENGTH_SHORT).show();
            } else {
                int position = category.getSelectedItemPosition();
                if (isEdit) {
                    BaseApplication.iService.editGoods(
                            uuid, goodsId, goodsNameText, categoryIds.get(position), retailPriceText, wholesalePriceText, wholesaleVolumeText, stockText
                    ).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                        @Override
                        public void parse(String data) {
                            try {
                                JSONObject jsonObject = new JSONObject(data);
                                int msg = jsonObject.getInt("msg");
                                if (msg == 1) {
                                    refreshGoodsList = true;
                                    Toast.makeText(PublishGoodsActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            } catch (JSONException e) {
                                ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                            }
                        }
                    }));
                } else {
                    MultipartBody.Builder build = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("uuid", uuid)
                            .addFormDataPart("name", goodsNameText)
                            .addFormDataPart("price", retailPriceText)
                            .addFormDataPart("wholesalePirce", wholesalePriceText)
                            .addFormDataPart("wholesaleCount", wholesaleVolumeText)
                            .addFormDataPart("stock", stockText)
                            .addFormDataPart("categoryId", String.valueOf(categoryIds.get(position)));
                    for (int i = 0; i < pictures.size(); i++) {
                        File file = new File(pictures.get(i));
                        build.addFormDataPart("goodsPic" + i, file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
                    }
                    List<MultipartBody.Part> parts = build.build().parts();
                    BaseApplication.iService.publishGoods(parts).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                        @Override
                        public void parse(String data) {
                            try {
                                JSONObject jsonObject = new JSONObject(data);
                                int isLogin = jsonObject.getInt("isLogin");
                                int msg = jsonObject.getInt("msg");
                                if (isLogin == 1 && msg == 1) {
                                    goodsName.setText("");
                                    retailPrice.setText("");
                                    wholesalePrice.setText("");
                                    wholesaleVolume.setText("");
                                    stock.setText("");
                                    description.setText("");
                                    pictures.clear();
                                    smallPhoto.setImageResource(R.mipmap.comment_camera);
                                    takePhoto1.setImageResource(R.mipmap.comment_camera);
                                    takePhoto2.setImageResource(R.mipmap.comment_camera);
                                    takePhoto3.setImageResource(R.mipmap.comment_camera);
                                    takePhoto4.setImageResource(R.mipmap.comment_camera);
                                    Toast.makeText(PublishGoodsActivity.this, "产品发布成功", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                            }
                        }
                    }));
                }
            }

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

    private void popAndSet(View v, int index) {
        if (pictures.size() > index - 1) {
            FrameLayout layout = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.select_pictures_pop_window, (ViewGroup) findViewById(R.id.popWin));
            layout.findViewById(R.id.useGallery).setOnClickListener(this);
            layout.findViewById(R.id.useCamera).setOnClickListener(this);
            layout.findViewById(R.id.popWin).setOnClickListener(this);
            pop = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            pop.showAtLocation(findViewById(R.id.activity_publish_goods), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            curIndex = index;
            curImageView = (ImageView) v;
        } else {
            Toast.makeText(this, "请从上往下，从左往右添加图片", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void takeSuccess(TResult result) {
        TImage image = result.getImage();
        final String originalPath = image.getOriginalPath();
        Log.i(">>", originalPath);
        curImageView.setImageURI(Uri.parse(originalPath));
        if (isEdit) {
            File file = new File(originalPath);
            if (curIndex == 0) {
                // 修改商品小图
                MultipartBody build = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("uuid", uuid)
                        .addFormDataPart("goodsId", String.valueOf(goodsId))
                        .addFormDataPart("smallPhoto", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                        .build();
                BaseApplication.iService.updateSmallPhoto(build.parts()).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                    @Override
                    public void parse(String data) {
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            int msg = jsonObject.getInt("msg");
                            if (msg == 1) {
                                refreshGoodsList = true;
                                Toast.makeText(PublishGoodsActivity.this, "图片修改成功", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                        }
                    }
                }));
            } else {
                // 修改图片
                if (pictureIds.size() > curIndex) {
                    MultipartBody build = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("uuid", uuid)
                            .addFormDataPart("gpId", String.valueOf(pictureIds.get(curIndex)))
                            .addFormDataPart("goodsPhoto", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                            .build();
                    BaseApplication.iService.updateGoodsPhoto(build.parts()).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                        @Override
                        public void parse(String data) {
                            try {
                                JSONObject jsonObject = new JSONObject(data);
                                int msg = jsonObject.getInt("msg");
                                if (msg == 1) {
                                    Toast.makeText(PublishGoodsActivity.this, "图片修改成功", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                            }
                        }
                    }));
                } else {
                    // 添加图片
                    MultipartBody build = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("uuid", uuid)
                            .addFormDataPart("goodsId", String.valueOf(goodsId))
                            .addFormDataPart("goodsPhoto", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                            .build();
                    BaseApplication.iService.addGoodsPhoto(build.parts()).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                        @Override
                        public void parse(String data) {
                            try {
                                JSONObject jsonObject = new JSONObject(data);
                                int msg = jsonObject.getInt("msg");
                                if (msg == 1) {
                                    int gpId = jsonObject.getInt("gpId");
                                    pictureIds.add(gpId);
                                    pictures.add(originalPath);
                                    Toast.makeText(PublishGoodsActivity.this, "图片添加成功", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                            }
                        }
                    }));
                }
            }
        } else {
            if (pictures.size() > curIndex) {
                pictures.set(curIndex, originalPath);
            } else {
                pictures.add(originalPath);
            }
        }
        super.takeSuccess(result);
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    protected void onDestroy() {
        if (refreshGoodsList) {
            sendBroadcast(new Intent("refreshGoodsList"));
        }
        super.onDestroy();
    }
}
