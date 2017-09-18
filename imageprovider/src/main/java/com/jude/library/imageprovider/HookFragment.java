package com.jude.library.imageprovider;

import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by Jude on 2017/7/2.
 */

public class HookFragment extends Fragment {

    private OnActivityResultListener listener;

    void startActivityForResult(int requestCode, Intent intent, OnActivityResultListener listener){
        startActivityForResult(intent, requestCode);
        this.listener = listener;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        listener.onActivityResult(requestCode, resultCode, data);
    }
}
