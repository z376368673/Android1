package com.jhobor.zzb.fragments;


import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jhobor.zzb.R;
import com.jhobor.zzb.adapter.CollectProductsAdapter;
import com.jhobor.zzb.base.BaseDialog;
import com.jhobor.zzb.base.BasePopupWin;
import com.jhobor.zzb.entity.Category;
import com.jhobor.zzb.entity.CategoryListMap;
import com.jhobor.zzb.entity.Product;
import com.jhobor.zzb.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollectProductFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    ImageView collectMenu;
    LinearLayout oneLevel,twoLevel;
    GridView products;
    HorizontalScrollView oneLevelScroll,twoLevelScroll;

    List<CategoryListMap> categoryListMaps = new ArrayList<>();
    List<Product> productList = new ArrayList<>(4);
    int onePos,twoPos;
    CollectProductsAdapter adapter;

    public CollectProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_collect_product, container, false);
        collectMenu = (ImageView) view.findViewById(R.id.collectMenu);
        oneLevel = (LinearLayout) view.findViewById(R.id.oneLevel);
        twoLevel = (LinearLayout) view.findViewById(R.id.twoLevel);
        products = (GridView) view.findViewById(R.id.products);
        oneLevelScroll = (HorizontalScrollView) view.findViewById(R.id.oneLevelScroll);
        twoLevelScroll = (HorizontalScrollView) view.findViewById(R.id.twoLevelScroll);

        collectMenu.setOnClickListener(this);
        oneLevelScroll.setHorizontalScrollBarEnabled(false);
        twoLevelScroll.setHorizontalScrollBarEnabled(false);

        Category c1 = Category.makeTop(1, "背景墙");
        List<Category> children1 = new ArrayList<>();
        children1.add(c1.makeChild(11,"瓷砖背景墙"));
        children1.add(c1.makeChild(12,"硅藻泥背景墙"));
        children1.add(c1.makeChild(13,"xx背景墙"));
        CategoryListMap categoryListMap1 = new CategoryListMap(c1, children1);
        categoryListMaps.add(categoryListMap1);

        Category c2 = Category.makeTop(2, "木地板");
        List<Category> children2 = new ArrayList<>();
        children2.add(c2.makeChild(21,"瓷砖木地板"));
        children2.add(c2.makeChild(22,"硅藻泥木地板"));
        children2.add(c2.makeChild(23,"xx木地板"));
        children2.add(c2.makeChild(24,"xx木地板"));
        children2.add(c2.makeChild(25,"xx木地板"));
        children2.add(c2.makeChild(26,"xx木地板"));
        CategoryListMap categoryListMap2 = new CategoryListMap(c2, children2);
        categoryListMaps.add(categoryListMap2);

        initOneLevel();
        expandOneLevel(0);

        return view;
    }

    LinearLayout.LayoutParams initParams(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.rightMargin = DisplayUtil.dip2px(getContext(),20);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            params.setMarginEnd(DisplayUtil.dip2px(getContext(),20));
        }
        return params;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CollectProductsAdapter.ViewHolder holder = (CollectProductsAdapter.ViewHolder) view.getTag();
        if (holder.actionBox.getVisibility()==View.VISIBLE){
            holder.actionBox.setVisibility(View.GONE);
        }else {

        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        CollectProductsAdapter.ViewHolder holder = (CollectProductsAdapter.ViewHolder) view.getTag();
        if (holder.actionBox.getVisibility()!=View.VISIBLE){
            holder.actionBox.setVisibility(View.VISIBLE);
        }else {
            holder.actionBox.setVisibility(View.GONE);
        }

        return true;
    }

    private class OneLevelClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int newPos = (int) v.getTag();
            expandOneLevel(newPos);
        }
    }

    private class TwoLevelMenuItemClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            int pos = (int) v.getTag();
            if (pos == 0){
                BaseDialog.showSharePanel(getContext());
            }else if (pos==1){
                showConfirmDialog("确定删除该类目内的所有商品？");
            }
        }
    }
    private class TwoLevelLongClickListener implements View.OnLongClickListener{

        @Override
        public boolean onLongClick(View v) {
            int newPos = (int) v.getTag();
            List<String> items = new ArrayList<>();
            items.add("分享");
            items.add("删除");
            BasePopupWin.showPopupWinActionMenu(getContext(),LinearLayout.VERTICAL,items,new TwoLevelMenuItemClickListener(), Gravity.BOTTOM,v);
            return true;
        }
    }
    private class TwoLevelClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int newPos = (int) v.getTag();
            expandTwoLevel(newPos);
        }
    }
    private void expandOneLevel(int newPos) {
        if (newPos == onePos&&onePos!=0)
            return;
        TextView last = (TextView) oneLevel.getChildAt(onePos);
        int grayText = ContextCompat.getColor(getContext(), R.color.grayText2);
        last.setTextColor(grayText);
        TextView now = (TextView) oneLevel.getChildAt(newPos);
        int purple = ContextCompat.getColor(getContext(), R.color.purple);
        now.setTextColor(purple);
        onePos = newPos;
        twoLevel.removeAllViews();
        LinearLayout.LayoutParams params = initParams();
        TwoLevelClickListener twoLevelClickListener = new TwoLevelClickListener();
        TwoLevelLongClickListener twoLevelLongClickListener = new TwoLevelLongClickListener();
        List<Category> children = categoryListMaps.get(newPos).getChildren();
        int size = children.size();
        for (int i = 0;i<size;i++){
            Category c = children.get(i);
            TextView tv2 = new TextView(getContext());
            tv2.setText(c.getName());
            tv2.setLayoutParams(params);
            tv2.setTextColor(grayText);
            tv2.setTag(i);
            Log.i("->>", String.valueOf(i));
            tv2.setOnClickListener(twoLevelClickListener);
            tv2.setOnLongClickListener(twoLevelLongClickListener);
            twoLevel.addView(tv2);
        }
        twoPos = 0;
        expandTwoLevel(twoPos);
    }

    private void expandTwoLevel(int newPos) {
        if (newPos == twoPos&&twoPos!=0)
            return;
        Log.i(">>", String.valueOf(twoPos));
        if (categoryListMaps.get(onePos).getChildren().size()>0) {
            TextView last = (TextView) twoLevel.getChildAt(twoPos);
            int grayText = ContextCompat.getColor(getContext(), R.color.grayText2);
            last.setTextColor(grayText);
            TextView now = (TextView) twoLevel.getChildAt(newPos);
            int purple = ContextCompat.getColor(getContext(), R.color.purple);
            now.setTextColor(purple);
        }
        twoPos = newPos;
        initProducts(newPos);
    }

    private void initProducts(int newPos) {
        productList.clear();
        List<String> list = new ArrayList<String>(1) {{
            add("http://img15.3lian.com/2015/f2/147/d/74.jpg");
        }};
        productList.add(new Product(1, 5200, "雪花白", list));
        productList.add(new Product(1, 5200, "雪花白", list));
        productList.add(new Product(1, 5200, "雪花白", list));
        productList.add(new Product(1, 5200, "雪花白", list));
        if (adapter==null) {
            adapter = new CollectProductsAdapter(getContext(), productList);
            products.setAdapter(adapter);
            products.setOnItemClickListener(this);
            products.setOnItemLongClickListener(this);
        }else {
            adapter.notifyDataSetChanged();
        }
    }

    private void showConfirmDialog(String mes) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_confirm,null);
        TextView msg = (TextView) view.findViewById(R.id.msg);
        Button yes = (Button) view.findViewById(R.id.yes);
        Button no = (Button) view.findViewById(R.id.no);
        msg.setText(mes);
        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .create();
        dialog.show();
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    private class OneLevelMenuItemClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            int pos = (int) v.getTag();
            if (pos == 0){
                BaseDialog.showSharePanel(getContext());
            }else if (pos==1){
                showConfirmDialog("确定删除该类目内的所有商品？");
            }
        }
    }
    private class OneLevelLongClickListener implements View.OnLongClickListener{

        @Override
        public boolean onLongClick(View v) {
            int newPos = (int) v.getTag();
            List<String> items = new ArrayList<>();
            items.add("分享");
            items.add("删除");
            BasePopupWin.showPopupWinActionMenu(getContext(),LinearLayout.VERTICAL,items,new OneLevelMenuItemClickListener(), Gravity.BOTTOM,v);
            return true;
        }
    }

    private void initOneLevel() {
        LinearLayout.LayoutParams params = initParams();
        int size = categoryListMaps.size();
        OneLevelClickListener oneLevelClickListener = new OneLevelClickListener();
        OneLevelLongClickListener oneLevelLongClickListener = new OneLevelLongClickListener();
        for (int i = 0;i<size;i++){
            CategoryListMap cm = categoryListMaps.get(i);
            TextView tv = new TextView(getContext());
            tv.setText(cm.getParent().getName());
            tv.setLayoutParams(params);
            tv.setTag(i);
            tv.setOnClickListener(oneLevelClickListener);
            tv.setOnLongClickListener(oneLevelLongClickListener);
            oneLevel.addView(tv);
        }
    }

    private void showFilterDialog() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_filter_bottom_sheet, null, false);
        final BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        dialog.setContentView(view);

        dialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v==collectMenu){
            showFilterDialog();
        }
    }
}
