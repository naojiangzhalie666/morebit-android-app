package com.zjzy.morebit.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;

/**
 * 压缩图片工具类
 * 
 * @author orathee
 * 
 */
public class ZoomPictrue {

	public static final String IMAGE_FILE_LOCATION = Environment
			.getExternalStorageDirectory() + "/JupinApp/";

	/**
	 * 根据目标计算缩放比例系数
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	/**
	 * 获取资源文件
	 */
	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return compressImage(BitmapFactory.decodeResource(res, resId, options));
	}

	/**
	 * 从SD卡中获取压缩图片
	 * 
	 * @param path
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromSDCard(String path,
			int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(IMAGE_FILE_LOCATION + path,
				options);// 此时返回bm为空

		options.inJustDecodeBounds = false;
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		bitmap = BitmapFactory.decodeFile(IMAGE_FILE_LOCATION + path, options);
		return compressImage(bitmap);
	}
	/**
	 * 根据bitmap对象压缩返回bitmap
	 * @param image
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeSampleBitmapFromBitmap(Bitmap image,int reqWidth, int reqHeight){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, baos);
		
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, options);
		options.inJustDecodeBounds = false;
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, options);
		return compressImage(bitmap);
	}
	/**
	 * 质量压缩
	 * @param image
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			options -= 10;// 每次都减少10
			if(options <= 0){
				image.compress(Bitmap.CompressFormat.PNG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
				break;
			}
			image.compress(Bitmap.CompressFormat.PNG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	/**
	 * 保存文件到SD卡
	 * @param bm
	 * @param fileName
	 * @return -1,SD卡不存在
	 * @throws IOException SD卡存储空间不足
	 */
	public static int saveFile(Bitmap bm, String fileName) throws IOException {
		if (ExistSDCard()) {
			File dirFile = new File(IMAGE_FILE_LOCATION);
			if (!dirFile.exists()) {
				dirFile.mkdir();
			}
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(new File(IMAGE_FILE_LOCATION
							+ fileName)));
			bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
			bos.flush();
			bos.close();
			return 0;
		} else {
			return -1;
		}
	}

	/**
	 * 判断SD卡是否存在
	 * 
	 * @return
	 */
	public static boolean ExistSDCard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}

	/**
	 * SD卡剩余空间
	 * 
	 * @return
	 */
	public long getSDFreeSize() {
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 空闲的数据块的数量
		long freeBlocks = sf.getAvailableBlocks();
		// 返回SD卡空闲大小
		// return freeBlocks * blockSize; //单位Byte
		// return (freeBlocks * blockSize)/1024; //单位KB
		return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
	}
}
