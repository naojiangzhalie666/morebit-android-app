package com.zjzy.morebit.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Dialog.FansRemarkDialog;
import com.zjzy.morebit.Module.common.Dialog.PurchaseRuleDialog;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.MyFansAdapter;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.FansInfo;
import com.zjzy.morebit.pojo.TeamInfo;
import com.zjzy.morebit.pojo.request.RequestRemarkBean;
import com.zjzy.morebit.pojo.request.RequestTeanmListBean;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.StringsUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;

/*
*
* 粉丝龙虎榜
* */
public class FansDragonActivity extends BaseActivity implements View.OnClickListener {
    private TextView txt_head_title;
    private LinearLayout btn_back;
    //总预估佣金
    private LinearLayout title_zong_volume_ll;
    private TextView title_comprehensive_tv;
    private ImageView title_comprehensive_iv;
    //活跃时间
    private LinearLayout title_sales_volume_ll;
    private TextView title_sales_volume_tv;
    private ImageView title_sales_volume_iv;
    //七日拉新
    private LinearLayout title_post_coupon_price__ll;
    private TextView title_post_coupon_price_tv;
    private ImageView title_post_coupon_price_iv;

    private int type=4;//1：最后登录时间 2：近30日预估佣金3：近七日拉新     4：总佣金
    private int page=1;
    private String mOrder="desc";//排序

    private LinearLayout dateNullView;
    private LinearLayout top;


    //排序方向
    private int eSortDirection = C.OrderType.E_UPLIMIT_SORT_DOWN;// 0降序  1升序
    //排序类型
    private long mSortType;//排序类型 0 综合排序 2 销量排序 3 价格排序 4 奖励排序

    private RecyclerView rcy_fans;

    private  MyFansAdapter fansAdapter;
    private SmartRefreshLayout swipeList;
    private List<TeamInfo> mdata=new ArrayList<>();
    private FansRemarkDialog mRemarkDialog;
    private TextView btn_invite;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans_dragon);
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .fitsSystemWindows(true)
                .statusBarColor(R.color.white)
                .init();

        initView();
    }

    private void initView() {
        top= (LinearLayout) findViewById(R.id.top);
        txt_head_title = (TextView) findViewById(R.id.txt_head_title);
        txt_head_title.setText("粉丝龙虎榜");
        txt_head_title.getPaint().setFakeBoldText(true);
        txt_head_title.setTextSize(18);
        btn_back = (LinearLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        swipeList= (SmartRefreshLayout) findViewById(R.id.swipeList);

        title_comprehensive_tv = (TextView) findViewById(R.id.title_comprehensive_tv);//总预估
        title_zong_volume_ll= (LinearLayout) findViewById(R.id.title_zong_volume_ll);
        title_comprehensive_iv= (ImageView) findViewById(R.id.title_comprehensive_iv);


        title_sales_volume_ll = (LinearLayout) findViewById(R.id.title_sales_volume_ll);//活跃
        title_sales_volume_tv = (TextView) findViewById(R.id.title_sales_volume_tv);
        title_sales_volume_iv = (ImageView) findViewById(R.id.title_sales_volume_iv);

        title_post_coupon_price__ll = (LinearLayout) findViewById(R.id.title_post_coupon_price__ll);//七日拉新
        title_post_coupon_price_tv = (TextView) findViewById(R.id.title_post_coupon_price_tv);
        title_post_coupon_price_iv = (ImageView) findViewById(R.id.title_post_coupon_price_iv);



        title_zong_volume_ll.setOnClickListener(this);
        title_sales_volume_ll.setOnClickListener(this);
        title_post_coupon_price__ll.setOnClickListener(this);

        dateNullView= (LinearLayout) findViewById(R.id.dateNullView);
        btn_invite= (TextView) findViewById(R.id.btn_invite);
        rcy_fans= (RecyclerView) findViewById(R.id.rcy_fans);

        LinearLayoutManager linearLayout=new LinearLayoutManager(this);
        rcy_fans.setLayoutManager(linearLayout);
        fansAdapter=new MyFansAdapter(this);
        rcy_fans.setAdapter(fansAdapter);
        //弹出圆圈 样式

        swipeList.setEnableLoadMore(true);
        swipeList.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page=1;
                getData();
            }
        });
        swipeList.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getData();
            }
        });

        getData();
        fansAdapter.setOnAdapterClickListener(new MyFansAdapter.OnAdapterClickListener() {
            @Override
            public void onRemark(int position) {
                openRemarkDialog(mdata.get(position).getRemark(), position);
            }
        });

        btn_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到分享界面
                //   OpenFragmentUtils.goToSimpleFragment(getActivity(), ShareFriendsFragment.class.getName(), new Bundle());
                startActivity(new Intent(FansDragonActivity.this, InvateActivity.class));
            }
        });

    }

    private void openRemarkDialog(String remark, final int position) {  //退出确认弹窗
        if (mRemarkDialog == null) {
            mRemarkDialog = new FansRemarkDialog(this);
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
        bean.setId(mdata.get(position).getId() + "");
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


    private void loadData() {
        swipeList.finishLoadMore();
        getData();
    }
    private void getData() {


        getRankCompose()
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                       // mRecyclerView.getSwipeList().setRefreshing(false);
                    }
                })
                .subscribe(new DataObserver<List<TeamInfo>>() {
                    @Override
                    protected void onDataListEmpty() {
                        if (page==1){
                            dateNullView.setVisibility(View.VISIBLE);
                            rcy_fans.setVisibility(View.GONE);
                        }else{
                            swipeList.finishLoadMore();
                        }

                    }

                    @Override
                    protected void onSuccess(List<TeamInfo> data) {
                        swipeList.finishRefresh();
                        if (data!=null && data.size() > 0){
                            mdata=data;
                            dateNullView.setVisibility(View.GONE);
                            rcy_fans.setVisibility(View.VISIBLE);
                            if (page==1){
                                fansAdapter.setData(data,type);
                            }else{
                                swipeList.finishLoadMore();
                                fansAdapter.addData(data);
                            }
                        }else{
                            dateNullView.setVisibility(View.VISIBLE);
                            rcy_fans.setVisibility(View.GONE);
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
                page=1;
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.title_zong_volume_ll:
                requestClickRadar(null, title_comprehensive_tv, 4);
                showPopupWindow(top);
                title_comprehensive_iv.setImageResource(R.drawable.zong_tubiao);
                break;
            case R.id.title_sales_volume_ll:
                type=1;
                requestClickRadar(title_sales_volume_iv, title_sales_volume_tv, 1);
                getData();
                break;
            case R.id.title_post_coupon_price__ll:
                type=3;
                requestClickRadar(title_post_coupon_price_iv, title_post_coupon_price_tv, 3);
                getData();
                break;
        }
    }

    private void showPopupWindow(View view){
        //加载布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.pop_dragon_zong, null);
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
            mPopupWindow.showAsDropDown(view,0,0, Gravity.BOTTOM);
        }
        //对popupWindow进行显示
        mPopupWindow.update();

        LinearLayout ll = inflate.findViewById(R.id.ll);

        TextView tv_zong = inflate.findViewById(R.id.tv_zong);
        TextView tv_zong2 = inflate.findViewById(R.id.tv_zong2);

        tv_zong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title_comprehensive_tv.setText("总预估佣金");
                type=4;
                eSortDirection=0;                getData();
                mPopupWindow.dismiss();
            }
        });
        tv_zong2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title_comprehensive_tv.setText("30天佣金");
                type=2;
                eSortDirection=0;
                getData();
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
    private void requestClickRadar(ImageView clickIv, TextView textView, int orderType) {
        if (orderType == 0) {
            //综合只有降序
            eSortDirection = C.OrderType.E_UPLIMIT_SORT_DOWN;
        } else {
            if (!textView.isSelected()) {
                eSortDirection = C.OrderType.E_UPLIMIT_SORT_DOWN;
            } else {
                eSortDirection = eSortDirection == C.OrderType.E_UPLIMIT_SORT_DOWN ? C.OrderType.E_UPLIMIT_SORT_UP : C.OrderType.E_UPLIMIT_SORT_DOWN;
            }
        }
        mSortType = orderType;
        resetTitleRankDrawable(clickIv, textView, eSortDirection);

//        mLoadMoreHelper.loadData();
    }

    private void resetTitleRankDrawable(ImageView clickIv, TextView textView, int eSortDir) {
        //对点击的重置图

        title_sales_volume_iv.setImageResource(R.drawable.icon_jiage_no);
        title_post_coupon_price_iv.setImageResource(R.drawable.icon_jiage_no);
        title_comprehensive_iv.setImageResource(R.drawable.zong_tubiao2);
        title_post_coupon_price_tv.setSelected(false);
        title_sales_volume_tv.setSelected(false);
        title_comprehensive_tv.setSelected(false);
        title_comprehensive_tv.setTextColor(Color.parseColor("#333333"));
        title_sales_volume_tv.setTextColor(Color.parseColor("#333333"));
        title_post_coupon_price_tv.setTextColor(Color.parseColor("#333333"));


        //对点击的设置 图片
        if (clickIv != null) {
            clickIv.setImageResource(getDrawableIdBySortDir(eSortDir));
        }
        if (textView != null) {
            textView.setSelected(true);
            textView.setTextColor(Color.parseColor("#F05557"));
        }
    }

    private int getDrawableIdBySortDir(int sortDir) {
        int res = R.drawable.icon_jiage_no;
        switch (sortDir) {
            case 0:
                res = R.drawable.icon_jiage_down;
                break;
            case 1:
                res = R.drawable.icon_jiage_up;
                break;
        }
        return res;
    }
    private Observable<BaseResponse<List<TeamInfo>>> getRankCompose() {
        RequestTeanmListBean requestTeanmListBean = new RequestTeanmListBean();
        requestTeanmListBean.setType(type);
        requestTeanmListBean.setPage(page);
        if (eSortDirection==0){
            mOrder="desc";
        }else{
            mOrder="asc";
        }
        requestTeanmListBean.setOrder(mOrder);
        return RxHttp.getInstance().getCommonService().getTeamRanking(requestTeanmListBean)
                .compose(RxUtils.<BaseResponse<List<TeamInfo>>>switchSchedulers())
                .compose(this.<BaseResponse<List<TeamInfo>>>bindToLifecycle());
    }

}
