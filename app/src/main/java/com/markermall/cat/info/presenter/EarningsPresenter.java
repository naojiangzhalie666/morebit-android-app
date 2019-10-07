package com.markermall.cat.info.presenter;

import com.markermall.cat.Module.common.Activity.BaseActivity;
import com.markermall.cat.info.contract.EarningsContract;
import com.markermall.cat.info.model.InfoModel;
import com.markermall.cat.mvp.base.frame.MvpPresenter;
import com.markermall.cat.network.observer.DataObserver;
import com.markermall.cat.utils.TaobaoUtil;
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
