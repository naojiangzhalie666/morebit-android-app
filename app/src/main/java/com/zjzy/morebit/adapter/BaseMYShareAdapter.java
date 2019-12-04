package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.zjzy.morebit.Module.common.Dialog.WeChatMomentsHintDialog;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.network.CallBackObserver;
import com.zjzy.morebit.pojo.MarkermallCircleInfo;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.utils.FileUtils;
import com.zjzy.morebit.utils.GoodsUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LogUtils;
import com.zjzy.morebit.utils.ShareUtil;
import com.zjzy.morebit.utils.action.MyAction;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * 密封圈分享滴 Adapter
 */
public abstract class BaseMYShareAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    //纯图片
    public static final int  TYPE_IMAGE = 1;
    //商品，可以分享图片和二维码
    public static final int  TYPE_GOODS_IMAGE = 2;
    //纯视频
    public static final int  TYPE_VIDEO =3;
    //视频和图片
    public static final int  TYPE_VIDEO_IMAGE =4;
    //商品二维码，视频，图片
    public static final int  TYPE_GOODS_IMAGE_VIDEO =5;
    protected Context mContext;

    /**
     * 显示修改头像的对话框
     */
    protected void showChoosePicDialog(int type,final MyAction.One<Integer> action) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("选择分享");
        String[] items;
        if(type==TYPE_GOODS_IMAGE) {
            items = new String[]{"分享二维码海报","分享图片"};
        } else if(type == TYPE_IMAGE){
            items =new String[]{"分享图片"};
        } else if(type == TYPE_VIDEO){
            items =new String[]{"分享视频"};
        } else if(type == TYPE_VIDEO_IMAGE){
            items =new String[]{"分享图片" ,"分享视频"};
        } else{
            items =new String[]{"分享二维码海报","分享图片" ,"分享视频"};
        }

        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                action.invoke(which);
            }
        });
        builder.create().show();
    }

    /* 分享图片,
           *
           * @param item
          * @param picture
          */
    protected void shareImg(final MarkermallCircleInfo item, final List<String> picture, final int sharePlatform) {
        if (picture == null || picture.size() == 0) {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.goodsShertError), Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingView.showDialog(mContext, "");

        Observable.just(picture)
                .observeOn(Schedulers.io())
                .map(new Function<List<String>, Map<String, File>>() {
                    @Override
                    public Map<String, File> apply(List<String> strings) throws Exception {
                        LogUtils.Log("MarkermallCircleAdapter ", "" + Thread.currentThread().getName());
                        Map<String, File> map = new HashMap<>();
                        for (int i = 0; i < picture.size(); i++) {
                            final String s = picture.get(i);
                            if (TextUtils.isEmpty(s)) {
                                continue;
                            }
                            Bitmap bitmap = null;
                            File img = null;
                            try {
                                bitmap = LoadImgUtils.getImgBitmapOnIo1(mContext, s);
                                String pictureName = FileUtils.getPictureName(s);
                                img = GoodsUtil.saveBitmapToFile(mContext, bitmap, pictureName);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (bitmap == null || img == null) continue;
                            map.put(s, img);
                        }
                        return map;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                })
                .subscribe(new CallBackObserver<Map<String, File>>() {
                    @Override
                    public void onNext(Map<String, File> map) {
                        ArrayList<File> urlList = new ArrayList();
                        for (String str : picture) {
                            if (!TextUtils.isEmpty(str)) {
                                File file = map.get(str);
                                if (file != null) {
                                    LogUtils.Log("MarkermallCircleAdapter ", "fileName" + file.getName());
                                    urlList.add(file);
                                }
                            }
                        }
                        if (urlList.size() != 0) {
                            if(sharePlatform == ShareUtil.WeMomentsType){
                                if(urlList.size()==1){

                                    GoodsUtil.sysShareImage(mContext, urlList,sharePlatform);
                                    MarkermallCircleAdapter.mShareCountId = item.getId();
                                } else {
                                  openShareDialog().show();
                                }
                            } else {
                                GoodsUtil.sysShareImage(mContext, urlList,sharePlatform);
                                MarkermallCircleAdapter.mShareCountId = item.getId();
                            }
                            LoadingView.dismissDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });

    }
    /* 分享图片,
     *
     * @param item
    * @param picture
    */
    protected void shareImg(final MarkermallCircleInfo item, final List<String> picture) {
        if (picture == null || picture.size() == 0) {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.goodsShertError), Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingView.showDialog(mContext, "");

        Observable.just(picture)
                .observeOn(Schedulers.io())
                .map(new Function<List<String>, Map<String, File>>() {
                    @Override
                    public Map<String, File> apply(List<String> strings) throws Exception {
                        LogUtils.Log("MarkermallCircleAdapter ", "" + Thread.currentThread().getName());
                        Map<String, File> map = new HashMap<>();
                        for (int i = 0; i < picture.size(); i++) {
                            final String s = picture.get(i);
                            if (TextUtils.isEmpty(s)) {
                                continue;
                            }
                            Bitmap bitmap = null;
                            File img = null;
                            try {
                                bitmap = LoadImgUtils.getImgBitmapOnIo1(mContext, s);
                                String pictureName = FileUtils.getPictureName(s);
                                img = GoodsUtil.saveBitmapToFile(mContext, bitmap, pictureName);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (bitmap == null || img == null) continue;
                            map.put(s, img);
                        }
                        return map;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                })
                .subscribe(new CallBackObserver<Map<String, File>>() {
                    @Override
                    public void onNext(Map<String, File> map) {
                        ArrayList<File> urlList = new ArrayList();
                        for (String str : picture) {
                            if (!TextUtils.isEmpty(str)) {
                                File file = map.get(str);
                                if (file != null) {
                                    LogUtils.Log("MarkermallCircleAdapter ", "fileName" + file.getName());
                                    urlList.add(file);
                                }
                            }
                        }
                        if (urlList.size() != 0) {
                            GoodsUtil.originalShareImage(mContext, urlList, ShareUtil.MoreType);
                            MarkermallCircleAdapter.mShareCountId = item.getId();
                            LoadingView.dismissDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });

    }

    /**
     * 分享商品
     * @param item
     * @param osgData
     */
    protected void shareGoods(final MarkermallCircleInfo item, List<ShopGoodInfo> osgData) {
        GoodsUtil.SharePosterList2((Activity) mContext, osgData, new MyAction.OnResult<String>() {
            @Override
            public void invoke(String arg) {
                LoadingView.dismissDialog();
                MarkermallCircleAdapter. mShareCountId = item.getId();
            }

            @Override
            public void onError() {
                LoadingView.dismissDialog();
            }
        });
    }
    /**
     * 分享商品
     * @param item
     * @param osgData
     */
    protected void shareGoods(final MarkermallCircleInfo item, List<ShopGoodInfo> osgData, int sharePlatform) {
        GoodsUtil.SharePosterList2((Activity) mContext, osgData,sharePlatform, openShareDialog(),new MyAction.OnResult<String>() {
            @Override
            public void invoke(String arg) {
                LoadingView.dismissDialog();
                MarkermallCircleAdapter. mShareCountId = item.getId();
            }

            @Override
            public void onError() {
                LoadingView.dismissDialog();
            }
        });
    }

    private WeChatMomentsHintDialog openShareDialog() {

        WeChatMomentsHintDialog dialog = new WeChatMomentsHintDialog(mContext);
        dialog.setmOkListener(new WeChatMomentsHintDialog.OnOkListener() {
            @Override
            public void onClick(View view) {
                String url="weixin://";
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
        return dialog;

    }
}
