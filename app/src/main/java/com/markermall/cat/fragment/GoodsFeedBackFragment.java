package com.markermall.cat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.markermall.cat.Module.common.Fragment.BaseFragment;
import com.markermall.cat.Module.push.Logger;
import com.markermall.cat.R;
import com.markermall.cat.fragment.base.BaseFeedBackFragment;
import com.markermall.cat.network.BaseResponse;
import com.markermall.cat.network.RxHttp;
import com.markermall.cat.network.RxUtils;
import com.markermall.cat.network.observer.DataObserver;
import com.markermall.cat.pojo.myInfo.FeedBackBean;
import com.markermall.cat.pojo.request.RequestFeedBackTypeBean;
import com.markermall.cat.utils.ActivityStyleUtil;
import com.markermall.cat.utils.MyGsonUtils;
import com.markermall.cat.utils.UI.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品反馈
 */

public class GoodsFeedBackFragment extends BaseFragment {

    private boolean[] isLock = new boolean[6];
    private ArrayList<LinearLayout> menuList = new ArrayList<LinearLayout>();
    private String gid = "";
    private BaseFeedBackFragment mInfoFragment;
    private RecyclerView mRecyclerView;
    private GoodsFeedAdapter mGoodsFeedAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_goods_feedback, container, false);
        initBundle();
        initView(view);
        getFeedBackType();
        showAnimFragment();
        return view;
    }

    private void showAnimFragment() {
        // Create the fragment
        mInfoFragment = BaseFeedBackFragment.newInstance();
        mInfoFragment.setGId(gid);
        ActivityUtils.replaceFragmentToActivity(
                getActivity().getSupportFragmentManager(), mInfoFragment, R.id.select_img);

    }

    private void initBundle() {
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            gid = bundle.getString("gid");
        }
    }

    public void initView(View view) {
        ActivityStyleUtil.initSystemBar(getActivity(), R.color.color_757575); //设置标题栏颜色值

        mRecyclerView = view.findViewById(R.id.mListView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mGoodsFeedAdapter = new GoodsFeedAdapter(null);
        mRecyclerView.setAdapter(mGoodsFeedAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mInfoFragment != null) {
            mInfoFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    class GoodsFeedAdapter extends BaseQuickAdapter<FeedBackBean, BaseViewHolder> {

        public GoodsFeedAdapter(@Nullable List<FeedBackBean> data) {
            super(R.layout.item_goods_feed_back, data);
        }

        @Override
        protected void convert(final BaseViewHolder baseViewHolder, final FeedBackBean feedBackBean) {
            if (feedBackBean == null) return;

            baseViewHolder.setText(R.id.tv_text, feedBackBean.getTitle());
            baseViewHolder.getView(R.id.iv_click).setSelected(feedBackBean.isClick);

            baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mInfoFragment != null) {
                        mInfoFragment.setKeys(feedBackBean.getKey() + "");
                    }
                    List<FeedBackBean> items = mGoodsFeedAdapter.getData();
                    if (items != null) {
                        for (int i = 0; i < items.size(); i++) {
                            FeedBackBean feedBackBean = items.get(i);
                            if (feedBackBean != null) {
                                feedBackBean.isClick = false;
                            }
                        }
                        FeedBackBean feedBackBean = items.get(baseViewHolder.getAdapterPosition());
                        feedBackBean.isClick = true;
                        mGoodsFeedAdapter.setNewData(items);
                    }
                }
            });
        }
    }

    public void getFeedBackType() {
        RequestFeedBackTypeBean requestBean = new RequestFeedBackTypeBean();
        requestBean.setType(3);

        RxHttp.getInstance().getSysteService()
                //.getFeedBackType(3)
                .getFeedBackType(requestBean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(this.<BaseResponse<String>>bindToLifecycle())
                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onSuccess(String data) {
                        ArrayList<FeedBackBean> feedBackBeanList = null;
                        try {
                            feedBackBeanList = (ArrayList<FeedBackBean>) MyGsonUtils.getListBeanWithResult(data, FeedBackBean.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (feedBackBeanList == null || feedBackBeanList.size() == 0) return;
                        FeedBackBean feedBackBean = feedBackBeanList.get(0);
                        if (feedBackBean != null) {
                            feedBackBean.isClick = true;
                            mGoodsFeedAdapter.setNewData(feedBackBeanList);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Logger.e("==onError==" + e.getMessage());
                    }
                });
    }
}