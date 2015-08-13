package com.jude.library.imageprovider;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class Utils {
	public static String TAG;

	private static Context mApplicationContent;
	public static void initialize(Application app,String TAG){
		mApplicationContent = app.getApplicationContext();
		Utils.TAG = TAG;
	}

	/**
	 * dp转px
	 *
	 */
	public static int dip2px(float dpValue) {
		final float scale = mApplicationContent.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}


	/**
	 *	px转dp
	 */
	public static int px2dip(float pxValue) {
		final float scale = mApplicationContent.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}


	public static void Log(String text){
		Log.i(TAG, text);
	}

	/**
	 * 取屏幕宽度
	 * @return
	 */
	public static int getScreenWidth(){
		DisplayMetrics dm = mApplicationContent.getResources().getDisplayMetrics();
		return dm.widthPixels;
	}

	/**
	 * 取屏幕高度
	 * @return
	 */
	public static int getScreenHeight(){
		DisplayMetrics dm = mApplicationContent.getResources().getDisplayMetrics();

		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, sbar = 0;
		try {
		    c = Class.forName("com.android.internal.R$dimen");
		    obj = c.newInstance();
		    field = c.getField("status_bar_height");
		    x = Integer.parseInt(field.get(obj).toString());
		    sbar = mApplicationContent.getResources().getDimensionPixelSize(x);
		} catch(Exception e1) {
		}

		return dm.heightPixels-sbar;
	}

	/**
	 * 保存图片
	 * @param bitmap
	 * @param path
	 */
	public static void BitmapSave(Bitmap bitmap, String path){
		File file = new File(path);
		try {
			if (!file.exists()) {
				new File(path.substring(0, path.lastIndexOf('/'))).mkdirs();
			}else{
				file.delete();
			}
			file.createNewFile();
			BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
			os.flush();
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭输入法
	 * @param act
	 */
	public static void closeInputMethod(Activity act){
		View view = act.getCurrentFocus();
		if(view!=null){
			((InputMethodManager)act.getSystemService(Context.INPUT_METHOD_SERVICE)).
			hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

    public static String sendGet(String url, String param) throws Exception {
        String result = "";
        BufferedReader in = null;

		String urlNameString;
		if (param==null||param.isEmpty())urlNameString = url;
		else urlNameString = url + "?" + param;
		URL realUrl = new URL(urlNameString);
		// 打开和URL之间的连接
		URLConnection connection = realUrl.openConnection();
		// 设置通用的请求属性
		connection.setRequestProperty("accept", "*/*");
		connection.setRequestProperty("connection", "Keep-Alive");
		connection.setRequestProperty("user-agent",
				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		// 建立实际的连接
		connection.connect();
		// 获取所有响应头字段
		Map<String, List<String>> map = connection.getHeaderFields();
		// 遍历所有的响应头字段
		for (String key : map.keySet()) {
			System.out.println(key + "--->" + map.get(key));
		}
		// 定义 BufferedReader输入流来读取URL的响应
		in = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		String line;
		while ((line = in.readLine()) != null) {
			result += line;
		}
		in.close();
        return result;
    }

	public static Bitmap readBitmapAutoSize(String filePath, int outWidth, int outHeight) {
		//outWidth和outHeight是目标图片的最大宽度和高度，用作限制
		FileInputStream fs = null;
		BufferedInputStream bs = null;
		try {
			fs = new FileInputStream(filePath);
			bs = new BufferedInputStream(fs);
			BitmapFactory.Options options = setBitmapOption(filePath, outWidth, outHeight);
			return BitmapFactory.decodeStream(bs, null, options);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bs.close();
				fs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static BitmapFactory.Options setBitmapOption(String file, int width, int height) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		//设置只是解码图片的边距，此操作目的是度量图片的实际宽度和高度
		BitmapFactory.decodeFile(file, opt);

		int outWidth = opt.outWidth; //获得图片的实际高和宽
		int outHeight = opt.outHeight;
		opt.inDither = false;
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		//设置加载图片的颜色数为16bit，默认是RGB_8888，表示24bit颜色和透明通道，但一般用不上
		opt.inSampleSize = 1;
		//设置缩放比,1表示原比例，2表示原来的四分之一....
		//计算缩放比
		if (outWidth != 0 && outHeight != 0 && width != 0 && height != 0) {
			int sampleSize = (outWidth / width + outHeight / height) / 2;
			opt.inSampleSize = sampleSize;
		}

		opt.inJustDecodeBounds = false;//最后把标志复原
		return opt;
	}

}
