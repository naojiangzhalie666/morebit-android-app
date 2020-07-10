package com.zjzy.morebit.home.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.Activity.ShowWebActivity;
import com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.LimitSkillAdapter;
import com.zjzy.morebit.adapter.SelectGoodsAdapter;
import com.zjzy.morebit.adapter.SimpleAdapter;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.goodsvideo.contract.VideoContract;
import com.zjzy.morebit.goodsvideo.presenter.VideoPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserZeroInfoBean;
import com.zjzy.morebit.pojo.VideoClassBean;
import com.zjzy.morebit.pojo.goods.NewRecommendBean;
import com.zjzy.morebit.pojo.request.RequestRecommendBean;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.StringsUtils;
import com.zjzy.morebit.utils.TimeUtil;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * 精选商品
 */
public class SelectGoodsFragment extends BaseMainFragmeng {

    private RecyclerView rcy_goods;

    private List<ShopGoodInfo>  list=new ArrayList<>();
    private LinearLayout searchNullTips_ly;
    private int mMinNum=0;
    private int page = 1;
    private int mType=0;
    private  SelectGoodsAdapter selectGoodsAdapter;
    private  List<ShopGoodInfo> goods;
    private NestedScrollView netscrollview;
    private long days;
    private long hours;
    private long minutes;
    private long seconds;
    private  boolean isRun = true;
    private TextView daysTv,hoursTv,minutesTv,secondsTv,tv_title,tv_icon;
    private LinearLayout new_goods;
    private ImageView img;
    private Handler timeHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1) {
                computeTime();
                daysTv.setText("剩余"+days+"天");
                hoursTv.setText(hours+":");
                minutesTv.setText(minutes+":");
                secondsTv.setText(seconds+"");
//                if (mDay==0&&mHour==0&&mMin==0&&mSecond==0) {
//                    countDown.setVisibility(View.GONE);
//                }
            }
        }
    };
    private void computeTime() {
        seconds--;
        if (seconds < 0) {
            minutes--;
            seconds = 59;
            if (minutes < 0) {
                minutes = 59;
                hours--;
                if (hours < 0) {
                    // 倒计时结束
                    hours = 23;
                    days--;
                }
            }
        }


    }





    public static SelectGoodsFragment newInstance() {
        SelectGoodsFragment fragment = new SelectGoodsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selectgoods, container, false);
        getData();
        getTime();
        initView(view);
        return view;
    }

    private void getTime() {

        getUserZeroInfo(this)
                .subscribe(new DataObserver<UserZeroInfoBean>(false) {
                    @Override
                    protected void onDataListEmpty() {
                        onActivityFailure();
                    }

                    @Override
                    protected void onDataNull() {
                        onActivityFailure();
                    }

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        onActivityFailure();
                    }

                    @Override
                    protected void onSuccess(final UserZeroInfoBean data) {

                        initTime(Long.parseLong(data.getTime()));
                        if (data.isIsNewUser()){
                            new_goods.setVisibility(View.VISIBLE);
                        }else{
                            new_goods.setVisibility(View.GONE);
                        }
                        List<UserZeroInfoBean.ItemListBean> itemList = data.getItemList();

                        if (itemList.size()!=0){
                            Paint paint = new Paint();
                            paint.setTextSize(tv_icon.getTextSize());
                            float size = paint.measureText(tv_icon.getText().toString());
                            StringsUtils.retractTitles(tv_title, itemList.get(0).getTitle(), (int) (size)+20);
                            LoadImgUtils.loadingCornerTop2(getActivity(),img,itemList.get(0).getItemPicture(), 8);
                        }
                        new_goods.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ShowWebActivity.start(getActivity(), data.getLinkUrl(),"");

                            }
                        });
                    }
                });
    }

    private void onActivityFailure() {
    }


    private void getData() {
        getNewRecommend(this, page, mMinNum, mType)
                .subscribe(new DataObserver<NewRecommendBean>(false) {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        onRecommendFailure(errorMsg, errCode);
                    }


                    @Override
                    protected void onSuccess(NewRecommendBean data) {
                       onRecommendSuccessful(data);
                    }
                });
    }

    private void onRecommendSuccessful(NewRecommendBean recommendBean) {
        goods = recommendBean.getItemList();
        if (page==1){
            selectGoodsAdapter=new SelectGoodsAdapter(getActivity(),goods);
            rcy_goods.setAdapter(selectGoodsAdapter);


        }else{
                selectGoodsAdapter.setData(goods);
        }
        mMinNum = recommendBean.getMinNum();
        mType = recommendBean.getType();

    }

    private void onRecommendFailure(String errorMsg, String errCode) {

    }


    private void initView(View view) {


        Bundle arguments = getArguments();
        if (arguments != null) {
        }
        secondsTv=view.findViewById(R.id.secondsTv);
        minutesTv=view.findViewById(R.id.minutesTv);
        hoursTv=view.findViewById(R.id.hoursTv);
        daysTv=view.findViewById(R.id.daysTv);
        rcy_goods = view.findViewById(R.id.rcy_goods);
        netscrollview=view.findViewById(R.id.netscrollview);
        new_goods=view.findViewById(R.id.new_goods);
        tv_icon=view.findViewById(R.id.tv_icon);
        tv_title=view.findViewById(R.id.tv_title);
        img=view.findViewById(R.id.img);
        rcy_goods.setNestedScrollingEnabled(false);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rcy_goods.setLayoutManager(manager);
        netscrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                //判断是否滑到的底部
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    page++;
                    getData();//调用刷新控件对应的加载更多方法
                }
            }
        });
        rcy_goods.getItemAnimator().setAddDuration(0);
        rcy_goods.getItemAnimator().setChangeDuration(0);
        rcy_goods.getItemAnimator().setMoveDuration(0);
        rcy_goods.getItemAnimator().setRemoveDuration(0);
        ((SimpleItemAnimator) rcy_goods.getItemAnimator()).setSupportsChangeAnimations(false);

        View header = LayoutInflater.from(getActivity()).inflate(R.layout.item_selectgoods_head, null, false);
        TextView tv_icon = header.findViewById(R.id.tv_icon);
        TextView tv_title = header.findViewById(R.id.tv_title);
        StringsUtils.retractTitle(tv_icon,tv_title, "特仑苏牛奶大减价了 快来买啊实践活动放松放松房间里刷卡缴费卡洛斯");


    }

    private void initTime(long mss) {
        days = mss / (1000 * 60 * 60 * 24);
        hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        seconds = (mss % (1000 * 60)) / 1000;
        startRun();
    }
    /**
     * 开启倒计时
     */
    private void startRun() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (isRun) {
                    try {
                        Thread.sleep(1000); // sleep 1000ms
                        Message message = Message.obtain();
                        message.what = 1;
                        timeHandler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public Observable<BaseResponse<NewRecommendBean>> getNewRecommend(RxFragment fragment, int page, int minNum, int type) {
        RequestRecommendBean requestBean = new RequestRecommendBean();
        requestBean.setPage(page);
        requestBean.setMinNum(minNum);
        requestBean.setType(type);

        return RxHttp.getInstance().getGoodsService()
                .getNewRecommend(requestBean)
                .compose(RxUtils.<BaseResponse<NewRecommendBean>>switchSchedulers())
                .compose(fragment.<BaseResponse<NewRecommendBean>>bindToLifecycle());
    }

    //0元购信息
    public Observable<BaseResponse<UserZeroInfoBean>> getUserZeroInfo(RxFragment fragment) {
        return RxHttp.getInstance().getSysteService().getUserZeroInfo()
                .compose(RxUtils.<BaseResponse<UserZeroInfoBean>>switchSchedulers())
                .compose(fragment.<BaseResponse<UserZeroInfoBean>>bindToLifecycle());
    }

}
