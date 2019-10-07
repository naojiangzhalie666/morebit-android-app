package com.markermall.cat.main.model;

import com.markermall.cat.LocalData.UserLocalData;
import com.markermall.cat.Module.common.Activity.BaseActivity;
import com.markermall.cat.Module.common.Utils.LoadingView;
import com.markermall.cat.Module.push.Logger;
import com.markermall.cat.main.ui.CollectFragment2;
import com.markermall.cat.mvp.base.frame.MvpModel;
import com.markermall.cat.network.BaseResponse;
import com.markermall.cat.network.RxHttp;
import com.markermall.cat.network.RxUtils;
import com.markermall.cat.pojo.RankingTitleBean;
import com.markermall.cat.pojo.ShopGoodInfo;
import com.markermall.cat.pojo.request.RequestByGoodList;
import com.markermall.cat.pojo.request.RequestCollectionListBean;
import com.markermall.cat.pojo.request.RequestMaterial;
import com.markermall.cat.pojo.requestbodybean.RequestDeleteCollection;
import com.markermall.cat.pojo.requestbodybean.RequestGetActivitiesDetails;
import com.markermall.cat.pojo.requestbodybean.RequestGetGoodsByBrand;
import com.markermall.cat.pojo.requestbodybean.RequestGetNinepinkageGoods;
import com.markermall.cat.pojo.requestbodybean.RequestGetRankings;
import com.markermall.cat.pojo.requestbodybean.RequestGoodsLike;
import com.markermall.cat.pojo.requestbodybean.RequestShopId;
import com.markermall.cat.utils.MyGsonUtils;
import com.markermall.cat.utils.encrypt.EncryptUtlis;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * Created by fengrs
 * Data: 2018/8/28.
 */
public class MainModel extends MvpModel {


    public Observable<BaseResponse<List<ShopGoodInfo>>> getCollectData(CollectFragment2 fragment, int page) {
        RequestCollectionListBean requestBean = new RequestCollectionListBean();
        requestBean.setPage(page);
        return RxHttp.getInstance().getGoodsService()
                .getCollectData(requestBean)
                .compose(RxUtils.<BaseResponse<List<ShopGoodInfo>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<ShopGoodInfo>>>bindToLifecycle());
    }


    public Observable<BaseResponse<String>> delUserCollection(BaseActivity activity, String ids) {
        RequestDeleteCollection requestDeleteCollection = new RequestDeleteCollection();
        String sign = EncryptUtlis.getSign2(requestDeleteCollection.setIds(ids));
        requestDeleteCollection.setIds(ids).setSign(sign);
        Logger.e("==json==" + MyGsonUtils.beanToJson(requestDeleteCollection));
        return RxHttp.getInstance().getCommonService().delUserCollection(requestDeleteCollection)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(activity.<BaseResponse<String>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                });

    }

    /**
     * 排行
     *
     * @param rxFragmen
     * @param page
     * @param order
     * @param where
     */
    public Observable<BaseResponse<List<ShopGoodInfo>>> getRankings(RxFragment rxFragmen, String type, int page, String order, String where) {

        return RxHttp.getInstance().getGoodsService().getRankings(new RequestGetRankings().setType(type)
                .setPage(String.valueOf(page))
                .setOrder(order)
                .setSort(where))
                .compose(RxUtils.<BaseResponse<List<ShopGoodInfo>>>switchSchedulers())
                .compose(rxFragmen.<BaseResponse<List<ShopGoodInfo>>>bindToLifecycle());
    }

    /**
     * 精选活动详情
     *
     * @param rxFragmen
     * @param page
     * @param order
     * @param where
     */
    public Observable<BaseResponse<List<ShopGoodInfo>>> getActivitiesDetails(RxFragment rxFragmen, int page, String order, String where, String activity_id) {
        return RxHttp.getInstance().getSysteService().getActivitiesDetails(new RequestGetActivitiesDetails().setPage(page)
                .setOrder(order)
                .setSort(where)
                .setActivityId(activity_id))
                .compose(RxUtils.<BaseResponse<List<ShopGoodInfo>>>switchSchedulers())
                .compose(rxFragmen.<BaseResponse<List<ShopGoodInfo>>>bindToLifecycle());

    }

    /**
     * 通过品牌ID获取商品列表
     *
     * @param brandId 品牌ID
     * @param page
     * @return
     */
    public Observable<BaseResponse<List<ShopGoodInfo>>> getGoodsByBrand(RxFragment rxFragmen, int page, String order, String where, String brandId) {

        return RxHttp.getInstance().getGoodsService().getGoodsByBrand(new RequestGetGoodsByBrand().setBrandId(brandId)
                .setPage(String.valueOf(page))
                .setOrder(order)
                .setSort(where))
                .compose(RxUtils.<BaseResponse<List<ShopGoodInfo>>>switchSchedulers())
                .compose(rxFragmen.<BaseResponse<List<ShopGoodInfo>>>bindToLifecycle());

    }

    /**
     * 根据type 获取商品
     *
     * @return
     */
    public Observable<BaseResponse<List<ShopGoodInfo>>> findTypeByGoodList(RxFragment rxFragmen, int page, String order, String where, int type) {
        RequestByGoodList requestBean = new RequestByGoodList();
        requestBean.setPage(page);
        requestBean.setSort(where);
        requestBean.setOrder(order);
        requestBean.setType(type);

        return RxHttp.getInstance().getGoodsService()
                .findTypeByGoodList(requestBean)
                .compose(RxUtils.<BaseResponse<List<ShopGoodInfo>>>switchSchedulers())
                .compose(rxFragmen.<BaseResponse<List<ShopGoodInfo>>>bindToLifecycle());
    }

    /**
     * 9.9包邮
     *
     * @return
     */
    public Observable<BaseResponse<List<ShopGoodInfo>>> getNinepinkageGoods(RxFragment rxFragmen, int page, String where, String order) {

        return RxHttp.getInstance().getGoodsService().getNinepinkageGoods(new RequestGetNinepinkageGoods().setPage(page + "")
                .setSort(where)
                .setOrder(order))
                .compose(RxUtils.<BaseResponse<List<ShopGoodInfo>>>switchSchedulers())
                .compose(rxFragmen.<BaseResponse<List<ShopGoodInfo>>>bindToLifecycle());

    }

    /**
     * 商家同店商品
     *
     * @return
     */
    public Observable<BaseResponse<List<ShopGoodInfo>>> getIdentical(RxFragment rxFragmen, String userId, int page) {
        return RxHttp.getInstance().getGoodsService().getIdentical(new RequestShopId().setShopId(userId).setPage(page))
                .compose(RxUtils.<BaseResponse<List<ShopGoodInfo>>>switchSchedulers())
                .compose(rxFragmen.<BaseResponse<List<ShopGoodInfo>>>bindToLifecycle());
    }

    /**
     * 超值大牌活动商品列表
     *
     * @return
     */
    public Observable<BaseResponse<List<ShopGoodInfo>>> getGreatValueList(RxFragment rxFragmen, int page, String order, String where) {
        RequestByGoodList requestBean = new RequestByGoodList();
        requestBean.setPage(page);
        requestBean.setSort(where);
        requestBean.setOrder(order);
        return RxHttp.getInstance().getGoodsService()
                .getGreatValueList(requestBean)
                .compose(RxUtils.<BaseResponse<List<ShopGoodInfo>>>switchSchedulers())
                .compose(rxFragmen.<BaseResponse<List<ShopGoodInfo>>>bindToLifecycle());
    }

    /**
     * 第三方活动-预告单
     *
     * @return
     */
    public Observable<BaseResponse<List<ShopGoodInfo>>> getHandanku(RxFragment rxFragmen, int page, String order, String where, int type) {
        RequestByGoodList requestBean = new RequestByGoodList();
        requestBean.setPage(page);
        requestBean.setSort(where);
        requestBean.setOrder(order);
        requestBean.setType(type);
        return RxHttp.getInstance().getGoodsService()
                .getHandanku(requestBean)
                .compose(RxUtils.<BaseResponse<List<ShopGoodInfo>>>switchSchedulers())
                .compose(rxFragmen.<BaseResponse<List<ShopGoodInfo>>>bindToLifecycle());
    }

    /**
     * 猜你喜欢
     *
     * @return
     */
    public Observable<BaseResponse<List<ShopGoodInfo>>> getWhatLike(RxFragment rxFragmen, int page, String imei) {
        RequestGoodsLike requestGoodsLike = new RequestGoodsLike();
        requestGoodsLike.setDeviceType("IMEI");
        requestGoodsLike.setDeviceValue(imei);
        requestGoodsLike.setPage(page);
        return    RxHttp.getInstance().getCommonService().getRecommendItemsById(requestGoodsLike)
                .compose(RxUtils.<BaseResponse<List<ShopGoodInfo>>>switchSchedulers())
                .compose(rxFragmen.<BaseResponse<List<ShopGoodInfo>>>bindToLifecycle());
    }

    /**
     * 淘礼金列表
     *
     * @return
     */
    public Observable<BaseResponse<List<ShopGoodInfo>>> getMaterial(RxFragment rxFragmen, int page, String material) {
        RequestMaterial requestBean = new RequestMaterial();
        requestBean.setPage(page);
        requestBean.setMaterial(material);
        requestBean.setInvedeCode(UserLocalData.getUser().getInviteCode());
        return RxHttp.getInstance().getGoodsService()
                .getGoodsMaterial(requestBean)
                .compose(RxUtils.<BaseResponse<List<ShopGoodInfo>>>switchSchedulers())
                .compose(rxFragmen.<BaseResponse<List<ShopGoodInfo>>>bindToLifecycle());
    }

    /**
     * 爆款排行_新接口
     *
     * @return
     */
    public Observable<BaseResponse<List<ShopGoodInfo>>> getRankingNews(RxFragment rxFragmen, RankingTitleBean bean) {
        return RxHttp.getInstance().getGoodsService().getRankingNews(bean)
                .compose(RxUtils.<BaseResponse<List<ShopGoodInfo>>>switchSchedulers())
                .compose(rxFragmen.<BaseResponse<List<ShopGoodInfo>>>bindToLifecycle());
    }


}
