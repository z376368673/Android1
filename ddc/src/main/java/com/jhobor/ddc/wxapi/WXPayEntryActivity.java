package com.jhobor.ddc.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.entity.Orders;
import com.jhobor.ddc.greendao.ShopCarDao;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.List;

/**
 * Created by Administrator on 2017/5/13.
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI iwxapi;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(">>","回调成功");
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
    public void onReq(BaseReq baseReq) {
        Log.i(">>","发起支付请求");
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0){
                delGoodsInShopCar();
                sendBroadcast(new Intent("finishPayActivity"));
                Toast.makeText(this,"支付成功",Toast.LENGTH_LONG).show();
            }else if (resp.errCode == -2){
                Toast.makeText(this,"取消支付",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this,"支付失败",Toast.LENGTH_LONG).show();
            }
//            Intent intent = new Intent("wechatPayCallback");
//            intent.putExtra("code",resp.errCode);
//            sendBroadcast(intent);
            finish();
        }
    }

    private void delGoodsInShopCar() {
        List<Orders> ordersList = (List<Orders>) BaseApplication.dataMap.get("ordersList");
        for (Orders orders : ordersList) {
            if (orders.isChecked()) {
                BaseApplication.dbService.getShopCarDao().queryBuilder().where(ShopCarDao.Properties.GoodsId.eq(orders.getGoodsId())).buildDelete().executeDeleteWithoutDetachingEntities();
            }
        }
    }
}
