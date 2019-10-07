//package com.jf.my.utils;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Matrix;
//import android.graphics.Paint;
//import android.graphics.PorterDuff;
//import android.graphics.PorterDuffXfermode;
//import android.graphics.drawable.Drawable;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.ImageView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
//import com.bumptech.glide.request.target.SimpleTarget;
//import com.bumptech.glide.request.target.Target;
//import com.bumptech.glide.request.target.ViewTarget;
//import com.jf.my.R;
//import com.jf.my.utils.UI.CornerTransform;
//import com.jf.my.utils.UI.GlideCircleTransform;
//import com.jf.my.utils.action.MyAction;
//
//import java.util.concurrent.ExecutionException;
//
//import io.reactivex.Observable;
//import io.reactivex.ObservableEmitter;
//import io.reactivex.ObservableOnSubscribe;
//
///**
// * Created by fengr on 2018/5/28.
// * 加载图片
// */
//
//public class LoadImgUtils {
//    public static void setImg(Context context, ImageView iv, String url) {
//        setImg(context, iv, url, R.drawable.icon_default);
//    }
//
//    public static void setImg(Context context, ImageView iv, String url, int placeholderRes) {
//        if (context == null || iv == null || url == null) {
//            return;
//        }
//        Glide.with(context)
//                .load(url)
//                .placeholder(placeholderRes)
//                .error(placeholderRes)
//                .into(iv);
//    }
//
//    /**
//     * 缓存变色问题
//     *
//     * @param context
//     * @param iv
//     * @param url
//     * @param placeholderRes
//     */
//    public static void setCacheImg(Context context, ImageView iv, String url, int placeholderRes) {
//        if (context == null || iv == null || url == null) {
//            return;
//        }
//        Glide.with(context)
//                .load(url)
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .placeholder(placeholderRes)
//                .error(placeholderRes)
//                .into(iv);
//    }
//
//    /**
//     * 圆角图片
//     *
//     * @param context
//     * @param iv
//     * @param url
//     */
//    public static void setImgCircle(Context context, ImageView iv, String url) {
//
//        setImgCircle(context, iv, url, R.drawable.head_icon);
//    }
//
//    public static void setImgCircle(Context context, ImageView iv, String url, int placeholderRes) {
//        if (context == null || iv == null || TextUtils.isEmpty(url)) {
//            return;
//        }
//        Glide.with(context)
//                .load(url)
//                .placeholder(placeholderRes)
//                .error(placeholderRes)
//                .transform(new GlideCircleTransform(context))
//                .into(iv);
//    }
//
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
//
//    /**
//     * 加载图片返回Bitmap
//     *
//     * @param context
//     * @param url
//     * @param action
//     */
//
//    public static void LoadImgToBitmap(Context context, String url, final MyAction.OnResult<Bitmap> action) {
//        if (context == null || url == null) {
//            if (action != null) {
//                action.onError();
//            }
//            return;
//        }
//
//        Glide.with(context)
//                .load(url)
//                .asBitmap()
//                .dontAnimate()
//                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
//                        if (action != null) {
//                            action.invoke(resource);
//                        }
//
//                    }
//
//                    @Override
//                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                        super.onLoadFailed(e, errorDrawable);
//                        if (action != null) {
//                            action.onError();
//                        }
//                    }
//                });
//    }
//
//    /**
//     * 加载图片返回Bitmap
//     *
//     * @param context
//     * @param downPath
//     * @return
//     */
//    public static Observable<Bitmap> getImgToBitmapObservable(final Context context, final String downPath) {
//
//        return getBitmapObservable(context, downPath, null,R.drawable.icon_default);
//    }
//
//    public static Observable<Bitmap> getBitmapObservable(final Context context, final String downPath,final BitmapTransformation transformation ,final int resourceId) {
//        return Observable.create(new ObservableOnSubscribe<Bitmap>() {
//            @Override
//            public void subscribe(final ObservableEmitter<Bitmap> emitter) throws Exception {
//                if (context == null || downPath == null) {
//                    emitter.onError(new Exception());
//                    return;
//                }
//                BitmapTypeRequest<String> stringBitmapTypeRequest = Glide.with(context)
//                        .load(downPath)
//                        .asBitmap();
//                if (transformation != null) {
//                    stringBitmapTypeRequest.transform(new GlideCircleTransform(context));
//                }
//                stringBitmapTypeRequest.error(resourceId)
//                        .placeholder(resourceId);
//                stringBitmapTypeRequest.dontAnimate()
//                        .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
//                            @Override
//                            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
//                                emitter.onNext(resource);
//                            }
//
//                            @Override
//                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                                super.onLoadFailed(e, errorDrawable);
//                                emitter.onError(e);
//                            }
//                        });
//            }
//        });
//    }
//
//    /**
//     * 加载图片返回Bitmap  在子线程调用
//     *
//     * @param context
//     * @param downPath
//     * @return
//     */
//    public static Bitmap getImgBitmapOnIo(final Context context, final String downPath) throws ExecutionException, InterruptedException {
//        if (TextUtils.isEmpty(downPath)) {
//            return null;
//        }
//        return Glide.with(context)
//                .load(downPath)
//                .asBitmap()
//                .centerCrop()
//                .into(500, 500)
//                .get();
//    }
//
//    /**
//     * 加载图片返回Bitmap  在子线程调用
//     *
//     * @param context
//     * @param downPath
//     * @return
//     */
//    public static Bitmap getImgBitmapOnIo1(final Context context, final String downPath) throws ExecutionException, InterruptedException {
//        if (TextUtils.isEmpty(downPath)) {
//            return null;
//        }
//        return Glide.with(context)
//                .load(downPath)
//                .asBitmap()
//                .centerCrop()
//                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                .get();
//    }
//
//    /**
//     * 加载图片返回Bitmap
//     *
//     * @param context
//     * @param downPath
//     * @return
//     */
//    public static Observable<Bitmap> getImgCircleToBitmapObservable(final Context context, final String downPath) {
//        return getBitmapObservable(context, downPath, new GlideCircleTransform(context),R.drawable.icon_default);
////        return Observable.create(new ObservableOnSubscribe<Bitmap>() {
////            @Override
////            public void subscribe(final ObservableEmitter<Bitmap> emitter) throws Exception {
////                Glide.with(context)
////                        .load(downPath)
////                        .asBitmap()
////                        .transform(new GlideCircleTransform(context))
////                        .dontAnimate()
////                        .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
////                            @Override
////                            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
////                                emitter.onNext(resource);
////                            }
////
////                            @Override
////                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
////                                super.onLoadFailed(e, errorDrawable);
////                                emitter.onError(e);
////                            }
////                        });
////            }
////        });
//    }
//
//    /**
//     * 获取图片宽高比例
//     *
//     * @param context
//     * @param imgUri
//     */
//    public static void getImageRatio(final Context context, final String imgUri, final MyAction.One<Float> action) {
//        try {
//            Glide.with(context)
//                    .load(imgUri)
//                    .asBitmap()
//                    .dontAnimate()
//                    .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
//                        @Override
//                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
//
//                            int imageHeight = resource.getHeight();
//                            int imageWigth = resource.getWidth();
//                            if (imageWigth == 0 || imageHeight == 0) {
//                                action.invoke((float) 3.0f / 4.0f);
//                            } else {
//                                action.invoke((float) (imageHeight + 0.0f) / (imageWigth + 0.0f));
//                            }
//                        }
//
//                        @Override
//                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                            super.onLoadFailed(e, errorDrawable);
//                            action.invoke((float) 3.0f / 4.0f);
//                        }
//                    });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 生成头像log
//     *
//     * @param bitmap   原图
//     * @param bgBitmap
//     * @return
//     */
//    public static Bitmap getHeadBitmap(Bitmap bitmap, Bitmap bgBitmap) {
//        int width = bgBitmap.getWidth();
//        int padding = width * 3 / 10;
//        Bitmap headBitmap = getBitmapByBitmap(bitmap, bgBitmap.getWidth() - padding, bgBitmap.getHeight() - padding);
//        // 背图
//        Bitmap newBitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(newBitmap);
//
//        Paint paint = new Paint();
//        paint.setAntiAlias(true);
//        canvas.drawARGB(0, 0, 0, 0);
////        Bitmap bgBitmap = getBitmapByBitmap(context,icon_default, num / 2, num / 2);
//        canvas.drawBitmap(bgBitmap, 0, 0, paint);
//        // 生成白色的
//        paint.setColor(Color.WHITE);
//        canvas.drawBitmap(headBitmap, padding / 2, padding / 2, paint);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
//        // 画正方形的
//        canvas.drawRect(0, 0, width, width, paint);
//        return newBitmap;
//    }
//
//    /**
//     * 设置bitmap四周白边
//     *
//     * @param bitmap           原图
//     * @param whiteBitmapWidth
//     * @return
//     */
//    public static Bitmap bg2WhiteBitmap(Bitmap bitmap, int whiteBitmapWidth) {
//        int size = bitmap.getWidth() < bitmap.getHeight() ? bitmap.getWidth() : bitmap.getHeight();
//        int num = whiteBitmapWidth * 2;
//        int sizebig = size + num;
//        // 背图
//        Bitmap newBitmap = Bitmap.createBitmap(sizebig, sizebig, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(newBitmap);
//
//        Paint paint = new Paint();
//        paint.setAntiAlias(true);
//        canvas.drawARGB(0, 0, 0, 0);
//        // 生成白色的
//        paint.setColor(Color.WHITE);
//        canvas.drawBitmap(bitmap, num / 2, num / 2, paint);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
//        // 画正方形的
//        canvas.drawRect(0, 0, sizebig, sizebig, paint);
//        return newBitmap;
//    }
//
//    /**
//     * 画布上能分配给Bitmap的空间是有限的，为了让图片全部显示就对图片做适当的压缩
//     *
//     * @param bitMap
//     * @param newHeight
//     * @param newWidth  设置想要的大小
//     */
//    private static Bitmap getBitmapByBitmap(Bitmap bitMap, float newWidth, float newHeight) {
//        int width = bitMap.getWidth();
//        int height = bitMap.getHeight();
//// 计算缩放比例
//        float scaleWidth = ((float) newWidth) / width;
//        float scaleHeight = ((float) newHeight) / height;
//// 取得想要缩放的matrix参数
//        Matrix matrix = new Matrix();
//        matrix.postScale(scaleWidth, scaleHeight);
//// 得到新的图片
//        return Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true);
//    }
//
//    /**
//     * @param context
//     * @param iv
//     * @param url
//     * @param placeholderRes
//     * @param radius         角度
//     * @param leftTop        是否画圆角（左上）  false 圆角，true不要
//     * @param rightTop       是否画圆角（右上）
//     * @param leftBottom     是否画圆角（左下）
//     * @param rightBottom    是否画圆角（右下）
//     */
//    public static void loadingCornerBitmap(Context context, ImageView iv, String url, int placeholderRes,
//                                           int radius,
//                                           boolean leftTop, boolean rightTop, boolean leftBottom, boolean rightBottom) {
//        if (context == null || url == null || iv == null) return;
//        int i = DensityUtil.dip2px(context, radius);
//        CornerTransform transformation = new CornerTransform(context, i);
//        //只是绘制左上角和右上角圆角
//        transformation.setExceptCorner(leftTop, rightTop, leftBottom, rightBottom);
//        Glide.with(context).
//                load(url).
//                asBitmap().
//                placeholder(placeholderRes)
//                .error(placeholderRes).
//                transform(transformation).
//                into(iv);
//    }
//
//    /**
//     * @param context
//     * @param iv
//     * @param url
//     * @param placeholderRes
//     * @param radius         角度
//     */
//    public static void loadingCornerBitmap(Context context, ImageView iv, String url, int placeholderRes, int radius) {
//        loadingCornerBitmap(context, iv, url, placeholderRes, radius, false, false, false, false);
//    }
//
//    /**
//     * @param context
//     * @param iv
//     * @param url
//     * @param radius  角度
//     */
//    public static void loadingCornerTop(Context context, ImageView iv, String url, int radius) {
//        loadingCornerBitmap(context, iv, url, R.drawable.icon_default, radius, false, false, true, true);
//    }
//
//    /**
//     * @param context
//     * @param iv
//     * @param url
//     * @param radius  角度
//     */
//    public static void loadingCornerBitmap(Context context, ImageView iv, String url, int radius) {
//        loadingCornerBitmap(context, iv, url, R.drawable.icon_default, radius);
//    }
//
//    /**
//     * @param context
//     * @param iv
//     * @param url
//     */
//    public static void loadingCornerBitmap(Context context, ImageView iv, String url) {
//        loadingCornerBitmap(context, iv, url, R.drawable.icon_default, 5);
//    }
//
//
//    /**
//     * 加载背景
//     *
//     * @param context
//     * @param url
//     */
//    public static void setViewBackground(Context context, View view, String url) {
//        if (url == null || view == null) {
//            return;
//        }
//        Glide.with(context).load(url)
//                .placeholder(R.color.white)
//                .error(R.color.white)
//                .into(new ViewTarget<View, GlideDrawable>(view) {
//                    //括号里为需要加载的控件
//                    @Override
//                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                        view.setBackground(resource.getCurrent());
//                    }
//                });
//    }
//}
