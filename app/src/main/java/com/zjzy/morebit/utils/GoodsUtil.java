package com.zjzy.morebit.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.Activity.ShareMoneyActivity;
import com.zjzy.morebit.Activity.ShowWebActivity;
import com.zjzy.morebit.App;
import com.zjzy.morebit.LocalData.CommonLocalData;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Dialog.WeChatMomentsHintDialog;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.contact.SdDirPath;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.CallBackObserver;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.RxWXHttp;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.CommonShareTemplateBean;
import com.zjzy.morebit.pojo.HotKeywords;
import com.zjzy.morebit.pojo.MarkermallCircleInfo;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.TkBean;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.goods.CheckCouponStatusBean;
import com.zjzy.morebit.pojo.goods.CouponUrlBean;
import com.zjzy.morebit.pojo.goods.PddShareContent;
import com.zjzy.morebit.pojo.goods.ShareUrlListBaen;
import com.zjzy.morebit.pojo.goods.ShareUrlMoreBaen;
import com.zjzy.morebit.pojo.goods.TKLBean;
import com.zjzy.morebit.pojo.request.RequestActivityLinkBean;
import com.zjzy.morebit.pojo.request.RequestCheckGoodsBean;
import com.zjzy.morebit.pojo.request.RequestCircleShareBean;
import com.zjzy.morebit.pojo.request.RequestCouponUrlBean;
import com.zjzy.morebit.pojo.request.RequestPddShareContent;
import com.zjzy.morebit.pojo.request.RequestTKLBean;
import com.zjzy.morebit.pojo.request.WxCodeBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestKeyBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestUploadCouponInfo;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.utils.fire.BuglyUtils;
import com.zjzy.morebit.utils.share.ShareMore;
import com.zjzy.morebit.utils.sys.RequestPermissionUtlis;
import com.makeramen.roundedimageview.RoundedImageView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 商品分享或购买或详情跳转
 */

public class GoodsUtil {


    private static Bitmap bitmap;


    public static void getTaoKouLing(final RxAppCompatActivity activity, final ShopGoodInfo goodsInfo, final MyAction.OnResult<TKLBean> action) {// 获取淘口令

        getGetTkLObservable(activity, goodsInfo)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                })
                .subscribe(new DataObserver<TKLBean>() {
                    @Override
                    protected void onSuccess(TKLBean bean) {
                        if (action != null)
                            action.invoke(bean);
                        ShareMoneyActivity.start(activity, goodsInfo, bean);
                    }
                });
    }

    public static Observable<BaseResponse<TKLBean>> getGetTkLObservable(final RxAppCompatActivity activity, final ShopGoodInfo goodsInfo) {
        return getGetTkLFinalObservable(activity, goodsInfo);

    }

    /**
     * 获取拼多多的推广内容
     *
     * @param activity
     * @param goodsInfo
     * @return
     */
    public static Observable<BaseResponse<PddShareContent>> getGenerateForPDD(RxAppCompatActivity activity,
                                                                              ShopGoodInfo goodsInfo) {
        int isInvitecode = App.getACache().getAsInt(C.sp.SHARE_MOENY_IS_INVITECODE);
        int isDownloadUrl = App.getACache().getAsInt(C.sp.SHARE_MOENY_IS_DOWNLOAD_URL);
        String isShortLink = App.getACache().getAsString(C.sp.isShortLink);
        if (TextUtils.isEmpty(isShortLink)) {
            isShortLink = "1";
        }
        RequestPddShareContent requestBean = new RequestPddShareContent();
        requestBean.setGoodsId(goodsInfo.getGoodsId().toString());
        requestBean.setGoodsDesc(goodsInfo.getItemDesc());
        requestBean.setGoodsTitle(goodsInfo.getTitle());
        requestBean.setGoodsPrice(goodsInfo.getPriceForPdd());
        requestBean.setGoodsVoucherPrice(goodsInfo.getVoucherPriceForPdd());
        requestBean.setIsDownLoadUrl(isDownloadUrl);
        requestBean.setIsInviteCode(isInvitecode);
        requestBean.setIsShortLink(isShortLink);
        return RxHttp.getInstance().getGoodsService().getGenerateForPDD(
                requestBean
        )
                .compose(RxUtils.<BaseResponse<PddShareContent>>switchSchedulers())
                .compose(activity.<BaseResponse<PddShareContent>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                });
    }

    /**
     * 获取京东的推广内容
     *
     * @param activity
     * @param goodsInfo
     * @return
     */
    public static Observable<BaseResponse<String>> getGenerateForJD(RxAppCompatActivity activity,
                                                                    ShopGoodInfo goodsInfo) {
        int isInvitecode = App.getACache().getAsInt(C.sp.SHARE_MOENY_IS_INVITECODE);
        int isDownloadUrl = App.getACache().getAsInt(C.sp.SHARE_MOENY_IS_DOWNLOAD_URL);

        RequestPddShareContent requestBean = new RequestPddShareContent();
        requestBean.setItemTitle(goodsInfo.getTitle());
        requestBean.setPrice(goodsInfo.getPriceForPdd());
        requestBean.setVoucherPrice(goodsInfo.getVoucherPriceForPdd());
        requestBean.setIsDownLoadUrl(isDownloadUrl);
        requestBean.setIsInviteCode(isInvitecode);
        requestBean.setClickURL(goodsInfo.getClickURL());

        return RxHttp.getInstance().getGoodsService().getGenerateForJD(
                requestBean
        )
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(activity.<BaseResponse<String>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                });
    }

    /**
     * 获取唯品会的推广内容
     *
     * @param activity
     * @param goodsInfo
     * @return
     */
    public static Observable<BaseResponse<String>> getGenerateForWph(RxAppCompatActivity activity,
                                                                     ShopGoodInfo goodsInfo) {
        int isInvitecode = App.getACache().getAsInt(C.sp.SHARE_MOENY_IS_INVITECODE);
        int isDownloadUrl = App.getACache().getAsInt(C.sp.SHARE_MOENY_IS_DOWNLOAD_URL);

        RequestPddShareContent requestBean = new RequestPddShareContent();
        requestBean.setItemTitle(goodsInfo.getGoodsName());
        requestBean.setPrice(goodsInfo.getMarketPrice());
        requestBean.setVoucherPrice(goodsInfo.getVipPrice());
        requestBean.setIsDownLoadUrl(isDownloadUrl);
        requestBean.setIsInviteCode(isInvitecode);
        requestBean.setClickURL(goodsInfo.getClickURL());

        return RxHttp.getInstance().getGoodsService().getGenerateForWph(
                requestBean
        )
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(activity.<BaseResponse<String>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                });
    }

    /**
     * 获取koala的推广内容
     *
     * @param activity
     * @param goodsInfo
     * @return
     */
    public static Observable<BaseResponse<String>> getGenerateForKaola(RxAppCompatActivity activity,
                                                                       ShopGoodInfo goodsInfo) {
        int isInvitecode = App.getACache().getAsInt(C.sp.SHARE_MOENY_IS_INVITECODE);
        int isDownloadUrl = App.getACache().getAsInt(C.sp.SHARE_MOENY_IS_DOWNLOAD_URL);

        RequestPddShareContent requestBean = new RequestPddShareContent();
        requestBean.setItemTitle(goodsInfo.getGoodsTitle());
        requestBean.setPrice(goodsInfo.getMarketPrice());
        requestBean.setVoucherPrice(goodsInfo.getCurrentPrice());
        requestBean.setIsDownLoadUrl(1);
        requestBean.setIsInviteCode(isInvitecode);
        requestBean.setClickURL(goodsInfo.getPurchaseLink());

        return RxHttp.getInstance().getGoodsService().getGenerateForKaola(
                requestBean
        )
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(activity.<BaseResponse<String>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                });
    }

    public static Observable<BaseResponse<TKLBean>> getGetTkLFinalObservable(RxAppCompatActivity activity, ShopGoodInfo goodsInfo) {


        String editTemplate = (String) SharedPreferencesUtils.get(activity, C.sp.editTemplate, "");
        String isShortLink = App.getACache().getAsString(C.sp.isShortLink);
        if (TextUtils.isEmpty(isShortLink)) {
            isShortLink = "1";
        }
        RequestTKLBean requestBean = new RequestTKLBean();
        requestBean.setItemSourceId(goodsInfo.getItemSourceId());
        requestBean.setItemTitle(goodsInfo.getTitle());
        requestBean.setItemDesc(goodsInfo.getItemDesc());
        requestBean.setItemPicture(goodsInfo.getPicture());
        requestBean.setItemPrice(goodsInfo.getPrice());
        requestBean.setCouponPrice(goodsInfo.getCouponPrice());
        requestBean.setItemVoucherPrice(goodsInfo.getVoucherPrice());
        requestBean.setSaleMouth(TextUtils.isEmpty(goodsInfo.getSaleMonth()) ? "0" : goodsInfo.getSaleMonth());
        requestBean.setCouponUrl(goodsInfo.getCouponUrl());
        requestBean.setCommission(goodsInfo.getCommission());
        requestBean.setTemplate(editTemplate);
        requestBean.setIsShortLink(isShortLink);

        int isInviteCode = App.getACache().getAsInt(C.sp.SHARE_MOENY_IS_INVITECODE);
        int isDownLoadUrl = App.getACache().getAsInt(C.sp.SHARE_MOENY_IS_DOWNLOAD_URL);
        requestBean.setIsDownLoadUrl(isDownLoadUrl);
        requestBean.setIsInviteCode(isInviteCode);

        if (TextUtils.isEmpty(goodsInfo.material) || !"11".equals(goodsInfo.getItemSource())) {
            //非物料商品
            requestBean.setMaterial("0");
        } else {
            requestBean.setMaterial(goodsInfo.material);
        }
        return RxHttp.getInstance().getGoodsService().getTKL(requestBean)
                .compose(RxUtils.<BaseResponse<TKLBean>>switchSchedulers())
                .compose(activity.<BaseResponse<TKLBean>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                });

    }


    public static Observable<BaseResponse<CommonShareTemplateBean>> getGetTkLFinalObservable2(RxAppCompatActivity activity, ShopGoodInfo goodsInfo, int type) {



        TkBean requestBean = new TkBean();
        requestBean.setItemSourceId(goodsInfo.getItemSourceId());
        requestBean.setItemTitle(goodsInfo.getTitle());
        requestBean.setItemDesc(goodsInfo.getItemDesc());
        requestBean.setItemPicture(goodsInfo.getPicture());
        requestBean.setItemPrice(goodsInfo.getPrice());
        requestBean.setCouponPrice(goodsInfo.getCouponPrice());
        requestBean.setItemVoucherPrice(goodsInfo.getVoucherPrice());
        requestBean.setSaleMonth(TextUtils.isEmpty(goodsInfo.getSaleMonth()) ? "0" : goodsInfo.getSaleMonth());
        requestBean.setCouponUrl(goodsInfo.getCouponUrl());
        requestBean.setCommission(goodsInfo.getCommission());
        requestBean.setType(type);


        String isInviteCode = App.getACache().getAsString(C.sp.SHARE_MOENY_IS_INVITECODE);
        String isDownLoadUrl = App.getACache().getAsString(C.sp.SHARE_MOENY_IS_DOWNLOAD_URL);
        if (TextUtils.isEmpty(isInviteCode)){
            isInviteCode = "1";
        }
        if (TextUtils.isEmpty(isDownLoadUrl)){
            isDownLoadUrl = "1";
        }
        String isShortLink = App.getACache().getAsString(C.sp.isShortLink);
        if (TextUtils.isEmpty(isShortLink)) {
            isShortLink = "0";
        }
        requestBean.setIsDownLoadUrl(isDownLoadUrl);
        requestBean.setIsInviteCode(isInviteCode);
        requestBean.setIsShowTkl(isShortLink);

        if (TextUtils.isEmpty(goodsInfo.material) || !"11".equals(goodsInfo.getItemSource())) {
            //非物料商品
            requestBean.setMaterial("0");
        } else {
            requestBean.setMaterial(goodsInfo.material);
        }
        return RxHttp.getInstance().getGoodsService().getCommonShareTemplate(requestBean)
                .compose(RxUtils.<BaseResponse<CommonShareTemplateBean>>switchSchedulers())
                .compose(activity.<BaseResponse<CommonShareTemplateBean>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                });
    }

    public static Observable<BaseResponse<CommonShareTemplateBean>> getGetTkLFinalObservable3(RxAppCompatActivity activity, ShopGoodInfo goodsInfo, int type) {

        TkBean requestBean = new TkBean();
        requestBean.setItemSourceId(goodsInfo.getItemSourceId());
        requestBean.setItemTitle(goodsInfo.getTitle());
        requestBean.setItemDesc(goodsInfo.getItemDesc());
        requestBean.setItemPicture(goodsInfo.getPicture());
        requestBean.setItemPrice(goodsInfo.getPrice());
        requestBean.setItemVoucherPrice(goodsInfo.getVoucherPrice());
        requestBean.setCouponUrl(goodsInfo.getCouponUrl());
        requestBean.setCommission(goodsInfo.getCommission());
        requestBean.setType(type);


        String isInviteCode = App.getACache().getAsString(C.sp.SHARE_MOENY_IS_INVITECODE);
        String isDownLoadUrl = App.getACache().getAsString(C.sp.SHARE_MOENY_IS_DOWNLOAD_URL);
        if (TextUtils.isEmpty(isInviteCode)){
            isInviteCode = "1";
        }
        if (TextUtils.isEmpty(isDownLoadUrl)){
            isDownLoadUrl = "1";
        }
        requestBean.setIsDownLoadUrl(isDownLoadUrl);
        requestBean.setIsInviteCode(isInviteCode);
        requestBean.setIsShortLink(1);

        return RxHttp.getInstance().getGoodsService().getCommonShareTemplate(requestBean)
                .compose(RxUtils.<BaseResponse<CommonShareTemplateBean>>switchSchedulers())
                .compose(activity.<BaseResponse<CommonShareTemplateBean>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                });
    }





    public static Observable<BaseResponse<TKLBean>> getGetTkObservable(RxAppCompatActivity activity, ShopGoodInfo goodsInfo) {


        String editTemplate = (String) SharedPreferencesUtils.get(activity, C.sp.editTemplate, "");
        String isShortLink = App.getACache().getAsString(C.sp.isShortLink);
        if (TextUtils.isEmpty(isShortLink)) {
            isShortLink = "1";
        }
        RequestTKLBean requestBean = new RequestTKLBean();
        requestBean.setItemSourceId(goodsInfo.getItemSourceId());
        requestBean.setItemTitle(goodsInfo.getTitle());
        requestBean.setItemDesc(goodsInfo.getItemDesc());
        requestBean.setItemPicture(goodsInfo.getPicture());
        requestBean.setItemPrice(goodsInfo.getPrice());
        requestBean.setCouponPrice(goodsInfo.getCouponPrice());
        requestBean.setItemVoucherPrice(goodsInfo.getVoucherPrice());
        requestBean.setSaleMouth(TextUtils.isEmpty(goodsInfo.getSaleMonth()) ? "0" : goodsInfo.getSaleMonth());
        requestBean.setCouponUrl(goodsInfo.getCouponUrl());
        requestBean.setCommission(goodsInfo.getCommission());
        requestBean.setTemplate(editTemplate);
        requestBean.setIsShortLink(isShortLink);

        int isInviteCode = App.getACache().getAsInt(C.sp.SHARE_MOENY_IS_INVITECODE);
        int isDownLoadUrl = App.getACache().getAsInt(C.sp.SHARE_MOENY_IS_DOWNLOAD_URL);
        requestBean.setIsDownLoadUrl(1);
        requestBean.setIsInviteCode(isInviteCode);

        if (TextUtils.isEmpty(goodsInfo.material) || !"11".equals(goodsInfo.getItemSource())) {
            //非物料商品
            requestBean.setMaterial("0");
        } else {
            requestBean.setMaterial(goodsInfo.material);
        }
        return RxHttp.getInstance().getGoodsService().getTKL(
                requestBean
        )
                .compose(RxUtils.<BaseResponse<TKLBean>>switchSchedulers())
                .compose(activity.<BaseResponse<TKLBean>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                });

    }

    /**
     * 获取优惠券
     *
     * @param activity
     * @param goodsInfo
     */
    public static void getCouponInfo(final RxAppCompatActivity activity, final ShopGoodInfo goodsInfo) {
        LoadingView.showDialog(activity, "请求中...");

        RequestCouponUrlBean requestBean = new RequestCouponUrlBean();
        requestBean.setItemSourceId(goodsInfo.getItemSourceId());
        requestBean.setCouponUrl(goodsInfo.getCouponUrl());
        if (TextUtils.isEmpty(goodsInfo.material) || !"11".equals(goodsInfo.getItemSource())) {
            //非物料商品
            requestBean.setMaterial("0");
        } else {
            requestBean.setMaterial(goodsInfo.material);
        }

        RxHttp.getInstance().getGoodsService()
//                .getCouponUrl(goodsInfo.getItemSourceId(), goodsInfo.getCouponUrl() )
                .getCouponUrl(requestBean)
                .compose(RxUtils.<BaseResponse<CouponUrlBean>>switchSchedulers())
                .compose(activity.<BaseResponse<CouponUrlBean>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                })

                .subscribe(new DataObserver<CouponUrlBean>() {

                    @Override
                    protected void onSuccess(final CouponUrlBean data) {

                        AppClickAgent.goodsAddClickEvent(activity, "跳转淘宝详情-其他商品");//统计按钮的点击数
                        if (data.getCheckCouponStatus() != null){
                            putCouponInfo(data.getCheckCouponStatus(),data, 1);
                        }else{
                           String endTime =  goodsInfo.getCouponEndTime();
                           String formatEndTime = "";
                           if (!TextUtils.isEmpty(endTime)){
                               try{
                                   formatEndTime = DateTimeUtils.formatMMddForEndTime(endTime);
                               }catch (Exception e){
                                   MyLog.d("lhp","e错误："+e.getMessage());
                               }

                           }

                           String nowTime = DateTimeUtils.getNowMMDD(CommonLocalData.syncServiceTime());
                            if (formatEndTime != null  ){
                                if (formatEndTime.compareToIgnoreCase(nowTime) < 0 ){
                                    TaobaoUtil.showUrl(activity, data.getItemUrl());
                                }else{
                                    TaobaoUtil.showUrl(activity, data.getCouponUrl());
                                }
                            }else{
                                TaobaoUtil.showUrl(activity, data.getCouponUrl());
                            }

                        }

                    }

                    /**
                     *  上传优费卷数据
                     *  重试一次
                     * @param data
                     */
                    private void putCouponInfo(final CheckCouponStatusBean data, final CouponUrlBean couponUrlData,
                                               final int retryNnm) {
                        if (data == null || data.getCookie() == null || data.getUrl() == null)
                            return;
                        if (retryNnm > 2)
                            return;

                        Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(ObservableEmitter<String> e) throws Exception {
                                Response response = getResponse(data);
                                if (response.isSuccessful()) {
                                    String msg = response.body().string();
                                    if (!TextUtils.isEmpty(msg)) {
                                        e.onNext(msg);
                                    }
                                }
                            }
                        })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new CallBackObserver<String>() {
                                    @Override
                                    public void onNext(String json) {

                                        //                                        RxHttp.getInstance().getUsersService().uploadCouponInfo(goodsInfo.getItemSourceId(), json, retryNnm, 1)
                                        RxHttp.getInstance().getUsersService().uploadCouponInfo(new RequestUploadCouponInfo().setItemSourceId(goodsInfo.getItemSourceId())
                                                .setCouponJson(json)
                                                .setCount(retryNnm)
                                                .setBeParsed(1))
                                                .compose(RxUtils.<BaseResponse<CheckCouponStatusBean>>switchSchedulers())
                                                .compose(activity.<BaseResponse<CheckCouponStatusBean>>bindToLifecycle())
                                                .subscribe(new DataObserver<CheckCouponStatusBean>(false) {
                                                    @Override
                                                    protected void onSuccess(CheckCouponStatusBean data) {
                                                        int validCode = data.getValideCode();
                                                        switch (validCode){
                                                            case 0:
                                                                int count = retryNnm + 1;
                                                                if (count >= 3 ){
                                                                    TaobaoUtil.showUrl(activity, couponUrlData.getCouponUrl());
                                                                }else{
                                                                    putCouponInfo(data, couponUrlData,count);
                                                                }
                                                                break;
                                                            case 1://有效优惠券
                                                                TaobaoUtil.showUrl(activity, couponUrlData.getCouponUrl());
                                                                break;
                                                            case 2://无效优惠券
                                                                TaobaoUtil.showUrl(activity, couponUrlData.getItemUrl());
                                                                break;
                                                             default:
                                                                 TaobaoUtil.showUrl(activity, couponUrlData.getCouponUrl());
                                                        }

                                                    }
                                                });
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                    }
                                });
                    }

                    /**
                     * 拼接获取优惠券地址
                     * @param data
                     * @return
                     * @throws IOException
                     */
                    private Response getResponse(CheckCouponStatusBean data) throws IOException {
                        OkHttpClient okClient = new OkHttpClient();
                        Request request = new Request.Builder()
                                .addHeader("cookie", data.getCookie())
                                .url(data.getUrl())
                                .build();
                        return okClient.newCall(request).execute();
                    }
                });

    }


    /**
     * 生成虚拟页面数据并保存为图片
     *
     * @param activity
     * @param goodsInfo
     * @param goodBitmap
     * @throws Exception
     */
    public static String saveGoodsImg(Activity activity, ShopGoodInfo goodsInfo, Bitmap goodBitmap, String extension, String ewmUrl) throws Exception {
        Bitmap qrBitmap = getShareEWMBitmap(activity, ewmUrl);
        return saveGoodsImg(activity, goodsInfo, goodBitmap, qrBitmap, extension);
    }

    /**
     * 生成虚拟页面数据并保存为图片
     *
     * @param activity
     * @param goodsInfo
     * @param goodBitmap
     * @throws Exception
     */
    public static String saveGoodsImg(Activity activity, ShopGoodInfo goodsInfo, Bitmap goodBitmap, Bitmap ewmBitmap, String extension) throws Exception {
        //设置布局数据
        View view = getGoodsPosterView(activity, goodsInfo, goodBitmap, ewmBitmap);
        Bitmap vBitmap = getViewBitmap(view);
        //保存图片到本地
        String saveMakePath = SdDirPath.IMAGE_CACHE_PATH + "goodqr_" + System.currentTimeMillis() + ".jpg";
        File file = new File(saveMakePath);

        vBitmap.compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(file));
        vBitmap.recycle();
        vBitmap = null;
        return saveMakePath;
    }



    /**
     * 生成虚拟页面数据并保存为图片
     *
     * @param activity
     * @throws Exception
     */
    public static String saveHungryGoodsImg(Activity activity,Bitmap img) throws Exception {
        //设置布局数据
        View view = getHungryPoster(activity,img);
        Bitmap vBitmap = getViewBitmap(view);
        //保存图片到本地
        String saveMakePath = SdDirPath.IMAGE_CACHE_PATH + "goodqr_" + System.currentTimeMillis() + ".jpg";
        File file = new File(saveMakePath);

        vBitmap.compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(file));
        vBitmap.recycle();
        vBitmap = null;
        return saveMakePath;
    }

    private static View getHungryPoster(Activity activity,Bitmap img) {
        View view = LayoutInflater.from(activity).inflate(R.layout.view_hungry_poster, null);

        ImageView share_img = (ImageView) view.findViewById(R.id.share_img2);
        if (img!=null)
            share_img.setImageBitmap(img);

        return view;
    }



    /**
     * 生成虚拟页面数据并保存为图片
     *
     * @param activity
     * @throws Exception
     */
    public static String saveAppletsGoodsImg(Activity activity,Bitmap img,Bitmap img2) throws Exception {
        //设置布局数据
        View view = getAppletsPoster(activity,img,img2);
        Bitmap vBitmap = getViewBitmap(view);
        //保存图片到本地
        String saveMakePath = SdDirPath.IMAGE_CACHE_PATH + "goodqr_" + System.currentTimeMillis() + ".jpg";
        File file = new File(saveMakePath);

        vBitmap.compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(file));
        vBitmap.recycle();
        vBitmap = null;
        return saveMakePath;
    }

    private static View getAppletsPoster(Activity activity,Bitmap img,Bitmap img2) {
        View view = LayoutInflater.from(activity).inflate(R.layout.view_applets_poster, null);

        ImageView imageView1 = (ImageView) view.findViewById(R.id.imgone);
        ImageView imageView2 = (ImageView) view.findViewById(R.id.imgtwo);
        RoundedImageView tv_tou= (RoundedImageView) view.findViewById(R.id.tv_tou2);
        TextView tv_name= (TextView) view.findViewById(R.id.tv_name2);

        UserInfo user = UserLocalData.getUser(activity);
        tv_name.setText(user.getNickName());
        LoadImgUtils.setImgCircle(activity, tv_tou, user.getHeadImg(), R.drawable.head_icon);
        if (img!=null&&img2!=null)
            Log.e("qwer",img2+"hhhh");
            imageView1.setImageBitmap(img);
            imageView2.setImageBitmap(img2);

        return view;
    }
    public static Bitmap getViewBitmap(View v) {
        if (v == null) {
            return null;
        }
        int me = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(me, me);
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap screenshot;
        screenshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.RGB_565);
        Canvas c = new Canvas(screenshot);
        v.draw(c);
        return screenshot;

    }

    @NonNull
    public static View getGoodsPosterView(Activity activity, ShopGoodInfo goodsInfo, Bitmap goodBitmap, Bitmap ewmBitmap) {
        View view = LayoutInflater.from(activity).inflate(R.layout.view_new_make_goods_poster, null);
        TextView goodShopTag = (TextView) view.findViewById(R.id.goodShopTag);

        RoundedImageView goods_img = (RoundedImageView) view.findViewById(R.id.goods_img);
        ImageView qrcode_img = (ImageView) view.findViewById(R.id.qrcode_img);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView cop_price = (TextView) view.findViewById(R.id.cop_price);
        TextView juanhou_prise = (TextView) view.findViewById(R.id.juanhou_prise);
        TextView tv_sales = (TextView) view.findViewById(R.id.tv_sales);
        TextView yuan_prise = (TextView) view.findViewById(R.id.yuan_prise);
        TextView tv_invite_code = (TextView) view.findViewById(R.id.tv_invite_code);
        TextView ll_prise=view.findViewById(R.id.ll_prise);
        RelativeLayout rl_cop_price=view.findViewById(R.id.rl_cop_price);
        if (goodBitmap != null) {
            goods_img.setImageBitmap(goodBitmap);
        }
        tv_invite_code.setText(activity.getString(R.string.invitation_code, UserLocalData.getUser().getInviteCode()));
        if (StringsUtils.isEmpty(goodsInfo.getCouponPrice())) {
            cop_price.setVisibility(View.GONE);
        } else {
            cop_price.setText(activity.getString(R.string.yuan, MathUtils.getnum(goodsInfo.getCouponPrice())));
        }
        juanhou_prise.setText("¥ " + MathUtils.getSalesPrice(MathUtils.getnum(goodsInfo.getVoucherPrice())));
        if (TextUtils.isEmpty(goodsInfo.getPrice())){
            rl_cop_price.setVisibility(View.GONE);
        }else{
            rl_cop_price.setVisibility(View.VISIBLE);
            yuan_prise.setText("¥ " + MathUtils.getnum(goodsInfo.getPrice()));
        }


        yuan_prise.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        if (!"0".equals(goodsInfo.getSaleMonth())) {
            tv_sales.setText("销量:  " + MathUtils.getSales(goodsInfo.getSaleMonth()));
        }
        if (goodsInfo.getShopType() == 2) {
            goodShopTag.setText("天猫");
        } else if (goodsInfo.getShopType() == 1) {
            goodShopTag.setText("淘宝");
        } else if (goodsInfo.getShopType() == 3) {
            goodShopTag.setText("拼多多");
        } else if(goodsInfo.getShopType() == 4){
            goodShopTag.setText("京东");
        } else if(goodsInfo.getShopType() == 5){
            goodShopTag.setText("考拉");
            tv_sales.setVisibility(View.GONE);
        } else if(goodsInfo.getShopType() == 6){
            goodShopTag.setText("唯品会");
            tv_sales.setVisibility(View.GONE);
            ll_prise.setText("抢购价");
        }
        Paint paint = new Paint();
        paint.setTextSize(goodShopTag.getTextSize());
        float size = paint.measureText(goodShopTag.getText().toString());
        if (!StringsUtils.isEmpty(goodsInfo.getTitle())) {
            StringsUtils.retractTitles(title, goodsInfo.getTitle(), (int) (size)+20);
        }

        Log.e("private","二维码"+ewmBitmap);
        if (ewmBitmap != null)
            qrcode_img.setImageBitmap(ewmBitmap);
        return view;
    }

    /**
     * 获取二维码
     *
     * @param activity
     * @return
     */
    public static Bitmap getShareEWMBitmap(Activity activity, String ewmUrl) {
        int qrSize = 790;
//        Bitmap logoBitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.share_logo);
        Bitmap bitmap = UserLocalData.getmMyHeadBitmap();
        if (TextUtils.isEmpty(ewmUrl) && TextUtils.isEmpty(UserLocalData.getUser(activity).getHeadImg())) {
            ViewShowUtils.showLongToast(activity, "生成失败");
            if (bitmap != null) {
                return bitmap;
            } else {
                return null;
            }
        } else {
            if (TextUtils.isEmpty(ewmUrl)) {
                ewmUrl = UserLocalData.getUser(activity).getHeadImg();
            }
            return QrcodeUtils.createQRCodeWithLogo(ewmUrl, qrSize, bitmap);
        }
    }
//网络图转化为bitmap
public static Bitmap returnBitMap(final String url){

    new Thread(new Runnable() {
        @Override
        public void run() {
            URL imageurl = null;

            try {
                imageurl = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                HttpURLConnection conn = (HttpURLConnection)imageurl.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                 bitmap = BitmapFactory.decodeStream(is);
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }).start();

    return bitmap;
}

    /**
     * 获取二维码
     *
     * @param activity
     * @return
     */
    public static Observable<Bitmap> getShareEWMBitmapObservable(final Activity activity, final String ewmUrl) {
        return Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> e) throws Exception {
                MyLog.threadName();
                Bitmap qrBitmap = getShareEWMBitmap(activity, ewmUrl);
                if (qrBitmap != null) {
                    e.onNext(qrBitmap);
                } else {
                    e.onError(new Exception("生成二维码失败"));
                }
            }
        });
    }


    // 获取淘口令
    public static void SharePosterList2(final Activity activity, final List<ShopGoodInfo> osgData, final MyAction.OnResult<String> action) {
        RequestPermissionUtlis.requestOne(activity, new MyAction.OnResult<String>() {
            @Override
            public void invoke(String arg) {
                LoadingView.showDialog(activity);
                if (osgData == null || osgData.size() == 0) {
                    LogUtils.Log("GoodsUtil", "dismissDialog  1");
                    LoadingView.dismissDialog();
                    return;
                }
                String ids = "";
                final List<ShopGoodInfo> list = new ArrayList<>();// 去掉过期商品
                for (int i = 0; i < osgData.size(); i++) {
                    ShopGoodInfo shopGoodInfo = osgData.get(i);
                    if (shopGoodInfo.getIsExpire() == 1) {//过期
                        continue;
                    }
                    if (!TextUtils.isEmpty(shopGoodInfo.getItemSourceId())) { // 添加 ids
                        list.add(shopGoodInfo);
                        if (i == osgData.size() - 1) {
                            ids = ids + shopGoodInfo.getItemSourceId();
                        } else {
                            ids = ids + shopGoodInfo.getItemSourceId() + ",";
                        }
                    }
                }
                if (TextUtils.isEmpty(ids) || list.size() == 0) {
                    LogUtils.Log("GoodsUtil", "dismissDialog  2");
                    ViewShowUtils.showShortToast(activity, activity.getResources().getString(R.string.goodsShertError));
                    LoadingView.dismissDialog();
                    return;
                }
                ShareMore.getShareGoodsUrlListObservable((RxAppCompatActivity) activity, osgData, ids)

                        .observeOn(Schedulers.io())
                        .map(new Function<BaseResponse<ShareUrlListBaen>, Map<String, String>>() {
                            @Override
                            public Map<String, String> apply(final BaseResponse<ShareUrlListBaen> shareUrlListBaen) throws Exception {
                                if (shareUrlListBaen == null || !C.requestCode.SUCCESS.equals(shareUrlListBaen.getCode())) {
                                    return null;
                                }
                                ShareUrlListBaen data = shareUrlListBaen.getData();
                                if (data == null || data.getLink() == null || data.getLink().size() == 0) {
                                    return null;
                                }
                                final Map<String, String> map = new HashMap<>();
                                try {
                                    for (int i = 0; i < data.getLink().size(); i++) {
                                        if (list.size() - 1 < i) {
                                            continue;
                                        }
                                        final ShopGoodInfo goodsInfo = list.get(i);
                                        final String shareUrl = data.getLink().get(i);
                                        Bitmap goodsBitmap = LoadImgUtils.getImgBitmapOnIo(activity, goodsInfo.getPicture());
                                        if (goodsBitmap != null) {

                                            String s = saveGoodsImg(activity, goodsInfo, goodsBitmap, data.getExtension() == null ? "" : data.getExtension(), shareUrl);
                                            map.put(goodsInfo.getItemSourceId(), s);

                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    LogUtils.Log("GoodsUtil", "LoadImgToBitmap  onError + " + e.getMessage());
                                }
                                return map;
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(new Action() {
                            @Override
                            public void run() throws Exception {
                                LogUtils.Log("GoodsUtil", "dismissDialog  3");
                                LoadingView.dismissDialog();
                            }
                        })
                        .subscribe(new CallBackObserver<Map<String, String>>() {
                            @Override
                            public void onNext(Map<String, String> map) {
                                LogUtils.Log("GoodsUtil", "map  onNext " + map.size());
                                ArrayList<String> urlList = new ArrayList();
                                for (ShopGoodInfo info : list) {
                                    String taobao = info.getItemSourceId();
                                    if (!TextUtils.isEmpty(taobao)) {
                                        String s = map.get(taobao);
                                        urlList.add(s);
                                    }

                                }
                                if (urlList.size() != 0) {
                                    toShareActionPosterList(activity, urlList, action);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                ViewShowUtils.showShortToast(activity, "存在商品已下架，无法分享");
                                LogUtils.Log("GoodsUtil", "map  onNext  onError " + e.getMessage());

                            }
                        });


            }

            @Override
            public void onError() {
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }
    // 获取淘口令
    public static void SharePosterList3(final Activity activity, final List<ShopGoodInfo> osgData, final MyAction.OnResult<String> action) {
        RequestPermissionUtlis.requestOne(activity, new MyAction.OnResult<String>() {
            @Override
            public void invoke(String arg) {
                LoadingView.showDialog(activity);
                if (osgData == null || osgData.size() == 0) {
                    LogUtils.Log("GoodsUtil", "dismissDialog  1");
                    LoadingView.dismissDialog();
                    return;
                }
                String ids = "";
                final List<ShopGoodInfo> list = new ArrayList<>();// 去掉过期商品
                for (int i = 0; i < osgData.size(); i++) {
                    ShopGoodInfo shopGoodInfo = osgData.get(i);
                    if (shopGoodInfo.getIsExpire() == 1) {//过期
                        continue;
                    }
                    if (!TextUtils.isEmpty(shopGoodInfo.getItemSourceId())) { // 添加 ids
                        list.add(shopGoodInfo);
                        if (i == osgData.size() - 1) {
                            ids = ids + shopGoodInfo.getItemSourceId();
                        } else {
                            ids = ids + shopGoodInfo.getItemSourceId() + ",";
                        }
                    }
                }
                if (TextUtils.isEmpty(ids) || list.size() == 0) {
                    LogUtils.Log("GoodsUtil", "dismissDialog  2");
                    ViewShowUtils.showShortToast(activity, activity.getResources().getString(R.string.goodsShertError));
                    LoadingView.dismissDialog();
                    return;
                }
                ShareMore.getShareGoodsUrlMoreObservable((RxAppCompatActivity) activity, osgData, ids)

                        .observeOn(Schedulers.io())
                        .map(new Function<BaseResponse<List<ShareUrlMoreBaen>>, Map<String, String>>() {
                            @Override
                            public Map<String, String> apply(final BaseResponse<List<ShareUrlMoreBaen>> shareUrlListBaen) throws Exception {
                                if (shareUrlListBaen == null || !C.requestCode.SUCCESS.equals(shareUrlListBaen.getCode())) {
                                    return null;
                                }
                                List<ShareUrlMoreBaen> data = shareUrlListBaen.getData();
                               // ShareUrlMoreBaen data = (ShareUrlMoreBaen) shareUrlListBaen.getData();
                                if (data == null || data.get(0).getShareUrl() == null || data.size() == 0) {
                                    return null;
                                }
                                final Map<String, String> map = new HashMap<>();
                                try {
                                    for (int i = 0; i < data.size(); i++) {
                                        if (list.size() - 1 < i) {
                                            continue;
                                        }
                                        final ShopGoodInfo goodsInfo = list.get(i);
                                        final String shareUrl = data.get(i).getShareUrl();
                                        Bitmap goodsBitmap = LoadImgUtils.getImgBitmapOnIo(activity, goodsInfo.getPicture());
                                        if (goodsBitmap != null) {

                                            String s = saveGoodsImg(activity, goodsInfo, goodsBitmap, "", shareUrl);
                                            map.put(goodsInfo.getItemSourceId(), s);

                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    LogUtils.Log("GoodsUtil", "LoadImgToBitmap  onError + " + e.getMessage());
                                }
                                return map;
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(new Action() {
                            @Override
                            public void run() throws Exception {
                                LogUtils.Log("GoodsUtil", "dismissDialog  3");
                                LoadingView.dismissDialog();
                            }
                        })
                        .subscribe(new CallBackObserver<Map<String, String>>() {
                            @Override
                            public void onNext(Map<String, String> map) {
                                LogUtils.Log("GoodsUtil", "map  onNext " + map.size());
                                ArrayList<String> urlList = new ArrayList();
                                for (ShopGoodInfo info : list) {
                                    String taobao = info.getItemSourceId();
                                    if (!TextUtils.isEmpty(taobao)) {
                                        String s = map.get(taobao);
                                        urlList.add(s);
                                    }

                                }
                                if (urlList.size() != 0) {
                                    toShareActionPosterList(activity, urlList, action);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                ViewShowUtils.showShortToast(activity, "存在商品已下架，无法分享");
                                LogUtils.Log("GoodsUtil", "map  onNext  onError " + e.getMessage());

                            }
                        });


            }

            @Override
            public void onError() {
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }

    // 获取淘口令
    public static void SharePosterList2(final Activity activity, final List<ShopGoodInfo> osgData, final int sharePlatform, final WeChatMomentsHintDialog dialog, final MyAction.OnResult<String> action) {
        RequestPermissionUtlis.requestOne(activity, new MyAction.OnResult<String>() {
            @Override
            public void invoke(String arg) {
                LoadingView.showDialog(activity);
                if (osgData == null || osgData.size() == 0) {
                    LogUtils.Log("GoodsUtil", "dismissDialog  1");
                    LoadingView.dismissDialog();
                    return;
                }
                String ids = "";
                final List<ShopGoodInfo> list = new ArrayList<>();// 去掉过期商品
                for (int i = 0; i < osgData.size(); i++) {
                    ShopGoodInfo shopGoodInfo = osgData.get(i);
                    if (shopGoodInfo.getIsExpire() == 1) {//过期
                        continue;
                    }
                    if (!TextUtils.isEmpty(shopGoodInfo.getItemSourceId())) { // 添加 ids
                        list.add(shopGoodInfo);
                        if (i == osgData.size() - 1) {
                            ids = ids + shopGoodInfo.getItemSourceId();
                        } else {
                            ids = ids + shopGoodInfo.getItemSourceId() + ",";
                        }
                    }
                }
                if (TextUtils.isEmpty(ids) || list.size() == 0) {
                    LogUtils.Log("GoodsUtil", "dismissDialog  2");
                    ViewShowUtils.showShortToast(activity, activity.getResources().getString(R.string.goodsShertError));
                    LoadingView.dismissDialog();
                    return;
                }
                ShareMore.getShareGoodsUrlListObservable((RxAppCompatActivity) activity, osgData, ids)

                        .observeOn(Schedulers.io())
                        .map(new Function<BaseResponse<ShareUrlListBaen>, Map<String, String>>() {
                            @Override
                            public Map<String, String> apply(final BaseResponse<ShareUrlListBaen> shareUrlListBaen) throws Exception {
                                if (shareUrlListBaen == null || !C.requestCode.SUCCESS.equals(shareUrlListBaen.getCode())) {
                                    return null;
                                }
                                ShareUrlListBaen data = shareUrlListBaen.getData();
                                if (data == null || data.getLink() == null || data.getLink().size() == 0) {
                                    return null;
                                }
                                final Map<String, String> map = new HashMap<>();
                                try {
                                    for (int i = 0; i < data.getLink().size(); i++) {
                                        if (list.size() - 1 < i) {
                                            continue;
                                        }
                                        final ShopGoodInfo goodsInfo = list.get(i);
                                        final String shareUrl = data.getLink().get(i);
                                        Bitmap goodsBitmap = LoadImgUtils.getImgBitmapOnIo(activity, goodsInfo.getPicture());
                                        if (goodsBitmap != null) {

                                            String s = saveGoodsImg(activity, goodsInfo, goodsBitmap, data.getExtension() == null ? "" : data.getExtension(), shareUrl);
                                            map.put(goodsInfo.getItemSourceId(), s);

                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    LogUtils.Log("GoodsUtil", "LoadImgToBitmap  onError + " + e.getMessage());
                                }
                                return map;
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(new Action() {
                            @Override
                            public void run() throws Exception {
                                LogUtils.Log("GoodsUtil", "dismissDialog  3");
                                LoadingView.dismissDialog();
                            }
                        })
                        .subscribe(new CallBackObserver<Map<String, String>>() {
                            @Override
                            public void onNext(Map<String, String> map) {
                                LogUtils.Log("GoodsUtil", "map  onNext " + map.size());
                                ArrayList<String> urlList = new ArrayList();
                                for (ShopGoodInfo info : list) {
                                    String taobao = info.getItemSourceId();
                                    if (!TextUtils.isEmpty(taobao)) {
                                        String s = map.get(taobao);
                                        urlList.add(s);
                                    }

                                }
                                if (urlList.size() != 0) {
                                    if (sharePlatform == ShareUtil.WeMomentsType) {
                                        if (urlList.size() == 1) {
                                            toShareActionPosterList(activity, urlList, sharePlatform, action);
                                        } else {
                                            if (dialog != null) {
                                                dialog.show();
                                            }
                                        }
                                    } else {
                                        toShareActionPosterList(activity, urlList, sharePlatform, action);
                                    }


                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                ViewShowUtils.showShortToast(activity, "存在商品已下架，无法分享");
                                LogUtils.Log("GoodsUtil", "map  onNext  onError " + e.getMessage());

                            }
                        });


            }

            @Override
            public void onError() {
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }


    /**
     * 分享生成的多张海报图片
     */
    private static void toShareActionPosterList(final Activity activity, final ArrayList<String> makeList, final MyAction.OnResult<String> action) {
        //下载完成
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<File> imageUris = new ArrayList<>();
                if (makeList == null || makeList.size() == 0) {
                    if (action != null) {
                        action.onError();
                    }
                    ViewShowUtils.showShortToast(activity, activity.getResources().getString(R.string.goodsShertError));
                    return;
                }
                for (int i = 0; i < makeList.size(); i++) {
                    String urlPs = makeList.get(i);
                    if (TextUtils.isEmpty(urlPs)) {
                        continue;
                    }
//                    Uri uri = Uri.fromFile(new File(urlPs));
                    imageUris.add(new File(urlPs));
                }
                if (imageUris.size() == 0) {
                    ViewShowUtils.showShortToast(activity, "商品优惠券已领完，请重新选择商品");
                    if (action != null) {
                        action.onError();
                    }
                    return;
                }
                try {// 可能分享有兼容问题
                    originalShareImage(activity, imageUris, ShareUtil.MoreType);
                    if (action != null) {
                        action.invoke("");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (action != null) {
                        action.onError();
                    }
                }
                //                Intent mulIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                //                mulIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
                //                mulIntent.setType("image/jpeg");
                //                activity.startActivity(Intent.createChooser(mulIntent, "多图文件分享"));

            }
        });

    }

    /**
     * 分享生成的多张海报图片
     */
    private static void toShareActionPosterList(final Activity activity, final ArrayList<String> makeList, final int sharePlatform, final MyAction.OnResult<String> action) {
        //下载完成
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<File> imageUris = new ArrayList<>();
                if (makeList == null || makeList.size() == 0) {
                    if (action != null) {
                        action.onError();
                    }
                    ViewShowUtils.showShortToast(activity, activity.getResources().getString(R.string.goodsShertError));
                    return;
                }
                for (int i = 0; i < makeList.size(); i++) {
                    String urlPs = makeList.get(i);
                    if (TextUtils.isEmpty(urlPs)) {
                        continue;
                    }
//                    Uri uri = Uri.fromFile(new File(urlPs));
                    imageUris.add(new File(urlPs));
                }
                if (imageUris.size() == 0) {
                    ViewShowUtils.showShortToast(activity, "商品优惠券已领完，请重新选择商品");
                    if (action != null) {
                        action.onError();
                    }
                    return;
                }
                try {// 可能分享有兼容问题
                    sysShareImage(activity, imageUris, sharePlatform);
                    if (action != null) {
                        action.invoke("");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (action != null) {
                        action.onError();
                    }
                }
                //                Intent mulIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                //                mulIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
                //                mulIntent.setType("image/jpeg");
                //                activity.startActivity(Intent.createChooser(mulIntent, "多图文件分享"));

            }
        });

    }

    /**
     * 兼容android 8.0 分享
     *
     * @param context
     * @param files
     * @param type
     */
    public static void originalShareImage(Context context, ArrayList<File> files, int type) {
        Intent share_intent = new Intent();
        ArrayList<Uri> imageUris = new ArrayList<Uri>();
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            for (File f : files) {
                Uri imageContentUri = getImageContentUri(context, f);
                imageUris.add(imageContentUri);
            }
        } else {
            for (File f : files) {
                //                Uri.fromFile(f).toString().contains("")
                imageUris.add(Uri.fromFile(f));
            }

        }
        if (type == ShareUtil.WeMomentsType) {
            share_intent.setClassName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");//微信朋友圈，仅支持分享图片
        }
        share_intent.setAction(Intent.ACTION_SEND_MULTIPLE);//设置分享行为
        share_intent.setType("image/png");//设置分享内容的类型
        share_intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        share_intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        context.startActivity(Intent.createChooser(share_intent, "分享"));
        LoadingView.dismissDialog();
    }


    /**
     * 兼容android 8.0 分享
     *
     * @param context
     * @param files
     * @param
     */
    public static void sysShareImage(Context context, ArrayList<File> files, int sharePlatform) {
        Intent share_intent = new Intent();
        ArrayList<Uri> imageUris = new ArrayList<Uri>();
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            for (File f : files) {
                Uri imageContentUri = getImageContentUri(context, f);
                imageUris.add(imageContentUri);
            }
        } else {
            for (File f : files) {
                //                Uri.fromFile(f).toString().contains("")
                imageUris.add(Uri.fromFile(f));
            }

        }

        if (sharePlatform == ShareUtil.WeMomentsType) {//微信朋友圈，仅支持分享图片
            if (files.size() > 0) {
                ShareUtil.Image.toWechatMoments((Activity) context, files.get(0).getPath(), null);
            } else {

            }
            LoadingView.dismissDialog();
            return;

//            ComponentName componentName = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
//            share_intent.setComponent(componentName);
        } else if (sharePlatform == ShareUtil.QQType) {
            ComponentName componentName = new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");
            share_intent.setComponent(componentName);
        } else if (sharePlatform == ShareUtil.QQZoneType) {
            String[] images = new String[files.size()];
            for (int i = 0; i < files.size(); i++) {
                images[i] = files.get(i).getPath();
            }
            ShareUtil.Image.toQQRoom((Activity) context, images, null);
            LoadingView.dismissDialog();
            return;
//            ComponentName componentName = new ComponentName( "com.qzone", "com.qzonex.module.operation.ui.QZonePublishMoodActivity");
//            share_intent.setComponent(componentName);
        } else if (sharePlatform == ShareUtil.WechatType) {
            ComponentName componentName = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
            share_intent.setComponent(componentName);
        } else if (sharePlatform == ShareUtil.WeiboType) {
            share_intent.setPackage("com.sina.weibo");
        }
        share_intent.setAction(Intent.ACTION_SEND_MULTIPLE);//设置分享行为
        share_intent.setType("image/png");//设置分享内容的类型
        share_intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        share_intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        context.startActivity(Intent.createChooser(share_intent, "分享"));
        LoadingView.dismissDialog();
    }

    /**
     * @param context
     * @param imageFile
     * @return content Uri
     */
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * 圈子的bean 转换成商品详情
     *
     * @param item
     * @return
     */

    @NonNull
    public static List<ShopGoodInfo> CirleInfoToGoodInfo(MarkermallCircleInfo item) {
        try {
            if (item == null || item.getGoods() == null || item.getGoods().size() == 0) {
                return null;
            }
            List<ShopGoodInfo> goods = item.getGoods();
            return goods;
        } catch (Exception e) {
            return null;
        }
    }


    public static void  poster(final Context context){
        RequestActivityLinkBean bean = new RequestActivityLinkBean();
        bean.setActivityId("1579491209717");
        RxHttp.getInstance().getGoodsService()
                .getQRcode(bean)
                .compose(RxUtils.<BaseResponse<WxCodeBean>>switchSchedulers())
                .subscribe(new DataObserver<WxCodeBean>() {
                    @Override
                    protected void onSuccess(WxCodeBean data) {
                        String activityLink = data.getWxQrcodeUrl();
                        if (TextUtils.isEmpty(activityLink)) return;
                        Glide.with(context)
                                .asBitmap()
                                .load(activityLink)
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        if (resource!=null){
                                            try {
                                               String shareHundry = GoodsUtil.saveHungryGoodsImg((Activity) context, resource);
                                               saveImg(context, shareHundry, FileUtils.getPictureName(shareHundry), new MyAction.OnResultTwo<File, Integer>() {
                                                   @Override
                                                   public void invoke(File arg, Integer arg1) {
                                                       if (arg != null) {
                                                           ToastUtils.showShort("下载成功请在相册查看");
                                                       }
                                                   }

                                                   @Override
                                                   public void onError() {

                                                   }
                                               });
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                });
                        //  LoadImgUtils.setViewBackground(ShareHungryActivity.this, share_img, activityLink);

                    }
                });

    }





    /**
     * Glide 加载图片保存到本地
     * <p>
     * imgUrl 图片地址
     * imgName 图片名称
     */


    public static void saveImg(final Context context, final String imgUrl, final String saveName, final MyAction.OnResultTwo<File, Integer> action) {
        String dir = SdDirPath.IMAGE_CACHE_PATH + saveName + ".jpg";
        File f = new File(dir);
        if (f.exists()) {//已经存在
            if (action != null) {
                action.invoke(f, 0);
            }
        }

        if (!imgUrl.startsWith("http")) {
            File imgFile = new File(imgUrl);
            if (imgFile.exists()) {
                if (action != null) {
                    action.invoke(imgFile, 1);
                }
                try {
                    MediaStore.Images.Media.insertImage(context.getContentResolver(), imgFile.getAbsolutePath(), saveName + ".jpg", null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                //  updataImgToTK(context, imgFile, saveName + ".jpg");
                BuglyUtils.e("saveImg", "permission state  == goSetting");
                return;
            }
        }
        LoadImgUtils.getImgToBitmapObservable(context, imgUrl)
                .subscribe(new CallBackObserver<Bitmap>() {
                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull Bitmap resource) {
                        /** * 分享图片 */
                        File file = null;
                        if (TextUtils.isEmpty(saveName)) {
                            file = saveBitmapToFile(context, resource, "img" + saveName);
                        } else {
                            file = saveBitmapToFile(context, resource, saveName);
                        }
                        if (file == null) {
                            BuglyUtils.putErrorToBugly("保存图片失败");
                        }
                        if (action != null) {
                            action.invoke(file, 0);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        BuglyUtils.e("saveImg", "  onLoadFailed " + imgUrl);
                        if (action != null)
                            action.invoke(null, 0);
                    }
                });

    }


    /**
     * 将图片存到本地
     */
    public static Uri saveBitmap(Context context, Bitmap bm, String picName) {
        try {
            String dir = SdDirPath.IMAGE_CACHE_PATH + picName + ".jpg";
            File f = new File(dir);
            if (f.exists()) {//已经存在
                return Uri.parse(dir);
            }

            if (!f.exists()) {
                f.getParentFile().mkdirs();
                f.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Uri uri = Uri.fromFile(f);
            updataImgToTK(context, f, picName + ".jpg");
            return uri;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将图片存到本地
     */
    public static File saveBitmapToFile(final Context context, final Bitmap bm, final String picName) {
        return saveBitmapToFile(context, bm, picName, true);
    }

    public static File saveBitmapToFile(final Context context, Bitmap bm, final String picName, final boolean isUpdataImgToTK) {
        String dir = SdDirPath.IMAGE_CACHE_PATH + picName + ".jpg";
        try {
            final File f = new File(dir);
            if (f.exists()) {//已经存在
                return f;
            }

            if (!f.exists()) {
                f.getParentFile().mkdirs();
                f.createNewFile();
            }
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(f);
                bm.compress(Bitmap.CompressFormat.PNG, 90, out);
                if (out == null) {
                    out.flush();
                    out.close();
                }
                if (isUpdataImgToTK) {
                  //  updataImgToTK(context, f, picName + ".jpg");
                    MediaStore.Images.Media.insertImage(context.getContentResolver(), f.getAbsolutePath(), picName + ".jpg", null);
                }
//                if(null != bm && !bm.isRecycled()){
//                    bm.recycle();
//                    bm = null;
//                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return f;
        } catch (Exception e) {
            e.printStackTrace();
        }
        BuglyUtils.e("saveImg", "  onLoadFailed " + dir);
        return null;
    }

    /**
     * 更新图片到图库
     *
     * @param context
     * @param file
     * @param fileName
     */
    public static void updataImgToTK(final Context context, final File file, String fileName) {
        MyLog.i("test", "file: " + file.getPath() + " fileName: " + fileName);
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //此时已在主线程中，可以更新UI了
                if (TextUtils.isEmpty(file.getPath())) {
                    ViewShowUtils.showLongToast(context, "保存图片失败,请(" + SdDirPath.APK_DOWNLOAD_CACHE_PATH + ")目录下查看图片");
                    return;
                }
            }
        });

        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file.getPath().contains("file")) {
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(file.getPath())));
        } else {
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));
        }

    }


    /**
     * @param context
     * @return
     */
    public static Observable<String> getGoodsData(final RxAppCompatActivity context, final String string) {
        MyLog.d("Thread  + " + Thread.currentThread().getName());

        return RxWXHttp.getInstance().getService(C.getInstance().getGoodsIp()).profilePicture(string)
                .compose(RxUtils.<String>switchSchedulers())
                .compose(context.<String>bindToLifecycle());

    }

    public static void checkGoods(RxFragment fragment, String itemSourceId, final MyAction.One<ShopGoodInfo> action) {
        RequestCheckGoodsBean requestCheckGoodsBean = new RequestCheckGoodsBean();
        requestCheckGoodsBean.setItemSourceId(itemSourceId);
        RxHttp.getInstance().getUsersService().onCheckGoods(requestCheckGoodsBean)
                .compose(RxUtils.<BaseResponse<ShopGoodInfo>>switchSchedulers())
                .compose(fragment.<BaseResponse<ShopGoodInfo>>bindToLifecycle()).subscribe(new DataObserver<ShopGoodInfo>() {
            @Override
            protected void onSuccess(ShopGoodInfo data) {
                action.invoke(data);
            }
        });
    }

    public static void checkGoods(RxAppCompatActivity activity, String itemSourceId, final MyAction.One<ShopGoodInfo> action) {
        RequestCheckGoodsBean requestCheckGoodsBean = new RequestCheckGoodsBean();
        requestCheckGoodsBean.setItemSourceId(itemSourceId);
        RxHttp.getInstance().getUsersService().onCheckGoods(requestCheckGoodsBean)
                .compose(RxUtils.<BaseResponse<ShopGoodInfo>>switchSchedulers())
                .compose(activity.<BaseResponse<ShopGoodInfo>>bindToLifecycle()).subscribe(new DataObserver<ShopGoodInfo>() {
            @Override
            protected void onSuccess(ShopGoodInfo data) {
                action.invoke(data);
            }
        });
    }



//跳转小程序
    public static  void jumpApplets(Context context){
        String appId = "wx0d185820ca66c15b"; // 填应用AppId
        IWXAPI api = WXAPIFactory.createWXAPI(context, appId);

        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = "gh_84aa568cb2a1"; // 填小程序原始id
        req.path = "pages/index/index";                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开  0是正式 1是开发 2是体验版本
        api.sendReq(req);

    }


    //获取vip页面地址
    public static  void getVipH5(final Context context){

        RxHttp.getInstance().getCommonService().getConfigForKey(new RequestKeyBean().setKey(C.SysConfig.UPGRADE_H5))
                .compose(RxUtils.<BaseResponse<HotKeywords>>switchSchedulers())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                    }
                })
                .subscribe(new DataObserver<HotKeywords>() {
                    @Override
                    protected void onSuccess(HotKeywords data) {
                        final String commssionH5 = data.getSysValue();
                        if (!TextUtils.isEmpty(commssionH5)){
                         ShowWebActivity.start((Activity) context,commssionH5,"");
                        }

                        Log.e("gggg",commssionH5+"");


                    }

                });

    }


    //获取注销页面
    public static  void getLogout(final Context context){

        RxHttp.getInstance().getCommonService().getConfigForKey(new RequestKeyBean().setKey(C.SysConfig.LOGOUT_H5))
                .compose(RxUtils.<BaseResponse<HotKeywords>>switchSchedulers())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                    }
                })
                .subscribe(new DataObserver<HotKeywords>() {
                    @Override
                    protected void onSuccess(HotKeywords data) {
                        final String commssionH5 = data.getSysValue();
                        if (!TextUtils.isEmpty(commssionH5)){
                            ShowWebActivity.start((Activity) context,commssionH5,"");
                        }

                        Log.e("gggg",commssionH5+"");


                    }

                });

    }

    //获取vip页面地址
    public static  void getOrderSearch(final Context context){

        RxHttp.getInstance().getCommonService().getConfigForKey(new RequestKeyBean().setKey(C.SysConfig.ORDER_TRACKING))
                .compose(RxUtils.<BaseResponse<HotKeywords>>switchSchedulers())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                    }
                })
                .subscribe(new DataObserver<HotKeywords>() {
                    @Override
                    protected void onSuccess(HotKeywords data) {
                        final String commssionH5 = data.getSysValue();
                        if (!TextUtils.isEmpty(commssionH5)){
                            ShowWebActivity.start((Activity) context,commssionH5,"订单找回");
                        }

                        Log.e("gggg",commssionH5+"");


                    }

                });

    }
    //获取订单问题
    public static  void getOrdeRule(final Context context){

        RxHttp.getInstance().getCommonService().getConfigForKey(new RequestKeyBean().setKey(C.SysConfig.ORDER_COMMON_RULES))
                .compose(RxUtils.<BaseResponse<HotKeywords>>switchSchedulers())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                    }
                })
                .subscribe(new DataObserver<HotKeywords>() {
                    @Override
                    protected void onSuccess(HotKeywords data) {
                        final String commssionH5 = data.getSysValue();
                        if (!TextUtils.isEmpty(commssionH5)){
                            ShowWebActivity.start((Activity) context,commssionH5,"常见问题");
                        }

                        Log.e("gggg",commssionH5+"");


                    }

                });

    }

}
