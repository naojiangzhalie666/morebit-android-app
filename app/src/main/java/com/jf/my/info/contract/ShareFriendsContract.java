package com.jf.my.info.contract;

import com.jf.my.mvp.base.base.BasePresenter;
import com.jf.my.mvp.base.base.BaseView;
import com.jf.my.pojo.ImageInfo;
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
