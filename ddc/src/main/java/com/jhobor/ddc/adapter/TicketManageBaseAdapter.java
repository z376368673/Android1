package com.jhobor.ddc.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.ddc.R;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.entity.Ticket;
import com.jhobor.ddc.utils.ErrorUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/1/5.
 */

public class TicketManageBaseAdapter extends BaseAdapter {
    private List<Ticket> ticketList;
    private Context context;
    private LayoutInflater inflater;

    public TicketManageBaseAdapter(List<Ticket> ticketList, Context context) {
        this.ticketList = ticketList;
        this.context = context;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_ticket_manage, parent, false);
            viewHolder.money = (TextView) convertView.findViewById(R.id.money);
            viewHolder.restrict = (TextView) convertView.findViewById(R.id.restrict);
            viewHolder.count = (TextView) convertView.findViewById(R.id.count);
            viewHolder.delete = (TextView) convertView.findViewById(R.id.delete);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Ticket ticket = ticketList.get(position);
        viewHolder.money.setText(String.valueOf((int) ticket.getMoney()));
        viewHolder.restrict.setText(String.format(Locale.CHINA, "满%d即可使用", (int) ticket.getRestrictMoney()));
        viewHolder.count.setText(String.valueOf(ticket.getCount()));
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("删除")
                        .setMessage("确定要删除吗？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String uuid = (String) BaseApplication.dataMap.get("token");
                                BaseApplication.iService.delStoreTickets(uuid, ticket.getId()).enqueue(new RetrofitCallback(context, new RetrofitCallback.DataParser() {
                                    @Override
                                    public void parse(String data) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(data);
                                            int msg = jsonObject.getInt("msg");
                                            if (msg == 1) {
                                                ticketList.remove(position);
                                                notifyDataSetChanged();
                                                Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            ErrorUtil.retrofitResponseParseFail(context, e);
                                        }
                                    }
                                }));
                            }
                        }).create().show();
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView money, restrict, count, delete;
    }
}
