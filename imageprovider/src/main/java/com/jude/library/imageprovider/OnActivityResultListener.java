package com.jude.library.imageprovider;

import android.content.Intent;

/**
 * Created by zhuchenxi on 2017/1/17.
 */

public interface OnActivityResultListener {

    void onActivityResult(int requestCode, int resultCode, final Intent data);
}
