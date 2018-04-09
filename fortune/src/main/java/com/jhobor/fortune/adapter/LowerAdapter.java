package com.jhobor.fortune.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jhobor.fortune.R;
import com.jhobor.fortune.entity.LowerLevel;

import java.util.List;

/**
 * Created by Administrator on 2017/3/31.
 */

public class LowerAdapter extends BaseQuickAdapter<LowerLevel, BaseViewHolder> {

    public LowerAdapter(int layoutResId, List<LowerLevel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LowerLevel item) {
        helper.setText(R.id.name, item.getMobile())
                .setText(R.id.joinTime, item.getMobile())
                .setText(R.id.capital, String.valueOf(item.getCapital()) + "元")
                .setText(R.id.profit, String.valueOf(item.getProfit()));
    }
}
