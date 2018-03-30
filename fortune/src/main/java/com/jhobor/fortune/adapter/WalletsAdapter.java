package com.jhobor.fortune.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jhobor.fortune.R;
import com.jhobor.fortune.entity.Income;

import java.util.List;

/**
 * Created by Administrator on 2017/4/1.
 */

public class WalletsAdapter extends BaseQuickAdapter<Income, BaseViewHolder> {
    public WalletsAdapter(int layoutResId, List<Income> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Income item) {
        helper.setText(R.id.time, item.getTime())
                .setText(R.id.capital, String.valueOf(item.getMoney()))
                .setText(R.id.way, item.getStatus());
    }
}
