package com.jf.my.info.presenter;


import com.jf.my.LocalData.UserLocalData;
import com.jf.my.info.contract.MakeMoenyContract;
import com.jf.my.info.model.InfoModel;
import com.jf.my.info.ui.MakeMoneyFragment;
import com.jf.my.mvp.base.frame.MvpPresenter;
import com.jf.my.network.observer.DataObserver;
import com.jf.my.pojo.ProtocolRuleBean;
import com.jf.my.pojo.myInfo.ApplyUpgradeBean;
import com.jf.my.pojo.myInfo.MakeMoenyBean;
import com.jf.my.utils.C;
import com.jf.my.utils.LoginUtil;
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
