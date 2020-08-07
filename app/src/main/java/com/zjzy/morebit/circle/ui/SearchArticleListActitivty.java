package com.zjzy.morebit.circle.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxItemDecoration;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.zjzy.morebit.Activity.ShowWebActivity;
import com.zjzy.morebit.App;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Dialog.ClearSDdataDialog;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.SearchHistoryAdapter;
import com.zjzy.morebit.circle.model.CircleModel;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.SearchHotKeyBean;
import com.zjzy.morebit.pojo.request.RequestCircleSearchBean;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.OpenFragmentUtils;
import com.zjzy.morebit.utils.SharedPreferencesUtils;
import com.zjzy.morebit.utils.UI.BannerInitiateUtils;
import com.zjzy.morebit.utils.UI.DeviceUtil;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.view.ClearEditText;
import com.zjzy.morebit.view.SearchClassLayout;
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
    SearchClassLayout searchViewLayout;
    CircleModel mCircleModel;
    private TextView txt_head_title;
    private LinearLayout btn_back;
    private ClearEditText search_et;

    private List<String> mHistoryList;
    private RecyclerView ryc_history;
    private SearchHistoryAdapter  mHistoryAdapter;
    private FlexboxLayoutManager  mHistoryFlexboxLayoutManager;
    private ImageView clear;
    private TextView history;
    private LinearLayout ll_history;
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
        ll_history= (LinearLayout) findViewById(R.id.ll_history);
        clear= (ImageView) findViewById(R.id.clear);
        mHistoryList=  SharedPreferencesUtils.getListData(this, C.Extras.KEY_SAVE_SEARCH_HISTORY);
        Log.e("sfsdfsf",mHistoryList+"");
        if (mHistoryList == null) {
            mHistoryList = new ArrayList<>();
        }
        if (mHistoryList.size() > 30) {
            //超过30条不要了
            mHistoryList = mHistoryList.subList(0, 30);
        }
        if (mHistoryList.size()==0){
            ll_history.setVisibility(View.GONE);
        }else{
            ll_history.setVisibility(View.VISIBLE);
        }
        search_et= (ClearEditText) findViewById(R.id.search_et);
        txt_head_title = (TextView) findViewById(R.id.txt_head_title);
        txt_head_title.setText("进阶学院");
        txt_head_title.setTextSize(18);
        txt_head_title.getPaint().setFakeBoldText(true);
        btn_back= (LinearLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        searchViewLayout.setCacheKey(C.sp.COLLEGE_SEARCH_HISTORY);
        ryc_history= (RecyclerView) findViewById(R.id.ryc_history);
        mHistoryAdapter = new SearchHistoryAdapter(this);
        mHistoryFlexboxLayoutManager = new FlexboxLayoutManager();
        //设置主轴排列方式
        mHistoryFlexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        //设置是否换行
        mHistoryFlexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
        mHistoryFlexboxLayoutManager.setAlignItems(AlignItems.STRETCH);
        ryc_history.setLayoutManager(mHistoryFlexboxLayoutManager);
        FlexboxItemDecoration hItemDecoration = new FlexboxItemDecoration(this);
        hItemDecoration.setDrawable(new ColorDrawable() {
            @Override
            public int getIntrinsicWidth() {
                return DeviceUtil.dip2px(SearchArticleListActitivty.this, 10);
            }

            @Override
            public int getIntrinsicHeight() {
                return DeviceUtil.dip2px(SearchArticleListActitivty.this, 10);
            }
        });
        ryc_history.addItemDecoration(hItemDecoration);


        ryc_history.setAdapter(mHistoryAdapter);
        mHistoryAdapter.setListData(mHistoryList);
        searchViewLayout.setOnClickHotKeyListener(new SearchClassLayout.OnClickHotKeyListener() {
            @Override
            public void onClick(int position, SearchHotKeyBean item) {
                gotoResult(item.getKeyWord(),position);
                finish();
//                ViewShowUtils.showShortToast(SearchArticleListActitivty.this,position+","+item.getKeyWord());
            }
        });

        searchViewLayout.setOnClickHistoryListener(new SearchClassLayout.OnClickHistoryListener() {
            @Override
            public void onClick(int position, String item) {
                gotoResult(item);
                finish();
//                ViewShowUtils.showShortToast(SearchArticleListActitivty.this,position+","+item);
            }
        });

        searchViewLayout.setOnClickSearchListener(new SearchClassLayout.OnClickSearchListener() {
            @Override
            public void onClick(String searchText) {
                gotoResult(searchText);
                finish();
//                ViewShowUtils.showShortToast(SearchArticleListActitivty.this,searchText);
            }
        });



        search_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                /*判断是否是“搜索”键*/
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    String key = search_et.getText().toString().trim();
                    if(!TextUtils.isEmpty(key)){
                        gotoResult(key);
                        if (!mHistoryList.contains(key)){
                            mHistoryList.add(0,key);
                        }
                        mHistoryAdapter.setListData(mHistoryList);
                        mHistoryAdapter.notifyDataSetChanged();
                        ll_history.setVisibility(View.VISIBLE);
                        finish();
                        return true;
                    }

                }
                return false;
            }
        });


        clear.setOnClickListener(new View.OnClickListener() {//清除缓存
            @Override
            public void onClick(View v) {
                openCleanDataDialog();


            }
        });

            }

    private void openCleanDataDialog() {
        ClearSDdataDialog    textDialog = new ClearSDdataDialog(this, R.style.dialog, "提示", "是否清除搜索历史", new ClearSDdataDialog.OnOkListener() {
            @Override
            public void onClick(View dialog, String text) {
                mHistoryList.clear();
                mHistoryAdapter.setListData(null);
                mHistoryAdapter.notifyDataSetChanged();
                ll_history.setVisibility(View.GONE);
                //   clearLy.setVisibility(View.INVISIBLE);
                ViewShowUtils.showShortToast(SearchArticleListActitivty.this, "删除成功");
            }

        });
        textDialog.show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferencesUtils.putListData(this,C.Extras.KEY_SAVE_SEARCH_HISTORY, mHistoryList);
    }
}
