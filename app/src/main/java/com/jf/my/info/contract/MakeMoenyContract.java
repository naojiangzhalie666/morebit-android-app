package com.jf.my.info.contract;


import com.jf.my.info.ui.MakeMoneyFragment;
import com.jf.my.mvp.base.base.BasePresenter;
import com.jf.my.mvp.base.base.BaseRcView;
import com.jf.my.pojo.ProtocolRuleBean;
import com.jf.my.pojo.myInfo.ApplyUpgradeBean;
import com.jf.my.pojo.myInfo.MakeMoenyBean;
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
