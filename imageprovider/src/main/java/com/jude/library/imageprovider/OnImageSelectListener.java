package com.jude.library.imageprovider;

import android.net.Uri;

/**
 * Created by Mr.Jude on 2015/3/29.
 */
public interface OnImageSelectListener {
    void onImageSelect();
    void onImageLoaded(Uri uri);
    void onError();
}
