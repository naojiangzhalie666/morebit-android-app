package com.zjzy.morebit.main.contract;

import com.zjzy.morebit.mvp.base.base.BasePresenter;
import com.zjzy.morebit.mvp.base.base.BaseRcView;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UI.BaseTitleTabBean;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

/**
 * Created by fengrs on 2018/9/10.
 */

public class RankingContract {
    public interface View extends BaseRcView {


        void setRankings(List<ShopGoodInfo> data, int loadType);

        void setRankingsError( int loadType);
    }

    public interface Present extends BasePresenter {

        void getRankings(RxFragment rxFragmen, int type, int loadType, BaseTitleTabBean baseTitleTabBean);
    }
}
