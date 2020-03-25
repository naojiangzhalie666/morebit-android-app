package com.zjzy.morebit.main.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.Activity.SearchResultActivity;
import com.zjzy.morebit.Activity.ShortVideoPlayActivity;
import com.zjzy.morebit.App;
import com.zjzy.morebit.Module.common.Dialog.ClearSDdataDialog;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.TodayGoodAdapter;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.main.model.ConfigModel;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.SearchHotKeyBean;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.SystemConfigBean;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.ConfigListUtlis;
import com.zjzy.morebit.utils.DensityUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MyGsonUtils;
import com.zjzy.morebit.utils.StringsUtils;
import com.zjzy.morebit.utils.UI.BannerInitiateUtils;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.action.MyAction;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;

/**
 * 搜索分页（子页面）
 * Created by fengrs on 2018/9/10.
 * 备注:
 */

public class SearchFragment extends BaseMainFragmeng implements View.OnClickListener {



    private static final int REQUEST_COUNT = 10;

    @BindView(R.id.todayLayout)
    LinearLayout todayLayout;
    @BindView(R.id.toDayTv)
    TextView toDayTv;
    @BindView(R.id.guideTitleTv)
    TextView guideTitleTv;
    @BindView(R.id.videoPlayIv)
    ImageView videoPlayIv;
    @BindView(R.id.guideIv)
    ImageView guideIv;
    @BindView(R.id.hotSearchLL)
    LinearLayout hotSearchLL;

    @BindView(R.id.moreHistoryTv)
    TextView moreHistoryTv;

    @BindView(R.id.arrowIv)
    ImageView arrowIv;

    private ImageView clearLy;

    private LinearLayout searchHistory;
    private LinearLayout hotKey_list;
    private RelativeLayout historyReLay;

    TodayGoodAdapter todayGoodAdapter;
    private View mView;
    private List<SearchHotKeyBean> mSearchHotKeyDatas;
    boolean isClserSearch = false;
    boolean isShowMoreHistory = false;
    private int mSearchType = 1;
    @BindView(R.id.moreHistoryLL)
    LinearLayout moreHistoryLL;

    private View clearLy_bottom_line;

    private ArrayList<String> mArrayList = new ArrayList<>();


    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_search_platform, container, false);

        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(mView);
        initData();
    }

    private void initView(View view) {

        searchHistory = (LinearLayout) getActivity().findViewById(R.id.searchHistory);
        hotKey_list = (LinearLayout) getActivity().findViewById(R.id.hotKey_list);
        historyReLay = (RelativeLayout)getActivity().findViewById(R.id.historyReLay);
    }

    private void initData() {
        getSearchGuide();
        getHotKeywords();
        getTodayTitle();
        getTodayGood();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clearLy: //清楚历史数据
                openCleanDataDialog();
                break;
            case R.id.moreHistoryLL:
                if(moreHistoryTv.getText().equals("更多搜索历史")){
                    isShowMoreHistory = true;
                    moreHistoryTv.setText("收起搜索历史");
                    arrowIv.setImageResource(R.drawable.icon_arrow_up);
                }else{
                    isShowMoreHistory = false;
                    moreHistoryTv.setText("更多搜索历史");
                    arrowIv.setImageResource(R.drawable.icon_arrow_down);
                }

                addViewToFlowLayout(mArrayList.toArray(new String[mArrayList.size()]), hotKey_list, 40);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    private Dialog textDialog;

    private void openCleanDataDialog() {  //打开清理缓存
        textDialog = new ClearSDdataDialog(getActivity(), R.style.dialog, "提示", "是否清除搜索历史", new ClearSDdataDialog.OnOkListener() {
            @Override
            public void onClick(View dialog, String text) {

                mArrayList.clear();
                hotKey_list.removeAllViews();
                historyReLay.setVisibility(View.GONE);
                App.getACache().put(C.sp.searchHotKey, mArrayList);
                clearLy.setVisibility(View.INVISIBLE);
                clearLy_bottom_line.setVisibility(View.INVISIBLE);
                moreHistoryLL.setVisibility(View.GONE);
                ViewShowUtils.showShortToast(getActivity(), "删除成功");
            }

        });
        textDialog.show();
    }

    /**
     * 获取搜索引导设置
     */
    private void getSearchGuide() {
        //搜索引导设置（写死30），小幺鸡的文档
        ConfigModel.getInstance().getSearchGuide((RxAppCompatActivity) getActivity(),30)
                .subscribe(new DataObserver<List<ImageInfo>>() {

                    @Override
                    protected void onSuccess(List<ImageInfo> data) {
                        setGuideUI(data);
                    }
                });
    }

    /**
     * 获取今日推荐title
     */
    private void getTodayTitle() {
        ConfigListUtlis.getConfigListCacheNet((RxAppCompatActivity)getActivity(), ConfigListUtlis.getConfigAllKey(), new MyAction.One<List<SystemConfigBean>>() {
            @Override
            public void invoke(List<SystemConfigBean> arg) {
                SystemConfigBean todayBean = ConfigListUtlis.getSystemConfigBean(C.ConfigKey.TODAY_RECOMMENDED_TITLE);
                if(null != todayBean && !TextUtils.isEmpty(todayBean.getSysValue())){
                    toDayTv.setText(todayBean.getSysValue());
                }
            }
        });
    }


    /**
     * 获取今日推荐
     */
    private void getTodayGood() {
        ConfigModel.getInstance().getTodayGoods((RxAppCompatActivity) getActivity())
                .subscribe(new DataObserver<List<ShopGoodInfo>>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        super.onError(errorMsg, errCode);
                        todayLayout.setVisibility(View.GONE);
                    }

                    @Override
                    protected void onSuccess(List<ShopGoodInfo> data) {
                        if(null != data && data.size()>0){
                            todayLayout.setVisibility(View.VISIBLE);
                            todayGoodAdapter.setData(data);
                        }
                    }
                });
    }

    private void setGuideUI(List<ImageInfo> data){
        if(null != data && data.size()>0){
            final ImageInfo info  = data.get(0);
            if(!TextUtils.isEmpty(info.getTitle()) && info.getMark() == 1){
                guideTitleTv.setVisibility(View.VISIBLE);
                guideTitleTv.setText(info.getTitle());
            }else{
                guideTitleTv.setVisibility(View.GONE);
            }
            if(info.getMediaType() == 1){
                videoPlayIv.setVisibility(View.VISIBLE);
            }
            guideIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(info.getMediaType() == 1){
                        //视频
                        if(!TextUtils.isEmpty(info.getVideoUrl())){
                            Intent it = new Intent(getActivity(), ShortVideoPlayActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(C.Extras.ITEMVIDEOID, info.getVideoUrl());
                            it.putExtras(bundle);
                            startActivity(it);
                        }
                    }else{
                        //图片
                        if(info.getOpen() != 0){
                            BannerInitiateUtils.gotoAction(getActivity(),info);
                        }
                    }
                }
            });
            if(!TextUtils.isEmpty(info.getPicture())){
                LoadImgUtils.setImg(getActivity(), guideIv, info.getPicture());
            }
        }
    }

    /**
     * 获取搜索发现
     */
    private void getHotKeywords() {
        ConfigModel.getInstance().getSearchKey((RxAppCompatActivity) getActivity(),31)
                .subscribe(new DataObserver<List<SearchHotKeyBean>>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        super.onError(errorMsg, errCode);
                        hotSearchLL.setVisibility(View.GONE);
                    }

                    @Override
                    protected void onSuccess(List<SearchHotKeyBean> data) {
                        hotSearchLL.setVisibility(View.VISIBLE);
                        if(null != data && data.size()>0){
                            mSearchHotKeyDatas = data;
                            List<String> keyLists = new ArrayList<>();
                            for (int i = 0; i < data.size(); i++) {
                                keyLists.add(data.get(i).getKeyWord());
                            }
                            String[] keyArrays = new String[data.size()];
                            keyArrays = keyLists.toArray(keyArrays);
                            addViewToFlowLayout(keyArrays, searchHistory, 40);
                        }
                    }
                });
    }

    private void addSearchText(String text) {


        Iterator<String> arrlistIterator = mArrayList.iterator();
        while(arrlistIterator.hasNext()){
            String str = arrlistIterator.next();
            if(str.equals(text)){
                arrlistIterator.remove();
            }
        }

        mArrayList.add(0, text);
        if(null != mArrayList && mArrayList.size() >0){
            historyReLay.setVisibility(View.VISIBLE);
            addViewToFlowLayout(mArrayList.toArray(new String[mArrayList.size()]), hotKey_list, 40);
        }


        App.getACache().put(C.sp.searchHotKey, mArrayList);
        clearLy.setVisibility(View.VISIBLE);
    }

    private void gotoSearchResultActivity(String getText) {

        Intent intent = new Intent(getActivity(), SearchResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("keyWord", getText);
        bundle.putInt(SearchResultActivity.KEY_SEARCH_TYPE,mSearchType);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * LinearLayout转流式布局
     *
     * @param hotKeys      字符数组
     * @param linearLayout 主布局
     * @param padding      主布局左右间距总和 db为单位
     */
    private void addViewToFlowLayout(String[] hotKeys, final LinearLayout linearLayout, int padding) {
        if (hotKeys == null || hotKeys.length == 0) {
            return;
        }
//        //如果是搜索历史，最多显示8个
//        if(linearLayout.getId() == R.id.hotKey_list && hotKeys.length > 8){
//            hotKeys = Arrays.copyOfRange(hotKeys,  0 ,  8 );
//        }
        //初始化布局
        linearLayout.removeAllViews();
        //行布局内的宽度
        int groupWidth = DensityUtil.getPhoneWidth(getActivity()) - DensityUtil.dip2px(getActivity(), padding);
        int sumWidth = 0;
        LinearLayout getLy = null;
        int line = 0;
        for (int i = 0; i < hotKeys.length; i++) {
            final int pot = i;
            if (TextUtils.isEmpty(hotKeys[i])) return;
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.flowlayout_tv, null);
            TextView tv = (TextView) view.findViewById(R.id.flow_text);
            GradientDrawable background = (GradientDrawable) tv.getBackground();
            ImageView hotTagIv = view.findViewById(R.id.hotTagIv);
            tv.setTextColor(ContextCompat.getColor(getActivity(),R.color.color_333333));
            background.setColor(ContextCompat.getColor(getActivity(),R.color.color_fff6f6f6));
            //字数限制
            if(linearLayout.getId() == R.id.searchHistory){
                //关键字
                SearchHotKeyBean shk = mSearchHotKeyDatas.get(i);
                if(null != shk && shk.getMark() == 1){
                    hotTagIv.setVisibility(View.VISIBLE);
                    if(!TextUtils.isEmpty(shk.getWordColor()) && StringsUtils.checkColor(shk.getWordColor().trim())){
                        tv.setTextColor(Color.parseColor(shk.getWordColor().trim()));
                    }
                    if(!TextUtils.isEmpty(shk.getBackgroundColor()) && StringsUtils.checkColor(shk.getBackgroundColor().trim())){
                        background.setColor(Color.parseColor(shk.getBackgroundColor().trim()));
                    }
                }
                tv.setText(hotKeys[i].length()>6 ?hotKeys[i].substring(0,6)+"...":hotKeys[i]);
            }else if(linearLayout.getId() == R.id.hotKey_list){
                tv.setText(hotKeys[i].length()>24 ?hotKeys[i].substring(0,24)+"...":hotKeys[i]);
            }
            tv.setTag(hotKeys[i]);
            int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            view.measure(w, h);
            int height = view.getMeasuredHeight();
            int width = view.getMeasuredWidth();

            if (sumWidth == 0 || (sumWidth + width) > groupWidth) {
                if(linearLayout.getId() == R.id.hotKey_list && line>2 ){
                    //历史搜索，默认显示3行，最多12行,显示更多按钮
                    if(!isShowMoreHistory){
                        isShowMoreHistory = true;
                        moreHistoryLL.setVisibility(View.VISIBLE);
                        return;
                    }else if(isShowMoreHistory && line > 11){
                        return;
                    }
                }
                LinearLayout ly = new LinearLayout(getActivity());
                ly.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                ly.setOrientation(LinearLayout.HORIZONTAL);
                getLy = ly;
                linearLayout.addView(ly);
                getLy.addView(view);
                sumWidth = width;
                line++;
            } else {
                if (getLy != null) {
                    getLy.addView(view);
                    sumWidth = sumWidth + width;
                }
            }

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView vt = (TextView) view;
                    String getText = (String) vt.getTag();
                    //判断是否有配置跳转，没有执行以下代码
                    //热门搜索
                    if(linearLayout.getId() == R.id.searchHistory){
                        SearchHotKeyBean shk = mSearchHotKeyDatas.get(pot);
                        if(shk.getOpen() != 0){
                            BannerInitiateUtils.gotoAction(getActivity(), MyGsonUtils.toImageInfo(shk));
                        }else{
                            addSearchText(getText.trim());
                            gotoSearchResultActivity(getText.trim());
                        }
                    }else{
                        addSearchText(getText.trim());
                        gotoSearchResultActivity(getText.trim());
                    }


                }
            });
        }

    }

}
