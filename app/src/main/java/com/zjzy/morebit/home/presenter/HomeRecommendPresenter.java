package com.zjzy.morebit.home.presenter;

import com.zjzy.morebit.home.contract.HomeRecommentContract;
import com.zjzy.morebit.home.model.HomeModel;
import com.zjzy.morebit.mvp.base.frame.MvpPresenter;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.FloorInfo;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.goods.HandpickBean;
import com.zjzy.morebit.pojo.goods.NewRecommendBean;
import com.zjzy.morebit.pojo.goods.VideoBean;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.action.ACache;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

import io.reactivex.functions.Action;

/**
 * Created by YangBoTian on 2018/9/12.
 */

public class HomeRecommendPresenter extends MvpPresenter<HomeModel, HomeRecommentContract.View> implements HomeRecommentContract.Present {


    @Override
    public void getRecommend(RxFragment rxFragment, int page, String extra) {
//        mModel.getRecommend(rxFragment, page, extra)
//                .doFinally(new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        getIView().onRecommendFinally();
//                    }
//                }).subscribe(new DataObserver<RecommendBean>(false) {
//            @Override
//            protected void onDataListEmpty() {
//                getIView().onRecommendFailure();
//            }
//
//            @Override
//            protected void onSuccess(RecommendBean data) {
//                getIView().onRecommendSuccessful(data);
//            }
//        });
    }
    @Override
    public void getNewRecommend(RxFragment rxFragment, int page, int minNum,int type) {
        mModel.getNewRecommend(rxFragment, page, minNum,type)
              .subscribe(new DataObserver<NewRecommendBean>(false) {
                  @Override
                  protected void onError(String errorMsg, String errCode) {
                      getIView().onRecommendFailure(errorMsg,errCode);
                  }


            @Override
            protected void onSuccess(NewRecommendBean data) {
                getIView().onRecommendSuccessful(data);
            }
        });
    }
    @Override
    public void getBanner(RxFragment fragment, final int back) {
        mModel.getBanner(fragment, back)
                .subscribe(new DataObserver<List<ImageInfo>>(false) {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        getIView().onBannerFailure(errorMsg, back);

                    }


                    @Override
                    protected void onSuccess(List<ImageInfo> data) {
                        getIView().onBrandBanner(data, back);
                    }
                });
    }



    @Override
    public void getActivities(RxFragment fragment, int page) {
        mModel.getActivities(fragment, page)
                .subscribe(new DataObserver<List<HandpickBean>>(false) {
                    @Override
                    protected void onDataListEmpty() {
                        getIView().onActivityFailure();
                    }

                    @Override
                    protected void onDataNull() {
                        getIView().onActivityFailure();
                    }

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        getIView().onActivityFailure();
                    }

                    @Override
                    protected void onSuccess(List<HandpickBean> data) {
                        getIView().onActivitySuccessFul(data);
                    }
                });
    }

    @Override
    public void getFloor(final RxFragment fragment) {
        mModel.getFloor(fragment)
                .subscribe(new DataObserver<List<FloorInfo>>(false) {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        //getIView().onBannerFailure(errorMsg, back);
                        ACache.get(fragment.getActivity()).remove(C.sp.FLOOR_CACHE);
                    }

                    @Override
                    protected void onDataListEmpty() {
                        super.onDataListEmpty();
                        ACache.get(fragment.getActivity()).remove(C.sp.FLOOR_CACHE);
                    }

                    @Override
                    protected void onSuccess(List<FloorInfo> data) {
                        getIView().onFloorSuccess(data);
                    }
                });
    }
/*
* 首页抖货商品
*
* */
    @Override
    public void getVideo(RxFragment fragment) {
        mModel.getVideo(fragment)
                .compose(RxUtils.<BaseResponse<List<ShopGoodInfo>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<ShopGoodInfo>>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                })
                .subscribe(new DataObserver<List<ShopGoodInfo>>() {

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        getIView().onVideoFailure();
                    }

                    @Override
                    protected void onSuccess(List<ShopGoodInfo> data) {
                        getIView().onVideoSuccess(data);

                    }

                });
    }


}
