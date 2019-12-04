package com.zjzy.morebit.Module.service;

import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.pojo.goods.AnalysisBean1;
import com.zjzy.morebit.pojo.goods.GoodsDetalisImgBean;
import com.zjzy.morebit.pojo.goods.TaobaoGoodBean;
import com.zjzy.morebit.pojo.goods.TaobaoGoodImgBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by fengrs on 2018/6/14 17:27
 * 微信 service
 */

public interface WXService {


    /**
     * 獲取微信信息
     *
     * @return
     */
    @GET("/sns/userinfo")
    public Observable<String> getWxUserInfo(
            @Query("access_token") String access_token,
            @Query("openid") String openid);

    /**
     * 获取淘宝搜索关键字
     * https://suggest.taobao.com/sug?code=utf-8&q=" + string + "&k=1&area=c2c";
     * @return
     */
    @GET
    public Observable<String> profilePicture(@Url String url);

    /**
     * 解析淘口令
     *http://pagoda.gzmiyuan.com/WirelessShareTpwdQueryRequest.php?tkl=%EF%BF%A5QY4ebeJbZyE%EF%BF%A5
     * @return
     */
    @GET("/api/goods/getAnalysis/")
    public Observable<BaseResponse<AnalysisBean1>> getAnalysis(
            @Query("tkl") String tkl
    );

    @GET
    public Observable<TaobaoGoodBean> getTaobaoData(@Url String url);

    @GET
    public Observable<TaobaoGoodImgBean> getTaobaoDetalisImg(@Url String url);
    /**
     * 限时抢购
     *
     * @param page
     * @param hour_type 时间类型
     * @return
     * "jsv", "2.4.11");
    "api", ",mtop.taobao.d
    "v", "6.0");
    "timeout", "1000");
    "data", "{\"id\":\"" +
    "t",  current.toString
     * https://hws.m.taobao.com/cache/desc/5.0?id=579138014428
     */
    @POST("/cache/desc/5.0")
    @FormUrlEncoded
    public Observable<GoodsDetalisImgBean> getFlashSale(
            @Field("id") String taobao

    );
}
