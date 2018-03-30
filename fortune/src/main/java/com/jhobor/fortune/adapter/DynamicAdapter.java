package com.jhobor.fortune.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jhobor.fortune.R;
import com.jhobor.fortune.entity.Booding;

import java.util.List;

/**
 * Author     Qijing
 * Created by YQJ on 2017/10/16.
 * Description:
 */

public class DynamicAdapter extends BaseQuickAdapter<Booding,BaseViewHolder> {

    public DynamicAdapter(int layoutResId, List<Booding> data) {
        super(layoutResId, data);
    }
                    /* b.setParentName(name);
                            b.setOrderNo(trader);
                            b.setMoney((float) money);
                            b.setParentPhone(phone);
                            b.setParticipator(date);*/

    @Override
    protected void convert(BaseViewHolder helper, Booding booding) {
        helper.setText(R.id.dy_money,booding.getParentName()+"")
                .setText(R.id.dy_trader,booding.getOrderNo())
                .setText(R.id.dy_reward,booding.getMoney()+"")
                .setText(R.id.dy_tel,booding.getParentPhone())
                .setText(R.id.dy_date,booding.getParticipator());

    }
}
