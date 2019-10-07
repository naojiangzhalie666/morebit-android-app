package com.jf.my.Activity;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.tablayout.SlidingTabLayout;
import com.jf.my.LocalData.UserLocalData;
import com.jf.my.Module.common.Fragment.BaseFragment;
import com.jf.my.R;
import com.jf.my.fragment.MyTeamListFragment;
import com.jf.my.info.ui.FansActivity;
import com.jf.my.network.BaseResponse;
import com.jf.my.network.RxHttp;
import com.jf.my.network.RxUtils;
import com.jf.my.network.observer.DataObserver;
import com.jf.my.pojo.HotKeywords;
import com.jf.my.pojo.ImageInfo;
import com.jf.my.pojo.TeamInfo;
import com.jf.my.pojo.event.MyFansEvent;
import com.jf.my.pojo.requestbodybean.RequestInviteCodeBean;
import com.jf.my.pojo.requestbodybean.RequestKeyBean;
import com.jf.my.utils.AppUtil;
import com.jf.my.utils.C;
import com.jf.my.utils.LoadImgUtils;
import com.jf.my.utils.MyLog;
import com.jf.my.utils.UI.BannerInitiateUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Action;


/**
 * 我的团队
 * Created by Administrator on 2017/9/11 0011.
 */

public class FansListFragment extends BaseFragment implements View.OnClickListener {
    public static final String TYPE_A = "A";
    public static final String TYPE_B = "B";
    public static final String TYPE_C = "C";
    private TextView txt_head_title;
    private MyTeamListFragment myTeamListFragment;
    private Bundle bundle;
    @BindView(R.id.nickname)
    TextView mTeamLeaderName;
    @BindView(R.id.team_leader_wxcode)
    TextView mTeamLeaderCode;
    @BindView(R.id.tv_copy)
    TextView copy;
    @BindView(R.id.slidingTabLayout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.userIcon)
    RoundedImageView mTeamLeaderHead;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//    @BindView(R.id.search_et)
//    ClearEditText search_et;
    @BindView(R.id.fans_count)
    TextView fans_count;
    @BindView(R.id.tv_contact)
    TextView tv_contact;
//    @BindView(R.id.search_rl)
//    RelativeLayout search_rl;
    @BindView(R.id.rl_teamLeader)
    RelativeLayout rl_teamLeader;
//    @BindView(R.id.status_bar)
//    View status_bar;
    @BindView(R.id.btn_stick)
    ImageButton btn_stick;
    @BindView(R.id.ll_num)
    LinearLayout ll_num;
    @BindView(R.id.people_num_text)
    TextView people_num_text;
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinator;
    ViewPager viewpager;
    private TeamAdapter mAdapter;
    private ImageInfo mImageInfo;
    private MyFansEvent mEventC;
    private MyFansEvent mEventA;
    private MyFansEvent mEventB;
   List<MyTeamListFragment> listFragments = new ArrayList<>();
    public static FansListFragment newInstance() {
        FansListFragment fragment = new FansListFragment();

        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_fans_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    public void initView(View view) {
        viewpager = (ViewPager) view.findViewById(R.id.viewpager);
        //解决toolbar左边有空白边距问题
//        toolbar.setContentInsetsAbsolute(0, 0);
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float verticalOffsetABs = Math.abs(verticalOffset * 1.0f);
                //用偏移量/最大偏移量得出百分比
                float percent = verticalOffsetABs / appBarLayout.getTotalScrollRange();
                ArgbEvaluator evaluator = new ArgbEvaluator();
                ArgbEvaluator evaluatorSearch = new ArgbEvaluator();
                //滑动渐变
                int colorSearch = (int) evaluatorSearch.evaluate(percent, ContextCompat.getColor(getActivity(), R.color.color_FFC200), ContextCompat.getColor(getActivity(), R.color.color_ECECEC));
                int color = (int) evaluator.evaluate(percent, ContextCompat.getColor(getActivity(), R.color.color_FFD800), ContextCompat.getColor(getActivity(), R.color.white));
//                toolbar.setBackgroundColor(color);
                appbar.setBackgroundColor(color);
                mTabLayout.setBackgroundColor(color);
//                search_rl.setBackgroundColor(colorSearch);
//                status_bar.setBackgroundColor(color);
                ((FansActivity)getActivity()).setTopBg(percent);
                if (verticalOffsetABs >= appBarLayout.getTotalScrollRange()) {
                    mTabLayout.setIndicatorColor(ContextCompat.getColor(getActivity(), R.color.color_FFD800));
                    ((FansActivity)getActivity()).setImmersionBarBg(true);
                    //  ImmersionBar.with(FansListFragment.this).statusBarColor(R.color.white).fitsSystemWindows(true).statusBarDarkFont(true, 0.2f).init();
                } else if (verticalOffset == 0) {
                    ((FansActivity)getActivity()).setImmersionBarBg(false);
                    //ImmersionBar.with(FansListFragment.this).statusBarColor(R.color.color_FFD800).fitsSystemWindows(true).statusBarDarkFont(true, 0.2f).init();
                    mTabLayout.setIndicatorColor(ContextCompat.getColor(getActivity(), R.color.color_333333));
                }

            }
        });
        if (C.UserType.operator.equals(UserLocalData.getUser().getPartner())) {
            rl_teamLeader.setVisibility(View.GONE);
            tv_contact.setVisibility(View.VISIBLE);
        } else {
            rl_teamLeader.setVisibility(View.VISIBLE);
            tv_contact.setVisibility(View.GONE);
        }
        getTabText();
        getTeamLeader();
//        search_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    searchFans();
//                    return true;
//                }
//                return false;
//            }
//        });


        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        showMemberCount(mEventC);
                        break;
                    case 1:
                        showMemberCount(mEventA);
                        break;
                    case 2:
                        showMemberCount(mEventB);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    /**
     * 置顶
     */
    private void stick() {
        appbar.setExpanded(true);
        MyLog.i("test","mAdapter.getItem(viewpager.getCurrentItem()): " + mAdapter.getmCurrentFragment());
        if(mAdapter.getmCurrentFragment()!=null){
            mAdapter.getmCurrentFragment().stick();
        }

    }

//    private void searchFans() {
//        String search_key = search_et.getText().toString().trim();
//        if (TextUtils.isEmpty(search_key)) {
//            ViewShowUtils.showShortToast(getActivity(), "输入粉丝手机号/邀请码");
//            return;
//        }
//        SearchFansActivity.start(getActivity(), search_key);
//    }


    @OnClick({ R.id.tv_contact, R.id.btn_stick,R.id.tv_copy})
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btn_back:
//                finish();
//                break;
//            case R.id.search:
//                searchFans();
//                break;
            case R.id.tv_contact:
                if (mImageInfo == null) {
                    getCustomService();
                } else {
                    BannerInitiateUtils.gotoAction(getActivity(), mImageInfo);
                }

                break;
            case R.id.btn_stick:
                stick();
                break;
            case R.id.tv_copy:
                AppUtil.coayText(getActivity(),   mTeamLeaderCode.getText().toString().trim());
                Toast.makeText(getActivity(), "已复制到粘贴版", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

    }


    private void getTeamLeader() {

        RequestInviteCodeBean requestBean = new RequestInviteCodeBean();
        requestBean.setWxShowType(1);
        RxHttp.getInstance().getCommonService().getServiceQrcode(requestBean)
                .compose(RxUtils.<BaseResponse<TeamInfo>>switchSchedulers())
                .compose(this.<BaseResponse<TeamInfo>>bindToLifecycle())
                .subscribe(new DataObserver<TeamInfo>() {
                    @Override
                    protected void onSuccess(TeamInfo data) {
                        if (data != null) {
                            setTeamLeaderInfo(data);
                        }
                    }
                });
    }

    private void getCustomService() {

        RxHttp.getInstance().getCommonService().getCustomService()
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(this.<BaseResponse<String>>bindToLifecycle())
                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onSuccess(String data) {
                        mImageInfo = new ImageInfo();
                        mImageInfo.setUrl(data);
                        mImageInfo.setOpen(3);
                        BannerInitiateUtils.gotoAction(getActivity(), mImageInfo);
                    }
                });
    }

    private void setTeamLeaderInfo(final TeamInfo data) {

        if (TextUtils.isEmpty(data.getWxNumber())) {
            mTeamLeaderCode.setText("未填写");
            mTeamLeaderCode.setTextColor(ContextCompat.getColor(getActivity(),R.color.color_999999));
            copy.setBackgroundResource(R.drawable.bg_solid_ececec_5dp);
            copy.setTextColor(ContextCompat.getColor(getActivity(), R.color.color_999999));
            copy.setEnabled(false);
        } else {
            mTeamLeaderCode.setText(data.getWxNumber());
            mTeamLeaderCode.setTextColor(ContextCompat.getColor(getActivity(),R.color.color_333333));
            copy.setBackgroundResource(R.drawable.bg_stroke_dadada_5dp);
            copy.setTextColor(ContextCompat.getColor(getActivity(), R.color.color_333333));
            copy.setEnabled(true);
        }

        if (!TextUtils.isEmpty(data.getHeadImg())) {
            LoadImgUtils.setImgCircle(getActivity(), mTeamLeaderHead, data.getHeadImg());
        } else {
            mTeamLeaderHead.setImageResource(R.drawable.head_icon);
        }

        if(TextUtils.isEmpty(data.getNickname())){
            mTeamLeaderName.setText("团队长： 未填写");
        } else {
            mTeamLeaderName.setText("团队长： "+data.getNickname());
        }

    }

    private void setupViewPager(List<String> homeColumns) {

            MyTeamListFragment fragment1 = MyTeamListFragment.newInstance(TYPE_C,MyTeamListFragment.TYPE_NORMAL);
            MyTeamListFragment fragment2 =   MyTeamListFragment.newInstance(TYPE_A,MyTeamListFragment.TYPE_NORMAL);
            MyTeamListFragment fragment3 =   MyTeamListFragment.newInstance(TYPE_B,MyTeamListFragment.TYPE_NORMAL);
            listFragments.add(fragment1);
            listFragments.add(fragment2);
            listFragments.add(fragment3);

        mAdapter = new TeamAdapter(getChildFragmentManager(), homeColumns);
        if (viewpager!=null&&mTabLayout!=null){
            viewpager.setAdapter(mAdapter);
            mTabLayout.setViewPager(viewpager);
        }
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appbar.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                return true;
            }
        });
    }

    private class TeamAdapter extends FragmentStatePagerAdapter {
        private final List<String> homeColumns;
        private  MyTeamListFragment mCurrentFragment;
        public TeamAdapter(FragmentManager fragmentManager, List<String> homeColumns) {
            super(fragmentManager);
            this.homeColumns = homeColumns;
        }

        //订单状态,1.付款; 3.已结算; 4.已失效 ;5.全部
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return listFragments.get(0);
            } else if (position == 1) {;
                return listFragments.get(1);
            } else if (position == 2) {
                return listFragments.get(2);
            }


            return listFragments.get(0);
        }

        @Override
        public int getCount() {
            return homeColumns.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);

        }


        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            mCurrentFragment = (MyTeamListFragment) object;
            super.setPrimaryItem(container, position, object);
        }




        @Override
        public CharSequence getPageTitle(int position) {
            return homeColumns.get(position);
        }

        public MyTeamListFragment getmCurrentFragment() {
            return mCurrentFragment;
        }
    }

    public void getTabText() {

//        LoadingView.showDialog(this, "请求中...");

        RxHttp.getInstance().getCommonService().getConfigForKey(new RequestKeyBean().setKey(C.SysConfig.WEB_FANS_LIST))
                .compose(RxUtils.<BaseResponse<HotKeywords>>switchSchedulers())
                 .compose(this.<BaseResponse<HotKeywords>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                    }
                })
                .subscribe(new DataObserver<HotKeywords>() {
                    @Override
                    protected void onSuccess(HotKeywords data) {
                        MyLog.i("test", "data: " + data.getSysValue());
                        List<String> arrayList = new ArrayList<String>();
                        String[] temp = data.getSysValue().split(",");
                        if (temp != null) {
                            arrayList.clear();
                            for (int i = 0; i < temp.length; i++) {
                                arrayList.add(temp[i]);
                            }
                        }
                        setupViewPager(arrayList);
                    }

                });
    }

    @Subscribe  //订阅事件
    public void onEventMainThread(MyFansEvent event) {
        if (TYPE_A.equals(event.getCurType())) {
            mEventA = event;
        } else if (TYPE_B.equals(event.getCurType())) {
            mEventB = event;
        } else if (TYPE_C.equals(event.getCurType())) {
            mEventC = event;

        }

        if(viewpager.getCurrentItem()==0){
            showMemberCount(mEventC);
        } else if(viewpager.getCurrentItem()==1){
            showMemberCount(mEventA);
        } else if(viewpager.getCurrentItem()==2){
            showMemberCount(mEventB);
        }


        if (event.getAllFansCount() != 0) {
            fans_count.setText(event.getAllFansCount() + "");
        }

    }

    /**
     * 显示会员人数
     *
     * @param data
     */
    private void showMemberCount(MyFansEvent data) {

        if(data==null){
            return;
        }
        if (ll_num.getVisibility() != (View.VISIBLE)) {
            ll_num.setVisibility(View.VISIBLE);
        }
        if (data.getConsumerCount() == 0 && data.getAgentCount() == 0) {
            ll_num.setVisibility(View.GONE);
        } else if (data.getConsumerCount() == 0 & data.getAgentCount() != 0) {
            people_num_text.setText("VIP会员共" + data.getAgentCount() + "位");
        } else if (data.getAgentCount() == 0 & data.getConsumerCount() != 0) {
            people_num_text.setText("会员共" + data.getConsumerCount() + "位");
        } else {
            people_num_text.setText("会员共" + data.getConsumerCount() + "位" + ", vip会员共" + data.getAgentCount() + "位");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void isShowStick(boolean ishow){
        if(btn_stick!=null){
            btn_stick.setVisibility(ishow? View.VISIBLE:View.GONE);
        }
    }
}
