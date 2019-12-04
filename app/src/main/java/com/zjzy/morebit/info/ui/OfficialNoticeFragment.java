package com.zjzy.morebit.info.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.zjzy.morebit.Module.common.View.ReUseListView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.SimpleAdapter;
import com.zjzy.morebit.adapter.holder.SimpleViewHolder;
import com.zjzy.morebit.info.contract.OfficialNoticeContract;
import com.zjzy.morebit.info.presenter.OfficialNoticePresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.pojo.ProtocolRuleBean;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.PageToUtil;
import com.zjzy.morebit.view.ToolbarHelper;

import java.util.List;

import butterknife.BindView;

/**
 * Created by fengrs on 2018/8/3.
 * 备注:  官方公告
 */

public class OfficialNoticeFragment extends MvpFragment<OfficialNoticePresenter> implements OfficialNoticeContract.View {

    @BindView(R.id.mListView)
    ReUseListView mRecyclerView;
    NoticeAdapter mAdapter;
    private static final int REQUEST_COUNT = 10;
    String mTitle = "";

    @Override
    protected void initData() {
        mPresenter.getOfficialNotice(OfficialNoticeFragment.this, C.requestType.initData);
    }

    @Override
    protected void initView(View view) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mTitle = bundle.getString("title");
            new ToolbarHelper(this).setToolbarAsUp().setCustomTitle(mTitle);
        }
        mAdapter = new NoticeAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.getListView().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!mRecyclerView.getSwipeList().isRefreshing())
                    mPresenter.getOfficialNotice(OfficialNoticeFragment.this, C.requestType.loadMore);
            }
        });
        mRecyclerView.getSwipeList().setOnRefreshListener(new com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRecyclerView.getSwipeList().setRefreshing(true);
                mPresenter.getOfficialNotice(OfficialNoticeFragment.this, C.requestType.initData);

            }
        });
    }

    @Override
    protected int getViewLayout() {
        return R.layout.fragment_official_notice;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }


    @Override
    public void showFinally() {
        mRecyclerView.getSwipeList().setRefreshing(false);
    }

    @Override
    public void showData(List<ProtocolRuleBean> officialNotices, int type) {
        if (type != C.requestType.initData) {
            mRecyclerView.getListView().refreshComplete(REQUEST_COUNT);
        }
        if (type == C.requestType.initData) {
            mAdapter.replace(officialNotices);
            mRecyclerView.getListView().setNoMore(false);
        } else {
            if (officialNotices.size() != 0) {
                mAdapter.add(officialNotices);
            } else {
                mRecyclerView.getListView().setNoMore(true);
            }
        }
        mRecyclerView.notifyDataSetChanged();
    }

    @Override
    public void showFailureMessage() {
        mRecyclerView.getListView().setNoMore(true);
    }

    class NoticeAdapter extends SimpleAdapter<ProtocolRuleBean, SimpleViewHolder> {
        Context context;

        public NoticeAdapter(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        public void onBindViewHolder(SimpleViewHolder holder, int position) {

            final ProtocolRuleBean item = getItem(position);
            TextView title = holder.viewFinder().view(R.id.title);
            TextView create_time = holder.viewFinder().view(R.id.create_time);
            title.setText(item.getTitle());
            create_time.setText(item.getCreateTime());
            LoadImgUtils.loadingCornerBitmap(context, (ImageView) holder.viewFinder().view(R.id.image),
                    item.getIcon(), R.color.white, 10,
                    false, false, true, true
            );
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PageToUtil.goToWebview(getActivity(), mTitle, item.getHtmlUrl());
                }
            });
        }

        @Override
        protected View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
            return layoutInflater.inflate(R.layout.item_official_notice, parent, false);
        }
    }
}
