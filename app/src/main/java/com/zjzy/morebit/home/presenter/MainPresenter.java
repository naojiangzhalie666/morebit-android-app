package com.zjzy.morebit.home.presenter;

import android.text.TextUtils;

import com.zjzy.morebit.App;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.Module.push.Logger;
import com.zjzy.morebit.home.contract.MainContract;
import com.zjzy.morebit.main.model.MainModel;
import com.zjzy.morebit.mvp.base.frame.MvpPresenter;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.CallBackObserver;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.AppUpgradeInfo;
import com.zjzy.morebit.pojo.GrayUpgradeInfo;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.request.RequestBannerBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestOsBean;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.utils.FileUtils;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LogUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.NetWorkUtil;
import com.zjzy.morebit.utils.action.ACache;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by fengrs on 2019/4/15.
 */

public class MainPresenter extends MvpPresenter<MainModel, MainContract.View> implements MainContract.Present {

    @Override
    public void getPicture(final RxAppCompatActivity rxActivity) {
        RequestBannerBean requestBean = new RequestBannerBean();
        requestBean.setType(C.UIShowType.Start);
        RxHttp.getInstance().getSysteService()
                .getBanner(requestBean)
                .compose(RxUtils.<BaseResponse<List<ImageInfo>>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<List<ImageInfo>>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                })
                .subscribe(new DataObserver<List<ImageInfo>>(false) {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        App.getACache().remove(C.sp.appStartImg);
                    }

                    @Override
                    protected void onDataNull() {
                        App.getACache().remove(C.sp.appStartImg);
                    }

                    @Override
                    protected void onSuccess(List<ImageInfo> data) {
                        pictureOnSuccess(data,rxActivity);
                    }
                });
    }

    /**
     * app升级
     *
     * @param rxActivity
     */
    @Override
    public void getAppInfo(final RxAppCompatActivity rxActivity) {
        RequestOsBean requestOsBean = new RequestOsBean();
        RxHttp.getInstance().getSysteService().getAppVersion(requestOsBean)
                .compose(RxUtils.<BaseResponse<AppUpgradeInfo>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<AppUpgradeInfo>>bindToLifecycle())
                .subscribe(new DataObserver<AppUpgradeInfo>() {

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        getGrayUpgradeInfo(rxActivity,null);
                    }

                    @Override
                    protected void onSuccess(AppUpgradeInfo data) {
                        checkAppUpgrade(data,rxActivity);
                    }
                });
    }

    /**
     * 心跳接口
     *
     * @param rxActivity
     */
    @Override
    public void putHeart(RxAppCompatActivity rxActivity) {
        RxHttp.getInstance().getCommonService().putHeart()
                .compose(RxUtils.<BaseResponse>switchSchedulers())
                .subscribe(new Consumer<BaseResponse>() {
                    @Override
                    public void accept(BaseResponse baseResponse) throws Exception {
                    }
                });
    }

    /**
     * 获取系统通知
     *
     * @param rxActivity
     */
    @Override
    public void getSysNotification(RxAppCompatActivity rxActivity, final boolean isLoginSucceed) {
//        if (!LoginUtil.checkIsLogin(rxActivity, false)) {
//            return;
//        }
        RxHttp.getInstance().getSysteService().getSysNotification()
                .compose(RxUtils.<BaseResponse<List<ImageInfo>>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<List<ImageInfo>>>bindToLifecycle())
                .subscribe(new DataObserver<List<ImageInfo>>() {
                    @Override
                    protected void onSuccess(List<ImageInfo> data) {
                       getIView().setSysNotificationData(data,isLoginSucceed);
                    }
                });
    }


    private void checkAppUpgrade(AppUpgradeInfo data, RxAppCompatActivity rxActivity) {
        Logger.e("==checkAppUpgrade==");
        if (data == null) {
            return;
        }
        String myVersionCode = AppUtil.getVersionCode(rxActivity);
        String isDebug = data.getStatus();
        if (!"1".equals(isDebug)) {
            int myVcode = Integer.parseInt(myVersionCode);
            String sevrVersionCode = data.getVersion();
            int sevrVcode = Integer.parseInt(sevrVersionCode);
            if (myVcode < sevrVcode) { //需要升级
                if (!LoginUtil.checkIsLogin(rxActivity,false)) {
                    getIView().showUpdateView(data);
                }else{
                    getGrayUpgradeInfo(rxActivity,data);
                }
                return;
            }
        }
        getGrayUpgradeInfo(rxActivity,null);
    }

    /**
     * liys 本地视频有缓存, 则不需要重复下载
     *
     * @param data
     * @param rxActivity
     */
    private void pictureOnSuccess(final List<ImageInfo> data, RxAppCompatActivity rxActivity) {
        //获取上次缓存
        final List<ImageInfo> cacheImageInfoList = (ArrayList) App.getACache().getAsObject(C.sp.appStartImg);
        //保存(更新)缓存数据
        App.getACache().put(C.sp.appStartImg, (ArrayList) data);
        if (data == null) {
            return;
        }

        //下载视频逻辑(只下载一个)
        for (int i = 0; i < data.size(); i++) {
            final ImageInfo imageInfo = data.get(i);
            if (imageInfo.getMediaType() == 1) { //视频格式
                if (cacheImageInfoList == null) {
                    saveVideo(data, i, rxActivity); //没缓存, 直接下载
                    return;
                }

                boolean isCache = false; //本地是否缓存有当前mp4, true有缓存, false无缓存
                for (int j = 0; j < cacheImageInfoList.size(); j++) {
                    String cacheMp4Name = FileUtils.interceptingPictureName(cacheImageInfoList.get(j).videoPath);
                    String urlMp4Name = FileUtils.interceptingPictureName(imageInfo.getPicture());
                    if (cacheMp4Name.equals(urlMp4Name)) { //判断文件名是否相同
                        isCache = true;
                        //把缓存中的videoPath放到本次数据中, 再把本次数据保存到本地 (更新数据)
                        imageInfo.videoPath = cacheImageInfoList.get(j).videoPath;
                        App.getACache().put(C.sp.appStartImg, (ArrayList) data);
                        break;
                    }
                }
                if (!isCache) { //本地没缓存, 去下载
                    saveVideo(data, i,rxActivity);
                    return;
                }
            }else{
                //图片
                if(!TextUtils.isEmpty(imageInfo.getPicture())){
                    LoadImgUtils.getImgToFileObservable(rxActivity, imageInfo.getPicture())
                            .subscribe(new CallBackObserver<File>() {
                                @Override
                                public void onNext(File file) {
                                    imageInfo.setPicture(file.getAbsolutePath());
                                    App.getACache().put(C.sp.appStartImg, (ArrayList) data);
                                }

                                @Override
                                public void onError(Throwable e) {

                                }
                            });
                }
            }
        }
    }


    /**
     * 下载视频
     *  @param data
     * @param i
     * @param rxActivity
     */
    private void saveVideo(final List<ImageInfo> data, final int i, RxAppCompatActivity rxActivity) {
        ImageInfo imageInfo = data.get(i);
        String url = imageInfo.getPicture();
        if (TextUtils.isEmpty(url)) return;
        String target = FileUtils.getSDDataVideoPath(rxActivity);
        String interceptingName = FileUtils.interceptingPictureName(url);
        if (!NetWorkUtil.isNetWorkWifi(rxActivity)) {
            return;
        }

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new FileCallBack(target, interceptingName + ".mp4") {
                    @Override
                    public void inProgress(float progress, long total, int id) {
                        LogUtils.Log("OkHttpUtils", "progress  " + progress + "  total  " + total);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(File response, int id) {
                        String name = response.getName();
                        LogUtils.Log("OkHttpUtils", "name  " + name);
                        ImageInfo imageInfo = data.get(i);
                        imageInfo.videoPath = response.getPath();
                        App.getACache().put(C.sp.appStartImg, (ArrayList) data);
                    }

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                    }
                });
    }

    /**
     * 获取淘宝跳转链接
     *
     * @param rxActivity
     */
    @Override
    public void getTaobaoLink(final RxAppCompatActivity rxActivity) {
        RxHttp.getInstance().getSysteService().getTaobaoLink()
                .compose(RxUtils.<BaseResponse<List<String>>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<List<String>>>bindToLifecycle())
                .subscribe(new DataObserver<List<String>>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        super.onError(errorMsg, errCode);
                        ACache.get(rxActivity).remove(C.sp.TAOBAO_LINK_CACHE);
                    }

                    @Override
                    protected void onSuccess(List<String> data) {
                        getIView().saveTaobaoLinkData(data);
                    }
                });
    }

    /**
     - @Description:  灰度升级
     - @Author:  
     - @Time:  2019/9/8 10:33
     **/
    @Override
    public void getGrayUpgradeInfo(final RxAppCompatActivity rxActivity, final AppUpgradeInfo appUpgradeInfo) {
        if (!LoginUtil.checkIsLogin(rxActivity,false)) {
            return;
        }
        RequestOsBean requestOsBean = new RequestOsBean();
        RxHttp.getInstance().getSysteService().getGrayAppVersion(requestOsBean)
                .compose(RxUtils.<BaseResponse<GrayUpgradeInfo>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<GrayUpgradeInfo>>bindToLifecycle())
                .subscribe(new DataObserver<GrayUpgradeInfo>() {

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                         if(null != appUpgradeInfo){
                             getIView().showUpdateView(appUpgradeInfo);
                         }
                    }

                    @Override
                    protected void onSuccess(GrayUpgradeInfo data) {
                       checkGrayUpgrade(data,appUpgradeInfo,rxActivity);
                    }
                });
    }


    private void checkGrayUpgrade(GrayUpgradeInfo data, AppUpgradeInfo appUpgradeInfo, RxAppCompatActivity rxActivity) {
        Logger.e("==checkGrayUpgrade==");
        if (data == null || data.getVersion() == 0) {
            //显示版本更新
            if (null != appUpgradeInfo) {
                getIView().showUpdateView(appUpgradeInfo);
            }
            return;
        }
        String myVersionCode = AppUtil.getVersionCode(rxActivity);
        int myVcode = Integer.parseInt(myVersionCode);
        int graySevrVcode = data.getVersion(); //灰度的版本号
        if (myVcode < graySevrVcode) { //需要升级
            if (null != appUpgradeInfo) { //比较版本更新是否是强制升级,不然比较创建时间,时间最晚先弹
                if ("1".equals(appUpgradeInfo.getUpgradde())) {
                    //版本更新强制升级,不用比较灰度
                    //显示版本更新
                    getIView().showUpdateView(appUpgradeInfo);
                    return;
                }
                String appCreateTime = appUpgradeInfo.getCreateTime();
                long appCreateTimeL = 0;
                String grayCreateTime = data.getCreateTime();
                long grayCreateTimeL = 0;
                if(!TextUtils.isEmpty(appCreateTime)){
                    appCreateTimeL = DateTimeUtils.toLongTime(appCreateTime);
                }
                if(!TextUtils.isEmpty(grayCreateTime)){
                    grayCreateTimeL = DateTimeUtils.toLongTime(grayCreateTime);
                }
                if(grayCreateTimeL > appCreateTimeL){
                    //显示灰度升级
                    checkGrayUserid(data);
                }else{
                    //显示版本更新
                    getIView().showUpdateView(appUpgradeInfo);
                }

            }else{
                //比较userId
                checkGrayUserid(data);
            }

        }else{
            if(null != appUpgradeInfo){
                getIView().showUpdateView(appUpgradeInfo);
            }
        }

    }

    private void checkGrayUserid(GrayUpgradeInfo data){
        UserInfo userInfo = UserLocalData.getUser();
        if(!TextUtils.isEmpty(userInfo.getId())){
            int uid = Integer.parseInt(userInfo.getId());
            int userIdStart = data.getUserIdStart();
            int userIdEnd = data.getUserIdEnd();
            if(userIdStart> 0 && userIdStart > 0  && uid > 0 && uid >= userIdStart && uid <= userIdEnd){
                getIView().showGrayUpgradeView(data);
            }
        }

    }
}
