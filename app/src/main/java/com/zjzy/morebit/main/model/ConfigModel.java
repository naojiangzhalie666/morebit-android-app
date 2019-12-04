package com.zjzy.morebit.main.model;

import com.zjzy.morebit.mvp.base.frame.MvpModel;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.pojo.HotKeywords;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.SearchHotKeyBean;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.SystemConfigBean;
import com.zjzy.morebit.pojo.request.RequestConfigKeyBean;
import com.zjzy.morebit.pojo.request.RequestSearchGuideBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestKeyBean;
import com.zjzy.morebit.utils.C;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by fengrs
 * Data: 2018/8/28.
 */
public class ConfigModel extends MvpModel {

    private static ConfigModel sModel;

    public static ConfigModel getInstance() {
        if (sModel == null)
            sModel = new ConfigModel();
        return sModel;
    }

    public Observable<BaseResponse<HotKeywords>> getConfigForKey(RxAppCompatActivity activity, String key) {

        return RxHttp.getInstance().getCommonService().getConfigForKey(new RequestKeyBean().setKey(key))
                .compose(RxUtils.<BaseResponse<HotKeywords>>switchSchedulers())
                .compose(activity.<BaseResponse<HotKeywords>>bindToLifecycle());

//        return RxHttp.getInstance().getCommonService().getConfigForKey(key)
//                .compose(RxUtils.<BaseResponse<HotKeywords>>switchSchedulers())
//                .compose(activity.<BaseResponse<HotKeywords>>bindToLifecycle());
    }
    public Observable<BaseResponse<String>> getSystemCustomService(RxAppCompatActivity activity) {

        return RxHttp.getInstance().getCommonService().getSystemCustomService()
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(activity.<BaseResponse<String>>bindToLifecycle());

//        return RxHttp.getInstance().getCommonService().getConfigForKey(key)
//                .compose(RxUtils.<BaseResponse<HotKeywords>>switchSchedulers())
//                .compose(activity.<BaseResponse<HotKeywords>>bindToLifecycle());
    }


    public Observable<BaseResponse<List<ImageInfo>>> getSearchGuide(RxAppCompatActivity activity, int type) {
        RequestSearchGuideBean requestSearchGuideBean = new RequestSearchGuideBean();
        requestSearchGuideBean.setType(type);
        requestSearchGuideBean.setOs(C.Setting.os);
        return RxHttp.getInstance().getCommonService().getSearchGuideConfig(requestSearchGuideBean)
                .compose(RxUtils.<BaseResponse<List<ImageInfo>>>switchSchedulers())
                .compose(activity.<BaseResponse<List<ImageInfo>>>bindToLifecycle());
    }

    public Observable<BaseResponse<List<SearchHotKeyBean>>> getSearchKey(RxAppCompatActivity activity, int type) {
        RequestSearchGuideBean requestSearchGuideBean = new RequestSearchGuideBean();
        requestSearchGuideBean.setType(type);
        requestSearchGuideBean.setOs(C.Setting.os);
        return RxHttp.getInstance().getCommonService().getSearchKey(requestSearchGuideBean)
                .compose(RxUtils.<BaseResponse<List<SearchHotKeyBean>>>switchSchedulers())
                .compose(activity.<BaseResponse<List<SearchHotKeyBean>>>bindToLifecycle());
    }

    public Observable<BaseResponse<List<ShopGoodInfo>>> getTodayGoods(RxAppCompatActivity activity) {

        return RxHttp.getInstance().getCommonService().getTodayGoods()
                .compose(RxUtils.<BaseResponse<List<ShopGoodInfo>>>switchSchedulers())
                .compose(activity.<BaseResponse<List<ShopGoodInfo>>>bindToLifecycle());
    }

    public Observable<BaseResponse<SystemConfigBean>> getTodayTitle(RxAppCompatActivity activity, final String key) {
        RequestConfigKeyBean requestConfigKeyBean = new RequestConfigKeyBean();
        requestConfigKeyBean.setKey(key);
        return RxHttp.getInstance().getCommonService().getConfigByKey(requestConfigKeyBean)
                .compose(RxUtils.<BaseResponse<SystemConfigBean>>switchSchedulers())
                .compose(activity.<BaseResponse<SystemConfigBean>>bindToLifecycle());
    }
}
