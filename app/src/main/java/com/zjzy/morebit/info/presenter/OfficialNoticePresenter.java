package com.zjzy.morebit.info.presenter;


import com.zjzy.morebit.info.contract.OfficialNoticeContract;
import com.zjzy.morebit.info.model.InfoModel;
import com.zjzy.morebit.mvp.base.frame.MvpPresenter;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ProtocolRuleBean;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LogUtils;
import com.zjzy.morebit.utils.LoginUtil;
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
