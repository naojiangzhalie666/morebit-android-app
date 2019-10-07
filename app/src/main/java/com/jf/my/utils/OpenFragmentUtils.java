package com.jf.my.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.jf.my.Module.common.Activity.SinglePaneActivity;
import com.jf.my.fragment.AppAboutFragment;
import com.jf.my.fragment.AppFeedBackFragment;
import com.jf.my.fragment.AppFeedBackSucessFragment;
import com.jf.my.fragment.BindWeixinFragment;
import com.jf.my.fragment.BindZhiFuBaoFragment;
import com.jf.my.fragment.CircleFeedBackFragment;
import com.jf.my.fragment.ConsComDetailListFragment;
import com.jf.my.fragment.CustomerServiceFragment;
import com.jf.my.fragment.GetMoneySucessFragment;
import com.jf.my.fragment.GoodsFeedBackFragment;
import com.jf.my.fragment.NewcomersFragment;
import com.jf.my.fragment.OfficialRecomFragment;
import com.jf.my.fragment.PanicBuyFragment;
import com.jf.my.fragment.RenameFragment;
import com.jf.my.fragment.SystemMsgDetailFragment;
import com.jf.my.goods.shopping.ui.LoadComplaintOrderActivity;
import com.jf.my.info.ui.OfficialNoticeFragment;
import com.jf.my.login.ui.LoginSinglePaneActivity;

/**
 * Created by Administrator on 2017/11/24.
 */

public class OpenFragmentUtils {

    /**
     * 根据fragment的名称返回fragment实例
     *
     * @param fragmentName
     * @return
     */
    public static Fragment getFragment(String fragmentName) {

        //消费佣金-收支明细
        if ("ConsComDetailListFragment".equals(fragmentName)) {
            return new ConsComDetailListFragment();
        }
        //消息详情
        else if ("SystemMsgDetailFragment".equals(fragmentName)) {
            return new SystemMsgDetailFragment();
        }
        //商品意见反馈
        else if ("GoodsFeedBackFragment".equals(fragmentName)) {
            return new GoodsFeedBackFragment();
        }
        //APP意见反馈
        else if ("AppFeedBackFragment".equals(fragmentName)) {
            return new AppFeedBackFragment();
        } else if ("CircleFeedBackFragment".equals(fragmentName)) {
            return new CircleFeedBackFragment();
        }
        //关于
        else if ("AppAboutFragment".equals(fragmentName)) {
            return new AppAboutFragment();
        }


        //专属客服
        else if ("CustomerServiceFragment".equals(fragmentName)) {
            return new CustomerServiceFragment();
        }
        //绑定支付宝
        else if ("BindZhiFuBaoFragment".equals(fragmentName)) {
            return new BindZhiFuBaoFragment();
        }

        //意见反馈-成功页面
        else if ("AppFeedBackSucessFragment".equals(fragmentName)) {
            return new AppFeedBackSucessFragment();
        }

        //淘抢购
        else if ("PanicBuyFragment".equals(fragmentName)) {
            return new PanicBuyFragment();
        }

        //修改昵称
        else if ("RenameFragment".equals(fragmentName)) {
            return new RenameFragment();
        }
        //新人课堂
        else if ("NewcomersFragment".equals(fragmentName)) {
            return new NewcomersFragment();
        }
        //绑定微信
        else if ("BindWeixinFragment".equals(fragmentName)) {
            return new BindWeixinFragment();
        }
        //官方推荐页面
        else if ("OfficialRecomFragment".equals(fragmentName)) {
            return new OfficialRecomFragment();
        }
        //提现成功
        else if ("GetMoneySucessFragment".equals(fragmentName)) {
            return new GetMoneySucessFragment();
        }//官方公告
        else if ("OfficialNoticeFragment".equals(fragmentName)) {
            return new OfficialNoticeFragment();
        }
        return null;
    }

    /**
     * 跳转到Fragment页面
     *
     * @param context
     */
    public static void goToSimpleFragment(Context context, String fragmentClass, Bundle extras) {
        SinglePaneActivity.start(context, fragmentClass, extras);
    }

    /**
     * 登录页面统一处理
     * 跳转到Fragment页面
     *
     * @param context
     */
    public static void goToLoginSimpleFragment(Context context, String fragmentClass, Bundle extras) {
        LoginSinglePaneActivity.start(context, fragmentClass, extras);
    }


    /**
     * 订单投诉
     * 返现页面统一处理
     * 跳转到Fragment页面
     *
     * @param context
     */
    public static void goToComplaintOrderFragment(Context context, String fragmentClass, Bundle extras) {
        LoadComplaintOrderActivity.start(context, fragmentClass, extras);
    }


}
