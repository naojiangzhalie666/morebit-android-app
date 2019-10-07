package com.markermall.cat.main.contract;

import com.markermall.cat.Module.common.Activity.BaseActivity;
import com.markermall.cat.mvp.base.base.BasePresenter;
import com.markermall.cat.mvp.base.base.BaseRcView;
import com.markermall.cat.pojo.ImageInfo;

import java.util.List;

/**
 * @Author: wuchaowen
 * @Description:
 **/
public class NoticeContract {
    public interface View extends BaseRcView {
        //刷新数据
        void refreshData(List<ImageInfo> data);
        //加载更多数据
        void moreData(List<ImageInfo> data);
        //没有数据
        void showNoData();
        void refreshDateFail();
    }

    public interface Present extends BasePresenter {
          void getNoticeList(BaseActivity activity,boolean isRefresh,int type);
    }
}
