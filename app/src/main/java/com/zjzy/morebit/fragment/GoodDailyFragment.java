package com.zjzy.morebit.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.SpaceItemDecoration;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.Module.common.View.ReUseListView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.GoodDialyTitleAdapter;
import com.zjzy.morebit.adapter.GoodDialyTitleAdapter2;
import com.zjzy.morebit.adapter.GoodsDialyAdapter;
import com.zjzy.morebit.adapter.SelectGoodsAdapter;
import com.zjzy.morebit.adapter.SubNumberAdapter;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.CategoryListChildDtos;
import com.zjzy.morebit.pojo.CategoryListDtos;
import com.zjzy.morebit.pojo.MarkermallCircleInfo;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.number.NumberGoods;
import com.zjzy.morebit.pojo.number.NumberGoodsList;
import com.zjzy.morebit.pojo.request.RequestMarkermallCircleBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestNumberGoodsList;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.DensityUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * 每日好货
 */
public class GoodDailyFragment extends BaseMainFragmeng {

    private RecyclerView rcy_title;

    private List<CategoryListChildDtos> mchild;
    private ImageView img_below;
    private View view2;
    private int oneLevelId;
    private int twoLevelId;

    private ReUseListView mListView;
    private int page=1;
    private LinearLayout dateNullView;
    private GoodsDialyAdapter goodsDialyAdapter;
    private int type;
    private int  circletype;
    private  int stype;
    private RelativeLayout rl_title;



    public static GoodDailyFragment newInstance(List<CategoryListChildDtos> child, int id,int twoid,int type,int circletype,int threeid) {
        GoodDailyFragment fragment = new GoodDailyFragment();
        Bundle args = new Bundle();
        args.putSerializable(C.Circle.CIRCLE_TITLE, (Serializable) child);
        args.putInt(C.Circle.CIRCLE_ONEID, id);
        args.putInt(C.Circle.CIRCLE_TWOID, twoid);
        args.putInt(C.Circle.CIRCLE_THREEID, threeid);
        args.putInt(C.Circle.CIRCLE_TYPE,type);
        args.putInt(C.Circle.CIRCLEFRAGMENT,circletype);
        fragment.setArguments(args);
        return fragment;
    }
    public static GoodDailyFragment newInstance(List<CategoryListChildDtos> child, int id,int type,int circletype ) {
        GoodDailyFragment fragment = new GoodDailyFragment();
        Bundle args = new Bundle();
        args.putSerializable(C.Circle.CIRCLE_TITLE, (Serializable) child);
        args.putInt(C.Circle.CIRCLE_ONEID, id);
        args.putInt(C.Circle.CIRCLE_TYPE,type);
        args.putInt(C.Circle.CIRCLEFRAGMENT,circletype);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goodsdaily, container, false);

        getTime();
        initView(view);
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }
        getData();
        return view;
    }

    private void getTime() {


    }


    private void getData() {
        getMarkermallCircle()
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {


                    }
                })
                .subscribe(new DataObserver<List<MarkermallCircleInfo>>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
//                        super.onError(errorMsg, errCode);
                        showError(errCode, errorMsg);
                    }

                    @Override
                    protected void onDataListEmpty() {
                        if (page==1){
                            mListView.setVisibility(View.GONE);
                            dateNullView.setVisibility(View.VISIBLE);
                        }
                        mListView.getListView().setNoMore(true);
                    }

                    @Override
                    protected void onSuccess(List<MarkermallCircleInfo> data) {
                        showSuccessful(data);
                    }
                });
    }

    private void showSuccessful(List<MarkermallCircleInfo> data) {
        if (data!=null&&data.size()!=0){
            dateNullView.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            if (page==1){
                goodsDialyAdapter.setData(data);
            }else{
                goodsDialyAdapter.addData(data);
                mListView.getListView().setNoMore(true);
            }

        }else{
            dateNullView.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
            mListView.getListView().setNoMore(true);
        }
    }


    private void showError(String errCode, String errorMsg) {

    }


    private void initView(View view) {


        Bundle arguments = getArguments();
        if (arguments != null) {
            mchild= (List<CategoryListChildDtos>) arguments.getSerializable(C.Circle.CIRCLE_TITLE);
            oneLevelId=arguments.getInt(C.Circle.CIRCLE_ONEID);
            twoLevelId=arguments.getInt(C.Circle.CIRCLE_TWOID);
            type=arguments.getInt(C.Circle.CIRCLE_TYPE);
            circletype=arguments.getInt(C.Circle.CIRCLEFRAGMENT);
            stype=arguments.getInt(C.Circle.CIRCLE_TWO);
            Log.e("sfsdfsdf",twoLevelId+"");
        }
        rl_title=view.findViewById(R.id.rl_title);
        rcy_title=view.findViewById(R.id.rcy_title);
        Log.e("mbbmbmbm",mchild+"");
        if (mchild!=null){
            rl_title.setVisibility(View.VISIBLE);
            GoodDialyTitleAdapter titleAdapter=new GoodDialyTitleAdapter(getActivity(),mchild,oneLevelId,circletype,type);
            LinearLayoutManager gridLayoutManager=new LinearLayoutManager(getActivity());
            gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            rcy_title.setLayoutManager(gridLayoutManager);
            rcy_title.setAdapter(titleAdapter);
        }else{
            rl_title.setVisibility(View.GONE);
        }


        img_below=view.findViewById(R.id.img_below);
        view2=view.findViewById(R.id.view);
        img_below.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(view2);
            }
        });

        mListView=view.findViewById(R.id.listview);
        dateNullView=view.findViewById(R.id.dateNullView);
        goodsDialyAdapter=new GoodsDialyAdapter(getActivity(),circletype);
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        mListView.setLayoutManager(manager);
        mListView.setAdapter(goodsDialyAdapter);

        mListView.getSwipeList().setOnRefreshListener(new com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mListView.getSwipeList().post(new Runnable() {

                    @Override
                    public void run() {
                        mListView.getSwipeList().setRefreshing(true);
                    }
                });
                page = 1;
                getData();
            }
        });
        mListView.getListView().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!mListView.getSwipeList().isRefreshing()) {
                    page++;
                    getData();
                }

            }
        });



    }


//    @Subscribe  //订阅事件
//    public void onEventMainThread(MessageEvent event) {
//        if (event.getAction().equals(EventBusAction.ACTION_REFRSH)) {
//            getData();
//
//        }
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // EventBus.getDefault().unregister(this);
    }


    private void showPopupWindow(View view){
        //加载布局
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.pop_dialy_title, null);
        //更改背景颜色
//        inflate.setBackgroundColor(getContext().getResources().getColor(R.color.white));
        final PopupWindow mPopupWindow = new PopupWindow(inflate);
        //设置SelectPicPopupWindow弹出窗体的宽
        mPopupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        mPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        //点击其他地方隐藏,false为无反应
        mPopupWindow.setFocusable(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //对他进行便宜
            mPopupWindow.showAsDropDown(view,0,0, Gravity.TOP);
        }
        //对popupWindow进行显示
        mPopupWindow.update();

        RecyclerView rcy = inflate.findViewById(R.id.rcy);
        ImageView img_up = inflate.findViewById(R.id.img_up);
        LinearLayout ll=inflate.findViewById(R.id.ll);

        GoodDialyTitleAdapter2 titleAdapter=new GoodDialyTitleAdapter2(getActivity(),mchild,oneLevelId,circletype,type);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),4);
        rcy.addItemDecoration(new SpaceItemDecoration(DensityUtil.dip2px(getActivity(), 6)));
        rcy.setLayoutManager(gridLayoutManager);
        rcy.setAdapter(titleAdapter);


        img_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });

    }



    //获取发圈数据
    public Observable<BaseResponse<List<MarkermallCircleInfo>>> getMarkermallCircle() {
        RequestMarkermallCircleBean requestBean = new RequestMarkermallCircleBean();
        requestBean.setType(type);
        requestBean.setPage(page);
        requestBean.setOneLevelId(oneLevelId+"");
        if (circletype==0){
            if (twoLevelId!=0){
                requestBean.setTwoLevelId(twoLevelId+"");
            }
        }else{
            if (type!=0){
                requestBean.setTwoLevelId(type+"");
            }
        }


        return RxHttp.getInstance().getGoodsService().getMarkermallCircle(requestBean)
                .compose(RxUtils.<BaseResponse<List<MarkermallCircleInfo>>>switchSchedulers())
                .compose(this.<BaseResponse<List<MarkermallCircleInfo>>>bindToLifecycle());
    }

}
