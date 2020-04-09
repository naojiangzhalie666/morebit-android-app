package com.zjzy.morebit.purchase.model;

import com.trello.rxlifecycle2.components.RxActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.mvp.base.frame.MvpModel;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.CommonEmpty;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.VipUseInfoBean;
import com.zjzy.morebit.pojo.number.NumberGoodsList;
import com.zjzy.morebit.pojo.request.RequestByGoodList;
import com.zjzy.morebit.pojo.requestbodybean.RequestNumberGoodsList;
import com.zjzy.morebit.pojo.requestbodybean.RequestPage;

import java.util.List;

import io.reactivex.Observable;

public class PurchaseaModel extends MvpModel {

    /*public Observable<BaseResponse<VipUseInfoBean>> userInfo(BaseActivity rxActivity){
        return  RxHttp.getInstance().getSysteService()
                .checkPruchase()
                .compose(RxUtils.<BaseResponse<VipUseInfoBean>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<VipUseInfoBean>>bindToLifecycle());
    }*/

    /**
     * 获取0元购商品
     *
     * @return
     */
    public Observable<BaseResponse<List<ShopGoodInfo>>> getPurchaseGoods(BaseActivity rxActivity, int page) {
        RequestPage requestBean = new RequestPage();
        requestBean.setPage(page);

        return RxHttp.getInstance().getGoodsService()
                .getPurchaseGoods(requestBean)
                .compose(RxUtils.<BaseResponse<List<ShopGoodInfo>>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<List<ShopGoodInfo>>>bindToLifecycle());
    }

    /**
     * 获取0元购好货商品
     *
     * @return
     */
    public Observable<BaseResponse<List<ShopGoodInfo>>> getProductGoods(BaseActivity rxActivity) {

        return RxHttp.getInstance().getGoodsService()
                .getProductGoods()
                .compose(RxUtils.<BaseResponse<List<ShopGoodInfo>>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<List<ShopGoodInfo>>>bindToLifecycle());
    }

}
