package com.zjzy.morebit.goods.shopping.contract;

import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.mvp.base.base.BasePresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.pojo.BrandSell;
import com.zjzy.morebit.pojo.myInfo.UpdateInfoBean;
import com.zjzy.morebit.pojo.number.NumberGoods;
import com.zjzy.morebit.pojo.number.NumberGoodsList;

import java.util.List;

/**
 * 会员商品
 * Created by haiping.liu on 2019-12-11.
 */
public class NumberGoodsListContract {

    public interface View extends BaseView {

        void showSuccessful(NumberGoodsList goodsList);

        void showError();


        void showFinally();

        /**
         * 用户升级
         * @param info
         */
        void onGradeSuccess(UpdateInfoBean info);
    }

    public interface Present extends BasePresenter {
        /**
         * 获取商品列表
         * @param fragment
         * @param page
         */
        void getNumberGoodsList(RxFragment fragment, int page);

        /**
         * 用户升级
         * @param fragment
         */
        void updateGrade(RxFragment fragment,int userType);
    }
}
