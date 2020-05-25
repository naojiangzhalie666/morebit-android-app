package com.zjzy.morebit.main.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zjzy.morebit.App;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.SearchHotAdapter;
import com.zjzy.morebit.fragment.SearchResultForJdFragment;
import com.zjzy.morebit.fragment.SearchResultForPddFragment;
import com.zjzy.morebit.fragment.SearchResultForTaobaoFragment;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.main.ui.myview.xtablayout.XTabLayout;
import com.zjzy.morebit.network.CallBackObserver;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.RxWXHttp;
import com.zjzy.morebit.pojo.event.SearchGoodsForJdEvent;
import com.zjzy.morebit.pojo.event.SearchGoodsForPddEvent;
import com.zjzy.morebit.pojo.event.SearchGoodsForTaobaoEvent;
import com.zjzy.morebit.pojo.goods.TaobaoSearch;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.MyGsonUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.ViewShowUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.functions.Function;


/**
 * 首页分类-搜索淘宝、拼多多、京东
 */
public class SearchResultFragment extends BaseMainFragmeng {
    @BindView(R.id.xTablayout)
    XTabLayout xTablayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
//    @BindView(R.id.view_bar)
//    View view_bar;

//    @BindView(R.id.iv_head_bg)
//    ImageView iv_head_bg;

    @BindView(R.id.search_et)
    EditText etSearch;

    @BindView(R.id.list_serach_key)
    ListView mListView;

    @BindView(R.id.iv_clear)
    ImageView iv_clear;

    boolean isClserSearch = false;
    private boolean isFrist = true;
    private ArrayList<String> mTaobaoSearchData = new ArrayList<>();
    private SearchHotAdapter mTaobaoSearchAdapter;


    private ArrayList<SearchBean> mSearchBean = new ArrayList<>();
    private SearchResultForTaobaoFragment mSearchTaobaoFragment;
    private SearchResultForPddFragment mSearchPddFragment;
    private SearchResultForJdFragment mSearchJdFragment;
    private View mView;
    private int mHeadBgHeight;
    private String keyWord = "";
    private Bundle bundle;
    private ChannelAdapter mChannelAdapter;
    private int mType = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_search_result_pdd_tabao, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initBundle();
        initView(mView);

    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        getActivity().finish();
    }

    /**
     * 初始化界面
     */
   private void initView(View view) {
       mSearchTaobaoFragment = SearchResultForTaobaoFragment.newInstance(1);
       mSearchBean.add(new SearchBean(mSearchTaobaoFragment, getResources().getString(R.string.current_taobao)));


       mSearchJdFragment = SearchResultForJdFragment.newInstance(2);
       mSearchBean.add(new SearchBean(mSearchJdFragment, getResources().getString(R.string.current_jd)));

       mSearchPddFragment = SearchResultForPddFragment.newInstance(3);
       mSearchBean.add(new SearchBean(mSearchPddFragment, getResources().getString(R.string.current_pdd)));

       mChannelAdapter =new ChannelAdapter(getChildFragmentManager());
       viewPager.setOffscreenPageLimit(3);
       viewPager.setAdapter(mChannelAdapter);
       xTablayout.setupWithViewPager(viewPager);
       xTablayout.addOnTabSelectedListener(new XTabLayout.OnTabSelectedListener (){

           @Override
           public void onTabSelected(XTabLayout.Tab tab) {

               if (getResources().getString(R.string.current_taobao).equals(tab.getText())){
                   mType = 1;
               }else if (getResources().getString(R.string.current_pdd).equals(tab.getText())){
                   mType = 3;
               }else if (getResources().getString(R.string.current_jd).equals(tab.getText())){
                   mType = 2;
           }
               String currentSearch = etSearch.getText().toString();
               keyWord = currentSearch;
               sendMsgForChildFragment(mType);
//               if (!keyWord.equals(currentSearch)){
//                   keyWord = currentSearch;
//                   sendMsgForChildFragment(mType);
//               }

           }

           @Override
           public void onTabUnselected(XTabLayout.Tab tab) {

           }

           @Override
           public void onTabReselected(XTabLayout.Tab tab) {

           }
       });

       etSearch.setText(keyWord == null ? "" : keyWord);

       etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
           @Override
           public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
               if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                   // 先隐藏键盘
                   ((InputMethodManager) etSearch.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                           .hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                   //这里调用搜索方法


                   keyWord = etSearch.getText().toString();
                   mListView.setVisibility(View.GONE);
                   sendMsgForChildFragment(mType);
                   ViewShowUtils.hideSoftInput(getActivity(), etSearch);
                   return true;
               }
               return false;
           }
       });

       if (!TextUtils.isEmpty(keyWord)) {
           etSearch.setText(keyWord);
       }

       sendMsgForChildFragment(mType);
       initSearchAdapter();
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
                    mListView.setVisibility(View.VISIBLE);
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


    private class ChannelAdapter extends FragmentStatePagerAdapter {


        public ChannelAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mSearchBean.get(position).title;
        }

        @Override
        public Fragment getItem(int position) {
            return mSearchBean.get(position).mFragment;
        }

        @Override
        public int getCount() {
            return mSearchBean.size();
        }
    }


    public class SearchBean {
        public Fragment mFragment;
        public String title;

        public SearchBean(Fragment fragment, String title) {
            mFragment = fragment;
            this.title = title;
        }

    }

    private void clserSearchAdapter() {
       mListView.setVisibility(View.GONE);
        mTaobaoSearchAdapter.setData(mTaobaoSearchData);
        mTaobaoSearchData.clear();
        mTaobaoSearchAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.search, R.id.iv_back, R.id.iv_clear})
    public void Onclick(View v) {
        switch (v.getId()) {
            case R.id.search:

                keyWord = etSearch.getText().toString();
                sendMsgForChildFragment(mType);
                break;
            case R.id.iv_back:
                getActivity().finish();
                break;
            case R.id.iv_clear: //清楚搜索关键字
                etSearch.setText("");
                break;

            default:
                break;
        }
    }



    private void initBundle() {
        bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            keyWord = bundle.getString(C.Extras.search_keyword);

        }
//        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH){
//                    keyWord = etSearch.getText().toString();
//                    sendMsgForChildFragment(mType);
//                    return true;
//                }
//
//                return false;
//            }
//        });
    }

    /**
     * @return
     */
    public void getGoodsData(final String string) {

        final String taobaoSearchUrl = "https://suggest.taobao.com/sug?code=utf-8&q=" + string + "&k=1&area=c2c";

        RxWXHttp.getInstance().getService(C.getInstance().getGoodsIp()).profilePicture(taobaoSearchUrl)
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
                mTaobaoSearchAdapter = new SearchHotAdapter(getActivity(), mTaobaoSearchData);
                mListView.setAdapter(mTaobaoSearchAdapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ((InputMethodManager) etSearch.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                                .hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        String s = mTaobaoSearchData.get(position);
                        mListView.setVisibility(View.GONE);
                        keyWord = s;
                        etSearch.setText(keyWord);
                        addSearchText(s);
                        sendMsgForChildFragment(mType);


                    }
                });
            }

        } catch (Exception e) {
        }
    }

    /**
     * 发送子fragment
     * @param type
     */
    private void sendMsgForChildFragment(int type){
        if (type == 1){
            SearchGoodsForTaobaoEvent event = new SearchGoodsForTaobaoEvent();
            event.setKeyword(keyWord);
            EventBus.getDefault().post(event);
        }else if (type==3){
            SearchGoodsForPddEvent event = new SearchGoodsForPddEvent();
            event.setKeyword(keyWord);
            EventBus.getDefault().post(event);
        } else if (type==2){
            SearchGoodsForJdEvent event = new SearchGoodsForJdEvent();
            event.setKeyword(keyWord);
            EventBus.getDefault().post(event);
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







}
