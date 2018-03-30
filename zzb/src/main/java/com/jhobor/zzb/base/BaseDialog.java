package com.jhobor.zzb.base;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jhobor.zzb.R;

/**
 * Created by Administrator on 2017/7/24.
 */

public class BaseDialog {
    public static void showInfo(Context context,String info){
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_show_msg, null, false);
        TextView msg = (TextView) view.findViewById(R.id.msg);
        msg.setText(info);
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .show();
        view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public static void showSharePanel(final Context context){
        class MyClickListener implements View.OnClickListener {
            private BottomSheetDialog dialog;

            private MyClickListener(BottomSheetDialog dialog) {
                this.dialog = dialog;
            }

            @Override
            public void onClick(View v) {
                Log.i("onClick>>","BaseWithHeaderActivity onClick");
                if (v.getId()==R.id.wechatCircle){
                    Toast.makeText(context,"分享成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_SUBJECT,"标题");
                    intent.putExtra(Intent.EXTRA_TEXT,"内容内容内容");
//                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(context.getResources().getDrawable(R.drawable.).t+"")))
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(Intent.createChooser(intent,"分享"));
                    dialog.dismiss();
                }else if (v.getId()==R.id.wechat){
                    Toast.makeText(context,"分享成功",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }else if (v.getId()==R.id.qq){
                    Toast.makeText(context,"分享成功",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        }
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_share_bottom_sheet,null);
        dialog.setContentView(view);
        dialog.show();
        MyClickListener clickListener = new MyClickListener(dialog);
        view.findViewById(R.id.wechatCircle).setOnClickListener(clickListener);
        view.findViewById(R.id.wechat).setOnClickListener(clickListener);
        view.findViewById(R.id.qq).setOnClickListener(clickListener);

    }

}
