package com.jhobor.fortune.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jhobor.fortune.R;
import com.jhobor.fortune.entity.BillRecordBean;

import java.util.List;

/**
 *
 * 服务增长纪录 适配器
 *
 * Created by Administrator on 2017/3/31.
 */

public class ReportRecordAdapter extends BaseQuickAdapter<BillRecordBean, BaseViewHolder> {

    public ReportRecordAdapter(int layoutResId, List<BillRecordBean> data) {
        super(layoutResId, data);
    }

    @Override//
    protected void convert(BaseViewHolder helper, BillRecordBean item) {
        helper.setText(R.id.tv_add_integral, "+"+item.getAwardIntegral()+"积分")
                .setText(R.id.tv_add_count, item.getIncreaseNop() + "人")
                .setText(R.id.tv_reward_count, item.getHeadCount()+ "人")
                .setText(R.id.tv_jieyuJf, item.getDateBalanceIntegral()+"积分")
                .setText(R.id.tv_date, item.getCreateDate());
    }
}
