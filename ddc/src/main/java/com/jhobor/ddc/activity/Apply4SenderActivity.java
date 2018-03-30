package com.jhobor.ddc.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.ddc.R;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.entity.UserInfo;
import com.jhobor.ddc.utils.ErrorUtil;
import com.jhobor.ddc.utils.VerifyUtil;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Apply4SenderActivity extends TakePhotoActivity implements View.OnClickListener {
    ImageView topArrow, personalPic, IdCardFront, IdCardBack;
    TextView topTitle, done, getCode;
    EditText realName, phone, idCardNo, addr, code;
    Button ok;
    PopupWindow pop;

    List<String> pictures = new ArrayList<>();
    int curIndex = -1;
    ImageView curImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply4_sender);

        initView();
        handleEvt();
    }

    private void handleEvt() {
        topArrow.setOnClickListener(this);
        ok.setOnClickListener(this);
        personalPic.setOnClickListener(this);
        IdCardBack.setOnClickListener(this);
        IdCardFront.setOnClickListener(this);
        getCode.setOnClickListener(this);
        done.setOnClickListener(this);
    }

    private void initView() {
        topArrow = (ImageView) findViewById(R.id.topArrow);
        personalPic = (ImageView) findViewById(R.id.personalPic);
        IdCardFront = (ImageView) findViewById(R.id.IdCardFront);
        IdCardBack = (ImageView) findViewById(R.id.IdCardBack);
        topTitle = (TextView) findViewById(R.id.topTitle);
        done = (TextView) findViewById(R.id.done);
        getCode = (TextView) findViewById(R.id.getCode);
        realName = (EditText) findViewById(R.id.realName);
        phone = (EditText) findViewById(R.id.phone);
        idCardNo = (EditText) findViewById(R.id.idCardNo);
        addr = (EditText) findViewById(R.id.addr);
        code = (EditText) findViewById(R.id.code);
        ok = (Button) findViewById(R.id.ok);

        topTitle.setText("申请派单员");
        done.setText("我的信誉");
        done.setVisibility(View.VISIBLE);
        UserInfo userInfo = (UserInfo) BaseApplication.dataMap.get("userInfo");
        phone.setText(userInfo.getAccount());
        phone.setFocusable(false);
    }

    @Override
    public void onClick(View v) {
        if (v == topArrow) {
            finish();
        } else if (v == ok) {
            String realNameText = realName.getText().toString();
            String phoneText = phone.getText().toString();
            String idCardNoText = idCardNo.getText().toString();
            String addrText = addr.getText().toString();
            String codeText = code.getText().toString();
            if (realNameText.trim().length() < 2) {
                Toast.makeText(this, "姓名不正确", Toast.LENGTH_SHORT).show();
                realName.requestFocus();
            } else if (VerifyUtil.isPhone(phoneText)) {
                Toast.makeText(this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
                phone.requestFocus();
            } else if (!VerifyUtil.isIDCard15(idCardNoText) || !VerifyUtil.isIDCard18(idCardNoText)) {
                Toast.makeText(this, "身份证号不正确", Toast.LENGTH_SHORT).show();
                idCardNo.requestFocus();
            } else if (addrText.length() < 9) {
                Toast.makeText(this, "请具体到省、市、区、街道", Toast.LENGTH_SHORT).show();
                addr.requestFocus();
            } else if (codeText.length() < 4) {
                Toast.makeText(this, "验证码位数不足", Toast.LENGTH_SHORT).show();
                code.requestFocus();
            } else if (pictures.size() < 3) {
                Toast.makeText(this, "申请的图片数据不足", Toast.LENGTH_SHORT).show();
            } else {
                String uuid = (String) BaseApplication.dataMap.get("token");
                MultipartBody.Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("uuid", uuid)
                        .addFormDataPart("name", realNameText)
                        .addFormDataPart("identityNo", idCardNoText)
                        .addFormDataPart("address", addrText);
                for (int i = 0; i < pictures.size(); i++) {
                    String picturePath = pictures.get(i);
                    builder.addFormDataPart("takePicture" + i, picturePath, RequestBody.create(MediaType.parse("image/*"), new File(picturePath)));
                }
                List<MultipartBody.Part> parts = builder.build().parts();
                // String uuid,String name,String identityNo,String positive,String opposite,String cardholder,String address
                BaseApplication.iService.apply4sender(parts).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
                    @Override
                    public void parse(String data) {
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            int msg = jsonObject.getInt("msg");
                            if (msg == 1) {
                                Toast.makeText(Apply4SenderActivity.this, "申请成功，等待审核...", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent("changeApplySenderStatus");
                                intent.putExtra("applyStatus", 7);
                                sendBroadcast(intent);
                            } else {
                                Toast.makeText(Apply4SenderActivity.this, "审核中...", Toast.LENGTH_SHORT).show();
                            }
                            finish();
                        } catch (JSONException e) {
                            ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                        }
                    }
                }));
            }

        } else if (v == personalPic) {
            popAndSet(v, 0);
        } else if (v == IdCardFront) {
            popAndSet(v, 1);
        } else if (v == IdCardBack) {
            popAndSet(v, 2);
        } else if (v == getCode) {


        } else if (v == done) {
            Intent intent = new Intent(this, MyReputationActivity.class);
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

    private void popAndSet(View v, int index) {
        FrameLayout layout = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.select_pictures_pop_window, (ViewGroup) findViewById(R.id.popWin));
        layout.findViewById(R.id.useGallery).setOnClickListener(this);
        layout.findViewById(R.id.useCamera).setOnClickListener(this);
        layout.findViewById(R.id.popWin).setOnClickListener(this);
        pop = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        pop.showAtLocation(findViewById(R.id.activity_apply4_sender), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        curIndex = index;
        curImageView = (ImageView) v;
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
