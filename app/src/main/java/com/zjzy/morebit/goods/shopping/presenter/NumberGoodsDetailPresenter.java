package com.zjzy.morebit.goods.shopping.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.goods.shopping.contract.GoodsDetailContract;
import com.zjzy.morebit.goods.shopping.contract.NumberGoodsDetailContract;
import com.zjzy.morebit.goods.shopping.model.GoodsDetailModel;
import com.zjzy.morebit.goods.shopping.model.NumberGoodsDetailModel;
import com.zjzy.morebit.goods.shopping.ui.fragment.BrandListFragment;
import com.zjzy.morebit.main.model.ConfigModel;
import com.zjzy.morebit.main.model.MainModel;
import com.zjzy.morebit.main.ui.fragment.GoodNewsFramgent;
import com.zjzy.morebit.mvp.base.frame.MvpPresenter;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.CallBackObserver;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.GoodsBrowsHistory;
import com.zjzy.morebit.pojo.HotKeywords;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.ReleaseGoodsPermission;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.goods.TaobaoGoodBean;
import com.zjzy.morebit.pojo.number.NumberGoods;
import com.zjzy.morebit.pojo.number.NumberGoodsInfo;
import com.zjzy.morebit.pojo.request.RequestReleaseGoods;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.MyGsonUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.TaobaoUtil;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.dao.DBManager;

import java.util.List;

import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

/**
 * Created by fengrs on 2018/11/3.
 */

public class NumberGoodsDetailPresenter extends MvpPresenter<NumberGoodsDetailModel, NumberGoodsDetailContract.View> implements NumberGoodsDetailContract.Present {

    /**
     * 获取货物详情信息
     * @param rxActivity
     */

    @Override
    public void getGoodsDetail(BaseActivity rxActivity,String goodsId) {
        mModel.getNumberGoodsDetail(rxActivity,goodsId)
                .subscribe(new DataObserver<NumberGoodsInfo>() {

                    @Override
                    protected void onSuccess(NumberGoodsInfo data) {
                        getIView().showSuccessful(data);
                    }
                });
    }
}
