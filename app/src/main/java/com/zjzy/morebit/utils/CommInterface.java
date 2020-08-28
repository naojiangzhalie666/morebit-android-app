package com.zjzy.morebit.utils;

import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.pojo.CheckWithDrawBean;
import com.zjzy.morebit.pojo.DoorGodCategoryBean;
import com.zjzy.morebit.pojo.FloorBean2;
import com.zjzy.morebit.pojo.PanicBuyingListBean;
import com.zjzy.morebit.pojo.QueryDhAndGyBean;
import com.zjzy.morebit.pojo.RequestWithdrawBean2;
import com.zjzy.morebit.pojo.UnreadInforBean;
import com.zjzy.morebit.pojo.UserZeroInfoBean;
import com.zjzy.morebit.pojo.goods.HandpickBean;
import com.zjzy.morebit.pojo.request.RequestBannerBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestCheckWithdrawBean;

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


    //查询抖货和高佣专区接口
    public static Observable<BaseResponse<QueryDhAndGyBean>> getQueryDhAndGy(RxFragment fragment) {
        return RxHttp.getInstance().getSysteService().getQueryDhAndGy()
                .compose(RxUtils.<BaseResponse<QueryDhAndGyBean>>switchSchedulers())
                .compose(fragment.<BaseResponse<QueryDhAndGyBean>>bindToLifecycle());
    }

}
