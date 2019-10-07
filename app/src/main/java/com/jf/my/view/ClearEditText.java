package com.jf.my.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.jf.my.R;

/**
 * Created by YangBoTian on 2018/5/29 19:34
 *  带清楚按钮的 EditText
 */

public class ClearEditText extends AppCompatEditText {

    private Context mContext;
    private Drawable deleteImg;



    public ClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(attrs);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init(attrs);
    }


    private void init(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.clean_Edit);
        deleteImg = typedArray.getDrawable(R.styleable.clean_Edit_deleteImg);
        typedArray.recycle();
        if (deleteImg != null){//判断是否添加了删除按钮
            addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                   setDeleteImg();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }

    }

    private void setDeleteImg() {//设置删除按钮，输入字符串大于1时显示
        if (length()< 1){
            setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
        }else {
            setCompoundDrawablesWithIntrinsicBounds(null,null,deleteImg,null);

        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:

                boolean isClean =(event.getX() > (getWidth() - getTotalPaddingRight()))&&
                        (event.getX() < (getWidth() - getPaddingRight()));
                if (isClean) {
                  setText("");
                }
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }


}
