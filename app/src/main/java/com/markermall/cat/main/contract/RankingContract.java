package com.markermall.cat.main.contract;

import com.markermall.cat.mvp.base.base.BasePresenter;
import com.markermall.cat.mvp.base.base.BaseRcView;
import com.markermall.cat.pojo.ShopGoodInfo;
import com.markermall.cat.pojo.UI.BaseTitleTabBean;
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
