package com.moselin.rmlib.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.moselin.rmlib.Mosl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;



/**
 * 创建时间：2015-4-30 上午8:45:34 
 * 
 * @author mosl
 * @version 2.0.0
 * @since JDK 1.7 文件名称：PhotoHelper.java 类说明：
 */
public class PhotoHelper {
	public static final int REQUEST_LOAD_PHOTO_PICKED = 101;//打开相册的请求码
	public static final int REQUEST_LOAD_PHOTO_CAMERA = 102;//打开相册拍照的请求码
	public static final int REQUEST_PHOTO_CROP = 103;//对图片进行裁剪的请求码
	/**
	 * 从已有图库中获取相片
	 * @param context 上下文对象
	 * @param requestCode 请求码
     */
	public static void selectMyPhotoFormGallery(Activity context,
			int requestCode) {
		Intent _intent = new Intent(Intent.ACTION_PICK);
		_intent.setType("image/*");
		context.startActivityForResult(_intent, requestCode);
	}

	/**
	 * 从相机新拍相片中获取图片
	 * @param context 上下文对象
	 * @param pPhotoFile 保存图片的文件名，可为空，为空会调用
	 * @param requestCode 请求码
     * @return 拍照后得到的图片的uri路径
     */
	public static Uri selectMyPhotoForCamera(Activity context,
			String pPhotoFile, int requestCode) {

		if (StringUtils.isBlank(pPhotoFile)) {
			pPhotoFile = generateFileName();
		}
		String mTmpCameraFilePath = pathForNewCameraPhoto(pPhotoFile);
		Uri mCurrentPhotoFileUri = Uri.fromFile(new File(mTmpCameraFilePath));
		try {
			Intent _intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			_intent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoFileUri);
			context.startActivityForResult(_intent, requestCode);
		} catch (ActivityNotFoundException e) {
			L.e("没有找到相机设备！");
		}
		return mCurrentPhotoFileUri;
	}

	public static String selectForCamera(Activity context, String pPhotoFile,
			int requestCode) {

		if (StringUtils.isBlank(pPhotoFile)) {
			pPhotoFile = generateFileName();
		}
		String mTmpCameraFilePath = pathForNewCameraPhoto(pPhotoFile);
		Uri mCurrentPhotoFileUri = Uri.fromFile(new File(mTmpCameraFilePath));
		try {
			Intent _intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			_intent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoFileUri);
			context.startActivityForResult(_intent, requestCode);
		} catch (ActivityNotFoundException e) {
			L.e("没有找到相机设备！");
		}
		return mTmpCameraFilePath;
	}

	/*
	 * 根据当前的时间进行生成文件名
	 */
	private static String generateFileName() {
		return StringUtils.md5(String.valueOf(System.currentTimeMillis()))
				+ ".jpg";
	}

	/**
	 * 获取Camera拍摄的照片绝地路径
	 * 
	 * @param fileName
	 * @return
	 */
	private static String pathForNewCameraPhoto(String fileName) {
		return pathForNewCameraPhoto(null, fileName);
	}

	private static String pathForNewCameraPhoto(String dir, String fileName) {
		if (dir == null) {
			dir = Mosl.APP_PATH + "Users/Image/";
		}
		File file = FileUtils.makeFolder(dir);
		File iFile = new File(file.getAbsolutePath(), fileName);
		if (!iFile.exists())
			return iFile.getAbsolutePath();
		else
			return null;
	}

	/**
	 * 裁剪图片
	 * @param context 上下文对象
	 * @param pPhotoUri 原图片的uri路径
	 * @param pResultCode 裁剪的请求码
	 * @param isXY 是否设置1：1的裁剪框比例
     * @return 裁剪后的图片路径
     */
	public static String doCropPhoto(Activity context, Uri pPhotoUri,
			int pResultCode, boolean isXY) {
		String mTmpCameraFilePath = pathForNewCameraPhoto(generateFileName());
		Uri mCurrentPhotoFileUri = Uri.fromFile(new File(mTmpCameraFilePath));
		Intent _intent = new Intent("com.android.camera.action.CROP");
		_intent.setDataAndType(pPhotoUri, "image/*");
		_intent.putExtra("crop", "true");
		// 裁剪框的比例 1：1
		if (isXY) {
			_intent.putExtra("aspectX", 1);
			_intent.putExtra("aspectY", 1);
		}

		// 裁剪后输出图片的尺寸大小
		_intent.putExtra("outputX", 300);
		_intent.putExtra("outputY", 300);

		_intent.putExtra("outputFormat", "png");// 图片格式
		_intent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoFileUri);
		context.startActivityForResult(_intent, pResultCode);
		return mTmpCameraFilePath;
	}


	/**
	 * 压缩图片
	 * 
	 *            用户
	 * @param uri
	 *            地址
	 */
	public static Bitmap compressImage(Bitmap image, final String uri) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		final Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
				File iFile = new File(uri);
				try {
					FileOutputStream out = new FileOutputStream(iFile);
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
					out.flush();
					out.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
		return bitmap;
	}
 
	/**
	 * 按质量进行压缩图片
	 * @param image
	 * @param uri
	 * @return
	 */
	public static Bitmap comp(Bitmap image, String uri) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩options%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap, uri);// 压缩好比例大小后再进行质量压缩
	}

	/**
	 * 按比例进行压缩图片
	 * @param image
	 * @param uri
	 * @param isCache
	 * @return
	 */
	public static Bitmap compXY(Bitmap image, final String uri, boolean isCache) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		int options = 100;
		while (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		final Bitmap newBitmap = bitmap;
		if (isCache) {
					File iFile = new File(uri);
					try {
						FileOutputStream out = new FileOutputStream(iFile);
						newBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
						out.flush();
						out.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
		}
		return newBitmap;// 压缩好比例大小后再进行质量压缩
	}





	public static Bitmap decodeFile(String path) {
		Bitmap bm = null;
		BitmapFactory.Options bfOptions = new BitmapFactory.Options();
		bfOptions.inDither = false;
		bfOptions.inPurgeable = true;
		bfOptions.inInputShareable = true;
		bfOptions.inTempStorage = new byte[32 * 1024];

		File file = new File(path);
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			if (fs != null)
				bm = BitmapFactory.decodeFileDescriptor(fs.getFD(), null,
						bfOptions);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fs != null) {
				try {
					fs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bm;
	}

	/**
	 * 压缩图片
	 * 默认最大尺寸：1920*1080
	 * 默认大小：小于200KB
	 *
	 * @param photoPath 原图片路径
	 * @return 压缩后的图片路径
	 */
	public static String doCompressPhoto(String photoPath) {
		try
		{
			BitmapFactory.Options options = new BitmapFactory.Options();
			// inJustDecodeBounds设置为true,这样使用该option decode出来的Bitmap是null，
			// 只是把长宽存放到option中
			options.inJustDecodeBounds = true;
			options.inDither = true;
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			// 此时bitmap为null
			BitmapFactory.decodeFile(photoPath,options);
			int inSampleSize = 1; // 1表示不缩放
			// 计算宽高缩放比例
			int inSampleSizeW = options.outWidth / 1920;
			int inSampleSizeH = options.outHeight / 1080;
			if (inSampleSizeW > inSampleSizeH) {
				inSampleSize = inSampleSizeW;
			}else {
				inSampleSize = inSampleSizeH;
			}
			// 设置缩放比例
			options.inSampleSize = inSampleSize;
			// 一定要记得将inJustDecodeBounds设为false，否则Bitmap为null
			options.inJustDecodeBounds = false;

			Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			int quality = 100;
			bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
			while (baos.toByteArray().length / 1024 > 200 && quality > 6) {
				baos.reset();
				quality -= 6;
				bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
			}
			bitmap.recycle();
			//			L.w(String.valueOf(baos.toByteArray().length));

			FileOutputStream fos = new FileOutputStream(photoPath);
			fos.write(baos.toByteArray());
			fos.flush();
			fos.close();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return photoPath;
	}
}
