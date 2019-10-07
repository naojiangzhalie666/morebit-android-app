package com.jf.my.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jf.my.App;
import com.jf.my.Module.common.Dialog.ClearSDdataDialog;
import com.jf.my.R;
import com.jf.my.pojo.SearchHotKeyBean;
import com.jf.my.utils.StringsUtils;
import com.jf.my.utils.ViewShowUtils;
import com.jf.my.view.flowlayout.FlowLayout;
import com.jf.my.view.flowlayout.TagAdapter;
import com.jf.my.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 - @Description:  蜜粉圈搜索，商学院搜索
 - @Author:  wuchaowen
 - @Time:  2019/8/29 17:17
 **/
public class SearchViewLayout extends LinearLayout implements View.OnClickListener{
    private  LinearLayout hotSearchLL;
    private TagFlowLayout hotkeyFlowLayout;
    private List<SearchHotKeyBean> mSearchHotKeyDatas;
    private ArrayList<String> mHistoryData = new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private RelativeLayout historyReLay;
    private TagFlowLayout historyFlowLayout;
    private ImageView iv_back;
    private TextView searchTv;
    private EditText search_et;
    private String mCacheKey = null;
    private ImageView clearLy;
    private Dialog textDialog;
    private LinearLayout moreHistoryLL;
    private String search_hint_text = "";
    private OnClickHotKeyListener onClickHotKeyListener;
    private OnClickHistoryListener onClickHistoryListener;
    private OnClickSearchListener onClickSearchListener;
    private TextView moreHistoryTv;
    private  ImageView arrowIv;
    private int mSearchBtnColor = getResources().getColor(R.color.color_FFE033);
    boolean isShowMoreHistory = false;
    public SearchViewLayout(Context context) {
        super(context);
        this.mContext = context;
        initView(context);
    }

    public SearchViewLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initTypedArray(mContext, attrs);
        initView(context);
    }

    public SearchViewLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initTypedArray(mContext, attrs);
        initView(context);
    }

    private void initTypedArray(Context context, AttributeSet attrs) {
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.SearchViewLayout);
        search_hint_text = mTypedArray.getString(R.styleable.SearchViewLayout_s_search_hint_text);
        //获取资源后要及时回收
        mTypedArray.recycle();
    }

    public void initView(Context context){
        mLayoutInflater =  LayoutInflater.from(context);
        mLayoutInflater.inflate(R.layout.view_search_flow, this, true);
        hotSearchLL  = findViewById(R.id.hotSearchLL);
        hotkeyFlowLayout = findViewById(R.id.search_hot_key);
        historyReLay = findViewById(R.id.historyReLay);
        historyFlowLayout = findViewById(R.id.search_history_key);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        searchTv = findViewById(R.id.search);
        searchTv.setOnClickListener(this);
        search_et = findViewById(R.id.search_et);
        clearLy = findViewById(R.id.clearLy);
        clearLy.setOnClickListener(this);
        moreHistoryLL = findViewById(R.id.moreHistoryLL);
        moreHistoryLL.setOnClickListener(this);
        moreHistoryTv = findViewById(R.id.moreHistoryTv);
        arrowIv = findViewById(R.id.arrowIv);
        if(!TextUtils.isEmpty(search_hint_text)){
            search_et.setHint(search_hint_text);
        }

        hotkeyFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
        {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent)
            {
                if(null != onClickHotKeyListener){
                    onClickHotKeyListener.onClick(position,mSearchHotKeyDatas.get(position));
                }
                return true;
            }
        });

        historyFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
        {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent)
            {
                if(null != onClickHistoryListener){
                    onClickHistoryListener.onClick(position,mHistoryData.get(position));
                }
                return true;
            }
        });


    }

    /**
     *  设置热门搜索数据
     */
    public void setHotKeyData(List<SearchHotKeyBean> data){
        if(null != data && data.size()>0){
            hotSearchLL.setVisibility(View.VISIBLE);
            this.mSearchHotKeyDatas = data;
            hotkeyFlowLayout.setAdapter(new TagAdapter<SearchHotKeyBean>(data)
            {

                @Override
                public View getView(FlowLayout parent, int position, SearchHotKeyBean text)
                {
                    View  flowView =   mLayoutInflater.inflate(R.layout.flowlayout_tv,
                            hotkeyFlowLayout, false);
                    TextView tv =  flowView.findViewById(R.id.flow_text);
                    ImageView hotTagIv = flowView.findViewById(R.id.hotTagIv);
                    tv.setTextColor(ContextCompat.getColor(mContext,R.color.color_333333));
                    GradientDrawable background = (GradientDrawable) tv.getBackground();
                    background.setColor(ContextCompat.getColor(mContext,R.color.color_fff6f6f6));
                    String keyword = text.getKeyWord();
                    if(!TextUtils.isEmpty(keyword)){
                        tv.setText(keyword.length()>6 ?keyword.substring(0,6)+"...":keyword);
                    }
                    if(null != text && text.getMark() == 1 || text.getSign() == 1){
                        hotTagIv.setVisibility(View.VISIBLE);
                        if(!TextUtils.isEmpty(text.getWordColor()) && StringsUtils.checkColor(text.getWordColor().trim())){
                            tv.setTextColor(Color.parseColor(text.getWordColor().trim()));
                        }
                        if(!TextUtils.isEmpty(text.getBackgroundColor()) && StringsUtils.checkColor(text.getBackgroundColor().trim())){
                            background.setColor(Color.parseColor(text.getBackgroundColor().trim()));
                        }
                    }
                    return flowView;
                }
            });

        }else{
            hotSearchLL.setVisibility(View.GONE);
        }
    }

    /**
     * 设置搜索历史数据
     */
    public void setHistoryData(ArrayList<String> historyData){
        if(null != historyData && historyData.size()>0){
            historyReLay.setVisibility(View.VISIBLE);
            mHistoryData = historyData;
            historyFlowLayout.setAdapter(new TagAdapter<String>(historyData)
            {

                @Override
                public View getView(FlowLayout parent, int position, String text)
                {
                    View  flowView =   mLayoutInflater.inflate(R.layout.flowlayout_tv,
                            historyFlowLayout, false);
                    TextView tv =  flowView.findViewById(R.id.flow_text);
                    tv.setText(text.length()>24 ?text.substring(0,24)+"...":text);

                    return flowView;
                }
            });

            historyFlowLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if(historyFlowLayout.isMore()){
                        if(!isShowMoreHistory){
                            isShowMoreHistory = true;
                            App.mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    moreHistoryLL.setVisibility(View.VISIBLE);
                                }
                            },200);

                        }
                    }else{
                            isShowMoreHistory = false;
                        moreHistoryLL.setVisibility(View.GONE);
                    }
                    historyFlowLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });

        }else{
            historyReLay.setVisibility(View.GONE);
        }
    }

    /**
     * 设置缓存数据的key
     */
    public void setCacheKey(String key){
        mCacheKey = key;
        //加载缓存的历史数据
        ArrayList<String> asObject = (ArrayList<String>) App.getACache().getAsObject(mCacheKey);
        if (asObject != null) {
            mHistoryData.addAll(asObject);
        }
        if(null != mHistoryData && mHistoryData.size() >0){
           setHistoryData(mHistoryData);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                ((Activity) mContext).finish();
                break;
            case R.id.search:
                if (TextUtils.isEmpty(search_et.getText().toString().trim())) {
                    ViewShowUtils.showShortToast(mContext,"请输入搜索内容");
                    search_et.requestFocus();
                    return;
                } else {
                    if(null != onClickSearchListener){
                        onClickSearchListener.onClick(search_et.getText().toString());
                    }
                    addSearchText(search_et.getText().toString());
                }
                break;
            case R.id.clearLy: //清楚历史数据
                openCleanDataDialog();
                break;
            case R.id.moreHistoryLL:
                if(moreHistoryTv.getText().equals("更多搜索历史")){
                    isShowMoreHistory = true;
                    moreHistoryTv.setText("收起搜索历史");
                    arrowIv.setImageResource(R.drawable.icon_arrow_up);
                    historyFlowLayout.setMaxLine(12);
                }else{
                    isShowMoreHistory = false;
                    moreHistoryTv.setText("更多搜索历史");
                    arrowIv.setImageResource(R.drawable.icon_arrow_down);
                    historyFlowLayout.setMaxLine(3);
                }
                historyFlowLayout.onChanged();
                break;
        }
    }



    private void addSearchText(String text) {
        Iterator<String> arrlistIterator = mHistoryData.iterator();
        while(arrlistIterator.hasNext()){
            String str = arrlistIterator.next();
            if(str.equals(text)){
                arrlistIterator.remove();
            }
        }

        mHistoryData.add(0, text);
        if(null != mHistoryData && mHistoryData.size() >0){
            historyReLay.setVisibility(View.VISIBLE);
            setHistoryData(mHistoryData);
        }


        if(!TextUtils.isEmpty(mCacheKey)){
            App.getACache().put(mCacheKey, mHistoryData);
        }

        clearLy.setVisibility(View.VISIBLE);
    }

    private void openCleanDataDialog() {  //打开清理缓存
        textDialog = new ClearSDdataDialog(mContext, R.style.dialog, "提示", "是否清除搜索历史", new ClearSDdataDialog.OnOkListener() {
            @Override
            public void onClick(View dialog, String text) {
                mHistoryData.clear();
                historyFlowLayout.removeAllViews();
                historyReLay.setVisibility(View.GONE);
                if(!TextUtils.isEmpty(mCacheKey)){
                    App.getACache().put(mCacheKey, mHistoryData);
                }
                historyFlowLayout.setMaxLine(3);
                clearLy.setVisibility(View.INVISIBLE);
                moreHistoryLL.setVisibility(View.GONE);
                ViewShowUtils.showShortToast(mContext, "删除成功");
            }

        });
        textDialog.show();
    }

    public void setOnClickHotKeyListener(OnClickHotKeyListener onClickHotKeyListener) {
        this.onClickHotKeyListener = onClickHotKeyListener;
    }

    public void setOnClickHistoryListener(OnClickHistoryListener onClickHistoryListener) {
        this.onClickHistoryListener = onClickHistoryListener;
    }

    public void setOnClickSearchListener(OnClickSearchListener onClickSearchListener) {
        this.onClickSearchListener = onClickSearchListener;
    }

    /**
     * 热门搜索回调
     */
    public interface OnClickHotKeyListener{
        void onClick(int position,SearchHotKeyBean item);
    }

    /**
     * 历史搜索回调
     */
    public interface OnClickHistoryListener{
        void onClick(int position,String item);
    }

    /**
     * 搜索回调
     */
    public interface OnClickSearchListener{
        void onClick(String searchText);
    }
}
