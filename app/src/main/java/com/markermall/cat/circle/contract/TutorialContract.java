package com.markermall.cat.circle.contract;

import com.markermall.cat.mvp.base.base.BasePresenter;
import com.markermall.cat.mvp.base.base.BaseView;
import com.markermall.cat.pojo.Article;
import com.markermall.cat.pojo.requestbodybean.RequestTwoLevel;
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
