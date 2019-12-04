package com.zjzy.morebit.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: wuchaowen
 * @Description:
 **/
public class FloorChildInfo implements Serializable {

    private int classId;
    private String subTitle;
    private String backgroundImage;
    private String mainTitle;
    private int showType;
    private int sort;
    private String label;
    private int id;
    private String graphicInfoId;
    private String url;
    private int open;
    private String picture;
    private String splicePid;

    public String getSplicePid() {
        return splicePid;
    }

    public void setSplicePid(String splicePid) {
        this.splicePid = splicePid;
    }

    private List<FloorChildInfo> child;

    public List<FloorChildInfo> getChild() {
        return child;
    }

    public void setChild(List<FloorChildInfo> child) {
        this.child = child;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getMainTitle() {
        return mainTitle;
    }

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGraphicInfoId() {
        return graphicInfoId;
    }

    public void setGraphicInfoId(String graphicInfoId) {
        this.graphicInfoId = graphicInfoId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
