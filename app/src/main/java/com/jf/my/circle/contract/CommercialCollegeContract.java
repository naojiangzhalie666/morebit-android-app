package com.jf.my.circle.contract;

import com.jf.my.pojo.Article;
import com.jf.my.pojo.CollegeHome;
import com.jf.my.pojo.MiyuanInformation;
import com.jf.my.pojo.StudyRank;
import com.jf.my.mvp.base.base.BasePresenter;
import com.jf.my.mvp.base.base.BaseView;
import com.jf.my.pojo.request.RequestListBody;
import com.jf.my.pojo.ImageInfo;
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
        void onCarouselSuc(List<MiyuanInformation> data);
    }

    public interface Present extends BasePresenter {
        void getCommercialBanner(RxFragment rxFragment);
        void getStudyRank(RxFragment rxFragment);
        void getPageAggregationList(RxFragment rxFragment,RequestListBody page);
        void getCarousel(RxFragment rxFragment);
    }
}
