package com.zjzy.morebit.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.zjzy.morebit.App;
import com.zjzy.morebit.Module.push.Logger;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.FenleiHomeAdapter;
import com.zjzy.morebit.adapter.FenleiMenuAdapter;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.main.ui.fragment.ShowWebFragment;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ProtocolRuleBean;
import com.zjzy.morebit.pojo.event.FenleiToPage;
import com.zjzy.morebit.pojo.goods.GoodCategoryInfo;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.NetWorkUtil;
import com.zjzy.morebit.utils.OpenFragmentUtils;
import com.zjzy.morebit.utils.UI.ActivityUtils;
import com.zjzy.morebit.view.ToolbarHelper;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 首页分类-界面
 */
public class FenleiFragment extends BaseMainFragmeng implements View.OnClickListener {


    private List<GoodCategoryInfo> arrGcInfo = new ArrayList<>(); //分类信息
    private ListView lv_menu;
    private FenleiMenuAdapter flmAdapter;
    private LinearLayout search_statusbar_rl;
//    private RelativeLayout search_rl;
//    private View status_bar;
    private FrameLayout fl_course;
    private View mView;
    private ShowWebFragment mFragment;
    private ListView lv_home;
//    ImageView home_msg;

    ArrayList<String> menuList = new ArrayList<>();  // 一级分类类别
    private FenleiHomeAdapter mHomeAdapter;

    //    View dot;
    public static void start(Activity activity) {
        Bundle bundle = new Bundle();
        bundle.putString("title", "分类");
        OpenFragmentUtils.goToSimpleFragment(activity, FenleiFragment.class.getName(), bundle);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_fenlei, container, false);
        Logger.e("==onCreateView==" + mView.hashCode());
        initView(mView);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {

            new ToolbarHelper(this).setToolbarAsUp().setCustomTitle(bundle.getString("title"));
        }
        Logger.e("onViewCreated");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        Logger.e("setUserVisibleHint", "FenleiFragment 123   " + this.hashCode());
        Logger.e("setUserVisibleHint", "FenleiFragment  " + isVisibleToUser);
        Logger.e("setUserVisibleHint", "FenleiFragment 11 " + getUserVisibleHint());
//        if (getUserVisibleHint() && arrGcInfo.size() == 0 && mView != null) {
//
//        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initView(View view) {
        search_statusbar_rl = (LinearLayout) view.findViewById(R.id.search_statusbar_rl);
//        search_rl = (RelativeLayout) view.findViewById(R.id.search_rl);
//        dot = view.findViewById(R.id.dot);
//        search_rl.setBackgroundResource(R.drawable.bg_input_f6f6f6_round_6dp);

//        status_bar = (View) view.findViewById(R.id.status_bar);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //处理全屏显示
//            ViewGroup.LayoutParams viewParams = status_bar.getLayoutParams();
//            viewParams.height = ActivityStyleUtil.getStatusBarHeight(getActivity());
//            status_bar.setLayoutParams(viewParams);
//
//            ViewGroup.LayoutParams separrlParams = search_statusbar_rl.getLayoutParams();
//            separrlParams.height = ActivityStyleUtil.getStatusBarHeight(getActivity()) + DensityUtil.dip2px(getActivity(), 45);
//            search_statusbar_rl.setLayoutParams(separrlParams);
//            //            status_bar.setBackgroundResource(R.color.color_FFE742);
//        }
        lv_home = (ListView) view.findViewById(R.id.lv_home);
        lv_menu = (ListView) view.findViewById(R.id.lv_menu);
        fl_course = (FrameLayout) view.findViewById(R.id.fl_course);
//        view.findViewById(R.id.search_rl).setOnClickListener(this);
//        view.findViewById(R.id.home_msg).setOnClickListener(this);
//        home_msg = view.findViewById(R.id.home_msg);
//        home_msg.setImageResource(R.drawable.icon_xiaoxi_black);
        mHomeAdapter = new FenleiHomeAdapter(getActivity());

        lv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (menuList != null && menuList.size() > 0) {
                    Logger.e("==点击事件==" + menuList.get(position));
                    if (menuList.get(0).equals(getString(R.string.tutorial))) {
                        //有新手教程的情况
                        if (position == 0) {
                            showCourse();
                        } else {
                            hintCourse();
                            if (flmAdapter != null) {
                                flmAdapter.setSelectItem(position);
                            }
                            GoodCategoryInfo goodCategoryInfo;
                            //二级列表刷新
                            goodCategoryInfo = arrGcInfo.get(position - 1); //减去新手教程的第一个坐标
                            lv_home.setAdapter(mHomeAdapter);
                            if (goodCategoryInfo != null) {
                                mHomeAdapter.notifyData(goodCategoryInfo.getChild2(), goodCategoryInfo.getName());
                            }
                        }
                    } else {
                        //没有新手教程的情况
                        hintCourse();
                        if (flmAdapter != null) {
                            flmAdapter.setSelectItem(position);
                        }
                        GoodCategoryInfo goodCategoryInfo;
                        //二级列表刷新
                        goodCategoryInfo = arrGcInfo.get(position); //没有新手教程
                        lv_home.setAdapter(mHomeAdapter);
                        if (goodCategoryInfo != null) {
                            mHomeAdapter.notifyData(goodCategoryInfo.getChild2(), goodCategoryInfo.getName());
                        }
                    }

                    mHomeAdapter.notifyDataSetChanged();
                    flmAdapter.notifyDataSetChanged();
                }
            }
        });
        getTopFlData();//获取分类数据
    }

    private void showCourse() {
        Logger.e("setUserVisibleHint", "showCourse  123  " + this.hashCode());
        fl_course.setVisibility(View.VISIBLE);
        lv_home.setVisibility(View.GONE);
        if (flmAdapter != null) {
            flmAdapter.setSelectItem(0);
        }
    }

    private void hintCourse() {
        fl_course.setVisibility(View.GONE);
        lv_home.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.search_rl://搜索
//                startActivity(new Intent(getActivity(), SearchActivity.class));
//                break;
//            case R.id.home_msg://消息中心
//                if (!LoginUtil.checkIsLogin(getActivity())) {
//                    return;
//                }
//                if (AppUtil.isFastClick(100)) return;
//                OpenFragmentUtils.goToSimpleFragment(getActivity(), MsgFragment.class.getName(), new Bundle());
//                break;

            default:
                break;

        }
    }

    /**
     * 获取首页头部分类数据
     */
    public void getTopFlData() {

        final List<GoodCategoryInfo> list = (List<GoodCategoryInfo>) App.getACache().getAsObject(C.sp.homeFenleiData);
        if (!NetWorkUtil.isNetworkAvailable(getActivity())) {
            if (list != null && list.size() > 0) {
                Logger.e("==cache data==" + list.size());
                setCategoryData4Cache(list);
                return;
            }
        }

        RxHttp.getInstance().getGoodsService().getHomeSuerListData()
                .compose(RxUtils.<BaseResponse<List<GoodCategoryInfo>>>switchSchedulers())
                .compose(this.<BaseResponse<List<GoodCategoryInfo>>>bindToLifecycle())

                .subscribe(new DataObserver<List<GoodCategoryInfo>>(false) {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        if (list != null && list.size() > 0) {
                            //请求失败直接使用缓存数据
                            setCategoryData4Cache(list);
                        }
                    }

                    @Override
                    protected void onSuccess(List<GoodCategoryInfo> suerList) {
                        if (suerList != null && suerList.size() > 0) {
                            App.getACache().put(C.sp.homeFenleiData, (Serializable) suerList);
                            getCourseData(suerList);
                        }
                    }
                });

    }

    /**
     * 显示分类缓存数据
     *
     * @param list
     */
    private void setCategoryData4Cache(List<GoodCategoryInfo> list) {
        try {
            arrGcInfo.clear();
            EventBus.getDefault().post(new FenleiToPage(list));
            arrGcInfo.addAll(list);
            menuList.clear();
            for (int i = 0; i < arrGcInfo.size(); i++) {
                GoodCategoryInfo goodCategoryInfo = arrGcInfo.get(i);
                if (goodCategoryInfo == null) continue;
                if (!TextUtils.isEmpty(goodCategoryInfo.getName()))
                    menuList.add(goodCategoryInfo.getName());
            }

            GoodCategoryInfo goodCategoryInfo = arrGcInfo.get(0);
            lv_home.setAdapter(mHomeAdapter);
            if (goodCategoryInfo != null) {
                mHomeAdapter.notifyData(goodCategoryInfo.getChild2(), goodCategoryInfo.getName());
            }

            if (flmAdapter == null) {
                flmAdapter = new FenleiMenuAdapter(getActivity(), menuList);
            }

            if (flmAdapter != null) {
                lv_menu.setAdapter(flmAdapter);
                //设置选中 新手教程 条目
                flmAdapter.setSelectItem(0);
            }

            mHomeAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyLog.d("setUserVisibleHint", "onDestroy  " + this.hashCode());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MyLog.d("setUserVisibleHint", "onDestroyView  " + this.hashCode());
    }

    /**
     * 获取 新手教程图片地址
     *
     * @param goodCategoryInfoList
     */
    public void getCourseData(final List<GoodCategoryInfo> goodCategoryInfoList) {
        try {
            LoginUtil.getSystemStaticPage((RxAppCompatActivity) this.getActivity(), C.ProtocolType.categoryMenu)
                    .subscribe(new DataObserver<List<ProtocolRuleBean>>(false) {
                        /**
                         * 成功回调
                         *
                         * @param data 结果
                         */
                        @Override
                        protected void onSuccess(List<ProtocolRuleBean> data) {

                            if (data == null) return;

                            //设置左边分类ListView的数据显示
                            setCourseSuccessData(goodCategoryInfoList);

                            ProtocolRuleBean protocolRuleBean = data.get(0);
                            if (protocolRuleBean != null) {
                                Logger.e("==onSuccess==" + data.get(0).getHtmlUrl());
                                if (!TextUtils.isEmpty(protocolRuleBean.getHtmlUrl())) {
                                    showCourse();
                                    try {
                                        if (mFragment == null) {
                                            mFragment = ShowWebFragment.newInstance(protocolRuleBean.getHtmlUrl());
                                            ActivityUtils.replaceFragmentToActivity(
                                                    getActivity().getSupportFragmentManager(), mFragment, R.id.fl_course);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    if (flmAdapter != null)
                                        flmAdapter.setSelectItem(0);
                                }
                            }
                        }

                        @Override
                        protected void onError(String errorMsg, String errCode) {
                            super.onError(errorMsg, errCode);
                            setCourseEmptyOrErrorData(goodCategoryInfoList);
                        }
                    });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取新手教程数据成功的情况 设置相关界面数据显示
     *
     * @param goodCategoryInfoList
     */
    private void setCourseSuccessData(List<GoodCategoryInfo> goodCategoryInfoList) {
        try {
            arrGcInfo.clear();
            menuList.clear();
            EventBus.getDefault().post(new FenleiToPage(goodCategoryInfoList));
            arrGcInfo.addAll(goodCategoryInfoList);

            //添加新手教程条目
            menuList.add(getString(R.string.tutorial));

            for (int i = 0; i < arrGcInfo.size(); i++) {
                GoodCategoryInfo goodCategoryInfo = arrGcInfo.get(i);
                if (goodCategoryInfo == null) continue;
                if (!TextUtils.isEmpty(goodCategoryInfo.getName())) {
                    menuList.add(goodCategoryInfo.getName());
                }
            }

            if (flmAdapter == null) {
                flmAdapter = new FenleiMenuAdapter(getActivity(), menuList);
            }

            if (flmAdapter != null) {
                lv_menu.setAdapter(flmAdapter);
                //设置选中 新手教程 条目
                flmAdapter.setSelectItem(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取新手教程数据失败的情况 设置相关界面数据显示
     *
     * @param goodCategoryInfoList
     */
    private void setCourseEmptyOrErrorData(List<GoodCategoryInfo> goodCategoryInfoList) {
        try {
            hintCourse();
            arrGcInfo.clear();
            menuList.clear();
            EventBus.getDefault().post(new FenleiToPage(goodCategoryInfoList));
            arrGcInfo.addAll(goodCategoryInfoList);

            for (int i = 0; i < arrGcInfo.size(); i++) {
                GoodCategoryInfo goodCategoryInfo = arrGcInfo.get(i);
                if (goodCategoryInfo == null) continue;
                if (!TextUtils.isEmpty(goodCategoryInfo.getName())) {
                    menuList.add(goodCategoryInfo.getName());
                }
            }

            //显示右边分类的具体数据
            GoodCategoryInfo goodCategoryInfo = arrGcInfo.get(0);
            lv_home.setAdapter(mHomeAdapter);

            if (goodCategoryInfo != null) {
                mHomeAdapter.notifyData(goodCategoryInfo.getChild2(), goodCategoryInfo.getName());
            }
            mHomeAdapter.notifyDataSetChanged();

            if (flmAdapter == null) {
                flmAdapter = new FenleiMenuAdapter(getActivity(), menuList);
            }

            if (flmAdapter != null) {
                lv_menu.setAdapter(flmAdapter);
                //设置选中 新手教程 条目
                flmAdapter.setSelectItem(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        getDot();
    }

    private void getDot() {
//        if (dot == null) return;
//        if (HomeFragment.mUserscoreBean != null) {
//            if ((HomeFragment.mUserscoreBean.getIncomeCount() + HomeFragment.mUserscoreBean.getFansCount() + HomeFragment.mUserscoreBean.getSystemCount()) > 0) {
//                dot.setVisibility(View.VISIBLE);
//            } else {
//                dot.setVisibility(View.GONE);
//            }
//        } else {
//            dot.setVisibility(View.GONE);
//        }
    }

}
