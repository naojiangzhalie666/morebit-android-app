package com.jf.my.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: wuchaowen
 * @Description: 楼层
 * @Time:
 **/
public class FloorInfo implements Serializable {


    private int id;
    private int showType;
    private int mainTitleShow;
    private String mainTitle;
    private String subTitle;
    private int sort;
    private int status;
    private int childNum;
    private List<FloorChildInfo> child;
    private FloorInfo childTwo; //为你推荐


    public FloorInfo getChildTwo() {
        return childTwo;
    }

    public void setChildTwo(FloorInfo childTwo) {
        this.childTwo = childTwo;
    }

    public List<FloorChildInfo> getChild() {
        return child;
    }

    public void setChild(List<FloorChildInfo> child) {
        this.child = child;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public int getMainTitleShow() {
        return mainTitleShow;
    }

    public void setMainTitleShow(int mainTitleShow) {
        this.mainTitleShow = mainTitleShow;
    }

    public String getMainTitle() {
        return mainTitle;
    }

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getChildNum() {
        return childNum;
    }

    public void setChildNum(int childNum) {
        this.childNum = childNum;
    }




}
