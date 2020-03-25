package com.zjzy.morebit.view.goods;

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

import com.zjzy.morebit.App;
import com.zjzy.morebit.R;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.action.MyAction;

import java.util.ArrayList;

/**
 * @author fengrs
 * @date 2019/8/31
 */
public class ShareMoneySwitchForPddTemplateView extends LinearLayout {

    private MyAction.One<Integer> mAction;
    private ArrayList<ImageView> mImageViewArrayList;

    public ShareMoneySwitchForPddTemplateView(Context context) {
        this(context, null);
    }

    public ShareMoneySwitchForPddTemplateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        int isInvitecode = App.getACache().getAsInt(C.sp.SHARE_MOENY_IS_INVITECODE);
        int isDownloadUrl = App.getACache().getAsInt(C.sp.SHARE_MOENY_IS_DOWNLOAD_URL);
        String shortLinkForPdd = App.getACache().getAsString(C.sp.SHARE_SHORT_LINK_FOR_PDD);
        mImageViewArrayList = new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();
        list.add(context.getString(R.string.share_money_download));
        list.add(context.getString(R.string.share_money_buy_url));
        list.add(context.getString(R.string.invitation_code_text));
        if (list != null && list.size() != 0) {
            this.removeAllViews();
            for (int i = 0; i < list.size(); i++) {

                final LinearLayout linearLayout = new LinearLayout(context);
                LayoutParams param = new LayoutParams(
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
                        if (finalI == 0 ){
                            imageView.setSelected(!isSelector);
                            imageView.setTag(!isSelector);
                            App.getACache().put(C.sp.SHARE_MOENY_IS_DOWNLOAD_URL, (boolean) imageView.getTag() ? "1" : "0");
                        }else if (finalI == 1){
//                            ImageView imageView1 = mImageViewArrayList.get(1);
//                            imageView1.setSelected(true);
//                            imageView1.setTag(!(boolean) imageView1.getTag());
//                            App.getACache().put(C.sp.isShortLink, (boolean) imageView1.getTag() ? "1" : "2");
                            App.getACache().put(C.sp.SHARE_SHORT_LINK_FOR_PDD,  "1");
                        }else if (finalI == 2 ){
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
                        if (isDownloadUrl == 1) {
                            imageView.setSelected(!(boolean) imageView.getTag());
                            imageView.setTag(!(boolean) imageView.getTag());
                        }
                        break;
                    case 1:
//                        if ("1".equals(isShortLink)) {
                        imageView.setSelected(true);
//                            imageView.setTag(!(boolean) imageView.getTag());
//                        }
                        break;
                    case 2:
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
