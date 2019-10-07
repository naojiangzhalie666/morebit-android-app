package com.markermall.cat.circle.contract;

import com.markermall.cat.pojo.Article;
import com.markermall.cat.pojo.ArticleBody;
import com.markermall.cat.pojo.SearchArticleBody;
import com.markermall.cat.mvp.base.base.BasePresenter;
import com.markermall.cat.mvp.base.base.BaseView;
import com.markermall.cat.network.CommonEmpty;
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
