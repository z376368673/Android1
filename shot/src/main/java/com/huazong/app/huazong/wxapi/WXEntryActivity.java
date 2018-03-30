package com.huazong.app.huazong.wxapi;


import android.content.Intent;
import android.os.Bundle;

import com.umeng.socialize.weixin.view.WXCallbackActivity;

public class WXEntryActivity extends WXCallbackActivity /*implements IWXAPIEventHandler*/ {
    private boolean isFirstTime = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        BaseApplication.iwxapi.handleIntent(getIntent(), this);
    }

//	@Override
//	protected void onNewIntent(Intent intent) {
//		super.onNewIntent(intent);
//		setIntent(intent);
//		BaseApplication.iwxapi.handleIntent(intent, this);
//	}

    private void returnMsg(String s, boolean canLogin) {
        Intent loginIntent = new Intent("loginReceiver");
        loginIntent.putExtra("canLogin", canLogin);
        loginIntent.putExtra("msg", s);
        sendBroadcast(loginIntent);
        finish();
    }

//    @Override
//    public void onReq(BaseReq baseReq) {
//    }
//
//    @Override
//    public void onResp(BaseResp baseResp) {
//        if(!isFirstTime)
//            return;
//        isFirstTime = false;
//        String result = "未知原因";
//        String state = null;
//        switch (baseResp.errCode) {
//            case BaseResp.ErrCode.ERR_OK:
//                try {
//                    SendAuth.Resp resp = (SendAuth.Resp) baseResp;
//                    result = resp.code;
//                    state = resp.state;
//                }catch (Exception e){
//                    result = "分享成功";
//                }
//                break;
//            case BaseResp.ErrCode.ERR_USER_CANCEL:
//                result = "取消请求";
//                break;
//            case BaseResp.ErrCode.ERR_AUTH_DENIED:
//                result = "请求被拒绝";
//                break;
//        }
//
//        if (state !=null && "huazong".equals(state)) {
//            returnMsg(result, true);
//        }else {
//            Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
//            finish();
//        }
//    }

}