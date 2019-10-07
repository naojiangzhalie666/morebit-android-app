package com.markermall.cat.utils.UI;

import android.content.Context;
import android.widget.ImageView;

import com.markermall.cat.utils.DensityUtil;
import com.markermall.cat.utils.LoadImgUtils;
import com.youth.banner.loader.ImageLoader;


public class GlideImageCircularLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
        if (context == null || path == null) return;

        int i = DensityUtil.dip2px(context, 5);

        LoadImgUtils.loadingCornerBitmap(context,imageView, (String) path);
//
//        CornerTransform transformation = new CornerTransform(context, i);
//        //只是绘制左上角和右上角圆角
//        transformation.setExceptCorner(false, false, false, false);
//        Glide.with(context).
//                load(path).
//                asBitmap()
//                .load(path)
//                .error(R.drawable.icon_default).
//                transform(transformation)
//                .into(imageView);
    }

//    @Override
//    public ImageView createImageView(Context context) {
//        //圆角
//        return new RoundAngleImageView(context);
//    }
}
