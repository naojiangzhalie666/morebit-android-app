package com.zjzy.morebit.circle.presenter;

import com.zjzy.morebit.circle.contract.TutorialContract;
import com.zjzy.morebit.circle.model.TutorialModel;
import com.zjzy.morebit.circle.ui.TutorialFragment;
import com.zjzy.morebit.mvp.base.frame.MvpPresenter;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.Article;
import com.zjzy.morebit.pojo.requestbodybean.RequestTwoLevel;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

import io.reactivex.functions.Action;

/**
 * Created by JerryHo on 2019/3/15
 * Description:
 */
public class TutorialPresenter extends MvpPresenter<TutorialModel, TutorialContract.View> implements TutorialContract.Present {

    /**
     * 获取数据 设置数据
     *
     * @param rxFragment
     */
    @Override
    public void getTutorialData(RxFragment rxFragment, RequestTwoLevel requestTwoLevel, int type) {
        if (type == TutorialFragment.ALL) {
            mModel.getArticleList(rxFragment, requestTwoLevel)
                    .subscribe(new DataObserver<List<Article>>() {

                        @Override
                        protected void onSuccess(List<Article> data) {
                            getIView().onTutorialDataSuccessful(data);
                        }

                        @Override
                        protected void onDataListEmpty() {
                            super.onDataListEmpty();
                            getIView().onTutorialDataEmpty();
                        }

                        @Override
                        protected void onError(String errorMsg, String errCode) {
                            super.onError(errorMsg, errCode);
                            getIView().onTutorialDataEmpty();
                        }
                    });
        } else {
            mModel.getTutorialData(rxFragment, requestTwoLevel).doFinally(new Action() {
                @Override
                public void run() throws Exception {
                    getIView().onTutorialFinally();
                }
            }).subscribe(new DataObserver<List<Article>>() {

                @Override
                protected void onSuccess(List<Article> data) {
                    getIView().onTutorialDataSuccessful(data);
                }

                @Override
                protected void onDataListEmpty() {
                    super.onDataListEmpty();
                    getIView().onTutorialDataEmpty();
                }

                @Override
                protected void onError(String errorMsg, String errCode) {
                    super.onError(errorMsg, errCode);
                    getIView().onTutorialDataEmpty();
                }
            });
        }

    }


}
