package com.zjzy.morebit.main.contract;

import com.zjzy.morebit.main.ui.CollectFragment2;
import com.zjzy.morebit.mvp.base.base.BasePresenter;
import com.zjzy.morebit.mvp.base.base.BaseRcView;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.network.CommonEmpty;
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
        void showEmity();
    }

    public interface Present extends BasePresenter {
        void getCollectData(CollectFragment2 fragment, int page);
        void getDeleteCollection(RxFragment fragment, String ids);
    }
}
