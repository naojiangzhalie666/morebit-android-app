package com.zjzy.morebit.goods.shopping.model;


import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.mvp.base.frame.MvpModel;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;

import com.zjzy.morebit.pojo.number.NumberGoods;
import com.zjzy.morebit.pojo.requestbodybean.RequestPage;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by haiping.liu on 2019-12-11.
 */
public class NumberGoodsModel extends MvpModel {
    /**
     * 会员商品列表
     *
     * @param fragment
     * @return
     */
    public Observable<BaseResponse<List<NumberGoods>>> getNumberGoodsList(RxFragment fragment, int page) {

        return RxHttp.getInstance().getGoodsService().getNumberGoodsList(new RequestPage().setPage(page))
                .compose(RxUtils.<BaseResponse<List<NumberGoods>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<NumberGoods>>>bindToLifecycle());
    }
}
