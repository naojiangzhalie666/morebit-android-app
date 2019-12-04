package com.zjzy.morebit.info.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.info.model.WechatModel;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.AccountDestroy;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.request.RequestWechatCodeBean;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.ReadImgUtils;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.utils.encrypt.EncryptUtlis;
import com.zjzy.morebit.utils.fire.OssManage;
import com.zjzy.morebit.view.AlterWechatView;
import com.zjzy.morebit.view.ToolbarHelper;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Action;

/**
 * Created by YangBoTian on 2018/5/31 21:59
 * 专属微信修改页
 */

public class SettingWechatActivity extends BaseActivity implements View.OnClickListener {
    public final static String viewType = "viewType";
    public final static String amityHintExtra = "amityHintExtra";
    @BindView(R.id.ll_add_view)
    LinearLayout ll_add_view;
    @BindView(R.id.tv_submit)
    TextView tv_submit;
    @BindView(R.id.tv_wechat)
    TextView tv_title_wechat;
    @BindView(R.id.iv_qrcode)
    ImageView mIvQrcode;
    private String mHead;
    private String mEditText = "";
    private int mSelectType = -1;
    private int mViewType =0;
    private ToolbarHelper mToolbarHelper;
    public AccountDestroy mData;

    public static void start(Activity activity, int type) {
        start(activity, type, "");
    }

    public static void start(Activity activity, int type, String amityHint) {
        Intent intent = new Intent(activity, SettingWechatActivity.class);
        intent.putExtra(viewType, type);
        intent.putExtra(amityHintExtra, amityHint);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_wechat);
        setSystemBar();
        mToolbarHelper = new ToolbarHelper(this).setToolbarAsUp().setCustomTitle(getString(R.string.wechat_ewm));
        initData();
        mViewType = getIntent().getIntExtra(viewType,0);
    }


    private void initData() {
        LoadingView.showDialog(this);
        RxHttp.getInstance().getCommonService().getAccountDestroyHint()
                .compose(RxUtils.<BaseResponse<AccountDestroy>>switchSchedulers())
                .compose(this.<BaseResponse<AccountDestroy>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                })
                .subscribe(new DataObserver<AccountDestroy>() {


                    @Override
                    protected void onSuccess(AccountDestroy data) {
                        SettingWechatActivity.this.mData = data;

                        mSelectType = data.getWxShowType();
                        mEditText = data.getWxNumber();
                        mHead = data.getWxQrCode();
                        if (!TextUtils.isEmpty(data.getWxQrCode())) {
                            LoadImgUtils.setImg(SettingWechatActivity.this, mIvQrcode, data.getWxQrCode());
                        }

                        initsubmitView();
                    }
                });
    }


    private void initsubmitView() {
        tv_submit.setText(getString(R.string.submit_text));
        tv_title_wechat.setText(getString(R.string.submit_wechat_ewm));
        final AlterWechatView alterWechatView = new AlterWechatView(this);
        alterWechatView.setData(mData);
        alterWechatView.setAction(new MyAction.OnResultTwo<Integer, String>() {
            @Override
            public void invoke(Integer arg, String editText) {
                if (arg < 0) {
                    mEditText = editText;
                } else {
                    mSelectType = arg;
                }
            }

            @Override
            public void onError() {
            }
        });
        ll_add_view.removeAllViews();
        ll_add_view.addView(alterWechatView);
        mToolbarHelper.setCustomRightTitle(getString(R.string.clear), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectType = -1;
                mHead = "";
                mEditText = "";
                mIvQrcode.setImageResource(R.drawable.icon_morenerweima);
                alterWechatView.clearWxCode();
            }
        });
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
     * @param path
     */
    private void uploadPicOnly(String path) {
        LoadingView.showDialog(SettingWechatActivity.this);
        OssManage.newInstance().putFielToOss(path, new MyAction.OnResult<String>() {
            @Override
            public void invoke(String data) {
                LoadingView.dismissDialog();
                if (!TextUtils.isEmpty(data)) {
                    mHead = data;
                    if (!TextUtils.isEmpty(mHead)) {
                        LoadImgUtils.setImg(SettingWechatActivity.this, mIvQrcode, mHead);
                        ViewShowUtils.showShortToast(SettingWechatActivity.this, "上传成功");
                        return;
                    }
                }
                ViewShowUtils.showShortToast(SettingWechatActivity.this, "上传失败, 请重新上传");
            }

            @Override
            public void onError() {
                LoadingView.dismissDialog();
                ViewShowUtils.showShortToast(SettingWechatActivity.this, "上传失败, 请重新上传");
            }
        });


    }


    private void setWechatCode() {
        if (mViewType == 1 && TextUtils.isEmpty(mEditText.trim())) {
            ViewShowUtils.showShortToast(SettingWechatActivity.this, this.getString(R.string.wechat_ewm_no_empty));
            return;
        }
        LoadingView.showDialog(SettingWechatActivity.this, "");
        RequestWechatCodeBean bean = new RequestWechatCodeBean();
        bean.setWxNumber(mEditText);
        bean.setWxQrCode(mHead);
        MyLog.i("test", "mSelectType: " + mSelectType);
        bean.setWxShowType(mSelectType == -1 ? null : mSelectType + "");
        String sign2 = EncryptUtlis.getSign2(bean);
        bean.setSign(sign2);
        RxHttp.getInstance().getCommonService().saveWechatInfo(bean)
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

                        UserInfo user = UserLocalData.getUser(SettingWechatActivity.this);
                        if (user != null) {
                            user.setQrCode(mHead);
                            user.setWxNumber(mEditText);
                            user.setQr_code_show(mSelectType);
                        }
                        EventBus.getDefault().post(new MessageEvent(EventBusAction.MAINPAGE_MYCENTER_REFRESH_DATA));
                        if (TextUtils.isEmpty(UserLocalData.getUser().getOauthWx()) && mViewType == 1) {
                            new WechatModel(SettingWechatActivity.this, new MyAction.OnResult<String>() {
                                @Override
                                public void invoke(String arg) {
                                    ViewShowUtils.showShortToast(SettingWechatActivity.this, "提交成功");
                                    EventBus.getDefault().post(new MessageEvent(EventBusAction.MAINPAGE_MYCENTER_REFRESH_DATA));
                                    SettingWechatActivity.this.finish();
                                }

                                @Override
                                public void onError() {

                                }
                            }).authorize();
                        } else {
                            ViewShowUtils.showShortToast(SettingWechatActivity.this, "提交成功");
                        }
                    }
                });
    }

    @Override
    @OnClick({R.id.tv_submit, R.id.iv_qrcode})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit:
                setWechatCode();
                break;
            case R.id.iv_qrcode:
                ReadImgUtils.callPermissionOfEnableCrop(SettingWechatActivity.this);
                break;


            default:
                break;
        }
    }


    /**
     * 是否弹出去全网搜索
     *
     * @return
     */
    @Override
    public boolean isShowAllSeek() {
        return false;
    }


}
