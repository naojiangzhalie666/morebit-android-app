package com.zjzy.morebit.goodsvideo.model;




import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.mvp.base.frame.MvpModel;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.pojo.ConsComGoodsInfo;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.VideoClassBean;
import com.zjzy.morebit.pojo.request.RequestCommissionGoodsBean;
import com.zjzy.morebit.pojo.request.RequestGoodsOrderBean;
import com.zjzy.morebit.pojo.request.RequestVideoGoodsBean;

import java.util.List;

import io.reactivex.Observable;

public class VideoModel extends MvpModel {
    /**
     * 获取抖货分类条目
     *
     * @return
     */
    public Observable<BaseResponse<List<VideoClassBean>>> getVideoClass(BaseActivity rxActivity) {

        return RxHttp.getInstance().getGoodsService()
                .getVideoClass()
                .compose(RxUtils.<BaseResponse<List<VideoClassBean>>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<List<VideoClassBean>>>bindToLifecycle());
    }


    /**
     * 获取抖货商品列表
     *
     * @param rxFragment
     *
     */
    public Observable<BaseResponse<List<ShopGoodInfo>>> getVideoGoods(RxFragment rxFragment, String catId, int page) {
        RequestVideoGoodsBean requestBean = new RequestVideoGoodsBean();
        requestBean.setCatId(catId);
        requestBean.setPage(page);

        return RxHttp.getInstance().getUsersService()
                .getVideoGoods(requestBean)
                .compose(RxUtils.<BaseResponse<List<ShopGoodInfo>>>switchSchedulers())
                .compose(rxFragment.<BaseResponse<List<ShopGoodInfo>>>bindToLifecycle());
    }

    /**
     * 获取首页高佣专区商品
     *
     * @return
     */
    public Observable<BaseResponse<List<ShopGoodInfo>>> getCommissionGoods(RxFragment fragment, String catId, int minId) {
        RequestCommissionGoodsBean commissionGoodsBean=new RequestCommissionGoodsBean();
        commissionGoodsBean.setCatId(catId);
        commissionGoodsBean.setMinId(minId);
        return RxHttp.getInstance().getGoodsService()
                .getCommissionGoods(commissionGoodsBean)
                .compose(RxUtils.<BaseResponse<List<ShopGoodInfo>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<ShopGoodInfo>>>bindToLifecycle());
    }
}
