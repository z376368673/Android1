package com.huazong.app.huazong;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huazong.app.huazong.adapter.SharePlatformAdapter;
import com.huazong.app.huazong.base.BaseApplication;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.GridHolder;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MsgShareActivity extends Activity implements View.OnClickListener {
    ImageView userIconBorder,userIcon;
    TextView tvNickname,tvRank,tvInform;
    EditText etComment;
    Button shareBtn;
    String nickname,rank,depth,time,scores,rate,beatPerson,inform,link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_share);

        initData();
        initView();
    }

    private void initData() {
        Intent intent = getIntent();
        String content = intent.getStringExtra("content");
        String regex = "用户名：([\\s\\S]+)，分数：(\\d+)，所用时间：([\\s\\S]+)，关数：(\\d+)，排名：(\\w+)名，超过了([\\d\\.]+)%的好友，打败全国(\\d+)人，([\\s\\S]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        Log.i("share>>",content);
        if (matcher.find()){
            nickname = matcher.group(1);
            scores = matcher.group(2);
            time = matcher.group(3);
            depth = matcher.group(4);
            rank = matcher.group(5);
            rate = matcher.group(6);
            beatPerson = matcher.group(7);
            link = matcher.group(8);
        }
    }

    private void initView() {
        userIcon = (ImageView) findViewById(R.id.userIcon);
        userIconBorder = (ImageView) findViewById(R.id.userIconBorder);
        tvNickname = (TextView) findViewById(R.id.userName);
        tvRank = (TextView) findViewById(R.id.rank);
        tvInform = (TextView) findViewById(R.id.inform);
        etComment = (EditText) findViewById(R.id.comment);
        shareBtn = (Button) findViewById(R.id.shareBtn);

        Glide.with(getBaseContext())
                .load(link)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(BaseApplication.GLIDE_ERROR)
                .bitmapTransform(new CropCircleTransformation(getBaseContext()))
                .into(userIcon);
        userIconBorder.setVisibility(View.VISIBLE);

        tvNickname.setText(nickname);
        tvRank.setText("第"+rank+"名");
        rate += "%";
        inform = String.format(Locale.CHINA,"您在第%s关用时%s获得%s分\n已超过%s的好友，打败全国%s人",depth,time,scores,rate,beatPerson);
        SpannableString ss = new SpannableString(inform);
        int colour = Color.rgb(240,136,4);
        int start = 2;
        int end = start+depth.length()+2;//5
        ss.setSpan(new ForegroundColorSpan(colour),start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        start = end+2;//7
        end = start + time.length();//8
        ss.setSpan(new ForegroundColorSpan(colour),start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        start = end + 2;//10
        end = start + scores.length()+1;//12
        ss.setSpan(new ForegroundColorSpan(colour),start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        start = end + 4;//16
        end = start + rate.length();//17
        ss.setSpan(new ForegroundColorSpan(colour),start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        start = end + 8;
        end = start + beatPerson.length();
        ss.setSpan(new ForegroundColorSpan(colour),start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvInform.setText(ss);
        shareBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == shareBtn){
            String comment = etComment.getText().toString();
            showAlert(comment);
        }
    }

    private void showAlert(final String comment) {
        final String[] platforms = {"QQ空间","微信朋友圈"};
        final int[] platformIcons = {R.drawable.umeng_socialize_qzone,R.drawable.umeng_socialize_wxcircle};
        SharePlatformAdapter adapter = new SharePlatformAdapter(getBaseContext(),platforms,platformIcons);
        DialogPlus dialog = DialogPlus.newDialog(this)
                .setAdapter(adapter)
                .setContentHolder(new GridHolder(2))
                .setGravity(Gravity.CENTER)
                .setCancelable(false)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        if (position==0){
                            share(SHARE_MEDIA.QZONE,inform,comment);
                        }else if (position==1){
                            share(SHARE_MEDIA.WEIXIN_CIRCLE,inform,comment);
                        }
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }
    public void share(SHARE_MEDIA platform,String title, String comment){
        ShareAction shareAction = new ShareAction(this);
        UMWeb umWeb = new UMWeb("https://shequ.yunzhijia.com/thirdapp/forum/network/57e88a91e4b07556d59440e3");
        umWeb.setTitle(title);
        umWeb.setDescription(comment);
        UMImage thumb = new UMImage(this, BitmapFactory.decodeResource(getResources(), R.drawable.qr_apk_download));
        umWeb.setThumb(thumb);
        shareAction.withMedia(umWeb);
        shareAction.setPlatform(platform)
                .withMedia(umWeb)
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
        UMShareAPI.get( this ).onActivityResult( requestCode, resultCode, data);
    }
}
