package com.jhobor.ddc.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.ddc.R;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.entity.Ticket;

import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/1/5.
 */

public class TicketBaseAdapter extends BaseAdapter {
    private List<Ticket> ticketList;
    private Context context;
    private LayoutInflater inflater;
    private float total;
    private int storeId;

    public TicketBaseAdapter(List<Ticket> ticketList, Context context, float total, int storeId) {
        this.ticketList = ticketList;
        this.context = context;
        this.total = total;
        this.storeId = storeId;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return ticketList.size();
    }

    @Override
    public Object getItem(int position) {
        return ticketList.get(position);
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
            convertView = inflater.inflate(R.layout.item_ticket, parent, false);
            viewHolder.money = (TextView) convertView.findViewById(R.id.money);
            viewHolder.restrict = (TextView) convertView.findViewById(R.id.restrict);
            viewHolder.expiry = (TextView) convertView.findViewById(R.id.expiry);
            viewHolder.company = (TextView) convertView.findViewById(R.id.company);
            viewHolder.state = (TextView) convertView.findViewById(R.id.state);
            viewHolder.privilege = (TextView) convertView.findViewById(R.id.privilege);
            viewHolder.container = (LinearLayout) convertView.findViewById(R.id.container);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Ticket ticket = ticketList.get(position);
        viewHolder.money.setText(String.format(Locale.CHINA, "%.0f", ticket.getMoney()));
        viewHolder.restrict.setText(String.format(Locale.CHINA, "满%.0f即可使用", ticket.getRestrictMoney()));
        viewHolder.expiry.setText(String.format(Locale.CHINA, "%s", ticket.getStartDate()));
        viewHolder.company.setText(ticket.getStoreName());
        String status;
        if (ticket.getState() == 0) {
            status = "选择使用";
        } else {
            status = "已经使用";
        }
        viewHolder.state.setText(status);

        if (0 == ticket.getState()) {
            viewHolder.container.setBackgroundResource(R.mipmap.ticket_unuse_bg);
            int color = ContextCompat.getColor(context, R.color.white);
            viewHolder.money.setTextColor(color);
            viewHolder.restrict.setTextColor(color);
            viewHolder.privilege.setTextColor(color);
            viewHolder.expiry.setTextColor(color);
            viewHolder.company.setTextColor(color);
        } else {
            viewHolder.container.setBackgroundResource(R.mipmap.ticket_useless_bg);
            int color = ContextCompat.getColor(context, R.color.textGray);
            viewHolder.money.setTextColor(color);
            viewHolder.restrict.setTextColor(color);
            viewHolder.privilege.setTextColor(color);
            viewHolder.expiry.setTextColor(color);
            viewHolder.company.setTextColor(color);
        }
        viewHolder.state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (total == 0) {
                    Toast.makeText(context, "只能在支付的时候使用", Toast.LENGTH_SHORT).show();
                } else if (ticket.getState() == 1) {
                    Toast.makeText(context, "已使用或过期", Toast.LENGTH_SHORT).show();
                } else if (ticket.getStoreId() != storeId) {
                    Toast.makeText(context, "只能在发出优惠券的店铺使用", Toast.LENGTH_SHORT).show();
                } else if (total < ticket.getRestrictMoney()) {
                    Toast.makeText(context, String.format(Locale.CHINA, "消费【%.2f】，未满【%d】", total, (int) ticket.getRestrictMoney()), Toast.LENGTH_SHORT).show();
                } else {
                    BaseApplication.dataMap.put("ticket", ticket);
                    ((Activity) context).finish();
                }
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView money, restrict, expiry, company, state, privilege;
        LinearLayout container;
    }
}
