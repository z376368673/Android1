package com.jhobor.fortune.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jhobor.fortune.R;
import com.jhobor.fortune.entity.Interest;

import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/5/2.
 */

public class InterestAdapter extends BaseQuickAdapter<Interest, BaseViewHolder> {

    public InterestAdapter(int layoutResId, List<Interest> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Interest item) {
        helper.setText(R.id.money, String.format(Locale.CHINA, "%.1f", item.getMoney()))
                .setText(R.id.rate, item.getRate())
                .setText(R.id.date, item.getDate())
                .setText(R.id.no_rate, item.getInterestMoney());
    }
}
