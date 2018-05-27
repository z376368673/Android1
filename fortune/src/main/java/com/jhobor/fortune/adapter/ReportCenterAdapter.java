package com.jhobor.fortune.adapter;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jhobor.fortune.R;
import com.jhobor.fortune.entity.ChildCenterBean;
import com.jhobor.fortune.entity.LowerLevel;

import java.util.List;

/**
 * Created by Administrator on 2017/3/31.
 */

public class ReportCenterAdapter extends BaseQuickAdapter<ChildCenterBean, BaseViewHolder> {

    public ReportCenterAdapter(int layoutResId, List<ChildCenterBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChildCenterBean item) {
        helper.setText(R.id.tv_account, item.getMobile())
                .setText(R.id.tv_increasing_people, String.valueOf(item.getNowDateChild()))
                .setText(R.id.tv_report_reward, item.getNowDateBillIntegral()+"")
                .setTextColor(R.id.tv_report_reward,R.color.tabActived)
                .setText(R.id.tv_countNum, item.getCountChild()+"");
    }
}
