package com.jhobor.fortune.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jhobor.fortune.R;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.utils.BarUtil;
import com.jhobor.fortune.utils.BitmapUtlis;
import com.jhobor.fortune.utils.HideIMEUtil;
import com.vincent.filepicker.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Author     Qijing
 * Created by YQJ on 2018/3/27.
 * Description:
 */
public class ShareActivity extends BaseActivity {

    private ImageView mShare_share;
    private ImageView mShare_iv;
    private TextView mSave;
    private TextView tv_yaoqing;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        context = this;
        HideIMEUtil.wrap(this);
        BarUtil.topBar(this, "分享");
        BarUtil.topBarRight(this, false, " ");
        mShare_share = (ImageView) findViewById(R.id.share_share);
        mShare_iv = (ImageView) findViewById(R.id.share_iv);
        mSave = (TextView) findViewById(R.id.share_submmit);
        tv_yaoqing = (TextView) findViewById(R.id.tv_yaoqing);
        mShare_share.setOnClickListener(this);
        getData();
    }

    private void getData() {
        String uuid = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.share(uuid).enqueue(new RetrofitCallback(this, new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int msg = jsonObject.optInt("msg");
                    if (msg == 1) {
                        String qrCode = jsonObject.optString("qrCode");
                        String inviteCode = jsonObject.optString("inviteCode");
                        Glide.with(ShareActivity.this).load(qrCode).into(mShare_iv);
//                        Bitmap rQcode = RQcode.getRQcode(qrCode);
//                        mShare_iv.setImageBitmap(rQcode);
                        tv_yaoqing.setText("邀请码：" + inviteCode);
                    } else {
                        Toast.makeText(ShareActivity.this, "二维码获取失败", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view==mShare_share){
            share();
        }
    }

    private void share() {
        String path = saveImg();
        File file = new File(path);
        Intent intent1 = new Intent(Intent.ACTION_SEND);
        intent1.setType("image/*");
       // intent1.putExtra(Intent.EXTRA_SUBJECT, "通众分享：\n" + "http://www.baidu.com");
        intent1.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(path)));
        startActivity(Intent.createChooser(intent1, "分享"));
    }


    public void saveBitmap(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    ) {

                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        }, 1);
            } else {
                saveImg();
            }
        }
    }

    private String saveImg() {
        mShare_iv.setDrawingCacheEnabled(true);
        Bitmap bm = mShare_iv.getDrawingCache();

        String galleryPath = Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_DCIM
                + File.separator + "Camera" + File.separator;
        File file = new File(galleryPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(file.getPath(), System.currentTimeMillis() + ".png");
        String path = new BitmapUtlis().saveFile(bm, file.getPath());
        Log.e("PATH", path);
        //ToastUtil.getInstance(ShareActivity.this).showToast("图片保存到：" + path);
        return path;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveBitmap(null);
            } else {
                ToastUtil.getInstance(ShareActivity.this).showToast("权限被拒绝，某些功能将无法使用");
            }
        }
    }

}
