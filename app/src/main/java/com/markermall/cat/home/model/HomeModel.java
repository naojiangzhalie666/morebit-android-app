package com.markermall.cat.home.model;


import com.markermall.cat.mvp.base.frame.MvpModel;
import com.markermall.cat.network.BaseResponse;
import com.markermall.cat.network.RxHttp;
import com.markermall.cat.network.RxUtils;
import com.markermall.cat.pojo.FloorInfo;
import com.markermall.cat.pojo.ImageInfo;
import com.markermall.cat.pojo.ShopGoodInfo;
import com.markermall.cat.pojo.SystemConfigBean;
import com.markermall.cat.pojo.goods.HandpickBean;
import com.markermall.cat.pojo.goods.NewRecommendBean;
import com.markermall.cat.pojo.goods.RecommendBean;
import com.markermall.cat.pojo.request.RequestBannerBean;
import com.markermall.cat.pojo.request.RequestConfigKeyBean;
import com.markermall.cat.pojo.request.RequestRecommendBean;
import com.markermall.cat.pojo.requestbodybean.RequestOfficialRecommend;
import com.markermall.cat.utils.C;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by fengrs
 * Data: 2018/8/3.
 */
public class HomeModel extends MvpModel {
    public Observable<BaseResponse<RecommendBean>> getRecommend(RxFragment fragment, int page,String extra) {
        RequestRecommendBean requestBean = new RequestRecommendBean();
        requestBean.setPage(page);
        requestBean.setExtra(extra);
        requestBean.setType(C.more.jptjType);

        return RxHttp.getInstance().getGoodsService()
//                .getRecommend( page,extra, C. more.jptjType)
                .getRecommend(requestBean)
                .compose(RxUtils.<BaseResponse<RecommendBean>>switchSchedulers())
                .compose(fragment.<BaseResponse<RecommendBean>>bindToLifecycle());
    }
    public Observable<BaseResponse<NewRecommendBean>> getNewRecommend(RxFragment fragment, int page,int minNum,int type) {
        RequestRecommendBean requestBean = new RequestRecommendBean();
        requestBean.setPage(page);
        requestBean.setMinNum(minNum);
        requestBean.setType(type);

        return RxHttp.getInstance().getGoodsService()
//                .getRecommend( page,extra, C. more.jptjType)
                .getNewRecommend(requestBean)
                .compose(RxUtils.<BaseResponse<NewRecommendBean>>switchSchedulers())
                .compose(fragment.<BaseResponse<NewRecommendBean>>bindToLifecycle());
    }
    public Observable<BaseResponse<List<ImageInfo>>> getBanner(RxFragment fragment, int back) {
        RequestBannerBean requestBean = new RequestBannerBean();
        requestBean.setType(back);
        return   RxHttp.getInstance().getSysteService()
//                .getBanner(back,2)
                .getBanner(requestBean)
                .compose(RxUtils.<BaseResponse<List<ImageInfo>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<ImageInfo>>>bindToLifecycle());
    }


    public Observable<BaseResponse<List<FloorInfo>>> getFloor(RxFragment fragment) {
        return   RxHttp.getInstance().getSysteService()
                .getFloor()
                .compose(RxUtils.<BaseResponse<List<FloorInfo>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<FloorInfo>>>bindToLifecycle());
    }


    public Observable<BaseResponse<List<HandpickBean>>> getActivities(RxFragment fragment,   int page) {
        return     RxHttp.getInstance().getSysteService().getActivities(   )
                .compose(RxUtils.<BaseResponse<List<HandpickBean>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<HandpickBean>>>bindToLifecycle());
    }

    public Observable<BaseResponse<List<ShopGoodInfo>>> getOfficalList(RxFragment fragment, int type, int page, String keywords) {

        return RxHttp.getInstance().getGoodsService().getOfficalList(new RequestOfficialRecommend().setType(type).setPageNum(page).setKeyword(keywords))
                .compose(RxUtils.<BaseResponse<List<ShopGoodInfo>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<ShopGoodInfo>>>bindToLifecycle());


        //        return   RxHttp.getInstance().getGoodsService().getOfficalList(type,   page, keywords)
        //                .compose(RxUtils.<BaseResponse<List<ShopGoodInfo>>>switchSchedulers())
        //                .compose(fragment.<BaseResponse<List<ShopGoodInfo>>>bindToLifecycle());
    }


    public Observable<BaseResponse<SystemConfigBean>> getConfigByKey(RxFragment fragment, String  key) {
        RequestConfigKeyBean requestConfigKeyBean = new RequestConfigKeyBean();
        requestConfigKeyBean.setKey(key);
        return     RxHttp.getInstance().getSysteService().getConfigByKey(requestConfigKeyBean)
                .compose(RxUtils.<BaseResponse<SystemConfigBean>>switchSchedulers())
                .compose(fragment.<BaseResponse<SystemConfigBean>>bindToLifecycle());
    }
}
