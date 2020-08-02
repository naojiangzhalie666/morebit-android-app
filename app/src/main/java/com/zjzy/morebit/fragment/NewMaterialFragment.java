package com.zjzy.morebit.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.flyco.tablayout.SlidingTabLayout;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.zjzy.morebit.Activity.CircleSearchActivity;
import com.zjzy.morebit.Module.common.Fragment.BaseFragment;
import com.zjzy.morebit.Module.common.View.ReUseListView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.GoodDialyTitleAdapter;
import com.zjzy.morebit.adapter.GoodsDialyAdapter;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.home.fragment.IconFragment;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.CategoryListChildDtos;
import com.zjzy.morebit.pojo.CategoryListDtos;
import com.zjzy.morebit.pojo.MarkermallCircleInfo;
import com.zjzy.morebit.pojo.request.RequestMarkermallCircleBean;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.MyLog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * 拉新素材
 */
public class NewMaterialFragment extends BaseMainFragmeng {

    private RecyclerView rcy_title;

    private List<CategoryListChildDtos> mchild;
    private ImageView img_below;
    private View view2;
    private int oneLevelId;

    private GoodsDialyAdapter goodsDialyAdapter;
    private SlidingTabLayout tab;
   private ViewPager viewPager;
   private List<GoodDailyFragment> mFragments=new ArrayList<>();
   private ImageView search_circle;
   private int type;
   private int circletype;


    public static NewMaterialFragment newInstance(List<CategoryListChildDtos> child, int id,int type,int circletype) {
        NewMaterialFragment fragment = new NewMaterialFragment();
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
        View view = inflater.inflate(R.layout.fragment_new_material, container, false);

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

    }




    private void showError(String errCode, String errorMsg) {

    }


    private void initView(View view) {


        Bundle arguments = getArguments();
        if (arguments != null) {
            mchild= (List<CategoryListChildDtos>) arguments.getSerializable(C.Circle.CIRCLE_TITLE);
            oneLevelId=arguments.getInt(C.Circle.CIRCLE_ONEID);
            type=arguments.getInt(C.Circle.CIRCLE_TYPE);
            circletype=arguments.getInt(C.Circle.CIRCLEFRAGMENT);
            Log.e("sfsdfsdf",oneLevelId+"");
        }



        tab=view.findViewById(R.id.tab);

        viewPager=view.findViewById(R.id.viewPager);
         search_circle = view.findViewById(R.id.search_circle);

        for (int i=0;i<mchild.size();i++){
            GoodDailyFragment fragment = null;
            fragment = new GoodDailyFragment();
            mFragments.add(fragment.newInstance(mchild.get(i).getChild(),oneLevelId,mchild.get(i).getId(),type));
        }
        viewPager.setAdapter(new ChannelAdapter(getChildFragmentManager()));
        tab.setViewPager(viewPager);

        search_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CircleSearchActivity.class);
                intent.putExtra(C.Circle.CIRCLEFRAGMENT,circletype+"");
                startActivity(intent);
            }
        });


    }
    private class ChannelAdapter extends FragmentPagerAdapter {


        public ChannelAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mchild == null ? "" + position : mchild.get(position).getTitle();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            MyLog.i("test", "this: " + this);
        }
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





}
