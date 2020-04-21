package com.zjzy.morebit.goods.shopping.model;

import com.zjzy.morebit.mvp.base.frame.MvpModel;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.RxWXHttp;
import com.zjzy.morebit.pojo.BrandSell;
import com.zjzy.morebit.pojo.request.RequestMtopJsonpBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestPage;
import com.zjzy.morebit.utils.C;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * Created by fengrs
 * Data: 2018/8/15.
 * 返现 model
 */
public class ReturnCashModel extends MvpModel {



    /**
     * 获取品牌列表
     *
     * @param fragment
     * @return
     */
    public Observable<BaseResponse<List<BrandSell>>> getBrandSellList(RxFragment fragment, int page) {
        return RxHttp.getInstance().getGoodsService().getBrandsList( new RequestPage().setPage(1))
                .compose(RxUtils.<BaseResponse<List<BrandSell>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<BrandSell>>>bindToLifecycle());

    }

    /**
     * 品牌推荐
     *
     * @param fragment
     * @return
     */
    public Observable<BaseResponse<List<BrandSell>>> getBrandSellGoodsList(RxFragment fragment, int page) {

        return RxHttp.getInstance().getGoodsService().getBrandSellGoodsList(new RequestPage().setPage(page))
                .compose(RxUtils.<BaseResponse<List<BrandSell>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<BrandSell>>>bindToLifecycle());
    }

    public Observable<String> getprofileUrlObservable(RxFragment fragment, String url) {
        return RxWXHttp.getInstance().getService(C.getInstance().getGoodsIp()).profilePicture(url)
                .compose(fragment.<String>bindToLifecycle());
    }


    public Observable<BaseResponse<List<String>>> getMtopjsonpObservable(RxFragment fragment, String mtopjsonp) {
        RequestMtopJsonpBean bean = new RequestMtopJsonpBean();
        bean.setMtopjsonp(mtopjsonp);
        return RxHttp.getInstance().getGoodsService().getMtopjsonp(bean )
                .compose(fragment.<BaseResponse<List<String>>>bindToLifecycle());
    }

    public Observable<BaseResponse<List<String>>> getItemDetailPictureCode(RxFragment fragment, String data) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), data );
        return RxHttp.getInstance().getGoodsService().getItemDetailPictureCode(body)
                .compose(fragment.<BaseResponse<List<String>>>bindToLifecycle());
    }

}