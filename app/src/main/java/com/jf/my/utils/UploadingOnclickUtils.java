package com.jf.my.utils;

import com.jf.my.network.BaseResponse;
import com.jf.my.network.RxHttp;
import com.jf.my.network.RxUtils;
import com.jf.my.network.observer.DataObserver;
import com.jf.my.pojo.request.RequestVideoId;
import com.trello.rxlifecycle2.components.support.RxFragment;


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
