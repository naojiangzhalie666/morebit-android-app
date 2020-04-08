package com.zjzy.morebit.purchase.model;

import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.mvp.base.frame.MvpModel;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.pojo.VipUseInfoBean;

import io.reactivex.Observable;

public class PurchaseaModel extends MvpModel {

    /*public Observable<BaseResponse<VipUseInfoBean>> userInfo(BaseActivity rxActivity){
        return  RxHttp.getInstance().getSysteService()
                .checkPruchase()
                .compose(RxUtils.<BaseResponse<VipUseInfoBean>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<VipUseInfoBean>>bindToLifecycle());
    }*/
}
