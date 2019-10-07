package com.markermall.cat.main.presenter;

import android.text.TextUtils;

import com.markermall.cat.main.contract.RankingContract;
import com.markermall.cat.main.model.MainModel;
import com.markermall.cat.main.ui.fragment.ShoppingListFragment2;
import com.markermall.cat.mvp.base.frame.MvpPresenter;
import com.markermall.cat.network.BaseResponse;
import com.markermall.cat.network.RxHttp;
import com.markermall.cat.network.RxUtils;
import com.markermall.cat.network.observer.DataObserver;
import com.markermall.cat.pojo.RankingTitleBean;
import com.markermall.cat.pojo.ShopGoodBean;
import com.markermall.cat.pojo.ShopGoodInfo;
import com.markermall.cat.pojo.UI.BaseTitleTabBean;
import com.markermall.cat.pojo.request.RequestSearchBean;
import com.markermall.cat.utils.C;
import com.markermall.cat.utils.fire.DeviceIDUtils;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * Created by fengrs
 * Data: 2018/8/3.
 */
public class ShoppingListPresenter extends MvpPresenter<MainModel, RankingContract.View> implements RankingContract.Present {
    int page = 1;
    // 三级分类用地  全网搜索
    private int mSearchType = 1;
    private String mMinId = "1";
    private String mPageString = "1";
    // 是否加载猜你喜欢数据
    private boolean mIsLoadWhatLike;
    private String mDeviceImei;
    // 三级分类用地  全网搜索

    @Override
    public void getRankings(RxFragment rxFragmen, int type, final int loadType, BaseTitleTabBean bean) {
        if (bean == null) return;
        if (loadType == C.requestType.initData) {
            page = 1;
        }

        Observable<BaseResponse<List<ShopGoodInfo>>> observable = null;

        if (type == C.GoodsListType.CurrentRanking) {
            observable = mModel.getRankings(rxFragmen, "1", page, bean.order, bean.where);
        } else if (type == C.GoodsListType.RealTimeRanking) {
            observable = mModel.getRankings(rxFragmen, "2", page, bean.order, bean.where);
        } else if (type == C.GoodsListType.TypeActivity) {
            observable = mModel.getActivitiesDetails(rxFragmen, page, bean.order, bean.where, bean.activity_id);
        } else if (type == ShoppingListFragment2.TYPEGOODSBYBRAND) {//
            observable = getGoodsByBrand(rxFragmen, bean);
        } else if (type == C.GoodsListType.DAYRECOMMEND) {
            observable = mModel.findTypeByGoodList(rxFragmen, page, bean.order, bean.where, type);
        } else if (type == C.GoodsListType.TAOQIANGGOU) {
            observable = mModel.findTypeByGoodList(rxFragmen, page, bean.order, bean.where, C.GoodsListType.TAOQIANGGOU);
        } else if (type == C.GoodsListType.JVHUASUAN) {
            observable = mModel.findTypeByGoodList(rxFragmen, page, bean.order, bean.where, type);
        } else if (type == C.GoodsListType.THREEGOODS) {
            getThreeGoods(rxFragmen, bean.categoryId, bean, loadType);
            return;

        } else if (type == C.GoodsListType.NINEPINKAGE) {
            observable = mModel.getNinepinkageGoods(rxFragmen, page, bean.where, bean.order);
        } else if (type == C.GoodsListType.DiscountStores) {
            observable = mModel.getIdentical(rxFragmen, bean.categoryId, page);
        } else if (type == C.GoodsListType.ForeShow_1) {
            observable = mModel.getHandanku(rxFragmen, page, bean.order, bean.where, 1);
        } else if (type == C.GoodsListType.ForeShow_2) {
            observable = mModel.getHandanku(rxFragmen, page, bean.order, bean.where, 2);
        } else if (type == C.GoodsListType.ForeShow_3) {
            observable = mModel.getHandanku(rxFragmen, page, bean.order, bean.where, 3);
        } else if (type == C.GoodsListType.BigShopList) {
            observable = mModel.getGreatValueList(rxFragmen, page, bean.order, bean.where);
        } else if (type == C.GoodsListType.WHAT_LIKE) {
            if (page == 1) {
                mDeviceImei = DeviceIDUtils.getimei(rxFragmen.getActivity());
                mIsLoadWhatLike =  !TextUtils.isEmpty(mDeviceImei);
            }
            if (mIsLoadWhatLike) {
                observable = mModel.getWhatLike(rxFragmen, page, mDeviceImei);

            } else {
                observable = mModel.getRankings(rxFragmen, "1", page, bean.order, bean.where);
            }
        } else if (type == C.GoodsListType.MATERIAL) {
            observable = mModel.getMaterial(rxFragmen, page, bean.material);
        } else {
            observable = mModel.findTypeByGoodList(rxFragmen, page, bean.order, bean.where, C.GoodsListType.TAOQIANGGOU);

        }
        sendObservable(loadType, observable);
    }

    private void sendObservable(final int loadType, Observable<BaseResponse<List<ShopGoodInfo>>> observable) {
        if (observable == null) {
            getIView().showFinally();
            return;
        }
        observable.doFinally(new Action() {
            @Override
            public void run() throws Exception {
                getIView().showFinally();
            }
        }).subscribe(new DataObserver<List<ShopGoodInfo>>() {

            @Override
            protected void onDataListEmpty() {
                getIView().setRankingsError(loadType);
            }

            @Override
            protected void onSuccess(List<ShopGoodInfo> data) {
                getIView().setRankings(data, loadType);
                page++;
            }
        });
    }


    private void getThreeGoods(RxFragment rxFragmen, String keyword, BaseTitleTabBean bean, final int loadType) {
        if (loadType == C.requestType.initData) {
            mSearchType = 1;
            mMinId = "1";
            mPageString = "1";
        }

        RequestSearchBean requestBean = new RequestSearchBean();
        requestBean.setSort(bean.where);
        requestBean.setOrder(bean.order);
        requestBean.setPage(mPageString);
        requestBean.setCoupon("1");
        requestBean.setKeywords(keyword);
        requestBean.setMinId(mMinId);
        requestBean.setSearchType(mSearchType);
        RxHttp.getInstance().getGoodsService().getSearchGoodsList(requestBean)
                .compose(RxUtils.<BaseResponse<ShopGoodBean>>switchSchedulers())
                .compose(rxFragmen.<BaseResponse<ShopGoodBean>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().showFinally();
                    }
                })
                .subscribe(new DataObserver<ShopGoodBean>() {
                    @Override
                    protected void onSuccess(ShopGoodBean data) {
                        List<ShopGoodInfo> goodsList = data.getData();
                        if (goodsList != null && goodsList.size() != 0) {
                            mMinId = data.getMinId();
                            mPageString = data.getPage();
                            mSearchType = data.getSearchType();
                            getIView().setRankings(goodsList, loadType);
                        } else {
                            getIView().setRankingsError(loadType);
                        }
                    }
                });
    }


    /**
     * g根据品牌ID获取商品列表
     *
     * @return
     */
    public Observable<BaseResponse<List<ShopGoodInfo>>> getGoodsByBrand(RxFragment rxFragmen, BaseTitleTabBean bean) {
        return mModel.getGoodsByBrand(rxFragmen, page, bean.order, bean.where, bean.activity_id);

    }


    /**
     * 排行榜 v2
     *
     * @param
     * @param loadType
     * @param rankingTitleBean
     */
    public void getRankingsNews(RxFragment fragment, int loadType, RankingTitleBean rankingTitleBean) {
        if (loadType == C.requestType.initData) {
            page = 1;
        }
        rankingTitleBean.setPage(page);
        Observable<BaseResponse<List<ShopGoodInfo>>> observable = mModel.getRankingNews(fragment, rankingTitleBean);
        sendObservable(loadType, observable);
    }
}
