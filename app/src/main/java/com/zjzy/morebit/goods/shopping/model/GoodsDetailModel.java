package com.zjzy.morebit.goods.shopping.model;


import android.text.TextUtils;

import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.mvp.base.frame.MvpModel;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.RxWXHttp;
import com.zjzy.morebit.pojo.ProgramGetGoodsDetailBean;
import com.zjzy.morebit.pojo.ReleaseGoodsPermission;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.request.RequestGoodsCollectBean;
import com.zjzy.morebit.pojo.request.RequestMaterialLink;
import com.zjzy.morebit.pojo.request.RequestReleaseGoods;
import com.zjzy.morebit.pojo.requestbodybean.RequestItemSourceId;
import com.zjzy.morebit.pojo.requestbodybean.RequestShopId;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.encrypt.EncryptUtlis;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import okhttp3.RequestBody;

/**
 * Created by fengrs
 * Data: 2018/12/15.
 * 返现 model
 */
public class GoodsDetailModel extends MvpModel {

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


    public Observable<BaseResponse<List<ShopGoodInfo>>> getShopList(final BaseActivity rxActivity, final ShopGoodInfo goodsInfo) {
//        return RxHttp.getInstance().getGoodsService().getIdentical(goodsInfo.getShopId())
        return RxHttp.getInstance().getGoodsService().getIdentical(new RequestShopId().setShopId(goodsInfo.getShopId()))
                .compose(RxUtils.<BaseResponse<List<ShopGoodInfo>>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<List<ShopGoodInfo>>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                });
    }

    public Observable<BaseResponse<ShopGoodInfo>> getBaseResponseObservable(BaseActivity rxActivity, ShopGoodInfo goodsInfo) {
        return RxHttp.getInstance().getCommonService().getDetailData(new RequestItemSourceId().setItemSourceId(goodsInfo.getItemSourceId()).setItemFrom(goodsInfo.getItemFrom()))
                .compose(RxUtils.<BaseResponse<ShopGoodInfo>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<ShopGoodInfo>>bindToLifecycle());
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



    public Observable<String> getprofileUrlObservable(BaseActivity rxActivity, String url) {
        return RxWXHttp.getInstance().getService(C.getInstance().getGoodsIp()).profilePicture(url)
                .compose(RxUtils.<String>switchSchedulers())
                .compose(rxActivity.<String>bindToLifecycle());
    }

    public Observable<BaseResponse<ShopGoodInfo>> putTaobaoData(BaseActivity rxActivity, String data) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), data );
        return RxHttp.getInstance().getGoodsService().postTaobaoData(body)
                .compose(RxUtils.<BaseResponse<ShopGoodInfo>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<ShopGoodInfo>>bindToLifecycle());
    }

    public Observable<BaseResponse<ReleaseGoodsPermission>> checkPermission(RxAppCompatActivity rxActivity, RequestReleaseGoods body) {
        return RxHttp.getInstance().getGoodsService().checkPermission(body)
                .compose(RxUtils.<BaseResponse<ReleaseGoodsPermission>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<ReleaseGoodsPermission>>bindToLifecycle());
    }
    public Observable<BaseResponse<String>> materialLinkList(RxAppCompatActivity rxActivity, String itemSourceId, String material) {
        RequestMaterialLink  link = new RequestMaterialLink();
        link.setInvedeCode(UserLocalData.getUser().getInviteCode());
        link.setItemId(itemSourceId);
        link.setMaterial(material);
        return RxHttp.getInstance().getGoodsService().materialLinkList(link )
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<String>>bindToLifecycle());
    }

}