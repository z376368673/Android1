package com.jhobor.ddc.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.jhobor.ddc.entity.UserInfo;
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

public class ProfileActivity extends TakePhotoActivity implements View.OnClickListener {
    ImageView topArrow, userPic;
    TextView topTitle, userName, account;
    LinearLayout userPicBox, userNameBox;
    PopupWindow pop;

    UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initView();
        handleEvt();
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);
        userPicBox.setOnClickListener(this);
        userNameBox.setOnClickListener(this);
    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        userPic = (ImageView) findViewById(R.id.userPic);
        topTitle = (TextView) findViewById(R.id.topTitle);
        userName = (TextView) findViewById(R.id.userName);
        account = (TextView) findViewById(R.id.account);
        userPicBox = (LinearLayout) findViewById(R.id.userPicBox);
        userNameBox = (LinearLayout) findViewById(R.id.userNameBox);

        topTitle.setText("个人信息");
        userInfo = (UserInfo) BaseApplication.dataMap.get("userInfo");
        Glide.with(this)
                .load(userInfo.getGravatar())
                .error(R.mipmap.load_img_fail)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(userPic);
        userName.setText(userInfo.getName());
    }

    @Override
    public void onClick(View v) {
        if (v == topArrow) {
            finish();
        } else if (v == userPicBox) {
            popAndSet();
        } else if (v == userNameBox) {
            Intent intent = new Intent(this, NicknameActivity.class);
            intent.putExtra("nickName", userInfo.getName());
            intent.putExtra("type", "userName");
            startActivity(intent);
        }  else if (v.getId() == R.id.popWin) {
            pop.dismiss();
        } else if (v.getId() == R.id.useGallery) {
            TakePhoto takePhoto = getTakePhoto();
            takePhoto.onPickFromGallery();
            pop.dismiss();
        } else if (v.getId() == R.id.useCamera) {
            TakePhoto takePhoto = getTakePhoto();
            Uri uri = Uri.fromFile(new File(getCacheDir(), "temp.jpg"));
            takePhoto.onPickFromCapture(uri);
            pop.dismiss();
        }
    }

    @Override
    protected void onResume() {
        Object nickName = BaseApplication.dataMap.get("nickName");
        if (nickName != null) {
            userName.setText((CharSequence) nickName);
            userInfo.setName((String) nickName);
            //BaseApplication.dataMap.put("userInfo",userInfo);
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
        pop.showAtLocation(findViewById(R.id.activity_profile), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    public void takeSuccess(TResult result) {
        TImage image = result.getImage();
        String originalPath = image.getOriginalPath();
        Log.i(">>", originalPath);
        userPic.setImageURI(Uri.parse(originalPath));
        File file = new File(originalPath);
        String uuid = (String) BaseApplication.dataMap.get("token");

        /*上传图文，方法一*/
       /* //构建body
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("uuid", uuid)
                .addFormDataPart("userPic", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                .build();
        Call<ResponseBody> call = userService.uploadUserPicture(requestBody);*/

         /*上传图文，方法二*/
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)//表单类型
                .addFormDataPart("uuid", uuid)
                .addFormDataPart("userPic", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        List<MultipartBody.Part> partList = builder.build().parts();
        BaseApplication.iService.uploadUserPicture2(partList).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String picture = BaseApplication.BASE_URL + jsonObject.getString("msg");
                    int isLogin = jsonObject.getInt("isLogin");
                    if (isLogin == 1) {
                        userInfo.setGravatar(picture);
                        Toast.makeText(ProfileActivity.this, "图片上传成功", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                }
            }
        }));

        super.takeSuccess(result);
    }

}
