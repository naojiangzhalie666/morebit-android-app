package com.markermall.cat.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.markermall.cat.App;
import com.markermall.cat.Module.common.Activity.BaseActivity;
import com.markermall.cat.Module.common.Dialog.ClearSDdataDialog;
import com.markermall.cat.R;
import com.markermall.cat.adapter.SearchHotAdapter;
import com.markermall.cat.adapter.SimpleAdapter;
import com.markermall.cat.adapter.TodayGoodAdapter;
import com.markermall.cat.adapter.holder.SimpleViewHolder;
import com.markermall.cat.main.model.ConfigModel;
import com.markermall.cat.main.model.SearchStatisticsModel;
import com.markermall.cat.network.CallBackObserver;
import com.markermall.cat.network.RxUtils;
import com.markermall.cat.network.RxWXHttp;
import com.markermall.cat.network.observer.DataObserver;
import com.markermall.cat.pojo.ImageInfo;
import com.markermall.cat.pojo.SearchHotKeyBean;
import com.markermall.cat.pojo.ShopGoodInfo;
import com.markermall.cat.pojo.SystemConfigBean;
import com.markermall.cat.pojo.goods.TaobaoSearch;
import com.markermall.cat.utils.C;
import com.markermall.cat.utils.ConfigListUtlis;
import com.markermall.cat.utils.DensityUtil;
import com.markermall.cat.utils.LoadImgUtils;
import com.markermall.cat.utils.MyGsonUtils;
import com.markermall.cat.utils.MyLog;
import com.markermall.cat.utils.SensorsDataUtil;
import com.markermall.cat.utils.StringsUtils;
import com.markermall.cat.utils.UI.BannerInitiateUtils;
import com.markermall.cat.utils.ViewShowUtils;
import com.markermall.cat.utils.action.MyAction;
import com.markermall.cat.view.GridSpacingItemDecoration;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnTextChanged;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;


/**
 * Created by fengrs on 2018/11/26 0011.
 * 搜索页
 */

public class SearchActivity extends BaseActivity implements View.OnClickListener {
    private  static final int KEY＿SEARCH_APP = 1; //搜索APP
    private static final int KEY＿SEARCH_NETWORK = 0; //搜索全网
    @BindView(R.id.mListView)
    ListView mListView;
    @BindView(R.id.bgIv)
    ImageView bgIv;
    @BindView(R.id.guideIv)
    ImageView guideIv;
    @BindView(R.id.moreHistoryLL)
    LinearLayout moreHistoryLL;
    @BindView(R.id.hotSearchLL)
    LinearLayout hotSearchLL;
    @BindView(R.id.moreHistoryTv)
    TextView moreHistoryTv;
    @BindView(R.id.arrowIv)
    ImageView arrowIv;
    @BindView(R.id.todayGoodRv)
    RecyclerView todayGoodRv;
    @BindView(R.id.todayLayout)
    LinearLayout todayLayout;
    @BindView(R.id.toDayTv)
    TextView toDayTv;
    @BindView(R.id.guideTitleTv)
    TextView guideTitleTv;
    TodayGoodAdapter todayGoodAdapter;
    @BindView(R.id.videoPlayIv)
    ImageView videoPlayIv;
    private EditText etSearch;
    SharedPreferences mSharedPreference;

    private LinearLayout searchHistory;
    private LinearLayout hotKey_list;
    private RelativeLayout historyReLay;

    private ImageView clearLy;
    private View clearLy_bottom_line;
    private SearchHotAdapter mHotkeyAdapter, mTaobaoSearchAdapter;
    private ArrayList<String> mArrayList = new ArrayList<>();
    private ArrayList<String> mTaobaoSearchData;

    boolean isClserSearch = false;
    boolean isShowMoreHistory = false;
    private int mSearchType = 1;
    private List<SearchHotKeyBean> mSearchHotKeyDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mSharedPreference = getSharedPreferences("user", Context.MODE_PRIVATE);
        inview();
    }

    public void inview() {
        mTaobaoSearchData = new ArrayList<>();
        ImmersionBar.with(this)
                .keyboardEnable(true)  //解决软键盘与底部输入框冲突问题
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                //.statusBarColor(R.color.color_FFD800)     //状态栏颜色，不写默认透明色
                //.fitsSystemWindows(true)    //解决状态栏和布局重叠问题，任选其一，默认为false，当为true时一定要指定statusBarColor()，不然状态栏为透明色，还有一些重载方法
                .titleBar(bgIv)
                .init();
        etSearch = (EditText) findViewById(R.id.search_et);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.search).setOnClickListener(this);
        moreHistoryLL.setOnClickListener(this);
//        btn_back.setOnClickListener(this);
        searchHistory = (LinearLayout) findViewById(R.id.searchHistory);
        hotKey_list = (LinearLayout) findViewById(R.id.hotKey_list);
        historyReLay = (RelativeLayout)findViewById(R.id.historyReLay);

        clearLy = (ImageView) findViewById(R.id.clearLy);
        clearLy_bottom_line = (View) findViewById(R.id.clearLy_bottom_line);
        clearLy.setOnClickListener(this);
        initTodayRv();
        //记录历史
        ArrayList<String> asObject = (ArrayList<String>) App.getACache().getAsObject(C.sp.searchHotKey);
        if (asObject != null) {
            mArrayList.addAll(asObject);
        }
        initSearchAdapter();
        if(null != mArrayList && mArrayList.size() >0){
            historyReLay.setVisibility(View.VISIBLE);
            addViewToFlowLayout(mArrayList.toArray(new String[mArrayList.size()]), hotKey_list, 40);
        }
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) etSearch.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //这里调用搜索方法
                    goToSearch();
                    return true;
                }
                return false;
            }
        });
        getSearchGuide();
        getHotKeywords();
        getTodayTitle();
        getTodayGood();

    }

    @OnTextChanged(value = R.id.search_et, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterTextChanged(Editable s) {
        try {
            if (!TextUtils.isEmpty(s.toString())) {
                getGoodsData(s.toString());
                isClserSearch = false;
            } else {
                isClserSearch = true;
                clserSearchAdapter();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.search:
                goToSearch();
                break;
            case R.id.clearLy: //清楚历史数据
                openCleanDataDialog();
                break;
            case R.id.moreHistoryLL:
                if(moreHistoryTv.getText().equals("更多搜索历史")){
                    isShowMoreHistory = true;
                    moreHistoryTv.setText("收起搜索历史");
                    arrowIv.setImageResource(R.drawable.icon_arrow_up);
                }else{
                    isShowMoreHistory = false;
                    moreHistoryTv.setText("更多搜索历史");
                    arrowIv.setImageResource(R.drawable.icon_arrow_down);
                }

                addViewToFlowLayout(mArrayList.toArray(new String[mArrayList.size()]), hotKey_list, 40);
                break;
            default:
                break;
        }
    }

    /**
     * 跳到搜索详细页
     */
    public void goToSearch() {
        if (TextUtils.isEmpty(etSearch.getText().toString().trim())) {
            Toast.makeText(SearchActivity.this, "请输入搜索内容", Toast.LENGTH_SHORT).show();
            etSearch.requestFocus();
            return;
        } else {
            addSearchText(etSearch.getText().toString());
        }

        gotoSearchResultActivity(etSearch.getText().toString());
    }

    private void addSearchText(String text) {


        Iterator<String> arrlistIterator = mArrayList.iterator();
        while(arrlistIterator.hasNext()){
            String str = arrlistIterator.next();
            if(str.equals(text)){
                arrlistIterator.remove();
            }
        }

            mArrayList.add(0, text);
            if(null != mArrayList && mArrayList.size() >0){
                historyReLay.setVisibility(View.VISIBLE);
                addViewToFlowLayout(mArrayList.toArray(new String[mArrayList.size()]), hotKey_list, 40);
            }


        App.getACache().put(C.sp.searchHotKey, mArrayList);
        clearLy.setVisibility(View.VISIBLE);
    }


    /**
     * 获取搜索发现
     */
    private void getHotKeywords() {
        ConfigModel.getInstance().getSearchKey((RxAppCompatActivity) this,31)
                .subscribe(new DataObserver<List<SearchHotKeyBean>>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        super.onError(errorMsg, errCode);
                        hotSearchLL.setVisibility(View.GONE);
                    }

                    @Override
                    protected void onSuccess(List<SearchHotKeyBean> data) {
                        hotSearchLL.setVisibility(View.VISIBLE);
                        if(null != data && data.size()>0){
                            mSearchHotKeyDatas = data;
                            List<String> keyLists = new ArrayList<>();
                            for (int i = 0; i < data.size(); i++) {
                                keyLists.add(data.get(i).getKeyWord());
                            }
                            String[] keyArrays = new String[data.size()];
                            keyArrays = keyLists.toArray(keyArrays);
                            addViewToFlowLayout(keyArrays, searchHistory, 40);
                        }
//                        String sysValue = data.getSysValue();
//                        if (!TextUtils.isEmpty(sysValue)) {
//                            String[] split = sysValue.split(",");
//                            addViewToFlowLayout(split, searchHistory, 40);
//                        }
                    }
                });
    }

    /**
     * 获取搜索引导设置
     */
    private void getSearchGuide() {
        //搜索引导设置（写死30），小幺鸡的文档
        ConfigModel.getInstance().getSearchGuide((RxAppCompatActivity) this,30)
                .subscribe(new DataObserver<List<ImageInfo>>() {

                    @Override
                    protected void onSuccess(List<ImageInfo> data) {
                         setGuideUI(data);
                    }
                });
    }

    /**
     * 获取今日推荐title
     */
    private void getTodayTitle() {
        ConfigListUtlis.getConfigListCacheNet(this, ConfigListUtlis.getConfigAllKey(), new MyAction.One<List<SystemConfigBean>>() {
            @Override
            public void invoke(List<SystemConfigBean> arg) {
                SystemConfigBean todayBean = ConfigListUtlis.getSystemConfigBean(C.ConfigKey.TODAY_RECOMMENDED_TITLE);
                if(null != todayBean && !TextUtils.isEmpty(todayBean.getSysValue())){
                    toDayTv.setText(todayBean.getSysValue());
                }
            }
        });
    }


    /**
     * 获取今日推荐
     */
    private void getTodayGood() {
        ConfigModel.getInstance().getTodayGoods((RxAppCompatActivity) this)
                .subscribe(new DataObserver<List<ShopGoodInfo>>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        super.onError(errorMsg, errCode);
                        todayLayout.setVisibility(View.GONE);
                    }

                    @Override
                    protected void onSuccess(List<ShopGoodInfo> data) {
                           if(null != data && data.size()>0){
                               todayLayout.setVisibility(View.VISIBLE);
                               todayGoodAdapter.setData(data);
                           }
                    }
                });
    }


    private void initTodayRv(){
        todayGoodAdapter = new TodayGoodAdapter(this);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(this,2);
        GridSpacingItemDecoration gridSpacingItemDecoration = GridSpacingItemDecoration.newGridItemDecoration(linearLayoutManager,0,DensityUtil.dip2px(this,9),false);
        if(todayGoodRv.getItemDecorationCount() == 0){
            todayGoodRv.addItemDecoration(gridSpacingItemDecoration);
        }
        todayGoodRv.setLayoutManager(linearLayoutManager);
        todayGoodRv.setAdapter(todayGoodAdapter);
    }

    private void setGuideUI(List<ImageInfo> data){
        if(null != data && data.size()>0){
            final ImageInfo info  = data.get(0);
            if(!TextUtils.isEmpty(info.getTitle()) && info.getMark() == 1){
                guideTitleTv.setVisibility(View.VISIBLE);
                guideTitleTv.setText(info.getTitle());
            }else{
                guideTitleTv.setVisibility(View.GONE);
            }
            if(info.getMediaType() == 1){
                videoPlayIv.setVisibility(View.VISIBLE);
            }
            guideIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(info.getMediaType() == 1){
                        //视频
                        if(!TextUtils.isEmpty(info.getVideoUrl())){
                            Intent it = new Intent(SearchActivity.this, ShortVideoPlayActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(C.Extras.ITEMVIDEOID, info.getVideoUrl());
                            it.putExtras(bundle);
                            startActivity(it);
                        }
                    }else{
                        //图片
                        if(info.getOpen() != 0){
                            BannerInitiateUtils.gotoAction(SearchActivity.this,info);
                        }
                    }
                }
            });
            if(!TextUtils.isEmpty(info.getPicture())){
                LoadImgUtils.setImg(this, guideIv, info.getPicture());
            }
        }
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

    private String delHTMLTag(String content) {
        String REGEX_HTML = "<[^>]+>";
        Pattern p_html = Pattern.compile(REGEX_HTML, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(content);
        content = m_html.replaceAll("");
        return content.trim();
    }

    private void initSearchAdapter() {
        try {
            if (mTaobaoSearchAdapter == null) {
                mTaobaoSearchAdapter = new SearchHotAdapter(SearchActivity.this, mTaobaoSearchData);
                mListView.setAdapter(mTaobaoSearchAdapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String s = mTaobaoSearchData.get(position);
                        addSearchText(s);
                        gotoSearchResultActivity(s);
                    }
                });
            }

        } catch (Exception e) {
        }
    }

    private void clserSearchAdapter() {
        mListView.setVisibility(View.GONE);
        mTaobaoSearchAdapter.setData(mTaobaoSearchData);
        mTaobaoSearchData.clear();
        mTaobaoSearchAdapter.notifyDataSetChanged();
    }

    /**
     * LinearLayout转流式布局
     *
     * @param hotKeys      字符数组
     * @param linearLayout 主布局
     * @param padding      主布局左右间距总和 db为单位
     */
    private void addViewToFlowLayout(String[] hotKeys, final LinearLayout linearLayout, int padding) {
        if (hotKeys == null || hotKeys.length == 0) {
            return;
        }
//        //如果是搜索历史，最多显示8个
//        if(linearLayout.getId() == R.id.hotKey_list && hotKeys.length > 8){
//            hotKeys = Arrays.copyOfRange(hotKeys,  0 ,  8 );
//        }
        //初始化布局
        linearLayout.removeAllViews();
        //行布局内的宽度
        int groupWidth = DensityUtil.getPhoneWidth(SearchActivity.this) - DensityUtil.dip2px(SearchActivity.this, padding);
        int sumWidth = 0;
        LinearLayout getLy = null;
        int line = 0;
        for (int i = 0; i < hotKeys.length; i++) {
            final int pot = i;
            if (TextUtils.isEmpty(hotKeys[i])) return;
            View view = LayoutInflater.from(SearchActivity.this).inflate(R.layout.flowlayout_tv, null);
            TextView tv = (TextView) view.findViewById(R.id.flow_text);
            GradientDrawable background = (GradientDrawable) tv.getBackground();
            ImageView hotTagIv = view.findViewById(R.id.hotTagIv);
            tv.setTextColor(ContextCompat.getColor(this,R.color.color_333333));
            background.setColor(ContextCompat.getColor(this,R.color.color_fff6f6f6));
            //字数限制
            if(linearLayout.getId() == R.id.searchHistory){
                //关键字
                SearchHotKeyBean shk = mSearchHotKeyDatas.get(i);
                if(null != shk && shk.getMark() == 1){
                    hotTagIv.setVisibility(View.VISIBLE);
                    if(!TextUtils.isEmpty(shk.getWordColor()) && StringsUtils.checkColor(shk.getWordColor().trim())){
                        tv.setTextColor(Color.parseColor(shk.getWordColor().trim()));
                    }
                    if(!TextUtils.isEmpty(shk.getBackgroundColor()) && StringsUtils.checkColor(shk.getBackgroundColor().trim())){
                        background.setColor(Color.parseColor(shk.getBackgroundColor().trim()));
                    }
                }
                tv.setText(hotKeys[i].length()>6 ?hotKeys[i].substring(0,6)+"...":hotKeys[i]);
            }else if(linearLayout.getId() == R.id.hotKey_list){
                tv.setText(hotKeys[i].length()>24 ?hotKeys[i].substring(0,24)+"...":hotKeys[i]);
            }
            tv.setTag(hotKeys[i]);
            int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            view.measure(w, h);
            int height = view.getMeasuredHeight();
            int width = view.getMeasuredWidth();

            if (sumWidth == 0 || (sumWidth + width) > groupWidth) {
                if(linearLayout.getId() == R.id.hotKey_list && line>2 ){
                    //历史搜索，默认显示3行，最多12行,显示更多按钮
                    if(!isShowMoreHistory){
                        isShowMoreHistory = true;
                        moreHistoryLL.setVisibility(View.VISIBLE);
                        return;
                    }else if(isShowMoreHistory && line > 11){
                        return;
                    }
                }
                LinearLayout ly = new LinearLayout(SearchActivity.this);
                ly.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                ly.setOrientation(LinearLayout.HORIZONTAL);
                getLy = ly;
                linearLayout.addView(ly);
                getLy.addView(view);
                sumWidth = width;
                line++;
            } else {
                if (getLy != null) {
                    getLy.addView(view);
                    sumWidth = sumWidth + width;
                }
            }

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView vt = (TextView) view;
                    String getText = (String) vt.getTag();
                    //判断是否有配置跳转，没有执行以下代码
                    //热门搜索
                    if(linearLayout.getId() == R.id.searchHistory){
                        SearchHotKeyBean shk = mSearchHotKeyDatas.get(pot);
                        if(shk.getOpen() != 0){
                            BannerInitiateUtils.gotoAction(SearchActivity.this,MyGsonUtils.toImageInfo(shk));
                        }else{
                            addSearchText(getText.trim());
                            gotoSearchResultActivity(getText.trim());
                        }
                    }else{
                        addSearchText(getText.trim());
                        gotoSearchResultActivity(getText.trim());
                    }


                }
            });
        }

    }



    private void gotoSearchResultActivity(String getText) {
        SensorsDataUtil.getInstance().searchKeyTrack(getText,C.BigData.NORMAL_SEARCH);
        Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("keyWord", getText);
        bundle.putInt(SearchResultActivity.KEY_SEARCH_TYPE,mSearchType);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private Dialog textDialog;

    private void openCleanDataDialog() {  //打开清理缓存
        textDialog = new ClearSDdataDialog(SearchActivity.this, R.style.dialog, "提示", "是否清除搜索历史", new ClearSDdataDialog.OnOkListener() {
            @Override
            public void onClick(View dialog, String text) {

                mArrayList.clear();
                hotKey_list.removeAllViews();
                historyReLay.setVisibility(View.GONE);
                App.getACache().put(C.sp.searchHotKey, mArrayList);
                clearLy.setVisibility(View.INVISIBLE);
                clearLy_bottom_line.setVisibility(View.INVISIBLE);
                moreHistoryLL.setVisibility(View.GONE);
                ViewShowUtils.showShortToast(SearchActivity.this, "删除成功");
            }

        });
        textDialog.show();
    }

    class SearchAdapter extends SimpleAdapter<String, SimpleViewHolder> {
        Context context;

        public SearchAdapter(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        public void onBindViewHolder(SimpleViewHolder holder, int position) {

            final String item = getItem(position);
            TextView create_time = holder.viewFinder().view(R.id.createTime);

        }

        @Override
        protected View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
            return layoutInflater.inflate(R.layout.item_myteam, parent, false);
        }
    }


    /**
     * 搜索统计
     * @param keywords
     */
    private void sendSearchStatistics(String keywords){
        SearchStatisticsModel.getInstance().sendSearchStatistics(this,keywords,C.SearchStatistics.ID_HOT_KEY,C.SearchStatistics.TYPE_HOT_KEY)
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
