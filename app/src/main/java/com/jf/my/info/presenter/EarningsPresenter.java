package com.jf.my.info.presenter;

import com.jf.my.Module.common.Activity.BaseActivity;
import com.jf.my.info.contract.EarningsContract;
import com.jf.my.info.model.InfoModel;
import com.jf.my.mvp.base.frame.MvpPresenter;
import com.jf.my.network.observer.DataObserver;
import com.jf.my.utils.TaobaoUtil;
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
