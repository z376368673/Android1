package com.jhobor.ddc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhobor.ddc.R;
import com.jhobor.ddc.entity.BankCard;

import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/1/5.
 */

public class ChooseBankCardBaseAdapter extends BaseAdapter {
    private List<BankCard> bankCardList;
    private Context context;
    private LayoutInflater inflater;

    public ChooseBankCardBaseAdapter(List<BankCard> bankCardList, Context context) {
        this.bankCardList = bankCardList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return bankCardList.size();
    }

    @Override
    public Object getItem(int position) {
        return bankCardList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_choose_bank_card, parent, false);
            viewHolder.bankIcon = (ImageView) convertView.findViewById(R.id.bankIcon);
            viewHolder.choose = (ImageView) convertView.findViewById(R.id.choose);
            viewHolder.brief = (TextView) convertView.findViewById(R.id.brief);
            viewHolder.bankName = (TextView) convertView.findViewById(R.id.bankName);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BankCard bankCard = bankCardList.get(position);
        //BaseApplication.display(msg.getPicture(),msgPic);
        viewHolder.bankName.setText(bankCard.getBankName());
        viewHolder.brief.setText(String.format(Locale.CHINA, "尾号%s储蓄卡", bankCard.getCardNo().substring(bankCard.getCardNo().length() - 4)));
        if (bankCard.getChosen() == 1) {
            viewHolder.choose.setVisibility(View.VISIBLE);
        } else {
            viewHolder.choose.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    private static class ViewHolder {
        ImageView bankIcon, choose;
        TextView bankName, brief;
    }
}
