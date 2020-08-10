package com.zjzy.morebit.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.wireless.security.open.middletier.fc.IFCActionCallback;
import com.blankj.utilcode.util.SPUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zjzy.morebit.Activity.SettingActivity;
import com.zjzy.morebit.BuildConfig;
import com.zjzy.morebit.MainActivity;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Dialog.ClearSDdataDialog;
import com.zjzy.morebit.Module.common.Dialog.DevelopIpSettingDialog;
import com.zjzy.morebit.Module.common.Dialog.InputVerificationCodeDialog;
import com.zjzy.morebit.Module.common.Fragment.BaseFragment;
import com.zjzy.morebit.R;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.AppUpgradeInfo;
import com.zjzy.morebit.pojo.HotKeywords;
import com.zjzy.morebit.pojo.requestbodybean.RequestOsBean;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MyGsonUtils;
import com.zjzy.morebit.utils.appDownload.QianWenUpdateUtlis;
import com.zjzy.morebit.utils.appDownload.update_app.UpdateAppBean;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import io.reactivex.functions.Consumer;
import okhttp3.RequestBody;


/**
 * 关于界面
 */
public class AppAboutFragment extends BaseFragment implements View.OnClickListener,Handler.Callback  {

    private TextView item_tv1, item_tv2,name_ip_tv;
    private AppUpgradeInfo auData;
    private ClearSDdataDialog mAccountDestroyDialog;
    private String mAccountDestroyHit;
    private RoundedImageView icon_img;
    private View mMSearviceIpRl;
    private boolean showDevlep;
    private int mClickAboutCount;
    private Handler mHandler;
    public static final int SETTING_CODE = 120;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_appabout, container, false);
        mHandler = new Handler(Looper.getMainLooper(),this);
        inview(view);
        getAppInfo();
        return view;
    }


    public void inview(View view) {
//        ActivityStyleUtil.initSystemBar(getActivity(), R.color.color_757575); //设置标题栏颜色值
        //设置页面头顶空出状态栏的高度
        item_tv1 = (TextView) view.findViewById(R.id.item_tv1);
        item_tv2 = (TextView) view.findViewById(R.id.item_tv2);
        icon_img=view.findViewById(R.id.icon_img);


        view.findViewById(R.id.item2_rl).setOnClickListener(this);

        try {
            item_tv1.setText("V" + AppUtil.getVersionName(getActivity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取App版本信息
     */
    @SuppressLint("CheckResult")
    public void getAppInfo() {

        RequestOsBean requestOsBean = new RequestOsBean();
        RequestBody osBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), MyGsonUtils.beanToJson(requestOsBean));
        RxHttp.getInstance().getSysteService().getAppVersion(requestOsBean)
                .compose(this.<BaseResponse<AppUpgradeInfo>>bindToLifecycle())
                .compose(RxUtils.<BaseResponse<AppUpgradeInfo>>switchSchedulers())
                .map(RxUtils.<AppUpgradeInfo>handleRESTFulResult())
                .subscribe(new Consumer<AppUpgradeInfo>() {
                    @Override
                    public void accept(AppUpgradeInfo appUpgradeInfo) throws Exception {
                        auData = appUpgradeInfo;
                        checkAppUpgrade(appUpgradeInfo);
                    }
                });

        //        RxHttp.getInstance().getSysteService().getAppVersion(1)
        //                .compose(RxUtils.<BaseResponse<AppUpgradeInfo>>switchSchedulers())
        //                .compose(this.<BaseResponse<AppUpgradeInfo>>bindToLifecycle())
        //                .map(RxUtils.<AppUpgradeInfo>handleRESTFulResult())
        //                .subscribe(new Consumer<AppUpgradeInfo>() {
        //                    @Override
        //                    public void accept(AppUpgradeInfo appUpgradeInfo) throws Exception {
        //                        auData = appUpgradeInfo;
        //                        checkAppUpgrade(appUpgradeInfo);
        //                    }
        //                });
    }

    /**
     * 判断版本大小
     *
     * @param data
     */
    private void checkAppUpgrade(AppUpgradeInfo data) {
        String myVersionCode = AppUtil.getVersionCode(getActivity());
        String isDebug = data.getStatus();
//        if (!"1".equals(isDebug)) {
            int myVcode = Integer.parseInt(myVersionCode);
            String sevrVersionCode = data.getVersion();
            int sevrVcode = Integer.parseInt(sevrVersionCode);
            if (myVcode < sevrVcode) { //需要升级
                item_tv2.setText("更新 V" + data.getEdition());
            } else {
//                if (App.mIsHotAppUpdataFailure) {
//                    item_tv2.setText("更新 V" + data.getEdition() + "1");
//                } else {
                    item_tv2.setText("暂无更新");
//                }
            }
//        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item2_rl: //更新版本
                try {
                    toUpload();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;

        }
    }


    /**
     * 更新版本
     */
    private void toUpload() {
        if (auData == null || auData == null) {
            getAppInfo();
            return;
        }
        String myVersionCode = AppUtil.getVersionCode(getActivity());
        String isDebug = auData.getStatus();

        int myVcode = Integer.parseInt(myVersionCode);
        String sevrVersionCode = auData.getVersion();
        int sevrVcode = Integer.parseInt(sevrVersionCode);
        UpdateAppBean data1 = QianWenUpdateUtlis.getData(auData);
        if (myVcode < sevrVcode) { //需要升级
            if ("2".equals(auData.getUpgradde())) {//是否强制升级 1:是，2：否，3：静默更新
                //普通升级
                data1.setConstraint(false);
                QianWenUpdateUtlis.constraintUpdate(getActivity(), auData.getDownload(), data1);
            } else if ("1".equals(auData.getUpgradde())) {
                //强制升级
                data1.setConstraint(true);
                QianWenUpdateUtlis.constraintUpdate(getActivity(), auData.getDownload(), data1);
            } else {//3：静默更新
                QianWenUpdateUtlis.onlyUpdateApp(getActivity(), auData.getDownload(), data1);
            }
        }
    }

    private void openAccountDestroyDialog(String data) {  //注销确认弹窗
        if (TextUtils.isEmpty(data)) return;
        mAccountDestroyDialog = new ClearSDdataDialog(getActivity(), R.style.dialog, "提示", data, new ClearSDdataDialog.OnOkListener() {
            @Override
            public void onClick(View view, String text) {
                destroyAccount();
            }
        });
        mAccountDestroyDialog.setOkTextAndColor(R.color.color_FF4848, "确认注销");
        mAccountDestroyDialog.setCancelTextAndColor(R.color.color_808080, "再想想");
        mAccountDestroyDialog.show();
    }

    /**
     * 账号注销
     */
    private void destroyAccount() {
        InputVerificationCodeDialog mInputVerificationCodeDialog = new InputVerificationCodeDialog((RxAppCompatActivity) getActivity());
        mInputVerificationCodeDialog.show();

    }

    /**
     * 返回账号注销提示
     */
    private void getAccountDestroyHint() {
        RxHttp.getInstance().getCommonService().getRtnInfo()
                .compose(RxUtils.<BaseResponse<HotKeywords>>switchSchedulers())
                .compose(this.<BaseResponse<HotKeywords>>bindToLifecycle())
                .subscribe(new DataObserver<HotKeywords>() {
                    @Override
                    protected void onSuccess(HotKeywords data) {
                        if (!TextUtils.isEmpty(data.getSysValue())) {
                            mAccountDestroyHit = data.getSysValue();
                            openAccountDestroyDialog(data.getSysValue());
                        }
                    }
                });

    }


    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case SETTING_CODE:
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                android.os.Process.killProcess(android.os.Process.myPid());
                return true;
        }
        return false;
    }
}
