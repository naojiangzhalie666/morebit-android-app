package com.markermall.cat.info.contract;

import com.markermall.cat.Module.common.Activity.BaseActivity;
import com.markermall.cat.mvp.base.base.BasePresenter;
import com.markermall.cat.mvp.base.base.BaseView;
import com.markermall.cat.pojo.HotKeywords;
import com.markermall.cat.pojo.TeamInfo;
import com.markermall.cat.pojo.UpgradeInstructions;
import com.markermall.cat.pojo.VipUseInfoBean;
import com.markermall.cat.pojo.request.ClassroomBean;

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
