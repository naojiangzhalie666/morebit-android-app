package com.zjzy.morebit.pojo.event;

/**
 * Created by fengrs on 2019/1/16.
 * 备注: 退出登录
 */

public class LogoutEvent {
    private  String logoutErrorMsg;
    private  boolean isPutError;

    public boolean isPutError() {
        return isPutError;
    }

    public void setPutError(boolean putError) {
        isPutError = putError;
    }

    public String getLogoutErrorMsg() {
        return logoutErrorMsg;
    }

    public void setLogoutErrorMsg(String logoutErrorMsg) {
        this.logoutErrorMsg = logoutErrorMsg;
    }
}
