package com.zjzy.morebit.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ali.auth.third.ui.context.CallbackContext;
import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.blankj.utilcode.util.ToastUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Dialog.ClearSDdataDialog;
import com.zjzy.morebit.Module.common.Dialog.UnBingdingWechatDialog;
import com.zjzy.morebit.Module.common.Dialog.WxBindDialog;
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
import com.zjzy.morebit.pojo.request.RequestNickNameBean;
import com.zjzy.morebit.pojo.request.RequestUpdateHeadPortraitBean;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.CleanSdUtil;
import com.zjzy.morebit.utils.FileSizeUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.PageToUtil;
import com.zjzy.morebit.utils.ReadImgUtils;
import com.zjzy.morebit.utils.SharedPreferencesUtils;
import com.zjzy.morebit.utils.TaobaoUtil;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.utils.encrypt.EncryptUtlis;
import com.zjzy.morebit.utils.fire.OssManage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Action;


public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout item1_rl, item9_rl, item_tabao_rl,accountsecurity,rl_head,rl_nickname,rl_sex;
    private TextView item_tv1, item_tv8, item_tv9, item_tv10, logout, item_tabao_tv,txt_head_title,tv_banben,
            tv_nickname,tv_sex;
    private RelativeLayout mModifyPassword;
    private ImageView userIcon2;
    private UnBingdingWechatDialog mUnBingdingWechatDialog;
    private LinearLayout ll_weichat;
    private OptionsPickerView<String> mPvCustomOptions;
    private UserInfo info;

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
        tv_nickname= (TextView) findViewById(R.id.tv_nickname);
        tv_sex= (TextView) findViewById(R.id.tv_sex);
        txt_head_title= (TextView) findViewById(R.id.txt_head_title);
        item1_rl = (RelativeLayout) findViewById(R.id.item1_rl);
        userIcon2 = (ImageView) findViewById(R.id.userIcon2);
        item9_rl = (RelativeLayout) findViewById(R.id.item9_rl);
        item_tabao_rl = (RelativeLayout) findViewById(R.id.item_tabao_rl);
        rl_head= (RelativeLayout) findViewById(R.id.rl_head);
        rl_nickname= (RelativeLayout) findViewById(R.id.rl_nickname);
        rl_sex= (RelativeLayout) findViewById(R.id.rl_sex);
        rl_sex.setOnClickListener(this);
        rl_nickname.setOnClickListener(this);
        rl_head.setOnClickListener(this);
        item1_rl.setOnClickListener(this);
        item9_rl.setOnClickListener(this);
        item_tabao_rl.setOnClickListener(this);
        findViewById(R.id.item8_rl).setOnClickListener(this);
        findViewById(R.id.item10_rl).setOnClickListener(this);
        findViewById(R.id.item11_rl).setOnClickListener(this);

        findViewById(R.id.rl_wx).setOnClickListener(this);
        findViewById(R.id.message_notification).setOnClickListener(this);
        findViewById(R.id.my_goods_address).setOnClickListener(this);
        item_tv1 = (TextView) findViewById(R.id.item_tv1);
        item_tv8 = (TextView) findViewById(R.id.item_tv8);
        item_tv9 = (TextView) findViewById(R.id.item_tv9);
        item_tv10 = (TextView) findViewById(R.id.item_tv10);
        logout = (TextView) findViewById(R.id.logout);
        item_tabao_tv = (TextView) findViewById(R.id.item_tabao_tv);
        tv_banben= (TextView) findViewById(R.id.tv_banben);
        accountsecurity= (RelativeLayout) findViewById(R.id.accountsecurity);
        accountsecurity.setOnClickListener(this);
        logout.setOnClickListener(this);

        findViewById(R.id.btn_back).setOnClickListener(this);
        mModifyPassword = (RelativeLayout) findViewById(R.id.modify_password);
        mModifyPassword.setOnClickListener(this);
        findViewById(R.id.modify_phone_num).setOnClickListener(this);
        ll_weichat = (LinearLayout) findViewById(R.id.ll_weichat);

        try {
            tv_banben.setText("V" + AppUtil.getVersionName(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initViewData() {
          info = UserLocalData.getUser(this);

        try {
            if (info.getAliPayNumber() != null && !"".equals(info.getAliPayNumber())) {
                item_tv8.setText(info.getAliPayNumber());
            }
            if (!TextUtils.isEmpty(info.getHeadImg())) {
                LoadImgUtils.setImgCircle(this, userIcon2, info.getHeadImg());
            } else {
                userIcon2.setImageResource(R.drawable.head_icon); //显示图片
            }
            if (info.getNickName() != null && !"".equals(info.getNickName())) {
                tv_nickname.setText(info.getNickName());
            }
            if (info.getSex() != 0) {
                tv_sex.setText(info.getSex() == 1 ? "男" : "女");
            }
            if (!TextUtils.isEmpty(info.getWxNumber())){
                item_tv9.setText(info.getWxNumber()+"");
            }





//            if (C.UserType.operator.equals(info.getUserType())){
//                ll_weichat.setVisibility(View.VISIBLE);
//            }else{
//                ll_weichat.setVisibility(View.GONE);
//            }
            //淘宝授权
            if (info.isNeedAuth()) { //是否需要授权
                item_tabao_rl.setVisibility(View.GONE);
            } else {
                item_tabao_rl.setVisibility(View.GONE);
            }
            if (AlibcLogin.getInstance().isLogin()) { //是否已经授权
                item_tabao_tv.setText("已授权");
            } else {
                item_tabao_tv.setText("未授权");
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

                break;
            case R.id.accountsecurity://账户安全
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
            case R.id.item9_rl: //填写微信号
                WxBindDialog wxBindDialog=new WxBindDialog(this);
                wxBindDialog.show();
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

            case R.id.rl_sex://性别
                openDialogUsersex();
                break;
            case R.id.rl_nickname://昵称
                openDialogUserName();
                break;
            case R.id.rl_head://头像
                ReadImgUtils.callPermissionOfEnableCrop(this);
                break;

            default:
                break;
        }
    }

    private void openDialogUserName() {
        Bundle bundle6 = new Bundle();
        bundle6.putString("title", "昵称");
        bundle6.putString("fragmentName", "RenameFragment");
        bundle6.putString("UserName", UserLocalData.getUser().getNickName());
        PageToUtil.goToUserInfoSimpleFragment(this, bundle6);
    }

    private void openDialogUsersex() {
        if (mPvCustomOptions == null) {
            mPvCustomOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    //返回的分别是三个级别的选中位置
                    MyLog.d(options1);
                    updataSex(options1 + 1);
                }
            })
                    .setDividerColor(Color.BLACK)
                    .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                    .setContentTextSize(20)
                    .build();
            ArrayList<String> cardItem = new ArrayList<>();
            cardItem.add("男");
            cardItem.add("女");
            mPvCustomOptions.setPicker(cardItem);//添加数据
        }
        mPvCustomOptions.show();
    }
    private void updataSex(final int sex) {
        if (sex == 0) return;
        RequestNickNameBean requestBean = new RequestNickNameBean();
        requestBean.setSex(sex+"");
        String sign = EncryptUtlis.getSign2(requestBean);
        requestBean.setSign(sign);
        RxHttp.getInstance().getUsersService()
                .setNickname(requestBean)
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
                        ViewShowUtils.showShortToast(SettingActivity.this, "修改成功");
                        //更新个人数据
                        UserLocalData.getUser().setSex(sex);
                        tv_sex.setText(sex == 1 ? "男" : "女");

                    }
                });
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if (selectList == null || selectList.size() == 0) return;
                    LocalMedia localMedia = selectList.get(0);
                    String compressPath = localMedia.getCompressPath();
                    if (!TextUtils.isEmpty(compressPath)) {

                        uploadPicOnly(compressPath);
                    } else {
                        if (!TextUtils.isEmpty(localMedia.getPath())) {
                            uploadPicOnly(localMedia.getPath());
                        }
                    }

                    break;
            }
        }
    }

    /**
     * 上传图片到服务器
     *
     * @param
     */
    private void uploadPicOnly(String path) {
        LoadingView.showDialog(this, "提交中...");
        OssManage.newInstance().putFielToOss(path, new MyAction.OnResult<String>() {
            @Override
            public void invoke(final String arg) {
                if (!TextUtils.isEmpty(arg)) {
                    setHead(arg);
                }
            }

            @Override
            public void onError() {
                LoadingView.dismissDialog();
            }
        });

    }

    /**
     * 修改头像
     *
     * @param picPath
     */
    private void setHead(final String picPath) {
        LoadingView.showDialog(this, "提交中...");
        RequestUpdateHeadPortraitBean requestBean = new RequestUpdateHeadPortraitBean();
        requestBean.setHeadImg(picPath);
        String sign = EncryptUtlis.getSign2(requestBean);
        requestBean.setSign(sign);
        RxHttp.getInstance().getUsersService()
                .setHead(requestBean)
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
                    protected void onError(String errorMsg, String errCode) {
                        super.onError(errorMsg, errCode);
                        LoadingView.dismissDialog();
                    }

                    @Override
                    protected void onDataNull() {
                        onSuccess("");
                    }

                    @Override
                    protected void onSuccess(String data) {
                        ViewShowUtils.showShortToast(SettingActivity.this, "修改成功");
                        //更新个人数据
                        UserLocalData.getUser(SettingActivity.this).setHeadImg(picPath);
                        UserLocalData.isPartner = true;
                        LoadImgUtils.setImgCircle(SettingActivity.this, userIcon2, picPath);
                        EventBus.getDefault().post(new MessageEvent(EventBusAction.MAINPAGE_MYCENTER_REFRESH_DATA));
                    }
                });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        CallbackContext.onActivityResult(requestCode, resultCode, data);
//    }
}
