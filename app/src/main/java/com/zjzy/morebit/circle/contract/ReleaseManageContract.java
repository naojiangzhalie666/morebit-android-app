package com.zjzy.morebit.circle.contract;

import com.zjzy.morebit.mvp.base.base.BasePresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.network.CommonEmpty;
import com.zjzy.morebit.pojo.ReleaseManage;
import com.zjzy.morebit.pojo.request.RequestReleaseGoodsDelete;
import com.zjzy.morebit.pojo.request.RequestReleaseManage;
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
