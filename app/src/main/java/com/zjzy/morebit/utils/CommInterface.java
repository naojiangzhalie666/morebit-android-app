package com.zjzy.morebit.utils;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.pojo.CheckWithDrawBean;
import com.zjzy.morebit.pojo.DoorGodCategoryBean;
import com.zjzy.morebit.pojo.FloorBean2;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.NoticemBean;
import com.zjzy.morebit.pojo.PanicBuyingListBean;
import com.zjzy.morebit.pojo.QueryDhAndGyBean;
import com.zjzy.morebit.pojo.RequestWithdrawBean2;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UnreadInforBean;
import com.zjzy.morebit.pojo.UserIncomeDetail2;
import com.zjzy.morebit.pojo.UserZeroInfoBean;
import com.zjzy.morebit.pojo.goods.HandpickBean;
import com.zjzy.morebit.pojo.myInfo.UpdateInfoBean;
import com.zjzy.morebit.pojo.request.RequestBannerBean;
import com.zjzy.morebit.pojo.request.RequestUpdateUserBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestCheckWithdrawBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestItemSourceId;

import java.util.List;

import io.reactivex.Observable;

/*
* 公共接口
* */
public class CommInterface {

    /**
     * 检测是否存在提现记录
     *
     * @param fragment
     * @return
     */

    public static Observable<BaseResponse<CheckWithDrawBean>> checkWithdrawalRecord(RxFragment fragment) {
        RequestCheckWithdrawBean requestCheckWithdrawBean = new RequestCheckWithdrawBean(1);
        return RxHttp.getInstance().getCommonService()
                .checkWithdrawalRecord(requestCheckWithdrawBean)
                .compose(RxUtils.<BaseResponse<CheckWithDrawBean>>switchSchedulers())
                .compose(fragment.<BaseResponse<CheckWithDrawBean>>bindToLifecycle());

    }


    /**
     * 提现明细
     *
     * @param fragment
     * @return
     */

    public static Observable<BaseResponse<String>> getUserWithdrawApplyNew(RxFragment fragment, String allMoney, int type) {
        RequestWithdrawBean2 withdrawBean2 = new RequestWithdrawBean2();
        withdrawBean2.setAmount(allMoney);
        withdrawBean2.setType(type + "");
        return RxHttp.getInstance().getCommonService()
                .getUserWithdrawApplyNew(withdrawBean2)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(fragment.<BaseResponse<String>>bindToLifecycle());

    }

    //消息是否已读
    public static Observable<BaseResponse<UnreadInforBean>> getUnreadInformation(RxFragment fragment) {
        return RxHttp.getInstance().getSysteService().getUnreadInformation()
                .compose(RxUtils.<BaseResponse<UnreadInforBean>>switchSchedulers())
                .compose(fragment.<BaseResponse<UnreadInforBean>>bindToLifecycle());
    }

    //新楼层查询
    public static Observable<BaseResponse<FloorBean2>> getListGraphicInfoSorting(RxFragment fragment) {
        return RxHttp.getInstance().getSysteService().getListGraphicInfoSorting()
                .compose(RxUtils.<BaseResponse<FloorBean2>>switchSchedulers())
                .compose(fragment.<BaseResponse<FloorBean2>>bindToLifecycle());
    }

    //爆款热销
    public static Observable<BaseResponse<List<HandpickBean>>> getActivities(RxFragment fragment) {
        return RxHttp.getInstance().getSysteService().getActivities()
                .compose(RxUtils.<BaseResponse<List<HandpickBean>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<HandpickBean>>>bindToLifecycle());
    }

    //限时秒杀
    public static Observable<BaseResponse<PanicBuyingListBean>> getpainBuyinglist(RxFragment fragment) {
        return RxHttp.getInstance().getSysteService().getpanicBuyingList()
                .compose(RxUtils.<BaseResponse<PanicBuyingListBean>>switchSchedulers())
                .compose(fragment.<BaseResponse<PanicBuyingListBean>>bindToLifecycle());
    }

    //0元购信息
    public static Observable<BaseResponse<UserZeroInfoBean>> getUserZeroInfo(RxFragment fragment) {
        return RxHttp.getInstance().getSysteService().getUserZeroInfo()
                .compose(RxUtils.<BaseResponse<UserZeroInfoBean>>switchSchedulers())
                .compose(fragment.<BaseResponse<UserZeroInfoBean>>bindToLifecycle());
    }

    //获取金刚位
    public static Observable<BaseResponse<DoorGodCategoryBean>> getDoorGodCategory(RxFragment fragment) {
        RequestBannerBean requestBannerBean = new RequestBannerBean();
        requestBannerBean.setType(C.UIShowType.HomeIcon);
        return RxHttp.getInstance().getSysteService().getDoorGodCategory(requestBannerBean)
                .compose(RxUtils.<BaseResponse<DoorGodCategoryBean>>switchSchedulers())
                .compose(fragment.<BaseResponse<DoorGodCategoryBean>>bindToLifecycle());
    }
    //获取金刚位
    public static Observable<BaseResponse<List<ImageInfo>>> getDoorGodCategory2(RxFragment fragment) {
        RequestBannerBean requestBannerBean = new RequestBannerBean();
        requestBannerBean.setType(C.UIShowType.HomeIcon);
        return RxHttp.getInstance().getSysteService().getBanner(requestBannerBean)
                .compose(RxUtils.<BaseResponse<List<ImageInfo>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<ImageInfo>>>bindToLifecycle());
    }


    //查询抖货和高佣专区接口
    public static Observable<BaseResponse<QueryDhAndGyBean>> getQueryDhAndGy(RxFragment fragment) {
        return RxHttp.getInstance().getSysteService().getQueryDhAndGy()
                .compose(RxUtils.<BaseResponse<QueryDhAndGyBean>>switchSchedulers())
                .compose(fragment.<BaseResponse<QueryDhAndGyBean>>bindToLifecycle());
    }

    /**
     * 用户等级升级
     *
     * @param fragment
     * @return
     */
    public static Observable<BaseResponse<UpdateInfoBean>> updateUserGrade(RxAppCompatActivity fragment, int userGrade) {
        RequestUpdateUserBean updateUserBean = new RequestUpdateUserBean();
        updateUserBean.setType(userGrade);
        return RxHttp.getInstance().getUsersService().updateUserGrade(updateUserBean)
                .compose(RxUtils.<BaseResponse<UpdateInfoBean>>switchSchedulers())
                .compose(fragment.<BaseResponse<UpdateInfoBean>>bindToLifecycle());
    }

    /**
     * 用户等级升级
     *
     * @param fragment
     * @return
     */
    public static Observable<BaseResponse<UpdateInfoBean>> updateUserGrade(RxFragment fragment, int userGrade) {
        RequestUpdateUserBean updateUserBean = new RequestUpdateUserBean();
        updateUserBean.setType(userGrade);
        return RxHttp.getInstance().getUsersService().updateUserGrade(updateUserBean)
                .compose(RxUtils.<BaseResponse<UpdateInfoBean>>switchSchedulers())
                .compose(fragment.<BaseResponse<UpdateInfoBean>>bindToLifecycle());
    }


    //消息列表
    public static Observable<BaseResponse<NoticemBean>> getUserNoticeList(RxFragment fragment) {
        return RxHttp.getInstance().getSysteService().getUserNoticeList()
                .compose(RxUtils.<BaseResponse<NoticemBean>>switchSchedulers())
                .compose(fragment.<BaseResponse<NoticemBean>>bindToLifecycle());
    }


    /**
     * 获取收益
     *
     * @param rxFragment
     */
    public static Observable<BaseResponse<UserIncomeDetail2>> getUserIncomeDetail(RxFragment rxFragment) {
        return RxHttp.getInstance().getUsersService().getUserIncome()
                .compose(RxUtils.<BaseResponse<UserIncomeDetail2>>switchSchedulers())
                .compose(rxFragment.<BaseResponse<UserIncomeDetail2>>bindToLifecycle());
    }
    //淘宝详情
    public static Observable<BaseResponse<ShopGoodInfo>> getBaseResponseObservable(BaseActivity rxActivity, String itemId) {
        return RxHttp.getInstance().getCommonService().getDetailData(new RequestItemSourceId().setItemSourceId(itemId).setItemFrom("1"))
                .compose(RxUtils.<BaseResponse<ShopGoodInfo>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<ShopGoodInfo>>bindToLifecycle());
    }

}
