package com.jf.my.circle.contract;

import com.jf.my.pojo.Article;
import com.jf.my.pojo.ArticleBody;
import com.jf.my.pojo.SearchArticleBody;
import com.jf.my.mvp.base.base.BasePresenter;
import com.jf.my.mvp.base.base.BaseView;
import com.jf.my.network.CommonEmpty;
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
