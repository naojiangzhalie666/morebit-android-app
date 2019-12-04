package com.zjzy.morebit.goods.shopping.presenter;

import android.text.TextUtils;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.zjzy.morebit.goods.shopping.contract.GoodsDetailImgContract;
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
import com.trello.rxlifecycle2.components.support.RxFragment;

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

public class GoodsDetailImgPresenter extends MvpPresenter<ReturnCashModel, GoodsDetailImgContract.View> implements GoodsDetailImgContract.Present {
    public static int PASTDUETIEM = 30 * 60 * 1000; // 30分钟
    public static int LIST = 0;
    public static int REQUEST = 1;
    public static int WEB = 2;
    private GoodsImgDetailBean.Bean mGoodsImgC;
    /**
     * img 正则匹配
     */
     final Pattern imgPattern = Pattern.compile("<img.*src\\s*=\\s*(.*?)[^>]*?>", Pattern.CASE_INSENSITIVE);

    /**
     * src 正则匹配
     */
    final Pattern srcPattern = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)");
    private int mAnalysisFlag;

    @Override
    public void getModuleDescUrlData(final RxFragment fragment, final ShopGoodInfo goodsInfo, GoodsImgDetailBean picUrls, int analysisFlag) {
        mAnalysisFlag =analysisFlag;
        String moduleDescUrl = goodsInfo.moduleDescUrl;
        if (picUrls != null) {
            final GoodsImgDetailBean.Bean a = picUrls.getA();
            mGoodsImgC = picUrls.getC();
            List<String> listA = a.getContent();
            if (listA != null && listA.size() != 0) {
                // 有数据直接加载
                setImgListData(listA);
                return;
            }
        }
        // 加载h5
        if (TextUtils.isEmpty(moduleDescUrl)) {
            showImgWebUrl(goodsInfo.getItemSourceId());
            return;
        }
        loadModuleDescUrl(fragment, goodsInfo, moduleDescUrl);

    }

    /**
     *  加载url数据
     * @param fragment
     * @param goodsInfo
     * @param moduleDescUrl
     */
    private void loadModuleDescUrl(final RxFragment fragment, final ShopGoodInfo goodsInfo, String moduleDescUrl) {
        // 掉接口加载
        mModel.getprofileUrlObservable(fragment, moduleDescUrl)
                .map(new Function<String, ArrayList<String>>() {
                    @Override
                    public ArrayList<String> apply(String s) throws Exception {
                        ArrayList<String> strings = new ArrayList<>();
                        try {
                            if (mAnalysisFlag == 1){
                                putItemDetailPictureCode(fragment, goodsInfo, s);
                                return strings;
                            }
                            ModuleDescUrlBean taobaoImgDataBean = MyGsonUtils.jsonToBean(s, ModuleDescUrlBean.class);
                            if (taobaoImgDataBean == null ||
                                    taobaoImgDataBean.getData() == null ||
                                    taobaoImgDataBean.getData().getChildren() == null ||
                                    taobaoImgDataBean.getData().getChildren().size() == 0) {
                                putItemDetailPictureCode(fragment, goodsInfo, s);
                                return strings;
                            }
                            // 自己解析成功
                            List<TaobaoImgChildrenBeanChildren> children = taobaoImgDataBean.getData().getChildren();
                            for (TaobaoImgChildrenBeanChildren taobaoImgChildrenBeanChildren : children) {
                                TaobaoImgChildrenBeanParams params = taobaoImgChildrenBeanChildren.getParams();
                                if (params != null && !TextUtils.isEmpty(params.getPicUrl())) {
                                    String picUrl = params.getPicUrl();
                                    if (LoadImgUtils.isPicture(AppUtil.jointUrl(picUrl))) {
                                        strings.add(AppUtil.jointUrl(picUrl));
                                    }
                                }
                            }
                            if (strings != null || strings.size() != 0) {
                                saveGoodsImgHistory(AppUtil.listToString(strings), goodsInfo.getItemSourceId(), fragment, LIST);
                            } else {
                                putItemDetailPictureCode(fragment, goodsInfo, s);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            putItemDetailPictureCode(fragment, goodsInfo, s);
                        }
                        return strings;
                    }
                })
                .compose(RxUtils.<ArrayList<String>>switchSchedulers())
                .subscribe(new CallBackObserver<ArrayList<String>>() {

                    @Override
                    public void onNext(ArrayList<String> s) {
                        if (s.size() != 0) {
                            setImgListData(s);
                        }
                    }


                    @Override
                    public void onError(Throwable e) {
                        showImgWebUrl(goodsInfo.getItemSourceId());
                    }
                });
    }

    /**
     * 服务器解析url 图片资源
     *
     * @param fragment
     * @param goodsInfo
     * @param s
     */
    private void putItemDetailPictureCode(final RxFragment fragment, final ShopGoodInfo goodsInfo, String s) {
        mModel.getItemDetailPictureCode(fragment, s)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DataObserver<List<String>>(false) {

                    /**
                     * 成功回调
                     *
                     * @param data 结果
                     */
                    @Override
                    protected void onSuccess(List<String> data) {
                        if (data.size() != 0) {
                            setImgListData(data);
                            saveGoodsImgHistory(AppUtil.listToString(data), goodsInfo.getItemSourceId(), fragment, LIST);
                        } else {
                            showImgWebUrl(goodsInfo.getItemSourceId());
                        }
                    }

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        showImgWebUrl(goodsInfo.getItemSourceId());
                    }
                });
    }

    /**
     * 服务器解析 web 监听滴图片地址
     *
     * @param fragment
     * @param goodsInfo
     * @param s
     */
    private void putMtopJson(final RxFragment fragment, final ShopGoodInfo goodsInfo, String s) {
        mModel.getMtopjsonpObservable(fragment, s)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DataObserver<List<String>>(false) {

                    @Override
                    protected void onSuccess(List<String> data) {
                        if (data.size() != 0) {
                            setImgListData(data);
                            saveGoodsImgHistory(AppUtil.listToString(data), goodsInfo.getItemSourceId(), fragment, LIST);
                        }
                    }

                });
    }


    /**
     * a 路线  直接加载
     *
     * @param listA
     */
    private void setImgListData(List<String> listA) {
        getIView().showDeImgSuccess(listA);
    }

    private void showImgWebUrl(String itemSourceId) {
        if (mGoodsImgC != null) {
            getIView().showImgWebSuccess(mGoodsImgC.getContent().get(0));
        }
    }

    public void getImgData(final RxFragment fragment, final String imgRequestUrl, final ShopGoodInfo info) {
        Observable.create(new ObservableOnSubscribe<ArrayList<String>>() {

            /**
             * 解析
             * @param sval
             * @return
             */
            public ArrayList<String> mtopjsonpParse(String  sval) {
                ArrayList<String> srcs = Lists.newArrayList();
                Matcher matcher = imgPattern.matcher(sval);
                String img = "";
                while (matcher.find()) {
                    // 得到<img />数据
                    img = matcher.group();
                    // 匹配<img>中的src数据
                    Matcher m = srcPattern.matcher(img);
                    while (m.find()) {
                        if (LoadImgUtils.isPicture(AppUtil.jointUrl(m.group(1)))) {
                            srcs.add(AppUtil.jointUrl(m.group(1)));
                        }
                    }
                }
                return srcs;
            }

            @Override
            public void subscribe(ObservableEmitter<ArrayList<String>> e) throws Exception {
                OkHttpClient okClient = new OkHttpClient();
                Request request = new Request.Builder().url(imgRequestUrl).build();
                Response response = okClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    String s = response.body().string();
                    if (TextUtils.isEmpty(s)) {
                        e.onError(new Exception("response body = null"));
                    }
                    try {
                        if (mAnalysisFlag == 1){
                            putMtopJson(fragment, info, s);
                            return;
                        }
                        if (!s.startsWith("mtopjsonp1(")) {
                            putMtopJson(fragment, info, s);
                            return;
                        }
                        s = s.replace("mtopjsonp1(", "");
                        s = s.substring(0, s.length() - 1);
                        TaobaoWebImgDataBean taobaoWebImgDataBean = MyGsonUtils.jsonToBean(s, TaobaoWebImgDataBean.class);
                        if (taobaoWebImgDataBean == null ||
                                taobaoWebImgDataBean.getData() == null ||
                                taobaoWebImgDataBean.getData().getPcDescContent() == null) {
                            putMtopJson(fragment, info, s);
                            return;
                        }
                        String pcDescContent = taobaoWebImgDataBean.getData().getPcDescContent();
                        ArrayList<String> imgS = mtopjsonpParse(pcDescContent);
                        if (imgS == null || imgS.size() == 0) {
                            putMtopJson(fragment, info, s);
                            return;
                        }
                        saveGoodsImgHistory(AppUtil.listToString(imgS), info.getItemSourceId(), fragment, LIST);
                        imgS.size();
                        e.onNext(imgS);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        putMtopJson(fragment, info, s);
                    }

                }
            }
        })
                .compose(fragment.<ArrayList<String>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CallBackObserver<ArrayList<String>>() {
                    @Override
                    public void onNext(ArrayList<String> imgS) {
                        setImgListData(imgS);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }


    /**
     * 保存一条记录
     *
     * @param itemSourceId
     * @param fragment
     */
    private void saveGoodsImgHistory(String encodeStr, String itemSourceId, RxFragment fragment, int type) {
        if (TextUtils.isEmpty(encodeStr)) return;
        GoodsImgHistory goodsImgHistory = new GoodsImgHistory(itemSourceId, encodeStr, type, System.currentTimeMillis());
        if (DBManager.getInstance().getCount() < 5) { // 小于2条就插入
            List<GoodsImgHistory> pastDueGoods = DBManager.getInstance().getPastDueGoods(PASTDUETIEM);
            if (pastDueGoods != null && pastDueGoods.size() != 0) {// 超时上传
                pastDueGoods.add(goodsImgHistory);
                putDetailPictures(fragment, pastDueGoods);
            } else {// // 没有超时插入
                if (!TextUtils.isEmpty(encodeStr)) {
                    DBManager.getInstance().updateGoodsHistory(goodsImgHistory);
                }
            }
        } else {
            List<GoodsImgHistory> pastDueGoods = DBManager.getInstance().getAllGoodsHistoryList();
            if (pastDueGoods != null && pastDueGoods.size() != 0) {// 超时上传
                pastDueGoods.add(goodsImgHistory);
                putDetailPictures(fragment, pastDueGoods);
            }
        }
    }

    /**
     * 上传 保存下来的详情图片 资源
     * 定时定量  超过30分 超过2条都上传
     *
     * @param fragment
     * @param pastDueGoods
     */
    private void putDetailPictures(RxFragment fragment, List<GoodsImgHistory> pastDueGoods) {
        String str = "";
        if (pastDueGoods == null || pastDueGoods.size() == 0) return;
        try {
            Gson gson = new Gson();
            PastDueGoodsImgBean bean = new PastDueGoodsImgBean();
            ArrayList<PastDueGoodsImgDataBean> arrayList = new ArrayList<>();
            for (int i = 0; i < pastDueGoods.size(); i++) {
                PastDueGoodsImgDataBean goodsImgDataBean = new PastDueGoodsImgDataBean();
                GoodsImgHistory goodsImgHistory = pastDueGoods.get(i);
                if (goodsImgHistory != null) {
                    goodsImgDataBean.setItemSourceId(goodsImgHistory.getItemSourceId());
                    goodsImgDataBean.setPictures(goodsImgHistory.getContent());
                    arrayList.add(goodsImgDataBean);
                }
            }
            bean.setList(arrayList);
            bean.setSize(pastDueGoods.size());
            str = gson.toJson(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), str);
        RxHttp.getInstance().getCommonService().putDetailPictures(body)
                .compose(fragment.<BaseResponse<Boolean>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DataObserver<Boolean>(false) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    protected void onSuccess(Boolean data) {
                        if (data) {
                            DBManager.getInstance().deleteGoodsHistoryList();
                        }
                    }
                });
    }


}
