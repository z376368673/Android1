package com.jhobor.fortune;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;

import com.jhobor.fortune.utils.BarUtil;

public class BrowserActivity extends AppCompatActivity {
    WebView webView;
    ImageView iv_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        iv_about = (ImageView) findViewById(R.id.iv_about);
        BarUtil.topBar(this, "公司介绍");
        /*webView = (WebView) findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);
        String url = "https://mp.weixin.qq.com/s?__biz=MzIwNzUzMzg2Ng==&mid=100000050&idx=1&sn=d550e3f246fd18b0d39df6900c4029d4&chksm=1711a37720662a616672f3e55bd27ee69fb42d5617a63e7411de28cc7b7519ea586f4f30b83f&mpshare=1&scene=23&srcid=0225E6s3HgYTQEuaWtWJwsiy#rd";
        webView.loadUrl(url);
        */
        //iv_about.setBackgroundResource(); android:adjustViewBounds="true"
        iv_about.setImageResource(R.mipmap.about);
        iv_about.setScaleType(ImageView.ScaleType.FIT_XY);
        iv_about.setAdjustViewBounds(true);


    }
}
