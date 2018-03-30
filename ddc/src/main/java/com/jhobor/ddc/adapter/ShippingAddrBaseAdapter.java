package com.jhobor.ddc.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.ddc.R;
import com.jhobor.ddc.activity.EditShippingAddrActivity;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.base.RetrofitCallback;
import com.jhobor.ddc.entity.Addr;
import com.jhobor.ddc.utils.ErrorUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017/1/5.
 */

public class ShippingAddrBaseAdapter extends BaseAdapter {
    private List<Addr> addrList;
    private Context context;
    private LayoutInflater inflater;
    private int defaultAddr = -1;

    public ShippingAddrBaseAdapter(List<Addr> addrList, Context context) {
        this.addrList = addrList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return addrList.size();
    }

    @Override
    public Object getItem(int position) {
        return addrList.get(position);
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
            convertView = inflater.inflate(R.layout.item_shipping_addr, parent, false);
            viewHolder.realName = (TextView) convertView.findViewById(R.id.realName);
            viewHolder.phone = (TextView) convertView.findViewById(R.id.phone);
            viewHolder.addr = (TextView) convertView.findViewById(R.id.addr);
            viewHolder.edit = (TextView) convertView.findViewById(R.id.edit);
            viewHolder.delete = (TextView) convertView.findViewById(R.id.delete);
            viewHolder.setAddrAsDefault = (RadioButton) convertView.findViewById(R.id.setAddrAsDefault);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Addr addr = addrList.get(position);
        viewHolder.realName.setText(addr.getRealName());
        viewHolder.phone.setText(addr.getPhone());
        viewHolder.addr.setText(addr.getAddress());
        if (addr.getIsDefault() == 1) {
            viewHolder.setAddrAsDefault.setChecked(true);
            defaultAddr = position;
        } else {
            viewHolder.setAddrAsDefault.setChecked(false);
        }
        viewHolder.setAddrAsDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != defaultAddr) {
                    if (defaultAddr != -1) {
                        addrList.get(defaultAddr).setIsDefault(0);
                    }
                    addrList.get(position).setIsDefault(1);
                    final String uuid = (String) BaseApplication.dataMap.get("token");
                    BaseApplication.iService.setDefaultAddr(uuid, addrList.get(position).getId(), 1).enqueue(new RetrofitCallback(context, new RetrofitCallback.DataParser() {
                        @Override
                        public void parse(String data) {
                            try {
                                JSONObject jsonObject = new JSONObject(data);
                                int isLogin = jsonObject.getInt("isLogin");
                                if (isLogin == 1 && defaultAddr != -1) {
                                    changeDefaultAddr(uuid, addrList.get(defaultAddr).getId(), 0);
                                }
                                notifyDataSetChanged();
                            } catch (JSONException e) {
                                ErrorUtil.retrofitResponseParseFail(context, e);
                            }
                        }
                    }));
                }
            }
        });
        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditShippingAddrActivity.class);
                intent.putExtra("addr", addr);
                context.startActivity(intent);
            }
        });
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog
                        .Builder(context)
                        .setTitle("删除")
                        .setMessage("确定删除该地址吗？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String uuid = (String) BaseApplication.dataMap.get("token");
                                BaseApplication.iService.delAddr(uuid, addr.getId()).enqueue(new RetrofitCallback(context, new RetrofitCallback.DataParser() {
                                    @Override
                                    public void parse(String data) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(data);
                                            int isLogin = jsonObject.getInt("isLogin");
                                            int msg = jsonObject.getInt("msg");
                                            if (isLogin == 1 && msg == 1) {
                                                addrList.remove(position);
                                                notifyDataSetChanged();
                                            }
                                        } catch (JSONException e) {
                                            ErrorUtil.retrofitResponseParseFail(context, e);
                                        }
                                    }
                                }));
                            }
                        }).create();
                dialog.show();
            }
        });

        return convertView;
    }

    private void changeDefaultAddr(String uuid, int addrId, int status) {
        BaseApplication.iService.setDefaultAddr(uuid, addrId, status).enqueue(new RetrofitCallback(context, new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int isLogin = jsonObject.getInt("isLogin");
                    if (isLogin == 1) {
                        Toast.makeText(context, "设置成功", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(context, e);
                }
            }
        }));
    }

    private static class ViewHolder {
        TextView realName, phone, addr, edit, delete;
        RadioButton setAddrAsDefault;
    }

}
