package com.zjzy.morebit.goods.shopping.model;


import android.text.TextUtils;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.mvp.base.frame.MvpModel;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.RxWXHttp;
import com.zjzy.morebit.pojo.HotKeywords;
import com.zjzy.morebit.pojo.ReleaseGoodsPermission;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.number.NumberGoods;
import com.zjzy.morebit.pojo.number.NumberGoodsInfo;
import com.zjzy.morebit.pojo.request.RequestGoodsCollectBean;
import com.zjzy.morebit.pojo.request.RequestMaterialLink;
import com.zjzy.morebit.pojo.request.RequestNumberGoodsDetailBean;
import com.zjzy.morebit.pojo.request.RequestReleaseGoods;
import com.zjzy.morebit.pojo.requestbodybean.RequestItemSourceId;
import com.zjzy.morebit.pojo.requestbodybean.RequestKeyBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestShopId;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.encrypt.EncryptUtlis;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import okhttp3.RequestBody;

/**
 * Created by fengrs
 * Data: 2018/12/15.
 * 会员商品详情 model
 */
public class NumberGoodsDetailModel extends MvpModel {
    /**
     * 会员商品详情
     * @param rxActivity
     * @return
     */
    public Observable<BaseResponse<NumberGoodsInfo>> getNumberGoodsDetail(BaseActivity rxActivity,String goodsId) {
        RequestNumberGoodsDetailBean requestBean = new RequestNumberGoodsDetailBean();
        requestBean.setGoodsId(goodsId);
        return RxHttp.getInstance().getCommonService().getNumberGoodsDetail(
                requestBean
        )
                .compose(RxUtils.<BaseResponse<NumberGoodsInfo>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<NumberGoodsInfo>>bindToLifecycle());
    }

    public Observable<BaseResponse<HotKeywords>> getConfigForKey(RxAppCompatActivity activity, String key) {

        return RxHttp.getInstance().getCommonService().getConfigForKey(new RequestKeyBean().setKey(key))
                .compose(RxUtils.<BaseResponse<HotKeywords>>switchSchedulers())
                .compose(activity.<BaseResponse<HotKeywords>>bindToLifecycle());

//        return RxHttp.getInstance().getCommonService().getConfigForKey(key)
//                .compose(RxUtils.<BaseResponse<HotKeywords>>switchSchedulers())
//                .compose(activity.<BaseResponse<HotKeywords>>bindToLifecycle());
    }




}