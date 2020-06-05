package com.zjzy.morebit.goods.shopping.model;


import android.text.TextUtils;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.mvp.base.frame.MvpModel;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.RxWXHttp;
import com.zjzy.morebit.pojo.KaolaBean;
import com.zjzy.morebit.pojo.ProgramGetGoodsDetailBean;
import com.zjzy.morebit.pojo.ReleaseGoodsPermission;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.request.RequesKoalaBean;
import com.zjzy.morebit.pojo.request.RequestGoodsCollectBean;
import com.zjzy.morebit.pojo.request.RequestMaterialLink;
import com.zjzy.morebit.pojo.request.RequestPromotionUrlBean;
import com.zjzy.morebit.pojo.request.RequestReleaseGoods;
import com.zjzy.morebit.pojo.requestbodybean.RequestItemSourceId;
import com.zjzy.morebit.pojo.requestbodybean.RequestShopId;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.encrypt.EncryptUtlis;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import okhttp3.RequestBody;

/**
 * Created by fengrs
 * Data: 2018/12/15.
 * 返现 model
 */
public class GoodsDetailForPddModel extends MvpModel {

    public Observable<BaseResponse<Integer>> getGoodsCollect(BaseActivity rxActivity, ShopGoodInfo goodsInfo) {
        RequestGoodsCollectBean requestBean = new RequestGoodsCollectBean();
        requestBean.setItemSourceId(goodsInfo.getItemSourceId());
        requestBean.setItemTitle(goodsInfo.getTitle());
        requestBean.setItemPicture(goodsInfo.getPicture());
        requestBean.setItemPrice(goodsInfo.getPrice());
        requestBean.setCouponPrice(goodsInfo.getCouponPrice());
        requestBean.setItemVoucherPrice(goodsInfo.getVoucherPrice());
        requestBean.setCouponUrl(goodsInfo.getCouponUrl());
        requestBean.setCouponEndTime(goodsInfo.getCouponEndTime());
        requestBean.setCommission(goodsInfo.getCommission());
        requestBean.setShopType(goodsInfo.getShopType()+"");
        requestBean.setShopName(goodsInfo.getShopName());
        requestBean.setSaleMonth(TextUtils.isEmpty(goodsInfo.getSaleMonth())?"0":goodsInfo.getSaleMonth());
        requestBean.setSign(EncryptUtlis.getSign2(requestBean));

        return RxHttp.getInstance().getCommonService().getGoodsCollect(
                requestBean
        )
                .compose(RxUtils.<BaseResponse<Integer>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<Integer>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                });
    }

    /**
     * 拼多多收藏
     * @param rxActivity
     * @param goodsInfo
     * @return
     */
    public Observable<BaseResponse<Integer>> getGoodsCollectForPdd(BaseActivity rxActivity, ShopGoodInfo goodsInfo) {
        RequestGoodsCollectBean requestBean = new RequestGoodsCollectBean();
        requestBean.setItemSourceId(goodsInfo.getGoodsId().toString());
        requestBean.setItemTitle(goodsInfo.getTitle());
        requestBean.setItemPicture(goodsInfo.getImageUrl());
        requestBean.setItemPrice(goodsInfo.getPriceForPdd());
        requestBean.setCouponPrice(goodsInfo.getCouponPrice());
        requestBean.setItemVoucherPrice(goodsInfo.getVoucherPriceForPdd());
        requestBean.setCouponUrl(goodsInfo.getCouponUrl());
        requestBean.setCouponEndTime(goodsInfo.getCouponEndTime());
        requestBean.setCommission(goodsInfo.getCommission());
        requestBean.setShopType(goodsInfo.getShopType()+"");
        requestBean.setShopName(goodsInfo.getShopName());
        requestBean.setSaleMonth(TextUtils.isEmpty(goodsInfo.getSaleMonth())?"0":goodsInfo.getSaleMonth());
        requestBean.setSign(EncryptUtlis.getSign2(requestBean));

        return RxHttp.getInstance().getCommonService().getGoodsCollect(
                requestBean
        )
                .compose(RxUtils.<BaseResponse<Integer>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<Integer>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                });
    }

    /**
     * 拼多多
     * @param rxActivity
     * @param goodsInfo
     * @return
     */
    public Observable<BaseResponse<ShopGoodInfo>> getBaseResponseObservableForPdd(BaseActivity rxActivity, ShopGoodInfo goodsInfo) {
        return RxHttp.getInstance().getCommonService().getJdPddGoodsDetail(new ProgramGetGoodsDetailBean().setType(2)
                .setGoodsId(goodsInfo.getGoodsId()))
                .compose(RxUtils.<BaseResponse<ShopGoodInfo>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<ShopGoodInfo>>bindToLifecycle());
    }

    /**
     * 拼多多
     * @param rxActivity
     * @param
     * @return
     */
    public Observable<BaseResponse<String>> generatePromotionUrlForPdd(BaseActivity rxActivity,
                                                                       Long goodsId,String couponUrl) {
        RequestPromotionUrlBean bean = new RequestPromotionUrlBean();
        bean.setType(2);
        bean.setGoodsId(goodsId);
        bean.setCouponUrl(couponUrl);
        return RxHttp.getInstance().getCommonService().generatePromotionUrlForPdd(bean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<String>>bindToLifecycle());
    }
    /**
     * 京东
     * @param rxActivity
     * @param goodsInfo
     * @return
     */
    public Observable<BaseResponse<ShopGoodInfo>> getBaseResponseObservableForJd(BaseActivity rxActivity, ShopGoodInfo goodsInfo) {
        return RxHttp.getInstance().getCommonService().getJdPddGoodsDetail(new ProgramGetGoodsDetailBean().setType(1)
                .setGoodsId(goodsInfo.getGoodsId()))
                .compose(RxUtils.<BaseResponse<ShopGoodInfo>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<ShopGoodInfo>>bindToLifecycle());
    }
    /**
     * 京东
     * @param rxActivity
     * @param
     * @return
     */
    public Observable<BaseResponse<String>> generatePromotionUrlForJd(BaseActivity rxActivity,
                                                                      Long goodsId,String couponUrl) {
        RequestPromotionUrlBean bean = new RequestPromotionUrlBean();
      //  bean.setType(2);
        bean.setGoodsId(goodsId);
        bean.setCouponUrl(couponUrl);
        return RxHttp.getInstance().getCommonService().generatePromotionUrlForJd(bean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<String>>bindToLifecycle());
    }



    public Observable<BaseResponse<ReleaseGoodsPermission>> checkPermission(RxAppCompatActivity rxActivity, RequestReleaseGoods body) {
        return RxHttp.getInstance().getGoodsService().checkPermission(body)
                .compose(RxUtils.<BaseResponse<ReleaseGoodsPermission>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<ReleaseGoodsPermission>>bindToLifecycle());
    }


    /**
     * 考拉海购
     * @param rxActivity
     * @param
     * @return
     */
    public Observable<BaseResponse<ShopGoodInfo>> getBaseResponseObservableForKaoLa(BaseActivity rxActivity, String  goodsId,String userId) {
        RequesKoalaBean requesKoalaBean = new RequesKoalaBean();
        requesKoalaBean .setUserId(userId);
        requesKoalaBean.setGoodsId(goodsId);
        return RxHttp.getInstance().getCommonService().getKaoLaGoodsDetail(requesKoalaBean)
                .compose(RxUtils.<BaseResponse<ShopGoodInfo>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<ShopGoodInfo>>bindToLifecycle());
    }


    /**
     * 考拉收藏
     * @param rxActivity
     * @param goodsInfo
     * @return
     */
    public Observable<BaseResponse<Integer>> getGoodsCollectForKaola(BaseActivity rxActivity, ShopGoodInfo goodsInfo) {
        RequestGoodsCollectBean requestBean = new RequestGoodsCollectBean();
        requestBean.setItemSourceId(goodsInfo.getGoodsId().toString());
        requestBean.setItemTitle(goodsInfo.getGoodsTitle());
        requestBean.setItemPicture(goodsInfo.getImageList().get(0));
        requestBean.setItemPrice(goodsInfo.getMarketPrice());
        requestBean.setCouponPrice(goodsInfo.getCouponPrice());
        requestBean.setItemVoucherPrice(goodsInfo.getCurrentPrice());
        requestBean.setCouponUrl(goodsInfo.getCouponUrl());
        requestBean.setCouponEndTime(goodsInfo.getCouponEndTime());
        requestBean.setCommission(goodsInfo.getCommission());
        requestBean.setShopType("5");
        requestBean.setShopName(goodsInfo.getShopName());
        requestBean.setSaleMonth(TextUtils.isEmpty(goodsInfo.getSaleMonth())?"0":goodsInfo.getSaleMonth());
        requestBean.setSign(EncryptUtlis.getSign2(requestBean));

        return RxHttp.getInstance().getCommonService().getGoodsCollect(
                requestBean
        )
                .compose(RxUtils.<BaseResponse<Integer>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<Integer>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                });
    }

}