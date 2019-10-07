package com.jf.my.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jf.my.Activity.SettingActivity;
import com.jf.my.App;
import com.jf.my.Module.common.Dialog.ClearSDdataDialog;
import com.jf.my.Module.common.Dialog.InputVerificationCodeDialog;
import com.jf.my.Module.common.Fragment.BaseFragment;
import com.jf.my.R;
import com.jf.my.network.BaseResponse;
import com.jf.my.network.RxHttp;
import com.jf.my.network.RxUtils;
import com.jf.my.network.observer.DataObserver;
import com.jf.my.pojo.AppUpgradeInfo;
import com.jf.my.pojo.HotKeywords;
import com.jf.my.pojo.requestbodybean.RequestOsBean;
import com.jf.my.utils.AppUtil;
import com.jf.my.utils.MyGsonUtils;
import com.jf.my.utils.appDownload.QianWenUpdateUtlis;
import com.jf.my.utils.appDownload.update_app.UpdateAppBean;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import io.reactivex.functions.Consumer;
import okhttp3.RequestBody;


/**
 * 关于界面
 */
public class AppAboutFragment extends BaseFragment implements View.OnClickListener {

    private TextView item_tv1, item_tv2;
    private AppUpgradeInfo auData;
    private ClearSDdataDialog mAccountDestroyDialog;
    private String mAccountDestroyHit;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_appabout, container, false);
        inview(view);
        getAppInfo();
        return view;
    }

    public void inview(View view) {
//        ActivityStyleUtil.initSystemBar(getActivity(), R.color.color_757575); //设置标题栏颜色值
        //设置页面头顶空出状态栏的高度
        item_tv1 = (TextView) view.findViewById(R.id.item_tv1);
        item_tv2 = (TextView) view.findViewById(R.id.item_tv2);
        view.findViewById(R.id.item2_rl).setOnClickListener(this);
        view.findViewById(R.id.item_account_destroy).setOnClickListener(this);
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
            case R.id.item_account_destroy:
                if (TextUtils.isEmpty(mAccountDestroyHit)) {
                    getAccountDestroyHint();
                } else {
                    openAccountDestroyDialog(mAccountDestroyHit);
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
}
