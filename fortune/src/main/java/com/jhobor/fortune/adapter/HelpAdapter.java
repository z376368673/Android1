package com.jhobor.fortune.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jhobor.fortune.R;
import com.jhobor.fortune.entity.Booding;

import java.util.List;

/**
 * Created by Administrator on 2017/5/3.
 */

public class HelpAdapter extends BaseQuickAdapter<Booding, BaseViewHolder> {

    public HelpAdapter(int layoutResId, List<Booding> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Booding item) {
        helper.setText(R.id.money, String.valueOf((int) item.getMoney()))
                .setText(R.id.trader, item.getParticipator())
                .setText(R.id.tel, item.getPhone())
                .setText(R.id.leader, item.getParentName())
                .setText(R.id.leaderTel, item.getParentPhone())
                .setText(R.id.date, item.getTime());
        if (item.getType() == 1) {
            helper.setText(R.id.moneyText, "提供金额")
                    .setText(R.id.traderText, "接受者姓名")
                    .setText(R.id.telText, "接受者电话");
            //helper.getConvertView().setBackgroundResource(R.mipmap.item_help_bg);
        } else if (item.getType() != 5){
            helper.setText(R.id.moneyText, "得到金额")
                    .setText(R.id.traderText, "提供者姓名")
                    .setText(R.id.telText, "提供者电话");
            //helper.getConvertView().setBackgroundResource(R.mipmap.item_gethelp_bg);
        }
    }
}
