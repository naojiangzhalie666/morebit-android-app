package com.zjzy.morebit.purchase.control;

import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.mvp.base.base.BasePresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;

public class PurchaseControl {
    public interface View extends BaseView {

        /**
         * 请求成功

         */
        void onSuccess();

        /**
         * 请求失败
         */
        void onError();
    }
    public interface Present extends BasePresenter {

        /**
         * 免单
         */
        void freesheet(BaseActivity activity);



    }
}
