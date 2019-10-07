package com.jf.my.main.contract;

import com.jf.my.main.ui.CollectFragment2;
import com.jf.my.mvp.base.base.BasePresenter;
import com.jf.my.mvp.base.base.BaseRcView;
import com.jf.my.pojo.ShopGoodInfo;
import com.jf.my.network.CommonEmpty;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

/**
 * Created by YangBoTian on 2018/8/8.
 */

public class CollectContract {
    public interface View extends BaseRcView {

        /**
         * 收藏 为空
         */
        void showCollectFailure(String errorMsg, String errorCode);
        void showCollectSuccess(List<ShopGoodInfo> data);
        void showDeleteSuccess();
    }

    public interface Present extends BasePresenter {
        void getCollectData(CollectFragment2 fragment, int page, CommonEmpty emptyView);
        void getDeleteCollection(RxFragment fragment, String ids);
    }
}
