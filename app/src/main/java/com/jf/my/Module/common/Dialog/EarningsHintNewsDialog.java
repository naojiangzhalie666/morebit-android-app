package com.jf.my.Module.common.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.jf.my.R;
import com.jf.my.pojo.EarningExplainBean;


/**
 * Created by fengrs on 2019/7/24.
 */

public class EarningsHintNewsDialog extends Dialog {

    private final Context mContext;
    private TextView mBtnTitle, mBtnContent1, mBtnContent2, mBtnContent3;

    public EarningsHintNewsDialog(Context context) {
        super(context, R.style.dialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_earnings_hint_news);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {

        mBtnTitle = findViewById(R.id.tv_title);
        mBtnContent1 = findViewById(R.id.tv_content1);
        mBtnContent2 = findViewById(R.id.tv_content2);
        mBtnContent3 = findViewById(R.id.tv_content3);
        findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void show(int type, EarningExplainBean mEarningExplainBean) {
        super.show();
        if (type == 0) {


            mBtnTitle.setText(mContext.getString(R.string.earnings_day));
            String payNumStatement = "";
            String transactionStatement = "";
            String settlementStatement = "";
            String payNumStatement_title = mContext.getString(R.string.earnings_day_hint_def_1_title);
            String transactionStatement_title = mContext.getString(R.string.earnings_day_hint_def_2_title);
            String settlementStatement_title = mContext.getString(R.string.earnings_day_hint_def_3_title);
            if (mEarningExplainBean == null) {
                payNumStatement = mContext.getString(R.string.earnings_day_hint_def_1);
                transactionStatement = mContext.getString(R.string.earnings_day_hint_def_2);
                settlementStatement = mContext.getString(R.string.earnings_day_hint_def_3);
            } else {
                payNumStatement = TextUtils.isEmpty(mEarningExplainBean.getPayNumStatement()) ? mContext.getString(R.string.earnings_day_hint_def_1) : mEarningExplainBean.getPayNumStatement();
                transactionStatement = TextUtils.isEmpty(mEarningExplainBean.getTransactionStatement()) ? mContext.getString(R.string.earnings_day_hint_def_2) : mEarningExplainBean.getTransactionStatement();
                settlementStatement = TextUtils.isEmpty(mEarningExplainBean.getSettlementStatement()) ? mContext.getString(R.string.earnings_day_hint_def_3) : mEarningExplainBean.getSettlementStatement();
            }

            SpannableString spannableString = getSpannableString(payNumStatement, payNumStatement_title);
            mBtnContent1.setText(spannableString);
            SpannableString transactionStatementString = getSpannableString(transactionStatement, transactionStatement_title);
            mBtnContent2.setText(transactionStatementString);
            SpannableString settlementStatementString = getSpannableString(settlementStatement, settlementStatement_title);
            mBtnContent3.setText(settlementStatementString);


        } else {
            mBtnTitle.setText(mContext.getString(R.string.earnings_month));
            String consumptionStatement = "";
            String thisMonthStatement = "";
            String lastMonthStatement = "";
            String consumptionStatement_title = mContext.getString(R.string.earnings_month_hint_def_1_title);
            String thisMonthStatement_title = mContext.getString(R.string.earnings_month_hint_def_2_title);
            String lastMonthStatement_title = mContext.getString(R.string.earnings_month_hint_def_3_title);
            if (mEarningExplainBean == null) {
                consumptionStatement = mContext.getString(R.string.earnings_month_hint_def_1);
                thisMonthStatement = mContext.getString(R.string.earnings_month_hint_def_2);
                lastMonthStatement = mContext.getString(R.string.earnings_month_hint_def_3);
            } else {
                consumptionStatement = TextUtils.isEmpty(mEarningExplainBean.getConsumptionStatement()) ? mContext.getString(R.string.earnings_month_hint_def_1) : mEarningExplainBean.getConsumptionStatement();
                thisMonthStatement = TextUtils.isEmpty(mEarningExplainBean.getThisMonthStatement()) ? mContext.getString(R.string.earnings_month_hint_def_2) : mEarningExplainBean.getThisMonthStatement();
                lastMonthStatement = TextUtils.isEmpty(mEarningExplainBean.getLastMonthStatement()) ? mContext.getString(R.string.earnings_month_hint_def_3) : mEarningExplainBean.getLastMonthStatement();
            }

            SpannableString spannableString = getSpannableString(consumptionStatement, consumptionStatement_title);
            mBtnContent1.setText(spannableString);
            SpannableString transactionStatementString = getSpannableString(thisMonthStatement, thisMonthStatement_title);
            mBtnContent2.setText(transactionStatementString);
            SpannableString settlementStatementString = getSpannableString(lastMonthStatement, lastMonthStatement_title);
            mBtnContent3.setText(settlementStatementString);
        }
    }

    @NonNull
    private SpannableString getSpannableString(String payNumStatement, String payNumStatement_title) {
        SpannableString spannableString = new SpannableString( payNumStatement_title+payNumStatement );
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.color_333333));
        spannableString.setSpan(colorSpan, 0, payNumStatement_title.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}
