package com.huazong.app.huazong.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.huazong.app.huazong.base.BaseApplication;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	private IWXAPI iwxapi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		iwxapi = WXAPIFactory.createWXAPI(this, BaseApplication.APP_ID);
		iwxapi.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		iwxapi.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if (resp.errCode == 0){
				Toast.makeText(this,"支付成功",Toast.LENGTH_LONG).show();
			}else if (resp.errCode == -2){
				Toast.makeText(this,"取消支付",Toast.LENGTH_LONG).show();
			}else {
				Toast.makeText(this,"支付失败",Toast.LENGTH_LONG).show();
			}
			Intent intent = new Intent("wechatPayCallback");
			intent.putExtra("code",resp.errCode);
			sendBroadcast(intent);
			finish();
		}
	}
}