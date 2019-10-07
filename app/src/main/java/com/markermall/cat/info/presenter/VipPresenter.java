package com.markermall.cat.info.presenter;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.markermall.cat.Module.common.Activity.BaseActivity;
import com.markermall.cat.Module.common.Utils.LoadingView;
import com.markermall.cat.info.contract.VipContract;
import com.markermall.cat.info.model.VipModel;
import com.markermall.cat.mvp.base.frame.MvpPresenter;
import com.markermall.cat.network.observer.DataObserver;
import com.markermall.cat.pojo.HotKeywords;
import com.markermall.cat.pojo.TeamInfo;
import com.markermall.cat.pojo.UpgradeInstructions;
import com.markermall.cat.pojo.VipUseInfoBean;
import com.markermall.cat.pojo.request.ClassroomBean;
import com.markermall.cat.utils.C;

import java.util.List;

import io.reactivex.functions.Action;

/**
 * Created by liys on 2019/1/11
 */
public class VipPresenter extends MvpPresenter<VipModel, VipContract.View> implements VipContract.Present {

    @Override
    public void userInfo(BaseActivity rxActivity) {
        mModel.userInfo(rxActivity)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().doFinally();
                    }
                })
                .subscribe(new DataObserver<VipUseInfoBean>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        getIView().upgradeVipFailure();
                    }

                    @SuppressLint("CheckResult")
                    @Override
                    protected void onSuccess(VipUseInfoBean data) {
                        getIView().onSuccess(data);
                    }
                });
    }


    @Override
    public void getServiceQrcode(BaseActivity rxActivity) {
        mModel.getServiceQrcode(rxActivity)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().doFinally();
                    }
                })
                .subscribe(new DataObserver<TeamInfo>() {
                    @SuppressLint("CheckResult")
                    @Override
                    protected void onSuccess(TeamInfo data) {
                        getIView().serviceSuccess(data);
                    }
                });
    }

    @Override
    public void upgradeVip(final BaseActivity rxActivity) {
        mModel.upgradeVip(rxActivity)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().doFinally();
                    }
                })
                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        if (C.requestCode.B10310.equals(errCode)) {
                            upgradeInstructions2(rxActivity, 3, errorMsg);
                        }

                    }

                    @Override
                    protected void onSuccess(String data) {
                        ToastUtils.showShort(data);
                    }
                });
    }

    @Override
    public void upgradeInstructions(BaseActivity rxActivity, String type) {
//        LoadingView.showDialog(rxActivity);
        mModel.upgradeInstructions(rxActivity, type)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().doFinally();
                    }
                })
                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onSuccess(String data) {
                        getIView().upgradeInstructionsSuccess(data);
                    }
                });
    }

    @Override
    public void upgradeInstructions2(BaseActivity rxActivity, int type) {
        upgradeInstructions2(rxActivity, type, "");
    }

    public void upgradeInstructions2(BaseActivity rxActivity, int type, final String errorMsg) {
        LoadingView.showDialog(rxActivity);
        mModel.upgradeInstructions2(rxActivity, type)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().doFinally();
                    }
                })
                .subscribe(new DataObserver<UpgradeInstructions>() {

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        if (C.requestCode.dataListEmpty.equals(errCode)) {
                            ToastUtils.showShort("数据为空, 请重试");
                        }
                    }

                    @Override
                    protected void onSuccess(UpgradeInstructions data) {
                        if (!TextUtils.isEmpty(errorMsg)) {
                            data.setDesc(errorMsg);
                        }
                        getIView().upgradeInstructionsSuccess2(data);
                    }
                });
    }

    @Override
    public void getConfigForKey(BaseActivity rxActivity) {
        mModel.getConfigForKey(rxActivity)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().doFinally();
                    }
                })
                .subscribe(new DataObserver<HotKeywords>() {
                    @Override
                    protected void onSuccess(HotKeywords data) {
                        getIView().forKeySuccess(data);
                    }
                });
    }

    @Override
    public void myCurriculum(BaseActivity rxActivity) {
        mModel.myCurriculum(rxActivity)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().doFinally();
                    }
                })
                .subscribe(new DataObserver<List<ClassroomBean>>() {
                    @Override
                    protected void onSuccess(List<ClassroomBean> datas) {
                        getIView().classroomSuccess(datas);
                    }
                });
    }

    @Override
    public void mp4Browse(BaseActivity rxActivity, int id) {
        mModel.mp4Browse(rxActivity, id)
                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onSuccess(String data) {
                    }
                });
    }
}
