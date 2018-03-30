package com.jhobor.zzb.base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jhobor.zzb.R;

import java.util.List;

/**
 * Created by Administrator on 2017/7/26.
 */

public class BasePopupWin {
    public static void showPopupWinActionMenu(Context context, int orientation, List<String> items, final View.OnClickListener listener, int gravity, View anchor){
        final PopupWindow popupWindow = new PopupWindow(context);
        popupWindow.setFocusable(true);
        class OnMenuItemClickListener implements View.OnClickListener{

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                listener.onClick(v);
            }
        }
        OnMenuItemClickListener clickListener = new OnMenuItemClickListener();
        LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.popup_win_action_menu,null);
        layout.setOrientation(orientation);
        int size = items.size();
        for (int i = 0;i<size;i++){
            TextView tv = new TextView(context);
            tv.setPadding(10,5,10,5);
            tv.setText(items.get(i));
            tv.setTextColor(Color.WHITE);
            tv.setGravity(Gravity.CENTER);
            tv.setTag(i);
            tv.setOnClickListener(clickListener);
            layout.addView(tv);
        }
        popupWindow.setContentView(layout);
        ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
        popupWindow.setBackgroundDrawable(dw);
        int[] loc = new int[2];
        anchor.getLocationInWindow(loc);
        int height = anchor.getHeight();
        int width = anchor.getWidth();
        popupWindow.getContentView().measure(0,0);
        int measuredHeight = popupWindow.getContentView().getMeasuredHeight();
        int measuredWidth = popupWindow.getContentView().getMeasuredWidth();
        Log.i("-->>",measuredHeight+" , "+measuredWidth);
        if (gravity==Gravity.TOP){
            layout.setBackgroundResource(R.drawable.popup_top);
            int x = loc[0] + width/2 - measuredWidth/2;
            popupWindow.showAtLocation(anchor, Gravity.TOP|Gravity.START,x,loc[1]);
        }else if (gravity==Gravity.BOTTOM){
            layout.setBackgroundResource(R.drawable.popup_bottom);
            int x = loc[0] + width/2 - measuredWidth/2;
            popupWindow.showAtLocation(anchor, Gravity.TOP|Gravity.START,x,loc[1]+height);
        }
    }
}
