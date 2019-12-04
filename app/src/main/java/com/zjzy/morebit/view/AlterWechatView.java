package com.zjzy.morebit.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.AccountDestroy;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.action.MyAction;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by fengrs on 2018/11/29.
 * 备注:
 */

public class AlterWechatView extends RelativeLayout {
    @BindView(R.id.edt_weichat)
    ClearEditText edt_weichat;
    @BindView(R.id.tv_xz1)
    TextView tv_xz1;
    @BindView(R.id.tv_xz2)
    TextView tv_xz2;
    @BindView(R.id.tv_xz3)
    TextView tv_xz3;
    @BindView(R.id.rl_xz1)
    RelativeLayout rl_xz1;
    @BindView(R.id.rl_xz2)
    RelativeLayout rl_xz2;
    @BindView(R.id.rl_xz3)
    RelativeLayout rl_xz3;
    @BindView(R.id.ll_select)
    LinearLayout ll_select;
    private Context mContext;
    private MyAction.OnResultTwo<Integer, String> mAction;
    private AccountDestroy mData;

    public AlterWechatView(Context context) {
        this(context, null);
    }

    public AlterWechatView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_setting_alter_wechat, null);
        ButterKnife.bind(this, view);
        addView(view);

    }

    public void setSelect(TextView textView, RelativeLayout relativeLayout) {
        Drawable drawable = mContext.getResources().getDrawable(R.drawable.icon_dagouxuanzhong);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, null, drawable, null);
        relativeLayout.setBackgroundResource(R.drawable.bg_000000_1dpline_yellowbg_round_30dp);
    }

    public void setNuSelect(TextView textView, RelativeLayout relativeLayout) {
        textView.setCompoundDrawables(null, null, null, null);
        relativeLayout.setBackgroundResource(R.drawable.bg_d3d3d3_white_round_30dp);
    }

    @OnClick({R.id.rl_xz1, R.id.rl_xz2, R.id.rl_xz3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_xz1:
                if (mAction != null)
                    mAction.invoke(0, "");
                setSelect(0);
                break;
            case R.id.rl_xz2:
                if (mAction != null)
                    mAction.invoke(1, "");
                setSelect(1);
                break;
            case R.id.rl_xz3:
                if (mAction != null)
                    mAction.invoke(2, "");
                setSelect(2);
                break;
        }
    }


    public void setAction(MyAction.OnResultTwo<Integer, String> action) {
        this.mAction = action;
    }

    @OnTextChanged(value = R.id.edt_weichat)
    void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mAction != null)
            mAction.invoke(-1, s.toString().trim());
    }

    public void setSelect(int type) {
        switch (type) {
            case 0:
                setSelect(tv_xz1, rl_xz1);
                setNuSelect(tv_xz2, rl_xz2);
                setNuSelect(tv_xz3, rl_xz3);
                break;
            case 1:
                setNuSelect(tv_xz1, rl_xz1);
                setSelect(tv_xz2, rl_xz2);
                setNuSelect(tv_xz3, rl_xz3);
                break;
            case 2:
                setNuSelect(tv_xz1, rl_xz1);
                setNuSelect(tv_xz2, rl_xz2);
                setSelect(tv_xz3, rl_xz3);
                break;

            default:
                break;
        }
    }

    public void clearWxCode() {
        edt_weichat.setText("");
    }

    public void setData(AccountDestroy data) {
        mData = data;

        if (C.UserType.operator.equals(UserLocalData.getUser(mContext).getPartner())) {
            ll_select.setVisibility(VISIBLE);
        } else {
            ll_select.setVisibility(GONE);

        }
        if (mData != null) {
            setSelect(mData.getWxShowType());
            edt_weichat.setText(mData.getWxNumber());
        }
    }
}
