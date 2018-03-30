package com.jhobor.ddc.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CommentActivity extends TakePhotoActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    ImageView topArrow, goodsPic, takePhoto1, takePhoto2, takePhoto3, takePhoto4, takePhoto5;
    TextView topTitle;
    EditText content;
    RatingBar fitRatingBar, attitudeRatingBar, senderRatingBar;
    CheckBox anonymous;
    Button ok;

    int ordersId;
    String goodsImg;
    List<String> pictures = new ArrayList<>();
    ImageView curImageView;
    int curIndex = 0;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Intent intent = getIntent();
        ordersId = intent.getIntExtra("ordersId", 0);
        goodsImg = intent.getStringExtra("goodsPic");
        pos = intent.getIntExtra("pos", -1);
        initView();
        handleEvt();
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);
        takePhoto1.setOnClickListener(this);
        takePhoto2.setOnClickListener(this);
        takePhoto3.setOnClickListener(this);
        takePhoto4.setOnClickListener(this);
        takePhoto5.setOnClickListener(this);
        ok.setOnClickListener(this);
    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        goodsPic = (ImageView) findViewById(R.id.goodsPic);
        takePhoto1 = (ImageView) findViewById(R.id.takePhoto1);
        takePhoto2 = (ImageView) findViewById(R.id.takePhoto2);
        takePhoto3 = (ImageView) findViewById(R.id.takePhoto3);
        takePhoto4 = (ImageView) findViewById(R.id.takePhoto4);
        takePhoto5 = (ImageView) findViewById(R.id.takePhoto5);
        topTitle = (TextView) findViewById(R.id.topTitle);
        content = (EditText) findViewById(R.id.content);
        fitRatingBar = (RatingBar) findViewById(R.id.fitRatingBar);
        attitudeRatingBar = (RatingBar) findViewById(R.id.attitudeRatingBar);
        senderRatingBar = (RatingBar) findViewById(R.id.senderRatingBar);
        anonymous = (CheckBox) findViewById(R.id.anonymous);
        ok = (Button) findViewById(R.id.ok);

        topTitle.setText("发表评价");
        Log.i("=================>>", goodsImg);
        Glide.with(this)
                .load(goodsImg)
                .error(R.mipmap.load_img_fail)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(goodsPic);
    }

    @Override
    public void onClick(View v) {
        if (v == topArrow) {
            finish();
        } else if (v == ok) {
            String ct = content.getText().toString();
            int rate = (int) fitRatingBar.getRating();
            if (ct.trim().isEmpty()) {
                Toast.makeText(this, "还没有实质性的评论内容哦！", Toast.LENGTH_SHORT).show();
                content.requestFocus();
            } else if (rate == 0) {
                Toast.makeText(this, "请在【描述相符】处给产品打评分！", Toast.LENGTH_SHORT).show();
            } else {
                String uuid = (String) BaseApplication.dataMap.get("token");
                //String uuid,int ordersId,String content,int score
                MultipartBody.Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("uuid", uuid)
                        .addFormDataPart("ordersId", String.valueOf(ordersId))
                        .addFormDataPart("content", ct)
                        .addFormDataPart("score", String.valueOf(rate));
                for (int i = 0; i < pictures.size(); i++) {
                    String picturePath = pictures.get(i);
                    builder.addFormDataPart("takePicture" + i, picturePath, RequestBody.create(MediaType.parse("image/*"), new File(picturePath)));
                }
                List<MultipartBody.Part> parts = builder.build().parts();
                BaseApplication.iService.comment(parts).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                    @Override
                    public void parse(String data) {
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            int msg = jsonObject.getInt("msg");
                            if (msg == 1) {
                                Toast.makeText(CommentActivity.this, "评论成功！", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent("reloadOrders");
                                sendBroadcast(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                        }
                    }
                }));
            }
        } else if (v == takePhoto1) {
            popAndSet(v, 0);
        } else if (v == takePhoto2) {
            popAndSet(v, 1);
        } else if (v == takePhoto3) {
            popAndSet(v, 2);
        } else if (v == takePhoto4) {
            popAndSet(v, 3);
        } else if (v == takePhoto5) {
            popAndSet(v, 4);
        }
    }

    private void popAndSet(View v, int index) {
        if (pictures.size() > index - 1) {
            PopupMenu popupMenu = new PopupMenu(this, v);
            popupMenu.getMenuInflater().inflate(R.menu.select_picture_popup_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
            curIndex = index;
            curImageView = (ImageView) v;
        } else {
            Toast.makeText(this, "请从左往右添加图片", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        TakePhoto takePhoto = getTakePhoto();
        if (itemId == R.id.fromGallery) {
            takePhoto.onPickFromGallery();
        } else if (itemId == R.id.fromCamera) {
            Uri uri = Uri.fromFile(new File(getCacheDir(), "temp.jpg"));
//            CropOptions cropOptions=new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
//            takePhoto.onPickFromCaptureWithCrop(uri,cropOptions);
            takePhoto.onPickFromCapture(uri);
        }
        return true;
    }

    @Override
    public void takeSuccess(TResult result) {
        TImage image = result.getImage();
        String originalPath = image.getOriginalPath();
        Log.i(">>", originalPath);
        curImageView.setImageURI(Uri.parse(originalPath));
        if (pictures.size() > curIndex) {
            pictures.set(curIndex, originalPath);
        } else {
            pictures.add(originalPath);
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
}
