package com.zjzy.morebit.utils;

import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.MarkermallInformation;

/**
 * @author YangBoTian
 * @date 2019/9/4
 * @des
 */
public class ObjectCastUtil {

    /**
     *
     * @param markermallInformation    转换对象
     * @return
     */
    public static ImageInfo MYCastToImageInfo(MarkermallInformation markermallInformation){
        ImageInfo imageInfo =new ImageInfo();
        if(markermallInformation !=null){
            imageInfo.setClassId(markermallInformation.getClassId());
            imageInfo.setOpen(markermallInformation.getOpen());
            imageInfo.setId(markermallInformation.getId());
            imageInfo.setTitle(markermallInformation.getTitle());
            imageInfo.setUrl(markermallInformation.getUrl());
        }
        return imageInfo;
    }
}
