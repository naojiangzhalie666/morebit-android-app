package com.zjzy.morebit.info.contract;


import com.zjzy.morebit.info.ui.MakeMoneyFragment;
import com.zjzy.morebit.mvp.base.base.BasePresenter;
import com.zjzy.morebit.mvp.base.base.BaseRcView;
import com.zjzy.morebit.pojo.ProtocolRuleBean;
import com.zjzy.morebit.pojo.myInfo.ApplyUpgradeBean;
import com.zjzy.morebit.pojo.myInfo.MakeMoenyBean;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

/**
 * Created by fengrs
 * Data: 2018/9/7.
 */
public class MakeMoenyContract {
    public interface View extends BaseRcView {
        /**
         * 申请升级
         *
         * @param data
         */
        void showApplyUpgradeDialog(ApplyUpgradeBean data);

        /**
         * 确认升级 成功
         *
         * @param data
         */
        void confirmUpgradeSuccess(String data);

        /**
         * @param data
         */
        void setMakeMoneyRuleData(MakeMoenyBean data);

        void setCommonProblemData(List<ProtocolRuleBean> data);
    }

    public interface Present extends BasePresenter {

        /**
         * 申请升级
         *
         * @param rxFragment
         */
        void applyUpgradeData(RxFragment rxFragment);

        /**
         * 确认升级
         *
         * @param makeMoneyFragment
         */
        void confirmUpgrade(MakeMoneyFragment makeMoneyFragment);


        /**
         * 赚钱三部曲
         *
         * @param makeMoneyFragment
         */
        void getMakeMoneyRuleData(MakeMoneyFragment makeMoneyFragment);

        /**
         * 常见问题
         *
         * @param makeMoneyFragment
         */

        void getCommonProblemData(MakeMoneyFragment makeMoneyFragment);
    }
}
