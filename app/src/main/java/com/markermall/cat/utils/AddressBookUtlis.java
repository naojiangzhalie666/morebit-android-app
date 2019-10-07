package com.markermall.cat.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.markermall.cat.pojo.AddressBookBean;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;

/**
 * Created by fengrs on 2018/6/23.
 * 获取手机通讯录
 */

public class AddressBookUtlis {
    public static ArrayList<AddressBookBean> getAddressBookPhome(Context context) {
        ArrayList<AddressBookBean> list = new ArrayList<>();

        //获取联系人信息的Uri
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        //获取ContentResolver
        ContentResolver contentResolver = context.getContentResolver();
        //查询数据，返回Cursor
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        while (cursor.moveToNext()) {
            AddressBookBean bookBean = new AddressBookBean();
            //获取联系人的ID
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            //获取联系人的姓名
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            //构造联系人信息

            bookBean.setName(name);


            //查询电话类型的数据操作
            Cursor phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                    null, null);
            while (phones.moveToNext()) {
                String phoneNumber = phones.getString(phones.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                //添加Phone的信息
                String replace = phoneNumber.replace(" ", "");
                bookBean.setPhome(replace);
            }
            phones.close();
            if (TextUtils.isEmpty(bookBean.getName()) || TextUtils.isEmpty(bookBean.getPhome())) {
                continue;
            }
            list.add(bookBean);
        }
        cursor.close();
        return list;
    }

    public static void sendSMS(final Context context, final String phoneNumber, final String message) {

        RxPermissions rxPermission = new RxPermissions((Activity) context);
        rxPermission.requestEach(Manifest.permission.READ_SMS)
                .subscribe(new Consumer<Permission>() {

                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {

                            try {
                                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
                                intent.putExtra("sms_body", message);
                                context.startActivity(intent);
                            } catch (Exception e) {
                                ViewShowUtils.showShortToast(context, "发送失败");
                            }
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            ViewShowUtils.showShortToast(context, "请开启权限,才能使用此功能哦");
                        } else {
                            AppUtil.goSetting((Activity) context);
                        }
                    }
                });


    }

}
