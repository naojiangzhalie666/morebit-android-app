package com.markermall.cat.Module.common.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.markermall.cat.R;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;


/**
 * Created by YangBoTian on 2019/3/18.
 */

public class EarningsHintDialog extends Dialog {

    private TextView mBtnConfirm;
    private OnOkListener mOkListener;
    private OnCancelListner mCancelListener;
    private ClearSDdataDialog.OnOkListener okListener;
   private String mTitle,mContent;
    private int mContentID;
    private Context mContext;
    private TextView mBtnTitle,mBtnContent;
    private boolean gravity ;//是否设置控件居中
    public EarningsHintDialog(Context context, int themeResId, String title, String content ) {
        super(context, themeResId);
        mContext = context;
        mTitle = title;
        mContent = content;
    }

    public EarningsHintDialog(Context context, int themeResId, String title, @StringRes int contentID) {
        super(context, themeResId);
        mContext = context;
        mTitle = title;
        mContentID = contentID;
    }
    public EarningsHintDialog(RxAppCompatActivity activity) {
        super(activity, R.style.dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_earnings_hint);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        mBtnConfirm = findViewById(R.id.btn_confirm);
        mBtnTitle = findViewById(R.id.tv_title);
        mBtnContent = findViewById(R.id.tv_content);
        if(!TextUtils.isEmpty(mTitle)){
            mBtnTitle.setText(mTitle);
        }
        if(!TextUtils.isEmpty(mContent)){
            mBtnContent.setText(Html.fromHtml(mContent));
         } else if(mContentID!=0) {
            String s = mContext.getString(mContentID);
            mBtnContent.setText(Html.fromHtml(s));
//            mBtnContent.setText(mContentID);
        }

        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOkListener != null) {
                    mOkListener.onClick(v);
                    dismiss();
                }
            }
        });
        if(isGravity()){
            setContentGravity();
        }

    }

    private  void setContentGravity() {
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mBtnContent.getLayoutParams();
//        params.gravity = Gravity.RIGHT;
        if(mBtnContent!=null){
            mBtnContent.setGravity(Gravity.CENTER);
//            mBtnContent.setLayoutParams(params);
        }

    }

    public interface OnOkListener {
        void onClick(View view);
    }

    public interface OnCancelListner {
        void onClick(View view);
    }

    public void setmOkListener(OnOkListener mOkListener) {
        this.mOkListener = mOkListener;
    }

    public void setmCancelListener(OnCancelListner mCancelListener) {
        this.mCancelListener = mCancelListener;
    }

    public boolean isGravity() {
        return gravity;
    }

    public void setGravity(boolean gravity) {
        this.gravity = gravity;
    }
}
