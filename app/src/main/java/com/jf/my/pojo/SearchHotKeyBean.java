package com.jf.my.pojo;

import java.io.Serializable;

/**
 * @Author: wuchaowen
 * @Description:
 * @Time: $date$ $time$
 **/
public class SearchHotKeyBean implements Serializable {

    /**
     * id : 202
     * keyWord : 关键词新增android
     * mark : 0
     * wordColor : #282828
     * backgroundColor : #686868
     * splicePid : 1
     * classId : 1
     * open : 3
     * url : baidu.com
     */

    private int id;
    private String keyWord;
    private int mark;
    private String wordColor;
    private String backgroundColor;
    private String splicePid;
    private int classId;
    private int open;  //打开方式（1：搜索列表 3：外站链接 9:文章）
    private String url;
    private int sign;

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public String getWordColor() {
        return wordColor;
    }

    public void setWordColor(String wordColor) {
        this.wordColor = wordColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getSplicePid() {
        return splicePid;
    }

    public void setSplicePid(String splicePid) {
        this.splicePid = splicePid;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
