package com.zjzy.morebit.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zjzy.morebit.App;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.goods.TKLBean;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.GoodsUtil;
import com.zjzy.morebit.utils.SharedPreferencesUtils;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.view.TemplatePreviewDialog;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zjzy.morebit.Activity.ShareMoneyActivity.goodsDataText;

/**
 * Created by YangBoTian on 2018/5/30 17:25
 * 编辑模板
 */

public class EditTemplateActivity extends BaseActivity {

    LinearLayout mBtnBack;
    TextView mTitle;
    TextView mExplain;
    private Bundle mBundle;
    private ShopGoodInfo goodsInfo;
    @BindView(R.id.et_copy)
    EditText et_copy;
    @BindView(R.id.tv_switcher_line)
    TextView tv_switcher_line;
    private String titleSign = "{标题}";  // 必填
    private String originalPrice = "{商品原价}";// 必填
    private String endPriceSign = "{券后价}";// 必填
    private String mTemp;
    private String mIsShortLink;
    private final String getTemplate = "get";
    private final String defaultTemplate = "default";
    private final String saveTemplate = "save";
    private final String switchTemplate = "switch";
    private final String longlink = "1";
    private final String shorelink = "2";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_template);
        setSystemBar();
        initView();
        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            goodsInfo = (ShopGoodInfo) mBundle.getSerializable(goodsDataText);
            mTemp = mBundle.getString("temp");
        }
        if (goodsInfo == null) return;

        et_copy.setText(mTemp);
    }

    private void initView() {

        mIsShortLink = App.getACache().getAsString(C.sp.isShortLink);
        switcherLineView();
        mBtnBack = (LinearLayout) findViewById(R.id.btn_back);
        mTitle = (TextView) findViewById(R.id.txt_head_title);
        mExplain = (TextView) findViewById(R.id.tv_explain);
        mTitle.setText(R.string.edit_template);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        String s = getString(R.string.explain1, "标题", "商品原价", "券后价", "宣传语", "下单链接");
        mExplain.setText(Html.fromHtml(s));

    }

    private void switcherLineView() {
        if (shorelink.equals(mIsShortLink)) {
            tv_switcher_line.setText(getString(R.string.switch_shore_link));
        } else {
            tv_switcher_line.setText(getString(R.string.switch_long_link));
        }
    }

    private void saveTkl(String type) {
        String etCopyText = isEmpty();
        if (etCopyText == null) return;
        saveOrGetTemplate(etCopyText, type);
    }

    @Nullable
    private String isEmpty() {
        String etCopyText = "";
        etCopyText = et_copy.getText().toString().trim();
        if (TextUtils.isEmpty(etCopyText)) {
            Toast.makeText(EditTemplateActivity.this, getResources().getString(R.string.goods_edit_template_null), Toast.LENGTH_SHORT).show();
            return null;
        }
        return etCopyText;
    }

    /**
     * 判断是不是没有
     *
     * @param etCopyText
     * @param titleSign
     * @param isMust
     * @return
     */
    private int indexText(String etCopyText, String titleSign, boolean isMust) {
        int indexTitle = etCopyText.indexOf(titleSign);
        if (indexTitle == -1 && isMust) {
            Toast.makeText(EditTemplateActivity.this, getResources().getString(R.string.goods_edit_shao) + titleSign, Toast.LENGTH_SHORT).show();
        }
        return indexTitle;
    }


    public void saveOrGetTemplate(final String template, final String type) {
        LoadingView.showDialog(this);
        SharedPreferencesUtils.put(EditTemplateActivity.this, C.sp.editTemplate, template);
        GoodsUtil.getGetTkLObservable(this, goodsInfo)

                .subscribe(new DataObserver<TKLBean>() {
                    @Override
                    protected void onSuccess(TKLBean bean) {
                        if (getTemplate.equals(type)) {
                            final String template = bean.getTemplate();
                            final TemplatePreviewDialog templatePreviewDialog = new TemplatePreviewDialog(EditTemplateActivity.this, template);
                            templatePreviewDialog.show();
                            templatePreviewDialog.setOnConfirmListener(new TemplatePreviewDialog.OnConfirmListener() {
                                @Override
                                public void onClick(View view) {
                                    saveOrGetTemplate(et_copy.getText().toString(), saveTemplate);
                                    templatePreviewDialog.dismiss();
                                }
                            });
                            templatePreviewDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    saveOrGetTemplate(mTemp, getTemplate);
                                }
                            });
                        } else if (defaultTemplate.equals(type)) {
                            final String template = bean.getTemp();
                            if (TextUtils.isEmpty(template)) return;
                            mTemp = template;
                            et_copy.setText(template);
                            setResult(RESULT_OK);
                            switcherLineView();
                            ViewShowUtils.showShortToast(EditTemplateActivity.this, getString(R.string.default_template_succeed)  );
                        } else if (switchTemplate.equals(type)) {
                            SharedPreferencesUtils.put(EditTemplateActivity.this, C.sp.editTemplate, template);
                            setResult(RESULT_OK);
                            switcherLineView();
                            if (longlink.equals(mIsShortLink)) {
                                ViewShowUtils.showShortToast(EditTemplateActivity.this, getString(R.string.has_been_for_you) + getString(R.string.switch_shore_link));
                            } else {
                                ViewShowUtils.showShortToast(EditTemplateActivity.this, getString(R.string.has_been_for_you) + getString(R.string.switch_long_link));
                            }
                        } else {
                            ViewShowUtils.showShortToast(EditTemplateActivity.this, "保存成功");
                            SharedPreferencesUtils.put(EditTemplateActivity.this, C.sp.editTemplate, template);
                            setResult(RESULT_OK);
                        }
                    }
                });
    }

    @OnClick({R.id.btn_back, R.id.tv_preview, R.id.tv_save, R.id.tv_default_template, R.id.tv_switcher_line})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                this.finish();
                break;
            case R.id.tv_preview:
                String etCopyText = isEmpty();
                if (etCopyText == null) return;
                saveOrGetTemplate(et_copy.getText().toString(), getTemplate);
                break;
            case R.id.tv_save:
                saveTkl(saveTemplate);
                break;
            case R.id.tv_default_template:
                App.getACache().put(C.sp.isShortLink, longlink);
                mIsShortLink = longlink;
                saveOrGetTemplate("", defaultTemplate);
                break;
            case R.id.tv_switcher_line:
                switcherLine();
                break;
            default:
                break;
        }
    }


    public void switcherLine() {

        if (shorelink.equals(mIsShortLink)) {
            mIsShortLink = longlink;
        } else {
            mIsShortLink = shorelink;
        }
        App.getACache().put(C.sp.isShortLink, mIsShortLink + "");
        saveTkl(switchTemplate);
    }

    @Override
    public boolean isShowAllSeek() {
        return false;
    }

}
