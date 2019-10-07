package com.markermall.cat.pojo.request;

/**
 * @Description:
 * @Author: liys
 * @CreateDate: 2019/3/18 13:57
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/3/18 13:57
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ClassroomBean extends RequestBaseBean{
    private int id; //文章视频id
    private String title; //标题
    private String image;// 图片
    private int virtualView; //虚拟浏览量
    private int realView; //真实浏览量
    private int type; //1.文章 2.视频
    private String releaseTime; //发布时间
    private String url; //
    private String videoUrl; //

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getVirtualView() {
        return virtualView;
    }

    public void setVirtualView(int virtualView) {
        this.virtualView = virtualView;
    }

    public int getRealView() {
        return realView;
    }

    public void setRealView(int realView) {
        this.realView = realView;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
