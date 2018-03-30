package com.jhobor.ddc.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jhobor.ddc.R;
import com.jhobor.ddc.base.BaseApplication;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by Administrator on 2016/12/24.
 */

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(final Context context, final Object path, ImageView imageView) {
        Glide.with(context)
                .load(path)
                .error(R.mipmap.load_img_fail)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
    }
}
