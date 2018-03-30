package com.jhobor.fortune.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jhobor.fortune.R;
import com.jhobor.fortune.entity.Finance;

import java.util.List;

/**
 * Created by Administrator on 2017/3/30.
 */

public class FinanceBaseQuickAdapter extends BaseQuickAdapter<Finance, BaseViewHolder> {

    public FinanceBaseQuickAdapter(int layoutResId, List<Finance> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Finance item) {

        helper.setText(R.id.capital, String.valueOf(item.getCapital()))
                .setText(R.id.interest, String.valueOf(item.getInterest()))
                .setText(R.id.total, String.valueOf(item.getTotal()))
                .setText(R.id.date, String.valueOf(item.getDate()))
                .setText(R.id.status, String.valueOf(item.getStatus()));
    }
}
