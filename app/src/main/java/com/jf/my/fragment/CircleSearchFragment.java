package com.jf.my.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jf.my.Activity.ShowWebActivity;
import com.jf.my.R;
import com.jf.my.circle.ui.CircleSearchListFragment;
import com.jf.my.fragment.base.BaseMainFragmeng;
import com.jf.my.network.BaseResponse;
import com.jf.my.network.RxHttp;
import com.jf.my.network.RxUtils;
import com.jf.my.network.observer.DataObserver;
import com.jf.my.pojo.SearchHotKeyBean;
import com.jf.my.pojo.request.RequestCircleSearchBean;
import com.jf.my.utils.C;
import com.jf.my.utils.OpenFragmentUtils;
import com.jf.my.view.SearchViewLayout;

import java.util.List;

import butterknife.ButterKnife;

/**
 - @Description:  蜜粉圈搜索
 - @Author:  wuchaowen
 - @Time:  2019/8/29 16:19
 **/
public class CircleSearchFragment extends BaseMainFragmeng {
    private View mView;
    private SearchViewLayout searchViewLayout;

    public CircleSearchFragment() {
    }

    public static void start(Activity activity) {
        OpenFragmentUtils.goToSimpleFragment(activity, CircleSearchFragment.class.getName(), null);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_circle_search, container, false);
        ButterKnife.bind(this, mView);
        initView(mView);
        return mView;
    }

    /**
     * 初始化界面
     */
    private void initView(View view) {
        searchViewLayout = view.findViewById(R.id.searchViewLayout);
        searchViewLayout.setCacheKey(C.sp.CIRCLE_SEARCH_HISTORY);
        searchViewLayout.setOnClickHotKeyListener(new SearchViewLayout.OnClickHotKeyListener() {
            @Override
            public void onClick(int position, SearchHotKeyBean item) {

                if(null != item && item.getOpen() == 1){
                    if(null != item && !TextUtils.isEmpty(item.getKeyWord())){
                        CircleSearchListFragment.start(getActivity(),item.getKeyWord());
                    }
                }else if(null != item && item.getOpen() == 3){
                    ShowWebActivity.start(getActivity(), item.getUrl(), item.getKeyWord());
                }
            }
        });

        searchViewLayout.setOnClickHistoryListener(new SearchViewLayout.OnClickHistoryListener() {
            @Override
            public void onClick(int position, String item) {
                CircleSearchListFragment.start(getActivity(),item);
            }
        });

        searchViewLayout.setOnClickSearchListener(new SearchViewLayout.OnClickSearchListener() {
            @Override
            public void onClick(String searchText) {
                CircleSearchListFragment.start(getActivity(),searchText);
            }
        });
        getSearchHotKey();
    }



    public void getSearchHotKey(){
        RequestCircleSearchBean requestBean = new RequestCircleSearchBean();
         RxHttp.getInstance().getSysteService()
                .getCircleHotKey(requestBean)
                .compose(RxUtils.<BaseResponse<List<SearchHotKeyBean>>>switchSchedulers())
                .compose(this.<BaseResponse<List<SearchHotKeyBean>>>bindToLifecycle())
                 .subscribe(new DataObserver<List<SearchHotKeyBean>>() {


                     @Override
                     protected void onSuccess(List<SearchHotKeyBean> data) {
                         searchViewLayout.setHotKeyData(data);
                     }
                 });

    }


}
