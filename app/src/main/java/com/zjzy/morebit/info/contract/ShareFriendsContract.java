package com.zjzy.morebit.info.contract;

import com.zjzy.morebit.mvp.base.base.BasePresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.pojo.ImageInfo;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

/**
 * Created by YangBoTian on 2018/9/12.
 */

public class ShareFriendsContract {
    public interface View extends BaseView {
        void onSuccessful(List<ImageInfo> datas);
    }

    public interface Present extends BasePresenter {
          void getBanner(RxFragment fragment, int back);
    }
}
