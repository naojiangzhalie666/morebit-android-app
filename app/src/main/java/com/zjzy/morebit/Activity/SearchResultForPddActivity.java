package com.zjzy.morebit.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.zjzy.morebit.App;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.View.ReUseListView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.SearchHotAdapter;
import com.zjzy.morebit.adapter.ShoppingListForPddAdapter;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UI.BaseTitleTabBean;
import com.zjzy.morebit.pojo.request.RequestSearchForPddBean;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.DensityUtil;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.ViewShowUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * 搜索结果展示
 */

public class SearchResultForPddActivity extends BaseActivity {

    public static final String KEY_SEARCH_FROM = "searchFrom";
    public  static final int FROM_SELF = 2; //来自搜索界面
    public static final int FROM_EXTERNAL = 3; //来自外部全局的搜索
    private ShoppingListForPddAdapter mAdapter;
    private List<ShopGoodInfo> listArray = new ArrayList<>();
    private String keyWord = "";
    private Bundle bundle;

    private static final int REQUEST_COUNT = 10;

    private TabLayout mTabLayout;


    private String mPage = "1";

    private ArrayList<BaseTitleTabBean> tabList = new ArrayList<>();
    private int mSelectedPos;
    @BindView(R.id.searchNullTips_ly)
    LinearLayout searchNullTips_ly;
    @BindView(R.id.listview)
    ReUseListView mRecyclerView;
    @BindView(R.id.search_et)
    EditText etSearch;
    @BindView(R.id.dataList_ly)
    LinearLayout dataList_ly;
    @BindView(R.id.iv_clear)
    ImageView iv_clear;


    private SearchHotAdapter mTaobaoSearchAdapter;
    boolean isClserSearch = false;
    private boolean isFrist = true;

    private int fromSearch = FROM_SELF;
    private boolean isLoadData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchcommodity_pdd);
        initBundle();
        initView();
    }

    private void initBundle() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            keyWord = getIntent().getStringExtra("keyWord");
        }
    }

    /**
     * 重新加载数据
     */
    private void reLoadData() {
        getFirstData();
    }

    public void initView() {
        ActivityStyleUtil.initSystemBar(this, R.color.white); //设置标题栏颜色值
        mAdapter = new ShoppingListForPddAdapter(SearchResultForPddActivity.this);
//        mAdapter.setShowHotTag(true);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.getSwipeList().setOnRefreshListener(new com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstData();
            }
        });

        mRecyclerView.getListView().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!mRecyclerView.getSwipeList().isRefreshing())
                    getMoreData();
            }
        });
//        initSearchAdapter();

        etSearch.setText(keyWord == null ? "" : keyWord);

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) etSearch.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SearchResultForPddActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //这里调用搜索方法
                    searchNullTips_ly.setVisibility(View.GONE);
                    dataList_ly.setVisibility(View.VISIBLE);
                    mRecyclerView.getSwipeList().post(new Runnable() {
                        @Override
                        public void run() {

                            getFirstData();
                        }
                    });
                    ViewShowUtils.hideSoftInput(SearchResultForPddActivity.this, etSearch);
                    return true;
                }
                return false;
            }
        });

        mTabLayout = (TabLayout) findViewById(R.id.tl_tab);
//        "综合", "券后价", "销量", "奖励"
        tabList.add(new BaseTitleTabBean("综合", false, ""));
        tabList.add(new BaseTitleTabBean("佣金比例", true, C.Setting.sort_commissionShare));
        tabList.add(new BaseTitleTabBean("销量", true, C.Setting.sort_inOrderCount30Days));
//        String partner = UserLocalData.getUser(this).getPartner();
//        if (null != partner && !C.UserType.member.equals(partner)) {
        tabList.add(new BaseTitleTabBean("奖励", false, C.Setting.sort_price));
//        }
        initTab(mTabLayout);
        if (!TextUtils.isEmpty(keyWord)) {
            etSearch.setText(keyWord);
        }
        //默认选择第一个
        reLoadData();
    }

    @OnTextChanged(value = R.id.search_et, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterTextChanged(Editable s) {
        //判断是否显示删除按钮
        if (TextUtils.isEmpty(etSearch.getText().toString().trim())) {
            if (iv_clear.getVisibility() != View.GONE) {
                iv_clear.setVisibility(View.GONE);
            }
        } else {
            if (iv_clear.getVisibility() != View.VISIBLE) {
                iv_clear.setVisibility(View.VISIBLE);
            }
        }
        MyLog.i("test", "isFrist: " + isFrist);
        MyLog.i("test", "editable: " + s.toString());
        if (!s.toString().equals(keyWord)) {
            try {
//                if (!TextUtils.isEmpty(s.toString())) {
//                    getGoodsData(s.toString());
//                    isClserSearch = false;
//                } else {
//                    isClserSearch = true;
//                    clserSearchAdapter();
//                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * 初始化TabLayout
     */
    private void initTab(final TabLayout tabLayout) {
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabTextColors(getResources().getColor(R.color.color_666666), getResources().getColor(R.color.color_666666));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.top_head));
        tabLayout.setSelectedTabIndicatorHeight(DensityUtil.dip2px(this, 0));

        //填充数据
        for (int i = 0; i < tabList.size(); i++) {
            BaseTitleTabBean bean = tabList.get(i);
            bean.order = C.Setting.descParms;
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setCustomView(R.layout.tablayout_donw_up_item_tv);
            TextView textView = (TextView) tab.getCustomView().findViewById(R.id.class_tv);

            textView.setText(bean.name);//设置tab上的文字
            tab.setTag(i);
            tabLayout.addTab(tab);
            //初始化上下按钮
            LinearLayout linearLayout = (LinearLayout) tab.getCustomView().findViewById(R.id.class_icon_ly);

            if (bean.isSelect) {
                linearLayout.setVisibility(View.VISIBLE);
                switchTab(tabLayout, i, bean, true);
            } else {
                linearLayout.setVisibility(View.INVISIBLE);
            }

        }

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            /**
             * 新选
             * @param tab
             */
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mSelectedPos = (int) tab.getTag();
                BaseTitleTabBean bean = tabList.get(mSelectedPos);
                switchTab(tabLayout, mSelectedPos, bean, false);
                getFirstData();
            }

            /**
             * 当tab从 选择 ->未选择
             * @param tab
             */
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int pos = (int) tab.getTag();
                BaseTitleTabBean bean = tabList.get(pos);
                bean.order =  C.Setting.descParms;// 清空未选择的状态
                switchTab(tabLayout, pos, bean, true);
            }

            /**
             * 复选
             * @param tab
             */
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int getPos = (int) tab.getTag();
                BaseTitleTabBean bean = tabList.get(getPos);
                if (bean.isSelect) {
                    bean.order =  C.Setting.ascParms.equals(bean.order) ?  C.Setting.descParms : C.Setting. ascParms;
                    switchTab(tabLayout, getPos, bean, false);
                    getFirstData();
                }
            }
        });
    }

    private void switchTab(TabLayout tabLayout, int i, BaseTitleTabBean switchTop, boolean isInit) {
        ImageView textIcon1 = (ImageView) tabLayout.getTabAt(i).getCustomView().findViewById(R.id.class_icon_up);
        ImageView textIcon2 = (ImageView) tabLayout.getTabAt(i).getCustomView().findViewById(R.id.class_icon_down);
        if (isInit) {
            textIcon1.setImageResource(R.drawable.icon_jiageshangxuanzhong);
            textIcon2.setImageResource(R.drawable.icon_jiagexia);
        } else {
            if ( C.Setting.ascParms.equals(switchTop.order)) {
                textIcon1.setImageResource(R.drawable.icon_jiageshangxuanzhong);
                textIcon2.setImageResource(R.drawable.icon_jiagexiaxuanzhong);
            } else {
                textIcon1.setImageResource(R.drawable.icon_jiageshang);
                textIcon2.setImageResource(R.drawable.icon_jiagexia);
            }
        }
    }

    @OnClick({R.id.search, R.id.iv_back, R.id.iv_clear})
    public void Onclick(View v) {
        switch (v.getId()) {
            case R.id.search:

                getFirstData();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_clear: //清楚搜索关键字
                etSearch.setText("");
                break;
            default:
                break;
        }
    }

    /*
     * 第一次获取数据
     */
    public void getFirstData() {
        keyWord = etSearch.getText().toString().trim();
        if (TextUtils.isEmpty(keyWord)) {
            mRecyclerView.getSwipeList().setRefreshing(false);
            ViewShowUtils.showShortToast(this, "请输入搜索内容");
            return;
        }
        mPage = "1";
        mRecyclerView.getListView().setNoMore(false);
        mRecyclerView.getSwipeList().setRefreshing(true);
        fristSearch(keyWord);
    }

    private void fristSearch(String keywords) {
        BaseTitleTabBean bean = tabList.get(mSelectedPos);
        mRecyclerView.getSwipeList().setRefreshing(true);
        bean.where = "";
        bean.order = "";
        getObservable( keywords, bean)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mRecyclerView.getSwipeList().setRefreshing(false);

                    }
                })
                .subscribe(new DataObserver<List<ShopGoodInfo> >() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        listArray.clear();
                        mAdapter.setData(listArray);
                        mRecyclerView.notifyDataSetChanged();
                        searchNullTips_ly.setVisibility(View.VISIBLE);

                    }

                    @Override
                    protected void onSuccess(List<ShopGoodInfo> data) {
                        mRecyclerView.getSwipeList().setRefreshing(false);
                        dataList_ly.setVisibility(View.VISIBLE);
                        if (data != null && data.size() != 0) {
                            searchNullTips_ly.setVisibility(View.GONE);
                            mAdapter.setData(data);
                        } else {
                            mAdapter.setData(listArray);
                            searchNullTips_ly.setVisibility(View.VISIBLE);
                        }
                        mRecyclerView.notifyDataSetChanged();
                        if (data != null && data.size() == 0){
                            mRecyclerView.getListView().setNoMore(true);
                        }
                    }
                });
    }

    private Observable<BaseResponse<List<ShopGoodInfo>>> getObservable(String keywords, BaseTitleTabBean bean) {
        RequestSearchForPddBean requestBean = new RequestSearchForPddBean();
        requestBean.setSort(bean.where);
        requestBean.setOrder(bean.order);
        requestBean.setPage(mPage);
        requestBean.setKeyword(keywords);
        return RxHttp.getInstance().getGoodsService().getSearchGoodsListForPdd(requestBean)
                .compose(RxUtils.<BaseResponse<List<ShopGoodInfo>>>switchSchedulers())
                .compose(this.<BaseResponse<List<ShopGoodInfo>>>bindToLifecycle());


    }

    /**
     * 加载更多数据
     */
    public void getMoreData() {
        keyWord = etSearch.getText().toString().trim();

        getMoreSearch(keyWord);
    }

    private void getMoreSearch( String keywords) {
        BaseTitleTabBean bean = tabList.get(mSelectedPos);
        getObservable(keywords, bean)
                .subscribe(new DataObserver<List<ShopGoodInfo>>() {
                    @Override
                    protected void onDataListEmpty() {
                        mRecyclerView.getListView().refreshComplete(REQUEST_COUNT);
                        mRecyclerView.getListView().setNoMore(true);
                    }

                    @Override
                    protected void onSuccess(List<ShopGoodInfo>  data) {
                        mRecyclerView.getListView().refreshComplete(REQUEST_COUNT);

                        if (data != null) {
                            if (data.size() > 0) {
                                mAdapter.setData(data);
                                //设置是否是代理商
                                mRecyclerView.notifyDataSetChanged();
                            } else {
                                mRecyclerView.getListView().setNoMore(true);
                            }
                        }
                    }
                });
    }

}
