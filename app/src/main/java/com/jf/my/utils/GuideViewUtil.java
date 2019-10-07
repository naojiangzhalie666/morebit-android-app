package com.jf.my.utils;


import android.app.Activity;
import android.view.View;

import com.jf.my.App;
import com.jf.my.R;
import com.jf.my.interfaces.GuideNextCallback;
import com.jf.my.view.guideview.Component;
import com.jf.my.view.guideview.Guide;
import com.jf.my.view.guideview.GuideBuilder;
import com.jf.my.view.guideview.component.GuideCircle;
import com.jf.my.view.guideview.component.GuideCircleCollege;
import com.jf.my.view.guideview.component.GuideCircleSucai;
import com.jf.my.view.guideview.component.GuideHomeCollege;
import com.jf.my.view.guideview.component.GuideInvite;
import com.jf.my.view.guideview.component.GuideMoney;
import com.jf.my.view.guideview.component.GuideOder;
import com.jf.my.view.guideview.component.GuideSearch;
import com.jf.my.view.guideview.component.GuideService;

public class GuideViewUtil {
    public static final int GUIDE_SEARCH = 0;
    public static final int GUIDE_CIRCLE = 1;
    public static final int GUIDE_ORDER = 3;
    public static final int GUIDE_SUCAI = 4;
    public static final int GUIDE_COLLEAGE = 5;
    public static final int GUIDE_INVITE = 6;
    public static final int GUIDE_MONEY = 7;
    public static final int GUIDE_SERVICE = 8;
    public static final int GUIDE_HOME_COLLEGE = 9;
    public static Guide guide = null;
    private static  int serviceRvPositon = 0; //专属客服的位置
    private static boolean isleft = false;


    public static void showGuideView(Activity activity, final View targetView, final int viewType, int width,boolean mIsleft,int sPotiion, final GuideNextCallback mGuideNextCallback, final GuideCallback guideCallback){
        serviceRvPositon = sPotiion;
        isleft = mIsleft;
        showGuideView(activity,targetView,viewType,width,mGuideNextCallback,guideCallback);
    }



    public static void showGuideView(Activity activity, final View targetView, final int viewType, int width, final GuideNextCallback mGuideNextCallback, final GuideCallback guideCallback) {
        GuideBuilder builder = new GuideBuilder();
        if (viewType == GUIDE_ORDER) {
            builder.setTargetView(targetView.findViewById(R.id.iv_mine));
        } else {
            builder.setTargetView(targetView);
        }

        builder.setAlpha(0)
//                .setHighTargetCorner(20)
//                .setHighTargetPadding(10)
                .setOverlayTarget(false)
                .setOutsideTouchable(false);
        builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override
            public void onShown() {
            }

            @Override
            public void onDismiss() {
                switch (viewType) {
                    case GUIDE_SERVICE:
                        SharedPreferencesUtils.put(App.getAppContext(), C.sp.isShowGuideMine, false);
                        break;
                    case GUIDE_ORDER:
                        SharedPreferencesUtils.put(App.getAppContext(), C.sp.isShowGuide, false);
                        SharedPreferencesUtils.put(App.getAppContext(), C.sp.isShowGuideCircle, false);
                        break;
                    case GUIDE_CIRCLE:
                       // SharedPreferencesUtils.put(App.getAppContext(), C.sp.isShowGuide, false);
                        break;
                    case GUIDE_COLLEAGE:
                    case GUIDE_HOME_COLLEGE:
                    case GUIDE_SEARCH:
                        if (null != mGuideNextCallback) {
                            mGuideNextCallback.nextGuide();
                        }
                        break;
                }
                if (null != guideCallback) {
                    guideCallback.onDissmiss();
                }
            }
        });
        switch (viewType) {
            case GUIDE_SEARCH:
                GuideSearch guideSearch = new GuideSearch();
                builder.addComponent(guideSearch);
                guide = builder.createGuide();
                guide.setShouldCheckLocInWindow(false);
                guide.show(activity);
                guideSearch.setmOnCloseListener(new Component.onCloseListener() {
                    @Override
                    public void onDismiss() {
                        if (null != mGuideNextCallback) {
                            guide.dismiss();
                        }
                    }
                });
                break;
            case GUIDE_CIRCLE:
                GuideCircle guideCircleCase = new GuideCircle();
                builder.addComponent(guideCircleCase);
                guide = builder.createGuide();
                guide.setShouldCheckLocInWindow(false);
                guide.show(activity);
                guideCircleCase.setmOnCloseListener(new Component.onCloseListener() {
                    @Override
                    public void onDismiss() {
                        guide.dismiss();

                    }
                });
                break;
            case GUIDE_HOME_COLLEGE:
                GuideHomeCollege guideHomeCollege = new GuideHomeCollege();
                builder.addComponent(guideHomeCollege);
                guide = builder.createGuide();
                guide.setShouldCheckLocInWindow(false);
                guide.show(activity);
                guideHomeCollege.setmOnCloseListener(new Component.onCloseListener() {
                    @Override
                    public void onDismiss() {
                        guide.dismiss();

                    }
                });
                break;
            case GUIDE_ORDER:
                GuideOder guideOder = new GuideOder();
                builder.addComponent(guideOder);
                guide = builder.createGuide();
                guide.setShouldCheckLocInWindow(false);
                guide.show(activity);
                guideOder.setmOnCloseListener(new Component.onCloseListener() {
                    @Override
                    public void onDismiss() {
                        if (null != guide) {
                            guide.dismiss();
                        }
                    }
                });
                break;
            case GUIDE_SUCAI:
                GuideCircleSucai guideCircleSucai = new GuideCircleSucai(width);
                builder.addComponent(guideCircleSucai);
                final Guide mGuideSucai = builder.createGuide();
                mGuideSucai.setShouldCheckLocInWindow(false);
                mGuideSucai.show(activity);
                guideCircleSucai.setmOnCloseListener(new Component.onCloseListener() {
                    @Override
                    public void onDismiss() {
                        mGuideSucai.dismiss();
                    }
                });
                break;
            case GUIDE_COLLEAGE:
                GuideCircleCollege guideCircleCollege = new GuideCircleCollege();

                builder.addComponent(guideCircleCollege);
                guide = builder.createGuide();
                guide.setShouldCheckLocInWindow(false);
                guide.show(activity);
                guideCircleCollege.setmOnCloseListener(new Component.onCloseListener() {
                    @Override
                    public void onDismiss() {
                        if (null != mGuideNextCallback) {
                            guide.dismiss();
                        }
                    }
                });
                break;

            case GUIDE_INVITE:
                GuideInvite guideInvite = new GuideInvite();

                builder.addComponent(guideInvite);
                final Guide mGuide = builder.createGuide();
                mGuide.setShouldCheckLocInWindow(false);
                mGuide.show(activity);
                guideInvite.setmOnCloseListener(new Component.onCloseListener() {
                    @Override
                    public void onDismiss() {
                        mGuide.dismiss();
                    }
                });

                break;
            case GUIDE_MONEY:
                GuideMoney guideMoney = new GuideMoney();
                builder.addComponent(guideMoney);
                guide = builder.createGuide();
                guide.setShouldCheckLocInWindow(false);
                guide.show(activity);
                guideMoney.setmOnCloseListener(new Component.onCloseListener() {
                    @Override
                    public void onDismiss() {
                        guide.dismiss();
                    }
                });
                break;
            case GUIDE_SERVICE:
                GuideService guideService = new GuideService(width,isleft,serviceRvPositon);
                builder.addComponent(guideService);
                guide = builder.createGuide();
                guide.setShouldCheckLocInWindow(false);
                guide.show(activity);
                guideService.setmOnCloseListener(new Component.onCloseListener() {
                    @Override
                    public void onDismiss() {
                        guide.dismiss();
                    }
                });

                break;


        }
    }


    public static void dismiss(){
        if(null != guide){
            guide.dismiss();
            guide = null;
        }
    }

    public interface GuideCallback {
        void onDissmiss();
    }

}
