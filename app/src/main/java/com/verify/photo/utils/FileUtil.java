package com.verify.photo.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.widget.Toast;

import com.verify.photo.config.Constants;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtil {

	private final String TAG = "FileUtil";

	// 获取文件的保存路径
	public static File getFile(String filePath) throws Exception {
		File rootPath = new File(Constants.SDCARD_PATH);
		if(!rootPath.exists()){
			rootPath.mkdirs();
		}
		File file = new File(filePath);
		if (!file.exists()) {
			file.createNewFile();
		}
		return file;
	}
	/**
	 * 	判断文件是否存在
	 */
	public boolean FileIsExists(String strFile)
	{
		try
		{
			File f=new File(strFile);
			if(!f.exists())
			{
				return false;
			}
		}
		catch (Exception e)
		{
			return false;
		}

		return true;
	}
	/**
	 * SD卡下创建文件夹
	 *
	 */
	public static boolean createFolder(String path, Context mContext) {
		boolean isSuccess = false;
		File file = new File(path);
		if (!file.exists())
			file.mkdir();
		isSuccess = true;
		if (!file.exists()) {
			isSuccess = false;
			if (mContext != null)
				Toast.makeText(mContext, "未检测到SD卡,某些功能可能无法正常使用!",
						Toast.LENGTH_LONG).show();
		}

		return isSuccess;
	}

	/**
	 * 删除指定的文件
	 * 
	 */
	public static void deleteFile(String filepath) {
		File file = new File(filepath);
		if (file.isDirectory()) {
			String[] files = file.list();
			for (int i = 0; i < files.length; i++) {
				new File(filepath, files[i]).delete();
			}
		} else {
			file.delete();
		}
	}

	public static List<File> readAllFilesInDir(File file) {
		ArrayList<File> mFileList = new ArrayList<File>();
		File[] fileArray = file.listFiles();
		if (fileArray != null && fileArray.length > 0) {
			for (File f : fileArray) {
				if (f.isFile()) {
					mFileList.add(f);
				}
			}
		}
		return mFileList;
	}

	/**
	 * 保存指定的data内容到指定的文件中
	 * 
	 * @param data
	 * @param fileName
	 */
	public static void saveFile(byte[] data, String fileName) {
		OutputStream os = null;
		try {
			File dir = new File(Constants.SDCARD_PATH);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(Constants.SDCARD_PATH + fileName);
			if (!file.exists()) {
				file.createNewFile();
			}

			os = new FileOutputStream(file);
			os.write(data, 0, data.length);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	public byte[] readImageByte(String imagePath) {
		byte[] tmp = new byte[4096];
		byte[] result = null;
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		InputStream in = null;
		try {
			in = new FileInputStream(imagePath);
			int len;
			while ((len = in.read(tmp)) != -1) {
				buffer.write(tmp, 0, len);
			}
			result = buffer.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
				buffer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public Bitmap readImageFile(String path, int maxLength) {
		Options opts = new Options();
		opts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(path, opts);
		opts.inJustDecodeBounds = false;
		int srcWidth = opts.outWidth;
		int srcHeight = opts.outHeight;
		float ratio = 0;
		float destWidth = 0;
		float destHeight = 0;

		if (srcWidth > srcHeight) {
			ratio = srcWidth / maxLength;
			destWidth = maxLength;
			destHeight = (int) (srcHeight / ratio);
		} else {
			ratio = srcHeight / maxLength;
			destHeight = maxLength;
			destWidth = (int) (srcWidth / ratio);

		}
		if (ratio <= 0) {
			ratio = 1;
		}
		int be = (int) ratio;
		opts.inSampleSize = be % 2 == 0 ? be : be + 1;
		bitmap = BitmapFactory.decodeFile(path, opts);
		srcWidth = bitmap.getWidth();
		srcHeight = bitmap.getHeight();
		destWidth = (float) maxLength / srcWidth;
		destHeight = (float) maxLength / srcHeight;
		if (destWidth != 1.0 && destHeight != 1.0) {
			Matrix matrix = new Matrix();
			matrix.postScale(destWidth, destHeight);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, srcWidth, srcHeight,
					matrix, false);
		}
		return bitmap;
	}

	public static Bitmap readImageFileRatio(String path, int maxLength) {
		Options opts = new Options();
		opts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(path, opts);
		opts.inJustDecodeBounds = false;
		int srcWidth = opts.outWidth;
		int srcHeight = opts.outHeight;
		float ratio = 0;
		float destWidth = 0;
		float destHeight = 0;

		if (srcWidth > srcHeight) {
			ratio = srcWidth / maxLength;
			destWidth = maxLength;
			destHeight = (int) (srcHeight / ratio);
		} else {
			ratio = srcHeight / maxLength;
			destHeight = maxLength;
			destWidth = (int) (srcWidth / ratio);

		}
		if (ratio <= 0) {
			ratio = 1;
		}
		int be = (int) ratio;
		opts.inSampleSize = be % 2 == 0 ? be : be + 1;
		bitmap = BitmapFactory.decodeFile(path, opts);
		srcWidth = bitmap.getWidth();
		srcHeight = bitmap.getHeight();
		destWidth = (float) maxLength / srcWidth;
		destHeight = (float) maxLength / srcHeight;
		// if (destWidth != 1.0 && destHeight != 1.0) {
		// Matrix matrix = new Matrix();
		// matrix.postScale(destWidth, destHeight);
		// bitmap = Bitmap.createBitmap(bitmap, 0, 0, srcWidth,
		// srcHeight,matrix, false);
		// }
		return bitmap;
	}

	public static Bitmap compressImage(String path, int maxWidth, int maxHeight) {
		Options newOpts = new Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(path, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > maxWidth) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / maxWidth);
		} else if (w < h && h > maxHeight) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / maxHeight);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(path, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	public static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(CompressFormat.JPEG, 50, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	public static Bitmap compressImage(String path, int be) {
		Options newOpts = new Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(path, newOpts);// 此时返回bm为空

		// newOpts.inDither = false;
		newOpts.inPreferredConfig = Config.RGB_565;

		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		newOpts.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(path, newOpts);
		return bitmap;
	}

	/**
	 *
	 * 图片的缩放方法 *
	 *
	 * @param bgimage
	 *            ：源图片资源
	 * @param newWidth
	 *            ：缩放后宽度
	 * @param newHeight
	 *            ：缩放后高度
	 * @return
	 */
	public Bitmap zoomBitmap(String path) {

		Bitmap bgimage = readImageFile(path, 160);

		// 获取这个图片的宽和高

		int width = bgimage.getWidth();

		int height = bgimage.getHeight();

		// 创建操作图片用的matrix对象

		Matrix matrix = new Matrix();

		// 计算缩放率，新尺寸除原始尺寸

		float scaleWidth = ((float) 160) / width;

		float scaleHeight = ((float) 160) / height;

		// 缩放图片动作

		matrix.postScale(scaleWidth, scaleHeight);

		Bitmap bitmap2 = Bitmap.createBitmap(bgimage, 0, 0, width, height,
				matrix, true);
		bgimage.recycle();
		return bitmap2;

	}

	/**
	 * 水印制作方法
	 *
	 * @param src
	 *            原图
	 * @param wmsrc
	 *            水印图片
	 * @return
	 */
	public Bitmap createBitMap(Bitmap src, Bitmap wmsrc) {
		// 画图
		if (src == null) {
			return null;
		}
		int w = src.getWidth();
		int h = src.getHeight();
		int wmw = wmsrc.getWidth();
		int wmh = wmsrc.getHeight();
		// create the new bitmap
		Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个底图
		Canvas cv = new Canvas(newb);
		// 将底图画进去
		cv.drawBitmap(src, 0, 0, null);// 在0,0坐标开始画入src
		// 讲水印画进去
		cv.drawBitmap(wmsrc, w - wmw + 5, h - wmh + 5, null);
		// 保存图片
		cv.save(Canvas.ALL_SAVE_FLAG);
		cv.restore();
		return newb;

	}

	/**
	 * 水印制作方法
	 *
	 * @param src
	 *            原图
	 * @param wmsrc
	 *            水印图片
	 * @return
	 */
	public static Bitmap createWatermark(Bitmap src, String wmsrc) {
		// 画图
		if (src == null) {
			return null;
		}
		int w = src.getWidth();
		int h = src.getHeight();
		// 设置字体样式
		Paint paint = new Paint();
		String familyName = "宋体";
		Typeface font = Typeface.create(familyName, Typeface.BOLD);
		Log.i("TAG", "字体");
		paint.setColor(android.graphics.Color.YELLOW);
		paint.setTypeface(font);
		paint.setTextSize(16);

		// create the new bitmap
		Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个底图
		Canvas cv = new Canvas(newb);
		// 将底图画进去
		cv.drawBitmap(src, 0, 0, null);// 在0,0坐标开始画入src
		// 讲水印画进去
		cv.drawText(wmsrc, 10, h - 10, paint);
		// 保存图片
		cv.save(Canvas.ALL_SAVE_FLAG);
		cv.restore();
		// src.recycle();
		return newb;
	}

	/**
	 * 在图片上添加水印（支持多行String）
	 *
	 * @param src
	 *            原图片
	 * @param lines
	 *            String数组
	 * @return 添加水印后的图片
	 */
	public static Bitmap createWatermark(Bitmap src, List<String> lines) {
		// 画图
		if (src == null) {
			return null;
		}
		int w = src.getWidth();
		int h = src.getHeight();
		// 设置字体样式
		TextPaint textPaint = new TextPaint();
		String familyName = "宋体";
		Typeface font = Typeface.create(familyName, Typeface.BOLD);
		Log.i("TAG", "字体");
		textPaint.setColor(android.graphics.Color.YELLOW);
		textPaint.setTypeface(font);
		textPaint.setTextSize(12);
		// int textHeight = (int)Math.ceil(textPaint.descent() -
		// textPaint.ascent());
		// create the new bitmap
		Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个底图
		Canvas canvas = new Canvas(newb);
		// 将底图画进去
		canvas.drawBitmap(src, 0, 0, null);// 在0,0坐标开始画入src

		for (int i = 0; i < lines.size(); i++) {
			StaticLayout layout = new StaticLayout(lines.get(i), textPaint, w,
					Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
			if (i == 0) {
				canvas.translate(0, h - layout.getHeight());
			} else {
				canvas.translate(0, -layout.getHeight());
			}
			layout.draw(canvas);
		}
		// cv.drawText(wmsrc, 10, h - 10, paint);
		// 保存图片
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		// src.recycle();
		return newb;
	}

	public static Bitmap compressImagePriview(String path, int maxWidth,
                                              int maxHeight) {
		Options newOpts = new Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(path, newOpts);// 此时返回bm为空
		newOpts.inPreferredConfig = Config.RGB_565;

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > maxWidth) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / maxWidth);
		} else if (w < h && h > maxHeight) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / maxHeight);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(path, newOpts);
		return bitmap;
	}

	/*
	 * 这个是解压ZIP格式文件的方法
	 *
	 * @zipFileName：是传进来你要解压的文件路径，包括文件的名字；
	 *
	 * @outputDirectory:选择你要保存的路劲；
	 */
	public void unzip(String zipFileName, String outputDirectory)
			throws Exception {

		InputStream fis = new FileInputStream(new File(zipFileName));
		ZipInputStream in = new ZipInputStream(new BufferedInputStream(fis));
		ZipEntry z = null; // 每个zip条目的实例
		String name = null; // 保存每个zip的条目名称

		while ((z = in.getNextEntry()) != null) {
			name = z.getName();
			Log.d(TAG, "unzipping file: " + name);
			if (z.isDirectory()) {
				Log.d(TAG, name + "is a folder");
				// get the folder name of the widget
				name = name.substring(0, name.length() - 1);
				File folder = new File(outputDirectory + File.separator + name);
				folder.mkdirs();
				Log.d(TAG, "mkdir " + outputDirectory + File.separator + name);
			} else {
				Log.d(TAG, name + "is a normal file");
				File file = new File(outputDirectory + File.separator + name);
				file.createNewFile();
				// get the output stream of the file
				FileOutputStream out = new FileOutputStream(file);
				int ch = -1;
				byte[] buffer = new byte[1024];
				// read (ch) bytes into buffer
				while ((ch = in.read(buffer)) != -1) {
					// write (ch) byte from buffer at the position 0
					out.write(buffer, 0, ch);
					out.flush();
				}
				out.close();
			}
		}
		in.close();

	}

	// 删除文件夹
	// param folderPath 文件夹完整绝对路径
	public void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 删除指定文件夹下所有文件
	// param path 文件夹完整绝对路径
	public boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	public static String inputStream2String(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString();
	}

	public static void saveBitmapToFile(Bitmap bmp, String fileAbsolutePath)
			throws FileNotFoundException {
		CompressFormat format = CompressFormat.PNG;
		int quality = 100;
		OutputStream stream = new FileOutputStream(fileAbsolutePath);
		bmp.compress(format, quality, stream);
	}


	/**
	 * 读取一个缩放后的图片，限定图片大小，避免OOM
	 * @param uri       图片uri，支持“file://”、“content://”
	 * @param maxWidth  最大允许宽度
	 * @param maxHeight 最大允许高度
	 * @return  返回一个缩放后的Bitmap，失败则返回null
	 */
	public static Bitmap decodeUri(Context context, Uri uri, int maxWidth, int maxHeight) {
		Options options = new Options();
		options.inJustDecodeBounds = true; //只读取图片尺寸
		resolveUri(context, uri, options);

		//计算实际缩放比例
		int scale = 1;
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			if ((options.outWidth / scale > maxWidth &&
					options.outWidth / scale > maxWidth * 1.4) ||
					(options.outHeight / scale > maxHeight &&
							options.outHeight / scale > maxHeight * 1.4)) {
				scale++;
			} else {
				break;
			}
		}

		options.inSampleSize = scale;
		options.inJustDecodeBounds = false;//读取图片内容
		options.inPreferredConfig = Config.RGB_565; //根据情况进行修改
		Bitmap bitmap = null;
		try {
			bitmap = resolveUriForBitmap(context, uri, options);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	private static void resolveUri(Context context, Uri uri, Options options) {
		if (uri == null) {
			return;
		}

		String scheme = uri.getScheme();
		if (ContentResolver.SCHEME_CONTENT.equals(scheme) ||
				ContentResolver.SCHEME_FILE.equals(scheme)) {
			InputStream stream = null;
			try {
				stream = context.getContentResolver().openInputStream(uri);
				BitmapFactory.decodeStream(stream, null, options);
			} catch (Exception e) {
				Log.w("resolveUri", "Unable to open content: " + uri, e);
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						Log.w("resolveUri", "Unable to close content: " + uri, e);
					}
				}
			}
		} else if (ContentResolver.SCHEME_ANDROID_RESOURCE.equals(scheme)) {
			Log.w("resolveUri", "Unable to close content: " + uri);
		} else {
			Log.w("resolveUri", "Unable to close content: " + uri);
		}
	}

	private static Bitmap resolveUriForBitmap(Context context, Uri uri, Options options) {
		if (uri == null) {
			return null;
		}

		Bitmap bitmap = null;
		String scheme = uri.getScheme();
		if (ContentResolver.SCHEME_CONTENT.equals(scheme) ||
				ContentResolver.SCHEME_FILE.equals(scheme)) {
			InputStream stream = null;
			try {
				stream = context.getContentResolver().openInputStream(uri);
				bitmap = BitmapFactory.decodeStream(stream, null, options);
			} catch (Exception e) {
				Log.w("resolveUriForBitmap", "Unable to open content: " + uri, e);
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						Log.w("resolveUriForBitmap", "Unable to close content: " + uri, e);
					}
				}
			}
		} else if (ContentResolver.SCHEME_ANDROID_RESOURCE.equals(scheme)) {
			Log.w("resolveUriForBitmap", "Unable to close content: " + uri);
		} else {
			Log.w("resolveUriForBitmap", "Unable to close content: " + uri);
		}

		return bitmap;
	}


	/**
	 * 复制单个文件
	 *
	 * @param oldPathName String 原文件路径+文件名 如：data/user/0/com.test/files/abc.txt
	 * @param newPathName String 复制后路径+文件名 如：data/user/0/com.test/cache/abc.txt
	 * @return <code>true</code> if and only if the file was copied;
	 * <code>false</code> otherwise
	 */
	public  static boolean copyFile(String oldPathName, String newPathName) {
		try {
			File oldFile = new File(oldPathName);
			if (!oldFile.exists()) {
				Log.e("--Method--", "copyFile:  oldFile not exist.");
				return false;
			} else if (!oldFile.isFile()) {
				Log.e("--Method--", "copyFile:  oldFile not file.");
				return false;
			} else if (!oldFile.canRead()) {
				Log.e("--Method--", "copyFile:  oldFile cannot read.");
				return false;
			}

        /* 如果不需要打log，可以使用下面的语句
        if (!oldFile.exists() || !oldFile.isFile() || !oldFile.canRead()) {
            return false;
        }
        */

			FileInputStream fileInputStream = new FileInputStream(oldPathName);    //读入原文件
			FileOutputStream fileOutputStream = new FileOutputStream(newPathName);
			byte[] buffer = new byte[1024];
			int byteRead;
			while ((byteRead = fileInputStream.read(buffer)) != -1) {
				fileOutputStream.write(buffer, 0, byteRead);
			}
			fileInputStream.close();
			fileOutputStream.flush();
			fileOutputStream.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
