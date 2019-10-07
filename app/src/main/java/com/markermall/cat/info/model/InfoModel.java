package com.markermall.cat.info.model;


import com.markermall.cat.mvp.base.frame.MvpModel;
import com.markermall.cat.network.BaseResponse;
import com.markermall.cat.network.RxHttp;
import com.markermall.cat.network.RxUtils;
import com.markermall.cat.pojo.ConsComGoodsInfo;
import com.markermall.cat.pojo.DayEarnings;
import com.markermall.cat.pojo.DayHotBean;
import com.markermall.cat.pojo.EarningExplainBean;
import com.markermall.cat.pojo.EarningsMsg;
import com.markermall.cat.pojo.ImageInfo;
import com.markermall.cat.pojo.MonthEarnings;
import com.markermall.cat.pojo.ShopGoodInfo;
import com.markermall.cat.pojo.UpgradeCarousel;
import com.markermall.cat.pojo.myInfo.ApplyUpgradeBean;
import com.markermall.cat.pojo.myInfo.MakeMoenyBean;
import com.markermall.cat.pojo.request.RequestBannerBean;
import com.markermall.cat.pojo.request.RequestCheckGoodsBean;
import com.markermall.cat.pojo.request.RequestGoodsOrderBean;
import com.markermall.cat.pojo.request.RequestIncomeBean;
import com.markermall.cat.pojo.request.RequestListBody;
import com.markermall.cat.pojo.request.RequestSearchOrderBean;
import com.markermall.cat.pojo.request.RequestSystemNoticeBean;
import com.markermall.cat.pojo.requestbodybean.RequestConfirmUpgradeData;
import com.markermall.cat.utils.encrypt.EncryptUtlis;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by fengrs
 * Data: 2018/8/3.
 */
public class InfoModel extends MvpModel {
    private static final String TAG = "InfoModel";
    public static final int msgSysType = 1; // 系统
    public static final int msgAwardType = 3;//奖励
    public static final int msgFansType = 2;//粉丝


    /**
     * 申请升级
     *
     * @param rxFragment
     */
    public Observable<BaseResponse<ApplyUpgradeBean>> applyUpgradeData(RxFragment rxFragment) {
        return RxHttp.getInstance().getUsersService().applyUpgradeData()
                .compose(RxUtils.<BaseResponse<ApplyUpgradeBean>>switchSchedulers())
                .compose(rxFragment.<BaseResponse<ApplyUpgradeBean>>bindToLifecycle());
    }

    /**
     * 确认升级
     *
     * @param rxFragment
     */
    public Observable<BaseResponse<String>> confirmUpgradeData(RxFragment rxFragment) {
        RequestConfirmUpgradeData requestConfirmUpgradeData = new RequestConfirmUpgradeData();
        requestConfirmUpgradeData.setIsApply("1").setSign(EncryptUtlis.getSign2(requestConfirmUpgradeData.setIsApply("1")));
        return RxHttp.getInstance().getUsersService().confirmUpgradeData(requestConfirmUpgradeData)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(rxFragment.<BaseResponse<String>>bindToLifecycle());
    }

    /**
     * 赚钱三部曲
     *
     * @param rxFragment
     */
    public Observable<BaseResponse<MakeMoenyBean>> getMakeMoneyRule(RxFragment rxFragment) {
        return RxHttp.getInstance().getUsersService().getMakeMoneyRule()
                .compose(RxUtils.<BaseResponse<MakeMoenyBean>>switchSchedulers())
                .compose(rxFragment.<BaseResponse<MakeMoenyBean>>bindToLifecycle());
    }


    /**
     * 获取订单列表
     *
     * @param rxFragment
     * @param orderType
     */
    public Observable<BaseResponse<List<ConsComGoodsInfo>>> getGoodsOrder(RxFragment rxFragment, int orderType, int teamType, int page) {
        RequestGoodsOrderBean requestBean = new RequestGoodsOrderBean();
        requestBean.setOrderStatus(orderType);
        requestBean.setType(teamType);
        requestBean.setPage(page);

        return RxHttp.getInstance().getUsersService()
                .getGoodsOrder(requestBean)
                .compose(RxUtils.<BaseResponse<List<ConsComGoodsInfo>>>switchSchedulers())
                .compose(rxFragment.<BaseResponse<List<ConsComGoodsInfo>>>bindToLifecycle());
    }

    /**
     * 获取订单列表
     *
     * @param rxFragment
     * @param type
     */
    public Observable<BaseResponse<List<ConsComGoodsInfo>>> searchGoodsOrder(RxFragment rxFragment, String orderNo, int type,int page) {
        RequestSearchOrderBean requestBean = new RequestSearchOrderBean();
        requestBean.setOrderNo(orderNo);
        requestBean.setType(type);
        requestBean.setPage(page);
        requestBean.setRid("");
        return RxHttp.getInstance().getUsersService().searchGoodsOrder(requestBean)
                .compose(RxUtils.<BaseResponse<List<ConsComGoodsInfo>>>switchSchedulers())
                .compose(rxFragment.<BaseResponse<List<ConsComGoodsInfo>>>bindToLifecycle());
    }



    /**
     * 获取收入明细(日)
     *
     * @param rxFragment
     */
    public Observable<BaseResponse<DayEarnings>> getDayIncome(RxFragment rxFragment) {
        return RxHttp.getInstance().getUsersService().getDayIncome()
                .compose(RxUtils.<BaseResponse<DayEarnings>>switchSchedulers())
                .compose(rxFragment.<BaseResponse<DayEarnings>>bindToLifecycle());
    }
    /**
     * 获取收入明细(月)
     *
     * @param rxFragment
     */
    public Observable<BaseResponse<MonthEarnings>> getMonthIncome(RxFragment rxFragment) {
        return RxHttp.getInstance().getUsersService().getMonthIncome()
                .compose(RxUtils.<BaseResponse<MonthEarnings>>switchSchedulers())
                .compose(rxFragment.<BaseResponse<MonthEarnings>>bindToLifecycle());
    }
    /**
     * 获取分享海报列表
     *
     * @param fragment
     * @param back
     * @return
     */
    public Observable<BaseResponse<List<ImageInfo>>> getBanner(RxFragment fragment, int back) {
        RequestBannerBean requestBean = new RequestBannerBean();
        requestBean.setType(back);
        return RxHttp.getInstance().getSysteService()
//                .getBanner(back,2)
                .getBanner(requestBean)
                .compose(RxUtils.<BaseResponse<List<ImageInfo>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<ImageInfo>>>bindToLifecycle());
    }


    /**
     * 获取订单列表(马克猫生活的订单)
     *
     * @param rxFragment
     */
    public Observable<BaseResponse<List<ConsComGoodsInfo>>> getGoodsLiveOrder(RxFragment rxFragment, int order_type, int page) {
        RequestGoodsOrderBean requestBean = new RequestGoodsOrderBean();
        requestBean.setOrderStatus(order_type);
        requestBean.setPage(page);
        return RxHttp.getInstance().getCommonService().getGoodsLiveOrder(requestBean)
                .compose(RxUtils.<BaseResponse<List<ConsComGoodsInfo>>>switchSchedulers())
                .compose(rxFragment.<BaseResponse<List<ConsComGoodsInfo>>>bindToLifecycle());
    }

    /**
     * 消息
     *
     * @param fragment
     * @param type
     * @param page
     * @return
     */

    public Observable<BaseResponse<List<EarningsMsg>>> getMsg(RxFragment fragment, int type, int page) {

        RequestSystemNoticeBean requestBean = new RequestSystemNoticeBean();
        requestBean.setType(type);
        requestBean.setPage(page);
        return RxHttp.getInstance().getCommonService()
                .systemNotice(requestBean)
                .compose(RxUtils.<BaseResponse<List<EarningsMsg>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<EarningsMsg>>>bindToLifecycle());

    }

    /**
     * 每日爆款
     *
     * @param fragment
     * @return
     */

    public Observable<BaseResponse<List<DayHotBean>>> getDayHotMsg(RxFragment fragment) {

        return RxHttp.getInstance().getCommonService()
                .dayHotMsg()
                .compose(RxUtils.<BaseResponse<List<DayHotBean>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<DayHotBean>>>bindToLifecycle());

    }

    /**
     * 消息
     *
     * @param fragment
     * @return
     */

    public Observable<BaseResponse<String>> checkWithdrawTime(RxFragment fragment) {


        return RxHttp.getInstance().getCommonService()
                .checkWithdrawTime()
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(fragment.<BaseResponse<String>>bindToLifecycle());

    }


//    /**
//     * 累计收益(日)
//     *
//     * @param rxFragment
//     */
//    public Observable<BaseResponse<String>> getAccumulatedAmount(RxFragment rxFragment) {
//        return RxHttp.getInstance().getUsersService().getAccumulatedAmount()
//                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
//                .compose(rxFragment.<BaseResponse<String>>bindToLifecycle());
//    }

    /**
     * 累计收益(日)
     *
     * @param rxFragment
     */
    public Observable<BaseResponse<ShopGoodInfo>> onCheckGoods(RxFragment rxFragment, String itemSourceId) {
        RequestCheckGoodsBean requestCheckGoodsBean = new RequestCheckGoodsBean();
        requestCheckGoodsBean.setItemSourceId(itemSourceId);
        return RxHttp.getInstance().getUsersService().onCheckGoods(requestCheckGoodsBean)
                .compose(RxUtils.<BaseResponse<ShopGoodInfo>>switchSchedulers())
                .compose(rxFragment.<BaseResponse<ShopGoodInfo>>bindToLifecycle());
    }


    /**
     * 用户反馈回复消息
     *
     * @param fragment
     * @param type
     * @param page
     * @return
     */

    public Observable<BaseResponse<List<EarningsMsg>>> getFeedbackMsg(RxFragment fragment,int page) {
        RequestListBody requestBean = new RequestListBody();
        requestBean.setPage(page);
        return RxHttp.getInstance().getCommonService()
                .getFeedbackMsg(requestBean)
                .compose(RxUtils.<BaseResponse<List<EarningsMsg>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<EarningsMsg>>>bindToLifecycle());

    }


    /**
     *  获取京东、苏宁、拼多多收益-日
     *
     * @param rxFragment
     */
    public Observable<BaseResponse<DayEarnings>> getPlatformDayIncome(RxFragment rxFragment,int type) {
        RequestIncomeBean incomeBean = new RequestIncomeBean();
        incomeBean.setType(type);
        return RxHttp.getInstance().getUsersService().getPlatformDayIncome(incomeBean)
                .compose(RxUtils.<BaseResponse<DayEarnings>>switchSchedulers())
                .compose(rxFragment.<BaseResponse<DayEarnings>>bindToLifecycle());
    }

    /**
     *  获取京东、苏宁、拼多多收益-月
     *
     * @param rxFragment
     */
    public Observable<BaseResponse<MonthEarnings>> getPlatformMonthIncome(RxFragment rxFragment,int type) {
        RequestIncomeBean incomeBean = new RequestIncomeBean();
        incomeBean.setType(type);
        return RxHttp.getInstance().getUsersService().getPlatformMonthIncome(incomeBean)
                .compose(RxUtils.<BaseResponse<MonthEarnings>>switchSchedulers())
                .compose(rxFragment.<BaseResponse<MonthEarnings>>bindToLifecycle());
    }
    /**
     * 我的--粉丝升级拉新轮播
     *
     * @param rxFragment
     */
    public Observable<BaseResponse<List<UpgradeCarousel>>> getUpgradeCarousel(RxFragment rxFragment) {
        return RxHttp.getInstance().getUsersService().getUpgradeCarousel()
                .compose(RxUtils.<BaseResponse<List<UpgradeCarousel>>>switchSchedulers())
                .compose(rxFragment.<BaseResponse<List<UpgradeCarousel>>>bindToLifecycle());
    }
    /**
     * 升级运营商弹窗提示
     *
     * @param rxFragment
     */
    public Observable<BaseResponse<UpgradeCarousel>> getIsUpgrade(RxFragment rxFragment) {
        return RxHttp.getInstance().getUsersService().getIsUpgrade()
                .compose(RxUtils.<BaseResponse<UpgradeCarousel>>switchSchedulers())
                .compose(rxFragment.<BaseResponse<UpgradeCarousel>>bindToLifecycle());
    }


    public Observable<BaseResponse<EarningExplainBean>> getEarningsExplain(RxFragment rxFragment) {
        return RxHttp.getInstance().getUsersService().getEarningsExplain()
                .compose(RxUtils.<BaseResponse<EarningExplainBean>>switchSchedulers())
                .compose(rxFragment.<BaseResponse<EarningExplainBean>>bindToLifecycle());
    }


}
