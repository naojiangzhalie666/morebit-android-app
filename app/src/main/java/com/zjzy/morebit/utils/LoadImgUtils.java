package com.zjzy.morebit.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.zjzy.morebit.Module.push.Logger;
import com.zjzy.morebit.R;
import com.zjzy.morebit.utils.UI.CornerTransform;

import java.io.File;
import java.util.concurrent.ExecutionException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by fengr on 2018/5/28.
 * 加载图片
 */

public class LoadImgUtils {

    public static RequestOptions mDefaultOptions = new RequestOptions()
            .placeholder(R.drawable.icon_default)
            .error(R.drawable.icon_default);

    public static RequestOptions noPlaceholderDefaultOptions = new RequestOptions()
            .error(R.drawable.icon_default);

    /**
     * 加载列表图
     *
     * @param context
     * @param iv
     * @param url
     */
    public static void setImg(Context context, ImageView iv, String url) {
        if (TextUtils.isEmpty(url)) {
            iv.setImageResource(R.drawable.icon_default);
        } else {
            setImg(context, iv, url, R.drawable.icon_default);
        }


    }

    /**
     * 加载头像
     *
     * @param context
     * @param iv
     * @param url
     */
    public static void setImgHead(Context context, ImageView iv, String url) {
        if (TextUtils.isEmpty(url)) {
            iv.setImageResource(R.drawable.logo);
        } else {
            setImg(context, iv, url, R.drawable.logo);
        }

    }

    public static void setImg(final Context context, final ImageView iv, final String url, int placeholderRes) {
        if (context == null || iv == null) {
            return;
        }
        try {
            final RequestOptions options = new RequestOptions()
                    .dontAnimate()
                    .centerCrop()
                    .placeholder(placeholderRes)
                    .error(placeholderRes);

            final RequestOptions options2 = new RequestOptions()
                    .dontAnimate()
                    .placeholder(placeholderRes)
                    .error(placeholderRes);
            Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .apply(options2)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            int imageHeight = resource.getHeight();
                            if(imageHeight > 4096) {
                                imageHeight = 4096;
                                ViewGroup.LayoutParams layoutParams = iv.getLayoutParams();
                                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                                layoutParams.height = imageHeight;
                                iv.setLayoutParams(layoutParams);
                                Glide.with(context)
                                        .load(url)
                                        .apply(options)
                                        .into(iv);

                            }else{
                                Glide.with(context)
                                        .load(url)
                                        .apply(options2)
                                        .into(iv);
                            }
                            }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError outOfMemoryError) {
            System.gc();
            Logger.e("Glide 加载图片 OOM");
        }

    }


    /**
     * 添加加载回调的Listener
     * @param context
     * @param iv
     * @param url
     * @param showPlaceholderImg
     * @param mGlideLoadListener
     */
    public static void setImg(Context context, ImageView iv, String url, boolean showPlaceholderImg, final GlideLoadListener mGlideLoadListener) {
        if (context == null || iv == null || url == null) {
            return;
        }

        try {
            if (showPlaceholderImg) {
                Glide.with(context)
                        .load(url)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                if(null != mGlideLoadListener){
                                    mGlideLoadListener.loadError();
                                }
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                if(null != mGlideLoadListener){
                                    mGlideLoadListener.loadSuccess();
                                }
                                return false;
                            }
                        })
                        .apply(mDefaultOptions)
                        .into(iv);
            } else {
                Glide.with(context)
                        .load(url)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                if(null != mGlideLoadListener){
                                    mGlideLoadListener.loadError();
                                }
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                if(null != mGlideLoadListener){
                                    mGlideLoadListener.loadSuccess();
                                }
                                return false;
                            }
                        })
                        .apply(noPlaceholderDefaultOptions)
                        .into(iv);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if(null != mGlideLoadListener){
                mGlideLoadListener.loadError();
            }
        } catch (OutOfMemoryError outOfMemoryError) {
            if(null != mGlideLoadListener){
                mGlideLoadListener.loadError();
            }
            System.gc();
            Logger.e("Glide 加载图片 OOM");
        }

    }

    /**
     * @param context
     * @param iv
     * @param url
     * @param showPlaceholderImg 是否显示ImageView初始化占位图
     */
    public static void setImg(Context context, ImageView iv, String url, boolean showPlaceholderImg) {
        if (context == null || iv == null || url == null) {
            return;
        }

        try {
            if (showPlaceholderImg) {
                Glide.with(context)
                        .load(url)
                        .apply(mDefaultOptions)
                        .into(iv);
            } else {
                Glide.with(context)
                        .load(url)
                        .apply(noPlaceholderDefaultOptions)
                        .into(iv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError outOfMemoryError) {
            System.gc();
            Logger.e("Glide 加载图片 OOM");
        }

    }

    /**
     * 缓存变色问题
     *
     * @param context
     * @param iv
     * @param url
     * @param placeholderRes
     */
    public static void setCacheImg(Context context, ImageView iv, String url, int placeholderRes) {
        if (context == null || iv == null || url == null) {
            return;
        }

        try {
            RequestOptions options = new RequestOptions()
                    .placeholder(placeholderRes)
                    .error(placeholderRes)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            Glide.with(context)
                    .load(url)
                    .apply(options)
                    .into(iv);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError outOfMemoryError) {
            System.gc();
            Logger.e("Glide 加载图片 OOM");
        }

    }

    /**
     * 圆角图片
     *
     * @param context
     * @param iv
     * @param url
     */
    public static void setImgCircle(Context context, ImageView iv, String url) {

        setImgCircle(context, iv, url, R.drawable.head_icon);
    }

    /**
     * 加载本地图片
     *
     * @param context
     * @param iv
     * @param path
     */
    public static void setImgPath(Context context, ImageView iv, String path) {
        //本地文件
        File file = null;
        try {
            file = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file == null) return;
        //加载图片
        try {
            Glide.with(context).load(file).apply(mDefaultOptions).into(iv);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError outOfMemoryError) {
            System.gc();
            Logger.e("Glide 加载图片 OOM");
        }
    }

    public static void setImgCircle(Context context, ImageView iv, String url, int placeholderRes) {
        if (context == null || iv == null || TextUtils.isEmpty(url)) {
            return;
        }

        //设置图片圆角角度
        //        RoundedCorners roundedCorners = new RoundedCorners(30);
        ////通过RequestOptions扩展功能
        //        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);

        try {
            RequestOptions options = RequestOptions.circleCropTransform()
                    .placeholder(placeholderRes)
                    .error(placeholderRes);

            Glide.with(context).load(url).apply(options).into(iv);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError outOfMemoryError) {
            System.gc();
            Logger.e("Glide 加载图片 OOM");
        }


        //
        //        Glide.with(context)
        //                .load(url)
        //                .placeholder(placeholderRes)
        //                .error(placeholderRes)
        //                .transform(new GlideCircleTransform(context))
        //                .into(iv);
    }

    //    /**
    //     * 加载图片返回GlideDrawable
    //     *
    //     * @param context
    //     * @param url
    //     * @param action
    //     */
    //    public static void LoadImgToDrawable(Context context, String url, final MyAction.One<GlideDrawable> action) {
    //        if (context == null || url == null) {
    //            return;
    //        }
    //        Glide.with(context).load(url).into(new SimpleTarget<GlideDrawable>() { // 加上这段代码 可以解决
    //            @Override
    //            public void onResourceReady(GlideDrawable resource,
    //                                        GlideAnimation<? super GlideDrawable> glideAnimation) {
    //                if (action != null)
    //                    action.invoke(resource);
    //            }
    //
    //            @Override
    //            public void onLoadFailed(Exception e, Drawable errorDrawable) {
    //                super.onLoadFailed(e, errorDrawable);
    //                if (action != null)
    //                    action.invoke(null);
    //            }
    //        });
    //    }


    /**
     * 加载图片返回Bitmap
     *
     * @param context
     * @param downPath
     * @return
     */
    public static Observable<Bitmap> getImgToBitmapObservable(final Context context, final String downPath) {
        return getBitmapObservable(context, downPath, mDefaultOptions);
    }

    public static Observable<Bitmap> getBitmapObservable(final Context context, final String downPath, final RequestOptions options) {
        return Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(final ObservableEmitter<Bitmap> emitter) throws Exception {
                if (context == null || downPath == null) {
                    emitter.onError(new Exception("加载图片失败"));
                    return;
                }

                try {
                    MyLog.threadName();
                    Glide.with(context)
                            .load(downPath)
                            .apply(options)
                            .into(new SimpleTarget<Drawable>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    MyLog.threadName();
                                    if (resource instanceof BitmapDrawable) {
                                        BitmapDrawable bd = (BitmapDrawable) resource;
                                        Bitmap bitmap = bd.getBitmap();
                                        if (bitmap == null) {
                                            emitter.onError(new Exception("加载图片失败"));
                                        } else {
                                            emitter.onNext(bitmap);
                                        }
                                    } else {
                                        emitter.onError(new Exception("加载图片失败"));
                                    }
                                }

                                @Override
                                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                    super.onLoadFailed(errorDrawable);
                                    emitter.onError(new Exception("加载图片失败"));
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (OutOfMemoryError outOfMemoryError) {
                    System.gc();
                    Logger.e("Glide 加载图片 OOM");
                }


            }
        });
    }


    public static Observable<File> getImgToFileObservable(final Context context, final String downPath) {

        return getImageFileObservable(context, downPath, mDefaultOptions);
    }

    public static Observable<File> getImageFileObservable(final Context context, final String downPath, final RequestOptions options) {
        return Observable.create(new ObservableOnSubscribe<File>() {
            @Override
            public void subscribe(final ObservableEmitter<File> emitter) throws Exception {
                if (context == null || downPath == null) {
                    emitter.onError(new Exception("加载图片失败"));
                    return;
                }

                try {
                    Glide.with(context)
                            .load(downPath)
                            .apply(options)
                            .downloadOnly(new SimpleTarget<File>() {
                                @Override
                                public void onResourceReady(@NonNull File file, @Nullable Transition<? super File> transition) {
                                    if (file == null) {
                                        emitter.onError(new Exception("加载图片失败"));
                                    } else {
                                        emitter.onNext(file);
                                    }
                                }

                                @Override
                                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                    super.onLoadFailed(errorDrawable);
                                    emitter.onError(new Exception("加载图片失败"));
                                }
                            });

                } catch (Exception e) {
                    e.printStackTrace();
                } catch (OutOfMemoryError outOfMemoryError) {
                    System.gc();
                    Logger.e("Glide 加载图片 OOM");
                }


            }
        });
    }




    /**
     * 加载图片返回Bitmap  在子线程调用
     *
     * @param context
     * @param downPath
     * @return
     */
    public static Bitmap getImgBitmapOnIo(final Context context, final String downPath) throws ExecutionException, InterruptedException {
        return getImgBitmapOnIo(context, downPath, 500, 500);
    }

    public static Bitmap getImgBitmapOnIo(final Context context, final String downPath, int width, int height) throws ExecutionException, InterruptedException {
        if (TextUtils.isEmpty(downPath)) {
            return null;
        }
        Drawable drawable = Glide.with(context)
                .load(downPath)
                .into(500, 500)
                .get();
        BitmapDrawable bd = (BitmapDrawable) drawable;
        if (bd != null) {
            return bd.getBitmap();
        } else {
            return null;
        }
    }

    /**
     * 加载图片返回Bitmap  在子线程调用
     *
     * @param context
     * @param downPath
     * @return
     */
    public static Bitmap getImgBitmapOnIo1(final Context context, final String downPath) throws ExecutionException, InterruptedException {
        return getImgBitmapOnIo(context, downPath, Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
    }

    /**
     * 加载图片返回Bitmap
     *
     * @param context
     * @param downPath
     * @return
     */
    public static Observable<Bitmap> getImgCircleToBitmapObservable(final Context context, final String downPath) {


        //设置图片圆角角度
        RequestOptions options = RequestOptions.circleCropTransform()
                .placeholder(R.drawable.icon_default)
                .error(R.drawable.icon_default);


        return getBitmapObservable(context, downPath, options);
    }


    /**
     * 生成头像log
     *
     * @param bitmap   原图
     * @param bgBitmap
     * @return
     */
    public static Bitmap getHeadBitmap(Bitmap bitmap, Bitmap bgBitmap) {
        int width = bgBitmap.getWidth();
        int padding = width * 3 / 10;
        Bitmap headBitmap = getBitmapByBitmap(bitmap, bgBitmap.getWidth() - padding, bgBitmap.getHeight() - padding);
        // 背图
        Bitmap newBitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        //        Bitmap bgBitmap = getBitmapByBitmap(context,icon_default, num / 2, num / 2);
        canvas.drawBitmap(bgBitmap, 0, 0, paint);
        // 生成白色的
        paint.setColor(Color.WHITE);
        canvas.drawBitmap(headBitmap, padding / 2, padding / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        // 画正方形的
        canvas.drawRect(0, 0, width, width, paint);
        return newBitmap;
    }

    /**
     * 设置bitmap四周白边
     *
     * @param bitmap           原图
     * @param whiteBitmapWidth
     * @return
     */
    public static Bitmap bg2WhiteBitmap(Bitmap bitmap, int whiteBitmapWidth) {
        int size = bitmap.getWidth() < bitmap.getHeight() ? bitmap.getWidth() : bitmap.getHeight();
        int num = whiteBitmapWidth * 2;
        int sizebig = size + num;
        // 背图
        Bitmap newBitmap = Bitmap.createBitmap(sizebig, sizebig, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        // 生成白色的
        paint.setColor(Color.WHITE);
        canvas.drawBitmap(bitmap, num / 2, num / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        // 画正方形的
        canvas.drawRect(0, 0, sizebig, sizebig, paint);
        return newBitmap;
    }

    /**
     * 画布上能分配给Bitmap的空间是有限的，为了让图片全部显示就对图片做适当的压缩
     *
     * @param bitMap
     * @param newHeight
     * @param newWidth  设置想要的大小
     */
    private static Bitmap getBitmapByBitmap(Bitmap bitMap, float newWidth, float newHeight) {
        int width = bitMap.getWidth();
        int height = bitMap.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        return Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true);
    }

    /**
     * @param context
     * @param iv
     * @param url
     * @param placeholderRes
     * @param radius         角度
     * @param leftTop        是否画圆角（左上）  false 圆角，true不要
     * @param rightTop       是否画圆角（右上）
     * @param leftBottom     是否画圆角（左下）
     * @param rightBottom    是否画圆角（右下）
     */
    public static void loadingCornerBitmap(Context context, ImageView iv, String url, int placeholderRes,
                                           int radius,
                                           boolean leftTop, boolean rightTop, boolean leftBottom, boolean rightBottom) {
        if (context == null || url == null || iv == null) return;
        int i = DensityUtil.dip2px(context, radius);
        CornerTransform transformation = new CornerTransform(context, i);
        //只是绘制左上角和右上角圆角
        transformation.setExceptCorner(leftTop, rightTop, leftBottom, rightBottom);
        RequestOptions options = new RequestOptions()
                .placeholder(placeholderRes)
                .error(placeholderRes)
                .transform(transformation);
        try {
            Glide.with(context).
                    load(url).
                    apply(options).
                    into(iv);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError outOfMemoryError) {
            System.gc();
            Logger.e("Glide 加载图片 OOM");
        }
    }

    /**
     * @param context
     * @param iv
     * @param url
     * @param placeholderRes
     * @param radius         角度
     */
    public static void loadingCornerBitmap(Context context, ImageView iv, String url, int placeholderRes, int radius) {
        loadingCornerBitmap(context, iv, url, placeholderRes, radius, false, false, false, false);
    }

    /**
     * @param context
     * @param iv
     * @param url
     * @param radius  角度
     */
    public static void loadingCornerTop(Context context, ImageView iv, String url, int radius) {
        loadingCornerBitmap(context, iv, url, R.drawable.icon_default, radius, false, false, true, true);
    }

    /**
     * @param context
     * @param iv
     * @param url
     * @param radius  角度
     */
    public static void loadingCornerBitmap(Context context, ImageView iv, String url, int radius) {
        loadingCornerBitmap(context, iv, url, R.drawable.icon_default, radius);
    }

    /**
     * @param context
     * @param iv
     * @param url
     */
    public static void loadingCornerBitmap(Context context, ImageView iv, String url) {
        loadingCornerBitmap(context, iv, url, R.drawable.icon_default, 5);
    }


    /**
     * 加载背景
     *
     * @param context
     * @param url
     */
    public static void setViewBackground(Context context, final View view, String url) {
        if (url == null || view == null) {
            return;
        }

        try {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.color.white)
                    .error(R.color.white);
            Glide.with(context).load(url)
                    .apply(options)
                    .into(new SimpleTarget<Drawable>() {
                        /**
                         * The method that will be called when the resource load has finished.
                         *
                         * @param resource   the loaded resource.
                         * @param transition
                         */
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            view.setBackground(resource.getCurrent());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError outOfMemoryError) {
            System.gc();
            Logger.e("Glide 加载图片 OOM");
        }

    }

    /**
     * 判断文件是否为图片
     * <p>
     * picture.endsWith("gif") ||
     * picture.endsWith("tif") ||
     * picture.endsWith("bmp")
     */
    public static boolean isPicture(String picture) {
        if (TextUtils.isEmpty(picture)) return false;
        if (picture.startsWith("http")) {
            if (picture.endsWith("jpg") ||
                    picture.endsWith("png")||
                    picture.endsWith("jpeg")||picture.contains("?x-oss-process")||picture.startsWith("http://kaola-pop.oss.kaolacdn.com")||picture.contains("haitao")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public interface  GlideLoadListener {
        void loadError();
        void loadSuccess();
    }
}
