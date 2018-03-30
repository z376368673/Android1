package com.huazong.app.huazong.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.huazong.app.huazong.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellEquipmentFragment extends Fragment {
    WebView webView;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            webView.goBack();
        }
    };

    public SellEquipmentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sell_equipment, container, false);
        webView = (WebView) view.findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        //支持js
        settings.setJavaScriptEnabled(true);
        //支持对网页缩放
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        webView.setInitialScale(1);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        });
        Bundle arguments = getArguments();
        String url = arguments.getString("url", "");
        webView.loadUrl(url);

        getContext().registerReceiver(receiver,new IntentFilter("goBack"));

        return view;
    }

    @Override
    public void onDestroyView() {
        getContext().unregisterReceiver(receiver);
        super.onDestroyView();
    }
}
