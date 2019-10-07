package com.jf.my.goods.shopping.ui.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jf.my.LocalData.UserLocalData;
import com.jf.my.R;
import com.jf.my.info.ui.VipActivity;
import com.jf.my.pojo.ShopGoodInfo;
import com.jf.my.pojo.UserInfo;
import com.jf.my.utils.C;
import com.jf.my.utils.LoginUtil;
import com.jf.my.utils.MathUtils;
import com.jf.my.utils.MyLog;

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
            view = mFrom.inflate(R.layout.view_goodsdetail_update_vip1, null);
            mTv_update_earnings = view.findViewById(R.id.tv_update_earnings);
            view.setOnClickListener(this);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        if (LoginUtil.checkIsLogin((Activity) mContext)) {
            VipActivity.start((Activity) mContext);
        }

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
            } else {//会员和未登录
                int integer = Integer.valueOf(C.UserType.vipMember);
                String s = split[integer];
                String muRatioComPrice = MathUtils.getMuRatioComPrice(s, data.getCommission());
                muRatioComPrice = TextUtils.isEmpty(muRatioComPrice) ? "0" : muRatioComPrice;
                //String getRatioSubside = MathUtils.getMuRatioSubSidiesPrice(s,data.getSubsidiesPrice());
               // String totalSubside = MathUtils.getTotleSubSidies(muRatioComPrice,getRatioSubside);
                mTv_update_earnings.setText(mContext.getString(R.string.update_vip1_earnings, muRatioComPrice));
            }
        }
    }
}
