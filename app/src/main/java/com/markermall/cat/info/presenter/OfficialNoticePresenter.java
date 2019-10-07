package com.markermall.cat.info.presenter;


import com.markermall.cat.info.contract.OfficialNoticeContract;
import com.markermall.cat.info.model.InfoModel;
import com.markermall.cat.mvp.base.frame.MvpPresenter;
import com.markermall.cat.network.observer.DataObserver;
import com.markermall.cat.pojo.ProtocolRuleBean;
import com.markermall.cat.utils.C;
import com.markermall.cat.utils.LogUtils;
import com.markermall.cat.utils.LoginUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

import io.reactivex.functions.Action;

/**
 * Created by fengrs
 * Data: 2018/8/3.
 */
public class OfficialNoticePresenter extends MvpPresenter<InfoModel, OfficialNoticeContract.View> implements OfficialNoticeContract.Present {
    private int page = 1;

    @Override
    public void getOfficialNotice(RxFragment fragment, final int type) {
        if (type == C.requestType.initData) {
            page = 1;
        }
        LogUtils.Log("OfficialNoticePresenter", " page = " + page);
        LoginUtil.getSystemStaticPage((RxAppCompatActivity) fragment.getActivity(),C.ProtocolType.notice,page+"")
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().showFinally();
                    }
                })
                .subscribe(new DataObserver<List<ProtocolRuleBean>>() {
                    @Override
                    protected void onSuccess(List<ProtocolRuleBean> data) {
                        getIView().showData(data, type);
                        page++;
                    }

                    @Override
                    protected void onDataListEmpty() {
                        getIView().showFailureMessage();
                    }
                });
    }
}
