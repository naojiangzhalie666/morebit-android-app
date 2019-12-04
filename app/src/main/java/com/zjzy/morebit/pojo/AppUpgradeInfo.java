package com.zjzy.morebit.pojo;

import java.io.Serializable;

/**
 * Created by fengrs on 2018/11/28.
 */

public class AppUpgradeInfo implements Serializable {
    private String version= ""; // 版本号
    private String info= "";  // 更新信息
    private String edition= ""; // 版名称
    private String upgradde= ""; // (1:是，2：否，3：静默更新)
    private String download= "";//下载地址
    private String status= "";
    private String md5= "";// 判断是不是同一个包
    private String size= "";// 大小
    private String createTime;



    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUpgradde() {
        return upgradde;
    }

    public void setUpgradde(String upgradde) {
        this.upgradde = upgradde;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
