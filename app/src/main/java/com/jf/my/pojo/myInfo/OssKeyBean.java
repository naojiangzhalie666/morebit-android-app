package com.jf.my.pojo.myInfo;

import java.io.Serializable;

/**
 * Created by fengrs on 2018/10/16.
 *  获取oss 信息
 */

public class OssKeyBean implements Serializable {

    private String AccessKeyId ="";
    private String AccessKeySecret ="";
    private String SecurityToken ="";
    private String Expiration ="";
    private String callback ="";
    private String privateBucket ="";
    private String bucket ="";
    private String host ="";

    public String getAccessKeyId() {
        return AccessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        AccessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return AccessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        AccessKeySecret = accessKeySecret;
    }

    public String getSecurityToken() {
        return SecurityToken;
    }

    public void setSecurityToken(String securityToken) {
        SecurityToken = securityToken;
    }

    public String getExpiration() {
        return Expiration;
    }

    public void setExpiration(String expiration) {
        Expiration = expiration;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public String getPrivateBucket() {
        return privateBucket;
    }

    public void setPrivateBucket(String privateBucket) {
        this.privateBucket = privateBucket;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
