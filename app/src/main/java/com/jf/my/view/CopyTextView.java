package com.jf.my.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.jf.my.R;
import com.jf.my.utils.AppUtil;
import com.jf.my.utils.ViewShowUtils;

/**
 * Created by feng on 2018/9/7.
 */

public class CopyTextView extends AppCompatTextView {

    public CopyTextView(Context context) {
        this(context, null);
    }

    public CopyTextView(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CharSequence text = getText();
                if (!TextUtils.isEmpty(text)) {
                    AppUtil.coayText((Activity) context, text.toString());
                    ViewShowUtils.showShortToast(context,context.getString(R.string.coayTextSucceed));
                }
                return false;
            }
        });
    }
}
