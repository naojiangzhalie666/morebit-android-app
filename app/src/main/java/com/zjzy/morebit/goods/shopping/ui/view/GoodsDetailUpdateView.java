package com.zjzy.morebit.goods.shopping.ui.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trello.rxlifecycle2.components.RxActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Dialog.NumberLeaderUpgradeDialog;
import com.zjzy.morebit.Module.common.Dialog.NumberVipUpgradeDialog;
import com.zjzy.morebit.R;
import com.zjzy.morebit.fragment.NumberSubFragment;
import com.zjzy.morebit.info.ui.VipActivity;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.event.RefreshUserInfoEvent;
import com.zjzy.morebit.pojo.myInfo.UpdateInfoBean;
import com.zjzy.morebit.pojo.request.RequestUpdateUserBean;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.ViewShowUtils;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * Created by fengrs on 2019/5/14.
 * 备注:
 */

public class GoodsDetailUpdateView extends RelativeLayout implements View.OnClickListener {

    private final Context mContext;
    private TextView mTv_update_earnings; // 升级后佣金
    private TextView mTv_estimate_earnings;// 当前佣金
    private LayoutInflater mFrom;

    public GoodsDetailUpdateView(Context context) {
        this(context, null);
    }

    public GoodsDetailUpdateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        mFrom = LayoutInflater.from(context);
        View view = getview();
        if (view != null) {
            this.addView(view);
        }
    }

    public void refreshView() {
        View view = getview();
        if (view != null) {
            this.removeAllViews();
            this.addView(view);
        }
    }

    @NonNull
    private View getview() {
        if (mFrom == null) return null;
        UserInfo user = UserLocalData.getUser();
        View view;
        if (C.UserType.vipMember.equals(user.getPartner())) {
            view = mFrom.inflate(R.layout.view_goodsdetail_update_vip2, null);
            mTv_update_earnings = view.findViewById(R.id.tv_update_earnings);
            mTv_estimate_earnings = view.findViewById(R.id.tv_estimate_earnings);
            view.setOnClickListener(this);
        } else if (C.UserType.operator.equals(user.getPartner())) {
            view = mFrom.inflate(R.layout.view_goodsdetail_update_vip3, null);
            mTv_estimate_earnings = view.findViewById(R.id.tv_estimate_earnings);
            view.setOnClickListener(this);
        } else {
            view = mFrom.inflate(R.layout.view_goodsdetail_update_vip2, null);
            mTv_update_earnings = view.findViewById(R.id.tv_update_earnings);
            mTv_estimate_earnings = view.findViewById(R.id.tv_estimate_earnings);
            view.setOnClickListener(this);
//            view = mFrom.inflate(R.layout.view_goodsdetail_update_vip1, null);
//            mTv_update_earnings = view.findViewById(R.id.tv_update_earnings);
//            view.setOnClickListener(this);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        if (LoginUtil.checkIsLogin((Activity) mContext)) {
            UserInfo userInfo = UserLocalData.getUser();
            if (userInfo != null){
                if (C.UserType.member.equals(userInfo.getUserType())){
                    updateGrade();

                }else if (C.UserType.vipMember.equals(userInfo.getUserType())){
                    updateGradeForLeader();
                }
            }
        }

    }

    /**
     * 升级vip的弹框
     */
    private void updateGrade(){
        NumberVipUpgradeDialog leaderUpgradeDialog = new NumberVipUpgradeDialog(mContext,R.style.dialog);
        leaderUpgradeDialog.setOnListner(new NumberVipUpgradeDialog.OnListener(){

            @Override
            public void onClick() {
                updateGradePresenter((BaseActivity)mContext,Integer.parseInt(C.UserType.vipMember));
            }
        });
        leaderUpgradeDialog.show();
    }

    /**
     * 升级团队长的弹框
     */
    private void updateGradeForLeader(){
        NumberLeaderUpgradeDialog vipUpgradeDialog = new NumberLeaderUpgradeDialog(mContext,R.style.dialog);
        vipUpgradeDialog.setOnListner(new NumberLeaderUpgradeDialog.OnListener(){

            @Override
            public void onClick(){
                updateGradePresenter((BaseActivity) mContext,Integer.parseInt(C.UserType.operator));
            }

        });
        vipUpgradeDialog.show();

    }
    /**
     * 升级
     * @param rxActivity
     * @param userType
     */
    public void updateGradePresenter(BaseActivity rxActivity, int userType) {
        updateUserGrade(rxActivity, userType)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                    }
                })
                .subscribe(new DataObserver<UpdateInfoBean>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
//                        super.onError(errorMsg, errCode);
                        showError(errCode,errorMsg);
                    }

                    @Override
                    protected void onDataListEmpty() {

                    }
                    @Override
                    protected void onSuccess(UpdateInfoBean data) {
                        onGradeSuccess(data);
                    }
                });
    }

    public void showError(String errorNo,String msg) {
        MyLog.i("test", "onFailure: " + this);
        if ("B1100007".equals(errorNo)
                ||"B1100008".equals(errorNo)
                ||"B1100009".equals(errorNo)
                || "B1100010".equals(errorNo)) {
            ViewShowUtils.showShortToast(mContext,msg);
        }

    }
    public void onGradeSuccess(UpdateInfoBean info) {
        if (info != null){
            UserInfo userInfo = UserLocalData.getUser();
            userInfo.setUserType(String.valueOf(info.getUserType()));
            userInfo.setMoreCoin(info.getMoreCoin());
            UserLocalData.setUser((Activity) mContext,userInfo);
            EventBus.getDefault().post(new RefreshUserInfoEvent());
        }else{
            MyLog.d("test","用户信息为空");
        }

    }

    /**
     * 用户等级升级
     *
     * @param rxActivity
     * @return
     */
    public Observable<BaseResponse<UpdateInfoBean>> updateUserGrade(BaseActivity rxActivity, int userGrade) {
        RequestUpdateUserBean updateUserBean = new RequestUpdateUserBean();
        updateUserBean.setType(userGrade);
        return RxHttp.getInstance().getUsersService().updateUserGrade(updateUserBean)
                .compose(RxUtils.<BaseResponse<UpdateInfoBean>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<UpdateInfoBean>>bindToLifecycle());
    }

    public void setEstimateData(ShopGoodInfo data) {
        if (mTv_estimate_earnings != null) {
            UserInfo user = UserLocalData.getUser();
            String muRatioComPrice = MathUtils.getMuRatioComPrice(user.getCalculationRate(), data.getCommission());
            muRatioComPrice = TextUtils.isEmpty(muRatioComPrice) ? "0" : muRatioComPrice;
            String getRatioSubside = MathUtils.getMuRatioSubSidiesPrice(user.getCalculationRate(),data.getSubsidiesPrice());
            String totalSubside = MathUtils.getTotleSubSidies(muRatioComPrice,getRatioSubside);
            if (C.UserType.vipMember.equals(user.getPartner())) { // vip
                mTv_estimate_earnings.setText(mContext.getString(R.string.estimate_earnings, totalSubside));
            } else if (C.UserType.operator.equals(user.getPartner())) {//运营专员
                mTv_estimate_earnings.setText(mContext.getString(R.string.update_vip3_earnings, totalSubside));
            }else if (C.UserType.member.equals(user.getPartner())){//会员只有佣金分佣。没有补贴。//添加新逻辑
                mTv_estimate_earnings.setText(mContext.getString(R.string.estimate_earnings, muRatioComPrice));
            }
            //未登录状态按照会员计算
            if (user == null
            || (user != null && user.getPartner() == null)){
                String unLoginmuRatioComPrice = MathUtils.getMuRatioComPrice(C.SysConfig.NUMBER_COMMISSION_PERCENT_VALUE, data.getCommission());
                mTv_estimate_earnings.setText("注册会员");
                mTv_update_earnings.setText("即可获得收益"+unLoginmuRatioComPrice+"元");
            }
        }
    }

    public void setUpdateView(ShopGoodInfo data, String updateView) {
        String[] split = null;
        if (!TextUtils.isEmpty(updateView)) {
            split = updateView.split(",");
            if (split.length < 3) return;
        }
        MyLog.d("commission=","setUpdateView="+data.getCommission());
        if (mTv_update_earnings != null) {
            UserInfo user = UserLocalData.getUser();
            if (C.UserType.vipMember.equals(user.getPartner())) { // vip
                int integer = Integer.valueOf(C.UserType.operator);
                String s = split[integer];
                String muRatioComPrice = MathUtils.getMuRatioComPrice(s, data.getCommission());
                muRatioComPrice = TextUtils.isEmpty(muRatioComPrice) ? "0" : muRatioComPrice;
                String getRatioSubside = MathUtils.getMuRatioSubSidiesPrice(s,data.getSubsidiesPrice());
                String totalSubside = MathUtils.getTotleSubSidies(muRatioComPrice,getRatioSubside);
                mTv_update_earnings.setText(mContext.getString(R.string.update_vip2_earnings, totalSubside));
            } else if (C.UserType.member.equals(user.getPartner())){//会员和未登录
                int integer = Integer.valueOf(C.UserType.vipMember);
                String s = split[integer];
                String muRatioComPrice = MathUtils.getMuRatioComPrice(s, data.getCommission());
                muRatioComPrice = TextUtils.isEmpty(muRatioComPrice) ? "0" : muRatioComPrice;
                //String getRatioSubside = MathUtils.getMuRatioSubSidiesPrice(s,data.getSubsidiesPrice());
               // String totalSubside = MathUtils.getTotleSubSidies(muRatioComPrice,getRatioSubside);
                mTv_update_earnings.setText(mContext.getString(R.string.update_vip2_earnings, muRatioComPrice));
            }
        }
    }
}
