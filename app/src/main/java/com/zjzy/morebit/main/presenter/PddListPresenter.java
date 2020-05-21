package com.zjzy.morebit.main.presenter;

import android.text.TextUtils;

import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.main.contract.PddContract;
import com.zjzy.morebit.main.contract.RankingContract;
import com.zjzy.morebit.main.model.MainModel;
import com.zjzy.morebit.main.ui.fragment.ShoppingListFragment2;
import com.zjzy.morebit.mvp.base.frame.MvpPresenter;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ProgramCatItemBean;
import com.zjzy.morebit.pojo.RankingTitleBean;
import com.zjzy.morebit.pojo.ShopGoodBean;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UI.BaseTitleTabBean;
import com.zjzy.morebit.pojo.pddjd.JdPddProgramItem;
import com.zjzy.morebit.pojo.request.RequestSearchBean;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.fire.DeviceIDUtils;

import org.jetbrains.annotations.Contract;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * Created by fengrs
 * Data: 2018/8/3.
 */
public class PddListPresenter extends MvpPresenter<MainModel, PddContract.View> implements PddContract.Present {
    int page = 1;
    // 三级分类用地  全网搜索
    private int mSearchType = 1;
    private String mMinId = "1";
    private String mPageString = "1";
    // 是否加载猜你喜欢数据
    private boolean mIsLoadWhatLike;
    private String mDeviceImei;
    // 三级分类用地  全网搜索



    private void sendObservable(final int loadType, Observable<BaseResponse<List<ShopGoodInfo>>> observable) {
        if (observable == null) {
            getIView().showFinally();
            return;
        }
        observable.doFinally(new Action() {
            @Override
            public void run() throws Exception {
                getIView().showFinally();
            }
        }).subscribe(new DataObserver<List<ShopGoodInfo>>() {

            @Override
            protected void onDataListEmpty() {
                getIView().setPddError(loadType);
            }

            @Override
            protected void onSuccess(List<ShopGoodInfo> data) {
                getIView().setPdd(data, loadType);
                page++;
            }
        });
    }

    @Override
    public void getJdPddGoodsList(RxFragment rxFragmen, ProgramCatItemBean programCatItemBean, int loadType) {
        if (loadType == C.requestType.initData) {
            page = 1;
        }

        Observable<BaseResponse<List<ShopGoodInfo>>> observable = null;
        programCatItemBean.setType(2);
        programCatItemBean.setPage(page);
        observable = mModel.getJdPddGoodsList(rxFragmen, programCatItemBean);
        sendObservable(loadType, observable);
    }

    @Override
    public void getJdGoodsList(RxFragment rxFragmen, ProgramCatItemBean programCatItemBean, int loadType) {

        Observable<BaseResponse<List<ShopGoodInfo>>> observable = null;
        programCatItemBean.setType(1);
        observable = mModel.getJdGoodsList(rxFragmen, programCatItemBean);
        getObservable(loadType, observable);
    }

    private void getObservable(final int loadType, Observable<BaseResponse<List<ShopGoodInfo>>> observable) {
        if (observable == null) {
            getIView().showFinally();
            return;
        }
        observable.doFinally(new Action() {
            @Override
            public void run() throws Exception {
                getIView().showFinally();
            }
        }).subscribe(new DataObserver<List<ShopGoodInfo>>() {

            @Override
            protected void onDataListEmpty() {
                getIView().setJdError(loadType);
            }

            @Override
            protected void onSuccess(List<ShopGoodInfo> data) {
                getIView().setJd(data, loadType);
            }
        });
    }


}
