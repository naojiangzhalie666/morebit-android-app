package com.jf.my.circle.presenter;

import com.jf.my.circle.contract.CommercialCollegeContract;
import com.jf.my.circle.model.CircleModel;
import com.jf.my.mvp.base.frame.MvpPresenter;
import com.jf.my.network.BaseResponse;
import com.jf.my.network.RxHttp;
import com.jf.my.network.RxUtils;
import com.jf.my.network.observer.DataObserver;
import com.jf.my.pojo.Article;
import com.jf.my.pojo.CollegeHome;
import com.jf.my.pojo.ImageInfo;
import com.jf.my.pojo.MiyuanInformation;
import com.jf.my.pojo.StudyRank;
import com.jf.my.pojo.request.RequestBannerBean;
import com.jf.my.pojo.request.RequestListBody;
import com.jf.my.utils.C;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

import io.reactivex.functions.Action;

/**
 * Created by YangBoTian on 2018/9/12.
 */

public class CommercialCollegePresenter extends MvpPresenter<CircleModel, CommercialCollegeContract.View> implements CommercialCollegeContract.Present {

    @Override
    public void getCommercialBanner(RxFragment rxFragment) {

        RequestBannerBean requestBean = new RequestBannerBean();
        requestBean.setType(C.UIShowType.commercial);
            RxHttp.getInstance().getSysteService()
                .getBanner(requestBean)
                .compose(RxUtils.<BaseResponse<List<ImageInfo>>>switchSchedulers())
                .compose(rxFragment.<BaseResponse<List<ImageInfo>>>bindToLifecycle())
                .subscribe(new DataObserver<List<ImageInfo>>() {
                    @Override
                    protected void onSuccess(List<ImageInfo> data) {
                        getIView().onBannerSuccessful(data);
                    }
                });


//        mModel.getCommercialBanner(rxFragment)
//                .subscribe(new DataObserver<List<ImageInfo>>() {
//                    @Override
//                    protected void onSuccess(List<ImageInfo> data) {
//                        getIView().onBannerSuccessful(data);
//                    }
//                });
    }

    @Override
    public void getStudyRank(RxFragment rxFragment) {
        mModel.getStudyRank(rxFragment)
                .subscribe(new DataObserver<List<StudyRank>>() {
                    @Override
                    protected void onDataListEmpty() {
                        getIView().onStudyRankEmpty();
                    }



                    @Override
                    protected void onSuccess(List<StudyRank> data) {
                        getIView().onStudyRankSuccessful(data);
                    }
                });
    }

    @Override
    public void getPageAggregationList(RxFragment rxFragment, RequestListBody page) {
        mModel.getPageAggregationList(rxFragment, page)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().onArticleFinally();
                    }
                })
                .subscribe(new DataObserver<List<CollegeHome>>(false) {
                    @Override
                    protected void onDataListEmpty() {
                        getIView().onArticleEmpty();
                    }


                    @Override
                    protected void onSuccess(List<CollegeHome> data) {
                        getIView().onArticleSuccessful(data);
                    }
                });
    }



    @Override
    public void getCarousel(RxFragment rxFragment) {
               mModel.getCarousel(rxFragment)
                       .subscribe(new DataObserver<List<MiyuanInformation>>() {
                           @Override
                           protected void onSuccess(List<MiyuanInformation> data) {
                               getIView().onCarouselSuc(data);
                           }
                       });
    }



}
