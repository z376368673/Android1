package com.jhobor.fortune.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jhobor.fortune.R;
import com.jhobor.fortune.entity.TeamRecord;

import java.util.List;

/**
 *  团队记录明细
 *
 * Created by Administrator on 2017/3/31.
 */

public class TeamRecordAdapter extends BaseQuickAdapter<TeamRecord, BaseViewHolder> {
    private int TYPE = 1;
    public TeamRecordAdapter(int layoutResId, List<TeamRecord> data) {
        super(layoutResId, data);
    }

    public void setType(int type) {
        this.TYPE = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, TeamRecord item) {
        if (TYPE==1){
            helper.setText(R.id.tv_text1, item.getMobile())
                    .setText(R.id.tv_text2, String.valueOf(item.getMoney()) )
                    .setText(R.id.tv_text3, item.getCreateDate());
        }else {
            helper.setText(R.id.tv_text1, item.getMobile())
                    .setText(R.id.tv_text2, String.valueOf(item.getBillMobile()) )
                    .setText(R.id.tv_text3, item.getCreateDate());
        }

    }
}
