package com.jf.my.pojo;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * @author YangBoTian
 * @date 2019/9/3
 * @des
 */
public class CollegeHome  implements MultiItemEntity {
    public static final int TYPE_HOTIZONTAL = 0;    //横排
    public static final int TYPE_VERTICAL= 1;     //竖排
    public static final int TYPE_RECOMMEND= 3;     //为你推荐

    private int modelId; // 一级分类id
    private String modelName; // 一级分类名称
    private int typeset;  // 分类排版
    List<Article> articleList;   //排版(0:横排  1;竖排)

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public int getTypeset() {
        return typeset;
    }

    public void setTypeset(int typeset) {
        this.typeset = typeset;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }

    @Override
    public int getItemType() {
        return typeset;
    }
}
