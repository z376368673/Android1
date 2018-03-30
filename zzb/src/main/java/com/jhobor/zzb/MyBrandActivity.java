package com.jhobor.zzb;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jhobor.zzb.adapter.CollectProductsAdapter;
import com.jhobor.zzb.adapter.MyBrandChooseCategoryAdapter;
import com.jhobor.zzb.adapter.MyBrandProductsAdapter;
import com.jhobor.zzb.base.BaseDialog;
import com.jhobor.zzb.base.BasePopupWin;
import com.jhobor.zzb.base.BaseWithHeaderActivity;
import com.jhobor.zzb.entity.Category;
import com.jhobor.zzb.entity.CategoryListMap;
import com.jhobor.zzb.entity.Product;
import com.jhobor.zzb.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyBrandActivity extends BaseWithHeaderActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    LinearLayout oneLevel, twoLevel;
    ImageView addOneLevel, addTwoLevel;
    GridView products;
    TextView contact;

    List<CategoryListMap> categoryListMaps = new ArrayList<>();
    List<Product> productList = new ArrayList<>(4);
    int onePos, twoPos;
    MyBrandProductsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fillContent(R.layout.activity_my_brand);
        share.setImageResource(R.mipmap.my_brand_menu);

        oneLevel = (LinearLayout) findViewById(R.id.oneLevel);
        twoLevel = (LinearLayout) findViewById(R.id.twoLevel);
        addOneLevel = (ImageView) findViewById(R.id.addOneLevel);
        addTwoLevel = (ImageView) findViewById(R.id.addTwoLevel);
        products = (GridView) findViewById(R.id.products);
        contact = (TextView) findViewById(R.id.contact);

        share.setOnClickListener(this);
        contact.setOnClickListener(this);
        initOneLevel();
        expandOneLevel(0);

    }

    private void initOneLevel() {
        Category c1 = Category.makeTop(1, "个人作品");
        List<Category> children1 = new ArrayList<>();
        children1.add(c1.makeChild(11, "家装"));
        children1.add(c1.makeChild(12, "别墅"));
        children1.add(c1.makeChild(13, "酒店"));
        CategoryListMap categoryListMap1 = new CategoryListMap(c1, children1);
        categoryListMaps.add(categoryListMap1);

        Category c2 = Category.makeTop(2, "品牌名称1");
        List<Category> children2 = new ArrayList<>();
        children2.add(c2.makeChild(21, "家装"));
        children2.add(c2.makeChild(22, "别墅"));
        children2.add(c2.makeChild(23, "酒店"));
        CategoryListMap categoryListMap2 = new CategoryListMap(c2, children2);
        categoryListMaps.add(categoryListMap2);
        LinearLayout.LayoutParams params = initParams();
        int size = categoryListMaps.size();
        OneLevelClickListener oneLevelClickListener = new OneLevelClickListener();
        OneLevelLongClickListener oneLevelLongClickListener = new OneLevelLongClickListener();
        int dp20 = DisplayUtil.dip2px(this, 20);
        int dp8 = DisplayUtil.dip2px(this, 8);
        for (int i = 0; i < size; i++) {
            CategoryListMap cm = categoryListMaps.get(i);
            TextView tv = new TextView(this);
            tv.setText(cm.getParent().getName());
            tv.setLayoutParams(params);
            tv.setPadding(dp20, dp8, dp20, dp8);
            tv.setTag(i);
            tv.setOnClickListener(oneLevelClickListener);
            tv.setOnLongClickListener(oneLevelLongClickListener);
            oneLevel.addView(tv);
        }
    }

    LinearLayout.LayoutParams initParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return params;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0) {
            MyBrandProductsAdapter.ViewHolder holder = (MyBrandProductsAdapter.ViewHolder) view.getTag();
            if (holder.actionBox.getVisibility() == View.VISIBLE) {
                holder.actionBox.setVisibility(View.GONE);
            } else {
                startActivity(new Intent(this, ProductEditActivity.class));
            }
        } else {

        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0)
            return true;
        MyBrandProductsAdapter.ViewHolder holder = (MyBrandProductsAdapter.ViewHolder) view.getTag();
        if (holder.actionBox.getVisibility() != View.VISIBLE) {
            holder.actionBox.setVisibility(View.VISIBLE);
        } else {
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

    private class OneLevelLongClickListener implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View v) {
            int newPos = (int) v.getTag();
            List<String> items = new ArrayList<>();
            items.add("分享");
            BasePopupWin.showPopupWinActionMenu(getBaseContext(), LinearLayout.VERTICAL, items, new OneLevelMenuItemClickListener(), Gravity.BOTTOM, v);
            return true;
        }
    }

    private class OneLevelMenuItemClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int pos = (int) v.getTag();
            if (pos == 0) {
                BaseDialog.showSharePanel(MyBrandActivity.this);
            }
        }
    }

    private void showConfirmDialog(String mes) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_confirm, null);
        TextView msg = (TextView) view.findViewById(R.id.msg);
        Button yes = (Button) view.findViewById(R.id.yes);
        Button no = (Button) view.findViewById(R.id.no);
        msg.setText(mes);
        final AlertDialog dialog = new AlertDialog.Builder(this)
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

    private void expandOneLevel(int newPos) {
        if (newPos == onePos && onePos != 0)
            return;
        int grayText = ContextCompat.getColor(this, R.color.grayText2);
        int white = ContextCompat.getColor(this, R.color.white);
        int purple = Color.parseColor("#B584E8");
        TextView last = (TextView) oneLevel.getChildAt(onePos);
        last.setTextColor(grayText);
        last.setBackgroundColor(white);
        TextView now = (TextView) oneLevel.getChildAt(newPos);
        now.setTextColor(white);
        now.setBackgroundColor(purple);
        onePos = newPos;
        twoLevel.removeAllViews();
        LinearLayout.LayoutParams params = initParams();
        TwoLevelClickListener twoLevelClickListener = new TwoLevelClickListener();
        TwoLevelLongClickListener twoLevelLongClickListener = new TwoLevelLongClickListener();
        int dp20 = DisplayUtil.dip2px(this, 20);
        int dp8 = DisplayUtil.dip2px(this, 8);
        List<Category> children = categoryListMaps.get(newPos).getChildren();
        int size = children.size();
        for (int i = 0; i < size; i++) {
            Category c = children.get(i);
            TextView tv2 = new TextView(this);
            tv2.setText(c.getName());
            tv2.setLayoutParams(params);
            tv2.setTextColor(grayText);
            tv2.setTag(i);
            tv2.setPadding(dp20, dp8, dp20, dp8);
            Log.i("->>", String.valueOf(i));
            tv2.setOnClickListener(twoLevelClickListener);
            tv2.setOnLongClickListener(twoLevelLongClickListener);
            twoLevel.addView(tv2);
        }
        twoPos = 0;
        expandTwoLevel(twoPos);
    }

    private class TwoLevelClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int newPos = (int) v.getTag();
            expandTwoLevel(newPos);
        }
    }

    private class TwoLevelLongClickListener implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View v) {
            int newPos = (int) v.getTag();
            List<String> items = new ArrayList<>();
            items.add("分享");
            items.add("删除");
            BasePopupWin.showPopupWinActionMenu(MyBrandActivity.this, LinearLayout.VERTICAL, items, new TwoLevelMenuItemClickListener(), Gravity.BOTTOM, v);
            return true;
        }
    }

    private class TwoLevelMenuItemClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int pos = (int) v.getTag();
            if (pos == 0) {
                BaseDialog.showSharePanel(MyBrandActivity.this);
            } else if (pos == 1) {
                showConfirmDialog("确定删除该类目内的所有商品？");
            }
        }
    }

    private void expandTwoLevel(int newPos) {
        if (newPos == twoPos && twoPos != 0)
            return;
        Log.i(">>", String.valueOf(twoPos));
        if (categoryListMaps.get(onePos).getChildren().size() > 0) {
            int grayText = ContextCompat.getColor(this, R.color.grayText2);
            int white = ContextCompat.getColor(this, R.color.white);
            int purple = Color.parseColor("#D9BCF7");
            TextView last = (TextView) twoLevel.getChildAt(twoPos);
            last.setTextColor(grayText);
            last.setBackgroundColor(white);
            TextView now = (TextView) twoLevel.getChildAt(newPos);
            now.setTextColor(white);
            now.setBackgroundColor(purple);
        }
        twoPos = newPos;
        initProducts(newPos);
    }

    private void initProducts(int newPos) {
        productList.clear();
        List<String> list = new ArrayList<String>(1) {{
            add("http://img15.3lian.com/2015/f2/147/d/74.jpg");
        }};
        productList.add(new Product(1, "", "", null));
        productList.add(new Product(2, "石材", "雪花白", list));
        productList.add(new Product(3, "石材", "雪花白", list));
        productList.add(new Product(4, "石材", "雪花白", list));
        if (adapter == null) {
            adapter = new MyBrandProductsAdapter(this, productList);
            products.setAdapter(adapter);
            products.setOnItemClickListener(this);
            products.setOnItemLongClickListener(this);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void showActionPopupWin() {
        View view = getLayoutInflater().inflate(R.layout.popup_win_right_top, null, false);
        final PopupWindow actionPopupWin = new PopupWindow(this);
        actionPopupWin.setContentView(view);
        actionPopupWin.setFocusable(true);
        actionPopupWin.showAsDropDown(share);
        TextView actionCollect = (TextView) view.findViewById(R.id.actionCollect);
        TextView actionShare = (TextView) view.findViewById(R.id.actionShare);
        actionCollect.setText("帮助");
        actionCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(">>", "collect");
                actionPopupWin.dismiss();
            }
        });
        actionShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(">>", "share");
                actionPopupWin.dismiss();
            }
        });
        actionPopupWin.showAsDropDown(share);
    }

    @Override
    public void onClick(View v) {
        if (v == share) {
            showActionPopupWin();
        } else if (v == contact) {
//            showContactInfoDialog();
//            showCategoryDialog();
//            showInputDailog();
//            showInput2Dailog();
//            showInput3Dailog();
            showAddDialog();
        }
    }

    private void showAddDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_my_brand_add, null, false);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(false)
                .create();
        ImageView close = (ImageView) view.findViewById(R.id.close);
        ImageView productImg = (ImageView) view.findViewById(R.id.productImg);
        EditText desc = (EditText) view.findViewById(R.id.desc);
        final TextView tips = (TextView) view.findViewById(R.id.tips);
        Button ok = (Button) view.findViewById(R.id.ok);
        desc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tips.setText(String.format(Locale.CHINA,"%d/100", s.toString().length()));
            }
        });
        class DialogOnClickListener implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.close) {
                    dialog.dismiss();
                } else if (v.getId() == R.id.ok) {

                    dialog.dismiss();
                }
            }
        }
        DialogOnClickListener listener = new DialogOnClickListener();
        close.setOnClickListener(listener);
        ok.setOnClickListener(listener);
        dialog.show();
    }


    private void showInput3Dailog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_my_brand_input2, null, false);
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        //dialog.setCancelable(false);
        dialog.setContentView(view);
        ImageView close = (ImageView) view.findViewById(R.id.close);
        Spinner addrs = (Spinner) view.findViewById(R.id.addrs);
        EditText name = (EditText) view.findViewById(R.id.name);
        EditText building = (EditText) view.findViewById(R.id.building);
        Spinner servicePrjs = (Spinner) view.findViewById(R.id.servicePrjs);
        Button ok = (Button) view.findViewById(R.id.ok);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_simple_spinner, new String[]{"请选择项目地址", "地址1", "地址2", "地址3", "地址4"});
        addrs.setAdapter(adapter);
        building.setHint("请输入服务内容：如室内石材");
        servicePrjs.setVisibility(View.GONE);

        class DialogOnClickListener implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.close) {
                    dialog.dismiss();
                } else if (v.getId() == R.id.ok) {

                    dialog.dismiss();
                }
            }
        }
        DialogOnClickListener listener = new DialogOnClickListener();
        close.setOnClickListener(listener);
        ok.setOnClickListener(listener);
        dialog.show();
    }
    private void showInput2Dailog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_my_brand_input2, null, false);
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        //dialog.setCancelable(false);
        dialog.setContentView(view);
        ImageView close = (ImageView) view.findViewById(R.id.close);
        Spinner addrs = (Spinner) view.findViewById(R.id.addrs);
        EditText name = (EditText) view.findViewById(R.id.name);
        EditText building = (EditText) view.findViewById(R.id.building);
        Spinner servicePrjs = (Spinner) view.findViewById(R.id.servicePrjs);
        Button ok = (Button) view.findViewById(R.id.ok);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_simple_spinner, new String[]{"请选择项目地址", "地址1", "地址2", "地址3", "地址4"});
        addrs.setAdapter(adapter);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.item_simple_spinner, new String[]{"请选择服务项目", "项目1", "项目2", "项目3", "项目4"});
        servicePrjs.setAdapter(adapter2);

        class DialogOnClickListener implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.close) {
                    dialog.dismiss();
                } else if (v.getId() == R.id.ok) {

                    dialog.dismiss();
                }
            }
        }
        DialogOnClickListener listener = new DialogOnClickListener();
        close.setOnClickListener(listener);
        ok.setOnClickListener(listener);
        dialog.show();
    }
    private void showInputDailog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_my_brand_input, null, false);
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        //dialog.setCancelable(false);
        dialog.setContentView(view);
        ImageView close = (ImageView) view.findViewById(R.id.close);
        Spinner addrs = (Spinner) view.findViewById(R.id.addrs);
        EditText name = (EditText) view.findViewById(R.id.name);
        EditText building = (EditText) view.findViewById(R.id.building);
        EditText service = (EditText) view.findViewById(R.id.service);
        Button ok = (Button) view.findViewById(R.id.ok);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_simple_spinner, new String[]{"请选择项目地址", "地址1", "地址2", "地址3", "地址4"});
        addrs.setAdapter(adapter);

        class DialogOnClickListener implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.close) {
                    dialog.dismiss();
                } else if (v.getId() == R.id.ok) {

                    dialog.dismiss();
                }
            }
        }
        DialogOnClickListener listener = new DialogOnClickListener();
        close.setOnClickListener(listener);
        ok.setOnClickListener(listener);
        dialog.show();
    }


    private void showCategoryDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_my_brand_choose_category, null, false);
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        //dialog.setCancelable(false);
        dialog.setContentView(view);
        ImageView close = (ImageView) view.findViewById(R.id.close);
        GridView categories = (GridView) view.findViewById(R.id.categories);
        Button ok = (Button) view.findViewById(R.id.ok);
        List<Category> categoryList = new ArrayList<>(8);
        categoryList.add(Category.makeTop(1,"家装"));
        categoryList.add(Category.makeTop(2,"酒店"));
        categoryList.add(Category.makeTop(3,"别墅"));
        categoryList.add(Category.makeTop(4,"小区"));
        categoryList.add(Category.makeTop(5,"写字楼"));
        categoryList.add(Category.makeTop(6,"医院"));
        categoryList.add(Category.makeTop(7,"会所"));
        categoryList.add(Category.makeTop(8,"餐饮"));
        MyBrandChooseCategoryAdapter adapter = new MyBrandChooseCategoryAdapter(this, categoryList);
        categories.setAdapter(adapter);
        class DialogOnClickListener implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.close) {
                    dialog.dismiss();
                } else if (v.getId() == R.id.ok) {

                    dialog.dismiss();
                }
            }
        }
        DialogOnClickListener listener = new DialogOnClickListener();
        close.setOnClickListener(listener);
        ok.setOnClickListener(listener);
        dialog.show();
    }

    private void showContactInfoDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_my_brand_info_bottom_sheet, null, false);
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        //dialog.setCancelable(false);
        dialog.setContentView(view);
        ImageView close = (ImageView) view.findViewById(R.id.close);
        ImageView shopIcon = (ImageView) view.findViewById(R.id.shopIcon);
        ImageView avatar = (ImageView) view.findViewById(R.id.avatar);
        Spinner brands = (Spinner) view.findViewById(R.id.brands);
        TextView store = (TextView) view.findViewById(R.id.store);
        TextView hotline = (TextView) view.findViewById(R.id.hotline);
        TextView storeAddr = (TextView) view.findViewById(R.id.storeAddr);
        TextView userName = (TextView) view.findViewById(R.id.userName);
        TextView phoneNum = (TextView) view.findViewById(R.id.phoneNum);
        brands.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,new String[]{"品牌1","品牌2","品牌3","品牌4","品牌5","品牌6"}));
        Glide.with(this)
                .load("http://preview.quanjing.com/mhrf005/mhrf-cpmh-82526f07z.jpg")
                .into(shopIcon);
        Glide.with(this)
                .load("http://pic2116.ytqmx.com:82/2017/0713/40/1.jpg")
                .into(avatar);
        class DialogOnClickListener implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.close) {
                    dialog.dismiss();
                } else if (v.getId() == R.id.phoneNum) {
                    String str = ((TextView) v).getText().toString();
                    if (ActivityCompat.checkSelfPermission(MyBrandActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        Toast.makeText(MyBrandActivity.this,"请先授予本应用电话权限",Toast.LENGTH_LONG).show();
                        return;
                    }
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + str)));
                }
            }
        }
        DialogOnClickListener listener = new DialogOnClickListener();
        close.setOnClickListener(listener);
        phoneNum.setOnClickListener(listener);
        dialog.show();
    }
}
