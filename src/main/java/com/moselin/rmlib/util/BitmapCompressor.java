/**
 * 图片压缩工具类
 */
package com.moselin.rmlib.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;



public class BitmapCompressor {
	/**
	 * 质量压缩
	 */
	public static Bitmap compressBitmap(Bitmap image,int maxkb) {
		//L.showlog(压缩图片);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		L.v("原始大小" + baos.toByteArray().length);
		while (baos.toByteArray().length / 1024 > maxkb) { // 循环判断如果压缩后图片是否大于(maxkb)50kb,大于继续压缩
			L.v("压缩一次!");
			baos.reset();// 重置baos即清空baos
			options -= 10;// 每次都减少10
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
		}
		L.v("压缩后大小" + baos.toByteArray().length);
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}
	
	/**
	 * http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
	 * 官网：获取压缩后的图片
	 * 
	 * @param res
	 * @param resId
	 * @param reqWidth
	 *            所需图片压缩尺寸最小宽度
	 * @param reqHeight
	 *            所需图片压缩尺寸最小高度
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	/**
	 * 官网：获取压缩后的图片
	 * 
	 * @param reqWidth
	 *            所需图片压缩尺寸最小宽度
	 * @param reqHeight
	 *            所需图片压缩尺寸最小高度
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromFile(String filepath,
			int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filepath, options);

		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filepath, options);
	}

	public static Bitmap decodeSampledBitmapFromBitmap(Bitmap bitmap,
			int reqWidth, int reqHeight) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 90, baos);
		byte[] data = baos.toByteArray();
		
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data, 0, data.length, options);
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeByteArray(data, 0, data.length, options);
	}

	/**
	 * 计算压缩比例值(改进版 by touch_ping)
	 * 
	 * 原版2>4>8...倍压缩
	 * 当前2>3>4...倍压缩
	 * 
	 * @param options
	 *            解析图片的配置信息
	 * @param reqWidth
	 *            所需图片压缩尺寸最小宽度O
	 * @param reqHeight
	 *            所需图片压缩尺寸最小高度
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		
		final int picheight = options.outHeight;
		final int picwidth = options.outWidth;
		L.v("原尺寸:" +  picwidth + " " +picheight);
		
		int targetheight = picheight;
		int targetwidth = picwidth;
		int inSampleSize = 1;
		
		if (targetheight > reqHeight || targetwidth > reqWidth) {
			while (targetheight  >= reqHeight
					&& targetwidth>= reqWidth) {
				L.v("压缩:" +inSampleSize + "倍");
				inSampleSize += 1;
				targetheight = picheight/inSampleSize;
				targetwidth = picwidth/inSampleSize;
			}
		}

		L.v("最终压缩比例:" +inSampleSize + "倍");
		L.v( "新尺寸:" +  targetwidth + " " +targetheight);
		return inSampleSize;
	}
	
	/**
	 * 保存压缩图片
	 */
	public static String saveCompressBitmap(Bitmap mBitmap,String mStoragePath)  {
        File file = new File(mStoragePath);
		if (file.exists()&&file.isFile())
		L.v("file.delete()=="+file.delete());
		File newFile = new File(mStoragePath);
        FileOutputStream out = null;
        try {
        	out = new FileOutputStream(newFile);
             mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
             out.flush();
             out.close();
        } catch (Exception e) {
             e.printStackTrace();
        }
        return newFile.getPath();
	} 
	
	/**
	 * 使用系统当前日期加以调整作为压缩图片的名称
	 */
	private static String getCompressBitmapName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'COMPRESS_IMG'_yyyyMMdd_HHmmssSS");
		return dateFormat.format(date) + ".jpg";
	}
}
