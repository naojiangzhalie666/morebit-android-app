package com.zjzy.morebit.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.zjzy.morebit.Module.common.Fragment.BaseFragment;
import com.zjzy.morebit.Module.common.View.ReUseListView;
import com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.MarkermallCircleAdapter;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.CategoryListChildDtos;
import com.zjzy.morebit.pojo.CategoryListDtos;
import com.zjzy.morebit.pojo.CircleBrand;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.MarkermallCircleInfo;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.event.CirleUpdataShareCountEvent;
import com.zjzy.morebit.pojo.event.LogoutEvent;
import com.zjzy.morebit.pojo.event.RefreshCircleEvent;
import com.zjzy.morebit.pojo.request.RequestCircleBransBean;
import com.zjzy.morebit.pojo.request.RequestCircleCollectBean;
import com.zjzy.morebit.pojo.request.RequestMarkermallCircleBean;
import com.zjzy.morebit.pojo.request.RequestRemoveCircleCollectBean;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.GlideImageLoader;
import com.zjzy.morebit.utils.LogUtils;
import com.zjzy.morebit.utils.MyGsonUtils;
import com.zjzy.morebit.utils.UI.BannerInitiateUtils;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.view.AspectRatioView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;


/**
 * 每日爆款
 */
public class CircleDayHotFragment extends BaseFragment {
    public final static int TypeImg = 2;//（ ，2：图文，3:文字商品)
    public final static int TypeCommodity = 3;//（ 2：图文，3:文字商品)
    public final static int TypeThisMaterial = 4;//个人素材
    public final static int TypeCommodityImg = 5;//商品文字分享
    public final static int TYPE_SHARE_VIDEO = 6;//视频分享
    public final static int TYPE_SEARCH = 7;//搜索
    public final static int TYPE_MY_COLLECTS = 8;//我的收藏
    ReUseListView mRecyclerView;
    LinearLayout dateNullView;
    AspectRatioView bannerRatioView;
    CardView bannerCardView;
    Banner banner;
    List<MarkermallCircleInfo> listArray = new ArrayList<>();
    private static final int REQUEST_COUNT = 10;
    private int pageNum = 1;
    private MarkermallCircleAdapter mAdapter;

    public int mLoadType = 3;
    public int mTwoLevelId; // 二级D, 如果有二级列表就是二级id,如果没有就是一级id
    public int mOneLevelId; // 一级D, 如果有二级列表就是二级id,如果没有就是一级id
    private View mView;
    private CategoryListChildDtos mChilds;
    private CategoryListDtos mCategoryDtos;
    private String mMainTitle;
    private String mSubTitle;
    private String mSearchName;
    private boolean refershData = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_circle_day_hot, container, false);
            initView(mView);
        }
        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }
        return mView;

    }

    //二级分类
    public static CircleDayHotFragment newInstance(CategoryListChildDtos child, int oneLevelId, String mainTitle) {
        Bundle args = new Bundle();
        args.putSerializable("mChild", child);
        args.putInt("oneLevelId", oneLevelId);
        args.putString("mainTitle", mainTitle);
        CircleDayHotFragment fragment = new CircleDayHotFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //一级分类
    public static CircleDayHotFragment newInstance(CategoryListDtos categoryDtos, String mainTitle) {
        Bundle args = new Bundle();
        args.putSerializable("categoryDtos", categoryDtos);
        args.putSerializable("mainTitle", mainTitle);
        CircleDayHotFragment fragment = new CircleDayHotFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //收藏
    public static CircleDayHotFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt(C.sp.CIRCLE_LOAD_DATA_TYPE, type);
        CircleDayHotFragment fragment = new CircleDayHotFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //搜索
    public static CircleDayHotFragment newInstance(Bundle args) {
        CircleDayHotFragment fragment = new CircleDayHotFragment();
        if (args != null) {
            args.putInt(C.sp.CIRCLE_LOAD_DATA_TYPE, CircleDayHotFragment.TYPE_SEARCH);
            fragment.setArguments(args);
        }
        return fragment;
    }


    public void initView(View view) {
        mRecyclerView = view.findViewById(R.id.listview);
        mRecyclerView.getListView().setFocusableInTouchMode(false);
        mRecyclerView.getListView().requestFocus();
        dateNullView = view.findViewById(R.id.dateNullView);
        Bundle bundle = getArguments();
        if (bundle == null) {
            mLoadType = TypeThisMaterial;
        } else {
            try {
                int loadDataType = bundle.getInt(C.sp.CIRCLE_LOAD_DATA_TYPE);
                if (loadDataType != TYPE_SEARCH && loadDataType != TYPE_MY_COLLECTS && loadDataType != TypeThisMaterial) {
                    mChilds = (CategoryListChildDtos) bundle.getSerializable("mChild");
                    mMainTitle = bundle.getString("mainTitle");
                    if (mChilds == null) {
                        mCategoryDtos = (CategoryListDtos) bundle.getSerializable("categoryDtos");
                        if (mCategoryDtos == null) {
                            mLoadType = TypeThisMaterial;
                        } else {
                            mOneLevelId = mCategoryDtos.getId();
                            mSubTitle = mCategoryDtos.getTitle();
                            mLoadType = Integer.valueOf(mCategoryDtos.getType());
                        }
                    } else {
                        mOneLevelId = bundle.getInt("oneLevelId");
                        mTwoLevelId = mChilds.getId();
                        mSubTitle = mChilds.getTitle();
                        mLoadType = Integer.valueOf(mChilds.getType());
                    }
                } else {
                    mLoadType = loadDataType;
                    mSearchName = bundle.getString(C.sp.CIRCLE_SEARCH_NAME);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (mRecyclerView.getSwipeList() != null) {
            getFirstData();
        }
        mAdapter = new MarkermallCircleAdapter(getActivity(), mLoadType);
        mAdapter.setTitle(mMainTitle, mSubTitle);
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.home_circle_head, null);
        bannerRatioView = headView.findViewById(R.id.ar_title_banner);
        bannerCardView = headView.findViewById(R.id.bannerCardView);
        banner = headView.findViewById(R.id.roll_view_pager);
        if (mChilds == null || mChilds.getOpens() == null || mChilds.getOpens().size() == 0 ) {
            //没有广告banner
            setBannerVisiable(false);
        } else {
            setBrandBanner(getActivity(), mChilds.getOpens(), banner, null, mMainTitle, mSubTitle);
        }

        mRecyclerView.setAdapterAndHeadView(headView, mAdapter);
        mAdapter.setmCollectAction(new MyAction.Two<MarkermallCircleInfo, Integer>() {
            @Override
            public void invoke(MarkermallCircleInfo item, Integer position) {
                if (1 == item.getIsCollection() || !TextUtils.isEmpty(item.getCollectionId())) {
                    //取消收藏
                    removeCollect(item, position,false);
                } else {
                    //收藏
                    addCollect(item, position,false);
                }
            }
        });
        mAdapter.setMbrandCollectAction(new MyAction.Two<MarkermallCircleInfo, Integer>() {
            @Override
            public void invoke(MarkermallCircleInfo item, Integer position) {
                MarkermallCircleInfo childInfo =  item.getCircleBrands().get(position);
                if (1 == childInfo.getIsCollection() || !TextUtils.isEmpty(childInfo.getCollectionId())) {
                    //取消收藏
                    removeCollect(item, position,true);

                } else {
                    //收藏
                    addCollect(item, position,true);
                }
            }
        });

        if (mRecyclerView.getSwipeList() != null) {
            mRecyclerView.getSwipeList().setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getFirstData();
                }
            });
        }
        mRecyclerView.getListView().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getMoreData();
            }
        });

    }

    private void setBannerVisiable(boolean show) {
        banner.setVisibility(show == true ? View.VISIBLE : View.GONE);
        bannerCardView.setVisibility(show == true ? View.VISIBLE : View.GONE);
        bannerRatioView.setVisibility(show == true ? View.VISIBLE : View.GONE);
    }


    /**
     * 第一次获取数据 //获取消费佣金明细列表
     */

    public void getFirstData() {
        if (mRecyclerView == null) return;
        pageNum = 1;
        mRecyclerView.getListView().setNoMore(false);

        Observable<BaseResponse<List<MarkermallCircleInfo>>> compose = getObservable();
        compose.doFinally(new Action() {
            @Override
            public void run() throws Exception {
                mRecyclerView.getSwipeList().setRefreshing(false);
            }
        })
                .subscribe(new DataObserver<List<MarkermallCircleInfo>>(false) {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        clearDate();
                    }

                    @Override
                    public void onSuccess(List<MarkermallCircleInfo> agentDetailList) {
                        if (agentDetailList != null && agentDetailList.size() > 0) {
                            dateNullView.setVisibility(View.GONE);
                            listArray.clear();
                            listArray.addAll(agentDetailList);
                            mAdapter.setData(agentDetailList);
                            if (mRecyclerView.getlRecyclerViewAdapter() != null) {
                                mRecyclerView.getlRecyclerViewAdapter().notifyDataSetChanged();
                            } else {
                                mAdapter.notifyDataSetChanged();
                            }
                            pageNum = pageNum + 1;
                            if (mLoadType != TYPE_SEARCH && mLoadType != TYPE_MY_COLLECTS
                                    && mLoadType != TypeThisMaterial && mLoadType != TypeCommodity) {
                                getGuiessList();
                            }

                        } else {
                            clearDate();
                        }
                    }

                    private void clearDate() {
                        mAdapter.clearData();
                        if (mRecyclerView.getlRecyclerViewAdapter() != null) {
                            mRecyclerView.getlRecyclerViewAdapter().notifyDataSetChanged();
                        } else {
                            mAdapter.notifyDataSetChanged();
                        }
                        dateNullView.setVisibility(View.VISIBLE);
                    }
                });
    }

    /**
     * 加载更多数据
     */
    public void getMoreData() {
        Observable<BaseResponse<List<MarkermallCircleInfo>>> compose = getObservable();
        compose.subscribe(new DataObserver<List<MarkermallCircleInfo>>() {
            @Override
            protected void onDataListEmpty() {
                mRecyclerView.getListView().setNoMore(true);
            }

            @Override
            public void onSuccess(List<MarkermallCircleInfo> agentDetailList) {
                mRecyclerView.getListView().refreshComplete(REQUEST_COUNT);
                if (agentDetailList != null && agentDetailList.size() > 0) {
                    listArray.addAll(agentDetailList);
                    mAdapter.setData(listArray);
                    if (mRecyclerView.getlRecyclerViewAdapter() != null) {
                        mRecyclerView.getlRecyclerViewAdapter().notifyDataSetChanged();
                    } else {
                        mAdapter.notifyDataSetChanged();
                    }
                    pageNum = pageNum + 1;
                } else {
                    mRecyclerView.getListView().setNoMore(true);
                }
            }
        });
    }


    private Observable<BaseResponse<List<MarkermallCircleInfo>>> getObservable() {
        RequestMarkermallCircleBean requestBean = new RequestMarkermallCircleBean();
        requestBean.setPage(pageNum);
        if (mLoadType == TypeThisMaterial) {
            return RxHttp.getInstance().getSysteService()
                    .getMaterial(requestBean)
                    .compose(RxUtils.<BaseResponse<List<MarkermallCircleInfo>>>switchSchedulers())
                    .compose(this.<BaseResponse<List<MarkermallCircleInfo>>>bindToLifecycle());
        } else if (mLoadType == TYPE_SEARCH) {
            RequestCircleBransBean requestCircleBean = new RequestCircleBransBean();
            requestCircleBean.setName(mSearchName);
            requestCircleBean.setPage(pageNum);
            return RxHttp.getInstance().getSysteService()
                    .getCollectSearchList(requestCircleBean)
                    .compose(RxUtils.<BaseResponse<List<MarkermallCircleInfo>>>switchSchedulers())
                    .compose(this.<BaseResponse<List<MarkermallCircleInfo>>>bindToLifecycle());
        } else if (mLoadType == TYPE_MY_COLLECTS) {
            RequestCircleBransBean requestCircleBean = new RequestCircleBransBean();
            requestCircleBean.setType("1");
            requestCircleBean.setPage(pageNum);
            return RxHttp.getInstance().getSysteService()
                    .getCollectList(requestCircleBean)
                    .compose(RxUtils.<BaseResponse<List<MarkermallCircleInfo>>>switchSchedulers())
                    .compose(this.<BaseResponse<List<MarkermallCircleInfo>>>bindToLifecycle());
        } else {
            requestBean.setOneLevelId(mOneLevelId + "");
            requestBean.setType(mLoadType);
            if (mTwoLevelId != 0) {
                requestBean.setTwoLevelId(mTwoLevelId + "");
            }
            return RxHttp.getInstance().getSysteService()
                    .getMarkermallCircle(requestBean)
                    .compose(RxUtils.<BaseResponse<List<MarkermallCircleInfo>>>switchSchedulers())
                    .compose(this.<BaseResponse<List<MarkermallCircleInfo>>>bindToLifecycle());
        }


    }

    /**
     *
     * @param item
     * @param itemPosition
     * @param isChild  是否收藏为你推荐的文章
     */
    private void addCollect(final MarkermallCircleInfo item, final int itemPosition, final boolean isChild) {
        int requestId = 0;
        if(!isChild){
            requestId = item.getId();
        }else{
            requestId = item.getCircleBrands().get(itemPosition).getId();
        }
        if (null != item) {
            Observable<BaseResponse<String>> compose = getCollectObservable(requestId + "");
            compose.subscribe(new DataObserver<String>() {
                @Override
                protected void onDataNull() {
                    onSuccess("");
                }


                @Override
                protected void onSuccess(String data) {
                    //更新收藏按钮
                    if (null != mAdapter) {
                        //这里整个刷新
                        if(!isChild){
                            item.setIsCollection(1);
                            if(!TextUtils.isEmpty(data)){
                                item.setCollectionId(data);
                            }
                            mAdapter.updateItem(item, itemPosition);
                        }else{
                            if(null != item.getCircleBrands() && item.getCircleBrands().size()>0){
                                List<MarkermallCircleInfo> list = item.getCircleBrands();
                                MarkermallCircleInfo info =  list.get(itemPosition);
                                info.setIsCollection(1);
                                if(!TextUtils.isEmpty(data)){
                                    info.setCollectionId(data);
                                }
                                list.set(itemPosition,info);
                                item.setCircleBrands(list);
                                mAdapter.updateBrandItem(item, itemPosition);
                            }

                        }

                    }


                }
            });
        }

    }


    private void removeCollect(final MarkermallCircleInfo item, final int itemPosition, final boolean isChild) {
        String collectId = "";
        String rangId = "";
        if(!isChild){
            collectId = item.getCollectionId();
            rangId = item.getId()+"";
        }else{
            collectId = item.getCircleBrands().get(itemPosition).getCollectionId();
            rangId = item.getCircleBrands().get(itemPosition).getId()+"";
        }

        if (null != item) {
            Observable<BaseResponse<String>> compose = getRemoveCollectObservable(collectId,rangId);
            compose.subscribe(new DataObserver<String>() {

                @Override
                protected void onDataNull() {
                    onSuccess("");
                }


                @Override
                protected void onSuccess(String data) {
                    //更新收藏按钮
                    if (null != mAdapter) {
                        if(!isChild){
                            item.setIsCollection(0);
                            item.setCollectionId(null);
                            if ( mLoadType == TYPE_MY_COLLECTS){
                                mAdapter.removeItem(item, itemPosition);
                                EventBus.getDefault().post(new RefreshCircleEvent(item));
                            }else{
                                mAdapter.updateItem(item, itemPosition);
                            }
                        }else{
                            if(null != item.getCircleBrands() && item.getCircleBrands().size()>0){
                                List<MarkermallCircleInfo> list = item.getCircleBrands();
                                MarkermallCircleInfo info =  list.get(itemPosition);
                                info.setIsCollection(0);
                                info.setCollectionId(null);
                                list.set(itemPosition,info);
                                item.setCircleBrands(list);
                                mAdapter.updateBrandItem(item, itemPosition);
                                //mAdapter.updateItem(item, 1);
                            }
                        }


                    }

                }
            });
        }

    }

    private Observable<BaseResponse<String>> getCollectObservable(String postId) {
        RequestCircleCollectBean requestBean = new RequestCircleCollectBean();
        requestBean.setRangId(postId);
        return RxHttp.getInstance().getSysteService()
                .addShareRangCollection(requestBean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(this.<BaseResponse<String>>bindToLifecycle())
                ;
    }

    private Observable<BaseResponse<String>> getRemoveCollectObservable(String postId,String rangId) {
        RequestRemoveCircleCollectBean requestBean = new RequestRemoveCircleCollectBean();
        requestBean.setId(postId);
        requestBean.setRangId(rangId);
        return RxHttp.getInstance().getSysteService()
                .removeShareRangCollection(requestBean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(this.<BaseResponse<String>>bindToLifecycle());
    }


    private void getGuiessList() {
        Observable<BaseResponse<List<CircleBrand>>> compose = getBransObservable();
        compose.subscribe(new DataObserver<List<CircleBrand>>() {
            @Override
            protected void onSuccess(List<CircleBrand> datas) {
                if (null != datas && datas.size() > 0) {
                    MarkermallCircleInfo markermallCircleInfo = new MarkermallCircleInfo();
                    markermallCircleInfo.setShowCircleBrand(true);
                    markermallCircleInfo.setCircleBrands(MyGsonUtils.toMarkermallCircleInfo(datas));
//                    if(null != listArray && listArray.size() >1){
//
//                       List<MarkermallCircleInfo> showDatas =  listArray.get(1).getCircleBrands();
//                        if(null != showDatas && showDatas.size() > 0){
//                            mAdapter.removeItem(listArray.get(1),1);
//                            listArray.remove(1);
//
//                        }
//                    }
                    listArray.add(1, markermallCircleInfo);
                    mAdapter.insertItem(markermallCircleInfo, 1);
                }

            }
        });
    }

    private Observable<BaseResponse<List<CircleBrand>>> getBransObservable() {
        RequestCircleBransBean requestBean = new RequestCircleBransBean();
        requestBean.setType("0"); //0：用户可选 1：发圈上架
        return RxHttp.getInstance().getSysteService()
                .getCircleGuessList(requestBean)
                .compose(RxUtils.<BaseResponse<List<CircleBrand>>>switchSchedulers())
                .compose(this.<BaseResponse<List<CircleBrand>>>bindToLifecycle())
                ;
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.Log("CircleDayHotFrgment", "onStart");
    }

    @Subscribe  //订阅事件
    public void onEventMainThread(CirleUpdataShareCountEvent event) {
        if (mAdapter != null)
            mAdapter.updataShareText();
    }


    @Subscribe  //订阅事件
    public void onEventMainThread(MessageEvent event) {
        switch (event.getAction()) {
            case EventBusAction.LOGINA_SUCCEED:
                if (mAdapter != null) {
                    getFirstData();
                    if (mRecyclerView.getlRecyclerViewAdapter() != null) {
                        mRecyclerView.getlRecyclerViewAdapter().notifyDataSetChanged();
                    } else {
                        mAdapter.notifyDataSetChanged();
                    }
                }

                break;

        }
    }


    @Subscribe  //订阅事件
    public void onEventMainThread(LogoutEvent event) {
        if (mAdapter != null) {
            getFirstData();
            if (mRecyclerView.getlRecyclerViewAdapter() != null) {
                mRecyclerView.getlRecyclerViewAdapter().notifyDataSetChanged();
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }

    }

    @Subscribe  //订阅事件
    public void onEventMainThread(RefreshCircleEvent event) {
        if(null != event.getItem()){
           refreshList(event.getItem());
        }
    }


    private void setBrandBanner(final Activity activity, final List<ImageInfo> data, Banner banner, AspectRatioView aspectRatioView, final String level, final String level2) {
        final int bannerId = banner.getId();
        if (data == null || data.size() == 0) {
            return;
        }
        if (aspectRatioView != null) {
            ImageInfo imageInfo = data.get(0);
            float width = imageInfo.getWidth();
            float height = imageInfo.getHeight();
            if (width != 0 && width / height != 0) {
                aspectRatioView.setAspectRatio(width / height);
            }
        }
        List<String> imgUrls = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            imgUrls.add(data.get(i).getThumb());
        }

        //简单使用
        banner.setImages(imgUrls)
                .setIndicatorGravity(BannerConfig.CENTER)
                .setImageLoader(new GlideImageLoader())
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        ImageInfo imageInfo = data.get(position);
//                        SensorsDataUtil.getInstance().advClickTrack(imageInfo.getId()+"",level,level2,imageInfo.getOpen()+"","发圈",position,imageInfo.getTitle(),imageInfo.getClassId()+"",imageInfo.getUrl(),"");
                        BannerInitiateUtils.gotoAction(activity, imageInfo);
                    }
                })
                .isAutoPlay(true)
                .setDelayTime(4000)
                .start();
    }

    public void setSearchName(String searchName) {
        mSearchName = searchName;
    }


    /**
     * 局部刷新列表
     */
    private void refreshList(MarkermallCircleInfo updateInfo){
        boolean isfind = false;
        int  childPos = 0;
        if (mAdapter != null) {
            if(null != mAdapter && mAdapter.getDatas().size()> 1){
                for (int i = 0; i < 2 ; i++) {
                    MarkermallCircleInfo eachItem =  mAdapter.getDatas().get(i);
                    List<MarkermallCircleInfo> childItems =  eachItem.getCircleBrands();
                    if(null != childItems && childItems.size()>0){
                        for (int j = 0; j < childItems.size(); j++) {
                            MarkermallCircleInfo eachChild =  childItems.get(j);
                            if(eachChild.getId() == updateInfo.getId()){
                                isfind = true;
                                childPos = j;
                                childItems.set(j,updateInfo);
                                mAdapter.updateBrandItem(eachItem, childPos);
                            }
                        }
                    }
                }
            }
            if(!isfind){
                mAdapter.updateItem(updateInfo, 0);
            }

        }
    }

}
