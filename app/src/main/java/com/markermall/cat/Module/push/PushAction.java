package com.markermall.cat.Module.push;

import android.content.Context;

import java.util.HashSet;

/**
 * 推送相关操作
 */

public class PushAction {

    /**
     * 绑定账户接口:CloudPushService.bindAccount调用示例
     * 1. 绑定账号后,可以在服务端通过账号进行推送
     * 2. 一个设备只能绑定一个账号
     */
    public static void bindAccount(Context context, String account) {
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.action = TagAliasOperatorHelper.ACTION_SET;
        boolean isAliasAction = true;
        tagAliasBean.alias = "my_" + account;

        tagAliasBean.isAliasAction = isAliasAction;
        TagAliasOperatorHelper.getInstance().handleAction(context, TagAliasOperatorHelper.sequence, tagAliasBean);
    }

    /**
     * 添加tag
     * @param context
     * @param tag
     */
    public static void addTags(Context context,String tag) {
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.action = TagAliasOperatorHelper.ACTION_ADD;
        HashSet set = new HashSet<String>();
        set.add("everydayHotCommodity");
        set.add(tag);
        tagAliasBean.tags = set;
        tagAliasBean.isAliasAction = false;
        TagAliasOperatorHelper.getInstance().handleAction(context, TagAliasOperatorHelper.sequence, tagAliasBean);
    }

    /**
     *  删除tag
     * @param context
     * @param tag
     */
    public static void deleteTags(Context context,String tag) {
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.action = TagAliasOperatorHelper.ACTION_DELETE;
        HashSet set = new HashSet<String>();
        set.add(tag);
        tagAliasBean.tags=set;
        tagAliasBean.isAliasAction = false;
        TagAliasOperatorHelper.getInstance().handleAction(context, TagAliasOperatorHelper.sequence, tagAliasBean);
    }

    /**
     *  清除绑定
     * @param context
     * @param account
     */
    public static void CleanAccount(Context context, String account) {
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.action = TagAliasOperatorHelper.ACTION_CLEAN;
        boolean isAliasAction = true;
        tagAliasBean.alias = "my_" + account;
        tagAliasBean.isAliasAction = isAliasAction;
        TagAliasOperatorHelper.getInstance().handleAction(context, TagAliasOperatorHelper.sequence, tagAliasBean);
    }

    /**
     * 绑定电话接口:CloudPushService.bindPhoneNumber调用示例
     * 1. 绑定手机功能主要是方便用户接入复合推送功能:当推送消息无法到达时,通过手机短信的方式提示用户
     * 2. 复合推送接入详情请参考:https://help.aliyun.com/document_detail/57008.html
     */
    public static void bindPhoneNumber(String phoneNumber) {

    }

    /**
     * 绑定标签接口:CloudPushService.bindTag调用示例
     * 1. 标签可以绑定到设备、账号和别名上,此处demo展示的是绑定标签到设备
     * 2. 绑定标签完成后,即可通过标签推送消息
     */
    public static void bindTag(String tag) {

    }


}
