package com.markermall.cat.circle.presenter;

import com.markermall.cat.circle.contract.CommercialCollegeContract;
import com.markermall.cat.circle.model.CircleModel;
import com.markermall.cat.mvp.base.frame.MvpPresenter;
import com.markermall.cat.network.BaseResponse;
import com.markermall.cat.network.RxHttp;
import com.markermall.cat.network.RxUtils;
import com.markermall.cat.network.observer.DataObserver;
import com.markermall.cat.pojo.CollegeHome;
import com.markermall.cat.pojo.ImageInfo;
import com.markermall.cat.pojo.MarkermallInformation;
import com.markermall.cat.pojo.StudyRank;
import com.markermall.cat.pojo.request.RequestBannerBean;
import com.markermall.cat.pojo.request.RequestListBody;
import com.markermall.cat.utils.C;
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
                       .subscribe(new DataObserver<List<MarkermallInformation>>() {
                           @Override
                           protected void onSuccess(List<MarkermallInformation> data) {
                               getIView().onCarouselSuc(data);
                           }
                       });
    }



}
