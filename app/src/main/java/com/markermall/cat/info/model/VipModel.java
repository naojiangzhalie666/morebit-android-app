package com.markermall.cat.info.model;

import com.markermall.cat.Module.common.Activity.BaseActivity;
import com.markermall.cat.mvp.base.frame.MvpModel;
import com.markermall.cat.network.BaseResponse;
import com.markermall.cat.network.RxHttp;
import com.markermall.cat.network.RxUtils;
import com.markermall.cat.pojo.HotKeywords;
import com.markermall.cat.pojo.TeamInfo;
import com.markermall.cat.pojo.UpgradeInstructions;
import com.markermall.cat.pojo.VipUseInfoBean;
import com.markermall.cat.pojo.request.ClassroomBean;
import com.markermall.cat.pojo.request.RequestGradeBean;
import com.markermall.cat.pojo.request.RequestGradeUrlBean;
import com.markermall.cat.pojo.request.RequestVideoId;
import com.markermall.cat.pojo.requestbodybean.RequestInviteCodeBean;
import com.markermall.cat.pojo.requestbodybean.RequestKeyBean;
import com.markermall.cat.utils.C;

import java.util.List;

import io.reactivex.Observable;

public class VipModel extends MvpModel {

    public Observable<BaseResponse<VipUseInfoBean>> userInfo(BaseActivity rxActivity){
        return  RxHttp.getInstance().getSysteService()
                    .getVipUserInfo()
                    .compose(RxUtils.<BaseResponse<VipUseInfoBean>>switchSchedulers())
                    .compose(rxActivity.<BaseResponse<VipUseInfoBean>>bindToLifecycle());
    }


    //客服
    public Observable<BaseResponse<TeamInfo>> getServiceQrcode(BaseActivity activity) {
        return RxHttp.getInstance().getUsersService().getServiceQrcode(new RequestInviteCodeBean().setWxShowType(1))
                .compose(RxUtils.<BaseResponse<TeamInfo>>switchSchedulers())
                .compose(activity.<BaseResponse<TeamInfo>>bindToLifecycle());
    }

    //联系我们 HotKeywords
    public Observable<BaseResponse<HotKeywords>> getConfigForKey(BaseActivity activity) {
        RequestKeyBean requestKeyBean = new RequestKeyBean();
        requestKeyBean.setKey(C.SysConfig.CUSTOMER_SERVICE_ADDRESS); //暂时写死
        return RxHttp.getInstance().getUsersService()
                .getConfigForKey(requestKeyBean)
                .compose(RxUtils.<BaseResponse<HotKeywords>>switchSchedulers())
                .compose(activity.<BaseResponse<HotKeywords>>bindToLifecycle());
    }

    public Observable<BaseResponse<String>> upgradeVip(BaseActivity rxActivity){
        return  RxHttp.getInstance().getSysteService()
                .upgradeVip()
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<String>>bindToLifecycle());
    }


    public Observable<BaseResponse<String>> upgradeInstructions(BaseActivity rxActivity, String type){
        RequestGradeUrlBean bean = new RequestGradeUrlBean();
        bean.setType(type);
        return  RxHttp.getInstance().getSysteService()
                .upgradeInstructions(bean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<String>>bindToLifecycle());
    }


    public Observable<BaseResponse<UpgradeInstructions>> upgradeInstructions2(BaseActivity rxActivity, int type){
        return  RxHttp.getInstance().getSysteService()
                .upgradeInstructions2(new RequestGradeBean(type))
                .compose(RxUtils.<BaseResponse<UpgradeInstructions>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<UpgradeInstructions>>bindToLifecycle());
    }


    public Observable<BaseResponse<List<ClassroomBean>>> myCurriculum(BaseActivity rxActivity){
        return  RxHttp.getInstance().getSysteService()
                .myCurriculum()
                .compose(RxUtils.<BaseResponse<List<ClassroomBean>>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<List<ClassroomBean>>>bindToLifecycle());
    }

    public Observable<BaseResponse<String>> mp4Browse(BaseActivity rxActivity, int id){
        return  RxHttp.getInstance().getSysteService()
                .mp4Browse(new RequestVideoId(id))
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<String>>bindToLifecycle());
    }
}
