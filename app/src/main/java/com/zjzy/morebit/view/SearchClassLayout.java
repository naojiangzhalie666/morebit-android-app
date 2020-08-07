package com.zjzy.morebit.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.android.flexbox.AlignItems;
import com.zjzy.morebit.App;
import com.zjzy.morebit.Module.common.Dialog.ClearSDdataDialog;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.SearchHistoryAdapter;
import com.zjzy.morebit.pojo.SearchHotKeyBean;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.SharePreferenceUtil;
import com.zjzy.morebit.utils.SharedPreferencesUtils;
import com.zjzy.morebit.utils.StringsUtils;
import com.zjzy.morebit.utils.UI.DeviceUtil;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.view.flowlayout.FlowLayout;
import com.zjzy.morebit.view.flowlayout.TagAdapter;
import com.zjzy.morebit.view.flowlayout.TagFlowLayout;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxItemDecoration;
import com.google.android.flexbox.FlexboxLayoutManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



/**
 - @Description:  发圈搜索，商学院搜索
 - @Author:  wuchaowen
 - @Time:  2019/8/29 17:17
 **/
public class SearchClassLayout extends LinearLayout implements View.OnClickListener{
    private  LinearLayout hotSearchLL;
    private TagFlowLayout hotkeyFlowLayout;
    private List<SearchHotKeyBean> mSearchHotKeyDatas;
    private ArrayList<String> mHistoryData = new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    private ImageView iv_back;
    private TextView searchTv;
    private EditText search_et;
    private String mCacheKey = null;
    private ImageView clearLy;
    private Dialog textDialog;
    private RelativeLayout historyReLay;
    private LinearLayout hotKey_list;
    private String search_hint_text = "";
    private OnClickHotKeyListener onClickHotKeyListener;
    private OnClickHistoryListener onClickHistoryListener;
    private OnClickSearchListener onClickSearchListener;


    public SearchClassLayout(Context context) {
        super(context);
        this.mContext = context;
        initView(context);
    }

    public SearchClassLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initTypedArray(mContext, attrs);
        initView(context);
    }

    public SearchClassLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        mLayoutInflater.inflate(R.layout.view_search_flow2, this, true);
        hotSearchLL  = findViewById(R.id.hotSearchLL);
        hotkeyFlowLayout = findViewById(R.id.search_hot_key);
        hotKey_list=findViewById(R.id.hotKey_list);
      //  historyReLay=findViewById(R.id.historyReLay);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        searchTv = findViewById(R.id.search);
        searchTv.setOnClickListener(this);
        search_et = findViewById(R.id.search_et);
//        clearLy = findViewById(R.id.clearLy);
//        clearLy.setOnClickListener(this);

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

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                ((Activity) mContext).finish();
                break;
            case R.id.search:
                Log.e("sdfsfsf"," 4"+search_et.getText().toString());
                if (TextUtils.isEmpty(search_et.getText().toString().trim())) {
                    ViewShowUtils.showShortToast(mContext,"请输入搜索内容");
                    Log.e("sdfsfsf","7");
                    search_et.requestFocus();
                    return;
                } else {
                    ViewShowUtils.showShortToast(mContext,"请输入搜索内容"+search_et.getText().toString());
                    if(null != onClickSearchListener){
                        onClickSearchListener.onClick(search_et.getText().toString());
                    }
                    Log.e("sdfsfsf","进来了4");
                    addSearchText(search_et.getText().toString());
                }
                break;
//            case R.id.clearLy: //清楚历史数据
//                openCleanDataDialog();
//                break;

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

            Log.e("sdfsfsf","进来了3");
        }


        if(!TextUtils.isEmpty(mCacheKey)){
            App.getACache().put(mCacheKey, mHistoryData);
        }

      //  clearLy.setVisibility(View.VISIBLE);
    }

    private void openCleanDataDialog() {  //打开清理缓存
        textDialog = new ClearSDdataDialog(mContext, R.style.dialog, "提示", "是否清除搜索历史", new ClearSDdataDialog.OnOkListener() {
            @Override
            public void onClick(View dialog, String text) {
                mHistoryData.clear();

                if(!TextUtils.isEmpty(mCacheKey)){
                    App.getACache().put(mCacheKey, mHistoryData);
                }

             //   clearLy.setVisibility(View.INVISIBLE);
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
        void onClick(int position, SearchHotKeyBean item);
    }

    /**
     * 历史搜索回调
     */
    public interface OnClickHistoryListener{
        void onClick(int position, String item);
    }

    /**
     * 搜索回调
     */
    public interface OnClickSearchListener{
        void onClick(String searchText);
    }
}
