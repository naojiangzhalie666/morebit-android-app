package com.markermall.cat.circle.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.markermall.cat.Activity.ShowWebActivity;
import com.markermall.cat.Module.common.Activity.BaseActivity;
import com.markermall.cat.R;
import com.markermall.cat.circle.model.CircleModel;
import com.markermall.cat.network.observer.DataObserver;
import com.markermall.cat.pojo.SearchHotKeyBean;
import com.markermall.cat.pojo.request.RequestCircleSearchBean;
import com.markermall.cat.utils.C;
import com.markermall.cat.utils.OpenFragmentUtils;
import com.markermall.cat.utils.UI.BannerInitiateUtils;
import com.markermall.cat.view.SearchViewLayout;

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
