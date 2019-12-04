package com.zjzy.morebit.info.presenter;

import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.info.contract.EarningsContract;
import com.zjzy.morebit.info.model.InfoModel;
import com.zjzy.morebit.mvp.base.frame.MvpPresenter;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.utils.TaobaoUtil;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * Created by YangBoTian on 2018/9/12.
 */

public class EarningsPresenter extends MvpPresenter<InfoModel, EarningsContract.View> implements EarningsContract.Present {


    @Override
    public void getAllianceAppKey(RxFragment rxFragment) {
        TaobaoUtil.getAllianceAppKey((BaseActivity) rxFragment.getActivity());
    }

    @Override
    public void checkWithdrawTime(RxFragment rxFragment) {
        mModel.checkWithdrawTime(rxFragment)
                .subscribe(new DataObserver<String>(false) {
                    @Override
                    protected void onSuccess(String data) {
                        getIView().checkWithdrawTime();
                    }

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        getIView().checkWithdrawTimeError(errorMsg,errCode);
                    }
                });
    }


}
