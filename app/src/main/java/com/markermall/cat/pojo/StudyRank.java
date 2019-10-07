package com.markermall.cat.pojo;

import java.io.Serializable;

/**
 * Created by YangBoTian on 2018/12/24.
 */

public class StudyRank implements Serializable {

    private String id;       //模块id
    private String modelName;    //模块名称
    private String image;     //图片地址
    private String articleCount;     //文章数量
    private int isCollegeCategory;   //是否属于商学院类别(0: 否 1:是)
    private String url;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(String articleCount) {
        this.articleCount = articleCount;
    }

    public int getIsCollegeCategory() {
        return isCollegeCategory;
    }

    public void setIsCollegeCategory(int isCollegeCategory) {
        this.isCollegeCategory = isCollegeCategory;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
