package com.markermall.cat.utils;

import com.markermall.cat.network.BaseResponse;
import com.markermall.cat.network.RxHttp;
import com.markermall.cat.network.RxUtils;
import com.markermall.cat.network.observer.DataObserver;
import com.markermall.cat.pojo.request.RequestVideoId;


/**
 * @author YangBoTian
 * @date 2019/9/5
 * @des 点击上传次数
 */
public class UploadingOnclickUtils {
    public static final int TYPE_BROWS_ONCLICK = 1;//浏览点击
    public static final int TYPE_SHARE_ONCLICK = 2;//分享点击

    /**
     * 商学院资讯点击上报
     */
    public static void updateThemeClicks(final int id, int type) {
        final RequestVideoId requestVideoId = new RequestVideoId(id);
        requestVideoId.setType(type);
        RxHttp.getInstance().getSysteService()
                .updateThemeClicks(requestVideoId)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onSuccess(String data) {
                        MyLog.i("test", "上报成功 -- " + id);
                    }
                });
    }

    /**
     * 文章/视频点击量上传
     *
     * @param rxFragment
     * @param id
     * @param type
     */
    public static void mp4Browse(final int id, int type) {
        final RequestVideoId requestVideoId = new RequestVideoId(id);
        requestVideoId.setType(type);
        RxHttp.getInstance().getSysteService()
                .mp4Browse(requestVideoId)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onSuccess(String data) {

                    }
                });
    }


}
