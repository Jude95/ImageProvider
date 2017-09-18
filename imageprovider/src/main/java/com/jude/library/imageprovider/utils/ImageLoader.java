package com.jude.library.imageprovider.utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by Jude on 2017/7/2.
 */

public class ImageLoader {

    String path;
    Handler handler;
    static ExecutorService executorService = Executors.newFixedThreadPool(8);

    private ImageLoader(String path) {
        this.path = path;
        this.handler = new Handler();
    }

    public static ImageLoader load(String path) {
        return new ImageLoader(path);
    }

    public SizedImageLoader size(int width, int height){
        return new SizedImageLoader(width, height);
    }

    public class SizedImageLoader{
        int width,height;

        private SizedImageLoader(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public void into(final ImageView imageView){
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    final Bitmap bitmap = Utils.readBitmapAutoSize(path,width,height);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                        }
                    });
                }
            });
        }

    }

}
