package com.zjzy.morebit.pojo;

import java.io.Serializable;

public class CircleCopyBean implements Serializable {
    private String tkl;
    private String purchaseLink;

    public String getTkl() {
        return tkl;
    }

    public void setTkl(String tkl) {
        this.tkl = tkl;
    }

    public String getPurchaseLink() {
        return purchaseLink;
    }

    public void setPurchaseLink(String purchaseLink) {
        this.purchaseLink = purchaseLink;
    }
}
