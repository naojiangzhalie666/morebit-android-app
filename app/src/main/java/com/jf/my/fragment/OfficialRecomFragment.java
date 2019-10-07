package com.jf.my.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.jf.my.LocalData.UserLocalData;
import com.jf.my.Module.common.Activity.BaseActivity;
import com.jf.my.Module.common.Dialog.GoodWebShareTextDialog;
import com.jf.my.Module.common.Fragment.BaseFragment;
import com.jf.my.Module.common.View.ReUseListView;
import com.jf.my.R;
import com.jf.my.adapter.OfficialSelectGoodsAdapter;
import com.jf.my.adapter.ShoppingListAdapter;
import com.jf.my.network.BaseResponse;
import com.jf.my.network.RxHttp;
import com.jf.my.network.RxUtils;
import com.jf.my.network.observer.DataObserver;
import com.jf.my.pojo.SelectFlag;
import com.jf.my.pojo.ShopGoodBean;
import com.jf.my.pojo.ShopGoodInfo;
import com.jf.my.pojo.UserInfo;
import com.jf.my.pojo.goods.ShareUrlStringBaen;
import com.jf.my.pojo.request.RequestSearchBean;
import com.jf.my.pojo.requestbodybean.RequestOfficialRecommend;
import com.jf.my.utils.AppUtil;
import com.jf.my.utils.C;
import com.jf.my.utils.GoodsUtil;
import com.jf.my.utils.LoginUtil;
import com.jf.my.utils.OpenFragmentUtils;
import com.jf.my.utils.ShareUtil;
import com.jf.my.utils.TaobaoUtil;
import com.jf.my.utils.ViewShowUtils;
import com.jf.my.utils.action.MyAction;
import com.jf.my.utils.share.ShareMore;
import com.jf.my.view.SharePopwindow;
import com.jf.my.view.ToolbarHelper;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import io.reactivex.Observable;
import io.reactivex.functions.Action;


/**
 * 官方推荐页面
 * Created by Dell on 2016/3/22.
 */
public class OfficialRecomFragment extends BaseFragment {
    private ReUseListView mRecyclerView;
    private ShoppingListAdapter shoppingListAdapter;
    List<ShopGoodInfo> listArray = new ArrayList<>();
    private static final int REQUEST_COUNT = 10;
    private int pageNum = 1;
    private int pageSiz = 12;
    private boolean isRefresh = true;
    private String inType = "小编推荐";
    private TabLayout mTabLayout;
    private String[] mtexts = {"小编推荐", "高佣推荐", "全网发布"};
    private LinearLayout dateNullView, dateNullView_clickbox;
    private TextView dateNullView_tips_tv;
    private EditText search_et;
    private String inGoodType = "monly";
    public static String ShareType = "monly";
    private TextView web_list_share;
    private RecyclerView selectGoodList;
    private OfficialSelectGoodsAdapter osgAdapter;
    private List<ShopGoodInfo> osgDatas = new ArrayList<>();
    private List<ShopGoodInfo> makeOsgDatas = new ArrayList<>();
    private TextView selectGoodCount;
    private List<String> makePosterPath = new ArrayList<>();
    private boolean mIsSeek;
    private UserInfo userInfo;
    private String mPage = "1";
    private String mMinId = "1";
    private String mShareUrl = "";
    private int mSearchType = 1;

    public static void start(Activity activity) {
        Bundle bundle = new Bundle();
        bundle.putString("title", "官方推荐");
        OpenFragmentUtils.goToSimpleFragment(activity, OfficialRecomFragment.class.getName(), bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //设置是否是代理商
        userInfo = UserLocalData.getUser(getActivity());

        initBundle();
        View view = inflater.inflate(R.layout.fragment_official_recom, container, false);
        inview(view);
        mRecyclerView.getSwipeList().post(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.getSwipeList().setRefreshing(true);
                getFirstData();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new ToolbarHelper(this).setToolbarAsUp().setCustomTitle(R.string.official_recom);
    }

    private void initBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
        }
    }


    public void inview(View view) {

        mRecyclerView = (ReUseListView) view.findViewById(R.id.listview);
        shoppingListAdapter = new ShoppingListAdapter(getActivity());
        mRecyclerView.setAdapter(shoppingListAdapter);
        mRecyclerView.getSwipeList().setOnRefreshListener(new com.jf.my.Module.common.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstData();
            }
        });

        mRecyclerView.getListView().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
//                if (mIsSeek && "高佣推荐".equals(inType)) {
//                    getSeekData(C.requestType.loadMore);
//                } else {
                if (!mRecyclerView.getSwipeList().isRefreshing())
                    getMoreData();
//                }
            }
        });
        mTabLayout = (TabLayout) view.findViewById(R.id.tl_tab);

        initTab();
        //数据为空的
        dateNullView = (LinearLayout) view.findViewById(R.id.dateNullView);
        dateNullView_clickbox = (LinearLayout) view.findViewById(R.id.dateNullView_clickbox);
        dateNullView_tips_tv = (TextView) view.findViewById(R.id.dateNullView_tips_tv);
        dateNullView_tips_tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        dateNullView_tips_tv.getPaint().setAntiAlias(true);//抗锯齿
//       dateNullView_clickbox.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View view) {
//               //跳转到分享界面
//               startActivity(new Intent(getActivity(),PartnerShareActivity.class));
//           }
//       });
        search_et = (EditText) view.findViewById(R.id.search_et);
        view.findViewById(R.id.search_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppUtil.isFastClick(1000)) {
                    return;
                }
                mRecyclerView.getSwipeList().post(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.getSwipeList().setRefreshing(true);
                        pageNum = 1;
                        getFirstData();

                    }
                });
            }
        });
        web_list_share = (TextView) view.findViewById(R.id.web_list_share);
        //海报分享
        view.findViewById(R.id.poster_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!LoginUtil.checkIsLogin(getActivity())) {
                    return;
                }
                if (osgDatas == null || osgDatas.size() == 0) {
                    ViewShowUtils.showShortToast(getActivity(), "请先选择商品");
                    return;
                }
//                makeShopListPoster();
                if (AppUtil.isFastClick(100)) return;
                if (TaobaoUtil.isAuth()) {//淘宝授权
                    TaobaoUtil.getAllianceAppKey((BaseActivity) getActivity(),false);
                } else {
                    GoodsUtil.SharePosterList2(getActivity(), osgDatas, null);
                }
            }
        });
        //Web集合页分享
        web_list_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!LoginUtil.checkIsLogin(getActivity())) {
                    return;
                }
                if (osgDatas == null || osgDatas.size() == 0) {
                    ViewShowUtils.showShortToast(getActivity(), "请先选择商品");
                    return;
                }
                if (TaobaoUtil.isAuth()) {//淘宝授权
                    TaobaoUtil.getAllianceAppKey((BaseActivity) getActivity(),false);
                } else {
                    openWebPage();
                }
            }
        });
        //选择商品列表
        selectGoodList = (RecyclerView) view.findViewById(R.id.selectGoodList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        selectGoodList.setLayoutManager(linearLayoutManager);
        osgAdapter = new

                OfficialSelectGoodsAdapter(getActivity());
        selectGoodList.setAdapter(osgAdapter);
        view.findViewById(R.id.selectGoodCount_ly).

                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (selectGoodList.getVisibility() == View.VISIBLE) {
                            selectGoodList.setVisibility(View.GONE);
                            Drawable rightDrawable = getResources().getDrawable(R.drawable.icon_chakanshangpin2);
                            rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
                            selectGoodCount.setCompoundDrawables(null, null, rightDrawable, null);
                        } else {
                            selectGoodList.setVisibility(View.VISIBLE);
                            Drawable rightDrawable = getResources().getDrawable(R.drawable.icon_chakanshangpin);
                            rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
                            selectGoodCount.setCompoundDrawables(null, null, rightDrawable, null);
                        }
                    }
                });
        shoppingListAdapter.setType(C.GoodsListType.officialrecomList);
        shoppingListAdapter.setOnSelectTagListener(new ShoppingListAdapter.OnSelectTagListener()

        {
            @Override
            public void onSelect(int position) {
                List<SelectFlag> getSlDatas = shoppingListAdapter.getSelectFlag();
                List<SelectFlag> newSlDatas = new ArrayList<SelectFlag>();
                newSlDatas.addAll(getSlDatas);
                if (getSlDatas == null || getSlDatas.size() == 0) {
                    return;
                }
                if ("0".equals(getSlDatas.get(position).getCheck())) {
                    if (osgDatas.size() == 9) {
                        ViewShowUtils.showShortToast(getActivity(), "最多只能选择9个商品");
                        return;
                    }
                    getSlDatas.get(position).setCheck("1");
                    int selectx = -1;
                    for (int i = 0; i < osgDatas.size(); i++) {
                        if (osgDatas.get(i).getItemSourceId().equals(listArray.get(position).getItemSourceId()) && osgDatas.get(i).inType.equals(listArray.get(position).inType)) {
                            selectx = i;
                            break;
                        }
                    }
                    if (selectx == -1) {
                        osgDatas.add(listArray.get(position));
                        osgAdapter.setData(osgDatas);
                        osgAdapter.notifyDataSetChanged();
                    }
                } else {
                    getSlDatas.get(position).setCheck("0");
                    int selectx = -1;
                    for (int i = 0; i < osgDatas.size(); i++) {
                        if (osgDatas.get(i).getItemSourceId().equals(listArray.get(position).getItemSourceId()) && osgDatas.get(i).inType.equals(listArray.get(position).inType)) {
                            selectx = i;
                            break;
                        }
                    }
                    if (selectx != -1) {
                        osgDatas.remove(selectx);
                        osgAdapter.setData(osgDatas);
                        osgAdapter.notifyDataSetChanged();
                    }
                }
                shoppingListAdapter.setSelectFlag(newSlDatas);
                mRecyclerView.notifyDataSetChanged();
                selectGoodCount.setText("查看已勾选商品(" + osgDatas.size() + ")");
            }
        });

        //已选择的商品
        osgAdapter.setOnItemClickListener(new OfficialSelectGoodsAdapter.OnRecyclerViewItemClickListener()

        {
            @Override
            public void onItemClick(View view, int position) {
                //处理勾选了的商品
                List<SelectFlag> getSlDatas = shoppingListAdapter.getSelectFlag();
                List<SelectFlag> newSlDatas = new ArrayList<SelectFlag>();
                newSlDatas.addAll(getSlDatas);
                int selectx = -1;
                for (int i = 0; i < listArray.size(); i++) {
                    if (listArray.get(i).getItemSourceId().equals(osgDatas.get(position).getItemSourceId())) {
                        if (listArray.get(i).inType.equals(osgDatas.get(position).inType)) {
                            newSlDatas.get(i).setCheck("0");
                        }
                    }
                }
                shoppingListAdapter.setSelectFlag(newSlDatas);
                mRecyclerView.notifyDataSetChanged();

                osgDatas.remove(position);
                osgAdapter.setData(osgDatas);
                osgAdapter.notifyDataSetChanged();
                selectGoodCount.setText("查看已勾选商品(" + osgDatas.size() + ")");

            }
        });
        selectGoodCount = (TextView) view.findViewById(R.id.selectGoodCount);

    }

    /**
     * 分享生成的多张海报图片
     */
    private void toShareActionPosterList() {
        ArrayList<Uri> imageUris = new ArrayList<>();
        for (int i = 0; i < makePosterPath.size(); i++) {
            Uri uri = Uri.fromFile(new File(makePosterPath.get(i)));
            imageUris.add(uri);
        }
        Intent mulIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        mulIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        mulIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(mulIntent, "多图文件分享"));
    }


    private GoodWebShareTextDialog gwstDialog;

    /**
     * 打开web集合页
     */
    private void openWebPage() {
        String getGoodsIds = "";
        for (int i = 0; i < osgDatas.size(); i++) {
            if (TextUtils.isEmpty(getGoodsIds)) {
                getGoodsIds = osgDatas.get(i).getItemSourceId();
            } else {
                getGoodsIds = getGoodsIds + "," + osgDatas.get(i).getItemSourceId();
            }
        }

        //获取商品id列表
        ShareMore.getShareGoodsUrlObservable((RxAppCompatActivity) getActivity(), getGoodsIds)
                .subscribe(new DataObserver<ShareUrlStringBaen>() {
                    @Override
                    protected void onSuccess(ShareUrlStringBaen data) {
                        String link = data.getLink();
                        if (!TextUtils.isEmpty(link)) {
                            mShareUrl = link;
                            gwstDialog = new GoodWebShareTextDialog(getActivity(), R.style.dialog, "", "", new GoodWebShareTextDialog.OnCloseListener() {
                                @Override
                                public void onClick(Dialog dialog, String title, String content) {
                                    openSharePop(title, content);
                                }
                            });
                            gwstDialog.show();
                        }
                    }
                });

    }

    private SharePopwindow sharePopwindow;

    /**
     * 打开分享窗口
     */
    private void openSharePop(final String title, final String content) {
        sharePopwindow = new SharePopwindow(getActivity(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.weixinFriend: //分享到好友
                        if (!AppUtil.isWeixinAvilible(getActivity())) {
                            ViewShowUtils.showShortToast(getActivity(), "请先安装微信客户端");
                            return;
                        }
                        UserInfo userInfo = UserLocalData.getUser(getActivity());
                        if (userInfo == null) {
                            ViewShowUtils.showShortToast(getActivity(), "user为空");
                            return;
                        }
                        AppUtil.isCheckImgUrlTrue(getActivity(), userInfo.getHeadImg(),
                                new MyAction.OnResult<Integer>() {
                                    @Override
                                    public void invoke(Integer arg) {
                                        shareToWeixinFriend(true, title, content);
                                    }

                                    @Override
                                    public void onError() {
                                        shareToWeixinFriend(false, title, content);
                                    }
                                });

                        sharePopwindow.dismiss();
                        break;
                    case R.id.weixinCircle: //分享到朋友圈
                        if (!AppUtil.isWeixinAvilible(getActivity())) {
                            ViewShowUtils.showShortToast(getActivity(), "请先安装微信客户端");
                            return;
                        }
                        UserInfo userInfo2 = UserLocalData.getUser(getActivity());
                        if (userInfo2 == null) {
                            return;
                        }
                        AppUtil.isCheckImgUrlTrue(getActivity(), userInfo2.getHeadImg(), new MyAction.OnResult<Integer>() {
                                    @Override
                                    public void invoke(Integer arg) {
                                        shareToWeixinCicle(true, title, content);
                                    }

                                    @Override
                                    public void onError() {
                                        shareToWeixinCicle(false, title, content);
                                    }
                                }
                        );
                        sharePopwindow.dismiss();
                        break;
                    case R.id.qqFriend: //分享到QQ
                        if (!AppUtil.isQQClientAvailable(getActivity())) {
                            ViewShowUtils.showShortToast(getActivity(), "请先安装QQ客户端");
                            return;
                        }
                        UserInfo userInfo3 = UserLocalData.getUser(getActivity());
                        if (userInfo3 == null) {
                            return;
                        }
                        String image3 = userInfo3.getHeadImg();
                        if (TextUtils.isEmpty(image3)) {
                            image3 = "applogo";
                        }
                        //获取商品id列表
                        String url3 = getShareUrl(userInfo3);
                        ShareUtil.App.toQQFriend(getActivity(), title, content, image3, url3, null);
                        sharePopwindow.dismiss();
                        break;
                    case R.id.qqRoom: //分享到QQ空间
                        if (!AppUtil.isQQClientAvailable(getActivity())) {
                            ViewShowUtils.showShortToast(getActivity(), "请先安装QQ客户端");
                            return;
                        }
                        UserInfo userInfo4 = UserLocalData.getUser(getActivity());
                        if (userInfo4 == null) {
                            return;
                        }
                        String image4 = userInfo4.getHeadImg();
                        if (TextUtils.isEmpty(image4)) {
                            image4 = "applogo";
                        }
                        //获取商品id列表
                        String url4 = getShareUrl(userInfo4);
                        ShareUtil.App.toQQRoom(getActivity(), title, content, image4, url4, null);
                        sharePopwindow.dismiss();
                        break;
                    case R.id.sinaWeibo: //分享到新浪微博
                        UserInfo userInfo5 = UserLocalData.getUser(getActivity());
                        if (userInfo5 == null) {
                            return;
                        }
                        String image5 = userInfo5.getHeadImg();
                        if (TextUtils.isEmpty(image5)) {
                            image5 = "applogo";
                        }
                        //获取商品id列表
                        String url5 = getShareUrl(userInfo5);
                        ShareUtil.App.toSinaWeibo(getActivity(), title, content, image5, url5, null);
                        sharePopwindow.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });
        sharePopwindow.showAtLocation(web_list_share, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 微信好友
     */
    private void shareToWeixinFriend(boolean isUrlTrue, final String title, final String content) {
        UserInfo userInfo = UserLocalData.getUser(getActivity());
        if (userInfo == null) {
            return;
        }
        String image = userInfo.getHeadImg();
        if (!isUrlTrue) {
            image = "applogo";
        }
        String url = getShareUrl(userInfo);

        ShareUtil.App.toWechatFriend(getActivity(), title, content, image, url, new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                ViewShowUtils.showShortToast(getActivity(), "分享成功");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                ViewShowUtils.showShortToast(getActivity(), "分享失败");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                ViewShowUtils.showShortToast(getActivity(), "分享取消");
            }
        });
    }

    private String getShareUrl(UserInfo userInfo) {
        return mShareUrl;
    }


    /**
     * 微信朋友圈
     *
     * @param isUrlTrue
     */

    private void shareToWeixinCicle(boolean isUrlTrue, final String title, final String content) {
        UserInfo userInfo2 = UserLocalData.getUser(getActivity());
        if (userInfo2 == null) {
            return;
        }
        String image2 = userInfo2.getHeadImg();
        if (!isUrlTrue) {
            image2 = "applogo";
        }
        //获取商品id列表
        String url2 = getShareUrl(userInfo2);
        ShareUtil.App.toWechatMoments(getActivity(), title, content, image2, url2, null);
    }


    /**
     * 初始化TabLayout
     */
    private void initTab() {
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setTabTextColors(getResources().getColor(R.color.color_666666), getResources().getColor(R.color.color_F32F19));
        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.color_F32F19));
        //填充数据
        for (int i = 0; i < mtexts.length; i++) {
            TabLayout.Tab tab = mTabLayout.newTab();
            tab.setText(mtexts[i]);
            tab.setTag(i);
            mTabLayout.addTab(tab);
        }
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            /**
             * 新选
             * @param tab
             */
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int getPos = (int) tab.getTag();
                if (getPos == 0) {
                    inType = "小编推荐";
                }
                if (getPos == 1) {
                    inType = "高佣推荐";
                }
                if (getPos == 2) {
                    inType = "全网发布";
                }
                dateNullView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mRecyclerView.getSwipeList().post(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.getSwipeList().setRefreshing(true);
                        pageNum = 1;
                        getFirstData();
                    }
                });

            }

            /**
             * 当tab从 选择 ->未选择
             * @param tab
             */
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            /**
             * 复选
             * @param tab
             */
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * 第一次获取数据 //获取消费佣金订单明细列表
     */
    String keyWords = "";

    public void getFirstData() {
        ViewShowUtils.hideSoftInput(getActivity(), search_et);
        pageNum = 1;
        mMinId = "1";
        mPage = "1";
        keyWords = search_et.getText().toString().trim();
//        if (!TextUtils.isEmpty(keyWords) && "高佣推荐".equals(inType)) {
////            getSeekFirstData(keyWords);
//            getSeekData(C.requestType.initData);
//            mIsSeek = true;
//        } else {
//            mIsSeek = false;
        getOffcialFirstData();
//        }
    }

    public void getSeekData(final int requestType) {
        keyWords = search_et.getText().toString().trim();
        if (TextUtils.isEmpty(keyWords)) {
            ViewShowUtils.showLongToast(getActivity(), "请输入您要搜索的商品");
            mRecyclerView.getSwipeList().setRefreshing(false);
            return;
        }


        RequestSearchBean requestBean = new RequestSearchBean();
        requestBean.setSort(C.Setting.commission);
        requestBean.setOrder(C.Setting.descParms);
        requestBean.setPage(mPage);
        requestBean.setCoupon("0");
        requestBean.setKeywords(keyWords);
        requestBean.setMinId(mMinId);
        requestBean.setSearchType(mSearchType);

        RxHttp.getInstance().getGoodsService()
//                .getSearchGoodsList("commission", "desc", mPage, "0", keyWords, mMinId)
                .getSearchGoodsList(requestBean)
                .compose(RxUtils.<BaseResponse<ShopGoodBean>>switchSchedulers())
                .compose(this.<BaseResponse<ShopGoodBean>>bindToLifecycle())
//                .map(RxUtils.<ShopGoodBean>handleRESTFulResult())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mRecyclerView.getSwipeList().setRefreshing(false);
                    }
                })
                .subscribe(new DataObserver<ShopGoodBean>() {


                    @Override
                    protected void onSuccess(ShopGoodBean shopGoodBean) {
                        mRecyclerView.getListView().refreshComplete(REQUEST_COUNT);
                        mMinId = shopGoodBean.getMinId();
                        mPage = shopGoodBean.getPage();
                        mSearchType = shopGoodBean.getSearchType();
                        List<ShopGoodInfo> data = shopGoodBean.getData();
                        if (requestType == C.requestType.initData) {
                            if (data != null) {
                                listArray.clear();
                                listArray.addAll(data);
                            }
                            pageNum = pageNum + 1;
                        } else {
                            if (data != null && data.size() > 0) {
                                listArray.addAll(data);
                                pageNum = pageNum + 1;
                                shoppingListAdapter.setData(listArray);
                            } else {
                                mRecyclerView.getListView().setNoMore(true);
                            }
                        }
                        shoppingListAdapter.setData(listArray);
                        //设置是否是代理商
                        UserInfo userInfo = UserLocalData.getUser(getActivity());

                        mRecyclerView.notifyDataSetChanged();
                    }
                });
    }


    /**
     * 获取推荐
     */
    private void getOffcialFirstData() {
        mRecyclerView.getListView().setNoMore(false);
        pageNum = 1;
        getObservable()
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mRecyclerView.getSwipeList().setRefreshing(false);
                    }
                })
                .subscribe(new DataObserver<List<ShopGoodInfo>>() {
                    @Override
                    protected void onDataListEmpty() {
                        listArray.clear();
                        shoppingListAdapter.setData(listArray);
                        mRecyclerView.notifyDataSetChanged();
                    }

                    @Override
                    protected void onSuccess(List<ShopGoodInfo> data) {
                        listArray.clear();
                        listArray.addAll(getList(data));
                        pageNum = pageNum + 1;
                        shoppingListAdapter.setData(listArray);

                        //处理勾选了的商品
                        List<SelectFlag> getSlDatas = shoppingListAdapter.getSelectFlag();
                        List<SelectFlag> newSlDatas = new ArrayList<SelectFlag>();
                        newSlDatas.addAll(getSlDatas);
                        int selectx = -1;
                        for (int i = 0; i < listArray.size(); i++) {
                            for (int j = 0; j < osgDatas.size(); j++) {
                                if (listArray.get(i).getItemSourceId().equals(osgDatas.get(j).getItemSourceId())) {
                                    if (inType.equals(osgDatas.get(j).inType)) {
                                        newSlDatas.get(i).setCheck("1");
                                    }
                                }
                            }
                        }
                        shoppingListAdapter.setSelectFlag(newSlDatas);

                        //设置是否是代理商
                        UserInfo userInfo = UserLocalData.getUser(getActivity());

                        mRecyclerView.notifyDataSetChanged();
                    }
                });

    }

    /**
     * 加载更多数据
     */
    public void getMoreData() {
        getObservable()
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mRecyclerView.getListView().refreshComplete(REQUEST_COUNT);
                    }
                })
                .subscribe(new DataObserver<List<ShopGoodInfo>>() {

                    @Override
                    protected void onDataListEmpty() {
                        mRecyclerView.getListView().setNoMore(true);
                    }

                    @Override
                    protected void onSuccess(List<ShopGoodInfo> data) {

                        listArray.addAll(getList(data));
                        pageNum = pageNum + 1;
                        shoppingListAdapter.setData(listArray);

                        //处理勾选了的商品
                        List<SelectFlag> getSlDatas = shoppingListAdapter.getSelectFlag();
                        List<SelectFlag> newSlDatas = new ArrayList<SelectFlag>();
                        newSlDatas.addAll(getSlDatas);
                        int selectx = -1;
                        for (int i = 0; i < listArray.size(); i++) {
                            for (int j = 0; j < osgDatas.size(); j++) {
                                if (listArray.get(i).getItemSourceId().equals(osgDatas.get(j).getItemSourceId())) {
                                    if (inType.equals(osgDatas.get(j).inType)) {
                                        newSlDatas.get(i).setCheck("1");
                                    }
                                }
                            }
                        }
                        shoppingListAdapter.setSelectFlag(newSlDatas);

                        mRecyclerView.notifyDataSetChanged();
                    }
                })
        ;
    }

    private Observable<BaseResponse<List<ShopGoodInfo>>> getObservable() {
        int type = 1;

        //小编推荐
        if ("小编推荐".equals(inType)) {
            type = 1;
        }

        //高佣推荐
        if ("高佣推荐".equals(inType)) {
            type = 0;
        }

        //全网发布
        if ("全网发布".equals(inType)) {
            type = 2;
        }
        String keywords = "";
        if (search_et != null) {
            keywords = search_et.getText().toString().trim();
        }

        return RxHttp.getInstance().getGoodsService().getOfficalList(new RequestOfficialRecommend().setType(type).setPageNum(pageNum).setKeyword(keywords))
                .compose(RxUtils.<BaseResponse<List<ShopGoodInfo>>>switchSchedulers())
                .compose(this.<BaseResponse<List<ShopGoodInfo>>>bindToLifecycle());

//        return RxHttp.getInstance().getGoodsService().getOfficalList(type, pageNum, keywords)
//                .compose(RxUtils.<BaseResponse<List<ShopGoodInfo>>>switchSchedulers())
//                .compose(this.<BaseResponse<List<ShopGoodInfo>>>bindToLifecycle());

    }

    /**
     * @param arr
     * @return
     */
    private ArrayList getList(List<ShopGoodInfo> arr) {
        List<ShopGoodInfo> list = new ArrayList();
//        HashMap<String, ShopGoodInfo> map = new HashMap<>();
//        Iterator it = arr.iterator();
//        while (it.hasNext()) {
//            ShopGoodInfo obj = (ShopGoodInfo) it.next();
//            obj.inType = this.inType;
//            map.put(obj.getItemSourceId(), obj);
//        }
        for (ShopGoodInfo v : arr) {
            v.inType = this.inType;
            list.add(v);
        }
        return (ArrayList) list;

    }

}
