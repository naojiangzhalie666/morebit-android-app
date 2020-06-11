package com.zjzy.morebit.utils.UI;

import android.content.Context;



public class ContextHolder {
    private Context sContext = null;
    private static volatile ContextHolder instance;
    public static synchronized ContextHolder getInstance(){
        if (instance==null){
            synchronized (ContextHolder.class){
                if (instance==null){
                    instance=new ContextHolder();
                }
            }
        }
        return instance;
    }
    public void init(Context ctx) {
        sContext = ctx;
    }

    public Context getContext() {
        return sContext;
    }

    /**
     * 获取Application标签内的meta data
     */
//    public String getChannelIDFromManifest() {
//       // return AppUtil.getChannelIDFromManifest(sContext);
//        return ;
//    }


}
