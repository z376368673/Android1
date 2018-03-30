package com.jhobor.zzb.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.jhobor.zzb.R;
import com.jhobor.zzb.entity.Category;
import com.jhobor.zzb.entity.CategoryListMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

/**
 * Created by Administrator on 2017/7/14.
 */

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private List<CategoryListMap> mDatas;
    private Context mContext;

    public CategoryAdapter(Context context, List<CategoryListMap> datas){
        mContext = context;
        mDatas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        switch (viewType){
            case 1:
                View view1 = inflater.inflate(R.layout.item_collect_default_path,parent,false);
                holder = new ViewHolder1(view1);
                break;
            case 2:
                View view2 = inflater.inflate(R.layout.item_collect_customize_path,parent,false);
                holder = new ViewHolder2(view2);
                break;
            default:
                holder = null;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        CategoryListMap item = mDatas.get(position);
        switch (type){
            case 1:
                ((ViewHolder1)holder).stuff(item,position);
                break;
            case 2:
                ((ViewHolder2)holder).stuff(item,position);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position>0?2:1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onClick(View v) {
        CheckBox cb = (CheckBox) v;
        if ("+".equals(cb.getText())){
            cb.setChecked(false);
            View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_type_chars, null, false);
            final AlertDialog dialog = new AlertDialog.Builder(mContext)
                    .setView(view)
                    .create();
            dialog.show();
            view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            final EditText et = (EditText) view.findViewById(R.id.input);
            Button ok = (Button) view.findViewById(R.id.ok);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str = et.getText().toString().trim();
                    if (!"".equals(str)&&!"+".equals(str)) {
                        Category c = Category.makeTop(getItemCount() + 1, str);
                        CategoryListMap categoryListMap = new CategoryListMap(c);
                        categoryListMap.addChild(c.makeChild((getItemCount() + 1)*10,"+"));
                        mDatas.add(getItemCount()-1,categoryListMap);
                        dialog.dismiss();
                        notifyDataSetChanged();
                    }
                }
            });
        }else {
            int itemCount = getItemCount();
            int pos = (int) v.getTag();
            for (int i = 0; i < itemCount; i++) {
                mDatas.get(i).getParent().setChecked(pos == i);
            }
            notifyDataSetChanged();

            Intent intent = new Intent("switchCollectButton");
            intent.putExtra("enable",pos==0);
            mContext.sendBroadcast(intent);
        }
    }

    private class ViewHolder1 extends RecyclerView.ViewHolder{
        CheckBox parent;
        TextView defaultPath;
        ViewHolder1(View view){
            super(view);
            parent = (CheckBox) view.findViewById(R.id.parent);
            defaultPath = (TextView) view.findViewById(R.id.defaultPath);
            parent.setOnClickListener(CategoryAdapter.this);
        }
        void stuff(CategoryListMap item, int pos){
            parent.setTag(pos);
            parent.setChecked(item.getParent().isChecked());
            defaultPath.setText(item.getParent().getName());
        }
    }
    private class ViewHolder2 extends RecyclerView.ViewHolder{
        CheckBox parent;
        TagContainerLayout children;
        ViewHolder2(View view){
            super(view);
            parent = (CheckBox) view.findViewById(R.id.parent);
            children = (TagContainerLayout) view.findViewById(R.id.children);
            parent.setOnClickListener(CategoryAdapter.this);
            children.setOnTagClickListener(new TagView.OnTagClickListener() {
                @Override
                public void onTagClick(int position, String text) {
                    if (!parent.isChecked()){
                        return;
                    }
                    if (text.equals("+")){
                        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_type_chars, null, false);
                        final AlertDialog dialog = new AlertDialog.Builder(mContext)
                                .setView(view)
                                .create();
                        dialog.show();
                        view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        final EditText et = (EditText) view.findViewById(R.id.input);
                        Button ok = (Button) view.findViewById(R.id.ok);
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String str = et.getText().toString().trim();
                                if (!"".equals(str)&&!"+".equals(str)) {
                                    int pos = (int) parent.getTag();
                                    CategoryListMap categoryListMap = mDatas.get(pos);
                                    Category parent = categoryListMap.getParent();
                                    Category child = parent.makeChild(100, str);
                                    List<Category> list = categoryListMap.getChildren();
                                    int size = list.size();
                                    list.add(size-1,child);
                                    ++size;
                                    children.removeAllTags();
                                    int[] defaultColor = {
                                            ContextCompat.getColor(mContext,R.color.tagBackgroundColor),
                                            ContextCompat.getColor(mContext,R.color.tagBorderColor),
                                            ContextCompat.getColor(mContext,R.color.tagTextColor)
                                    };
                                    int[] checkedColor = {
                                            ContextCompat.getColor(mContext,R.color.tagBorderColor),
                                            ContextCompat.getColor(mContext,R.color.tagBorderColor),
                                            ContextCompat.getColor(mContext,R.color.tagTextColor)
                                    };
                                    int[] addColor = {
                                            Color.TRANSPARENT,
                                            ContextCompat.getColor(mContext,R.color.tagBorderColor),
                                            ContextCompat.getColor(mContext,R.color.tagTextColor)
                                    };
                                    List<String> tags = new ArrayList<>(size);
                                    List<int[]> colors = new ArrayList<>(size);
                                    for(int i = 0;i<size;i++){
                                        Category c = list.get(i);
                                        tags.add(c.getName());
                                        if (c.isChecked()){
                                            colors.add(checkedColor);
                                        }else if (c.getName().equals("+")){
                                            colors.add(addColor);
                                        }else {
                                            colors.add(defaultColor);
                                        }
                                    }
                                    children.setTags(tags,colors);
                                    dialog.dismiss();
                                }
                            }
                        });
                    }else {
                        children.removeAllTags();
                        int[] defaultColor = {
                                ContextCompat.getColor(mContext,R.color.tagBackgroundColor),
                                ContextCompat.getColor(mContext,R.color.tagBorderColor),
                                ContextCompat.getColor(mContext,R.color.tagTextColor)
                        };
                        int[] checkedColor = {
                                ContextCompat.getColor(mContext,R.color.tagBorderColor),
                                ContextCompat.getColor(mContext,R.color.tagBorderColor),
                                ContextCompat.getColor(mContext,R.color.tagTextColor)
                        };
                        int[] addColor = {
                                Color.TRANSPARENT,
                                ContextCompat.getColor(mContext,R.color.tagBorderColor),
                                ContextCompat.getColor(mContext,R.color.tagTextColor)
                        };
                        int pos = (int)parent.getTag();
                        List<Category> list = mDatas.get(pos).getChildren();
                        int size = list.size();
                        List<String> tags = new ArrayList<>(size);
                        List<int[]> colors = new ArrayList<>(size);
                        for(int i = 0;i<size;i++){
                            Category c = list.get(i);
                            c.setChecked(false);
                            tags.add(c.getName());
                            if (position==i){
                                c.setChecked(true);
                                colors.add(checkedColor);
                            }else if (c.getName().equals("+")){
                                colors.add(addColor);
                            }else {
                                colors.add(defaultColor);
                            }
                        }
                        children.setTags(tags,colors);

                        Intent intent = new Intent("switchCollectButton");
                        intent.putExtra("enable",true);
                        mContext.sendBroadcast(intent);
                    }
                }

                @Override
                public void onTagLongClick(final int position, String text) {
                    if (!parent.isChecked()){
                        return;
                    }
                    if (text.equals("+"))
                        return;
                    new AlertDialog.Builder(mContext)
                            .setMessage(String.format(Locale.CHINA,"确定要删除【%s】吗？",text))
                            .setNegativeButton("取消",null)
                            .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int pos = (int) parent.getTag();
                                    mDatas.get(pos).getChildren().remove(position);
                                    children.removeTag(position);
                                }
                            })
                            .show();
                }

                @Override
                public void onTagCrossClick(int position) {
                }
            });
        }
        void stuff(CategoryListMap item, int pos){
            parent.setText(item.getParent().getName());
            parent.setTag(pos);
            parent.setChecked(item.getParent().isChecked());
            List<Category> list = item.getChildren();
            children.removeAllTags();
            int[] defaultColor = {
                    ContextCompat.getColor(mContext,R.color.tagBackgroundColor),
                    ContextCompat.getColor(mContext,R.color.tagBorderColor),
                    ContextCompat.getColor(mContext,R.color.tagTextColor)
            };
            int[] addColor = {
                    Color.TRANSPARENT,
                    ContextCompat.getColor(mContext,R.color.tagBorderColor),
                    ContextCompat.getColor(mContext,R.color.tagTextColor)
            };
            List<String> tags = new ArrayList<>(list.size());
            List<int[]> colors = new ArrayList<>(list.size());
            for (Category c:list) {
                tags.add(c.getName());
                if (c.getName().equals("+")){
                    colors.add(addColor);
                }else {
                    colors.add(defaultColor);
                }
            }
            children.setTags(tags,colors);
        }
    }
}
