package com.jude.library.imageprovider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import com.jude.library.imageprovider.net.NetImage;
import com.jude.library.imageprovider.net.NetImageSearchActivity;

import java.io.File;

/**
 * Created by Mr.Jude on 2015/3/15.
 */
public class ImageProvider {

    private String tempImagePath ;

    private Activity act;

    private OnImageSelectListener mListener;

    private static final int REQUEST_CAMERA = 12580;
    private static final int REQUEST_ALBUM = 12581;
    private static final int REQUEST_NET = 12582;

    public enum Searcher{
        Baidu,SOSO,HuaBan
    }

    public ImageProvider(Activity act){
        this.act = act;
    }

    public void getImageFromAlbum(OnImageSelectListener mListener){
        this.mListener = mListener;
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        act.startActivityForResult(intent, REQUEST_ALBUM);
    }

    public void getImageFromCamera(OnImageSelectListener mListener){
        this.mListener = mListener;
        tempImagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/"+System.currentTimeMillis()+".jpg";
        Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraintent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(tempImagePath)));
        act.startActivityForResult(cameraintent, REQUEST_CAMERA);
        Log.i("GodLog", mListener == null ? "bnull" : "bfuckvalue");
    }

    public void getImageFromNet(Searcher Searcher,OnImageSelectListener mListener){
        this.mListener = mListener;
        Intent intent = new Intent(act,NetImageSearchActivity.class);
        intent.putExtra(NetImageSearchActivity.Key_seacher, Searcher);
        act.startActivityForResult(intent, REQUEST_NET);
    }

    public void onActivityResult(int requestCode, int resultCode, final Intent data){
        Log.i("GodLog",resultCode+"--"+(mListener==null?"null":"fuckvalue"));
        if (resultCode != act.RESULT_OK) return ;
        if (mListener == null) return;
        Log.i("GodLog",requestCode+"");
        switch (requestCode){
            case REQUEST_CAMERA:
                mListener.onImageSelect(Uri.fromFile(new File(tempImagePath)));
                break;
            case REQUEST_ALBUM:
                mListener.onImageSelect(data.getData());
                break;
            case REQUEST_NET:
                final NetImage netImage = (NetImage) data.getSerializableExtra("data");
                mListener.onImageSelect(null);
                break;
        }
    }

}
