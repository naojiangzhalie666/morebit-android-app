package com.uuch.adlibrary;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.uuch.adlibrary.bean.AdInfo;
import com.uuch.adlibrary.bean.UpdateGradeInfo;
import com.uuch.adlibrary.utils.DisplayUtil;

/**
 * Created by Administrator on 2015/10/20 0020.
 * 首页广告管理类
 */
public class UpdateGradeManager {

    private Activity context;
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private View contentView;

    private RelativeLayout adRootContent;


    private AnimDialogUtils animDialogUtils;

    /**
     * 广告弹窗距离两侧的距离-单位(dp)
     */
    private int padding = 44;
    /**
     * 广告弹窗的宽高比
     */
    private float widthPerHeight = 0.75f;

    // 弹窗背景是否透明
    private boolean isAnimBackViewTransparent = false;
    // 弹窗是否可关闭
    private boolean isDialogCloseable = true;
    // 弹窗关闭点击事件
    private View.OnClickListener onCloseClickListener = null;
    // 设置弹窗背景颜色
    private int backViewColor = Color.parseColor("#bf000000");
    // 弹性动画弹性参数
    private double bounciness = AdConstant.BOUNCINESS;
    // 弹性动画速度参数
    private double speed = AdConstant.SPEED;

    // 是否覆盖全屏幕
    private boolean isOverScreen = true;

    private OnImageClickListener onImageClickListener = null;
    /**
     * leader对应的按钮监听
     */
    private OnVipDialogForLeaderClickListener onVipDialogForLeaderClickListener = null;
    /**
     * vip对应的按钮监听
     */
    private OnVipDialogForVipClickListener onVipDialogForVipClickListener = null;

    /**
     * 团队长的按钮监听
     */
    private OnLeaderDialogClickListener onLeaderDialogClickListener = null;
    /**
     * vip升级
     */
    LinearLayout vipUpdateInVipDailog;
    /**
     * leader升级
     */
    LinearLayout leaderUpdateInVipDailog;


    LinearLayout leaderUpdateInLeaderDailog;


    public UpdateGradeManager(Activity context) {
        this.context = context;
    }



    private View.OnClickListener vipDailogOnVipClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            if (onVipDialogForVipClickListener != null) {
                onVipDialogForVipClickListener.onUpdateVipClick(view);
            }
        }
    };

    private View.OnClickListener vipDailogOnLeaderClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (onVipDialogForLeaderClickListener != null) {
                onVipDialogForLeaderClickListener.onUpdateLeaderClick(view);
            }
        }
    };

    private View.OnClickListener leaderDailogOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (onLeaderDialogClickListener != null) {
                onLeaderDialogClickListener.onUpdateLeaderClick(view);
            }
        }
    };


    /**
     * vip展示升级对话框
     * @param animType
     */
    public void showUpdateVipGradeDialog(final int animType) {

        contentView = LayoutInflater.from(context).inflate(R.layout.update_vip_grade_dialog_content_layout, null);
        adRootContent = (RelativeLayout) contentView.findViewById(R.id.ad_root_content);

        vipUpdateInVipDailog = (LinearLayout)adRootContent.findViewById(R.id.update_vip_grade) ;
        vipUpdateInVipDailog.setOnClickListener(vipDailogOnVipClickListener);

        leaderUpdateInVipDailog = (LinearLayout)adRootContent.findViewById(R.id.update_leader_grade) ;
        leaderUpdateInVipDailog.setOnClickListener(vipDailogOnLeaderClickListener);

        animDialogUtils = AnimDialogUtils.getInstance(context)
                .setAnimBackViewTransparent(isAnimBackViewTransparent)
                .setDialogCloseable(isDialogCloseable)
                .setDialogBackViewColor(backViewColor)
                .setOnCloseClickListener(onCloseClickListener)
                .setOverScreen(isOverScreen)
                .initView(contentView);
        setRootContainerHeight();

        // 延迟1s展示，为了避免ImageLoader还为加载完缓存图片时就展示了弹窗的情况
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animDialogUtils.show(animType, bounciness, speed);
            }
        }, 1000);
    }
    /**
     * 团队长升级对话框
     * @param animType
     */
    public void showUpdateLeaderGradeDialog(final int animType) {

        contentView = LayoutInflater.from(context).inflate(R.layout.update_leader_grade_dialog_content_layout, null);
        adRootContent = (RelativeLayout) contentView.findViewById(R.id.ad_root_content);
        leaderUpdateInLeaderDailog = (LinearLayout) adRootContent.findViewById(R.id.update_leader_grade);

        leaderUpdateInLeaderDailog.setOnClickListener(leaderDailogOnClickListener);

        animDialogUtils = AnimDialogUtils.getInstance(context)
                .setAnimBackViewTransparent(isAnimBackViewTransparent)
                .setDialogCloseable(isDialogCloseable)
                .setDialogBackViewColor(backViewColor)
                .setOnCloseClickListener(onCloseClickListener)
                .setOverScreen(isOverScreen)
                .initView(contentView);
        setRootContainerHeight();

        // 延迟1s展示，为了避免ImageLoader还为加载完缓存图片时就展示了弹窗的情况
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animDialogUtils.show(animType, bounciness, speed);
            }
        }, 100);
    }

    /**
     * 开始执行销毁弹窗的操作
     */
    public void dismissAdDialog() {
        animDialogUtils.dismiss(AdConstant.ANIM_STOP_DEFAULT);
    }


    private void setRootContainerHeight() {

        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthPixels = displayMetrics.widthPixels;
        int totalPadding = DisplayUtil.dip2px(context, padding * 2);
        int width = widthPixels - totalPadding;
        final int height = (int) (width / widthPerHeight);
        ViewGroup.LayoutParams params = adRootContent.getLayoutParams();
        params.height = height;
    }




    // ######################## 点击事件处理操作类 ########################
    public interface OnVipDialogForLeaderClickListener {

        /**
         * 升级团队长的点击
         * @param view
         */
        public void onUpdateLeaderClick(View view);

    }

    public interface OnVipDialogForVipClickListener {
        /**
         * 升级vip会员的点击
         * @param view
         */
        public void onUpdateVipClick(View view);


    }
    public interface OnLeaderDialogClickListener {
        /**
         * 团队长界面，升级团队长的点击
         * @param view

         */
        public void onUpdateLeaderClick(View view);

    }

    /**
     * ViewPager每一项的单击事件
     */
    public interface OnImageClickListener {

        public void onImageClick(View view, AdInfo advInfo);

    }

    // ######################## get set方法 #########################

    /**
     * 设置弹窗距离屏幕左右两侧的距离
     * @param padding
     * @return
     */
    public UpdateGradeManager setPadding(int padding) {
        this.padding = padding;

        return this;
    }

    /**
     * 设置弹窗宽高比
     * @param widthPerHeight
     * @return
     */
    public UpdateGradeManager setWidthPerHeight(float widthPerHeight) {
        this.widthPerHeight = widthPerHeight;

        return this;
    }

    /**
     * 设置ViewPager Item点击事件
     * @return
     */
    public UpdateGradeManager setVipClickListener(OnVipDialogForVipClickListener vipClickListener) {
        this.onVipDialogForVipClickListener = vipClickListener;

        return this;
    }


    /**
     * 设置ViewPager Item点击事件
     * @return
     */
    public UpdateGradeManager setLeaderInVipDailogClickListener(OnVipDialogForLeaderClickListener leaderClickListener) {
        this.onVipDialogForLeaderClickListener = leaderClickListener;
        return this;
    }


    /**
     * 设置ViewPager Item点击事件
     * @return
     */
    public UpdateGradeManager setLeaderInLeadDailogClickListener(OnLeaderDialogClickListener onLeaderDialogClickListener) {
        this.onLeaderDialogClickListener = onLeaderDialogClickListener;

        return this;
    }


    /**
     * 设置ViewPager Item点击事件
     * @param onImageClickListener
     * @return
     */
    public UpdateGradeManager setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;

        return this;
    }

    /**
     * 设置背景是否透明
     * @param animBackViewTransparent
     * @return
     */
    public UpdateGradeManager setAnimBackViewTransparent(boolean animBackViewTransparent) {
        isAnimBackViewTransparent = animBackViewTransparent;

        return this;
    }

    /**
     * 设置弹窗关闭按钮是否可见
     * @param dialogCloseable
     * @return
     */
    public UpdateGradeManager setDialogCloseable(boolean dialogCloseable) {
        isDialogCloseable = dialogCloseable;

        return this;
    }

    /**
     * 设置弹窗关闭按钮点击事件
     * @param onCloseClickListener
     * @return
     */
    public UpdateGradeManager setOnCloseClickListener(View.OnClickListener onCloseClickListener) {
        this.onCloseClickListener = onCloseClickListener;

        return this;
    }

    /**
     * 设置弹窗背景颜色
     * @param backViewColor
     * @return
     */
    public UpdateGradeManager setBackViewColor(int backViewColor) {
        this.backViewColor = backViewColor;

        return this;
    }

    /**
     * 设置弹窗弹性动画弹性参数
     * @param bounciness
     * @return
     */
    public UpdateGradeManager setBounciness(double bounciness) {
        this.bounciness = bounciness;

        return this;
    }

    /**
     * 设置弹窗弹性动画速度参数
     * @param speed
     * @return
     */
    public UpdateGradeManager setSpeed(double speed) {
        this.speed = speed;

        return this;
    }

    /**

    /**
     * 设置弹窗背景是否覆盖全屏幕
     * @param overScreen
     * @return
     */
    public UpdateGradeManager setOverScreen(boolean overScreen) {
        isOverScreen = overScreen;

        return this;
    }
}
