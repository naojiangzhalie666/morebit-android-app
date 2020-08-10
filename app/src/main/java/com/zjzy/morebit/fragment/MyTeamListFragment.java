package com.zjzy.morebit.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.zjzy.morebit.Activity.InvateActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Dialog.FansRemarkDialog;
import com.zjzy.morebit.Module.common.Dialog.QrcodeDialog;
import com.zjzy.morebit.Module.common.Fragment.BaseFragment;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.Module.common.View.ReUseListView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.MyTeamAdapter;
import com.zjzy.morebit.info.ui.fragment.ShareFriendsFragment;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.FansInfo;
import com.zjzy.morebit.pojo.TeamData;
import com.zjzy.morebit.pojo.TeamInfo;
import com.zjzy.morebit.pojo.event.MyFansEvent;
import com.zjzy.morebit.pojo.request.RequestFansInfoBean;
import com.zjzy.morebit.pojo.request.RequestRemarkBean;
import com.zjzy.morebit.pojo.request.RequestTeanmListBean;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.OpenFragmentUtils;
import com.zjzy.morebit.utils.StringsUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;

import static android.content.Context.INPUT_METHOD_SERVICE;


/**
 * 我的团队列表
 * Created by Dell on 2016/3/22.
 */
public class MyTeamListFragment extends BaseFragment {
    public static final int TYPE_NORMAL = 0;      //我的粉丝
    public static final int TYPE_DYNAMIC_RANK = 1; //动态排行
    public static final String TYPE_DYNAMIC_RANK_ACTIVITY = "1"; //动态排行下活跃tab
    public static final String TYPE_DYNAMIC_RANK_COMMISSION = "2"; //动态排行下近七天预估佣金tab
    public static final String TYPE_DYNAMIC_RANK_NEW = "3"; //动态排行下近七天拉新tab
    private ReUseListView mRecyclerView;
    private MyTeamAdapter myTeamAdapter;
    private static final int REQUEST_COUNT = 10;
    List<TeamInfo> listArray = new ArrayList<>();
    private int pageNum = 1;


    private LinearLayout dateNullView;
    private TextView btn_invite;
    private TextView tv_hint;
    private ImageView iv_icon_null;
    private int mBUserId;
    private int mAllFnas;
    private int mConsumer;
    private int mAgent;

    private QrcodeDialog mQrcodeDialog;
    private FansRemarkDialog mRemarkDialog;
    private String mType;
    private int mFrom;
    private String mOrder = "";
    private boolean isInit;
    private String sOrder = "desc";
    private LinearLayout title_zong_volume_ll;
    private ImageView title_comprehensive_iv;
    private boolean isTime=false;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    Bundle bundle = msg.getData();
                    if (bundle != null) {

                        TeamInfo teamInfo = (TeamInfo) bundle.getSerializable("info");
                        FansInfo fansInfo = (FansInfo) bundle.getSerializable("fansInfo");
                        mQrcodeDialog.refreshData(teamInfo, fansInfo);
                    }
                    break;
                case 1:
                    Bundle bundle1 = msg.getData();
                    if (bundle1 != null) {
                        String remark = bundle1.getString("remark");
                        int position = bundle1.getInt("position");
                        mRemarkDialog.setRemarkAnPosition(remark, position);
                        break;
                    }
            }
        }
    };

    public static MyTeamListFragment newInstance(String team_type, int from) {
        Bundle args = new Bundle();
        args.putString("team_type", team_type);
        args.putInt("from", from);
        MyTeamListFragment fragment = new MyTeamListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myteam_list, container, false);
        initView(view);
        return view;
    }


    public void initView(View view) {
        isInit = true;
        initBundle();
        MyLog.i("test", "this: " + mType + "  " + this.hashCode());
        mRecyclerView = (ReUseListView) view.findViewById(R.id.listview_aole);
        //数据为空的
        dateNullView = (LinearLayout) view.findViewById(R.id.dateNullView);
        btn_invite = (TextView) view.findViewById(R.id.btn_invite);
        iv_icon_null =  view.findViewById(R.id.iv_icon_null);
        tv_hint = view.findViewById(R.id.tv_hint);
        title_comprehensive_iv=view.findViewById(R.id.title_comprehensive_iv);
        title_zong_volume_ll=view.findViewById(R.id.title_zong_volume_ll);
        myTeamAdapter = new MyTeamAdapter(getActivity(), listArray);
        myTeamAdapter.setUserInfo(UserLocalData.getUser(getActivity()));
        if (mFrom == MyTeamListFragment.TYPE_DYNAMIC_RANK) {
            myTeamAdapter.setType(mType);
            if(mType==TYPE_DYNAMIC_RANK_COMMISSION){
                iv_icon_null.setImageResource(R.drawable.icon_fans_dynmic_rank_null);
                tv_hint.setText(R.string.text_fans_dynmic_rank_commission_null);
                btn_invite.setVisibility(View.GONE);
            } else if(mType ==  TYPE_DYNAMIC_RANK_NEW){
                tv_hint.setText(R.string.text_fans_dynmic_rank_new_null);
                iv_icon_null.setImageResource(R.drawable.icon_fans_dynmic_rank_null);
                btn_invite.setVisibility(View.GONE);
            }
        }
        mRecyclerView.setAdapter(myTeamAdapter);
        mRecyclerView.getSwipeList().setOnRefreshListener(new com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        mRecyclerView.getListView().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mFrom == TYPE_NORMAL) {
                    getMoreData();
                } else {
                    getRankMoreData();
                }

            }
        });

        title_zong_volume_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTime){
                    sOrder="desc";
                    isTime=false;
                    title_comprehensive_iv.setImageResource(R.drawable.icon_jiage_down);
                }else{
                    title_comprehensive_iv.setImageResource(R.drawable.icon_jiage_up);
                    sOrder="asc";
                    isTime=true;
                }
                getFirstData();
            }
        });

        btn_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到分享界面
             //   OpenFragmentUtils.goToSimpleFragment(getActivity(), ShareFriendsFragment.class.getName(), new Bundle());
                startActivity(new Intent(getActivity(), InvateActivity.class));
            }
        });

        myTeamAdapter.setOnAdapterClickListener(new MyTeamAdapter.OnAdapterClickListener() {
            @Override
            public void onItem(int position) {
                findFansDetail(listArray.get(position), listArray.get(position).getId() + "");

            }

            @Override
            public void onRemark(int position) {
                openRemarkDialog(listArray.get(position).getRemark(), position);
            }
        });
        mRecyclerView.setShowStick(false);
        mRecyclerView.setOnExternalScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisibleItemPosition = linearManager.findFirstVisibleItemPosition();
//                if (firstVisibleItemPosition > 3) {
//                    ((FansListFragment)getActivity()).isShowStick(true);
//                } else {
//                    ((FansListFragment)getActivity()).isShowStick(false);
//                }
            }
        });
        loadData();
    }


    private void loadData() {
        mRecyclerView.getSwipeList().setRefreshing(true);
        initData();
    }

    private void initBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mType = bundle.getString("team_type");
            mFrom = bundle.getInt("from");
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (mFrom == TYPE_NORMAL) {
            getFirstData();
        } else {
            getRankFirstData(mOrder);
        }

    }


    private void findFansDetail(final TeamInfo info, String userId) {
        RequestFansInfoBean requestFansInfoBean = new RequestFansInfoBean();
        requestFansInfoBean.setId(userId);
        RxHttp.getInstance().getCommonService().getFansDetail(requestFansInfoBean)
                .compose(RxUtils.<BaseResponse<FansInfo>>switchSchedulers())
                .subscribe(new DataObserver<FansInfo>() {
                    @Override
                    protected void onSuccess(FansInfo data) {
                        openQrcodeDialog(info, data);
                    }
                });
    }


    /**
     * 第一次获取数据
     */
    public void getFirstData() { //获取我的团队A级数据
        MyLog.i("test", "getFirstData");
        mRecyclerView.getListView().setNoMore(false);
        pageNum = 1;

        getCompose()
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                        mRecyclerView.getSwipeList().setRefreshing(false);
                    }
                })
                .subscribe(new DataObserver<TeamData>() {
                    @Override
                    protected void onDataListEmpty() {
                        dateNullView.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                        listArray.clear();
                        myTeamAdapter.setData(listArray);
                        mRecyclerView.notifyDataSetChanged();
                    }


                    @Override
                    protected void onSuccess(TeamData data) {
                        mRecyclerView.getSwipeList().setRefreshing(false);
                        listArray.clear();
                        myTeamAdapter.setData(listArray);
                        mRecyclerView.notifyDataSetChanged();
                        List<TeamInfo> teamList = data.getChild();
                        if (teamList != null && teamList.size() > 0) {
                            listArray.clear();
                            listArray.addAll(teamList);
                            myTeamAdapter.setData(listArray);
                            mRecyclerView.notifyDataSetChanged();
                            pageNum = pageNum + 1;
                        } else {
                            dateNullView.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.GONE);
                            listArray.clear();
                            myTeamAdapter.setData(listArray);
                            mRecyclerView.notifyDataSetChanged();
                            return;
                        }
                        MyFansEvent myFansEvent = new MyFansEvent();
                        myFansEvent.setAllFansCount(data.getAllFansCount());
                        myFansEvent.setConsumerCount(data.getConsumerCount());
                        myFansEvent.setAgentCount(data.getAgentCount());
                        myFansEvent.setCurType(mType);
                        EventBus.getDefault().post(myFansEvent);
                    }
                });

    }


    /**
     * 获取排行榜第一次获取数据
     */
    public void getRankFirstData(String order) {

        if (TextUtils.isEmpty(mOrder) && !getUserVisibleHint()) {
            return;
        }
        if (!isInit) {
            return;
        }
        mRecyclerView.getListView().setNoMore(false);
        pageNum = 1;
        mOrder = order;

        getRankCompose()
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                        mRecyclerView.getSwipeList().setRefreshing(false);
                    }
                })
                .subscribe(new DataObserver<List<TeamInfo>>() {
                    @Override
                    protected void onDataListEmpty() {
                        dateNullView.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                        listArray.clear();
                        myTeamAdapter.setData(listArray);
                        mRecyclerView.notifyDataSetChanged();
                    }

                    @Override
                    protected void onSuccess(List<TeamInfo> data) {
                        mRecyclerView.getSwipeList().setRefreshing(false);
                        listArray.clear();
                        myTeamAdapter.setData(listArray);
                        mRecyclerView.notifyDataSetChanged();

                        if (data != null && data.size() > 0) {
                            listArray.clear();
                            listArray.addAll(data);
                            myTeamAdapter.setData(listArray);
                            mRecyclerView.notifyDataSetChanged();
                            pageNum = pageNum + 1;
                        } else {
                            dateNullView.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.GONE);
                            listArray.clear();
                            myTeamAdapter.setData(listArray);
                            mRecyclerView.notifyDataSetChanged();
                            return;
                        }

                    }
                });

    }

    private Observable<BaseResponse<TeamData>> getCompose() {

        RequestTeanmListBean requestTeanmListBean = new RequestTeanmListBean();
        requestTeanmListBean.setLevel(mType);
        requestTeanmListBean.setPage(pageNum);
        requestTeanmListBean.setOrder(sOrder);
        return RxHttp.getInstance().getCommonService().getTeanmList(requestTeanmListBean)
                .compose(RxUtils.<BaseResponse<TeamData>>switchSchedulers())
                .compose(this.<BaseResponse<TeamData>>bindToLifecycle());
    }


    private Observable<BaseResponse<List<TeamInfo>>> getRankCompose() {
        int type = 0;
        try {
            type = Integer.parseInt(mType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestTeanmListBean requestTeanmListBean = new RequestTeanmListBean();
        requestTeanmListBean.setType(type);
        requestTeanmListBean.setPage(pageNum);
        requestTeanmListBean.setOrder(mOrder);
        return RxHttp.getInstance().getCommonService().getTeamRanking(requestTeanmListBean)
                .compose(RxUtils.<BaseResponse<List<TeamInfo>>>switchSchedulers())
                .compose(this.<BaseResponse<List<TeamInfo>>>bindToLifecycle());
    }

    /**
     * 加载更多数据
     */
    public void getMoreData() {

        getCompose()
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mRecyclerView.getSwipeList().setRefreshing(false);
                    }
                })
                .subscribe(new DataObserver<TeamData>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        if(StringsUtils.isDataEmpty(errCode)){
                            mRecyclerView.getListView().refreshComplete(REQUEST_COUNT);
                            mRecyclerView.getListView().setNoMore(true);
                        }
                    }

                    @Override
                    protected void onSuccess(TeamData response) {
                        mRecyclerView.getListView().refreshComplete(REQUEST_COUNT);
                        List<TeamInfo> teamList = response.getChild();
                        if (teamList != null && teamList.size() > 0) {
                            listArray.addAll(teamList);
                            myTeamAdapter.setData(listArray);
                            mRecyclerView.notifyDataSetChanged();
                            //                            myTeamAdapter.notifyItemRangeChanged(listArray.size() - teamList.size(), listArray.size());
                            pageNum = pageNum + 1;
                        } else {
                            mRecyclerView.getListView().setNoMore(true);
                            //                            ViewShowUtils.showLongToast(getActivity(),"已经没有更多数据了");
                        }
                    }

                });

    }

    /**
     * 加载排行榜更多数据
     */
    public void getRankMoreData() {

        getRankCompose()
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mRecyclerView.getSwipeList().setRefreshing(false);
                    }
                })
                .subscribe(new DataObserver<List<TeamInfo>>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        if(StringsUtils.isDataEmpty(errCode)){
                            mRecyclerView.getListView().refreshComplete(REQUEST_COUNT);
                            mRecyclerView.getListView().setNoMore(true);
                        }
                    }

                    @Override
                    protected void onSuccess(List<TeamInfo> teamInfos) {
                        mRecyclerView.getListView().refreshComplete(REQUEST_COUNT);

                        if (teamInfos != null && teamInfos.size() > 0) {
                            listArray.addAll(teamInfos);
                            myTeamAdapter.setData(listArray);
                            mRecyclerView.notifyDataSetChanged();
                            //                            myTeamAdapter.notifyItemRangeChanged(listArray.size() - teamList.size(), listArray.size());
                            pageNum = pageNum + 1;
                        } else {
                            mRecyclerView.getListView().setNoMore(true);
                            //                            ViewShowUtils.showLongToast(getActivity(),"已经没有更多数据了");
                        }
                    }

                });

    }

    //    /**
    //     * 获取Tab文字
    //     */


    private void openQrcodeDialog(final TeamInfo info, final FansInfo fansInfo) {  //退出确认弹窗
        if (mQrcodeDialog == null) {
            mQrcodeDialog = new QrcodeDialog(getActivity());
        }
        mQrcodeDialog.show();
        Message message = new Message();
        message.what = 0;
        Bundle bundle = new Bundle();
        bundle.putSerializable("info", info);
        bundle.putSerializable("fansInfo", fansInfo);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    public void stick() {
        MyLog.i("test", "this: " + mType);
        if (mRecyclerView != null && mRecyclerView.getListView() != null) {
            mRecyclerView.getListView().scrollToPosition(0);
        }
    }


    private void openRemarkDialog(String remark, final int position) {  //退出确认弹窗
        if (mRemarkDialog == null) {
            mRemarkDialog = new FansRemarkDialog(getActivity());
            mRemarkDialog.setOnClickListener(new FansRemarkDialog.OnClickListener() {
                @Override
                public void onClick(View v, String remark, int position) {
                    updateRemark(remark, position);

                    mRemarkDialog.dismiss();


                }
            });
        }
        mRemarkDialog.show();
        Message message = new Message();
        message.what = 1;
        Bundle bundle = new Bundle();
        bundle.putString("remark", remark);
        bundle.putInt("position", position);
        message.setData(bundle);
        handler.sendMessage(message);
    }



    private void updateRemark(String remark, int position) {
//        if (TextUtils.isEmpty(remark)) {
//            return;
//        }
        RequestRemarkBean bean = new RequestRemarkBean();
        bean.setRemark(remark);
        bean.setId(listArray.get(position).getId() + "");
        RxHttp.getInstance().getCommonService().updateRemark(bean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(this.<BaseResponse<String>>bindToLifecycle())

                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onSuccess(String data) {
                        loadData();
                    }

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        if (StringsUtils.isDataEmpty(errCode)) {
                            loadData();
                        }

                    }
                });

    }

    public void setOrder(String mOrder) {
        this.mOrder = mOrder;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInit = false;
    }
}
