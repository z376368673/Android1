package com.jhobor.zzb;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jhobor.zzb.adapter.ChooseSizeColorAdapter;
import com.jhobor.zzb.adapter.StoreProductsAdapter;
import com.jhobor.zzb.base.BaseActivity;
import com.jhobor.zzb.entity.Product;
import com.jhobor.zzb.entity.SizeColor;
import com.jhobor.zzb.view.InlineGridView;
import com.jhobor.zzb.view.ObservableScrollView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HallActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, AdapterView.OnItemClickListener {
    ImageView back,share;
    TextView subject,title,indicator,desc,chartlet,choice,price,productNo,product,details,store,shop,material,collect,hideProduct,hideDetails,hideStore;
    ViewPager viewPager;
    LinearLayout imagesOfDetails;
    InlineGridView products;
    FrameLayout header,floatBar,nav,hideNav;
    RelativeLayout infoBox;
    ObservableScrollView scrollView;

    List<String> imgs = new ArrayList<>(5);
    List<ImageView> imageViewList = new ArrayList<>(5);
    List<String> detailsImgs = new ArrayList<>();

    int scrollHeight = 0;
    List<Product> productList = new ArrayList<>(4);
    StoreProductsAdapter adapter;
    int lastPos = 0;
    SparseArray<TextView[]> sparseArray = new SparseArray<>(3);
    Map<String,String> props = new LinkedHashMap<>(4);
    PopupWindow actionPopupWin;
    BottomSheetDialog choosePopupWin;
    AlertDialog dialog;
    LinearLayout dialogContainer;
    ChooseSizeColorAdapter sizeColorAdapter;
    List<SizeColor> sizeColorList = new ArrayList<>();
    static final int REQUEST_CODE_COLLECT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall);

        back = (ImageView) findViewById(R.id.back);
        share = (ImageView) findViewById(R.id.share);
        subject = (TextView) findViewById(R.id.subject);
        indicator = (TextView) findViewById(R.id.indicator);
        title = (TextView) findViewById(R.id.title);
        desc = (TextView) findViewById(R.id.desc);
        chartlet = (TextView) findViewById(R.id.chartlet);
        choice = (TextView) findViewById(R.id.choice);
        price = (TextView) findViewById(R.id.price);
        productNo = (TextView) findViewById(R.id.productNo);
        product = (TextView) findViewById(R.id.product);
        details = (TextView) findViewById(R.id.details);
        store = (TextView) findViewById(R.id.store);
        hideProduct = (TextView) findViewById(R.id.hideProduct);
        hideDetails = (TextView) findViewById(R.id.hideDetails);
        hideStore = (TextView) findViewById(R.id.hideStore);
        shop = (TextView) findViewById(R.id.shop);
        material = (TextView) findViewById(R.id.material);
        collect = (TextView) findViewById(R.id.collect);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        imagesOfDetails = (LinearLayout) findViewById(R.id.imagesOfDetails);
        products = (InlineGridView) findViewById(R.id.products);
        header = (FrameLayout) findViewById(R.id.header);
        floatBar = (FrameLayout) findViewById(R.id.floatBar);
        nav = (FrameLayout) findViewById(R.id.nav);
        hideNav = (FrameLayout) findViewById(R.id.hideNav);
        infoBox = (RelativeLayout) findViewById(R.id.infoBox);
        scrollView = (ObservableScrollView) findViewById(R.id.scrollView);

        back.setOnClickListener(this);
        subject.setText("诺贝尔");
        subject.setVisibility(View.VISIBLE);
        share.setImageResource(R.mipmap.menu);
        share.setOnClickListener(this);
        imgs.add("http://pic2116.ytqmx.com:82/2017/0713/40/1.jpg");
        imgs.add("http://pic2116.ytqmx.com:82/2017/0713/40/1.jpg");
        imgs.add("http://pic2116.ytqmx.com:82/2017/0713/40/1.jpg");
        imgs.add("http://pic2116.ytqmx.com:82/2017/0713/40/1.jpg");
        imgs.add("http://pic2116.ytqmx.com:82/2017/0713/40/1.jpg");
        for (String str:imgs){
            ImageView iv = new ImageView(getBaseContext());
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(getBaseContext())
                    .load(str)
                    .into(iv);
            imageViewList.add(iv);
        }
        viewPager.setAdapter(new ProductPagerAdapter());
        indicator.setText(String.format(Locale.CHINA,"%d/%d",1,imgs.size()));
        viewPager.addOnPageChangeListener(this);

        scrollView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                scrollView.getViewTreeObserver().removeOnPreDrawListener(this);
                scrollHeight = scrollView.getHeight();
                return true;
            }
        });
        infoBox.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                infoBox.getViewTreeObserver().removeOnPreDrawListener(this);
                viewPager.getLayoutParams().height = scrollHeight - infoBox.getHeight();
                return true;
            }
        });
        detailsImgs.add("http://tupian.enterdesk.com/2016/gha/02/1901/02.jpg");
        detailsImgs.add("http://tupian.enterdesk.com/2016/gha/02/1901/02.jpg");
        detailsImgs.add("http://tupian.enterdesk.com/2016/gha/02/1901/02.jpg");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = 10;
        for (String str : detailsImgs){
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(params);
            Glide.with(this)
                    .load(str)
                    .into(iv);
            imagesOfDetails.addView(iv);
        }
        List<String> list = new ArrayList<String>(1){{add("http://img15.3lian.com/2015/f2/147/d/74.jpg");}};
        productList.add(new Product(1,5200,"雪花白",list));
        productList.add(new Product(1,5200,"雪花白",list));
        productList.add(new Product(1,5200,"雪花白",list));
        productList.add(new Product(1,5200,"雪花白",list));
        nav.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                nav.getViewTreeObserver().removeOnPreDrawListener(this);
                int itemHeight = (scrollHeight - nav.getHeight())/2;
                Log.i("itemHeight>>", String.valueOf(itemHeight));
                adapter = new StoreProductsAdapter(getBaseContext(),productList,itemHeight);
                products.setAdapter(adapter);
                return true;
            }
        });
        products.setOnItemClickListener(this);
        product.setOnClickListener(this);
        hideProduct.setOnClickListener(this);
        details.setOnClickListener(this);
        hideDetails.setOnClickListener(this);
        store.setOnClickListener(this);
        hideStore.setOnClickListener(this);
        chartlet.setOnClickListener(this);
        choice.setOnClickListener(this);
        shop.setOnClickListener(this);
        material.setOnClickListener(this);
        collect.setOnClickListener(this);
        sparseArray.put(0,new TextView[]{product,hideProduct});
        sparseArray.put(1,new TextView[]{details,hideDetails});
        sparseArray.put(2,new TextView[]{store,hideStore});
        scrollView.setOnScrollChangeListener(new ObservableScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int top) {
                float y = nav.getY();
                float y2 = products.getY();
                if (top>y2-300) {
                    tab(2);
                }else if (top>y-200){
                    tab(1);
                }else {
                    tab(0);
                }
                if (top>=y){
                    hideNav.setVisibility(View.VISIBLE);
                }else {
                    hideNav.setVisibility(View.INVISIBLE);
                }
            }
        });
        props.put("素材类型","贴图");
        props.put("文件格式","jpg");
        props.put("文件大小","12M");
//        props2.put("素材类型","3D模型");
//        props2.put("文件格式","MAX");
//        props2.put("文件大小","50M");
//        props2.put("原始尺寸","是/否");
    }

    @Override
    public void onClick(View v) {
        if (v==back){
            finish();
        }else if (v==share){
            showActionPopupWin();
        }else if (v==product||v==hideProduct){
            scrollView.smoothScrollTo(0,0);
        }else if (v==details||v==hideDetails){
            scrollView.smoothScrollTo(0, (int) nav.getY());
        }else if (v==store||v==hideStore){
            scrollView.smoothScrollTo(0, (int) products.getY());
        }else if (v==chartlet){
            showDailog(props);
        }else if (v==choice){
            showBottomPopupWin();
        }else if (v.getId()==R.id.collect){
            startActivityForResult(new Intent(this,CollectionActivity.class),REQUEST_CODE_COLLECT);
        }else if (v==material){
//            startActivity(new Intent(this,GetMaterialActivity.class));
            startActivity(new Intent(this,GetMaterial2Activity.class));
        }else if (v==shop){
            startActivity(new Intent(this,ShopActivity.class));
        }
    }

    private void showActionPopupWin() {
        if (actionPopupWin ==null) {
            View view = getLayoutInflater().inflate(R.layout.popup_win_right_top, null, false);
            actionPopupWin = new PopupWindow(this);
            actionPopupWin.setContentView(view);
            actionPopupWin.setFocusable(true);
            actionPopupWin.showAsDropDown(share);
            view.findViewById(R.id.actionCollect).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(">>","collect");
                    actionPopupWin.dismiss();
                }
            });
            view.findViewById(R.id.actionShare).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(">>","share");
                    actionPopupWin.dismiss();
                }
            });
        }else if (actionPopupWin.isShowing()){
            actionPopupWin.dismiss();
        }else {
            actionPopupWin.showAsDropDown(share);
        }
    }

    private void showBottomPopupWin() {
        if (choosePopupWin==null){
            View view = getLayoutInflater().inflate(R.layout.popup_win_bottom_choose,null,false);
            choosePopupWin = new BottomSheetDialog(this);
            choosePopupWin.setContentView(view);
            ImageView img = (ImageView) view.findViewById(R.id.productImg);
            view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    choosePopupWin.dismiss();
                }
            });
            TextView price = (TextView) view.findViewById(R.id.price);
            ListView listView = (ListView) view.findViewById(R.id.listView);
            Button ok = (Button) view.findViewById(R.id.ok);
            sizeColorList.add(new SizeColor(0,0,"0.2x0.3m","湖蓝色"));
            sizeColorList.add(new SizeColor(0,0,"0.3x0.5m","橙紫色"));
            sizeColorList.add(new SizeColor(0,0,"0.5x0.5m","棕褐色"));
            sizeColorAdapter = new ChooseSizeColorAdapter(this,sizeColorList);
            listView.setAdapter(sizeColorAdapter);
        }
        choosePopupWin.show();
    }

    private void showDailog(Map<String, String> props) {
        LayoutInflater inflater = getLayoutInflater();
        if (dialog==null){
            dialogContainer = (LinearLayout) inflater.inflate(R.layout.dialog_round_grid,null,false);
            dialog = new AlertDialog.Builder(this).setView(dialogContainer).create();
            dialogContainer.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            for (Map.Entry<String,String> entry: props.entrySet()){
                View v = inflater.inflate(R.layout.item_dialog_property,null,false);
                TextView key = (TextView) v.findViewById(R.id.key);
                TextView value = (TextView) v.findViewById(R.id.value);
                key.setText(entry.getKey());
                value.setText(entry.getValue());
                dialogContainer.addView(v);
            }
        }
        dialog.show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        indicator.setText(String.format(Locale.CHINA,"%d/%d",position+1,imgs.size()));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private class ProductPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imgs.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imageViewList.get(position));
            return imageViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViewList.get(position));
        }
    }

    void tab(int pos){
        if (pos==lastPos)
            return;
        int color = ContextCompat.getColor(this,R.color.grayText2);
        for (TextView tv:sparseArray.get(lastPos)){
            tv.setTextColor(color);
        }
        color = ContextCompat.getColor(this,R.color.purple);
        for (TextView tv:sparseArray.get(pos)){
            tv.setTextColor(color);
        }
        lastPos = pos;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE_COLLECT&&resultCode==RESULT_OK){
            View view = getLayoutInflater().inflate(R.layout.dialog_show_msg, null, false);
            TextView msg = (TextView) view.findViewById(R.id.msg);
            msg.setText("收藏成功！");
            final AlertDialog dialog = new AlertDialog.Builder(this)
                    .setView(view)
                    .show();
            view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
    }
}
