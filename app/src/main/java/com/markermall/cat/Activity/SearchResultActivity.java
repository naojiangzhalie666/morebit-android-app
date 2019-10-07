package com.markermall.cat.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.markermall.cat.App;
import com.markermall.cat.LocalData.UserLocalData;
import com.markermall.cat.Module.common.Activity.BaseActivity;
import com.markermall.cat.Module.common.View.ReUseListView;
import com.markermall.cat.R;
import com.markermall.cat.adapter.SearchHotAdapter;
import com.markermall.cat.adapter.ShoppingListAdapter;
import com.markermall.cat.main.model.SearchStatisticsModel;
import com.markermall.cat.network.BaseResponse;
import com.markermall.cat.network.CallBackObserver;
import com.markermall.cat.network.RxHttp;
import com.markermall.cat.network.RxUtils;
import com.markermall.cat.network.RxWXHttp;
import com.markermall.cat.network.observer.DataObserver;
import com.markermall.cat.pojo.ShopGoodBean;
import com.markermall.cat.pojo.ShopGoodInfo;
import com.markermall.cat.pojo.UI.BaseTitleTabBean;
import com.markermall.cat.pojo.UserInfo;
import com.markermall.cat.pojo.goods.TaobaoSearch;
import com.markermall.cat.pojo.request.RequestSearchBean;
import com.markermall.cat.utils.ActivityStyleUtil;
import com.markermall.cat.utils.C;
import com.markermall.cat.utils.DensityUtil;
import com.markermall.cat.utils.MyGsonUtils;
import com.markermall.cat.utils.MyLog;
import com.markermall.cat.utils.SensorsDataUtil;
import com.markermall.cat.utils.ViewShowUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

/**
 * 搜索结果展示
 */

public class SearchResultActivity extends BaseActivity {
    public static final String KEY_SEARCH_TYPE = "searchType";
    public static final String KEY_SEARCH_FROM = "searchFrom";
    public  static final int SEARCH_APP = 1; //搜索APP
    public static final int SEARCH_NETWORK = 0; //搜索全网
    public  static final int FROM_SELF = 2; //来自搜索界面
    public static final int FROM_EXTERNAL = 3; //来自外部全局的搜索
    private ShoppingListAdapter mAdapter;
    private List<ShopGoodInfo> listArray = new ArrayList<>();
    private List<ShopGoodInfo> withinArray = new ArrayList<>(); //内网
    private List<ShopGoodInfo> outerArray = new ArrayList<>(); // 全网
    private String keyWord = "";
    private Bundle bundle;

    private static final int REQUEST_COUNT = 10;

    private TabLayout mTabLayout;
    private boolean isCouponShowOff = false;
    private String mMinId = "1";
    private String mPage = "1";
    private boolean isOneSwitchConpon = true; // 是否第一次切换优惠卷
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
    @BindView(R.id.list_serach_key)
    ListView mListView;
    @BindView(R.id.iv_clear)
    ImageView iv_clear;
    @BindView(R.id.couponIv)
    ImageView couponIv;
    @BindView(R.id.couponTv)
    TextView couponTv;
    private ArrayList<String> mTaobaoSearchData = new ArrayList<>();
    private SearchHotAdapter mTaobaoSearchAdapter;
    boolean isClserSearch = false;
    private boolean isFrist = true;
    private int mSearchType =1;
    private int fromSearch = FROM_SELF;
    private boolean isLoadData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchcommodity);
        initBundle();
        initView();
    }

    private void initBundle() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            keyWord = getIntent().getStringExtra("keyWord");
            //mSearchType = getIntent().getIntExtra(KEY_SEARCH_TYPE,SEARCH_APP);
            //fromSearch = getIntent().getIntExtra(KEY_SEARCH_FROM,FROM_SELF);
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
        mAdapter = new ShoppingListAdapter(SearchResultActivity.this);
        mAdapter.setShowHotTag(true);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.getSwipeList().setOnRefreshListener(new com.markermall.cat.Module.common.widget.SwipeRefreshLayout.OnRefreshListener() {
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
        initSearchAdapter();

        etSearch.setText(keyWord == null ? "" : keyWord);

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) etSearch.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SearchResultActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //这里调用搜索方法
                    searchNullTips_ly.setVisibility(View.GONE);
                    dataList_ly.setVisibility(View.VISIBLE);
                    mRecyclerView.getSwipeList().post(new Runnable() {
                        @Override
                        public void run() {
                            mListView.setVisibility(View.GONE);
                            getFirstData();
                        }
                    });
                    ViewShowUtils.hideSoftInput(SearchResultActivity.this, etSearch);
                    return true;
                }
                return false;
            }
        });

        mTabLayout = (TabLayout) findViewById(R.id.tl_tab);
//        "综合", "券后价", "销量", "奖励"
        tabList.add(new BaseTitleTabBean("综合", false, C.Setting.itemIndex));
        tabList.add(new BaseTitleTabBean("券后价", true, C.Setting.itemVoucherPrice));
        tabList.add(new BaseTitleTabBean("销量", true, C.Setting.saleMonth));
        String partner = UserLocalData.getUser(this).getPartner();
        if (null != partner && !C.UserType.member.equals(partner)) {
            tabList.add(new BaseTitleTabBean("奖励", false, C.Setting.commission));
        }
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
                if (!TextUtils.isEmpty(s.toString())) {
                    getGoodsData(s.toString());
                    isClserSearch = false;
                } else {
                    isClserSearch = true;
                    clserSearchAdapter();
                }
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

    @OnClick({R.id.search, R.id.iv_back, R.id.iv_clear,R.id.couponLayout})
    public void Onclick(View v) {
        switch (v.getId()) {
            case R.id.search:
                mListView.setVisibility(View.GONE);
                SensorsDataUtil.getInstance().searchKeyTrack(keyWord,C.BigData.NORMAL_SEARCH);
                getFirstData();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_clear: //清楚搜索关键字
                etSearch.setText("");
                break;
            case R.id.couponLayout:
                clickCouponSwitch();
                break;
            default:
                break;
        }
    }


    /**
     * 修改优惠卷状态
     */
    private void clickCouponSwitch() {
        Log.e("isCouponShowOff",isCouponShowOff+"");
        if (isCouponShowOff) {
            isCouponShowOff = false;
            couponIv.setImageResource(R.drawable.icon_search_coupon_unselect);
            couponTv.setTextColor(ContextCompat.getColor(this,R.color.tv_tablay_text));
            mRecyclerView.getSwipeList().setRefreshing(true);
            getFirstData();
            //重新读取数据
        } else {
            isCouponShowOff = true;
            couponIv.setImageResource(R.drawable.icon_search_coupon_select);
            couponTv.setTextColor(ContextCompat.getColor(this,R.color.color_333333));
            mRecyclerView.getSwipeList().setRefreshing(true);
            getFirstData();
            //重新读取数据
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
        mMinId = "1";
        mSearchType =1;
        String coupon = (isCouponShowOff == false ? 0 : 1) + "";
        mRecyclerView.getListView().setNoMore(false);
        mRecyclerView.getSwipeList().setRefreshing(true);
        fristSearch(coupon, keyWord, mMinId);
    }

    private void fristSearch(String coupon, String keywords, String minId) {

       // sendSearchStatistics(keywords);

        BaseTitleTabBean bean = tabList.get(mSelectedPos);
        mRecyclerView.getSwipeList().setRefreshing(true);
        getObservable(coupon, keywords, minId, bean)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mRecyclerView.getSwipeList().setRefreshing(false);

                    }
                })
                .subscribe(new DataObserver<ShopGoodBean>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        listArray.clear();
                        mAdapter.setData(listArray);
                        mRecyclerView.notifyDataSetChanged();
                        if (isOneSwitchConpon) {
                            isOneSwitchConpon = false;
                        } else {
                            searchNullTips_ly.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    protected void onSuccess(ShopGoodBean data) {
                        mRecyclerView.getSwipeList().setRefreshing(false);
                        dataList_ly.setVisibility(View.VISIBLE);

                        List<ShopGoodInfo> goodsList = data.getData();
                        if (goodsList != null && goodsList.size() != 0) {
                            mMinId = data.getMinId();
                            mPage = data.getPage();
                            mSearchType = data.getSearchType();
                            searchNullTips_ly.setVisibility(View.GONE);
                            listArray.clear();
                            List<ShopGoodInfo> shopGoodInfos = partList(goodsList);
                            mAdapter.setData(shopGoodInfos);
                            //设置是否是代理商
                            UserInfo userInfo = UserLocalData.getUser(SearchResultActivity.this);
                            mRecyclerView.notifyDataSetChanged();
                        } else {
                            listArray.clear();
                            mAdapter.setData(listArray);
                            mRecyclerView.notifyDataSetChanged();
                            if (isOneSwitchConpon) {
                                isOneSwitchConpon = false;
                            } else {
                                searchNullTips_ly.setVisibility(View.VISIBLE);
                            }
                        }
                        if (data.getIsPageEnd() == 1){
                            mRecyclerView.getListView().setNoMore(true);
                        }
                    }
                });
    }

    private Observable<BaseResponse<ShopGoodBean>> getObservable(String coupon, String keywords, String min_id, BaseTitleTabBean bean) {
        RequestSearchBean requestBean = new RequestSearchBean();
        requestBean.setSort(bean.where);
        requestBean.setOrder(bean.order);
        requestBean.setPage(mPage);
        requestBean.setCoupon(coupon);
        requestBean.setKeywords(keywords);
        requestBean.setMinId(min_id + "");
        requestBean.setSearchType(mSearchType);
        return RxHttp.getInstance().getGoodsService().getSearchGoodsList(requestBean)
                .compose(RxUtils.<BaseResponse<ShopGoodBean>>switchSchedulers())
                .compose(this.<BaseResponse<ShopGoodBean>>bindToLifecycle());

//        return RxHttp.getInstance().getGoodsService().getSearchGoodsList(
//                bean.where,
//                bean.order,
//                mPage,
//                coupon,
//                keywords,
//                min_id + "")
//                .compose(RxUtils.<BaseResponse<ShopGoodBean>>switchSchedulers())
//                .compose(this.<BaseResponse<ShopGoodBean>>bindToLifecycle());
    }

    /**
     * 加载更多数据
     */
    public void getMoreData() {
        keyWord = etSearch.getText().toString().trim();
        String coupon = (isCouponShowOff == true ? 1 : 0) + "";
        getMoreSearch(coupon, keyWord, mMinId);
    }

    private void getMoreSearch(String coupon, String keywords, String minId) {
        BaseTitleTabBean bean = tabList.get(mSelectedPos);
        getObservable(coupon, keywords, minId, bean)
                .subscribe(new DataObserver<ShopGoodBean>() {
                    @Override
                    protected void onDataListEmpty() {
                        mRecyclerView.getListView().refreshComplete(REQUEST_COUNT);
                        mRecyclerView.getListView().setNoMore(true);
                    }

                    @Override
                    protected void onSuccess(ShopGoodBean data) {
                        mRecyclerView.getListView().refreshComplete(REQUEST_COUNT);
                        List<ShopGoodInfo> goodsList = data.getData();
                        if (goodsList != null) {
                            if (goodsList.size() > 0) {
                                mMinId = data.getMinId();
                                mPage = data.getPage();
                                mSearchType = data.getSearchType();
                                List<ShopGoodInfo> shopGoodInfos = partList(goodsList);
                                mAdapter.setData(shopGoodInfos);
                                //设置是否是代理商
                                mRecyclerView.notifyDataSetChanged();
                            } else {
                                mRecyclerView.getListView().setNoMore(true);
                            }
                        }
                        if (data.getIsPageEnd() == 1){
                            mRecyclerView.getListView().setNoMore(true);
                        }
                    }
                });
    }

    /**
     * 分离全网 和内网
     *
     * @param goodsList
     */
    private List<ShopGoodInfo> partList(List<ShopGoodInfo> goodsList) {
        listArray.addAll(goodsList);
        outerArray.clear();
        withinArray.clear();
        for (int i = 0; i < listArray.size(); i++) {
            ShopGoodInfo shopGoodInfo = listArray.get(i);
            if (shopGoodInfo.isSearchType == 1) { // 排除分割
                continue;
            }
            if ("0".equals(shopGoodInfo.getItemFrom())) {//全给
                outerArray.add(shopGoodInfo);
            } else {
                withinArray.add(shopGoodInfo);
            }
        }
        listArray.clear();
        listArray.addAll(withinArray);
        if (outerArray != null && outerArray.size() != 0) {
            listArray.add(new ShopGoodInfo(1));
            listArray.addAll(outerArray);
        }
        return listArray;
    }


    /**
     * @return
     */
    public void getGoodsData(final String string) {

        final String taobaoSearchUrl = "https://suggest.taobao.com/sug?code=utf-8&q=" + string + "&k=1&area=c2c";

        RxWXHttp.getInstance().getService(C.BASE_YUMIN).profilePicture(taobaoSearchUrl)
                .compose(RxUtils.<String>switchSchedulers())
                .compose(this.<String>bindToLifecycle())
                .map(new Function<String, TaobaoSearch>() {
                    @Override
                    public TaobaoSearch apply(String s) throws Exception {
                        String s1 = delHTMLTag(s);
                        TaobaoSearch taobaoSearch = (TaobaoSearch) MyGsonUtils.jsonToBean(s1, TaobaoSearch.class);
                        return taobaoSearch;
                    }
                })
                .subscribe(new CallBackObserver<TaobaoSearch>() {
                    @Override
                    public void onNext(TaobaoSearch search) {
                        MyLog.d("SearchActivity ", Thread.currentThread().getName());

                        if (isClserSearch) {
                            clserSearchAdapter();
                            return;
                        }

                        if (search != null && search.getResult() != null && search.getResult().size() != 0) {
//                            List<String> strings = search.getResult().get(1);
                            mTaobaoSearchData.clear();
                            for (int i = 0; i < search.getResult().size(); i++) {
                                List<String> strings = search.getResult().get(i);
                                String s = strings.get(0);
                                if (!TextUtils.isEmpty(s)) {
                                    mTaobaoSearchData.add(s);
                                }
                            }
                            mListView.setVisibility(View.VISIBLE);
                            mTaobaoSearchAdapter.setData(mTaobaoSearchData);
                            mTaobaoSearchAdapter.notifyDataSetChanged();

                        } else {
                            clserSearchAdapter();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        clserSearchAdapter();
                    }


                });


    }

    private void initSearchAdapter() {
        try {
            if (mTaobaoSearchAdapter == null) {
                mTaobaoSearchAdapter = new SearchHotAdapter(SearchResultActivity.this, mTaobaoSearchData);
                mListView.setAdapter(mTaobaoSearchAdapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ((InputMethodManager) etSearch.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                                .hideSoftInputFromWindow(SearchResultActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        String s = mTaobaoSearchData.get(position);
                        mListView.setVisibility(View.GONE);
                        keyWord = s;
                        etSearch.setText(keyWord);
                        addSearchText(s);
                        getFirstData();


                    }
                });
            }

        } catch (Exception e) {
        }
    }

    private void addSearchText(String text) {
        ArrayList<String> asObject = (ArrayList<String>) App.getACache().getAsObject(C.sp.searchHotKey);
        if (asObject == null) {
            asObject = new ArrayList<>();
        }
        boolean isContains = false; //是否包含

        for (String string : asObject) {
            if (string.equals(text)) {
                isContains = true;
                break;
            }
        }
        if (!isContains) {
            asObject.add(0, text);
        }
        App.getACache().put(C.sp.searchHotKey, asObject);
    }

    private void clserSearchAdapter() {
        mListView.setVisibility(View.GONE);
        mTaobaoSearchAdapter.setData(mTaobaoSearchData);
        mTaobaoSearchData.clear();
        mTaobaoSearchAdapter.notifyDataSetChanged();
    }

    private String delHTMLTag(String content) {
        String REGEX_HTML = "<[^>]+>";
        Pattern p_html = Pattern.compile(REGEX_HTML, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(content);
        content = m_html.replaceAll("");
        return content.trim();
    }


    /**
     * 搜索统计
     * @param keywords
     */
    private void sendSearchStatistics(String keywords){
        String id = "";
        String type = "";
        if(fromSearch == FROM_SELF){
            id = C.SearchStatistics.ID_SEARCH;
            type = C.SearchStatistics.TYPE_SEARCH;
        }else if(fromSearch == FROM_EXTERNAL){
            id = C.SearchStatistics.ID_COPY_SEARCH;
            type = C.SearchStatistics.TYPE_COPY_SEARCH;
            if(fromSearch == FROM_EXTERNAL){
                fromSearch = FROM_SELF;
            }
        }
        SearchStatisticsModel.getInstance().sendSearchStatistics(this,keywords,id,type)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {


                    }
                })
                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        Log.e("",errorMsg);
                    }

                    @Override
                    protected void onSuccess(String data) {
                        Log.e("",data);

                    }
                });
    }
}
