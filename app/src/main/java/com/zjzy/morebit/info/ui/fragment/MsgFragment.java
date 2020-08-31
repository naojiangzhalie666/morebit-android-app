package com.zjzy.morebit.info.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.Activity.SettingNotificationActivity;
import com.zjzy.morebit.App;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Fragment.BaseFragment;
import com.zjzy.morebit.Module.common.View.ReUseListView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.MsgDayHotAdapter;
import com.zjzy.morebit.info.contract.MsgDayHotContract;
import com.zjzy.morebit.info.presenter.MsgDayHotPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.CommonEmpty;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.DayHotBean;
import com.zjzy.morebit.pojo.FloorBean2;
import com.zjzy.morebit.pojo.NoticemBean;
import com.zjzy.morebit.pojo.RequestReadNotice;
import com.zjzy.morebit.pojo.UnreadInforBean;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.CommInterface;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.NotificationsUtils;
import com.zjzy.morebit.utils.OpenFragmentUtils;
import com.zjzy.morebit.utils.PageToUtil;
import com.zjzy.morebit.utils.SharedPreferencesUtils;
import com.zjzy.morebit.utils.TaobaoUtil;
import com.zjzy.morebit.utils.ViewShowUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.functions.Action;


/**
 * Created by fengrs on 2018/12/4.
 */

public class MsgFragment extends BaseFragment {
    private static final int REQUEST_COUNT = 10;
    @BindView(R.id.msgNoticationRl)
    RelativeLayout msgNoticationRl;
    private final static int fans = 2;
    private final static int sys = 1;
    private final static int all = 4;
    private int mNureadConut;
    private int page = 1;
    private MsgDayHotAdapter mAdapter;
    private TextView tv1, tv2, tv3, tv4, tv5,tv6;
    private ImageView img1, img2, img3, img4, img5,img6;
    private  UnreadInforBean mdata;

    private CommonEmpty mEmptyView;
    private TextView tv_shouyi, tv_fans, tv_system, tv_answer, tv_activity,tv_day;
    private LinearLayout activity_huo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View   mView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_msg, null);
        initView(mView);
        initData();
        return mView;
    }


    private void  initData(){

        CommInterface.getUserNoticeList(this)
                .subscribe(new DataObserver<NoticemBean>(false) {
                    @Override
                    protected void onDataListEmpty() {
                        onActivityFailure();
                    }

                    @Override
                    protected void onDataNull() {
                        onActivityFailure();
                    }

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        onActivityFailure();
                    }

                    @Override
                    protected void onSuccess(NoticemBean data) {
                        onGetListGraphicInfoSorting(data);
                    }
                });

    }

    private void onGetListGraphicInfoSorting(NoticemBean data) {
        if (data != null) {
            for (int i = 0; i < data.getList().size(); i++) {
                if (data.getList().get(i).getType() == 1) {
                    tv3.setText(data.getList().get(i).getMsg());
                } else if (data.getList().get(i).getType() == 2) {
                    tv2.setText(data.getList().get(i).getMsg());
                } else if (data.getList().get(i).getType() == 3) {
                    tv1.setText(data.getList().get(i).getMsg());
                } else if (data.getList().get(i).getType() == 4) {
                    tv4.setText(data.getList().get(i).getMsg());
                } else if (data.getList().get(i).getType() == 6) {
                    tv5.setText(data.getList().get(i).getMsg());
                }else if (data.getList().get(i).getType()==7){
                    tv6.setText(data.getList().get(i).getMsg());
                }
            }
        }
    }

    private void onActivityFailure() {

    }

    private void initView(View view) {
        tv_day=view.findViewById(R.id.tv_day);
        tv_shouyi = view.findViewById(R.id.tv_shouyi);
        tv_fans = view.findViewById(R.id.tv_fans);
        tv_system = view.findViewById(R.id.tv_system);
        tv_answer = view.findViewById(R.id.tv_answer);
        tv_activity = view.findViewById(R.id.tv_activity);
        tv_activity.getPaint().setFakeBoldText(true);
        tv_answer.getPaint().setFakeBoldText(true);
        tv_system.getPaint().setFakeBoldText(true);
        tv_fans.getPaint().setFakeBoldText(true);
        tv_shouyi.getPaint().setFakeBoldText(true);
        tv_day.getPaint().setFakeBoldText(true);
        activity_huo = view.findViewById(R.id.activity_huo);
        tv1 = view.findViewById(R.id.tv1);
        tv2 = view.findViewById(R.id.tv2);
        tv3 = view.findViewById(R.id.tv3);
        tv4 = view.findViewById(R.id.tv4);
        tv5 = view.findViewById(R.id.tv5);
        tv6 = view.findViewById(R.id.tv6);
        img1 = view.findViewById(R.id.img1);
        img2 = view.findViewById(R.id.img2);
        img3 = view.findViewById(R.id.img3);
        img4 = view.findViewById(R.id.img4);
        img5 = view.findViewById(R.id.img5);
        img6 = view.findViewById(R.id.img6);
        getNotice();

        initPush();

    }


    private void initPush() {
//        Bundle extras = getArguments();
//        if (extras != null&&mTablayout!=null) {
//            int inType = extras.getInt(C.Extras.pushType, 0);
//            switch (inType) {
//                case C.Push.sysMsg://系统消息
//                    mTablayout.setCurrentTab(0);
//                    break;
//                case C.Push.awardMsg://收益消息
//                    mTablayout.setCurrentTab(1);
//                    break;
//                default:
//                    break;
//            }
//        }


    }

    private void getNotice() {

        CommInterface.getUnreadInformation(this)
                .subscribe(new DataObserver<UnreadInforBean>(false) {
                    @Override
                    protected void onDataListEmpty() {
                        onActivityFailure();
                    }

                    @Override
                    protected void onDataNull() {
                        onActivityFailure();
                    }

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        onActivityFailure();
                    }

                    @Override
                    protected void onSuccess(UnreadInforBean data) {
                        onUnreadInformation(data);
                    }
                });
    }

    private void onUnreadInformation(UnreadInforBean data) {
        mdata=data;
        img1.setVisibility(data.isIncome() ? View.VISIBLE : View.GONE);
        img2.setVisibility(data.isFs() ? View.VISIBLE : View.GONE);
        img3.setVisibility(data.isSystem() ? View.VISIBLE : View.GONE);
        img4.setVisibility(data.isFeedback() ? View.VISIBLE : View.GONE);
        img5.setVisibility(data.isActivity() ? View.VISIBLE : View.GONE);
        img6.setVisibility(data.isShop() ? View.VISIBLE : View.GONE);

    }


    @OnClick({R.id.back, R.id.msgSetIv, R.id.openNoticationBtn, R.id.closeNoticationIv, R.id.earningLay, R.id.fansLay, R.id.sysLay, R.id.feedbackLay, R.id.activity_huo,R.id.activity_day})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                getActivity().finish();
                break;
            case R.id.msgSetIv:
                startActivity(new Intent(getActivity(), SettingNotificationActivity.class));
                break;
            case R.id.openNoticationBtn:
                NotificationsUtils.openSystemNotifications(getActivity());
                break;
            case R.id.closeNoticationIv:
                SharedPreferencesUtils.put(App.getAppContext(), C.sp.isShowMsgNotication, false);
                msgNoticationRl.setVisibility(View.GONE);
                break;
            case R.id.earningLay:

                OpenFragmentUtils.goToSimpleFragment(getActivity(), MsgEarningsFragment.class.getName(), new Bundle());
                break;
            case R.id.fansLay:

                OpenFragmentUtils.goToSimpleFragment(getActivity(), MsgFansFragment.class.getName(), new Bundle());
                break;
            case R.id.sysLay:

                OpenFragmentUtils.goToSimpleFragment(getActivity(), MsgSysFragment.class.getName(), new Bundle());
                break;
            case R.id.feedbackLay:

                OpenFragmentUtils.goToSimpleFragment(getActivity(), MsgFeedbackFragment.class.getName(), new Bundle());
                break;
            case R.id.activity_huo:

                OpenFragmentUtils.goToSimpleFragment(getActivity(), MsgActiityFragment.class.getName(), new Bundle());
                break;
            case R.id.activity_day:

                OpenFragmentUtils.goToSimpleFragment(getActivity(), MsgGoodsFragment.class.getName(), new Bundle());
                break;
            default:
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        checkNoticationEnable();
        getNotice();
    }




    private void checkNoticationEnable() {
        boolean isshow = (boolean) SharedPreferencesUtils.get(App.getAppContext(), C.sp.isShowMsgNotication, true);
        if (!NotificationsUtils.isNotificationEnabled(getActivity()) && isshow) {
            //显示消息通知提示
            msgNoticationRl.setVisibility(View.VISIBLE);
        } else {
            SharedPreferencesUtils.put(App.getAppContext(), C.sp.isShowMsgNotication, false);
            msgNoticationRl.setVisibility(View.GONE);
        }
    }


}
