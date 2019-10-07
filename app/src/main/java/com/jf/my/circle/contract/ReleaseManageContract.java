package com.jf.my.circle.contract;

import com.jf.my.mvp.base.base.BasePresenter;
import com.jf.my.mvp.base.base.BaseView;
import com.jf.my.network.CommonEmpty;
import com.jf.my.pojo.Article;
import com.jf.my.pojo.ArticleBody;
import com.jf.my.pojo.ReleaseManage;
import com.jf.my.pojo.SearchArticleBody;
import com.jf.my.pojo.request.RequestReleaseGoodsDelete;
import com.jf.my.pojo.request.RequestReleaseManage;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

/**
 * Created by YangBoTian on 2018/9/12.
 */

public class ReleaseManageContract {
    public interface View extends BaseView {
        void onSuccessful(List<ReleaseManage> data);
        void onEmpty();
        void onFinally();
        void onNull(int position);
        void onGoodsDeleteSuc(String data,int position);
    }

    public interface Present extends BasePresenter {
        void getReleaseManageList(RxFragment rxFragment, RequestReleaseManage body, CommonEmpty emptyView);
        void getReleaseGoodsDelete(RxFragment rxFragment, RequestReleaseGoodsDelete body,int position);
    }
}
