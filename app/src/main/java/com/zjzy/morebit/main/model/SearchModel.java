package com.zjzy.morebit.main.model;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.Module.push.Logger;
import com.zjzy.morebit.main.ui.CollectFragment2;
import com.zjzy.morebit.mvp.base.frame.MvpModel;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.pojo.ProgramCatItemBean;
import com.zjzy.morebit.pojo.RankingTitleBean;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.address.AllRegionInfoList;
import com.zjzy.morebit.pojo.request.RequestByGoodList;
import com.zjzy.morebit.pojo.request.RequestCollectionListBean;
import com.zjzy.morebit.pojo.request.RequestMaterial;
import com.zjzy.morebit.pojo.requestbodybean.RequestDeleteCollection;
import com.zjzy.morebit.pojo.requestbodybean.RequestGetActivitiesDetails;
import com.zjzy.morebit.pojo.requestbodybean.RequestGetGoodsByBrand;
import com.zjzy.morebit.pojo.requestbodybean.RequestGetNinepinkageGoods;
import com.zjzy.morebit.pojo.requestbodybean.RequestGetRankings;
import com.zjzy.morebit.pojo.requestbodybean.RequestGoodsLike;
import com.zjzy.morebit.pojo.requestbodybean.RequestShopId;
import com.zjzy.morebit.utils.MyGsonUtils;
import com.zjzy.morebit.utils.encrypt.EncryptUtlis;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * Created by fengrs
 * Data: 2018/8/28.
 */
public class SearchModel extends MvpModel {





}
