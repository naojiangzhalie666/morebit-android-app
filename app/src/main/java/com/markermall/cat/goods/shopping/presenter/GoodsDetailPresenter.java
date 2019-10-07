package com.markermall.cat.goods.shopping.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.markermall.cat.Activity.GoodsDetailActivity;
import com.markermall.cat.LocalData.UserLocalData;
import com.markermall.cat.Module.common.Activity.BaseActivity;
import com.markermall.cat.R;
import com.markermall.cat.goods.shopping.contract.GoodsDetailContract;
import com.markermall.cat.goods.shopping.model.GoodsDetailModel;
import com.markermall.cat.main.model.ConfigModel;
import com.markermall.cat.main.model.MainModel;
import com.markermall.cat.main.ui.fragment.GoodNewsFramgent;
import com.markermall.cat.mvp.base.frame.MvpPresenter;
import com.markermall.cat.network.BaseResponse;
import com.markermall.cat.network.CallBackObserver;
import com.markermall.cat.network.RxHttp;
import com.markermall.cat.network.RxUtils;
import com.markermall.cat.network.observer.DataObserver;
import com.markermall.cat.pojo.GoodsBrowsHistory;
import com.markermall.cat.pojo.HotKeywords;
import com.markermall.cat.pojo.ImageInfo;
import com.markermall.cat.pojo.ReleaseGoodsPermission;
import com.markermall.cat.pojo.ShopGoodInfo;
import com.markermall.cat.pojo.goods.TaobaoGoodBean;
import com.markermall.cat.pojo.request.RequestReleaseGoods;
import com.markermall.cat.utils.C;
import com.markermall.cat.utils.MyGsonUtils;
import com.markermall.cat.utils.MyLog;
import com.markermall.cat.utils.TaobaoUtil;
import com.markermall.cat.utils.ViewShowUtils;
import com.markermall.cat.utils.dao.DBManager;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

/**
 * Created by fengrs on 2018/11/3.
 */

public class GoodsDetailPresenter extends MvpPresenter<GoodsDetailModel, GoodsDetailContract.View> implements GoodsDetailContract.Present {


    private MainModel mMainModel;


    /**
     * 获取详情页信息
     *
     * @param rxActivity
     * @param goodsInfo
     */
    @Override
    public void getDetailData(final BaseActivity rxActivity, ShopGoodInfo goodsInfo, final boolean isRefresh) {
        mModel.getBaseResponseObservable(rxActivity, goodsInfo)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().OngetDetailDataFinally();
                    }
                })
                .subscribe(new DataObserver<ShopGoodInfo>() {
                    @Override
                    protected void onSuccess(final ShopGoodInfo data) {
                        getIView().showDetailsView(data, false, isRefresh);
                        getIView().getTaoKouLing();
                        final boolean isImgsToData = getImgsToDataState(data);
                        if (isImgsToData) {
                            getIView().setModuleDescUrl(data, "");
                        }
                        if (!TextUtils.isEmpty(data.getTaobaoDetailUrl())) {
                            mModel.getprofileUrlObservable(rxActivity, data.getTaobaoDetailUrl())
                                    .map(new Function<String, String>() {
                                        @Override
                                        public String apply(String s) throws Exception {
                                            if (isImgsToData) {
                                                return s;
                                            } else {
                                                return getTaobaoDataForModuleDescUrl(s, data);
                                            }
                                        }
                                    })
                                    .subscribe(new CallBackObserver<String>() {
                                        @Override
                                        public void onNext(String s) {
                                            putTaobaoData(s);
                                        }
                                        @Override
                                        public void onError(Throwable e) {
                                            getIView().setModuleDescUrl(data, "");
                                        }
                                    });
                        } else {
                            getIView().showDetailsView(data, true, isRefresh);
                            if (!isImgsToData) {
                                getIView().setModuleDescUrl(data, "");
                            }
                        }
                    }

                    /**
                     *  上传淘宝数据到接口解析,成功后再加载数据
                     * @param s
                     */
                    private void putTaobaoData(String s) {
                        if (TextUtils.isEmpty(s)) {
                            return;
                        }
                        mModel.putTaobaoData(rxActivity, s)
                                .subscribe(new DataObserver<ShopGoodInfo>(false) {
                                    @Override
                                    protected void onSuccess(ShopGoodInfo data) {
                                        getIView().showDetailsView(data, true, isRefresh);
                                    }
                                });
                    }
                    /**
                     *  判断是否有商品详情页数据返回
                     * @param data
                     * @return
                     */
                    private boolean getImgsToDataState(ShopGoodInfo data) {
                        return data.getPicUrls() != null &&
                                                    data.getPicUrls().getA() != null &&
                                                    data.getPicUrls().getA().getContent() != null &&
                                                    data.getPicUrls().getA().getContent().size() != 0;
                    }

                    /**
                     *  自己解析淘宝详情数据中滴 ModuleDescUrl
                     *  ModuleDescUrl 是请求商品详情页滴请求url
                     * @param s
                     * @param data
                     * @return
                     */
                    private String getTaobaoDataForModuleDescUrl(String s, ShopGoodInfo data) {
                        try {
                            TaobaoGoodBean taobaoGoodBean = MyGsonUtils.jsonToBean(s, TaobaoGoodBean.class);
                            if (taobaoGoodBean == null ||
                                    taobaoGoodBean.getData() == null ||
                                    taobaoGoodBean.getData().getItem() == null ||
                                    TextUtils.isEmpty(taobaoGoodBean.getData().getItem().getModuleDescUrl())
                                    ) {
                                    getIView().setModuleDescUrl(data, "");
                                return s;
                            } else {
                                TaobaoGoodBean.DataBean taobaodataBean = taobaoGoodBean.getData();
                                TaobaoGoodBean.DataBean.BannerBean item = taobaodataBean.getItem();
                                String moduleDescUrl = item.getModuleDescUrl();
                                getIView().setModuleDescUrl(data, moduleDescUrl);
                                return s;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            return s;
                        }
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
            mModel.getGoodsCollect(rxActivity, goodsInfo)
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
     * 跳转店铺详情
     *
     * @param rxActivity
     * @param goodsInfo
     */
    @Override
    public void getShopList(final BaseActivity rxActivity, final ShopGoodInfo goodsInfo) {
        mModel.getShopList(rxActivity, goodsInfo)
                .subscribe(new CallBackObserver<BaseResponse<List<ShopGoodInfo>>>() {
                    @Override
                    public void onNext(BaseResponse<List<ShopGoodInfo>> baen) {
                        super.onNext(baen);
                        if (baen.getData() == null || baen.getData().size() == 0) {
                            ViewShowUtils.showShortToast(rxActivity, rxActivity.getString(R.string.shop_no_goods));
                            return;
                        }
                        ImageInfo imageInfo = new ImageInfo();
                        imageInfo.setTitle(rxActivity.getString(R.string.discount_stores));
                        imageInfo.categoryId = goodsInfo.getShopId();
                        GoodNewsFramgent.start(rxActivity, imageInfo, C.GoodsListType.DiscountStores);
                    }
                });

    }


    /**
     * 保存到我的足迹
     *
     * @param activity
     * @param info
     */
    @Override
    public void saveGoodsHistor(GoodsDetailActivity activity, ShopGoodInfo info) {
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


    private void saveGoodsHistorData(GoodsDetailActivity activity, ShopGoodInfo info) {
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

    public void materialLinkList(final RxAppCompatActivity rxActivity, String itemSourceId, String material) {
        mModel.materialLinkList(rxActivity, itemSourceId, material)
                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onSuccess(String data) {
                        if (!TextUtils.isEmpty(data)) {
                            TaobaoUtil.showUrl(rxActivity, data);
                        }
                    }
                });
    }
}
