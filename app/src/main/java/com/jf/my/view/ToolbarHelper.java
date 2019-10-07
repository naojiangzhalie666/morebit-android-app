package com.jf.my.view;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jf.my.R;

import butterknife.OnClick;


/**
 * Created by YangBoTian on 2018/6/12 18:44
 */

public class ToolbarHelper {

    private final int mToolbarId = R.id.toolbar;
    private final int mTitleId = R.id.toolbar_title;
    private final int mRighTitleId = R.id.toolbar_right_title;
    private final int mRighImgId = R.id.toolbar_right_img;
    private Toolbar mToolbar;
    private AppCompatActivity mActivity;
    private Fragment mFragment;
    private TextView mTitle;
    private TextView mRightTitle;
    private ImageView mRightImg;
    public ToolbarHelper(Fragment fragment) {
        this((AppCompatActivity) fragment.getActivity());
        mFragment = fragment;
    }

    public ToolbarHelper(AppCompatActivity activity) {
        mActivity = activity;
    }

    public ToolbarHelper setToolbarAsUp(View.OnClickListener clickListener) {
        ensureToolbar();
        mActivity.setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.btn_title_return_icon);
        mToolbar.setNavigationOnClickListener(clickListener);
        return this;
    }

    public ToolbarHelper setToolbarAsUp() {
        setToolbarAsUp(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationHelper.navigateUpOrBack(mActivity, null);
            }
        });
        return this;
    }

    private void ensureToolbar() {
        if (mToolbar == null) {
            if (mFragment != null) {
                mToolbar = getToolbarFromFragment(mFragment);
            } else {
                mToolbar = getToolbarFromActivity(mActivity);
            }

            if (mToolbar != null) {
                mTitle = (TextView) mToolbar.findViewById(mTitleId);
                mRightTitle = (TextView) mToolbar.findViewById(mRighTitleId);
                mRightImg = (ImageView) mToolbar.findViewById(mRighImgId);
            }
        }
    }

    private Toolbar getToolbarFromActivity(AppCompatActivity activity) {
        return (Toolbar) activity.findViewById(mToolbarId);
    }

    @Nullable
    public Toolbar getToolbar() {
        return mToolbar;
    }

    public Toolbar getToolbarFromFragment(Fragment fragment) {
        return (Toolbar) fragment.getView().findViewById(mToolbarId);
    }

    public ToolbarHelper setCustomTitle(String title) {
        setTitle(null);
        mToolbar.setTitle(null);
        if (mTitle != null) {
            mTitle.setText(title);
        }
        return this;
    }
    public ToolbarHelper setCustomRightTitle(String title, final View.OnClickListener onClickListener) {
        if (mRightTitle != null) {
            mRightTitle.setVisibility(View.VISIBLE);
            mRightTitle.setText(title);
            if(onClickListener!=null){
                mRightTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickListener.onClick(v);
                    }
                });

            }
        }
        return this;
    }
    public ToolbarHelper setCustomRightImg(int drawableId, final View.OnClickListener onClickListener) {
        if (mRightImg != null) {
            mRightImg.setVisibility(View.VISIBLE);
            mRightImg.setImageResource(drawableId);
            if(onClickListener!=null){
                mRightImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickListener.onClick(v);
                    }
                });

            }
        }
        return this;
    }
    private void setTitle(String title) {
        ActionBar supportActionBar = mActivity.getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(title);
        }
    }


    public ToolbarHelper setCustomTitle(@StringRes int title) {
        setTitle(null);
        mToolbar.setTitle(null);
        if (mTitle != null) {
            mTitle.setText(title);
        }
        return this;
    }
    public ToolbarHelper setCustomRightTitle(@StringRes int title,final View.OnClickListener onClickListener) {
        if (mRightTitle != null) {
            mRightTitle.setVisibility(View.VISIBLE);
            mRightTitle.setText(title);
            if(onClickListener!=null){
                mRightTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickListener.onClick(v);
                    }
                });

            }
        }
        return this;
    }

    @Nullable
    public TextView getTitleView() {
        return mTitle;
    }


}
