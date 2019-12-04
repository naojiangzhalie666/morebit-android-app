package com.zjzy.morebit.login.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjzy.morebit.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by fengrs on 2018/8/7.
 * 备注: 登录头布局
 */

public class LoginHeadView extends RelativeLayout {

    @BindView(R.id.tv_head)
    TextView tv_head;
    @BindView(R.id.iv_back)
    ImageView iv_back;

    private Context mContext;
    private Unbinder mBind;
    private String mTitle;
    private boolean mIsShowBack;
    private OnBackListener onBackListener;

    public LoginHeadView(Context context) {
        this(context, null);
    }

    public LoginHeadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoginHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_login_main_head, null);
        mBind = ButterKnife.bind(this, view);

        final TypedArray attributes = mContext.getTheme().obtainStyledAttributes(attrs, R.styleable.LoginHeadView, defStyleAttr, 0);

        mIsShowBack = attributes.getBoolean(R.styleable.LoginHeadView_isShowBack, false);
        mTitle = attributes.getString(R.styleable.LoginHeadView_titleName);
        if (mIsShowBack) {
            iv_back.setVisibility(VISIBLE);
        } else {
            iv_back.setVisibility(INVISIBLE);
        }
        if (!TextUtils.isEmpty(mTitle)) {
            tv_head.setText(mTitle);
        }
        this.addView(view);

    }


    @OnClick(R.id.iv_back)
    public void back() {
        if(null != this.onBackListener){
            this.onBackListener.onClick();
            return;
        }
        if (iv_back.getVisibility() == View.INVISIBLE) {
            return;
        }
        ((Activity) mContext).finish();
    }

    /**
     * 返回键隐藏
     *
     * @param visibility
     * @return
     */
    public LoginHeadView setVisibilityBack(int visibility) {
        iv_back.setVisibility(visibility);
        return this;
    }

    /**
     * 设置文字
     *
     * @param text
     * @return
     */
    public LoginHeadView setHeadText(String text) {
        if (text != null) {
            tv_head.setText(text);
        }
        return this;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mBind != null) {
            mBind.unbind();
        }
    }

    public void setOnBackListener(OnBackListener onBackListener) {
        this.onBackListener = onBackListener;
    }

    public interface OnBackListener {
        void onClick();
    }
}
