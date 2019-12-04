package com.zjzy.morebit.circle.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.zjzy.morebit.R;
import com.zjzy.morebit.circle.CollegeListActivity;
import com.zjzy.morebit.circle.ui.RecommendListActivity;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.Article;
import com.zjzy.morebit.pojo.CollegeHome;

import java.util.Collections;
import java.util.List;

import com.chad.library.adapter.base.BaseViewHolder;
import com.zjzy.morebit.pojo.request.RequestArticleBean;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * @author YangBoTian
 * @date 2019/9/3
 * @des
 */
public class CollegeHomeAdapter extends BaseMultiItemQuickAdapter<CollegeHome, BaseViewHolder> {
    private RxAppCompatActivity mRxAppCompatActivity;
    private RxFragment mRxFragment;
    private Context mContext;
    private SparseArray<List<Article>> listSparseArray = new SparseArray<>();
    public CollegeHomeAdapter(Context context, List<CollegeHome> homeRecommendGoods, RxFragment rxFragment) {
        super(homeRecommendGoods);
        mContext = context;
        addItemType(CollegeHome.TYPE_HOTIZONTAL, R.layout.item_tutorial_horizontal);
        addItemType(CollegeHome.TYPE_VERTICAL, R.layout.item_tutorial_vertical);
        addItemType(CollegeHome.TYPE_RECOMMEND, R.layout.item_tutorial_vertical);
        mRxFragment = rxFragment;
    }
    public CollegeHomeAdapter(Context context, List<CollegeHome> homeRecommendGoods, RxAppCompatActivity rxAppCompatActivity) {
        super(homeRecommendGoods);
        mContext = context;
        addItemType(CollegeHome.TYPE_HOTIZONTAL, R.layout.item_tutorial_horizontal);
        addItemType(CollegeHome.TYPE_VERTICAL, R.layout.item_tutorial_vertical);
        addItemType(CollegeHome.TYPE_RECOMMEND, R.layout.item_tutorial_vertical);
        mRxAppCompatActivity = rxAppCompatActivity;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final CollegeHome collegeHome) {
        switch (collegeHome.getItemType()) {
            case CollegeHome.TYPE_HOTIZONTAL:
                if(collegeHome.getArticleList()==null||collegeHome.getArticleList().size()==0) {
                    baseViewHolder.itemView.setVisibility(View.GONE);
                    return;
                }
                ((TextView) baseViewHolder.getView(R.id.tv_title)).setText(collegeHome.getModelName());
                RecyclerView rec_hotizontal =  baseViewHolder.getView(R.id.recyclerView_course);
                LinearLayout ll_more =  baseViewHolder.getView(R.id.ll_more);
                LinearLayoutManager hotizontal_layoutManager = new LinearLayoutManager(mContext);
                hotizontal_layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
               rec_hotizontal.setLayoutManager(hotizontal_layoutManager);
                final ArticleAdapter articleAdapter =getArticleAdapter();
                if(articleAdapter!=null){
                    articleAdapter.setType(ArticleAdapter.TYPE_VERTICAL);
                    rec_hotizontal.setAdapter(articleAdapter);
                    if(collegeHome.getArticleList()!=null){
                        articleAdapter.replace(collegeHome.getArticleList());
                        articleAdapter.notifyDataSetChanged();
                    }
                }
                ll_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CollegeListActivity.start((Activity)mContext,collegeHome.getModelName(),collegeHome.getModelId()+"",CollegeListActivity.CIRCLE_COURSE);
                    }
                });
                break;
            case CollegeHome.TYPE_VERTICAL:
                if(collegeHome.getArticleList()==null||collegeHome.getArticleList().size()==0) {
                    baseViewHolder.itemView.setVisibility(View.GONE);
                    return;
                }
                ((TextView) baseViewHolder.getView(R.id.tv_title)).setText(collegeHome.getModelName());
                RecyclerView rec_vertical =  baseViewHolder.getView(R.id.recyclerView_course);
                TextView tv_more =  baseViewHolder.getView(R.id.tv_more);
                TextView tv_change =  baseViewHolder.getView(R.id.tv_change);
                LinearLayoutManager vertical_layoutManager = new LinearLayoutManager(mContext);
                vertical_layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                rec_vertical.setLayoutManager(vertical_layoutManager);
                final ArticleAdapter verticalArticleAdapter =getArticleAdapter();
                if(verticalArticleAdapter!=null){
                    verticalArticleAdapter.setType(ArticleAdapter.TYPE_HOTIZONTAL);
                    rec_vertical.setAdapter(verticalArticleAdapter);
                    if(collegeHome.getArticleList()!=null){
                        if(collegeHome.getArticleList().size()>3){
                            verticalArticleAdapter.replace(collegeHome.getArticleList().subList(0,3));
                        }else {
                            verticalArticleAdapter.replace(collegeHome.getArticleList());
                        }
//                        verticalArticleAdapter.replace(collegeHome.getArticleList());
                        verticalArticleAdapter.notifyDataSetChanged();
                    }
                }
                tv_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CollegeListActivity.start((Activity)mContext,collegeHome.getModelName(),collegeHome.getModelId()+"",CollegeListActivity.CIRCLE_COURSE);
                    }
                });
                tv_change.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        change(baseViewHolder.getAdapterPosition()-1,2,verticalArticleAdapter,CollegeHome.TYPE_VERTICAL);

                    }
                });
                break;
            case CollegeHome.TYPE_RECOMMEND:
                if(collegeHome.getArticleList()==null||collegeHome.getArticleList().size()==0) {
                    baseViewHolder.itemView.setVisibility(View.GONE);
                    return;
                }
                ((TextView) baseViewHolder.getView(R.id.tv_title)).setText(collegeHome.getModelName());
                RecyclerView recommend_vertical =  baseViewHolder.getView(R.id.recyclerView_course);
                TextView tv_recommend_more =  baseViewHolder.getView(R.id.tv_more);
                TextView tv_recommend_change =  baseViewHolder.getView(R.id.tv_change);
                GridLayoutManager recommend_layoutManager = new GridLayoutManager(mContext,2);
                recommend_vertical.setLayoutManager(recommend_layoutManager);
                final ArticleAdapter recommendArticleAdapter=getArticleAdapter();
                if(recommendArticleAdapter!=null){
                    recommendArticleAdapter.setType(ArticleAdapter.TYPE_RECOMMEND);
                    recommend_vertical.setAdapter(recommendArticleAdapter);
                    if(collegeHome.getArticleList()!=null){
                        if(collegeHome.getArticleList().size()>2){
                            recommendArticleAdapter.replace(collegeHome.getArticleList().subList(0,2));
                        } else {
                            recommendArticleAdapter.replace(collegeHome.getArticleList());
                        }

                        recommendArticleAdapter.notifyDataSetChanged();
                    }
                }
                tv_recommend_change.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        change(baseViewHolder.getAdapterPosition()-1,2,recommendArticleAdapter,CollegeHome.TYPE_RECOMMEND);
                    }
                });
                tv_recommend_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecommendListActivity.start((Activity)mContext,RecommendListActivity.ARTICLE_RECOMMEND);
                    }
                });
                break;
        }
    }


    private  ArticleAdapter getArticleAdapter(){
        if(mRxFragment!=null){
            return new ArticleAdapter(mContext,mRxFragment);
        } else if(mRxAppCompatActivity !=null){
            return new ArticleAdapter(mContext,mRxAppCompatActivity);
        }
        return null;
    }

    private void change(final int position, final int type, final ArticleAdapter articleAdapter, final int viewType){
        final List<Article> articles = listSparseArray.get(position);
        if(articles==null||articles.size()==0){
            if(mRxFragment!=null){
                RequestArticleBean bean = new RequestArticleBean();
                bean.setModelId(getItem(position).getModelId());
                bean.setType(type);
                RxHttp.getInstance().getCommonService()
                        .forChange(bean)
                        .compose(RxUtils.<BaseResponse<List<Article>>>switchSchedulers())
                        .compose(mRxFragment.<BaseResponse<List<Article>>>bindToLifecycle())
                        .subscribe(new DataObserver<List<Article>>() {
                            @Override
                            protected void onSuccess(List<Article> data) {
                                listSparseArray.put(position,data);
                                randomData(position,articleAdapter,viewType);
                            }
                        });
            }

        } else {
            randomData(position,articleAdapter,viewType);
        }

    }

    private void randomData(int position,ArticleAdapter articleAdapter,int viewType){
        final List<Article> articles = listSparseArray.get(position);
         if(articles!=null){
             Collections.shuffle(articles);
             if(articleAdapter!=null){
                 if(CollegeHome.TYPE_RECOMMEND == viewType){
                     if(articles.size()>2){
                         articleAdapter.replace(articles.subList(0,2));
                     }
                 } else {
                     if(articles.size()>3){
                         articleAdapter.replace(articles.subList(0,3));
                     }
                 }

                 articleAdapter.notifyDataSetChanged();
             }

         }
    }
}
