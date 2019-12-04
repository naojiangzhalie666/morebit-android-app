package com.zjzy.morebit.network;

/**
 * <b>类名称：</b> MineSSLFactory <br/>
 * <b>类描述：</b> <br/>
 * <b>创建人：</b> Lincoln <br/>
 * <b>修改人：</b> Lincoln <br/>
 * <b>修改时间：</b> 2017年03月29日 16:11<br/>
 * <b>修改备注：</b> <br/>
 *
 * @version 1.0.0 <br/>
 */
public class MineSSLFactory {
//    private static final String KEY_STORE_TYPE_BKS = "bks";//证书类型
//    private static final String KEY_STORE_TYPE_P12 = "PKCS12";//证书类型
//
//
////    private static final String KEY_STORE_PASSWORD = "***";//证书密码（应该是客户端证书密码）
//    private static final String KEY_STORE_TRUST_PASSWORD = "***";//授信证书密码（应该是服务端证书密码）
//
//    public static SSLSocketFactory getSocketFactory(Context context) {
//        InputStream trust_input = context.getResources().openRawResource(R.raw.client_trust);//服务器授信证书
////        InputStream client_input = context.getResources().openRawResource(R.raw.client);//客户端证书
//        try {
//            SSLContext sslContext = SSLContext.getInstance("TLS");
//            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//            trustStore.load(trust_input, KEY_STORE_TRUST_PASSWORD.toCharArray());
//            KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE_P12);
////            keyStore.load(client_input, KEY_STORE_PASSWORD.toCharArray());
//            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//            trustManagerFactory.init(trustStore);
//
//            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
////            keyManagerFactory.init(keyStore, KEY_STORE_PASSWORD.toCharArray());
//            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
//            SSLSocketFactory factory = sslContext.getSocketFactory();
//            return factory;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        } finally {
//            try {
//                trust_input.close();
////                client_input.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}