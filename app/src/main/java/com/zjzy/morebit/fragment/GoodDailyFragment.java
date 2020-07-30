package com.zjzy.morebit.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.GoodDialyTitleAdapter;
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
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.number.NumberGoods;
import com.zjzy.morebit.pojo.number.NumberGoodsList;
import com.zjzy.morebit.pojo.requestbodybean.RequestNumberGoodsList;
import com.zjzy.morebit.utils.C;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
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



    public static GoodDailyFragment newInstance(List<CategoryListChildDtos> child, int id) {
        GoodDailyFragment fragment = new GoodDailyFragment();
        Bundle args = new Bundle();
        args.putSerializable(C.Circle.CIRCLE_TITLE, (Serializable) child);
        args.putInt(C.Circle.CIRCLE_ONEID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goodsdaily, container, false);
        getData();
        getTime();
        initView(view);
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }
        return view;
    }

    private void getTime() {


    }


    private void getData() {

    }



    private void showError(String errCode, String errorMsg) {

    }


    private void initView(View view) {


        Bundle arguments = getArguments();
        if (arguments != null) {
            mchild= (List<CategoryListChildDtos>) arguments.getSerializable(C.Circle.CIRCLE_TITLE);
            oneLevelId=arguments.getInt(C.Circle.CIRCLE_ONEID);
        }

        rcy_title=view.findViewById(R.id.rcy_title);
        GoodDialyTitleAdapter titleAdapter=new GoodDialyTitleAdapter(getActivity(),mchild,oneLevelId);
        LinearLayoutManager gridLayoutManager=new LinearLayoutManager(getActivity());
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcy_title.setLayoutManager(gridLayoutManager);
        rcy_title.setAdapter(titleAdapter);
        img_below=view.findViewById(R.id.img_below);
        view2=view.findViewById(R.id.view);
        img_below.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(view2);
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

        GoodDialyTitleAdapter titleAdapter=new GoodDialyTitleAdapter(getActivity(),mchild,oneLevelId);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),4);
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

}
