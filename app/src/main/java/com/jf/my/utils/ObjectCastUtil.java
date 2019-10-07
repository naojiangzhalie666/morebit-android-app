package com.jf.my.utils;

import com.jf.my.pojo.ImageInfo;
import com.jf.my.pojo.MiyuanInformation;

/**
 * @author YangBoTian
 * @date 2019/9/4
 * @des
 */
public class ObjectCastUtil {

    /**
     *
     * @param miyuanInformation    转换对象
     * @return
     */
    public static ImageInfo MYCastToImageInfo(MiyuanInformation miyuanInformation){
        ImageInfo imageInfo =new ImageInfo();
        if(miyuanInformation!=null){
            imageInfo.setClassId(miyuanInformation.getClassId());
            imageInfo.setOpen(miyuanInformation.getOpen());
            imageInfo.setId(miyuanInformation.getId());
            imageInfo.setTitle(miyuanInformation.getTitle());
            imageInfo.setUrl(miyuanInformation.getUrl());
        }
        return imageInfo;
    }
}
