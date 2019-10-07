#include "com_loveyou_miyuan_utils_JniUtils.h"
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
const char *DES_KEY = "12345678912345678912345678912345";


//const char *crypt = "JAmH24thIL8rs09iwMw1lVwLt6bk3Hh91u5jyps9mE0bEIcCt1TOQOUeJ0iJqbvm";
//const char *token = "zhitu_yGMptkq0ERNe";

//商家版测试
//const char *crypt = "JAmH24thIL8rs09iwMw1lVwLt6bk3Hh91u5jyps9mE0bEIcCt1TOQOUeJ0iJqbvm" ;
//const char *token = "zhitu_yGMptkq0ERNe";

////测试  在用
//const char *crypt = "QMrK5Y49SixPL4WsCYIklGEXBdkHMiWVSMNGW8KScMdmfBRZlLVRWoLlmoIGdhKv";
//const char *token = "zhitu_Cd1Jjzthf6lw";

////测试  在用
//const char *crypt = "ka90VFbEYiCyQ975LT3Zb3kFaxUXaRMy7lqaPht7CFOlaAU4wKzwkQsBr8aMg1n8";
//const char *token = "zhitu_uSVnYLWLeGpk";

//模拟
const char *crypt = "HOQalETDi9sqrWyGMU3FTF49j9WMQlu3SllT0NgurbYkcJhPFGd1q2yu3dG43H7Q";
const char *token = "zhitu_rBMFGb1VmpIb";



//正式的服务器地址
//const char *crypt = "HOQalETDi9sqrWyGMU3FTF49j9WMQlu3SllT0NgurbYkcJhPFGd1q2yu3dG43H7Q";
//const char *token = "zhitu_rBMFGb1VmpIb";


const char *cryptStart = "crypt=";
const char *tokenStart = "token=";
const char *st1 = "noncestr=";
const char *st2 = "timestamp=";
const char *st3 = "&";
/*
* Class:     com_jzp_myapplication_JniUtils
* Method:    getString
* Signature: ()Ljava/lang/String;
ka90VFbEYiCyQ975LT3Zb3kFaxUXaRMy7lqaPht7CFOlaAU4wKzwkQsBr8aMg1n8*crypt=ka90VFbEYiCyQ975LT3Zb3kFaxUXaRMy7lqaPht7CFOlaAU4wKzwkQsBr8aMg1n8&noncestr=398386eb33b3474a9c694a7c8c6bfd67&timestamp=1528879233&token=zhitu_uSVnYLWLeGpk*ka90VFbEYiCyQ975LT3Zb3kFaxUXaRMy7lqaPht7CFOlaAU4wKzwkQsBr8aMg1n8
ka90VFbEYiCyQ975LT3Zb3kFaxUXaRMy7lqaPht7CFOlaAU4wKzwkQsBr8aMg1n8*crypt=ka90VFbEYiCyQ975LT3Zb3kFaxUXaRMy7lqaPht7CFOlaAU4wKzwkQsBr8aMg1n8&noncestr=6e1ad6fa03204fe8aa78d4e847fa3fcd&timestamp=1528879372&token=zhitu_uSVnYLWLeGpk*ka90VFbEYiCyQ975LT3Zb3kFaxUXaRMy7lqaPht7CFOlaAU4wKzwkQsBr8aMg1n8
ka90VFbEYiCyQ975LT3Zb3kFaxUXaRMy7lqaPht7CFOlaAU4wKzwkQsBr8aMg1n8*crypt=ka90VFbEYiCyQ975LT3Zb3kFaxUXaRMy7lqaPht7CFOlaAU4wKzwkQsBr8aMg1n8&noncestr=5a161b8870754682b185d24228c361ad&timestamp=1528879499&token=zhitu_uSVnYLWLeGpk*ka90VFbEYiCyQ975LT3Zb3kFaxUXaRMy7lqaPht7CFOlaAU4wKzwkQsBr8aMg1n8
*/
JNIEXPORT jstring JNICALL Java_com_loveyou_miyuan_utils_JniUtils_getStringC
        (JNIEnv *env, jobject obj){
    return (*env)->NewStringUTF(env,"这里是来自jni的string");
}

JNIEXPORT jstring JNICALL Java_com_loveyou_miyuan_utils_JniUtils_getToken
        (JNIEnv *env, jobject obj){
    return (*env)->NewStringUTF(env,token);
}

JNIEXPORT jstring JNICALL Java_com_loveyou_miyuan_utils_JniUtils_myEncrypt
        (JNIEnv *env, jclass jclass1, jstring key1,jstring key2)
{
    if (key1 == NULL) {
        return NULL;
    }
    jstring key;
    jstring result;
    jclass AESencrypt;
    jmethodID mid;

    char str4[400];
    char *stKey1 = (*env)->GetStringUTFChars(env, key1, 0);
    char *stKey2 = (*env)->GetStringUTFChars(env, key2, 0);
    strcpy(str4, cryptStart);
    strcat(str4, crypt);
    strcat(str4, st3);
    strcat(str4, st1);
    strcat(str4, stKey2);
    strcat(str4, st3);
    strcat(str4, st2);
    strcat(str4, stKey1);
    strcat(str4, st3);
    strcat(str4, tokenStart);
    strcat(str4, token);

    char str5[900];
    strcpy(str5, crypt);
    strcat(str5, "*");
    strcat(str5, str4);
    strcat(str5, "*");
    strcat(str5, crypt);

    (*env)->ReleaseStringUTFChars(env, key1, stKey1);
    (*env)->ReleaseStringUTFChars(env, key2, stKey2);


    /*
    String crypt = "crypt=520KeAiDeXiaoMinZi15XiaoMinZi520520KeAiDeXiaoMinZi15XiaoMinZi520";
    String noncestr = "noncestr=" + nstr;
    String timestamp = "timestamp=" + timeStr;
    String token = "token=520KeAiDeXiaoMinZi15XiaoMinZi520";
    String signStr = crypt + "&" + noncestr + "&" + timestamp + "&" + token;

    AESencrypt = (*env)->FindClass(env, "com/jzp/myapplication/JniUtils");
    if (NULL == AESencrypt) {
        return NULL;
    }
    mid = (*env)->GetStaticMethodID(env, AESencrypt, "encrypt",
                                    "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
    if (NULL == mid) {
        (*env)->DeleteLocalRef(env, AESencrypt);
        return NULL;
    }
    key = (*env)->NewStringUTF(env, DES_KEY);
    result = (*env)->CallStaticObjectMethod(env, AESencrypt, mid, key, jstr);
    (*env)->DeleteLocalRef(env, AESencrypt);
    (*env)->DeleteLocalRef(env, key);
     */
    result = (*env)->NewStringUTF(env, str5);
    return result;
}

JNIEXPORT jstring JNICALL Java_com_loveyou_miyuan_utils_JniUtils_myOldEncrypt
        (JNIEnv *env, jclass jclass1, jstring key1,jstring key2)
{
    if (key1 == NULL) {
        return NULL;
    }
    jstring key;
    jstring result;
    jclass AESencrypt;
    jmethodID mid;

    char *crypt = "crypt=520KeAiDeXiaoMinZi15XiaoMinZi520520KeAiDeXiaoMinZi15XiaoMinZi520";
    char *token = "token=520KeAiDeXiaoMinZi15XiaoMinZi520";
    char *st1 = "noncestr=";
    char *st2 = "timestamp=";
    char *st3 = "&";

    char str4[300];

    char *stKey1 = (*env)->GetStringUTFChars(env, key1, 0);
    char *stKey2 = (*env)->GetStringUTFChars(env, key2, 0);
    strcpy(str4, crypt);
    strcat(str4, st3);
    strcat(str4, st1);
    strcat(str4, stKey2);
    strcat(str4, st3);
    strcat(str4, st2);
    strcat(str4, stKey1);
    strcat(str4, st3);
    strcat(str4, token);
    (*env)->ReleaseStringUTFChars(env, key1, stKey1);
    (*env)->ReleaseStringUTFChars(env, key2, stKey2);


    /*
    String crypt = "crypt=520KeAiDeXiaoMinZi15XiaoMinZi520520KeAiDeXiaoMinZi15XiaoMinZi520";
    String noncestr = "noncestr=" + nstr;
    String timestamp = "timestamp=" + timeStr;
    String token = "token=520KeAiDeXiaoMinZi15XiaoMinZi520";
    String signStr = crypt + "&" + noncestr + "&" + timestamp + "&" + token;

    AESencrypt = (*env)->FindClass(env, "com/jzp/myapplication/JniUtils");
    if (NULL == AESencrypt) {
        return NULL;
    }
    mid = (*env)->GetStaticMethodID(env, AESencrypt, "encrypt",
                                    "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
    if (NULL == mid) {
        (*env)->DeleteLocalRef(env, AESencrypt);
        return NULL;
    }
    key = (*env)->NewStringUTF(env, DES_KEY);
    result = (*env)->CallStaticObjectMethod(env, AESencrypt, mid, key, jstr);
    (*env)->DeleteLocalRef(env, AESencrypt);
    (*env)->DeleteLocalRef(env, key);
     */
    result = (*env)->NewStringUTF(env, str4);
    return result;
}

JNIEXPORT jstring JNICALL Java_com_loveyou_miyuan_utils_JniUtils_myDecrypt
        (JNIEnv *env, jclass jclass1, jstring jstr)
{
    if (jstr == NULL) {
        return NULL;
    }
    jstring key;
    jstring result;
    jclass AESencrypt;
    jmethodID mid;

    AESencrypt = (*env)->FindClass(env, "com/jzp/myapplication/JniUtils");
    if (NULL == AESencrypt) {
        return NULL;
    }
    mid = (*env)->GetStaticMethodID(env, AESencrypt, "decrypt",
                                    "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
    if (NULL == mid) {
        (*env)->DeleteLocalRef(env, AESencrypt);
        return NULL;
    }
    key = (*env)->NewStringUTF(env, DES_KEY);
    result = (*env)->CallStaticObjectMethod(env, AESencrypt, mid, key, jstr);
    (*env)->DeleteLocalRef(env, AESencrypt);
    (*env)->DeleteLocalRef(env, key);
    return result;
}
