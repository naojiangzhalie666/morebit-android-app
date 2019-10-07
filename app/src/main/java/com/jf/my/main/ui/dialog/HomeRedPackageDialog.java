package com.jf.my.main.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jf.my.App;
import com.jf.my.MainActivity;
import com.jf.my.R;
import com.jf.my.network.BaseResponse;
import com.jf.my.network.RxHttp;
import com.jf.my.network.RxUtils;
import com.jf.my.network.observer.DataObserver;
import com.jf.my.pojo.ImageInfo;
import com.jf.my.pojo.Records;
import com.jf.my.pojo.request.RequestPopupBean;
import com.jf.my.utils.C;
import com.jf.my.utils.GlideImageLoader;
import com.jf.my.utils.MyLog;
import com.jf.my.utils.SensorsDataUtil;
import com.jf.my.utils.UI.BannerInitiateUtils;
import com.jf.my.utils.action.MyAction;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品搜索弹窗
 */

public class HomeRedPackageDialog extends Dialog implements View.OnClickListener {

    private List<ImageInfo> mData;
    private Context mContext;
    private Banner mRoll_view_pager;

    public HomeRedPackageDialog(Context context, List<ImageInfo> arg) {
        super(context, R.style.dialog);
        this.mContext = context;
        this.mData = arg;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_hone_red_package);
        setCanceledOnTouchOutside(false);
        mRoll_view_pager = (Banner) findViewById(R.id.roll_view_pager);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setGoodsAdImg(mData);
    }

    /**
     * 设置商品轮播图
     *
     * @param info
     */
    private void setGoodsAdImg(final List<ImageInfo> info) {
        if (info == null || info.size() == 0) {
            return;
        }
        final List<String> imgUrls = new ArrayList<>();
        for (int i = 0; i < info.size(); i++) {
            ImageInfo imageInfo = info.get(i);
            imgUrls.add(imageInfo.getThumb());
        }
        //简单使用
        mRoll_view_pager.setImages(imgUrls)
                .setImageLoader(new GlideImageLoader( R.color.transparent))
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        ImageInfo imageInfo = info.get(position);
                        SensorsDataUtil.getInstance().advClickTrack(imageInfo.getTitle(),imageInfo.getId()+"",imageInfo.getOpen()+"","首页活动弹窗",0,imageInfo.getClassId()+"",imageInfo.getUrl());
                        BannerInitiateUtils.gotoAction((Activity) mContext, imageInfo);
                        HomeRedPackageDialog.this.dismiss();
                    }
                })
                .isAutoPlay(true)
                .setDelayTime(4000)
                .start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.cancel:
                this.dismiss();
                break;
        }
    }

    public static void getData(MainActivity mainActivity, final MyAction.OnResult<List<ImageInfo>> action) {
        List<Records> recordses = (ArrayList<Records>) App.getACache().getAsObject(C.sp.homeRedPagckageRecord);
        RequestPopupBean bean = new RequestPopupBean();
        bean.setType(C.UIShowType.Popup);
        bean.setRecords(recordses);
        RxHttp.getInstance().getCommonService().getPopup(bean)
                .compose(RxUtils.<BaseResponse<List<ImageInfo>>>switchSchedulers())
                .compose(mainActivity.<BaseResponse<List<ImageInfo>>>bindToLifecycle())

                .subscribe(new DataObserver<List<ImageInfo>>(false) {
                    @Override
                    protected void onSuccess(List<ImageInfo> data) {
                        action.invoke(data);
                        List<Records> records = data.get(0).getRecords();
                        MyLog.i("test","times: " +records);
                        Log.d("RequestPopupBean", "onError: records  "+records);
                        if (records!=null&&records.size()>0) {
                            App.getACache().put(C.sp.homeRedPagckageRecord,  (ArrayList)records);
                        }
                    }

                });
    }



}