package com.zjzy.morebit.circle.contract;

import com.zjzy.morebit.mvp.base.base.BasePresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.pojo.Article;
import com.zjzy.morebit.pojo.requestbodybean.RequestTwoLevel;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

/**
 * Created by JerryHo on 2019/3/15
 * Description:
 */
public class TutorialContract {
    public interface View extends BaseView {
        void onTutorialDataSuccessful(List<Article> data);

        void onTutorialDataEmpty();

        void onTutorialFinally();

    }

    public interface Present extends BasePresenter {

        void getTutorialData(RxFragment rxFragment, RequestTwoLevel requestTwoLevel,int type);


    }
}
