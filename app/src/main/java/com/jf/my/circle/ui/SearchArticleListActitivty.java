package com.jf.my.circle.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.jf.my.Activity.ShowWebActivity;
import com.jf.my.Module.common.Activity.BaseActivity;
import com.jf.my.Module.common.Fragment.BaseFragment;
import com.jf.my.Module.common.View.ReUseListView;
import com.jf.my.R;
import com.jf.my.circle.adapter.ArticleAdapter;
import com.jf.my.circle.contract.ArticleContract;
import com.jf.my.circle.model.CircleModel;
import com.jf.my.circle.presenter.ArticlePresenter;
import com.jf.my.contact.EventBusAction;
import com.jf.my.mvp.base.base.BaseView;
import com.jf.my.mvp.base.frame.MvpFragment;
import com.jf.my.network.CommonEmpty;
import com.jf.my.network.observer.DataObserver;
import com.jf.my.player.RecyclerItemNormalHolder;
import com.jf.my.pojo.Article;
import com.jf.my.pojo.MessageEvent;
import com.jf.my.pojo.SearchArticleBody;
import com.jf.my.pojo.SearchHotKeyBean;
import com.jf.my.pojo.request.RequestCircleSearchBean;
import com.jf.my.utils.C;
import com.jf.my.utils.MyLog;
import com.jf.my.utils.OpenFragmentUtils;
import com.jf.my.utils.UI.BannerInitiateUtils;
import com.jf.my.utils.ViewShowUtils;
import com.jf.my.view.SearchViewLayout;
import com.jf.my.view.ToolbarHelper;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Action;


/**
 * @author YangBoTian
 * @date 2019/8/30
 * @des 商学院文章搜索关键词页
 */

public class SearchArticleListActitivty extends BaseActivity {

    @BindView(R.id.searchViewLayout)
    SearchViewLayout searchViewLayout;
    CircleModel mCircleModel;
    List<SearchHotKeyBean> mList = new ArrayList<>();
    public static void start(Context context) {
        Intent intent = new Intent(context, SearchArticleListActitivty.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_article_list);
        setStatusBarWhite();
        mCircleModel = new CircleModel();
        initView();
        getData();

    }

    private void getData() {
         mCircleModel.getSearchSet(this,new RequestCircleSearchBean())
                 .subscribe(new DataObserver<List<SearchHotKeyBean>>() {
                     @Override
                     protected void onSuccess(List<SearchHotKeyBean> data) {
                         mList.clear();
                         mList.addAll(data);
                         searchViewLayout.setHotKeyData(mList);
                     }
                 });
    }

    private void initView() {

        searchViewLayout.setCacheKey(C.sp.COLLEGE_SEARCH_HISTORY);

        searchViewLayout.setOnClickHotKeyListener(new SearchViewLayout.OnClickHotKeyListener() {
            @Override
            public void onClick(int position, SearchHotKeyBean item) {
                gotoResult(item.getKeyWord(),position);
//                ViewShowUtils.showShortToast(SearchArticleListActitivty.this,position+","+item.getKeyWord());
            }
        });

        searchViewLayout.setOnClickHistoryListener(new SearchViewLayout.OnClickHistoryListener() {
            @Override
            public void onClick(int position, String item) {
                gotoResult(item);
//                ViewShowUtils.showShortToast(SearchArticleListActitivty.this,position+","+item);
            }
        });

        searchViewLayout.setOnClickSearchListener(new SearchViewLayout.OnClickSearchListener() {
            @Override
            public void onClick(String searchText) {
                gotoResult(searchText);
//                ViewShowUtils.showShortToast(SearchArticleListActitivty.this,searchText);
            }
        });
    }


    private void gotoResult(String keyword,int position) {
        SearchHotKeyBean bean =  mList.get(position);
        if(bean==null){
            return;
        }
        if(bean.getOpen()==1) {
            Bundle bundle = new Bundle();
            bundle.putString("keyword", keyword);
            OpenFragmentUtils.goToSimpleFragment(this, SearchArticleListResultFragment.class.getName(), bundle);
        } else if(bean.getOpen()==3) {
            ShowWebActivity.start(this, bean.getUrl(), keyword);
        } else if(bean.getOpen()==9){
                BannerInitiateUtils.goToArticle(this,bean.getUrl(),keyword);
        }

    }
    private void gotoResult(String keyword) {
        Bundle bundle = new Bundle();
        bundle.putString("keyword", keyword);
        OpenFragmentUtils.goToSimpleFragment(this, SearchArticleListResultFragment.class.getName(), bundle);

    }

}
