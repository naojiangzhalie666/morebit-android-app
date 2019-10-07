package com.jf.my.circle.ui;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jf.my.Module.common.Fragment.BaseFragment;
import com.jf.my.R;
import com.jf.my.fragment.CircleDayHotFragment;
import com.jf.my.utils.C;
import com.jf.my.utils.OpenFragmentUtils;
import com.jf.my.utils.UI.ActivityUtils;
import com.jf.my.utils.ViewShowUtils;
import com.jf.my.view.ClearEditText;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * - @Description:  蜜粉圈搜索
 * - @Author:
 * - @Time:  2019/9/4 14:21
 **/
public class CircleSearchListFragment extends BaseFragment {

    @BindView(R.id.search)
    TextView mSearch;
    @BindView(R.id.search_et)
    ClearEditText mSearchEt;
    private View mView;
    private CircleDayHotFragment mFragment;

    public CircleSearchListFragment() {
    }

    public static void start(Activity activity, String name) {
        Bundle args = new Bundle();
        args.putString(C.sp.CIRCLE_SEARCH_NAME, name);
        OpenFragmentUtils.goToSimpleFragment(activity, CircleSearchListFragment.class.getName(), args);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_circle_search_list, container, false);
        }
        return mView;
    }

    public void initView(View view) {

        Bundle arguments = getArguments();
        String name = arguments.getString(C.sp.CIRCLE_SEARCH_NAME);
        if (!TextUtils.isEmpty(name)) {
            mSearchEt.setText(name);
        }
        mFragment = CircleDayHotFragment.newInstance(arguments);
        ActivityUtils.replaceFragmentToActivity(
                getChildFragmentManager(), mFragment, R.id.fl_img);


    }

    @OnClick({R.id.search, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search:
                if (TextUtils.isEmpty(mSearchEt.getText().toString().trim())) {
                    ViewShowUtils.showShortToast(getActivity(), "请输入搜索内容");
                    mSearchEt.requestFocus();
                } else {
                    if (mFragment != null) {
                        mFragment.setSearchName(mSearchEt.getText().toString().trim());
                        mFragment.getFirstData();
                    }
                }
                break;
            case R.id.iv_back:
                getActivity().finish();
                break;

            default:
                break;
        }
    }
}
