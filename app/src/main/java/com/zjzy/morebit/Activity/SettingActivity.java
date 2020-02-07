package com.zjzy.morebit.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ali.auth.third.ui.context.CallbackContext;
import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.blankj.utilcode.util.ToastUtils;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Dialog.ClearSDdataDialog;
import com.zjzy.morebit.Module.common.Dialog.UnBingdingWechatDialog;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.address.ui.ManageGoodsAddressActivity;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.contact.SdDirPath;
import com.zjzy.morebit.info.model.WechatModel;
import com.zjzy.morebit.info.ui.SettingMineInfoActivity;
import com.zjzy.morebit.info.ui.SettingWechatActivity;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.CleanSdUtil;
import com.zjzy.morebit.utils.FileSizeUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.PageToUtil;
import com.zjzy.morebit.utils.SharedPreferencesUtils;
import com.zjzy.morebit.utils.TaobaoUtil;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.utils.fire.OssManage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;

import io.reactivex.functions.Action;


public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout item1_rl, item9_rl, item_tabao_rl;
    private TextView item_tv1, item_tv8, item_tv9, item_tv10, logout, item_tabao_tv;
    private RelativeLayout mModifyPassword;
    private ImageView mUserIcon;
    private UnBingdingWechatDialog mUnBingdingWechatDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        initViewData();
        OssManage.newInstance().initOss(null);
    }


    private void initView() {
        EventBus.getDefault().register(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityStyleUtil.initSystemBar(this, R.color.white); //设置标题栏颜色值
        } else {
            ActivityStyleUtil.initSystemBar(this, R.color.color_F8F8F8); //设置标题栏颜色值
        }
        item1_rl = (RelativeLayout) findViewById(R.id.item1_rl);
        mUserIcon = (ImageView) findViewById(R.id.userIcon);
        item9_rl = (RelativeLayout) findViewById(R.id.item9_rl);
        item_tabao_rl = (RelativeLayout) findViewById(R.id.item_tabao_rl);
        item1_rl.setOnClickListener(this);
        item9_rl.setOnClickListener(this);
        item_tabao_rl.setOnClickListener(this);
        findViewById(R.id.item8_rl).setOnClickListener(this);
        findViewById(R.id.item10_rl).setOnClickListener(this);
        findViewById(R.id.item11_rl).setOnClickListener(this);
        findViewById(R.id.item12_rl).setOnClickListener(this);
        findViewById(R.id.item13_rl).setOnClickListener(this);
        findViewById(R.id.rl_wx).setOnClickListener(this);
        findViewById(R.id.message_notification).setOnClickListener(this);
        findViewById(R.id.my_goods_address).setOnClickListener(this);
        item_tv1 = (TextView) findViewById(R.id.item_tv1);
        item_tv8 = (TextView) findViewById(R.id.item_tv8);
        item_tv9 = (TextView) findViewById(R.id.item_tv9);
        item_tv10 = (TextView) findViewById(R.id.item_tv10);
        logout = (TextView) findViewById(R.id.logout);
        item_tabao_tv = (TextView) findViewById(R.id.item_tabao_tv);

        logout.setOnClickListener(this);

        findViewById(R.id.btn_back).setOnClickListener(this);
        mModifyPassword = (RelativeLayout) findViewById(R.id.modify_password);
        mModifyPassword.setOnClickListener(this);
        findViewById(R.id.modify_phone_num).setOnClickListener(this);
    }

    private void initViewData() {
        UserInfo info = UserLocalData.getUser(this);

        try {
            if (info.getAliPayNumber() != null && !"".equals(info.getAliPayNumber())) {
                item_tv8.setText(info.getAliPayNumber());
            }
            if (!TextUtils.isEmpty(info.getHeadImg())) {
                LoadImgUtils.setImgCircle(this, mUserIcon, info.getHeadImg());
            } else {
                mUserIcon.setImageResource(R.drawable.head_icon); //显示图片
            }

            //淘宝授权
            if (info.isNeedAuth()) { //是否需要授权
                item_tabao_rl.setVisibility(View.VISIBLE);
            } else {
                item_tabao_rl.setVisibility(View.GONE);
            }
            if (AlibcLogin.getInstance().isLogin()) { //是否已经授权
                item_tabao_tv.setText("已授权");
            } else {
                item_tabao_tv.setText("未授权");
            }

            if (!TextUtils.isEmpty(info.getOauthWx())) {
                item_tv9.setText("已绑定");
                item_tv9.setTag(true);
            } else {
                item_tv9.setTag(false);
                item_tv9.setText("未绑定");
            }
            //计算缓存的容量
            item_tv10.setText(FileSizeUtil.getAutoFileOrFilesSize(SdDirPath.APK_CACHE_PATH));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe  //订阅事件
    public void onEventMainThread(MessageEvent event) {
        if (event.getAction().equals(EventBusAction.MAINPAGE_MYCENTER_REFRESH_DATA)) { //更新个人信息
            initViewData();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.item1_rl:
                // 个人
                startActivity(new Intent(this, SettingMineInfoActivity.class));
                break;


            case R.id.item8_rl:  //绑定支付宝
                PageToUtil.goToUserInfoSimpleFragment(SettingActivity.this, "绑定支付宝", "BindZhiFuBaoFragment");

                break;
            case R.id.item_tabao_rl:  //绑定淘宝
                if (AlibcLogin.getInstance().isLogin()) {
                    openTaobaoLogoutDialog();

                } else {
                    TaobaoUtil.authTaobao(new AlibcLoginCallback() {
                        @Override
                        public void onSuccess(int loginResult, String openId, String userNick) {
                            // 参数说明：
                            // loginResult(0--登录初始化成功；1--登录初始化完成；2--登录成功)
                            // openId：用户id
                            // userNick: 用户昵称
                            MyLog.d("test","loginresult:"+loginResult+"openId:"+openId+"userNick:"+userNick);
                            //授权成功回调
                            item_tabao_tv.setText("已授权");
                            ToastUtils.showShort("淘宝授权成功");

                        }

//                        @Override
//                        public void onSuccess(int i) {
//                            //授权成功回调
//                            item_tabao_tv.setText("已授权");
//                            ToastUtils.showShort("淘宝授权成功");
//                        }

                        @Override
                        public void onFailure(int code, String msg) {
                            ToastUtils.showShort("淘宝授权失败, 请重试");
                        }
                    });
                }
                break;
            case R.id.item9_rl: //绑定微信
                //去绑定微信
                if ((Boolean) item_tv9.getTag()) {
                    showUnBingdingWechatDialog();
                } else {
                    if (TextUtils.isEmpty(UserLocalData.getUser().getWxNumber())) {
                        SettingWechatActivity.start(this, 1);
                    } else {
                        new WechatModel(SettingActivity.this, new MyAction.OnResult<String>() {
                            @Override
                            public void invoke(String arg) {
                                ViewShowUtils.showShortToast(SettingActivity.this, SettingActivity.this.getString(R.string.binding_wechat_succeed));
                                EventBus.getDefault().post(new MessageEvent(EventBusAction.MAINPAGE_MYCENTER_REFRESH_DATA));
                            }

                            @Override
                            public void onError() {

                            }
                        }).authorize();
                    }
                }
                break;
            case R.id.message_notification: //消息通知
                startActivity(new Intent(this, SettingNotificationActivity.class));
                break;
            case R.id.my_goods_address:// 我的收货地址
                ManageGoodsAddressActivity.start(SettingActivity.this);
                break;
            case R.id.item10_rl: //清理缓存
                openCleanDataDialog();
                break;
            case R.id.logout: //退出登录
                openlogoutDialog();
                break;
            case R.id.modify_password:
                ModifyPasswordActivity.start(this, ModifyPasswordActivity.MODIFY_PASSWORD, UserLocalData.getUser(this).getPhone(), null);
                break;
            case R.id.modify_phone_num:
                ModifyPhoneNumAcitivity.start(this, "", 0);
                break;
            case R.id.rl_wx:     //管理微信 SettingWechatActivity
                SettingWechatActivity.start(this, 0);
                break;
            case R.id.item11_rl:
                Intent aboutIt = new Intent(this, OneFragmentDefaultActivity.class);
                Bundle aboutBundle = new Bundle();
                aboutBundle.putString("title", "关于我们");
                aboutBundle.putString("fragmentName", "AppAboutFragment");
                aboutIt.putExtras(aboutBundle);
                this.startActivity(aboutIt);

                break;
            case R.id.item12_rl:
                LoginUtil.getUserProtocolForHome(SettingActivity.this);
                break;

            case R.id.item13_rl:
                LoginUtil.getPrivateProtocolForHome(SettingActivity.this);
                break;
            default:
                break;
        }
    }

    private void untiedWechat() {
        LoadingView.showDialog(this);


        RxHttp.getInstance().getUsersService().untiedWechat()
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(this.<BaseResponse<String>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                })
                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onDataNull() {
                        onSuccess("");
                    }

                    @Override
                    protected void onSuccess(String data) {
                        UserLocalData.getUser().setOauthWx("");
                        if (item_tv9 != null) {
                            item_tv9.setTag(false);
                            item_tv9.setText("未绑定");
                            ViewShowUtils.showShortToast(SettingActivity.this, SettingActivity.this.getString(R.string.untied_wechat_succeed));
                        }
                    }
                });
    }

    private void showUnBingdingWechatDialog() {
        if (mUnBingdingWechatDialog == null) {
            mUnBingdingWechatDialog = new UnBingdingWechatDialog(this);
            mUnBingdingWechatDialog.setmCancelListener(new UnBingdingWechatDialog.OnCancelListner() {
                @Override
                public void onClick(View view) {
                    untiedWechat();
                }
            });
        }
        mUnBingdingWechatDialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    private ClearSDdataDialog clearDialog;

    private void openCleanDataDialog() {  //打开清理缓存
        clearDialog = new ClearSDdataDialog(SettingActivity.this, R.style.dialog, "提示", "", new ClearSDdataDialog.OnOkListener() {
            @Override
            public void onClick(View view, String text) {
                LoadingView.showDialog(SettingActivity.this, "删除中....");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            SharedPreferencesUtils.put(SettingActivity.this, C.sp.editTemplate, "");
                            File file = new File(SdDirPath.IMAGE_CACHE_PATH);
                            boolean directory = file.isDirectory();
                            if (!directory) {
                                file.delete();
                                CleanSdUtil.initCacheDirs(); //初始化文件目录
                            } else {
                                CleanSdUtil.deleteDir(file);
                            }
                        } catch (Exception e) {
                        }
                        SettingActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LoadingView.dismissDialog();
                                ViewShowUtils.showShortToast(SettingActivity.this, "清理缓存成功");
                                //计算缓存的容量
                                item_tv10.setText(FileSizeUtil.getAutoFileOrFilesSize(SdDirPath.APK_CACHE_PATH));
                            }
                        });
                    }
                }).start();
            }

        });
        clearDialog.show();
    }

    private ClearSDdataDialog logoutDialog;

    private void openlogoutDialog() {  //退出确认弹窗
        logoutDialog = new ClearSDdataDialog(SettingActivity.this, R.style.dialog, "提示", "确定退出登录吗?", new ClearSDdataDialog.OnOkListener() {
            @Override
            public void onClick(View view, String text) {
                LoginUtil.logout();
                SettingActivity.this.finish();

            }
        });
        logoutDialog.setOkTextAndColor(R.color.color_00A0EA, "退出");
        logoutDialog.setCancelTextAndColor(R.color.color_00A0EA, "点错了");

        logoutDialog.show();
    }

    private void openTaobaoLogoutDialog() {  //淘宝取消授权弹框
        ClearSDdataDialog taobaoDialog = new ClearSDdataDialog(SettingActivity.this, R.style.dialog, "温馨提示", "确定取消淘宝授权吗?", new ClearSDdataDialog.OnOkListener() {
            @Override
            public void onClick(View view, String text) {
                TaobaoUtil.logoutTaobao(new AlibcLoginCallback() {
                    @Override
                    public void onSuccess(int loginResult, String openId, String userNick) {
                        // 参数说明：
                        // loginResult(0--登录初始化成功；1--登录初始化完成；2--登录成功)
                        // openId：用户id
                        // userNick: 用户昵称
                        //授权成功回调
                        item_tabao_tv.setText("未授权");
                        ToastUtils.showShort("淘宝授权退出成功");

                    }

//                    @Override
//                    public void onSuccess(int i) { //取消授权成功
//                        item_tabao_tv.setText("未授权");
//                        ToastUtils.showShort("淘宝授权退出成功");
//
//                    }

                    @Override
                    public void onFailure(int i, String s) {
                        ToastUtils.showShort(s);
                    }
                });
            }
        });
        taobaoDialog.setOkTextAndColor(R.color.color_AFADA6, "确认");
        taobaoDialog.setCancelTextAndColor(R.color.color_DB2533, "点错了");

        taobaoDialog.show();
    }


    @Override
    public boolean isShowAllSeek() {
        return false;
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        CallbackContext.onActivityResult(requestCode, resultCode, data);
//    }
}
