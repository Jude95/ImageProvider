package com.jude.library.imageprovider;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.BufferedReader;
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

    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
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
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

}
