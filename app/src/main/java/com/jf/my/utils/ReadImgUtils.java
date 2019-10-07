package com.jf.my.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import com.jf.my.R;
import com.jf.my.utils.action.MyAction;
import com.luck.picture.lib.PictureSelectionModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.functions.Consumer;

/**
 * Created by fengrs on 2018/6/13.
 * 读取本地图片
 */

public class ReadImgUtils {

    public static final int AppFeedBackFragment = 1;
    //    public static final int AppFeedBackFragment = 2;
//    public static final int AppFeedBackFragment = 3;
    public static final int CHOOSE_PICTURE = 0;
    public static final int TAKE_PICTURE = 1;
    public static final int CROP_SMALL_PICTURE = 2;

    //图库
    public static final int PHOTO_TK = 0;
    //拍照
    public static final int PHOTO_PZ = 1;
    //裁剪
    public static final int PHOTO_CLIP = 2;

    public static void callPermission(final Activity activity) {
        callPermission(activity, null, false, false);
    }

    public static void callPermissionOfEnableCrop(final Activity activity) {
        callPermission(activity, null, false, true);
    }
    public static void callPermissionMultiplePic(final Activity activity,int maxSelect) {
        callPermission(activity, null, false, false,maxSelect);
    }
    public static void callPermission(final Activity activity, final MyAction.OnResult<Uri> action, final boolean isCancelable, final boolean isEnableCrop) {
        final ArrayList<Boolean> arrayList = new ArrayList<>();
        RxPermissions rxPermission = new RxPermissions(activity);
        rxPermission
                .requestEach(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            arrayList.add(true);
                            // 用户已经同意该权限
                            if (!arrayList.contains(false) && arrayList.size() == 3) {
                                showChoosePicDialog(activity, action, "图片上传", isCancelable, isEnableCrop);
                            }
                        } else {
                            arrayList.add(false);
                            // 用户拒绝了该权限，并且选中『不再询问』
                            if (arrayList.size() == 3) {
                                if (arrayList.get(0)) { // 相机已有
                                    ViewShowUtils.showShortToast(activity, R.string.call_premission_read_sd);
                                } else {
                                    ViewShowUtils.showShortToast(activity, R.string.call_premission_camera);
                                }
                                AppUtil.goSetting(activity);
                                if (action != null)
                                    action.onError();
                            }
                        }
                    }
                });
    }

    /**
     * 多图片选中
     * @param activity
     * @param action
     * @param isMultiple
     */
    public static void callPermission(final Activity activity, final MyAction.OnResult<Uri> action, final boolean isCancelable, final boolean isEnableCrop, final int maxSelect) {
        final ArrayList<Boolean> arrayList = new ArrayList<>();
        RxPermissions rxPermission = new RxPermissions(activity);
        rxPermission
                .requestEach(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            arrayList.add(true);
                            // 用户已经同意该权限
                            if (!arrayList.contains(false) && arrayList.size() == 3) {
                                 showChooseMultipPicDialog(activity, action, "图片上传", isCancelable, isEnableCrop,maxSelect);
                            }
                        } else {
                            arrayList.add(false);
                            // 用户拒绝了该权限，并且选中『不再询问』
                            if (arrayList.size() == 3) {
                                if (arrayList.get(0)) { // 相机已有
                                    ViewShowUtils.showShortToast(activity, R.string.call_premission_read_sd);
                                } else {
                                    ViewShowUtils.showShortToast(activity, R.string.call_premission_camera);
                                }
                                AppUtil.goSetting(activity);
                                if (action != null)
                                    action.onError();
                            }
                        }
                    }
                });
    }

    public static void callPermission(final Activity activity, final MyAction.OnResult<String> action, final String... permissions) {
        RxPermissions rxPermission = new RxPermissions(activity);
        rxPermission
                .requestEach(permissions)
                .subscribe(new Consumer<Permission>() {
                    int permissionsLength = permissions.length;
                    @Override
                    public void accept(Permission permission) throws Exception {
                        permissionsLength--;
                        if (permission.granted) {
                            if (permissionsLength == 0) {
                                if (action != null)
                                    action.invoke("");
                            }
                        } else {

                            if (permissionsLength == 0) {
                                AppUtil.goSetting(activity);
                                if (action != null)
                                    action.onError();
                                permissionsLength = -1;
                            }

                        }
                    }
                });
    }


    protected static void showChoosePicDialog(final Activity activity, final MyAction.OnResult<Uri> action, String title, boolean isCancelable, final boolean isEnableCrop) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        if (isCancelable) {
            builder.setCancelable(false);
        }
        builder.setTitle(title);
        String[] items = {"选择本地照片", "拍照"};
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (action != null)
                    action.onError();
            }
        });
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                PictureSelectionModel pictureSelectionModel = null;
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        pictureSelectionModel = PictureSelector.create(activity)
                                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                                .maxSelectNum(3)// 最大图片选择数量 int
                                .imageSpanCount(3)// 每行显示个数 int
                                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                                .previewImage(true)// 是否可预览图片 true or false
                                .isCamera(true)// 是否显示拍照按钮 true or false
                                .imageFormat(PictureMimeType.JPEG)// 拍照保存图片格式后缀,默认jpeg
                                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                                .setOutputCameraPath("/CustomPath");// 自定义拍照保存路径,可不填


                        break;
                    case TAKE_PICTURE: // 拍照
                        // 启动系统相机
                        pictureSelectionModel = PictureSelector.create(activity)
                                .openCamera(PictureMimeType.ofImage())
                                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                                .compress(true);// 是否压缩 true or false

                        break;
                }
                if (isEnableCrop && pictureSelectionModel != null) {
                    pictureSelectionModel
                            .compress(true)// 是否压缩 true or false
                            .enableCrop(true)// 是否裁剪 true or false
                            .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                            .rotateEnabled(false) // 裁剪是否可旋转图片 true or false
                            .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                } else {
                    pictureSelectionModel.forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                }
            }
        });
        builder.create().show();
    }

    /**
     * 多图片选中
     * @param activity
     * @param action
     * @param title
     * @param isCancelable
     * @param isEnableCrop
     */
    protected static void showChooseMultipPicDialog(final Activity activity, final MyAction.OnResult<Uri> action, String title, boolean isCancelable, final boolean isEnableCrop, final int maxSelect) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        if (isCancelable) {
            builder.setCancelable(false);
        }
        builder.setTitle(title);
        String[] items = {"选择本地照片", "拍照"};
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (action != null)
                    action.onError();
            }
        });
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                PictureSelectionModel pictureSelectionModel = null;
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        pictureSelectionModel = PictureSelector.create(activity)
                                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                                .maxSelectNum(maxSelect)// 最大图片选择数量 int
                                .imageSpanCount(3)// 每行显示个数 int
                                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                                .previewImage(true)// 是否可预览图片 true or false
                                .isCamera(true)// 是否显示拍照按钮 true or false
                                .imageFormat(PictureMimeType.JPEG)// 拍照保存图片格式后缀,默认jpeg
                                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                                .setOutputCameraPath("/CustomPath");// 自定义拍照保存路径,可不填


                        break;
                    case TAKE_PICTURE: // 拍照
                        // 启动系统相机
                        pictureSelectionModel = PictureSelector.create(activity)
                                .openCamera(PictureMimeType.ofImage())
                                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                                .compress(true);// 是否压缩 true or false

                        break;
                }
                if (isEnableCrop && pictureSelectionModel != null) {
                    pictureSelectionModel
                            .compress(true)// 是否压缩 true or false
                            .enableCrop(true)// 是否裁剪 true or false
                            .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                            .rotateEnabled(false) // 裁剪是否可旋转图片 true or false
                            .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                } else {
                    pictureSelectionModel.forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                }
            }
        });
        builder.create().show();
    }
    public static Uri startPhotoZoom(Activity activity, Uri uri) {
        return startPhotoZoom(activity, uri, 1, 1, 300, 300);
    }

    public static Uri startPhotoZoom(Activity activity, Uri uri, int aspectX, int aspectY, int outputX, int outputY) {

        Uri uritempFile;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        //uritempFile为Uri类变量，实例化uritempFile

        if (Build.VERSION.SDK_INT >= 24) {
            //开启临时权限
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //重点:针对7.0以上的操作
            intent.setClipData(ClipData.newRawUri(MediaStore.EXTRA_OUTPUT, uri));
            uritempFile = uri;
        } else {
            uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "temp.jpg");
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        activity.startActivityForResult(intent, ReadImgUtils.PHOTO_CLIP);
        return uritempFile;
    }

    /**
     * 裁剪图片
     * 将裁剪后的图片,保存到本地
     *
     * @param uri
     */
    public static Uri startPhotoZoomTk(Activity activity, Uri uri) {
        return startPhotoZoomTk(activity, uri, 1, 1, 300, 300);
    }

    ;

    public static Uri startPhotoZoomTk(Activity activity, Uri uri, int aspectX, int aspectY, int outputX, int outputY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
//参考上述知识点1
        intent.setDataAndType(uri, "image/*");
// 设置裁剪
        intent.putExtra("crop", "true");
// aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
// outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
//创建文件
        String headCachePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "hb" + File.separator + "headCache";

        File parentPath = new File(headCachePath);
        if (!parentPath.exists()) {
            parentPath.mkdirs();
        }
        File headCacheFile = new File(parentPath, "head.png");
        if (!headCacheFile.exists()) {
            try {
                headCacheFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        headCachePath = headCacheFile.getAbsolutePath();
        Uri uritempFile = Uri.parse("file://" + headCacheFile.getAbsolutePath());
//将裁剪好的图输出到所建文件中
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, ReadImgUtils.PHOTO_CLIP);
        return uritempFile;
    }

}

