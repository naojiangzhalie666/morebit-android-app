package com.jf.my.circle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.jf.my.Module.push.Logger;
import com.jf.my.R;
import com.jf.my.circle.adapter.CollegeFragmentPageAdapter;
import com.jf.my.circle.contract.CollegeListContract;
import com.jf.my.circle.presenter.CollegeListPresenter;
import com.jf.my.circle.ui.SearchArticleListActitivty;
import com.jf.my.circle.ui.TutorialFragment;
import com.jf.my.mvp.base.base.BaseView;
import com.jf.my.mvp.base.frame.MvpActivity;
import com.jf.my.pojo.requestbodybean.RequestModelId;
import com.jf.my.pojo.requestbodybean.RequestModelIdData;
import com.jf.my.utils.action.MyAction;
import com.jf.my.view.CollegeCategoryPopWindow;
import com.jf.my.view.ToolbarHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by JerryHo on 2019/3/15
 * Description: 商学院列表界面
 */
public class CollegeListActivity extends MvpActivity<CollegeListPresenter> implements CollegeListContract.View {
    public static final int CIRCLE_COURSE = 1;   //商学院
    public static final int NEW_COURSE = 2;    //新手教程

    @BindView(R.id.tab)
    SlidingTabLayout mSlidingTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.search)
    LinearLayout ll_search;
    @BindView(R.id.ll_category)
    LinearLayout ll_category;
    @BindView(R.id.btn_more)
    ImageView btn_more;

    private ArrayList<Fragment> mFragments;
    private String[] mTitles;
    private CollegeFragmentPageAdapter mAdapter;
    private int type = 1;
    private String mId;
    CollegeCategoryPopWindow mCollegeCategoryPopWindow;
    private List<String> mHomeColumns = new ArrayList<>();

    public static void start(Activity activity, String title, String id, int type) {
        Intent intent = new Intent(activity, CollegeListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("id", id);
        bundle.putInt("type", type);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragments = new ArrayList<>();

        String title = getIntent().getStringExtra("title");
        mId = getIntent().getStringExtra("id");
        type = getIntent().getIntExtra("type", 1);

        if (!TextUtils.isEmpty(title)) {
            new ToolbarHelper(this).setToolbarAsUp().setCustomTitle(title);
        } else {
            new ToolbarHelper(this).setToolbarAsUp().setCustomTitle(getString(R.string.college_list));
        }


        if (type == CIRCLE_COURSE) {
            if (!TextUtils.isEmpty(mId)) {
                //查询二级分类tab
                mPresenter.getCollegeListData(this, new RequestModelId().setModelId(Integer.parseInt(mId)), type);
            }
        } else if (type == NEW_COURSE) {
            mPresenter.getCollegeListData(this, new RequestModelId().setModelId(Integer.parseInt(mId)), type);
        }

        initListener();
    }


    private void initListener() {
        //Tab界面切换取消播放释放资源
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mSlidingTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_college;
    }

    @Override
    public void onCollegeListSuccessful(List<RequestModelIdData> data) {
        Logger.e("==onCollegeListSuccessful==" + data.size());
        if (data != null && data.size() > 0) {

            int size = data.size();
            mHomeColumns.clear();
            if (size == 1) {
                //只有一个tab 隐藏显示二级分类
                ll_category.setVisibility(View.GONE);
                ll_search.setVisibility(View.GONE);
                mTitles = new String[size];
                mTitles[0] = data.get(0).getTwoLevelName();
                mHomeColumns.add(data.get(0).getTwoLevelName());
                TutorialFragment tutorialFragment = TutorialFragment.newInstance(data.get(0).getId(), 0);
                mFragments.add(tutorialFragment);
                mAdapter = new CollegeFragmentPageAdapter(getSupportFragmentManager(), mFragments);
                mViewPager.setAdapter(mAdapter);
                mViewPager.setOffscreenPageLimit(size);
            } else {
                mTitles = new String[size + 1];
                ll_search.setVisibility(View.VISIBLE);
                ll_category.setVisibility(View.VISIBLE);
                RequestModelIdData requestModelIdData = new RequestModelIdData();
                requestModelIdData.setTwoLevelName("全部");
                data.add(0, requestModelIdData);
                for (int i = 0; i < data.size(); i++) {
                    mTitles[i] = data.get(i).getTwoLevelName();
                    mHomeColumns.add(data.get(i).getTwoLevelName());
                    TutorialFragment tutorialFragment;
                    if (i == 0) {
                        tutorialFragment = TutorialFragment.newInstance(Integer.parseInt(mId), TutorialFragment.ALL);
                    } else {
                        tutorialFragment = TutorialFragment.newInstance(data.get(i).getId(), 0);
                    }
                    mFragments.add(tutorialFragment);
                }
                mAdapter = new CollegeFragmentPageAdapter(getSupportFragmentManager(), mFragments);
                mViewPager.setAdapter(mAdapter);
                mViewPager.setOffscreenPageLimit(size);
                mSlidingTabLayout.setViewPager(mViewPager, mTitles);
            }


        }
    }

    @Override
    public void onCollegeListEmpty() {
        Logger.e("==onCollegeListEmpty==");
        ll_category.setVisibility(View.GONE);
    }

    @Override
    public void onCollegeListFinally() {

        Logger.e("==onCollegeListFinally==");
    }

    @OnClick({R.id.btn_more, R.id.search})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_more:
                showPopupWindow();
                break;
            case R.id.search:
                SearchArticleListActitivty.start(CollegeListActivity.this);
                break;
        }

    }

    private void showPopupWindow() {
        if (mCollegeCategoryPopWindow == null) {
            mCollegeCategoryPopWindow = new CollegeCategoryPopWindow(this, mHomeColumns, new MyAction.One<Integer>() {
                @Override
                public void invoke(Integer arg) {
                    if (arg >= 0) {
                        mViewPager.setCurrentItem(arg);
                        mCollegeCategoryPopWindow.dismiss();
                    }
                    btn_more.setImageResource(R.drawable.icon_college_category_down);
                }
            });

        }

        if (!mCollegeCategoryPopWindow.isShowing()) {
            mCollegeCategoryPopWindow.setSelected(mViewPager.getCurrentItem());
            btn_more.setImageResource(R.drawable.icon_college_category_up);
            mCollegeCategoryPopWindow.showAsDropDown(mSlidingTabLayout);
        }
    }
}
