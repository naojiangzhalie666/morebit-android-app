package com.zjzy.morebit.utils.share;

import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.pojo.ShareUrlBaen;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.goods.ShareUrlListBaen;
import com.zjzy.morebit.pojo.goods.ShareUrlMoreBaen;
import com.zjzy.morebit.pojo.goods.ShareUrlStringBaen;
import com.zjzy.morebit.pojo.request.RequestShareGoodsMoreUrlBean;
import com.zjzy.morebit.pojo.request.RequestShareGoodsUrlBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestShareGoods;
import com.zjzy.morebit.pojo.requestbodybean.RequestShareMoreGoods;
import com.zjzy.morebit.utils.MyGsonUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by fengres on 2018/6/26.
 * 分享数据获取的more
 */

public class ShareMore {

    /**
     * 获取商品二维码链接
     * <p>
     * 0. 商品分享链接
     * 1.多商品分享链接
     * 2.web集合页链接
     */
    public static Observable<BaseResponse<ShareUrlStringBaen>> getShareGoodsUrlObservable(RxAppCompatActivity activity, String taobaoId) {
        return RxHttp.getInstance().getCommonService().getShareGoodsUrlStringObservable(
                new RequestShareGoods().setType("2").setItemSourceId(taobaoId))
                .compose(RxUtils.<BaseResponse<ShareUrlStringBaen>>switchSchedulers())
                .compose(activity.<BaseResponse<ShareUrlStringBaen>>bindToLifecycle());

    }

    /**
     * 多商品分享链接
     */
    public static Observable<BaseResponse<ShareUrlListBaen>> getShareGoodsUrlListObservable(RxAppCompatActivity activity, List<ShopGoodInfo> goods,String ids) {
        ArrayList<RequestShareGoodsUrlBean> requestShareGoodsUrlBeans = new ArrayList<>();
        for (int i = 0; i < goods.size(); i++) {
            ShopGoodInfo info = goods.get(i);
            if (info.getIsExpire() == 1) {//过期
                continue;
            }
            requestShareGoodsUrlBeans.add(new RequestShareGoodsUrlBean(info.getItemSourceId(), info.getTitle(), info.getPicture(), info.getCouponUrl()));
        }
        RequestShareGoods requestShareGoods = new RequestShareGoods();
        requestShareGoods.setType("1");
        requestShareGoods.setItemSourceId(ids);
        requestShareGoods.setItemsJson(MyGsonUtils.beanToJson(requestShareGoodsUrlBeans));
        return RxHttp.getInstance().getCommonService().getShareGoodsUrlListObservable(requestShareGoods)
                .compose(RxUtils.<BaseResponse<ShareUrlListBaen>>switchSchedulers())
                .compose(activity.<BaseResponse<ShareUrlListBaen>>bindToLifecycle());

    }


    /**
     * 收藏多商品分享链接
     */
    public static Observable<BaseResponse<List<ShareUrlMoreBaen>>> getShareGoodsUrlMoreObservable(RxAppCompatActivity activity, List<ShopGoodInfo> goods, String ids) {
        ArrayList<RequestShareGoodsMoreUrlBean> requestShareGoodsUrlBeans = new ArrayList<>();
        for (int i = 0; i < goods.size(); i++) {
            ShopGoodInfo info = goods.get(i);
            if (info.getIsExpire() == 1) {//过期
                continue;
            }
            requestShareGoodsUrlBeans.add(new RequestShareGoodsMoreUrlBean(String.valueOf(info.getShopType()), info.getItemSourceId(),info.getCouponUrl()));
        }
        RequestShareMoreGoods requestShareGoods = new RequestShareMoreGoods();
        requestShareGoods.setItemShareVo(requestShareGoodsUrlBeans);
        return RxHttp.getInstance().getCommonService().getShareGoodsUrlMoreObservable(requestShareGoods)
                .compose(RxUtils.<BaseResponse<List<ShareUrlMoreBaen>>>switchSchedulers())
                .compose(activity.<BaseResponse<List<ShareUrlMoreBaen>>>bindToLifecycle());

    }


    /**
     * 下载APP分享
     */
    public static Observable<BaseResponse<ShareUrlBaen>> getShareAppLinkObservable(RxAppCompatActivity activity) {
        return RxHttp.getInstance().getGoodsService().getAppShareUrl()
                .compose(RxUtils.<BaseResponse<ShareUrlBaen>>switchSchedulers())
                .compose(activity.<BaseResponse<ShareUrlBaen>>bindToLifecycle());

    }

    /**
     * 下载APP分享
     */
    public static Observable<BaseResponse<ShareUrlBaen>> getShareAppLinkObservable(RxFragment rxFragment) {
        return RxHttp.getInstance().getGoodsService().getAppShareUrl()
                .compose(RxUtils.<BaseResponse<ShareUrlBaen>>switchSchedulers())
                .compose(rxFragment.<BaseResponse<ShareUrlBaen>>bindToLifecycle());

    }
}
