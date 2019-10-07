package com.markermall.cat.utils.fire;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;

/**
 * Created by jingdan on 2017/11/15.
 * Authentication server issued under the agreement of the official website agreement, you can directly use the provider
 */

public class OSSAuthCredentialsProvider extends OSSFederationCredentialProvider {

    private String mAccessKeyId;
    private String mAccessKeySecret;
    private String mSecurityToken;
    private String mExpiration;

    public OSSAuthCredentialsProvider(String accessKeyId, String accessKeySecret, String securityToken, String expiration) {
        mAccessKeyId = accessKeyId;
        mAccessKeySecret = accessKeySecret;
        mSecurityToken = securityToken;
        mExpiration = expiration;
    }

    @Override
    public OSSFederationToken getFederationToken() throws ClientException {
        OSSFederationToken authToken;
        try {
            authToken = new OSSFederationToken(mAccessKeyId, mAccessKeySecret, mSecurityToken, mExpiration);
            return authToken;
        } catch (Exception e) {
            throw new ClientException(e);
        }
    }

    public interface AuthDecoder {
        String decode(String data);
    }
}
