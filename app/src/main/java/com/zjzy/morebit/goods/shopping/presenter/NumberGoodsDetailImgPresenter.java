package com.zjzy.morebit.goods.shopping.presenter;

import android.text.TextUtils;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.goods.shopping.contract.GoodsDetailImgContract;
import com.zjzy.morebit.goods.shopping.contract.NumberGoodsDetailImgContract;
import com.zjzy.morebit.goods.shopping.model.NumberGoodsDetailContentModel;
import com.zjzy.morebit.goods.shopping.model.ReturnCashModel;
import com.zjzy.morebit.mvp.base.frame.MvpPresenter;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.CallBackObserver;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ModuleDescUrlBean;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.TaobaoImgChildrenBeanChildren;
import com.zjzy.morebit.pojo.TaobaoImgChildrenBeanParams;
import com.zjzy.morebit.pojo.TaobaoWebImgDataBean;
import com.zjzy.morebit.pojo.goods.GoodsImgDetailBean;
import com.zjzy.morebit.pojo.goods.PastDueGoodsImgBean;
import com.zjzy.morebit.pojo.goods.PastDueGoodsImgDataBean;
import com.zjzy.morebit.pojo.grenndao.GoodsImgHistory;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MyGsonUtils;
import com.zjzy.morebit.utils.dao.DBManager;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by fengrs on 2018/11/3.
 */

public class NumberGoodsDetailImgPresenter extends MvpPresenter<NumberGoodsDetailContentModel, NumberGoodsDetailImgContract.View> implements NumberGoodsDetailImgContract.Present {



}
