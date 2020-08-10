package com.zjzy.morebit.circle.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.zjzy.morebit.Module.common.View.ReUseListView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.circle.adapter.ArticleAdapter;
import com.zjzy.morebit.circle.contract.ArticleContract;
import com.zjzy.morebit.circle.presenter.ArticlePresenter;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.network.CommonEmpty;
import com.zjzy.morebit.pojo.Article;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.SearchArticleBody;
import com.zjzy.morebit.utils.ViewShowUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by YangBoTian on 2018/12/24.
 * 商学院搜索文章
 */

public class SearchArticleListResultFragment extends MvpFragment<ArticlePresenter> implements ArticleContract.View {
    private static final int REQUEST_COUNT = 10;
    @BindView(R.id.mListView)
    ReUseListView mReUseListView;
    @BindView(R.id.search_et)
    EditText mEditSearch;
    @BindView(R.id.text_clear)
    TextView text_clear;
    private TextView txt_head_title;
    private LinearLayout btn_back;

    private int page = 1;
    private ArticleAdapter mArticleAdapter;
    private String mId;
    private String mTitle = "";
    private String mKeyWord;
    private CommonEmpty mEmptyView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mKeyWord = bundle.getString("keyword");
        }
        if(!TextUtils.isEmpty(mKeyWord)){
            mEditSearch.setText(mKeyWord);
        }
        refreshData(true);
    }

    private void refreshData(boolean isRefresh) {
        if (isRefresh) {
            page = 1;
            mEmptyView.setmFirstComplete(false);
            mReUseListView.getListView().setNoMore(false);
        }
        SearchArticleBody body = new SearchArticleBody();
        body.setSearch(mKeyWord);
        body.setPage(page);
        mPresenter.searchArticleList(this, body, mEmptyView);

    }

    @Override
    protected void initView(View view) {

        txt_head_title = (TextView) view.findViewById(R.id.txt_head_title);
        txt_head_title.setText("进阶学院");
        txt_head_title.setTextSize(18);
        txt_head_title.getPaint().setFakeBoldText(true);
        btn_back= (LinearLayout) view.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        mReUseListView.getSwipeList().setEnableRefresh(false);
        mEmptyView = new CommonEmpty(view, "暂无文章哦~", R.drawable.search_noresult);
        mReUseListView.getSwipeList().setOnRefreshListener(new com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData(true);
            }
        });
        mArticleAdapter = new ArticleAdapter(getActivity(),this);
        mArticleAdapter.setStudyView(true);
        mReUseListView.getListView().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                refreshData(false);
            }
        });
        mReUseListView.setAdapter(mArticleAdapter);

        mEditSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) mEditSearch.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //这里调用搜索方法

                    ViewShowUtils.hideSoftInput(getActivity(), mEditSearch);
                    mKeyWord = mEditSearch.getText().toString();
                    refreshData(true);
                    return true;
                }
                return false;
            }
        });


        mEditSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //判断是否显示删除按钮
                if (TextUtils.isEmpty(mEditSearch.getText().toString().trim())) {
                    if (text_clear.getVisibility() != View.GONE) {
                        text_clear.setVisibility(View.GONE);
                    }
                } else {
                    if (text_clear.getVisibility() != View.VISIBLE) {
                        text_clear.setVisibility(View.VISIBLE);
                    }
                }

            }
        });

        mReUseListView.isDefaultRefreshing(false);
    }


    @Override
    protected int getViewLayout() {
        return R.layout.fragment_search_article_list_result;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }

    @Override
    public void onArticleSuccessful(List<Article> data) {
        if (page == 1) {
          mArticleAdapter.replace(data);
        } else {
            if (data.size() == 0) {
                mReUseListView.getListView().setNoMore(true);
            } else {
                mArticleAdapter.add(data);
            }
        }
        page++;
        mReUseListView.notifyDataSetChanged();
    }

    @Override
    public void onArticleEmpty() {
        if (page == 1) {
            mArticleAdapter.clear();
            mReUseListView.notifyDataSetChanged();
        } else {
            mReUseListView.getListView().setNoMore(true);
        }
    }


    @Override
    public void onArticleFinally() {
        mReUseListView.getSwipeList().setRefreshing(false);
        mReUseListView.getListView().refreshComplete(REQUEST_COUNT);
        //下拉刷新或者加载更多的时候停止播放
    }


    @OnClick({R.id.search, R.id.text_clear,R.id.iv_back})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                getActivity().finish();
                break;
            case R.id.search:
                if (TextUtils.isEmpty(mEditSearch.getText().toString())) {
                    ViewShowUtils.showShortToast(getActivity(), "请输入搜索关键词");
                    return;
                }
                mKeyWord = mEditSearch.getText().toString();
                refreshData(true);
                break;
            case R.id.text_clear:
                mKeyWord = "";
                mEditSearch.setText("");
                break;
        }
    }

    @Subscribe  //订阅事件
    public void onEventMainThread(MessageEvent event) {
        switch (event.getAction()) {
            case EventBusAction.LOGINA_SUCCEED:
                refreshData(true);
                break;
            default:
                break;
        }
    }

}
