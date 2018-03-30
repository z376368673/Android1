package com.huazong.app.huazong;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.huazong.app.huazong.adapter.ShareListAdapter;
import com.huazong.app.huazong.adapter.SharePlatformAdapter;
import com.huazong.app.huazong.base.BarUtil;
import com.huazong.app.huazong.base.BaseApplication;
import com.huazong.app.huazong.base.RetrofitCallback;
import com.huazong.app.huazong.entity.PhotoInfo;
import com.huazong.app.huazong.entity.PhotoInfoList;
import com.huazong.app.huazong.utils.ErrorUtil;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.GridHolder;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShareActivity extends AppCompatActivity implements View.OnClickListener {
    ListView photoListView;
    ImageView share;

    ArrayList<String> photoUrls = new ArrayList<>();;
    private List<PhotoInfoList> photoList = new ArrayList<>();;
    private ProgressDialog progressDialog;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            PhotoInfo photoInfo = intent.getParcelableExtra("photoInfo");
            photoUrls.clear();
            photoUrls.add(photoInfo.getPath());
            showAlert();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_share);

        share = (ImageView) findViewById(R.id.share);
        photoListView = (ListView) findViewById(R.id.photoListView);

        BarUtil.topBar(this, "我的照片");
        share.setOnClickListener(this);
        registerReceiver(receiver,new IntentFilter("showSharePanel"));
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("获取");
        progressDialog.setMessage("正在获取云端图片...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        getData();
    }

    private void getData() {
        String openid = (String) BaseApplication.dataMap.get("openid");
        int arrowOrGun = (int) BaseApplication.dataMap.get("arrowOrGun");
        BaseApplication.iService.sharePhoto(openid,arrowOrGun).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String result) {
                if (progressDialog!=null) {
                    progressDialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0;i<data.length();i++){
                        JSONArray jsonArray = data.getJSONArray(i);
                        String date = jsonArray.getString(0);
                        String path = BaseApplication.BASE_URL_HOST2+jsonArray.getString(1);
                        PhotoInfo photoInfo = new PhotoInfo(path,date);
                        boolean flag = false;
                        for (PhotoInfoList photoInfoList:photoList){
                            if (photoInfoList.getDate().equals(date)){
                                photoInfoList.getPhotoInfos().add(photoInfo);
                                flag = true;
                                break;
                            }
                        }
                        if (!flag){
                            List<PhotoInfo> list = new ArrayList<>();
                            list.add(photoInfo);
                            PhotoInfoList photoInfoList = new PhotoInfoList(date,list);
                            photoList.add(photoInfoList);
                        }
                    }
                    if (photoList.size() > 0) {
                        photoListView.setAdapter(new ShareListAdapter(getBaseContext(),photoList));
                    } else {
                        Toast.makeText(getBaseContext(), "没有图片可分享", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getBaseContext(),e);
                }
            }
        }));
    }

    @Override
    public void onClick(View view) {
        if (view == share){
            showAlert();
        }
    }
    private void showAlert() {
        final String[] platforms = {"QQ空间","微信朋友圈"};
        final int[] platformIcons = {R.drawable.umeng_socialize_qzone,R.drawable.umeng_socialize_wxcircle};
        SharePlatformAdapter adapter = new SharePlatformAdapter(getBaseContext(),platforms,platformIcons);
        DialogPlus dialog = DialogPlus.newDialog(this)
                .setAdapter(adapter)
                .setContentHolder(new GridHolder(2))
                .setGravity(Gravity.CENTER)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        String url = photoUrls.size()>0?photoUrls.get(0):"";
                        if (position==0){
                            shareImage(SHARE_MEDIA.QZONE,url);
                        }else if (position==1){
                            shareImage(SHARE_MEDIA.WEIXIN_CIRCLE,url);
                        }
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    private void shareImage(SHARE_MEDIA platform,String image){
        UMImage pic;
        if (image.isEmpty()){
            pic = new UMImage(this, R.drawable.show1);
        }else {
            pic = new UMImage(this, image);
        }
        new ShareAction(this)
                .setPlatform(platform)
                .withMedia(pic)
                .withText("【战术设计训练营】")
                .setCallback(umShareListener)
                .share();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {}

        @Override
        public void onResult(SHARE_MEDIA platform) {
            if(platform.name().equals("WEIXIN_FAVORITE")){
                Toast.makeText(getBaseContext(),"收藏成功啦",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getBaseContext(), "分享成功啦", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getBaseContext(),"分享失败", Toast.LENGTH_SHORT).show();
                }
            });
            if(t.getMessage()!=null){
                Log.i("throw>>",t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(getBaseContext(),"取消分享", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult( requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
