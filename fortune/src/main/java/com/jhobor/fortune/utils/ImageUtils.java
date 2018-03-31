package com.jhobor.fortune.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
/**
 * 拍照/相册选取/图片压缩
 * @author yaozu
 *
 */
public class ImageUtils {

	public static final int GET_IMAGE_BY_CAMERA = 5001;
	public static final int GET_IMAGE_FROM_PHONE = 5002;
	public static final int CROP_IMAGE = 5003;
	private static final String TAG = ImageUtils.class.getSimpleName();
	public static Uri imageUriFromCamera;
	public static Uri cropImageUri;
	
	/**
	 * 拍照
	 * @param activity
	 */
	public static void openCameraImage(final Activity activity) {
		ImageUtils.imageUriFromCamera = ImageUtils.createImagePathUri(activity);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// MediaStore.EXTRA_OUTPUT参数不设置时,系统会自动生成一个uri,但是只会返回一个缩略图
		// 返回图片在onActivityResult中通过以下代码获取
		// Bitmap bitmap = (Bitmap) data.getExtras().get("data");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageUtils.imageUriFromCamera);
		activity.startActivityForResult(intent, ImageUtils.GET_IMAGE_BY_CAMERA);
	}

	/**
	 * 获取本地图片
	 * @param activity
	 */
	public static void openLocalImage(final Activity activity) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		activity.startActivityForResult(intent, ImageUtils.GET_IMAGE_FROM_PHONE);
	}

	/**
	 * 剪辑图片的大小
	 * 默认输出 150*150
	 * @param activity
	 * @param srcUri
	 */
	public static void cropImage(Activity activity, Uri srcUri) {
		/**
		 * 应对三星设备图片拍照默认横屏的问题处理
		*/
		/*String path = getRealPathByURI(srcUri, fragment.getActivity());
		int degree = readBitmapDegree(path);
		Bitmap bm = rotateBitmapByDegree(BitmapFactory.decodeFile(path), degree);
		saveBitmapToLocal(bm, path);*/
		
		
		ImageUtils.cropImageUri = ImageUtils.createImagePathUri(activity);

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(srcUri, "image/*");
		intent.putExtra("crop", "true");

		// //////////////////////////////////////////////////////////////
		// 1.宽高和比例都不设置时,裁剪框可以自行调整(比例和大小都可以随意调整)
		// //////////////////////////////////////////////////////////////
		// 2.只设置裁剪框宽高比(aspect)后,裁剪框比例固定不可调整,只能调整大小
		// //////////////////////////////////////////////////////////////
		// 3.裁剪后生成图片宽高(output)的设置和裁剪框无关,只决定最终生成图片大小
		// //////////////////////////////////////////////////////////////
		// 4.裁剪框宽高比例(aspect)可以和裁剪后生成图片比例(output)不同,此时,
		// 会以裁剪框的宽为准,按照裁剪宽高比例生成一个图片,该图和框选部分可能不同,
		// 不同的情况可能是截取框选的一部分,也可能超出框选部分,向下延伸补足
		// //////////////////////////////////////////////////////////////

		// aspectX aspectY 是裁剪框宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪后生成图片的宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("scale", true); 
		// return-data为true时,会直接返回bitmap数据,但是大图裁剪时会出现问题,推荐下面为false时的方式
		// return-data为false时,不会返回bitmap,但需要指定一个MediaStore.EXTRA_OUTPUT保存图片uri
		intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageUtils.cropImageUri);
		intent.putExtra("return-data", false);

		activity.startActivityForResult(intent, CROP_IMAGE);
	}
	
	/**
	 * 根据尺寸剪辑图片
	 * @param activity
	 * @param srcUri
	 * @param height
	 * @param wdith
	 */
	public static void cropImageBySize(Activity activity, Uri srcUri,int height,int wdith) {
		ImageUtils.cropImageUri = ImageUtils.createImagePathUri(activity);
		
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(srcUri, "image/*");
		intent.putExtra("crop", "true");
		
		// aspectX aspectY 是裁剪框宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪后生成图片的宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("scale", true); 
		// return-data为true时,会直接返回bitmap数据,但是大图裁剪时会出现问题,推荐下面为false时的方式
		// return-data为false时,不会返回bitmap,但需要指定一个MediaStore.EXTRA_OUTPUT保存图片uri
		intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageUtils.cropImageUri);
		intent.putExtra("return-data", false);
		
		activity.startActivityForResult(intent, CROP_IMAGE);
	}
	/**
	 * 根据Uri获取路径
	 * @param contentUri
	 * @return
	 */
	public static String getRealPathByURI(Uri contentUri,Context context) {
		String res = null;
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(contentUri,
				proj, null, null, null);
		if (cursor.moveToFirst()) {
			;
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			res = cursor.getString(column_index);
		}
		cursor.close();
		return res;
	}

	/**
	 * 创建一条图片地址uri,用于保存拍照后的照片
	 * 
	 * @param context
	 * @return 图片的uri
	 */
	private static Uri createImagePathUri(Context context) {
		Uri imageFilePath = null;
		String status = Environment.getExternalStorageState();
		SimpleDateFormat timeFormatter = new SimpleDateFormat(
				"yyyyMMdd_HHmmss", Locale.CHINA);
		long time = System.currentTimeMillis();
		String imageName = timeFormatter.format(new Date(time));
		// ContentValues是我们希望这条记录被创建时包含的数据信息
		ContentValues values = new ContentValues(3);
		values.put(MediaStore.Images.Media.DISPLAY_NAME, imageName);
		values.put(MediaStore.Images.Media.DATE_TAKEN, time);
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
		if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
			imageFilePath = context.getContentResolver().insert(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		} else {
			imageFilePath = context.getContentResolver().insert(
					MediaStore.Images.Media.INTERNAL_CONTENT_URI, values);
		}
		Log.i("", "生成的照片输出路径：" + imageFilePath.toString());
		return imageFilePath;
	}

	/**
	 * 图片压缩
	 * 
	 * @param bmp
	 * @param file
	 */
	public static void compressBmpToFile(File file,int height,int width) {
		Bitmap bmp = decodeSampledBitmapFromFile(file.getPath(), height, width);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 100;
		bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
		/*while (baos.toByteArray().length / 1024 > 30) {
			baos.reset();
			if (options - 10 > 0) {
				options = options - 10;
				bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
			}
			if (options - 10 <= 0) {
				break;
			}
		}*/
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baos.toByteArray());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将图片变成bitmap
	 * 
	 * @param path
	 * @return
	 */
	public static Bitmap getImageBitmap(String path) {
		Bitmap bitmap = null;
		File file = new File(path);
		if (file.exists()) {
			bitmap = BitmapFactory.decodeFile(path);
			return bitmap;
		}
		return null;
	}

	
//=================================图片压缩方法===============================================
	
	/**
     * 质量压缩
     * @author ping 2015-1-5 下午1:29:58
     * @param image
     * @param maxkb
     * @return
     */
    public static Bitmap compressBitmap(Bitmap image,int maxkb) {
        //L.showlog(压缩图片);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        int options = 100;
        // 循环判断如果压缩后图片是否大于(maxkb)50kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > maxkb) { 
        	// 重置baos即清空baos
            baos.reset();
            if(options-10>0){
            	// 每次都减少10
            	options -= 10;
            }
            // 这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        // 把ByteArrayInputStream数据生成图片
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }
     
    /**
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
     * 
     * @param filepath
     * 			 图片路径
     * @param reqWidth
     *			所需图片压缩尺寸最小宽度
     * @param reqHeight
     *          所需图片压缩尺寸最小高度
     * @return
     */
    public static Bitmap decodeSampledBitmapFromFile(String filepath,int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filepath, options);
 
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filepath, options);
    }
 
    /**
     * 
     * @param bitmap
     * @param reqWidth
     * 			所需图片压缩尺寸最小宽度
     * @param reqHeight
     * 			所需图片压缩尺寸最小高度
     * @return
     */
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
         
        int targetheight = picheight;
        int targetwidth = picwidth;
        int inSampleSize = 1;
         
        if (targetheight > reqHeight || targetwidth > reqWidth) {
            while (targetheight  >= reqHeight
                    && targetwidth>= reqWidth) {
                inSampleSize += 1;
                targetheight = picheight/inSampleSize;
                targetwidth = picwidth/inSampleSize;
            }
        }
         
        Log.i("===","最终压缩比例:" +inSampleSize + "倍");
        Log.i("===", "新尺寸:" +  targetwidth + "*" +targetheight);
        return inSampleSize;
    }
    
    // 读取图像的旋转度
 	public static int readBitmapDegree(String path) {
 		int degree = 0;
 		try {
 			ExifInterface exifInterface = new ExifInterface(path);
 			int orientation = exifInterface.getAttributeInt(
 					ExifInterface.TAG_ORIENTATION,
 					ExifInterface.ORIENTATION_NORMAL);
 			switch (orientation) {
 			case ExifInterface.ORIENTATION_ROTATE_90:
 				degree = 90;
 				break;
 			case ExifInterface.ORIENTATION_ROTATE_180:
 				degree = 180;
 				break;
 			case ExifInterface.ORIENTATION_ROTATE_270:
 				degree = 270;
 				break;
 			}
 		} catch (IOException e) {
 			e.printStackTrace();
 		}
 		return degree;
 	}

 	
 	/**
 	 * 将图片按照某个角度进行旋转
 	 *
 	 * @param bm
 	 *            需要旋转的图片
 	 * @param degree
 	 *            旋转角度
 	 * @return 旋转后的图片
 	 */
 	public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
 	    Bitmap returnBm = null;
 	  
 	    // 根据旋转角度，生成旋转矩阵
 	    Matrix matrix = new Matrix();
 	    matrix.postRotate(degree);
 	    try {
 	        // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
 	        returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
 	    } catch (OutOfMemoryError e) {
 	    }
 	    if (returnBm == null) {
 	        returnBm = bm;
 	    }
 	    if (bm != returnBm) {
 	        bm.recycle();
 	    }
 	    return returnBm;
 	}
 	
 	/**
 	 * 
 	 * @param mBitmap
 	 * @param fileName
 	 */
 	public static void saveBitmapToLocal(Bitmap mBitmap,String fileName) {
		if(mBitmap != null){
				FileOutputStream fos = null;
				try {
					File file = new File(fileName);
					if(file.exists()){
						file.delete();
					}
					file.createNewFile();
					fos = new FileOutputStream(file);
					mBitmap.compress(Bitmap.CompressFormat.PNG, 80, fos);
					fos.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					
					try {
						if(fos != null){
							fos.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
		}
	}
 	
 	/**
 	 * 将下载下来的图片保存到SD卡或者本地.并返回图片的路径(包括文件命和扩展名)
 	 * @param context
 	 * @param bitName
 	 * @param mBitmap
 	 * @return
 	 */
	public static String saveBitmap(Context context,String bitName, Bitmap mBitmap) {
		String path = null;
		File f;
		if(mBitmap != null){
			if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
				f = new File(Environment.getExternalStorageDirectory() + File.separator +"images/");
				String fileName = Environment.getExternalStorageDirectory() + File.separator +"images/"+ bitName + ".png";
				path = fileName;
				FileOutputStream fos = null;
				try {
					if(!f.exists()){
						f.mkdirs();
					}
					File file = new File(fileName);
					file.createNewFile();
					fos = new FileOutputStream(file);
					mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
					fos.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					
					try {
						if(fos != null){
							fos.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}else{
				//本地存储路径
				f = new File(context.getFilesDir() + File.separator +"images/");
				Log.i(TAG, "本地存储路径:"+context.getFilesDir() + File.separator +"images/"+ bitName + ".png");
				path = context.getFilesDir() + File.separator +"images/"+ bitName + ".png";
				FileOutputStream fos = null;
				try {
					if(!f.exists()){
						f.mkdirs();
					}
					File file = new File(path);
					file.createNewFile();
					fos = new FileOutputStream(file);
					mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
					fos.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					try {
						if(fos != null){
							fos.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}
		}
		
		return path;
	}
	
	/**
	 * 删除图片
	 * @param context
	 * @param bitName
	 */
	public void deleteFile(Context context,String bitName) {  
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
			File dirFile = new File(Environment.getExternalStorageDirectory() + File.separator + "images/"+ bitName + ".png"); 
			if (!dirFile.exists()) {
				return;
			}
			
			dirFile.delete();
		} else {
			File f = new File(context.getFilesDir() + File.separator
					+ "images/" + bitName + ".png");
			if(!f.exists()){
				return;
			}
			f.delete();
		}
	}
	
}