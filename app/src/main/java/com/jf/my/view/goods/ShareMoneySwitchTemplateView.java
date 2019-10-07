package com.jf.my.view.goods;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jf.my.App;
import com.jf.my.R;
import com.jf.my.utils.C;
import com.jf.my.utils.action.MyAction;

import java.util.ArrayList;

/**
 * @author fengrs
 * @date 2019/8/31
 */
public class ShareMoneySwitchTemplateView extends LinearLayout {

    private MyAction.One<Integer> mAction;
    private ArrayList<ImageView> mImageViewArrayList;

    public ShareMoneySwitchTemplateView(Context context) {
        this(context, null);
    }

    public ShareMoneySwitchTemplateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        int isInvitecode = App.getACache().getAsInt(C.sp.SHARE_MOENY_IS_INVITECODE);
        int isDownloadUrl = App.getACache().getAsInt(C.sp.SHARE_MOENY_IS_DOWNLOAD_URL);
        String isShortLink = App.getACache().getAsString(C.sp.isShortLink);
        mImageViewArrayList = new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();
        list.add(context.getString(R.string.share_money_phone_line));
        list.add(context.getString(R.string.share_money_pc_line));
        list.add(context.getString(R.string.share_money_download));
        list.add(context.getString(R.string.invitation_code_text));
        if (list != null && list.size() != 0) {
            this.removeAllViews();
            for (int i = 0; i < list.size(); i++) {

                final LinearLayout linearLayout = new LinearLayout(context);
                LayoutParams param = new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT, 1.0f);
                param.gravity = Gravity.CENTER;
//                linearLayout.setPadding(5, 0, 0, 0);
                linearLayout.setGravity(Gravity.CENTER);
                String title = list.get(i);
                TextView textView2 = new TextView(context);
                textView2.setGravity(Gravity.CENTER);
                textView2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                textView2.setTextColor(ContextCompat.getColor(context, R.color.color_666666));
                textView2.setText(title);
                textView2.setPadding(6, 0, 0, 0);

                final ImageView imageView = new ImageView(context);
                imageView.setImageResource(R.drawable.selector_share_money_line);
                linearLayout.addView(imageView);
                linearLayout.addView(textView2);
                mImageViewArrayList.add(imageView);
                imageView.setTag(false);
                final int finalI = i;
                linearLayout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isSelector = (boolean) imageView.getTag();
                        if (finalI == 0 || finalI == 1) {
                            ImageView imageView1 = mImageViewArrayList.get(0);
                            imageView1.setSelected(!(boolean) imageView1.getTag());
                            imageView1.setTag(!(boolean) imageView1.getTag());
                            ImageView imageView2 = mImageViewArrayList.get(1);
                            imageView2.setSelected(!(boolean) imageView2.getTag());
                            imageView2.setTag(!(boolean) imageView2.getTag());
                            App.getACache().put(C.sp.isShortLink, (boolean) imageView1.getTag() ? "1" : "2");
                        } else if (finalI == 2 ){
                            imageView.setSelected(!isSelector);
                            imageView.setTag(!isSelector);
                            App.getACache().put(C.sp.SHARE_MOENY_IS_DOWNLOAD_URL, (boolean) imageView.getTag() ? "1" : "0");
                        }else if (finalI == 3 ){
                            imageView.setSelected(!isSelector);
                            imageView.setTag(!isSelector);
                            App.getACache().put(C.sp.SHARE_MOENY_IS_INVITECODE, (boolean) imageView.getTag() ? "1" : "0");
                        }
                        if (mAction != null) {
                            mAction.invoke(finalI);
                        }
                    }
                });
                linearLayout.setLayoutParams(param);
                switch (i) {
                    case 0:
                        if (TextUtils.isEmpty(isShortLink) || "1".equals(isShortLink)) {
                            imageView.setSelected(!(boolean) imageView.getTag());
                            imageView.setTag(!(boolean) imageView.getTag());
                        }
                        break;
                    case 1:
                        if ("2".equals(isShortLink)) {
                            imageView.setSelected(!(boolean) imageView.getTag());
                            imageView.setTag(!(boolean) imageView.getTag());
                        }
                        break;
                    case 2:
                        if (isDownloadUrl == 1) {
                            imageView.setSelected(!(boolean) imageView.getTag());
                            imageView.setTag(!(boolean) imageView.getTag());
                        }
                        break;
                    case 3:
                        if (isInvitecode == 1) {
                            imageView.setSelected(!(boolean) imageView.getTag());
                            imageView.setTag(!(boolean) imageView.getTag());
                        }
                        break;

                    default:
                        break;
                }

                this.addView(linearLayout);

            }
        }
    }


    public void setAction(MyAction.One<Integer> action) {
        mAction = action;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mImageViewArrayList != null) {
            mImageViewArrayList.clear();
        }
    }
}
