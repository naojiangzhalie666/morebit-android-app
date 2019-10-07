package com.markermall.cat.info.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.markermall.cat.Module.common.View.ReUseListView;
import com.markermall.cat.R;
import com.markermall.cat.adapter.SimpleAdapter;
import com.markermall.cat.adapter.holder.SimpleViewHolder;
import com.markermall.cat.info.adapter.MsgEarningsAdapter;
import com.markermall.cat.info.contract.MsgContract;
import com.markermall.cat.info.model.InfoModel;
import com.markermall.cat.info.presenter.MsgPresenter;
import com.markermall.cat.mvp.base.base.BaseView;
import com.markermall.cat.mvp.base.frame.MvpFragment;
import com.markermall.cat.network.CommonEmpty;
import com.markermall.cat.pojo.EarningsMsg;
import com.markermall.cat.pojo.ImageInfo;
import com.markermall.cat.pojo.event.MsgSysScore;
import com.markermall.cat.utils.DateTimeUtils;
import com.markermall.cat.utils.DensityUtil;
import com.markermall.cat.utils.LoadImgUtils;
import com.markermall.cat.utils.UI.BannerInitiateUtils;
import com.markermall.cat.view.AspectRatioView;
import com.markermall.cat.view.ToolbarHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by YangBoTian on 2018/11/26.
 */

public class MsgSysFragment extends MvpFragment<MsgPresenter> implements MsgContract.View {
    private static final int REQUEST_COUNT = 10;

    @BindView(R.id.mListView)
    ReUseListView mReUseListView;
    private int page = 1;
    private MsgAdapter mAdapter;

    private CommonEmpty mEmptyView;

    public static MsgSysFragment newInstance() {
        Bundle args = new Bundle();
        MsgSysFragment fragment = new MsgSysFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        refreshData();
    }


    @Override
    protected void initView(View view) {
        new ToolbarHelper(this).setToolbarAsUp().setCustomTitle("系统通知");
        mEmptyView = new CommonEmpty(view, getString(R.string.no_msg), R.drawable.image_meiyouxiaoxi);
        mAdapter = new MsgAdapter(getActivity());
        mReUseListView.getSwipeList().setOnRefreshListener(new com.markermall.cat.Module.common.widget.SwipeRefreshLayout.OnRefreshListener() {
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
                    mPresenter.getMsg(MsgSysFragment.this, InfoModel.msgSysType, page, mEmptyView);
                }

            }
        });
        mReUseListView.getListView().setMarkermallNoMore(true);
        mReUseListView.setAdapter(mAdapter);

    }


    private void refreshData() {
        mReUseListView.getSwipeList().post(new Runnable() {

            @Override
            public void run() {
                mReUseListView.getSwipeList().setRefreshing(true);
            }
        });
        page = 1;
        mReUseListView.getListView().setNoMore(false);
        mReUseListView.getListView().setFootViewVisibility(View.GONE);
        mPresenter.getMsg(this, InfoModel.msgSysType, page, mEmptyView);
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
            mAdapter.replace(MsgEarningsFragment.handlerData(data));
            mAdapter.notifyDataSetChanged();
            if (data.size() < 10) {
                mReUseListView.getListView().setFootViewVisibility(View.VISIBLE);
                mReUseListView.getListView().setNoMore(true);
            }
        } else {
            mAdapter.add(MsgEarningsFragment.handlerData(data));
            mAdapter.notifyDataSetChanged();
        }
        page++;
    }

    @Override
    public void onMsgfailure() {
        mReUseListView.getListView().setNoMore(true);
    }

    @Override
    public void onMsgFinally() {
        mReUseListView.getListView().refreshComplete(REQUEST_COUNT);
        mReUseListView.getSwipeList().setRefreshing(false);
    }


    @Subscribe  //订阅事件
    public void onEventMainThread(MsgSysScore event) {
//        if (event.getAction().equals(EventBusAction.MSG_NOTICE_SYS)) {
        refreshData();
//        }
    }


    class MsgAdapter extends SimpleAdapter<EarningsMsg, SimpleViewHolder> {

        private LayoutInflater mInflater;
        private Context mContext;

        public MsgAdapter(Context context) {
            super(context);
            this.mContext = context;
        }

        @Override
        public int getItemViewType(int position) {
            if (getItem(position).getViewType() == EarningsMsg.TWO_TYPE) {
                return EarningsMsg.TWO_TYPE;
            }
            return EarningsMsg.ONE_TYPE;
        }

        @Override
        public void onBindViewHolder(SimpleViewHolder holder, int position) {
            final EarningsMsg item = getItem(position);

            if (holder instanceof MsgEarningsAdapter.TimeViewHolder) {
                TextView time = holder.viewFinder().view(R.id.time);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) time.getLayoutParams();
                layoutParams.setMargins(DensityUtil.dip2px(mContext, (float) 8), DensityUtil.dip2px(mContext, (float) 8), 0, 0);
                time.setLayoutParams(layoutParams);
                String currentDay = DateTimeUtils.getDatetoString(item.getCreateTime());
                if (currentDay.equals("昨日")) {
                    time.setText(currentDay);
                } else {
                    time.setText(DateTimeUtils.getYMDTime(item.getCreateTime()));
                }

            } else {
                TextView tv_name = holder.viewFinder().view(R.id.tv_name);
                TextView tv_content = holder.viewFinder().view(R.id.tv_content);
                TextView tv_time = holder.viewFinder().view(R.id.tv_time);
                final CardView cardView = holder.viewFinder().view(R.id.cardView);
                final View line = holder.viewFinder().view(R.id.line);
                AspectRatioView as_banner_make_money = holder.viewFinder().view(R.id.as_banner_make_money);
                ImageView iv_img = holder.viewFinder().view(R.id.iv_img);
                tv_name.setText(item.getTitle());
                if (TextUtils.isEmpty(item.getPicture())) {
                    tv_content.setText(item.getDigest());
                    tv_content.setVisibility(View.VISIBLE);
                    as_banner_make_money.setVisibility(View.GONE);
                } else {
                    LoadImgUtils.loadingCornerBitmap(getActivity(), iv_img, item.getPicture(), 3);
                    tv_content.setVisibility(View.GONE);
                    as_banner_make_money.setVisibility(View.VISIBLE);
                }
                tv_time.setText(DateTimeUtils.getHMSTime(item.getCreateTime()));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                    if (item.getReadStatus() == 0) {
//                        setRead(item, dot);
//                    } else {
//                        ShowWebActivity.start(getActivity(), item.getContentSourceUrl(), item.getTitle());
                        ImageInfo info = new ImageInfo();
                        info.setId(item.getId());
                        info.setClassId(item.getClassId());
                        info.setOpen(item.getOpen());
                        info.setWidth(item.getWidth());
                        info.setHeight(item.getHeight());
                        info.setBackgroundImage(item.getBackgroundImage());
                        info.setTitle(item.getTitle());
                        info.setPicture(item.getPicture());
                        info.setUrl(item.getContentSourceUrl());
                        BannerInitiateUtils.gotoAction(getActivity(), info);
//                    }
                    }
                });
                cardView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int height = cardView.getHeight();
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) line.getLayoutParams();
                        params.height = height;
                        line.setLayoutParams(params);
                        cardView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }

        }

        @Override
        protected View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
            return layoutInflater.inflate(R.layout.item_msg_sys, parent, false);
        }

        @Override
        public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == EarningsMsg.TWO_TYPE)
                return new MsgEarningsAdapter.TimeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg_time, parent, false));
            return super.onCreateViewHolder(parent, viewType);
        }

    }


//
//    private void setRead(final EarningsMsg item, final View view) {
//        RequestCircleShareCountBean bean = new RequestCircleShareCountBean();
//        bean.setId(item.getId() + "");
//        RxHttp.getInstance().getCommonService().updateNoticeStatus(bean)
//                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
//                .compose(this.<BaseResponse<String>>bindToLifecycle())
//                .subscribe(new DataObserver<String>() {
//
//                    @Override
//                    protected void onDataNull() {
//                        onSuccess(null);
//                    }
//
//                    @Override
//                    protected void onSuccess(String data) {
//                        view.setVisibility(View.GONE);
//                        MsgMainScore messageEvent = new MsgMainScore();
////                        messageEvent.setAction(EventBusAction.MSG_NOTICE);
//                        EventBus.getDefault().postSticky(messageEvent);
////                        ShowWebActivity.start(getActivity(), item.getContentSourceUrl(), item.getTitle());
//
//                    }
//
//                });
//    }


}
