package com.jf.my.circle.contract;

import com.jf.my.Module.common.Activity.BaseActivity;
import com.jf.my.mvp.base.base.BasePresenter;
import com.jf.my.mvp.base.base.BaseView;
import com.jf.my.network.CommonEmpty;
import com.jf.my.pojo.Article;
import com.jf.my.pojo.request.RequestListBody;
import com.jf.my.pojo.requestbodybean.RequestModelId;
import com.jf.my.pojo.requestbodybean.RequestModelIdData;
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
