package com.zjzy.morebit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.home.fragment.HomeRecommendFragment;
import com.zjzy.morebit.interfaces.GuideNextCallback;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.utils.MyLog;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeOtherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeOtherFragment extends BaseMainFragmeng {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static HomeOtherFragment newInstance(String param1, String param2) {
        HomeOtherFragment fragment = new HomeOtherFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getLoginView();
        return inflater.inflate(R.layout.fragment_home_other, container, false);
    }

    public void cleseRecommendGoodsView() {
//        if (rl_urgency_notifi != null)
//            rl_urgency_notifi.removeAllViews();
//        if (null != mHomeAdapter && fragments.size() > 0) {
//            HomeRecommendFragment homeRecommendFragment = fragments.get(0);
//            homeRecommendFragment.setTopButtonPosition();
//        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        if (!isVisibleToUser) {
//            if (null != mHomeAdapter && fragments.size() > 0) {
//                MyLog.i("test", "setUserVisibleHint");
//                HomeRecommendFragment homeRecommendFragment = fragments.get(0);
//                homeRecommendFragment.setUserVisibleHint(isVisibleToUser);
//            }
//        }
    }

    public void setmGuideNextCallback(GuideNextCallback mGuideNextCallback) {
      //  this.mGuideNextCallback = mGuideNextCallback;
    }
    public void getLoginView() {

    }

    public void selectFirst() {
//        try {
//            if (mViewPager != null) {
//                mViewPager.setCurrentItem(0);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


    public void setSysNotificationData(List<ImageInfo> data) {
//        boolean isLogin = LoginUtil.checkIsLogin(getActivity(), false);
//        if (isLogin) {
//            if (data != null && data.size() != 0) {
//                for (int i = 0; i < data.size(); i++) {
//                    ImageInfo imageInfo = data.get(i);
//                    if (imageInfo.getDisplayPage() == 1 && imageInfo.getMediaType() == 3) {//首页
//                        Integer index = (Integer) App.getACache().getAsObject(C.sp.CLESE_RECOMMEND_GOODS + UserLocalData.getUser().getPhone() + imageInfo.getId());
//                        index = index == null ? 0 : index;
//                        if (index != null && index < 2) {
//                            addRecommendGoodsView(imageInfo, index);
//                        }
//                        return;
//                    }
//                }
//            }
//
//        }
    }
}
