package com.markermall.cat.info.contract;

import com.markermall.cat.mvp.base.base.BasePresenter;
import com.markermall.cat.mvp.base.base.BaseView;
import com.markermall.cat.pojo.ImageInfo;
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
