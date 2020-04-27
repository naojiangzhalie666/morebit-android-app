package com.zjzy.morebit.main.contract;

import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.mvp.base.base.BasePresenter;
import com.zjzy.morebit.mvp.base.base.BaseRcView;
import com.zjzy.morebit.pojo.ProgramCatItemBean;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UI.BaseTitleTabBean;
import com.zjzy.morebit.pojo.pddjd.JdPddProgramItem;

import java.util.List;

/**
 * Created by fengrs on 2018/9/10.
 */

public class PddContract {
    public interface View extends BaseRcView {


        void setPdd(List<ShopGoodInfo> data, int loadType);

        void setPddError(int loadType);

        void setJd(List<ShopGoodInfo> data, int loadType);

        void setJdError(int loadType);
    }

    public interface Present extends BasePresenter {

        void getJdPddGoodsList(RxFragment rxFragmen, ProgramCatItemBean programCatItemBean, int loadType);

        void getJdGoodsList(RxFragment rxFragmen, ProgramCatItemBean programCatItemBean, int loadType);

    }
}
