package com.zjzy.morebit.utils;

import android.content.Context;
import android.widget.ImageView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.youth.banner.loader.ImageLoader;


public class GlideImageLoader2 extends ImageLoader {

    private int mTransparent;

    public GlideImageLoader2(int transparent) {
        mTransparent = transparent;
    }

    public GlideImageLoader2() {
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
        img.setCornerRadius(DensityUtil.dip2px(context,4));
        return img;
    }
}
