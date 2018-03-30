package com.jhobor.fortune;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.utils.BarUtil;
import com.jhobor.fortune.utils.ErrorUtil;
import com.jhobor.fortune.utils.HideIMEUtil;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.filter.entity.ImageFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vincent.filepicker.activity.ImagePickActivity.IS_NEED_CAMERA;

public class BoodingDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    TextView status, money, nameText, name, phoneText, phone, leader, leaderPhone
            , bankText, bank, bankNoText, bankNo, payWay, alipay, alipayAccount, alipayName
            , wechat, wechatPayAccount, wechatName, finishTime,certificate;
    ImageView pickImg;
    Button ok;

    int helpId, type;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booding_details);
        HideIMEUtil.wrap(this);

        status = (TextView) findViewById(R.id.status);
        money = (TextView) findViewById(R.id.money);
        nameText = (TextView) findViewById(R.id.nameText);
        name = (TextView) findViewById(R.id.name);
        phoneText = (TextView) findViewById(R.id.phoneText);
        phone = (TextView) findViewById(R.id.phone);
        leader = (TextView) findViewById(R.id.leader);
        leaderPhone = (TextView) findViewById(R.id.leaderPhone);
        bank = (TextView) findViewById(R.id.bank);
        bankNo = (TextView) findViewById(R.id.bankNo);
        bankText = (TextView) findViewById(R.id.bankText);
        bankNoText = (TextView) findViewById(R.id.bankNoText);
        payWay = (TextView) findViewById(R.id.payWay);
        alipay = (TextView) findViewById(R.id.alipay);
        alipayAccount = (TextView) findViewById(R.id.alipayAccount);
        alipayName = (TextView) findViewById(R.id.alipayName);
        wechat = (TextView) findViewById(R.id.wechat);
        wechatPayAccount = (TextView) findViewById(R.id.wechatPayAccount);
        wechatName = (TextView) findViewById(R.id.wechatName);
        finishTime = (TextView) findViewById(R.id.finishTime);
        certificate = (TextView) findViewById(R.id.certificate);
        pickImg = (ImageView) findViewById(R.id.pickImg);
        ok = (Button) findViewById(R.id.ok);
        BarUtil.topBar(this, "排单详情");
        pickImg.setOnClickListener(this);
        ok.setOnClickListener(this);
        Intent intent = getIntent();
        helpId = intent.getIntExtra("helpId", 0);
        type = intent.getIntExtra("type", 0);
        // 得到帮助时
        if (type == 0) {
            nameText.setText("打款人姓名");
            phoneText.setText("打款人电话");
            bankText.setText("打款银行");
            bankNoText.setText("打款卡号");
            certificate.setText("支付凭证（点击可放大查看）");
            ok.setText("确认收款");
        }
        getData();
    }

    private void getData() {
        Log.i(">>", String.format(Locale.CHINA, "helpId:%d  type:%d", helpId, type));
        String token = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.boodingDetails(token, helpId, type).enqueue(new RetrofitCallback(this, new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int isLogin = jsonObject.getInt("isLogin");
                    if (isLogin == 1) {
                        String date = jsonObject.getString("date");
                        double dmoney = jsonObject.getDouble("money");
                        String strPhone = jsonObject.getString("phone");
                        int helpId = jsonObject.getInt("helpId");
                        String sname = jsonObject.getString("sName");
                        String sMobile = jsonObject.getString("sMobile");
                        int istatus = jsonObject.getInt("status");
                        String strName = jsonObject.getString("name");
                        if (type == 0) {
                            path = BaseApplication.BASE_URL + jsonObject.getString("picture");
                            Glide.with(getBaseContext())
                                    .load(path)
                                    .into(pickImg);
                        }else {
                            JSONArray omList = jsonObject.getJSONArray("omList");
                            int length = omList.length();
                            for (int i = 0;i<length;i++){
                                JSONArray jsonArray = omList.getJSONArray(i);
                                int paywayId = jsonArray.getInt(0);
                                String userName = jsonArray.getString(1);
                                String account = jsonArray.getString(2);
                                String bankName = jsonArray.getString(3);
                                String type = jsonArray.getString(4);
                                if (type.equals("银行")){
                                    bank.setText(bankName);
                                    bankNo.setText(account);
                                }else if (type.equals("支付宝")){
                                    alipay.setText(type);
                                    alipayAccount.setText(account);
                                    alipayName.setText(userName);
                                }else if (type.equals("微信")){
                                    wechat.setText(type);
                                    wechatPayAccount.setText(account);
                                    wechatName.setText(userName);
                                }
                            }
                        }

                        String str = "排队中";
                        if (istatus == 1) {
                            str = "等待打款";
                        } else if (istatus == 2) {
                            str = "等待确认";
                        } else if (istatus == 3) {
                            str = "已完成";
                        }
                        status.setText(str);
                        money.setText(String.format(Locale.CHINA, "%d", (int)dmoney));
                        name.setText(strName);
                        phone.setText(strPhone);
                        leader.setText(sname);
                        leaderPhone.setText(sMobile);
                        payWay.setText("银行卡");
                        finishTime.setText(date);
                    } else {
                        Toast.makeText(getBaseContext(), "未登录，获取不到数据", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                }
            }
        }));
    }

    @Override
    public void onClick(View v) {
        if (v == pickImg) {
            if (type == 1) {
                pickImage();
            } else if (type == 0) {
                showImage();
            }
        } else if (v == ok) {
            if (type == 1) {
                uploadImage();
            } else if (type == 0) {
                confirmGetHelp();
            }
        }
    }

    private void showImage() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_image, null, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        dialog.show();
        Glide.with(getBaseContext())
                .load(path)
                .into(imageView);
        Log.i(">>", path);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void confirmGetHelp() {
        BaseApplication.dataMap.put("reload", true);
        String token = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.confirmGetHelp(token, helpId).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int isLogin = jsonObject.getInt("isLogin");
                    if (isLogin == 1) {
                        int msg = jsonObject.getInt("msg");
                        if (msg == 1) {
                            Toast.makeText(getBaseContext(), "确认收款成功", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(getBaseContext(), "确认收款失败，请稍候再试", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getBaseContext(), "未登录，获取不到数据", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                }
            }
        }));
    }

    private void pickImage() {
        Intent intent1 = new Intent(this, ImagePickActivity.class);
        intent1.putExtra(IS_NEED_CAMERA, true);
        intent1.putExtra(Constant.MAX_NUMBER, 1);
        startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("正在上传凭证图片...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        if (path == null || path.isEmpty()) {
            Toast.makeText(getBaseContext(), "请先选择凭证图片", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else {
            BaseApplication.dataMap.put("reload", true);
            String token = (String) BaseApplication.dataMap.get("token");
            File file = new File(path);
            MultipartBody build = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("uuid", token)
                    .addFormDataPart("helpId", String.valueOf(helpId))
                    .addFormDataPart("goodsPhoto", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                    .build();
            List<MultipartBody.Part> parts = build.parts();
            BaseApplication.iService.uploadCertificate(parts).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        progressDialog.dismiss();
                        if (response.body()==null){
                            Toast.makeText(getBaseContext(), "获取的数据为：null", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            return;
                        }
                        String data = response.body().string();
                        Log.i(">>", data);
                        JSONObject jsonObject = new JSONObject(data);
                        int isLogin = jsonObject.getInt("isLogin");
                        if (isLogin == 1) {
                            int msg = jsonObject.getInt("msg");
                            if (msg == 1) {
                                Toast.makeText(getBaseContext(), "上传成功", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                finish();
                            } else {
                                Toast.makeText(getBaseContext(), "上传失败，请稍候再试", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        } else {
                            Toast.makeText(getBaseContext(), "未登录，不能和服务器交互", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    } catch (JSONException | IOException e) {
                        ErrorUtil.retrofitResponseParseFail(getBaseContext(), e);
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorUtil.retrofitGetDataFail(getBaseContext(),t);
                    progressDialog.dismiss();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.REQUEST_CODE_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    ArrayList<ImageFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_IMAGE);
                    if (list.size() > 0) {
                        path = list.get(0).getPath();
                        pickImg.setImageBitmap(BitmapFactory.decodeFile(path));
                        Log.i(">>", path);
                    }
                }
                break;
        }

    }
}
