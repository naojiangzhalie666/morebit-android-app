package com.zjzy.morebit.pojo.request;

/**
 * @Description:
 * @Author: liys
 * @CreateDate: 2019/3/22 13:50
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/3/22 13:50
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class RequestGradeBean extends RequestBaseBean{

    private int displayLocal;

    public RequestGradeBean(int displayLocal) {
        this.displayLocal = displayLocal;
    }

    public int getDisplayLocal() {
        return displayLocal;
    }

    public void setDisplayLocal(int displayLocal) {
        this.displayLocal = displayLocal;
    }
}
