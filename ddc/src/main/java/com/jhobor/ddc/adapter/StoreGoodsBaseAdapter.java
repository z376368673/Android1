package com.jhobor.ddc.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jhobor.ddc.R;
import com.jhobor.ddc.base.BaseApplication;
import com.jhobor.ddc.entity.BezierEvaluator;
import com.jhobor.ddc.entity.Goods;
import com.jhobor.ddc.entity.Point;
import com.jhobor.ddc.entity.ShopCar;
import com.jhobor.ddc.greendao.ShopCarDao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/1/5.
 */

public class StoreGoodsBaseAdapter extends BaseAdapter {
    private List<Goods> goodsList;
    private Activity activity;
    private Context context;
    private LayoutInflater inflater;
    private ImageView shopCar;
    private TextView countTextView;
    private TextView moneyTextView;
    private String storeName;
    private int storeId;

    public StoreGoodsBaseAdapter(List<Goods> msgList, Activity activity, ImageView shopCar, TextView countTextView, TextView moneyTextView, int storeId, String storeName) {
        this.goodsList = msgList;
        this.context = this.activity = activity;
        this.shopCar = shopCar;
        this.countTextView = countTextView;
        this.moneyTextView = moneyTextView;
        this.storeId = storeId;
        this.storeName = storeName;
        inflater = LayoutInflater.from(context);
    }

    public void setMoneyTextView(TextView moneyTextView) {
        this.moneyTextView = moneyTextView;
    }

    public void setShopCar(ImageView shopCar) {
        this.shopCar = shopCar;
    }

    public void setCountTextView(TextView countTextView) {
        this.countTextView = countTextView;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    @Override
    public int getCount() {
        return goodsList.size();
    }

    @Override
    public Object getItem(int position) {
        return goodsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_store_goods, parent, false);
            viewHolder.goodsPic = (ImageView) convertView.findViewById(R.id.goodsPic);
            viewHolder.add = (ImageView) convertView.findViewById(R.id.add);
            viewHolder.reduce = (ImageView) convertView.findViewById(R.id.reduce);
            viewHolder.goodsName = (TextView) convertView.findViewById(R.id.goodsName);
            viewHolder.price = (TextView) convertView.findViewById(R.id.price);
            viewHolder.salesVolume = (TextView) convertView.findViewById(R.id.salesVolume);
            viewHolder.count = (TextView) convertView.findViewById(R.id.count);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Goods goods = goodsList.get(position);
        Glide.with(context)
                .load(goods.getPicture())
                .error(R.mipmap.load_img_fail)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(viewHolder.goodsPic);
        viewHolder.goodsName.setText(goods.getName());
        viewHolder.price.setText(String.format(Locale.CHINA, "￥%.1f", goods.getPrice()));
        viewHolder.salesVolume.setText(String.format(Locale.CHINA, "销量 %d", goods.getSalesVolume()));
        viewHolder.count.setText(String.valueOf(goods.getCount()));
        viewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = viewHolder.count.getText().toString();
                int count = Integer.parseInt(text) + 1;
                goods.setCount(count);
                viewHolder.count.setText(String.valueOf(count));

                int fromPosition[] = new int[2];
                v.getLocationInWindow(fromPosition);
                int[] toPosition = new int[2];
                shopCar.getLocationInWindow(toPosition);
                toPosition[0] += 15;
                toPosition[1] += 15;
                fromPosition[0] += 5;
                fromPosition[1] += 5;
                Point startPoint = new Point(fromPosition[0], fromPosition[1]);
                Point endPoint = new Point(toPosition[0], toPosition[1]);

                String s = countTextView.getText().toString();
                int sum = Integer.parseInt(s) + 1;
                float amount = (float) BaseApplication.dataMap.get("amount") + goods.getPrice();
                BaseApplication.dataMap.put("sum", sum);
                BaseApplication.dataMap.put("amount", amount);
                updateViewDatas(sum, amount);
                startAnima(startPoint, endPoint, 800);
                ShopCarDao shopCarDao = BaseApplication.dbService.getShopCarDao();
                if (count == 1) {
                    Log.i("greenDao insert>>", String.format(Locale.CHINA, "goodsId: %d", goods.getId()));
                    long id = shopCarDao.insert(new ShopCar(goods.getId(), goods.getName(), goods.getPrice(), goods.getPicture(), count, storeId, storeName, new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).format(new Date())));
                    Log.i("greenDao insert>>", String.format(Locale.CHINA, "shopCarId: %d", id));
                } else {
                    ShopCar shopCar = shopCarDao.queryBuilder().where(ShopCarDao.Properties.GoodsId.eq(goods.getId())).unique();
                    shopCar.setCount(count);
                    shopCarDao.update(shopCar);
                    Log.i("greenDao update>>", shopCar.toString());
                }
            }
        });
        viewHolder.reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = viewHolder.count.getText().toString();
                int count = Integer.parseInt(text) - 1;
                if (count >= 0) {
                    goods.setCount(count);
                    viewHolder.count.setText(String.valueOf(count));
                    String s = countTextView.getText().toString();
                    int sum = Integer.parseInt(s) - 1;

                    BaseApplication.dataMap.put("sum", sum);
                    float amount = (float) BaseApplication.dataMap.get("amount") - goods.getPrice();
                    BaseApplication.dataMap.put("amount", amount);
                    countTextView.setText(String.valueOf(sum));
                    moneyTextView.setText(String.format(Locale.CHINA, "%.2f元", amount));
                    ShopCarDao shopCarDao = BaseApplication.dbService.getShopCarDao();
                    if (count == 0) {
                        ShopCar shopCar = shopCarDao.queryBuilder().where(ShopCarDao.Properties.GoodsId.eq(goods.getId())).unique();
                        shopCarDao.delete(shopCar);
                        Log.i("greenDao delete>>", "删除成功");
                    } else {
                        ShopCar shopCar = shopCarDao.queryBuilder().where(ShopCarDao.Properties.GoodsId.eq(goods.getId())).unique();
                        shopCar.setCount(count);
                        shopCarDao.update(shopCar);
                        Log.i("greenDao update>>", shopCar.toString());
                    }
                }
            }
        });

        return convertView;
    }

    private void startAnima(Point startPoint, Point endPoint, int millis) {
        Point controlPoint = new Point();
        controlPoint.x = startPoint.x + (endPoint.x - startPoint.x) / 2;
        controlPoint.y = startPoint.y - 200;

        ValueAnimator objectAnimator = ObjectAnimator.ofObject(new BezierEvaluator(controlPoint), startPoint, endPoint);
        objectAnimator.setDuration(millis);
        final ViewGroup viewGroup = (ViewGroup) activity.getWindow().getDecorView();
        final ImageView iv = new ImageView(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(35, 35);
        iv.setLayoutParams(params);
        iv.setImageResource(R.mipmap.store_goods_pic);
        viewGroup.addView(iv);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Point point = (Point) animation.getAnimatedValue();
                iv.setX(point.x);
                iv.setY(point.y);
            }
        });
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                viewGroup.removeView(iv);
                Animation scaleAnimation = AnimationUtils.loadAnimation(context, R.anim.anim_scale);
                shopCar.startAnimation(scaleAnimation);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        objectAnimator.start();
    }

    private void updateViewDatas(int sum, float amount) {
        countTextView.setText(String.valueOf(sum));
        moneyTextView.setText(String.format(Locale.CHINA, "%.2f元", amount));
    }

    private static class ViewHolder {
        ImageView goodsPic, reduce, add;
        TextView goodsName, price, salesVolume, count;
    }
}
