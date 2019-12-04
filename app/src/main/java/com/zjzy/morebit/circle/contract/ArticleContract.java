package com.zjzy.morebit.circle.contract;

import com.zjzy.morebit.pojo.Article;
import com.zjzy.morebit.pojo.ArticleBody;
import com.zjzy.morebit.pojo.SearchArticleBody;
import com.zjzy.morebit.mvp.base.base.BasePresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.network.CommonEmpty;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

/**
 * Created by YangBoTian on 2018/9/12.
 */

public class ArticleContract {
    public interface View extends BaseView {
        void onArticleSuccessful(List<Article> data);
        void onArticleEmpty();
        void onArticleFinally();
    }

    public interface Present extends BasePresenter {
        void getArticleList(RxFragment rxFragment, ArticleBody body, CommonEmpty emptyView);
        void searchArticleList(RxFragment rxFragment, SearchArticleBody body, CommonEmpty emptyView);
    }
}
