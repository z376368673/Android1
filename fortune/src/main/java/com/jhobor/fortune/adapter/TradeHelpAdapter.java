package com.jhobor.fortune.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jhobor.fortune.R;
import com.jhobor.fortune.entity.Booding;

import java.util.List;
import java.util.Locale;

import cn.iwgang.countdownview.CountdownView;

/**
 * Created by Administrator on 2017/4/28.
 */

public class TradeHelpAdapter extends BaseQuickAdapter<Booding, BaseViewHolder> {

    public TradeHelpAdapter(int layoutResId, List<Booding> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Booding item) {
        helper/*.setText(R.id.orderNo, item.getOrderNo())
                .setText(R.id.participator, item.getParticipator())*/
                .setText(R.id.date, item.getTime())
                .setText(R.id.type, item.getType() == 0 ? "得到帮助" : "提供帮助");
        String str;
        switch (item.getState()) {
            case 0:
                str = "排单中";
                break;
            case 1:
                str = "等待打款";
                break;
            case 2:
                str = "等待确认";
                break;
            case 3:
                str = "已完成";
                break;
            default:
                str = "已过期";
        }
        TextView money = helper.getView(R.id.money);
        TextView status = helper.getView(R.id.status);
        View view = helper.getView(R.id.rootView);
        CountdownView countdownView = helper.getView(R.id.countDown);

        money.setText(String.format(Locale.CHINA, "%.1f", item.getMoney()));
        status.setText(str);
        //得到帮助
        if (item.getType() == 0) {
            int color = Color.parseColor("#009FE8");
            money.setTextColor(color);
            status.setTextColor(color);
            view.setBackgroundResource(R.mipmap.hbg2);
            //待收款
            if (item.getState()==2) {
                countdownView.setVisibility(View.VISIBLE);
                countdownView.start(item.getCountdown() * 1000);
            }else {
                countdownView.setVisibility(View.INVISIBLE);
            }
        } else {
            //提供帮助
            int color = Color.parseColor("#ff0000");
            money.setTextColor(color);
            status.setTextColor(color);
            //view.setBackgroundResource(R.mipmap.help_item_bg);
            if (item.getState()==1){
                countdownView.start(item.getCountdown()*1000);
            }else {
                countdownView.setVisibility(View.INVISIBLE);
            }
        }
    }
}
