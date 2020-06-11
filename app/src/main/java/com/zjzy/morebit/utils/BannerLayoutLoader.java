package com.zjzy.morebit.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.youth.banner.loader.ImageLoaderInterface;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.R;

/**
 * (Hangzhou)
 *
 * @author:
 * @date :  2019/9/21 15:04
 * Summary:
 */
public class BannerLayoutLoader implements ImageLoaderInterface<View> {


    private Drawable mresource;
    @Override
    public void displayImage(final Context context, Object path, final View imageView) {

        /**
         注意：
         1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
         2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
         传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
         切记不要胡乱强转！
         */
        //   imageView.setScaleType(ImageView.ScaleType.FIT_XY);//拉伸图片，全部显示在imageView中
//        Glide.with(context).load(path).into(new SimpleTarget<Drawable>() {
//            @Override
//            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                View view = LayoutInflater.from(context).inflate(R.layout.invate_code, null);
//                FrameLayout review = view.findViewById(R.id.loadimg);
//                if (resource!=null){
//                    review.setBackgroundDrawable(resource);
//                }
//               // imageView.findViewById(R.id.review).setBackground(resource);
//            }
//        });
//        Glide.with(context).load(path).into(new SimpleTarget<Drawable>() {
//            @Override
//            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<?
//                    super Drawable> transition) {
//                imageView.findViewById(R.id.review).setBackground(resource);
//            }
//        });

        Glide.with(context)
                .load(path)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        imageView.findViewById(R.id.review).setBackground(resource);
                    }
                });
       // LoadImgUtils.setImg(context.getApplicationContext(), (ImageView) imageView, (String) path);
//        if (mTransparent == 0) {
//            LoadImgUtils.setImg(context.getApplicationContext(), (ImageView) imageView, (String) path);
//        } else {
//            LoadImgUtils.setImg(context.getApplicationContext(), (ImageView) imageView, (String) path, mTransparent);
//        }

    }

    //提供createImageView 方法，如果不用可以不重写这个方法，主要是方便自定义ImageView的创建
    @Override
    public View createImageView(Context context) {

        View view = LayoutInflater.from(context).inflate(R.layout.invate_code, null);
        String wxresginlink = SPUtils.getInstance().getString(C.sp.WXRESGINLINK);
        String  mInvite_code = UserLocalData.getUser(context).getInviteCode();
        ImageView img_code = view.findViewById(R.id.img_code);
            TextView invate_text = view.findViewById(R.id.invate_text);
            invate_text.setVisibility(View.VISIBLE);
            invate_text.setText("邀请码:" + mInvite_code);
            if (wxresginlink!=null){
                Bitmap qrCode = QRCodeGenerater.createQRCode(wxresginlink, 400, 400);
                img_code.setImageBitmap(qrCode);
            }
        return view;


    }


}
