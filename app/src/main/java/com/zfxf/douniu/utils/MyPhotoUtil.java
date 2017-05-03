package com.zfxf.douniu.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;

/***
 * 
 * 头像上传工具类 调用 getPhoto 在onactivityResult 调用
 * 
 * onPhotoFromCamera
 * 
 * onPhotoFromPick
 */
public class MyPhotoUtil {
	//定义图片的Uri
	public static Uri photoUri;
	//图片文件路径
	public static String picPath;

	/**临时存放图片的地址，如需修改，请记得创建该路径下的文件夹*/
	private static final String lsimg = "file:///sdcard/temp.jpg";
	/**
	 * 拍照获取图片
	 */
	public static void picTyTakePhoto(Activity context, int TAKE_PHOTO_CODE) {
		//判断SD卡是否存在
		String SDState = Environment.getExternalStorageState();
		if (SDState.equals(Environment.MEDIA_MOUNTED)) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//"android.media.action.IMAGE_CAPTURE"
			/***
			 * 使用照相机拍照，拍照后的图片会存放在相册中。使用这种方式好处就是：获取的图片是拍照后的原图，
			 * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图有可能不清晰
			 */
			ContentValues values = new ContentValues();
			photoUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
			context.startActivityForResult(intent, TAKE_PHOTO_CODE);
		} else {
			Toast.makeText(context, "内存卡不存在", Toast.LENGTH_LONG).show();
		}
	}

	/***
	 * 从相册中取图片
	 */
	public static void pickPhoto(Activity context, int SELECT_PIC_CODE) {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		context.startActivityForResult(intent, SELECT_PIC_CODE);
	}
	public static void onPhotoFromCamera(Activity context, Intent data, int REQUE_CODE_CROP){
		String[] pojo = {MediaStore.Images.Media.DATA};
		CursorLoader loader = new CursorLoader(context, photoUri, pojo, null, null, null);
		Cursor cursor = loader.loadInBackground();
		if (cursor != null) {
			int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
			cursor.moveToFirst();
			picPath = cursor.getString(columnIndex);
			if (Build.VERSION.SDK_INT < 14) {
				cursor.close();
			}
		}
		if (picPath != null) {
			photoUri = Uri.fromFile(new File(picPath));
			startPhotoZoom(context,photoUri, REQUE_CODE_CROP);
		} else {
			Toast.makeText(context, "图片选择失败", Toast.LENGTH_LONG).show();
		}

	}
	public static void onPhotoFromPick(Activity context, Intent data, int REQUE_CODE_CROP){
		if (null != data && null != data.getData()) {
			photoUri = data.getData();
			picPath = uriToFilePath(context,photoUri);
			startPhotoZoom(context,photoUri, REQUE_CODE_CROP);
		} else {
			Toast.makeText(context, "图片选择失败", Toast.LENGTH_LONG).show();
		}
	}

	private static String uriToFilePath(Activity context, Uri uri) {
		//获取图片数据
		if(uri.toString().startsWith("file")){
			return uri.toString().substring(7);//小米手机直接返回的是地址，不需要cursor转换
		}
		String[] proj = {MediaStore.Images.Media.DATA};
		CursorLoader loader = new CursorLoader(context, uri, proj, null, null, null);
		Cursor cursor = loader.loadInBackground();
		//获得用户选择的图片的索引值
		int image_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		//返回图片路径
		return cursor.getString(image_index);
	}
	/**
	 * @param
	 * @description 裁剪图片
	 */
	private static void startPhotoZoom(Activity context, Uri uri, int REQUE_CODE_CROP) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// 去黑边
		intent.putExtra("scale", true);
		intent.putExtra("scaleUpIfNeeded", true);
		// aspectX aspectY 是宽高的比例，根据自己情况修改
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高像素
		intent.putExtra("outputX", 600);
		intent.putExtra("outputY", 600);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		//取消人脸识别功能
		intent.putExtra("noFaceDetection", true);
		//设置返回的uri
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse(lsimg));//该为临时路径
		//设置为不返回数据
		intent.putExtra("return-data", false);
		context.startActivityForResult(intent, REQUE_CODE_CROP);
	}

	public static Bitmap getZoomBitMap(Context context) {
		Uri uri = Uri.parse(lsimg);
		if (photoUri != null) {
//			Bitmap bitmap = BitmapFactory.decodeFile(picPath);
			Bitmap bitmap = null;
			try {
				bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			if (bitmap != null) {
				return bitmap;
			}
		}
		return null;
	}

	/**
	 * 获取缓存的图片信息
	 * @param context
	 * @param str
     * @return
     */
	public static Bitmap getCacheFile(Context context , String str){
		//把ivUrl转换MD5值，再把md5 做文件名
		File file = new File(context.getFilesDir(),str);
		if (file != null  && file.exists()) {
			//文件存在
			//把文件转换成bitmap
			Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
			return bitmap;
		} else {
			//不存在
			return null;
		}
	}
}
