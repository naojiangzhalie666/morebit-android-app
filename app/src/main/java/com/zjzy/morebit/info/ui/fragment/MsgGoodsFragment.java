package com.zjzy.morebit.info.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.Activity.ShowWebActivity;
import com.zjzy.morebit.Module.common.View.ReUseListView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.SimpleAdapter;
import com.zjzy.morebit.adapter.holder.SimpleViewHolder;
import com.zjzy.morebit.info.contract.MsgContract;
import com.zjzy.morebit.info.model.InfoModel;
import com.zjzy.morebit.info.presenter.MsgPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.network.CommonEmpty;
import com.zjzy.morebit.pojo.EarningsMsg;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.utils.GoodsUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.action.MyAction;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by YangBoTian on 2018/11/26.
 */

public class MsgGoodsFragment extends MvpFragment<MsgPresenter> implements MsgContract.View {
    private static final int REQUEST_COUNT = 10;

    @BindView(R.id.mListView)
    ReUseListView mReUseListView;
    private int page = 1;
    private MsgAdapter mAdapter;

    private CommonEmpty mEmptyView;
    private TextView txt_head_title;
    private LinearLayout btn_back;

    public static MsgGoodsFragment newInstance() {
        Bundle args = new Bundle();
        MsgGoodsFragment fragment = new MsgGoodsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initData() {
        refreshData();
    }


    @Override
    protected void initView(View view) {
        txt_head_title = (TextView) view.findViewById(R.id.txt_head_title);
        txt_head_title.setText("每日好货");
        txt_head_title.setTextSize(18);
        btn_back = (LinearLayout) view.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        mEmptyView = new CommonEmpty(view, "暂无好货", R.drawable.image_meiyouxiaoxi);
        mAdapter = new MsgAdapter(getActivity());
        mReUseListView.getSwipeList().setOnRefreshListener(new com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout.OnRefreshListener() {
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
                    mPresenter.getMsg(MsgGoodsFragment.this, InfoModel.msgGoodsype, page, mEmptyView);
                }

            }
        });
        mReUseListView.getListView().setMarkermallNoMore(true);
        mReUseListView.setAdapter(mAdapter);

    }


    private void refreshData() {
        mReUseListView.getSwipeList().setRefreshing(true);
        page = 1;
        mReUseListView.getListView().setNoMore(false);
        mReUseListView.getListView().setFootViewVisibility(View.GONE);
        mPresenter.getMsg(this, InfoModel.msgGoodsype, page, mEmptyView);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.fragment_msg_list3;
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
         final EarningsMsg item = getItem(position);

//            if(holder instanceof MsgEarningsAdapter.TimeViewHolder) {
//                TextView time = holder.viewFinder().view(R.id.time);
//                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) time.getLayoutParams();
//                layoutParams.setMargins(DensityUtil.dip2px(mContext,(float)10),DensityUtil.dip2px(mContext,(float)11),0,0);
//                time.setLayoutParams(layoutParams);
//                String currentDay = DateTimeUtils.getDatetoString(item.getCreateTime());
//                time.setText(item.getCreateTime()+"");
////                if(currentDay.equals("昨日")){
////                    time.setText(currentDay);
////                }else{
////                    time.setText(DateTimeUtils.getYMDTime(item.getCreateTime()));
////                }
//
//            } else {
                TextView time = holder.viewFinder().view(R.id.time);
                LinearLayout rl2=holder.viewFinder().view(R.id.rl2);
                ImageView img_bg = holder.viewFinder().view(R.id.img_bg);

                TextView tv_title = holder.viewFinder().view(R.id.tv_title);
                TextView tv_content = holder.viewFinder().view(R.id.tv_content);
                MyLog.i("test", "item.getPicture(): " + item.getPicture());
                LoadImgUtils.setImg(mContext, img_bg, item.getPicture());
                time.setText(item.getCreateTime()+"");
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tv_time.getLayoutParams();
//            layoutParams.setMargins(DensityUtil.dip2px(mContext,(float)10),DensityUtil.dip2px(mContext,(float)11),0,0);
//            tv_time.setLayoutParams(layoutParams);


                tv_title.setText(item.getTitle());
                tv_content.setText(item.getContentDesc());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GoodsUtil.checkGoods((RxAppCompatActivity) mContext,item.getGoodsId(), new MyAction.One<ShopGoodInfo>() {
                            @Override
                            public void invoke(ShopGoodInfo arg) {
                                MyLog.i("test", "arg: " + arg);
                                GoodsDetailActivity.start(mContext, arg);
                            }
                        });
                    }
                });
            }

//        }

        @Override
        protected View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
            return layoutInflater.inflate(R.layout.item_msg_goods, parent, false);
        }
    }


}
