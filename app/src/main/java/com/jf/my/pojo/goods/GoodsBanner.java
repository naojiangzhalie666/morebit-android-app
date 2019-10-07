package com.jf.my.pojo.goods;

import java.io.Serializable;
import java.util.List;

public class GoodsBanner implements Serializable {
     List<String> banner;

    public List<String> getBanner() {
        return banner;
    }

    public void setBanner(List<String> banner) {
        this.banner = banner;
    }
}