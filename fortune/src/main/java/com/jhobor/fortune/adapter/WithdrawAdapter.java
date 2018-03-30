package com.jhobor.fortune.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jhobor.fortune.R;
import com.jhobor.fortune.entity.Withdraw;

import java.util.List;

/**
 * Created by Administrator on 2017/4/1.
 */

public class WithdrawAdapter extends BaseQuickAdapter<Withdraw, BaseViewHolder> {
    public WithdrawAdapter(int layoutResId, List<Withdraw> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Withdraw item) {
        helper.setText(R.id.time, item.getTime())
                .setText(R.id.capital, String.valueOf(item.getMoney()))
                .setText(R.id.status, item.getStatus())
                .setText(R.id.way, item.getPayWay());
    }
}
