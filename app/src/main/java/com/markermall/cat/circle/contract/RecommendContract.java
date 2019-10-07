package com.markermall.cat.circle.contract;

import com.markermall.cat.mvp.base.base.BasePresenter;
import com.markermall.cat.mvp.base.base.BaseView;
import com.markermall.cat.network.CommonEmpty;
import com.markermall.cat.pojo.Article;
import com.markermall.cat.pojo.request.RequestListBody;
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
