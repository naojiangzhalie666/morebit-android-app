package com.zjzy.morebit.goods.shopping.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.goods.shopping.contract.GoodsDetailContract;
import com.zjzy.morebit.goods.shopping.contract.GoodsDetailForPddContract;
import com.zjzy.morebit.goods.shopping.model.GoodsDetailForPddModel;
import com.zjzy.morebit.goods.shopping.model.GoodsDetailModel;
import com.zjzy.morebit.main.model.ConfigModel;
import com.zjzy.morebit.main.model.MainModel;
import com.zjzy.morebit.main.ui.fragment.GoodNewsFramgent;
import com.zjzy.morebit.mvp.base.frame.MvpPresenter;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.CallBackObserver;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.GoodsBrowsHistory;
import com.zjzy.morebit.pojo.HotKeywords;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.KaolaBean;
import com.zjzy.morebit.pojo.ReleaseGoodsPermission;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.goods.TaobaoGoodBean;
import com.zjzy.morebit.pojo.request.RequestReleaseGoods;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.MyGsonUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.TaobaoUtil;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.dao.DBManager;

import java.util.List;

import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

/**
 * Created by fengrs on 2018/11/3.
 */

public class GoodsDetailForPddPresenter extends MvpPresenter<GoodsDetailForPddModel, GoodsDetailForPddContract.View> implements GoodsDetailForPddContract.Present {


    private MainModel mMainModel;

    @Override
    public void getDetailDataForPdd(BaseActivity rxActivity, ShopGoodInfo goodsInfo, final boolean isRefresh) {
        mModel.getBaseResponseObservableForPdd(rxActivity, goodsInfo)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().OngetDetailDataFinally();
                    }
                })
                .subscribe(new DataObserver<ShopGoodInfo>() {
                    @Override
                    protected void onSuccess(final ShopGoodInfo data) {
                        getIView().showDetailsView(data, true, isRefresh);

                    }
                });
    }


    @Override
    public void generatePromotionUrl(BaseActivity rxActivity, Long goodsId, String couponUrl) {
        mModel.generatePromotionUrlForPdd(rxActivity, goodsId, couponUrl)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().OngetDetailDataFinally();
                    }
                })
                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onSuccess(final String data) {
                        getIView().setPromotionUrl(data);
//                        getIView().showDetailsView(data, false, isRefresh);
                    }
                });
    }



    /*
     * 京东领劵链接
     *
     * */
    @Override
    public void generatePromotionJdUrl(BaseActivity rxActivity, Long goodsId, String couponUrl) {
        mModel.generatePromotionUrlForJd(rxActivity, goodsId, couponUrl)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().OngetDetailDataFinally();
                    }
                })
                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onSuccess(final String data) {
                        getIView().setPromotionJdUrl(data);
                    }
                });
    }

    @Override
    public void getDetailDataForJd(BaseActivity rxActivity, ShopGoodInfo goodsInfo, final boolean isRefresh) {
        mModel.getBaseResponseObservableForJd(rxActivity, goodsInfo)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().OngetDetailDataFinally();
                    }
                })
                .subscribe(new DataObserver<ShopGoodInfo>() {
                    @Override
                    protected void onSuccess(final ShopGoodInfo data) {
                        getIView().showDetailsView(data, true, isRefresh);

                    }
                });
    }




    /*
    *
    * 考拉详情
    * */

    public void generateDetailForKaola(BaseActivity rxActivity, String goodsId, String userId, final boolean isRefresh) {
        mModel.getBaseResponseObservableForKaoLa(rxActivity, goodsId, userId)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().OngetDetailDataFinally();
                    }
                })
                .subscribe(new DataObserver<ShopGoodInfo>() {
                    @Override
                    protected void onSuccess(final ShopGoodInfo data) {
                        getIView().setDetaisData(data,true,isRefresh);
                    }
                });
    }
    /*
     *
     * 唯品会详情
     * */

    @Override
    public void generateDetailForWph(BaseActivity rxActivity, String goodsId, final boolean isRefresh) {
        mModel.getBaseResponseObservableForWph(rxActivity, goodsId)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().OngetDetailDataFinally();
                    }
                })
                .subscribe(new DataObserver<ShopGoodInfo>() {
                    @Override
                    protected void onSuccess(final ShopGoodInfo data) {
                        getIView().setDetaisDataWph(data,true,isRefresh);
                    }
                });
    }


    /**
     * 切换收藏
     *
     * @param goodsInfo
     */
    @Override
    public void switchCollect(final BaseActivity rxActivity, final ShopGoodInfo goodsInfo) {

        if (goodsInfo.getColler() != 0) {
            if (mMainModel == null) {
                mMainModel = new MainModel();
            }
            mMainModel.delUserCollection(rxActivity, goodsInfo.getColler() + "")
                    .subscribe(new DataObserver<String>(false) {
                        @Override
                        protected void onDataNull() {
                            onSuccess("");
                        }

                        @Override
                        protected void onSuccess(String data) {
                            UserLocalData.isCollect = true;
                            goodsInfo.setColler(0);
                            getIView().switchColler(goodsInfo);
                            ViewShowUtils.showShortToast(rxActivity, rxActivity.getString(R.string.cancel_collect_succeed));
                        }
                    });
        } else {
            mModel.getGoodsCollectForPdd(rxActivity, goodsInfo)
                    .subscribe(new DataObserver<Integer>() {
                        @Override
                        protected void onSuccess(Integer data) {
                            UserLocalData.isCollect = true;
                            goodsInfo.setColler(data);
                            getIView().switchColler(goodsInfo);
                            ViewShowUtils.showShortToast(rxActivity, rxActivity.getString(R.string.collect_succeed));

                        }
                    });
        }
    }

    /*
    *
    * 考拉收藏
    * */
    @Override
    public void switchKaolaCollect(final BaseActivity rxActivity, final ShopGoodInfo goodsInfo) {

        if (goodsInfo.getColler() != 0) {
            if (mMainModel == null) {
                mMainModel = new MainModel();
            }
            mMainModel.delUserCollection(rxActivity, goodsInfo.getColler() + "")
                    .subscribe(new DataObserver<String>(false) {
                        @Override
                        protected void onDataNull() {
                            onSuccess("");
                        }

                        @Override
                        protected void onSuccess(String data) {
                            UserLocalData.isCollect = true;
                            goodsInfo.setColler(0);
                            getIView().switchColler(goodsInfo);
                            ViewShowUtils.showShortToast(rxActivity, rxActivity.getString(R.string.cancel_collect_succeed));
                        }
                    });
        } else {
            mModel.getGoodsCollectForKaola(rxActivity, goodsInfo)
                    .subscribe(new DataObserver<Integer>() {
                        @Override
                        protected void onSuccess(Integer data) {
                            UserLocalData.isCollect = true;
                            goodsInfo.setColler(data);
                            getIView().switchColler(goodsInfo);
                            ViewShowUtils.showShortToast(rxActivity, rxActivity.getString(R.string.collect_succeed));

                        }
                    });
        }
    }
    /*
     *
     * 唯品会收藏
     * */
    @Override
    public void switchWphCollect(final BaseActivity rxActivity, final ShopGoodInfo goodsInfo) {

        if (goodsInfo.getColler() != 0) {
            if (mMainModel == null) {
                mMainModel = new MainModel();
            }
            mMainModel.delUserCollection(rxActivity, goodsInfo.getColler() + "")
                    .subscribe(new DataObserver<String>(false) {
                        @Override
                        protected void onDataNull() {
                            onSuccess("");
                        }

                        @Override
                        protected void onSuccess(String data) {
                            UserLocalData.isCollect = true;
                            goodsInfo.setColler(0);
                            getIView().switchColler(goodsInfo);
                            ViewShowUtils.showShortToast(rxActivity, rxActivity.getString(R.string.cancel_collect_succeed));
                        }
                    });
        } else {
            mModel.getGoodsCollectForWph(rxActivity, goodsInfo)
                    .subscribe(new DataObserver<Integer>() {
                        @Override
                        protected void onSuccess(Integer data) {
                            UserLocalData.isCollect = true;
                            goodsInfo.setColler(data);
                            getIView().switchColler(goodsInfo);
                            ViewShowUtils.showShortToast(rxActivity, rxActivity.getString(R.string.collect_succeed));

                        }
                    });
        }
    }


    /**
     * 保存到我的足迹
     *
     * @param activity
     * @param info
     */
    @Override
    public void saveGoodsHistor(BaseActivity activity, ShopGoodInfo info) {
        info.setPicUrls(null);
        Log.i("test", "count: " + DBManager.getInstance().getGoodsHistoryCount());
        if (DBManager.getInstance().getGoodsHistoryCount() < 200) {  //最多存200
            saveGoodsHistorData(activity, info);
        } else {  //超过200条,删去最后一条再添加
            GoodsBrowsHistory goodsHistory = DBManager.getInstance().queryLastGoodsHistory();
            MyLog.i("test", "goods: " + goodsHistory);
            DBManager.getInstance().deleteGoodsHistory(goodsHistory);
            saveGoodsHistorData(activity, info);
        }
    }

    /**
     * 获取系统通知
     *
     * @param rxActivity
     */
    @Override
    public void getSysNotification(RxAppCompatActivity rxActivity) {
//        if (!LoginUtil.checkIsLogin(rxActivity, false)) {
//            return;
//        }
        RxHttp.getInstance().getSysteService().getSysNotification()
                .compose(RxUtils.<BaseResponse<List<ImageInfo>>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<List<ImageInfo>>>bindToLifecycle())
                .subscribe(new DataObserver<List<ImageInfo>>() {
                    @Override
                    protected void onSuccess(List<ImageInfo> data) {
                        getIView().setSysNotificationData(data);
                    }
                });
    }


    private void saveGoodsHistorData(BaseActivity activity, ShopGoodInfo info) {
        String phone = UserLocalData.getUser(activity).getPhone();
        GoodsBrowsHistory goodsHistory = DBManager.getInstance().queryGoodsHistory(info.getItemSourceId());
        if (goodsHistory == null) {
            GoodsBrowsHistory newGoodsHistory = (GoodsBrowsHistory) MyGsonUtils.jsonToBean(MyGsonUtils.beanToJson(info), GoodsBrowsHistory.class);
            if (newGoodsHistory == null) return;
            newGoodsHistory.setBrowse_time(System.currentTimeMillis());

            if (!TextUtils.isEmpty(phone)) {
                newGoodsHistory.setPhone(phone);
            }
            DBManager.getInstance().insertGoodsHistory(newGoodsHistory);
        } else {
            DBManager.getInstance().deleteGoodsHistory(goodsHistory);
            GoodsBrowsHistory newGoodsHistory = (GoodsBrowsHistory) MyGsonUtils.jsonToBean(MyGsonUtils.beanToJson(info), GoodsBrowsHistory.class);
            if (newGoodsHistory == null) return;
            if (!TextUtils.isEmpty(phone)) {
                newGoodsHistory.setPhone(phone);
            }
            newGoodsHistory.setBrowse_time(System.currentTimeMillis());
            DBManager.getInstance().insertGoodsHistory(newGoodsHistory);
        }
    }

    /**
     * 获取普通会员，VIP，运营商佣金比例
     *
     * @param rxActivity
     */
    @Override
    public void getSysCommissionPercent(RxAppCompatActivity rxActivity) {
        ConfigModel.getInstance().getConfigForKey((RxAppCompatActivity) rxActivity, C.SysConfig.COMMISSION_PERCENT)
                .subscribe(new DataObserver<HotKeywords>() {
                    @Override
                    protected void onSuccess(HotKeywords data) {
                        String sysValue = data.getSysValue();
                        if (!TextUtils.isEmpty(sysValue)) {
                            C.SysConfig.COMMISSION_PERCENT_VALUE = sysValue;
                            getIView().setUpdateView(sysValue);
                        }
                    }
                });

    }

    @Override
    public void checkPermission(RxAppCompatActivity rxActivity, RequestReleaseGoods releaseGoods) {
        mModel.checkPermission(rxActivity, releaseGoods)
                .subscribe(new DataObserver<ReleaseGoodsPermission>() {
                    @Override
                    protected void onSuccess(ReleaseGoodsPermission data) {
                        getIView().checkPermission(data);
                    }
                });
    }


}
