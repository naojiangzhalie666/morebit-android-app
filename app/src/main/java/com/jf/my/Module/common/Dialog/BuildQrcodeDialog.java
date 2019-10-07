package com.jf.my.Module.common.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jf.my.R;
import com.jf.my.utils.GoodsUtil;
import com.jf.my.utils.ViewShowUtils;

/**
 * Created by Administrator on 2017/10/23.
 */

public class BuildQrcodeDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private TextView title_tv;
    private EditText edtext;
    private TextView cancel, ok;
    private String title;
    private String sex = "男";
    private String content = "";
    private OnCloseListener listener;
    private ImageView qrcode;
    private Bitmap qrBitmap;
    private String fileName;
    public BuildQrcodeDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public BuildQrcodeDialog(Context context, int themeResId, Bitmap qrBitmap) {
        super(context, themeResId);
        this.mContext = context;
        this.qrBitmap = qrBitmap;
        this.title = title;
    }
    public BuildQrcodeDialog(Context context, int themeResId, Bitmap qrBitmap,String fileName) {
        super(context, themeResId);
        this.mContext = context;
        this.qrBitmap = qrBitmap;
        this.title = title;
        this.fileName = fileName;
    }
    public BuildQrcodeDialog(Context context, int themeResId, String title, String content, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
        this.content = content;
        this.listener = listener;
    }

    public BuildQrcodeDialog(Context context, int themeResId, String title, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
        this.listener = listener;
    }

    protected BuildQrcodeDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_buildqrcode);
        setCanceledOnTouchOutside(false);
        initView();
    }

    public void setTitle(String text) {
        if (title_tv != null) {
            title_tv.setText(text);
        }
    }

    public void setContent(String text) {
        if (edtext != null) {
            edtext.setText(text);
        }
    }

    private void initView() {
        title_tv = (TextView) findViewById(R.id.title_tv);
        cancel = (TextView) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        findViewById(R.id.ok).setOnClickListener(this);
        qrcode = (ImageView) findViewById(R.id.qrcode);
        if (qrBitmap == null) {
            qrcode.setImageResource(R.drawable.service_person_qrcode);
        } else {
            qrcode.setImageBitmap(qrBitmap);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok:
                if (qrBitmap == null) {
                    return;
                }
                Uri uri = GoodsUtil.saveBitmap(mContext, qrBitmap, !TextUtils.isEmpty(fileName)?fileName:"img" + "weiweima");
                if (uri != null) {
                    ViewShowUtils.showShortToast(mContext, "保存成功");
                }
                this.dismiss();
                break;
            case R.id.cancel:
                this.dismiss();
                break;
        }
    }

    public interface OnCloseListener {
        void onClick(Dialog dialog, String text);
    }

}