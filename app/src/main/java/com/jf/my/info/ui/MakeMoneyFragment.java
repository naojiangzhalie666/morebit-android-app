package com.jf.my.info.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jf.my.Activity.ShowWebActivity;
import com.jf.my.LocalData.UserLocalData;
import com.jf.my.Module.common.Dialog.ClearSDdataDialog;
import com.jf.my.Module.common.Utils.LoadingView;
import com.jf.my.R;
import com.jf.my.fragment.NewcomersFragment;
import com.jf.my.info.contract.MakeMoenyContract;
import com.jf.my.info.presenter.MakeMoenyPresenter;
import com.jf.my.info.ui.fragment.ShareFriendsFragment;
import com.jf.my.mvp.base.base.BaseView;
import com.jf.my.mvp.base.frame.MvpFragment;
import com.jf.my.pojo.ProtocolRuleBean;
import com.jf.my.pojo.UserInfo;
import com.jf.my.pojo.myInfo.ApplyUpgradeBean;
import com.jf.my.pojo.myInfo.MakeMoenyBean;
import com.jf.my.utils.AppUtil;
import com.jf.my.utils.C;
import com.jf.my.utils.LoadImgUtils;
import com.jf.my.utils.LoginUtil;
import com.jf.my.utils.MathUtils;
import com.jf.my.utils.OpenFragmentUtils;
import com.jf.my.utils.PageToUtil;
import com.jf.my.utils.ViewShowUtils;
import com.jf.my.view.ToolbarHelper;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by fengrs on 2018/8/3.
 * 备注:  赚钱计划
 */

public class MakeMoneyFragment extends MvpFragment<MakeMoenyPresenter> implements MakeMoenyContract.View {
    @BindView(R.id.ll_issue)
    LinearLayout ll_issue;
    @BindView(R.id.iv_picture)
    ImageView mIvPicture;
    @BindView(R.id.tv_left_title)
    TextView mTvLeftTitle;
    @BindView(R.id.tv_left_subtitle)
    TextView mTvLeftSubtitle;
    @BindView(R.id.tv_right_title)
    TextView mTvRightTitle;
    @BindView(R.id.tv_right_subtitle)
    TextView mTvRightSubtitle;
    @BindView(R.id.tv_content)
    TextView mTvContent;

    @BindView(R.id.tv_invite_code)
    TextView mTvInviteCode;
    @BindView(R.id.tv_income)
    TextView mTvIncome;
    @BindView(R.id.tv_e_month)
    TextView mTvEMonth;
    @BindView(R.id.tv_grade)
    TextView mTvGrade;
    @BindView(R.id.tv_fan_count)
    TextView mTvFanCount;
    @BindView(R.id.tv_mack)
    TextView mTvMack;
    @BindView(R.id.tv_share)
    TextView mShare;
    private String mLink = "";
    private UserInfo mUser;
    private String mMack;

    public static void start(Activity activity) {
        OpenFragmentUtils.goToComplaintOrderFragment(activity, MakeMoneyFragment.class.getName(), null);
    }

    @Override
    public void showFinally() {
        LoadingView.dismissDialog();
    }

    @Override
    protected void initData() {
        LoadingView.showDialog(getActivity());
        mPresenter.getMakeMoneyRuleData(this);
        mPresenter.getCommonProblemData(this);
        mUser = UserLocalData.getUser(getActivity());
        String invite_code = mUser.getInviteCode();
        mMack = getString(R.string.mack_step);

        if (TextUtils.isEmpty(mUser.getInviteCode())) {  //未登录
            mTvGrade.setText("未登录");
            mTvFanCount.setText("0");
            mShare.setVisibility(View.GONE);
            mTvInviteCode.setText("未登录");
        }
    }

    @Override
    protected void initView(final View view) {
        if (C.UserType.operator.equals(UserLocalData.getUser(getActivity()).getPartner()) || TextUtils.isEmpty(UserLocalData.getToken())) {
            new ToolbarHelper(this).setToolbarAsUp().setCustomTitle(getString(R.string.mack_plan));
        } else {
            new ToolbarHelper(this).setToolbarAsUp().setCustomTitle(getString(R.string.mack_plan)).setCustomRightTitle(getString(R.string.apply_updata), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadingView.showDialog(getActivity());
                    mPresenter.applyUpgradeData(MakeMoneyFragment.this);
                }
            });
        }
    }

    @Override
    protected int getViewLayout() {
        return R.layout.fragment_make_money;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }


    private void openlogoutDialog(final ApplyUpgradeBean bean) {
        String ok = "";
        switch (bean.getType()) {
            case "1":
                ok = getString(R.string.confirm);
                break;
            case "2":
                ok = getString(R.string.immediately_invited);
                break;
            case "3":
                ok = getString(R.string.contact_relation);

                break;
        }
        if (TextUtils.isEmpty(bean.getMessage())) return;
        ClearSDdataDialog logoutDialog = new ClearSDdataDialog(getActivity(), R.style.dialog, "提示", bean.getMessage(), new ClearSDdataDialog.OnOkListener() {
            @Override
            public void onClick(View view, String text) {
                switch (bean.getType()) {
                    case "1":
                        mPresenter.confirmUpgrade(MakeMoneyFragment.this);
                        break;
                    case "2":
                        OpenFragmentUtils.goToSimpleFragment(getActivity(), ShareFriendsFragment.class.getName(), new Bundle());
                        break;
                    case "3":
                        PageToUtil.goToSimpleFragment(getActivity(), "专属客服", "CustomerServiceFragment");

                        break;
                }

            }
        });
        logoutDialog.setCancelTextAndColor(R.color.color_333333, getString(R.string.cancel));
        logoutDialog.setOkTextAndColor(R.color.color_FF4848, ok);

        logoutDialog.show();
    }

    /**
     * 申请升级 成功
     *
     * @param data
     */
    @Override
    public void showApplyUpgradeDialog(ApplyUpgradeBean data) {
        openlogoutDialog(data);
    }

    /**
     * 确认升级 成功
     *
     * @param data
     */
    @Override
    public void confirmUpgradeSuccess(String data) {
        ViewShowUtils.showShortToast(getActivity(), data);
        LoginUtil.getUserInfo((RxAppCompatActivity) getActivity(), false);
    }

    /**
     * @param data
     */
    @Override
    public void setMakeMoneyRuleData(MakeMoenyBean data) {
        mLink = data.getLink();
        LoadImgUtils.setImg(getActivity(), mIvPicture, data.getPicture(), R.drawable.image_zhuanqianjihua);
        mTvInviteCode.setText(getString(R.string.my_invite_code, data.getInviteCode()));
        mTvEMonth.setText(getString(R.string.income, MathUtils.getMoney(data.getEMonth() + "")));
        mTvIncome.setText(getString(R.string.income, MathUtils.getMoney(data.getEToday() + "")));
        mTvGrade.setText(data.getGrade());
        mTvFanCount.setText(data.getFanCount() + "");
        mMack = TextUtils.isEmpty(data.getTitle()) ? getString(R.string.mack_step) : data.getTitle();
        mTvMack.setText(mMack);
        MakeMoenyBean.IntroBean intro = data.getIntro();
        if (intro != null) {
            if (!TextUtils.isEmpty(intro.getContent()))
            mTvContent.setText(Html.fromHtml(intro.getContent()));
            MakeMoenyBean.IntroBean.ItmeBean left = intro.getLeft();
            if (left != null) {
                mTvLeftTitle.setText(left.getTitle());
                if (!TextUtils.isEmpty(left.getSubtitle()))
                mTvLeftSubtitle.setText(Html.fromHtml(left.getSubtitle()));

            }
            MakeMoenyBean.IntroBean.ItmeBean ritht = intro.getRight();
            if (left != null) {
                mTvRightTitle.setText(ritht.getTitle());
                if (!TextUtils.isEmpty(ritht.getSubtitle()))
                mTvRightSubtitle.setText(Html.fromHtml(ritht.getSubtitle()));
            }
        }
    }

    /**
     * 常见问题数据
     *
     * @param data
     */
    @Override
    public void setCommonProblemData(List<ProtocolRuleBean> data) {
        if (data.size() == 0) {
            return;
        }
        for (int i = 0; i < data.size(); i++) {
            ProtocolRuleBean commonProblemBean = data.get(i);
            View issueView = LayoutInflater.from(getContext()).inflate(R.layout.view_make_moeny_issue, null);
            final TextView iv_con = (TextView) issueView.findViewById(R.id.iv_con);
            TextView iv_text = (TextView) issueView.findViewById(R.id.iv_text);
            final ImageView iv_arrows = (ImageView) issueView.findViewById(R.id.iv_arrows);
            iv_text.setText(commonProblemBean.getTitle());
            if(!TextUtils.isEmpty(commonProblemBean.getContent())){
                iv_con.setText(Html.fromHtml(commonProblemBean.getContent()));
            }

            issueView.findViewById(R.id.ll_cot).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iv_con.getVisibility() == View.GONE) {
                        iv_con.setVisibility(View.VISIBLE);
                        iv_arrows.setSelected(true);
                    } else {
                        iv_con.setVisibility(View.GONE);
                        iv_arrows.setSelected(false);

                    }
                }
            });
            ll_issue.addView(issueView);
        }
    }

    @OnClick({R.id.tv_mack, R.id.tv_share, R.id.tv_capy, R.id.tv_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_mack:
                if (AppUtil.isFastClick(500)) {
                    return;
                }
                if (TextUtils.isEmpty(mLink)) {
                    return;
                }
                //跳转到网页
                ShowWebActivity.start(getActivity(),mLink,mMack);

//                Intent it = new Intent(getActivity(), ShowWebActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("title", mMack);
//                bundle.putString("url", mLink);
//                it.putExtras(bundle);
//                getActivity().startActivity(it);
                break;
            case R.id.tv_share:
                OpenFragmentUtils.goToSimpleFragment(getActivity(), ShareFriendsFragment.class.getName(), new Bundle());
                break;
            case R.id.tv_capy:
                if (mUser != null && TextUtils.isEmpty(mUser.getInviteCode())) {
                    ViewShowUtils.showShortToast(getActivity(), getString(R.string.please_login));
                    return;
                }
                if (mUser != null) {
                    AppUtil.coayTextPutNative(getActivity(), mUser.getInviteCode());
                    ViewShowUtils.showShortToast(getActivity(), getString(R.string.coayTextSucceed));
                }
                break;
            case R.id.tv_more:
                NewcomersFragment.start(getActivity());
                break;
        }
    }

}
