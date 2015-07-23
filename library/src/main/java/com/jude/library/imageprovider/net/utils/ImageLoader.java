package com.jude.library.imageprovider.net.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

import java.io.IOException;
import java.net.URL;

/**
 * Created by zhuchenxi on 15/7/23.
 */
public class ImageLoader {
    private static ImageLoader instance = new ImageLoader();
    private ImageLoader(){};
    public static ImageLoader getInstance(){return instance;}
    private ImageCache cache;
    private Context ctx;
    public void init(Context ctx){
        this.ctx = ctx;
        cache = new ImageCache(ctx);
    }

    public void image(final String url, final ImageCallback callback){
        final Handler handler = new Handler();
        Bitmap bitmap = cache.get(url);
        if (bitmap != null)callback.success(bitmap);
        else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final Bitmap bitmap = BitmapFactory.decodeStream(new URL(url).openStream());
                        cache.putBitmap(url, bitmap);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.success(bitmap);
                            }
                        });
                    } catch (IOException e) {
                        callback.error();
                    }
                }
            }).start();
        }
    }

    public interface ImageCallback{
        void success(Bitmap bitmap);
        void error();
    }
}
