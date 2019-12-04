package com.zjzy.morebit.circle.contract;

import com.zjzy.morebit.pojo.CollegeHome;
import com.zjzy.morebit.pojo.MarkermallInformation;
import com.zjzy.morebit.pojo.StudyRank;
import com.zjzy.morebit.mvp.base.base.BasePresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.pojo.request.RequestListBody;
import com.zjzy.morebit.pojo.ImageInfo;
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
