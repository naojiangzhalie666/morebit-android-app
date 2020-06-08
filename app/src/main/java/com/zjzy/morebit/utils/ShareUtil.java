package com.zjzy.morebit.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.zjzy.morebit.R;
import com.zjzy.morebit.contact.SdDirPath;

import java.io.File;
import java.util.HashMap;

import javax.net.ssl.SSLHandshakeException;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qq.QQClientNotExistException;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import cn.sharesdk.wechat.utils.WechatClientNotExistException;
import cn.sharesdk.wechat.utils.WechatFavoriteNotSupportedException;
import cn.sharesdk.wechat.utils.WechatTimelineNotSupportedException;

/**
 * Created by Administrator on 2017/10/24.
 */

public class ShareUtil {

    public final static int QQType = 1;
    public final static int QQZoneType = 2;
    public final static int WechatType = 3;
    public final static int WeMomentsType = 4;
    public final static int WeiboType = 5;
    public final static int MoreType = 6;//更多

    public final static class Image {

        public static void toWechatFriend(Activity activity, String image, PlatformActionListener platformActionListener) { //分享到好友
            Wechat.ShareParams sp = new Wechat.ShareParams();
            sp.setTitle("");
            sp.setText("");
            sp.setShareType(Platform.SHARE_IMAGE);
            if (image.startsWith("drawable_icon")) {
                //获取本地图片
                String[] picSp = image.split(",");
                if (picSp.length == 2) {
                    if ("goods_picnull_icon".equals(picSp[1])) {
                        Resources resources = activity.getResources();
                        Bitmap bmp = BitmapFactory.decodeResource(resources, R.drawable.logo);
                        sp.setImageData(bmp);
                    }
                }
            } else {
                if (image.contains("http")) {
                    sp.setImageUrl(image);
                } else {
                    sp.setImagePath(image);
                }
            }
            Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
            if (platformActionListener == null)
                wechat.setPlatformActionListener(new MyPlatformActionListener(activity));
            else
                wechat.setPlatformActionListener(platformActionListener);
            wechat.share(sp);
        }

        public static void toWechatFriendArray(Activity activity, String image, PlatformActionListener platformActionListener) { //分享到好友
            Wechat.ShareParams sp = new Wechat.ShareParams();
            sp.setTitle("");
            sp.setText("");
            sp.setShareType(Platform.SHARE_IMAGE);
            if (image.startsWith("drawable_icon")) {
                //获取本地图片
                String[] picSp = image.split(",");
                if (picSp.length == 2) {
                    if ("goods_picnull_icon".equals(picSp[1])) {
                        Resources resources = activity.getResources();
                        Bitmap bmp = BitmapFactory.decodeResource(resources, R.drawable.logo);
                        sp.setImageData(bmp);
                    }
                }
            } else {
                if (image.contains("http")) {
                    sp.setImageUrl(image);
                } else {
                    sp.setImagePath(image);
                }
            }
            Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
            if (platformActionListener == null)
                wechat.setPlatformActionListener(new MyPlatformActionListener(activity));
            else
                wechat.setPlatformActionListener(platformActionListener);
            wechat.share(sp);
        }

        public static void toWechatMoments(Activity activity, String image, PlatformActionListener platformActionListener) {  //分享到朋友圈
            WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
            sp.setTitle("");
            sp.setText("");
            sp.setShareType(Platform.SHARE_IMAGE);
            if (image.startsWith("drawable_icon")) {
                //获取本地图片
                String[] picSp = image.split(",");
                if (picSp.length == 2) {
                    if ("goods_picnull_icon".equals(picSp[1])) {
                        Resources resources = activity.getResources();
                        Bitmap bmp = BitmapFactory.decodeResource(resources, R.drawable.logo);
                        sp.setImageData(bmp);
                    }
                }
            } else {
                if (image.contains("http")) {
                    sp.setImageUrl(image);
                } else {
                    sp.setImagePath(image);
                }
            }
            Platform wechat = ShareSDK.getPlatform(WechatMoments.NAME);
            if (platformActionListener == null)
                wechat.setPlatformActionListener(new MyPlatformActionListener(activity));
            else
                wechat.setPlatformActionListener(platformActionListener);
            wechat.share(sp);
        }

        public static void toQQFriend(Activity activity, String image, PlatformActionListener platformActionListener) { //分享到QQ好友
            QQ.ShareParams sp = new QQ.ShareParams();
            sp.setTitle("");
            sp.setText("");
            sp.setShareType(Platform.SHARE_IMAGE);
            if (image.startsWith("drawable_icon")) {
                //获取本地图片
                String[] picSp = image.split(",");
                if (picSp.length == 2) {
                    if ("goods_picnull_icon".equals(picSp[1])) {
                        Resources resources = activity.getResources();
                        Bitmap bmp = BitmapFactory.decodeResource(resources, R.drawable.logo);
                        sp.setImageData(bmp);
                    }
                }
            } else {
                if (image.contains("http")) {
                    sp.setImageUrl(image);
                } else {
                    sp.setImagePath(image);

                }
            }
            Platform wechat = ShareSDK.getPlatform(QQ.NAME);
            if (platformActionListener == null)
                wechat.setPlatformActionListener(new MyPlatformActionListener(activity));
            else
                wechat.setPlatformActionListener(platformActionListener);
            wechat.share(sp);
        }

        public static void toQQFriend(Activity activity, String[] image, PlatformActionListener platformActionListener) { //分享到QQ好友
            QQ.ShareParams sp = new QQ.ShareParams();
            sp.setTitle("");
            sp.setText("");
            sp.setShareType(Platform.SHARE_IMAGE);

            sp.setImageArray(image);
            Platform wechat = ShareSDK.getPlatform(QQ.NAME);
            if (platformActionListener == null)
                wechat.setPlatformActionListener(new MyPlatformActionListener(activity));
            else
                wechat.setPlatformActionListener(platformActionListener);
            wechat.share(sp);
        }

        public static void toQQRoom(Activity activity, String image, PlatformActionListener platformActionListener) { //分享到QQ空间
            QZone.ShareParams sp = new QZone.ShareParams();
            sp.setTitle("");
            sp.setText("");

            sp.setShareType(Platform.SHARE_IMAGE);
            if (image.startsWith("drawable_icon")) {
                //获取本地图片
                String[] picSp = image.split(",");
                if (picSp.length == 2) {
                    if ("goods_picnull_icon".equals(picSp[1])) {
                        Resources resources = activity.getResources();
                        Bitmap bmp = BitmapFactory.decodeResource(resources, R.drawable.logo);
                        sp.setImageData(bmp);
                    }
                }
            } else {
                if (image.contains("http")) {
                    sp.setImageUrl(image);
                } else {
                    sp.setImagePath(image);
                }
            }
            Platform wechat = ShareSDK.getPlatform(QZone.NAME);
            if (platformActionListener == null)
                wechat.setPlatformActionListener(new MyPlatformActionListener(activity));
            else
                wechat.setPlatformActionListener(platformActionListener);
            wechat.share(sp);
        }
        public static void toQQRoom(Activity activity, String[] image, PlatformActionListener platformActionListener) { //分享到QQ空间
            QZone.ShareParams sp = new QZone.ShareParams();
            sp.setTitle("");
            sp.setText("");
            sp.setShareType(Platform.SHARE_IMAGE);
           sp.setImageArray(image);
            Platform wechat = ShareSDK.getPlatform(QZone.NAME);
            if (platformActionListener == null)
                wechat.setPlatformActionListener(new MyPlatformActionListener(activity));
            else
                wechat.setPlatformActionListener(platformActionListener);
            wechat.share(sp);
        }
        public static void toSinaWeibo(Activity activity, String image, PlatformActionListener platformActionListener) { //分享到新浪微博
            SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
            sp.setTitle("");
            sp.setText("");
            sp.setShareType(Platform.SHARE_IMAGE);

            if (image.startsWith("drawable_icon")) {
                //获取本地图片
                String[] picSp = image.split(",");
                if (picSp.length == 2) {
                    if ("goods_picnull_icon".equals(picSp[1])) {
                        Resources resources = activity.getResources();
                        Bitmap bmp = BitmapFactory.decodeResource(resources, R.drawable.logo);
                        sp.setImageData(bmp);
                    }
                }
            } else {
                if (image.contains("http")) {
                    sp.setImageUrl(image);
                } else {
                    sp.setImagePath(image);
                }
            }
            Platform wechat = ShareSDK.getPlatform(SinaWeibo.NAME);


            if (wechat.isAuthValid()) {
                wechat.removeAccount(true);
                ShareSDK.removeCookieOnAuthorize(true);
            }
            if (platformActionListener == null)
                wechat.setPlatformActionListener(new MyPlatformActionListener(activity));
            else
                wechat.setPlatformActionListener(platformActionListener);
            wechat.share(sp);
        }
    }

    public static class App {


        public static void toWechatFriend(Activity activity, String title, String text, String image, String url, PlatformActionListener platformActionListener) { //分享到好友
           MyLog.i("test","title: " +title+"  text: " +text+" image: " +image+" url: " +url);
            Wechat.ShareParams sp = new Wechat.ShareParams();
            sp.setShareType(Platform.SHARE_WEBPAGE);
            sp.setText(text);
            sp.setTitle(title);
            if (!LoadImgUtils.isPicture(image)) {
                Resources resources = activity.getResources();
                Bitmap bmp = BitmapFactory.decodeResource(resources, R.drawable.head_icon);
                sp.setImageData(bmp);
            } else {
                if (image.contains("http")) {
                    sp.setImageUrl(image);
                } else {
                    sp.setImagePath(image);
                }
            }
            sp.setUrl(url);
            Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
            if (platformActionListener == null)
                wechat.setPlatformActionListener(new MyPlatformActionListener(activity));
            else
                wechat.setPlatformActionListener(platformActionListener);
            wechat.share(sp);
        }

        public static void toWechatFriendVideo(Activity activity, String url, PlatformActionListener platformActionListener) { //分享到好友
          //  MyLog.i("test","title: " +title+"  text: " +text+" image: " +image+" url: " +url);
            Wechat.ShareParams sp = new Wechat.ShareParams();
            sp.setShareType(Platform.SHARE_FILE);
            sp.setText("");
            sp.setTitle("");
            sp.setFilePath(url);
            Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
            if (platformActionListener == null)
                wechat.setPlatformActionListener(new MyPlatformActionListener(activity));
            else
                wechat.setPlatformActionListener(platformActionListener);
            wechat.share(sp);
        }



        public static void toWechatMoments(Activity activity, String title, String text, String image, String url, PlatformActionListener platformActionListener) {  //分享到朋友圈
            WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
            sp.setTitle(title);
            sp.setText(text);
            sp.setShareType(Platform.SHARE_WEBPAGE);
            if (!LoadImgUtils.isPicture(image)) {
                Resources resources = activity.getResources();
                Bitmap bmp = BitmapFactory.decodeResource(resources, R.drawable.head_icon);
                sp.setImageData(bmp);
            } else {
                if (image.contains("http")) {
                    sp.setImageUrl(image);
                } else {
                    sp.setImagePath(image);
                }
            }
            sp.setUrl(url);
            Platform wechat = ShareSDK.getPlatform(WechatMoments.NAME);
            if (platformActionListener == null)
                wechat.setPlatformActionListener(new MyPlatformActionListener(activity));
            else
                wechat.setPlatformActionListener(platformActionListener);
            wechat.share(sp);
        }

        public static void toQQFriend(Activity activity, String title, String text, String image, String url, PlatformActionListener platformActionListener) { //分享到好友
            QQ.ShareParams sp = new QQ.ShareParams();
            sp.setShareType(Platform.SHARE_WEBPAGE);
            sp.setText(text);
            sp.setTitle(title);
            if (!LoadImgUtils.isPicture(image)) {
                //                Resources resources = activity.getResources();
                //                Bitmap bmp = BitmapFactory.decodeResource(resources, R.drawable.logo);
                //                sp.setImageData(bmp);
                String filePath = SdDirPath.IMAGE_CACHE_PATH + "applogo" + ".png";
                File file = new File(filePath);
                if (file.exists()) {
                    sp.setImagePath(filePath);
                } else {
                    Resources resources = activity.getResources();
                    Bitmap bmp = BitmapFactory.decodeResource(resources, R.drawable.logo);
                    String applogo = FileUtils.savePhoto(bmp, SdDirPath.IMAGE_CACHE_PATH, "applogo");
                    sp.setImagePath(applogo);
                }
            } else {
                if (image.contains("http")) {
                    sp.setImageUrl(image);
                } else {
                    sp.setImagePath(image);
                }
            }
            sp.setTitleUrl(url);
            Platform wechat = ShareSDK.getPlatform(QQ.NAME);
            if (platformActionListener == null)
                wechat.setPlatformActionListener(new MyPlatformActionListener(activity));
            else
                wechat.setPlatformActionListener(platformActionListener);
            wechat.share(sp);
        }

        public static void toQQRoom(Activity activity, String title, String text, String image, String url, PlatformActionListener platformActionListener) { //分享到好友
            QZone.ShareParams sp = new QZone.ShareParams();
            sp.setShareType(Platform.SHARE_WEBPAGE);
            sp.setText(text);
            sp.setTitle(title);
            if (!LoadImgUtils.isPicture(image)) {
                //                Resources resources = activity.getResources();
                //                Bitmap bmp = BitmapFactory.decodeResource(resources, R.drawable.logo);
                //                sp.setImageData(bmp);
                String filePath = SdDirPath.IMAGE_CACHE_PATH + "applogo" + ".png";
                File file = new File(filePath);
                if (file.exists()) {
                    sp.setImagePath(filePath);
                } else {
                    Resources resources = activity.getResources();
                    Bitmap bmp = BitmapFactory.decodeResource(resources, R.drawable.logo);
                    String applogo = FileUtils.savePhoto(bmp, SdDirPath.IMAGE_CACHE_PATH, "applogo");
                    sp.setImagePath(applogo);
                }
            } else {
                if (image.contains("http")) {
                    sp.setImageUrl(image);
                } else {
                    sp.setImagePath(image);

                }
            }
            sp.setTitleUrl(url);
            Platform wechat = ShareSDK.getPlatform(QZone.NAME);
            if (platformActionListener == null)
                wechat.setPlatformActionListener(new MyPlatformActionListener(activity));
            else
                wechat.setPlatformActionListener(platformActionListener);
            wechat.share(sp);
        }

        public static void toSinaWeibo(Activity activity, String title, String text, String image, String url, PlatformActionListener platformActionListener) { //分享到好友
            SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
            sp.setShareType(Platform.SHARE_WEBPAGE);
            sp.setText(text + url);
            sp.setTitle(title);
            if (!LoadImgUtils.isPicture(image)) {
                Resources resources = activity.getResources();
                Bitmap bmp = BitmapFactory.decodeResource(resources, R.drawable.logo);
                sp.setImageData(bmp);
            } else {
                if (image.contains("http")) {
                    sp.setImageUrl(image);
                } else {
                    sp.setImagePath(image);
                }
            }
            sp.setTitleUrl(url);
            Platform wechat = ShareSDK.getPlatform(SinaWeibo.NAME);
            if (platformActionListener == null)
                wechat.setPlatformActionListener(new MyPlatformActionListener(activity));
            else
                wechat.setPlatformActionListener(platformActionListener);
            wechat.share(sp);
        }


    }

    public static class MyPlatformActionListener implements PlatformActionListener {

        Context mContext;

        public MyPlatformActionListener(Context context) {
            mContext = context;
        }

        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            ViewShowUtils.showShortToast(com.zjzy.morebit.App.getAppContext(), "分享成功");
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            String msg = "分享失败，请稍后再试";
            String type = "";
            if (platform.getName().equalsIgnoreCase(Wechat.NAME)) {
                type = "微信";
            } else if (platform.getName().equalsIgnoreCase(SinaWeibo.NAME)) {
                type = "微博";
            } else if (platform.getName().equalsIgnoreCase(QQ.NAME)) {
                type = "QQ";
            } else if (platform.getName().equalsIgnoreCase(QZone.NAME)) {
                type = "QQ空间";
            }
            if (throwable instanceof SSLHandshakeException) {
                msg = type + "分享失败，请检查您的网络状态";
            } else if (throwable instanceof WechatClientNotExistException
                    || throwable instanceof WechatFavoriteNotSupportedException
                    || throwable instanceof WechatTimelineNotSupportedException) {
                msg = "请先安装微信客户端";
            } else if (throwable instanceof QQClientNotExistException) {
                msg = "请先安装" + type + "客户端";
            } else if (throwable instanceof cn.sharesdk.tencent.qzone.QQClientNotExistException) {
                msg = "请先安装" + type + "客户端";
            }

            if (mContext != null && mContext instanceof Activity) {
                final Activity activity = (Activity) mContext;
                if (!activity.isFinishing()) {
                    final String finalMsg = msg;
                    activity.runOnUiThread(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ViewShowUtils.showShortToast(activity, finalMsg);
                        }
                    }));
                }
            }
        }

        @Override
        public void onCancel(Platform platform, int i) {
            //修改为成功。第三方的问题。成功也回调取消。
            ViewShowUtils.showShortToast(com.zjzy.morebit.App.getAppContext(), "分享成功");
        }
    }


    public static void shareImg(Activity activity, String url, int type) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        switch (type) {
            case ShareUtil.QQType:
                Image.toQQFriend(activity, url, new MyPlatformActionListener(activity));
                break;
            case ShareUtil.QQZoneType:
                Image.toQQRoom(activity, url, new MyPlatformActionListener(activity));
                break;
            case ShareUtil.WechatType:
                Image.toWechatFriend(activity, url, new MyPlatformActionListener(activity));
                break;
            case ShareUtil.WeMomentsType:
                Image.toWechatMoments(activity, url, new MyPlatformActionListener(activity));
                break;
            case ShareUtil.WeiboType:
                Image.toSinaWeibo(activity, url, new MyPlatformActionListener(activity));
                break;
        }
    }


    // 调用系統方法分享文件
    public static void shareWxFile(Context context, File file) {
        if (null != file && file.exists()) {
            Intent share = new Intent(Intent.ACTION_SEND);
            Uri uri = null;
            // 判断版本大于等于7.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // "项目包名.fileprovider"即是在清单文件中配置的authorities
                uri = FileProvider.getUriForFile(context, "com.zjzy.morebit.provider", file);
                // 给目标应用一个临时授权
                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                uri = Uri.fromFile(file);
            }
            ComponentName comp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.tools.ShareImgUI");
            share.putExtra(Intent.EXTRA_STREAM, uri);
            share.setType(getMimeType(file.getAbsolutePath()));//此处可发送多种文件
            share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            share.setComponent(comp);
            context.startActivity(share);
        }


    }
/*
*
*  if (appType == WEICHAT) {
            ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
            intent.setComponent(comp);
        } else if (appType == WEICHAT_CIRCE) {
            ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
            intent.setComponent(comp);
        } else if (appType == QQ) {
            ComponentName comp = new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");
            intent.setComponent(comp);
        } else if (appType == QQ_CIRCE) {
            ComponentName comp = new ComponentName("com.qzone", "com.qzonex.module.operation.ui.QZonePublishMoodActivity");
            intent.setComponent(comp);
        }

* */
    // 调用系統方法分享文件
    public static void shareQqFile(Context context, File file) {
        if (null != file && file.exists()) {
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            Uri uri = null;
            // 判断版本大于等于7.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // "项目包名.fileprovider"即是在清单文件中配置的authorities
                uri = FileProvider.getUriForFile(context, "com.zjzy.morebit.provider", file);
                // 给目标应用一个临时授权
                sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                uri = Uri.fromFile(file);
            }
//            ComponentName comp = new ComponentName("com.tencent.mobileqq","com.tencent.mobileqq.activity.JumpActivity");
//            share.putExtra(Intent.EXTRA_STREAM, uri);
//           share.setType(getMimeType(file.getAbsolutePath()));//此处可发送多种文件
//            share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            share.setComponent(comp);
//            context.startActivity(share);



            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            sendIntent.setType(getMimeType(file.getAbsolutePath()));
            sendIntent.setClassName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");//QQ好友或QQ群
            context.startActivity(sendIntent);

       }


    }
    // 根据文件后缀名获得对应的MIME类型。
    private static String getMimeType(String filePath) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String mime = "*/*";
        if (filePath != null) {
            try {
                mmr.setDataSource(filePath);
                mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
            } catch (IllegalStateException e) {
                return mime;
            } catch (IllegalArgumentException e) {
                return mime;
            } catch (RuntimeException e) {
                return mime;
            }
        }
        return mime;
    }


}
