package com.markermall.cat.circle.contract;

import com.markermall.cat.pojo.CollegeHome;
import com.markermall.cat.pojo.MarkermallInformation;
import com.markermall.cat.pojo.StudyRank;
import com.markermall.cat.mvp.base.base.BasePresenter;
import com.markermall.cat.mvp.base.base.BaseView;
import com.markermall.cat.pojo.request.RequestListBody;
import com.markermall.cat.pojo.ImageInfo;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

/**
 * Created by YangBoTian on 2018/9/12.
 */

public class CommercialCollegeContract {
    public interface View extends BaseView {
        void onBannerSuccessful(List<ImageInfo> data);
        void onStudyRankSuccessful(List<StudyRank> data);
        void onStudyRankEmpty();
        void onArticleSuccessful(List<CollegeHome> data);
        void onArticleEmpty();
        void onArticleFinally();
        void onCarouselSuc(List<MarkermallInformation> data);
    }

    public interface Present extends BasePresenter {
        void getCommercialBanner(RxFragment rxFragment);
        void getStudyRank(RxFragment rxFragment);
        void getPageAggregationList(RxFragment rxFragment,RequestListBody page);
        void getCarousel(RxFragment rxFragment);
    }
}
