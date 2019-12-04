package com.zjzy.morebit.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.zjzy.morebit.App;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Dialog.NotificationDialog;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.Module.push.PushAction;
import com.zjzy.morebit.R;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.PushSwitchBean;
import com.zjzy.morebit.pojo.request.RequestPushBean;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.NotificationsUtils;
import com.zjzy.morebit.view.ToolbarHelper;

import butterknife.BindView;

/**
 * @Description: 通知消息设置 (简单页面, 无需MVP模式)
 * @Author: liys
 * @CreateDate: 2019/3/15 11:14
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/3/15 11:14
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class SettingNotificationActivity extends BaseActivity {

    @BindView(R.id.messageTxt)
    TextView mMessageTxt;
    @BindView(R.id.message_switch)
    Switch mMessageSwitch;
    @BindView(R.id.profit_switch)
    Switch mProfitSwitch;
    @BindView(R.id.fans_switch)
    Switch mFansSwitch;
    @BindView(R.id.share_goods_switch)
    Switch share_goods_switch;
    @BindView(R.id.rl_share_goods)
    RelativeLayout rl_share_goods;

    @BindView(R.id.hot_switch)
    Switch mHotSwitch;

    //开关当前状态(与服务端同步)
    private boolean isProfitPush = true; //消息推送
    private boolean isFansPush = true; //粉丝推送
    private boolean isGoodsPush = true; //粉丝推送
    private String mIsOpenHotMsgPush;
    private HosSwitch mHosSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_notification);
        init();
    }

    private void init() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .statusBarColor(R.color.white)
                .fitsSystemWindows(true)
                .init();
        //设置标题栏
        new ToolbarHelper(this).setToolbarAsUp().setCustomTitle(getString(R.string.message_notification));
        mMessageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mMessageSwitch.setChecked(false);
                NotificationsUtils.openSystemNotifications(SettingNotificationActivity.this);
//                showDialog();
            }
        });

        if(UserLocalData.getUser().getReleasePermission()==1){
            rl_share_goods.setVisibility(View.VISIBLE);
        } else {
            rl_share_goods.setVisibility(View.GONE);
        }
        if (NotificationsUtils.isNotificationEnabled(this)) {
            getSwitch();
        }
        mIsOpenHotMsgPush = App.getACache().getAsString(UserLocalData.getUser().getPhone()+C.sp.isOpenHotMsgPush);
        mHotSwitch.setChecked(TextUtils.isEmpty(mIsOpenHotMsgPush));
        mHosSwitch = new HosSwitch();
        mHotSwitch.setOnClickListener(mHosSwitch);
    }

    private void setOpenHotPush(boolean isOpenHotMsgPush) {
        if (isOpenHotMsgPush) {
            PushAction.deleteTags(getApplicationContext(), C.Push.tag_everydayHotCommodity);
        } else {
            PushAction.addTags(getApplicationContext(), C.Push.tag_everydayHotCommodity);
        }
    }
    private class  HosSwitch implements View.OnClickListener{

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            setOpenHotPush(TextUtils.isEmpty(mIsOpenHotMsgPush));
            mIsOpenHotMsgPush = TextUtils.isEmpty(mIsOpenHotMsgPush) ? "1" : "";
            App.getACache().put(UserLocalData.getUser().getPhone()+C.sp.isOpenHotMsgPush, mIsOpenHotMsgPush);

        }
    }



    /**
     * 设置开关
     */
    private void setSwitchCheck(final Switch switchCheck, boolean isChecked) {
        MyLog.i("test","switchCheck: " +switchCheck +" isChecked: " +isChecked );
        switchCheck.setOnCheckedChangeListener(null);
        switchCheck.setChecked(isChecked);
        switchCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MyLog.i("test","isChecked: " +isChecked +" switchCheck: " +switchCheck +" isProfitPush: " +isProfitPush);
                switchCheck.setChecked(!isChecked);
                if (switchCheck == mProfitSwitch) {
                    editJpush(isChecked, isFansPush,isGoodsPush);
                } else if(switchCheck == mFansSwitch) {
                    editJpush( isProfitPush,isChecked,isGoodsPush);
                } else if(switchCheck == share_goods_switch){
                    editJpush(isProfitPush, isFansPush,isChecked);
                }
            }
        });
    }

    private void showDialog() {
        NotificationDialog dialog = new NotificationDialog(SettingNotificationActivity.this, R.style.dialog)
                .setOnListner(new NotificationDialog.OnListener() {
                    @Override
                    public void onClick(int type) {
                        if (type == NotificationDialog.OnListener.RIGHT) {
//                        NotificationsUtils.openSystemSetting(SettingNotificationActivity.this);
                            NotificationsUtils.openSystemNotifications(SettingNotificationActivity.this);
                        }
                    }
                });
        dialog.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        switchRefresh();
    }

    /**
     * 刷新状态
     */
    public void switchRefresh() {
        if (NotificationsUtils.isNotificationEnabled(this)) {
            setSwitchCheck(mProfitSwitch, isProfitPush);
            setSwitchCheck(mFansSwitch, isFansPush);
            setSwitchCheck(share_goods_switch,isGoodsPush);
            mFansSwitch.setEnabled(true);
            mProfitSwitch.setEnabled(true);
            share_goods_switch.setEnabled(true);
            mHotSwitch.setEnabled(true);
            mHotSwitch.setOnClickListener(mHosSwitch);
            if (TextUtils.isEmpty(mIsOpenHotMsgPush)) {
                mHotSwitch.setChecked(true);
                setOpenHotPush(false);
            }
            mMessageSwitch.setVisibility(View.GONE);
            mMessageTxt.setVisibility(View.VISIBLE);
        } else {
            showDialog(); //弹框获取权限
            setSwitchCheck(mFansSwitch, false);
            setSwitchCheck(mProfitSwitch, false);
            setSwitchCheck(share_goods_switch, false);
            mFansSwitch.setEnabled(false);
            mProfitSwitch.setEnabled(false);
            share_goods_switch.setEnabled(false);
            mHotSwitch.setChecked(false);
            mHotSwitch.setOnClickListener(null);

            mHotSwitch.setEnabled(false);
            setOpenHotPush(true);
            mMessageSwitch.setVisibility(View.VISIBLE);
            mMessageTxt.setVisibility(View.GONE);
        }
    }

    /**
     * 开关设置
     *
     * @param isFansChecked
     * @param isProfitChecked
     */
    private void editJpush(final boolean isProfitChecked, final boolean isFansChecked,final  boolean isGoodsChecked) {
       MyLog.i("test","isProfitChecked: " +isProfitChecked +" isFansChecked: " +isFansChecked+" isGoodsChecked"+isGoodsChecked);
        LoadingView.showDialog(this);
        RequestPushBean bean = new RequestPushBean(isFansChecked, isProfitChecked,isGoodsChecked);
        RxHttp.getInstance().getUsersService().editJpush(bean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(this.<BaseResponse<String>>bindToLifecycle())
                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        LoadingView.dismissDialog();
                    }

                    @Override
                    protected void onDataNull() {
                        onSuccess("");
                    }

                    @Override
                    protected void onSuccess(String data) {
                        LoadingView.dismissDialog();
                        isProfitPush = isProfitChecked;
                        isFansPush = isFansChecked;
                        isGoodsPush = isGoodsChecked;
                        MyLog.i("test","isProfitPush: " +isProfitPush + " isFansPush: " + " isGoodsPush: " +isGoodsPush );
                        setSwitchCheck(mProfitSwitch, isProfitChecked);
                        setSwitchCheck(mFansSwitch, isFansChecked);
                        setSwitchCheck(share_goods_switch, isGoodsChecked);
                        ToastUtils.showShort("修改成功");
                    }
                });
    }

    public void getSwitch() {
        LoadingView.showDialog(this);
        RxHttp.getInstance().getUsersService().getPushSwitch()
                .compose(RxUtils.<BaseResponse<PushSwitchBean>>switchSchedulers())
                .compose(this.<BaseResponse<PushSwitchBean>>bindToLifecycle())
                .subscribe(new DataObserver<PushSwitchBean>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        LoadingView.dismissDialog();
                    }

                    @Override
                    protected void onSuccess(PushSwitchBean data) {
                        LoadingView.dismissDialog();
                        if (data != null) {
                            isProfitPush = (data.getIncomeMessage() == 1);
                            isFansPush = (data.getFansMessage() == 1);
                            isGoodsPush = (data.getShareItemMessage()== 1);

                            setSwitchCheck(mProfitSwitch, isProfitPush);
                            setSwitchCheck(mFansSwitch, isFansPush);
                            setSwitchCheck(share_goods_switch, isGoodsPush);
                        }
                    }
                });
    }
}
