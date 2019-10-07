package com.jf.my.info.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.jf.my.Module.common.View.ReUseListView;
import com.jf.my.R;
import com.jf.my.adapter.SimpleAdapter;
import com.jf.my.adapter.holder.SimpleViewHolder;
import com.jf.my.info.adapter.MsgEarningsAdapter;
import com.jf.my.info.contract.MsgContract;
import com.jf.my.info.model.InfoModel;
import com.jf.my.info.presenter.MsgPresenter;
import com.jf.my.mvp.base.base.BaseView;
import com.jf.my.mvp.base.frame.MvpFragment;
import com.jf.my.network.CommonEmpty;
import com.jf.my.pojo.EarningsMsg;
import com.jf.my.utils.DateTimeUtils;
import com.jf.my.utils.DensityUtil;
import com.jf.my.utils.LoadImgUtils;
import com.jf.my.utils.MyLog;
import com.jf.my.view.ToolbarHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by YangBoTian on 2018/11/26.
 */

public class MsgFansFragment extends MvpFragment<MsgPresenter> implements MsgContract.View {
    private static final int REQUEST_COUNT = 10;

    @BindView(R.id.mListView)
    ReUseListView mReUseListView;
    private int page = 1;
    private MsgAdapter mAdapter;

    private CommonEmpty mEmptyView;

    public static MsgFansFragment newInstance() {
        Bundle args = new Bundle();
        MsgFansFragment fragment = new MsgFansFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initData() {
        refreshData();
    }


    @Override
    protected void initView(View view) {
        new ToolbarHelper(this).setToolbarAsUp().setCustomTitle("粉丝消息");
        mEmptyView = new CommonEmpty(view, getString(R.string.no_msg), R.drawable.image_meiyouxiaoxi);
        mAdapter = new MsgAdapter(getActivity());
        mReUseListView.getSwipeList().setOnRefreshListener(new com.jf.my.Module.common.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                refreshData();
            }
        });
        mReUseListView.getListView().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!mReUseListView.getSwipeList().isRefreshing()) {
                    mPresenter.getMsg(MsgFansFragment.this, InfoModel.msgFansType, page, mEmptyView);
                }

            }
        });
        mReUseListView.getListView().setMiyuanNoMore(true);
        mReUseListView.setAdapter(mAdapter);

    }


    private void refreshData() {
        mReUseListView.getSwipeList().setRefreshing(true);
        page = 1;
        mReUseListView.getListView().setNoMore(false);
        mReUseListView.getListView().setFootViewVisibility(View.GONE);
        mPresenter.getMsg(this, InfoModel.msgFansType, page, mEmptyView);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.fragment_msg_list;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }

    @OnClick({R.id.empty_view})
    public void Onclick(View v) {
        switch (v.getId()) {
            case R.id.empty_view:
                refreshData();
                break;
        }
    }


    @Override
    public void onMsgSuccessful(List<EarningsMsg> data) {
        if (page == 1) {
            mAdapter.replace(data );
            mAdapter.notifyDataSetChanged();
            if(data.size()<10){
                mReUseListView.getListView().setFootViewVisibility(View.VISIBLE);
                mReUseListView.getListView().setNoMore(true);
            }
        } else {

            mAdapter.add(data);
            mAdapter.notifyDataSetChanged();
        }
        page++;
    }

    @Override
    public void onMsgfailure() {
        if (page != 1) {
            mReUseListView.getListView().setNoMore(true);
        }

    }

    @Override
    public void onMsgFinally() {
        mReUseListView.getListView().refreshComplete(REQUEST_COUNT);
        mReUseListView.getSwipeList().setRefreshing(false);
    }


    class MsgAdapter extends SimpleAdapter<EarningsMsg, SimpleViewHolder> {

        private LayoutInflater mInflater;
        private Context mContext;

        public MsgAdapter(Context context) {
            super(context);
            this.mContext = context;
        }


        @Override
        public void onBindViewHolder(SimpleViewHolder holder, int position) {
            EarningsMsg item = getItem(position);
//            if(holder instanceof MsgEarningsAdapter.TimeViewHolder) {
//                TextView time = holder.viewFinder().view(R.id.time);
//                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) time.getLayoutParams();
//                layoutParams.setMargins(DensityUtil.dip2px(mContext,(float)10),DensityUtil.dip2px(mContext,(float)11),0,0);
//                time.setLayoutParams(layoutParams);
//                String currentDay = DateTimeUtils.getDatetoString(item.getCreateTime());
//                if(currentDay.equals("昨日")){
//                    time.setText(currentDay);
//                }else{
//                    time.setText(DateTimeUtils.getYMDTime(item.getCreateTime()));
//                }
//
//            } else {
                ImageView iv_img = holder.viewFinder().view(R.id.iv_icon);
                TextView tv_name = holder.viewFinder().view(R.id.tv_name);
                TextView tv_content = holder.viewFinder().view(R.id.tv_content);
                TextView tv_time = holder.viewFinder().view(R.id.time);
                MyLog.i("test", "item.getPicture(): " + item.getPicture());
                LoadImgUtils.setImgHead(mContext, iv_img, item.getPicture());
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tv_time.getLayoutParams();
            layoutParams.setMargins(DensityUtil.dip2px(mContext,(float)10),DensityUtil.dip2px(mContext,(float)11),0,0);
            tv_time.setLayoutParams(layoutParams);
//            if(!TextUtils.isEmpty(item.getPicture())){
//                LoadImgUtils.setImg(mContext, iv_img,item.getPicture(),R.drawable.logo);
//            } else {
//                iv_img.setImageResource(R.drawable.logo);
//            }

                tv_name.setText(item.getTitle());
                tv_content.setText(item.getDigest());
                tv_time.setText(item.getCreate_time());
//            }

        }

        @Override
        protected View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
            return layoutInflater.inflate(R.layout.item_msg_fans, parent, false);
        }
    }


}
