package com.jf.my.circle.contract;

import com.jf.my.Module.common.Activity.BaseActivity;
import com.jf.my.mvp.base.base.BasePresenter;
import com.jf.my.mvp.base.base.BaseView;
import com.jf.my.pojo.Article;
import com.jf.my.pojo.requestbodybean.RequestTwoLevel;
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
