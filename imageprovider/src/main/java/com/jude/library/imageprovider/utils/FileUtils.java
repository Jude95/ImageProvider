package com.jude.library.imageprovider.utils;

import android.content.Context;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 文件操作类
 * Created by Nereo on 2015/4/8.
 */
public class FileUtils {

    public static File createTmpFile(Context context){

        File cacheDir = context.getCacheDir();
        File imageDir = new File(cacheDir,"image-provider");
        if (!imageDir.exists()){
            imageDir.mkdir();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String fileName = "multi_image_"+timeStamp+"";
        File tmpFile = new File(imageDir, fileName+".jpg");
        return tmpFile;
    }

}
