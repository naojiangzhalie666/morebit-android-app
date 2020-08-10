package com.zjzy.morebit.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.makeramen.roundedimageview.RoundedImageView;
import com.youth.banner.loader.ImageLoader;


public class GlideImageLoader extends ImageLoader {

    private int mTransparent;

    public GlideImageLoader(int transparent) {
        mTransparent = transparent;
    }

    public GlideImageLoader() {
    }

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择


        if (mTransparent == 0) {
            LoadImgUtils.setImg(context.getApplicationContext(), imageView, (String) path);
        } else {
            LoadImgUtils.setImg(context.getApplicationContext(), imageView, (String) path, mTransparent);
        }
    }

    @Override
    public ImageView createImageView(Context context) {
        RoundedImageView img = new RoundedImageView(context);
        img.setCornerRadius(DensityUtil.dip2px(context,0));
        return img;
    }
}
