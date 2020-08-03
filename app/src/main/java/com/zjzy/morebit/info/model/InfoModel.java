package com.zjzy.morebit.info.model;


import com.zjzy.morebit.mvp.base.frame.MvpModel;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.pojo.ConsComGoodsInfo;
import com.zjzy.morebit.pojo.UserIncomeDetail;
import com.zjzy.morebit.pojo.DayHotBean;
import com.zjzy.morebit.pojo.EarningExplainBean;
import com.zjzy.morebit.pojo.EarningsMsg;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.MonthEarnings;
import com.zjzy.morebit.pojo.ProgramGetGoodsDetailBean;
import com.zjzy.morebit.pojo.RequestReadNotice;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UpgradeCarousel;
import com.zjzy.morebit.pojo.myInfo.ApplyUpgradeBean;
import com.zjzy.morebit.pojo.myInfo.MakeMoenyBean;
import com.zjzy.morebit.pojo.request.RequestBannerBean;
import com.zjzy.morebit.pojo.request.RequestCheckGoodsBean;
import com.zjzy.morebit.pojo.request.RequestGoodsOrderBean;
import com.zjzy.morebit.pojo.request.RequestIncomeBean;
import com.zjzy.morebit.pojo.request.RequestListBody;
import com.zjzy.morebit.pojo.request.RequestOrderDetailBean;
import com.zjzy.morebit.pojo.request.RequestSearchOrderBean;
import com.zjzy.morebit.pojo.request.RequestSystemNoticeBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestConfirmUpgradeData;
import com.zjzy.morebit.utils.encrypt.EncryptUtlis;
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
    public static final int msgActivityType = 6;//活动
    public static final int msgGoodsype = 7;//好货

    //消息已读
    public Observable<BaseResponse<String>> getReadNotice(RxFragment fragment,int  type) {
        RequestReadNotice notice=new RequestReadNotice();
        notice.setType(type);
        return RxHttp.getInstance().getSysteService().getReadNotice(notice)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(fragment.<BaseResponse<String>>bindToLifecycle());
    }
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
     * 获取收益
     *
     * @param rxFragment
     */
    public Observable<BaseResponse<UserIncomeDetail>> getUserIncomeDetail(RxFragment rxFragment) {
        return RxHttp.getInstance().getUsersService().getUserIncomeDetail()
                .compose(RxUtils.<BaseResponse<UserIncomeDetail>>switchSchedulers())
                .compose(rxFragment.<BaseResponse<UserIncomeDetail>>bindToLifecycle());
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
     * 获取订单列表(多点优选生活的订单)
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
     * 消息
     *
     * @param fragment
     * @param type
     * @param page
     * @return
     */

    public Observable<BaseResponse<List<EarningsMsg>>> getMsg2(RxFragment fragment, int type, int page,int orderSource) {

        RequestSystemNoticeBean requestBean = new RequestSystemNoticeBean();
        requestBean.setType(type);
        requestBean.setPage(page);
        requestBean.setOrderSource(orderSource);
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
     * @param
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
    public Observable<BaseResponse<UserIncomeDetail>> getPlatformDayIncome(RxFragment rxFragment, int type) {
        RequestIncomeBean incomeBean = new RequestIncomeBean();
        incomeBean.setType(type);
        return RxHttp.getInstance().getUsersService().getPlatformDayIncome(incomeBean)
                .compose(RxUtils.<BaseResponse<UserIncomeDetail>>switchSchedulers())
                .compose(rxFragment.<BaseResponse<UserIncomeDetail>>bindToLifecycle());
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

    /**
     * 确认收货
     * @param rxFragment
     * @param OrderId
     * @return
     */
    public Observable<BaseResponse<Boolean>> confirmOrder(RxFragment rxFragment,String OrderId) {
        RequestOrderDetailBean bean = new RequestOrderDetailBean();
        bean.setOrderId(OrderId);
        return RxHttp.getInstance().getCommonService().confirmOrder(bean)
                .compose(RxUtils.<BaseResponse<Boolean>>switchSchedulers())
                .compose(rxFragment.<BaseResponse<Boolean>>bindToLifecycle());
    }

    public Observable<BaseResponse<ShopGoodInfo>> getBaseResponseObservableForJd(RxFragment rxFragment, ShopGoodInfo goodsInfo) {
        return RxHttp.getInstance().getCommonService().getJdPddGoodsDetail(new ProgramGetGoodsDetailBean().setType(1)
                .setGoodsId(goodsInfo.getGoodsId()))
                .compose(RxUtils.<BaseResponse<ShopGoodInfo>>switchSchedulers())
                .compose(rxFragment.<BaseResponse<ShopGoodInfo>>bindToLifecycle());
    }
    public Observable<BaseResponse<ShopGoodInfo>> getBaseResponseObservableForPd(RxFragment rxFragment, ShopGoodInfo goodsInfo) {
        return RxHttp.getInstance().getCommonService().getJdPddGoodsDetail(new ProgramGetGoodsDetailBean().setType(2)
                .setGoodsId(goodsInfo.getGoodsId()))
                .compose(RxUtils.<BaseResponse<ShopGoodInfo>>switchSchedulers())
                .compose(rxFragment.<BaseResponse<ShopGoodInfo>>bindToLifecycle());
    }

}
