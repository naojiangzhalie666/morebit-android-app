package com.jf.my.view.helper;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jf.my.R;


/**
 * Created by YangBoTian on 2018/6/12 18:44
 */

public class ToolbarWebHelper {

    private final int mToolbarId = R.id.ll_toolbar;
    private final int mTitleId = R.id.toolbar_title;
    private final int mIvBack = R.id.iv_back;
    private final int mIvOff = R.id.iv_off;
    private final int mIvRefresh = R.id.iv_refresh;
    private AppCompatActivity mActivity;
    private Fragment mFragment;
    private TextView mTitle;
    private ImageView mTextViewOff;
    private ImageView mRefresh;
    private ImageView mIvBackView;
    private ImageView mShare;
    private RelativeLayout mLlToolbar;

    public ToolbarWebHelper(Fragment fragment) {
        mFragment = fragment;
        ensureToolbar();
    }

    public ToolbarWebHelper(AppCompatActivity activity) {
        mActivity = activity;
        ensureToolbar();
    }


    public ToolbarWebHelper setCustomTitle(String title) {
        if (mTitle != null) {
            mTitle.setText(title);
        }
        return this;
    }


    private void ensureToolbar() {
        if (mLlToolbar == null) {
            if (mFragment != null) {
                mLlToolbar = getToolbarFromFragment(mFragment);
            } else {
                mLlToolbar = getToolbarFromActivity(mActivity);
            }

            if (mLlToolbar != null) {
                mTitle = (TextView) mLlToolbar.findViewById(mTitleId);
                mIvBackView = (ImageView) mLlToolbar.findViewById(mIvBack);
                mTextViewOff = (ImageView) mLlToolbar.findViewById(mIvOff);
                mRefresh = (ImageView) mLlToolbar.findViewById(mIvRefresh);
                mShare = (ImageView) mLlToolbar.findViewById(R.id.iv_share);
            }
        }
    }

    private RelativeLayout getToolbarFromActivity(AppCompatActivity activity) {
        return (RelativeLayout) activity.findViewById(mToolbarId);
    }

    public RelativeLayout getToolbarFromFragment(Fragment fragment) {
        return (RelativeLayout) fragment.getView().findViewById(mToolbarId);
    }

    public ToolbarWebHelper setCustomOff(final View.OnClickListener onClickListener) {
        if (mTextViewOff != null) {
            if (onClickListener != null) {
                mTextViewOff.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickListener.onClick(v);
                    }
                });
            }
        }
        return this;
    }

    public ToolbarWebHelper setCustomRefresh(final View.OnClickListener onClickListener) {
        if (mRefresh != null) {
            if (onClickListener != null) {
                mRefresh.setVisibility(View.VISIBLE);
                mRefresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickListener.onClick(v);
                    }
                });
            }
        }
        return this;
    }

    public ToolbarWebHelper setCustomShare(final View.OnClickListener onClickListener) {
        if (mShare != null) {
            if (onClickListener != null) {
                mShare.setVisibility(View.VISIBLE);
                mShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickListener.onClick(v);
                    }
                });
            }
        }
        return this;
    }
    public ToolbarWebHelper setCustomBack(final View.OnClickListener onClickListener) {
        if (mIvBackView != null) {
            if (onClickListener != null) {
                mIvBackView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickListener.onClick(v);
                    }
                });
            }
        }
        return this;
    }

    public void setOffVisibility(int visibility) {
        if (mTextViewOff != null)
            mTextViewOff.setVisibility(visibility);
    }

}
