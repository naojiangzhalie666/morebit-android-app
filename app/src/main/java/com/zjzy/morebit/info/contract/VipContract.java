package com.zjzy.morebit.info.contract;

import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.mvp.base.base.BasePresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.pojo.HotKeywords;
import com.zjzy.morebit.pojo.TeamInfo;
import com.zjzy.morebit.pojo.UpgradeInstructions;
import com.zjzy.morebit.pojo.VipUseInfoBean;
import com.zjzy.morebit.pojo.request.ClassroomBean;

import java.util.List;

public class VipContract {
    public interface View extends BaseView {
        void onSuccess(VipUseInfoBean vipUseInfoBean);
        void serviceSuccess(TeamInfo data);
        void upgradeVipSuccess(String data);
        void upgradeVipFailure();
        void upgradeInstructionsSuccess(String data);
        void upgradeInstructionsSuccess2(UpgradeInstructions data);
        void forKeySuccess(HotKeywords data);
        void doFinally();
        void classroomSuccess(List<ClassroomBean> datas);
    }

    public interface Present extends BasePresenter {
        void userInfo(BaseActivity rxActivity);
        void upgradeVip(BaseActivity rxActivity);
        void getServiceQrcode(BaseActivity rxActivity);
        void upgradeInstructions(BaseActivity rxActivity, String type);
        void upgradeInstructions2(BaseActivity rxActivity, int type);
        void getConfigForKey(BaseActivity rxActivity);
        void myCurriculum(BaseActivity rxActivity);
        void mp4Browse(BaseActivity rxActivity, int id);
    }
}
