package com.markermall.cat.Module.common.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.markermall.cat.Module.common.Fragment.ImageDetailFragment;
import com.markermall.cat.Module.common.Utils.LoadingView;
import com.markermall.cat.Module.common.View.HackyViewPager;
import com.markermall.cat.R;
import com.markermall.cat.contact.SdDirPath;
import com.markermall.cat.utils.ActivityStyleUtil;
import com.markermall.cat.utils.FileUtils;
import com.markermall.cat.utils.GoodsUtil;
import com.markermall.cat.utils.ViewShowUtils;
import com.markermall.cat.utils.action.MyAction;
import com.markermall.cat.utils.fire.BuglyUtils;
import com.markermall.cat.utils.sys.RequestPermissionUtlis;

import java.io.File;
import java.util.ArrayList;

/**
 * 图片查看器
 */
public class ImagePagerActivity extends BaseActivity {
    public static final String STATE_POSITION = "STATE_POSITION";
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_URLS = "image_urls";
    public static final String TAOBAO_ID = "taobao_id"; // 淘宝Id标识图片
    public static final String IS_NATIVE_IMG = "is_native_img"; // 是否本地图片

    private HackyViewPager mPager;
    private int pagerPosition;
    private TextView indicator;
    public int mPage;

    public static void startActivity(Context context, ArrayList<String> imgUrls, int position ) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(ImagePagerActivity.EXTRA_IMAGE_URLS, imgUrls);
        bundle.putInt(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_detail_pager);
        ActivityStyleUtil.setStatusBar(this);
        pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
        final ArrayList<String> urls = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS);
        if (urls == null || urls.size() == 0) {
            ViewShowUtils.showLongToast(this, "改图片无法查看");
            return;
        }
        mPager = (HackyViewPager) findViewById(R.id.pager);
        ImagePagerAdapter mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urls);
        mPager.setAdapter(mAdapter);
        indicator = (TextView) findViewById(R.id.indicator);
        View iv_save_img = findViewById(R.id.iv_save_img);
        if (getIntent().getBooleanExtra(IS_NATIVE_IMG, false)) {
            iv_save_img.setVisibility(View.GONE);
        } else {
            iv_save_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RequestPermissionUtlis.requestOne(ImagePagerActivity.this, new MyAction.OnResult<String>() {
                        @Override
                        public void invoke(String arg) {
                            saveImg(urls);
                        }


                        @Override
                        public void onError() {

                        }
                    }, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                }
            });
        }
        CharSequence text = getString(R.string.viewpager_indicator, 1, mPager.getAdapter().getCount());
        if(mPager.getAdapter().getCount()<=1){
            indicator.setVisibility(View.GONE);
        } else {
            indicator.setVisibility(View.VISIBLE);
        }
        indicator.setText(text);
        // 更新下标
        mPager.setOnPageChangeListener(new OnPageChangeListener() {


            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                ImagePagerActivity.this.mPage = arg0;
                CharSequence text = getString(R.string.viewpager_indicator, arg0 + 1, mPager.getAdapter().getCount());
                indicator.setText(text);

            }

        });
        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }

        mPager.setCurrentItem(pagerPosition);
    }

    private void saveImg(ArrayList<String> urls) {
//        final String bitName = getIntent().getStringExtra(TAOBAO_ID) + mPage;
        String bitName = "";
        String s = urls.get(mPage);
        if (TextUtils.isEmpty(s)) {
            BuglyUtils.e("ImagePagerActivity", "url 为空");
            ViewShowUtils.showLongToast(this, "保存图片失败");
            return;
        }
        bitName = FileUtils.getPictureName(s);
        LoadingView.showDialog(ImagePagerActivity.this, "");
        GoodsUtil.saveImg(ImagePagerActivity.this, s, bitName, new MyAction.OnResultTwo<File, Integer>() {
            @Override
            public void invoke(File arg, Integer arg1) {
                LoadingView.dismissDialog();
                if (arg == null) {
                    ViewShowUtils.showShortToast(ImagePagerActivity.this, "保存图片失败");
                    return;
                }
                if (arg1 == 1) {
                    ViewShowUtils.showShortToast(ImagePagerActivity.this, R.string.save_img_succeed);
                } else {
                    String dir = SdDirPath.IMAGE_CACHE_PATH;
//                                FileUtils.updaImgToMedia(ImagePagerActivity.this,dir,bitName);
                    String string = ImagePagerActivity.this.getResources().getString(R.string.img_save_to, dir);
                    ViewShowUtils.showShortToast(ImagePagerActivity.this, string);
                }
            }

            @Override
            public void onError() {
                LoadingView.dismissDialog();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, mPager.getCurrentItem());
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public ArrayList<String> fileList;

        public ImagePagerAdapter(FragmentManager fm, ArrayList<String> fileList) {
            super(fm);
            this.fileList = fileList;
        }

        @Override
        public int getCount() {
            return fileList == null ? 0 : fileList.size();
        }

        @Override
        public Fragment getItem(int position) {
            String url = fileList.get(position);
            return ImageDetailFragment.newInstance(url);
        }

    }
}
