package com.zjzy.morebit.pojo;

import java.io.Serializable;

/**
 * @Author: wuchaowen
 * @Description:
 **/
public class SystemConfigBean implements Serializable {

    /**
     * id : null
     * remark : 公告区-显示更多入口
     * sysKey : NOTICE_SHOW_MORE
     * sysValue : 1
     */

    private Object id;
    private String remark;
    private String sysKey;
    private String sysValue;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSysKey() {
        return sysKey;
    }

    public void setSysKey(String sysKey) {
        this.sysKey = sysKey;
    }

    public String getSysValue() {
        return sysValue;
    }

    public void setSysValue(String sysValue) {
        this.sysValue = sysValue;
    }
}
