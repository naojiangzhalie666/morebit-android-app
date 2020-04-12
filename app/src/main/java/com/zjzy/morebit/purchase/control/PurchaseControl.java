package com.zjzy.morebit.purchase.control;

import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.mvp.base.base.BasePresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.pojo.ShopGoodInfo;

import java.util.List;

public class PurchaseControl {
     public interface View extends BaseView {

        /**
         * 请求成功

         */
        void onSuccess(List<ShopGoodInfo> shopGoodInfo);

        /**
         * 请求失败
         */
        void onError(String throwable);



        void onProductSuccess(List<ShopGoodInfo> shopGoodInfo);
         void onProductError(String throwable);


    }
    public interface Present extends BasePresenter {

        /**
         * 免单商品
         */
        void getPurchase(BaseActivity activity,int page);
        /**
         * 免单好货商品
         */
        void getProduct(BaseActivity activity);

    }
}
