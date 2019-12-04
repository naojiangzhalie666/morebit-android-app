package com.zjzy.morebit.circle.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zjzy.morebit.Activity.ShowWebActivity;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.circle.model.CircleModel;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.SearchHotKeyBean;
import com.zjzy.morebit.pojo.request.RequestCircleSearchBean;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.OpenFragmentUtils;
import com.zjzy.morebit.utils.UI.BannerInitiateUtils;
import com.zjzy.morebit.view.SearchViewLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


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
