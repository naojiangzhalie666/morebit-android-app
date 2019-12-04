package com.zjzy.morebit.circle.contract;

import com.zjzy.morebit.mvp.base.base.BasePresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.network.CommonEmpty;
import com.zjzy.morebit.pojo.Article;
import com.zjzy.morebit.pojo.request.RequestListBody;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

/**
 * Created by JerryHo on 2019/3/16
 * Description: 商学院activity 获取tab等数据
 */
public class RecommendContract {

    public interface View extends BaseView {

        void onListSuccessful(List<Article> data);

        void onListEmpty();

        void onListFinally();
    }

    public interface Present extends BasePresenter {

        void getRecommendMoreList(RxAppCompatActivity baseActivity, RequestListBody body, CommonEmpty emptyView,int type);
    }

}
