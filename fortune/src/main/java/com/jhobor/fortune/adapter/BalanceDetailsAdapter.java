package com.jhobor.fortune.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jhobor.fortune.R;
import com.jhobor.fortune.entity.Booding;

import java.util.List;

/**
 * Created by Administrator on 2017/4/28.
 */

public class BalanceDetailsAdapter extends BaseQuickAdapter<Booding, BaseViewHolder> {

    public BalanceDetailsAdapter(int layoutResId, List<Booding> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Booding item) {
        helper.setText(R.id.money, String.valueOf((int) item.getMoney()))
                .setText(R.id.trader, item.getParticipator())
                .setText(R.id.tel, item.getPhone())
                .setText(R.id.date, item.getTime());
        String str = "排单中";
        if (item.getState() == 1 || item.getState() == 2) {
            str = "未完成";
        } else if (item.getState() == 3) {
            str = "已完成";
        }
        helper.setText(R.id.status, str);
        if (item.getType() == 0) {
            helper.setText(R.id.moneyText, "提供金额")
                    .setText(R.id.traderText, "提供者姓名")
                    .setText(R.id.telText, "提供者电话");
        } else {
            helper.setText(R.id.moneyText, "提供金额")
                    .setText(R.id.traderText, "接受者姓名")
                    .setText(R.id.telText, "接受者电话");
        }
    }
}
