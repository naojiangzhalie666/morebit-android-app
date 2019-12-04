package com.zjzy.morebit.info.presenter;


import com.zjzy.morebit.info.contract.MakeMoenyContract;
import com.zjzy.morebit.info.model.InfoModel;
import com.zjzy.morebit.info.ui.MakeMoneyFragment;
import com.zjzy.morebit.mvp.base.frame.MvpPresenter;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ProtocolRuleBean;
import com.zjzy.morebit.pojo.myInfo.ApplyUpgradeBean;
import com.zjzy.morebit.pojo.myInfo.MakeMoenyBean;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoginUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

import io.reactivex.functions.Action;

/**
 * Created by fengrs
 * Data: 2018/9/7.
 */
public class MakeMoenyPresenter extends MvpPresenter<InfoModel, MakeMoenyContract.View> implements MakeMoenyContract.Present {

    /**
     * 申请升级
     *
     * @param rxFragment
     */
    @Override
    public void applyUpgradeData(RxFragment rxFragment) {

        mModel.applyUpgradeData(rxFragment)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().showFinally();
                    }
                }).subscribe(new DataObserver<ApplyUpgradeBean>() {
            @Override
            protected void onSuccess(ApplyUpgradeBean data) {
                getIView().showApplyUpgradeDialog(data);
            }
        });
    }

    /**
     * 确认升级
     *
     * @param rxFragment
     */
    @Override
    public void confirmUpgrade(MakeMoneyFragment rxFragment) {

        mModel.confirmUpgradeData(rxFragment)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().showFinally();
                    }
                }).subscribe(new DataObserver<String>() {
            @Override
            protected void onDataNull() {
                onSuccess("");
            }

            @Override
            protected void onSuccess(String data) {
                getIView().confirmUpgradeSuccess(data);
            }
        });
    }

    /**
     * 赚钱三部曲
     *
     * @param rxFragment
     */
    @Override
    public void getMakeMoneyRuleData(MakeMoneyFragment rxFragment) {
        mModel.getMakeMoneyRule(rxFragment )
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().showFinally();
                    }
                }).subscribe(new DataObserver<MakeMoenyBean>() {
            @Override
            protected void onSuccess(MakeMoenyBean data) {
                getIView().setMakeMoneyRuleData(data);
            }
        });
    }

    /**
     * 常见问题
     *
     * @param rxFragment
     */
    @Override
    public void getCommonProblemData(MakeMoneyFragment rxFragment) {
        LoginUtil.getSystemStaticPage((RxAppCompatActivity) rxFragment.getActivity(), C.ProtocolType.commonProblem)
                .subscribe(new DataObserver<List<ProtocolRuleBean>>() {
                    @Override
                    protected void onSuccess(List<ProtocolRuleBean> data) {
                        getIView().setCommonProblemData(data);
                    }
                });

    }
}
