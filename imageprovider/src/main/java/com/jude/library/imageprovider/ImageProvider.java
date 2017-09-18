package com.jude.library.imageprovider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.jude.library.imageprovider.album.MultiImageSelectorActivity;
import com.jude.library.imageprovider.corpimage.CropImageIntentBuilder;
import com.jude.library.imageprovider.utils.FileUtils;
import com.jude.library.imageprovider.utils.Utils;

import java.io.File;
import java.util.List;

/**
 * Created by Mr.Jude on 2015/3/15.
 */
public class ImageProvider{

    private Activity act;

    private OnImageSelectListener mListener;

    private static final int REQUEST_CAMERA = 12580;
    private static final int REQUEST_ALBUM = 12581;
    private static final int REQUEST_CORP = 12585;

    private File dir;
    private File tempImage;


    public static ImageProvider from(Context ctx){
        if (ctx instanceof Activity){
            return  new ImageProvider((Activity) ctx);
        }
        throw new IllegalArgumentException("input can't be a application");
    }

    private ImageProvider(Activity act){
        this.act = act;
        dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        dir.mkdir();
    }

    public void getImageFromAlbum(OnImageSelectListener mListener,int maxCount){
        this.mListener = mListener;
        Intent intent = new Intent(act, MultiImageSelectorActivity.class);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, false);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxCount);
        startActivityForResult(intent, REQUEST_ALBUM);
    }

    public void getImageFromAlbum(OnImageSelectListener mListener){
        this.mListener = mListener;
        Intent intent = new Intent(act, MultiImageSelectorActivity.class);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, false);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
        startActivityForResult(intent, REQUEST_ALBUM);
    }

    public void getImageFromCamera(OnImageSelectListener mListener){
        this.mListener = mListener;
        tempImage =  FileUtils.createTmpFile(act);
        Uri uri = FileProvider.getUriForFile(act,act.getPackageName()+".fileProvider",tempImage);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                uri);
       startActivityForResult(intent, REQUEST_CAMERA);
    }

    public void getImageFromCameraOrAlbum(OnImageSelectListener mListener){
        this.mListener = mListener;
        Intent intent = new Intent(act, MultiImageSelectorActivity.class);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
        startActivityForResult(intent, REQUEST_ALBUM);
    }

    public void getImageFromCameraOrAlbum(OnImageSelectListener mListener,int maxCount){
        this.mListener = mListener;
        Intent intent = new Intent(act, MultiImageSelectorActivity.class);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxCount);
        startActivityForResult(intent, REQUEST_ALBUM);
    }

    private void startActivityForResult(Intent intent,int requestCode){
        ActivityResultHooker.startHookFragment(act,requestCode, intent, new OnActivityResultListener() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (resultCode != act.RESULT_OK) return ;
                if (mListener == null) return;
                switch (requestCode){
                    case REQUEST_CAMERA:
                        mListener.onImageLoaded(tempImage);
                        break;
                    case REQUEST_ALBUM:
                        List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                        for (String s : path) {
                            mListener.onImageLoaded(new File(s));
                        }
                        break;
                    case REQUEST_CORP:
                        mListener.onImageLoaded(tempImage);
                        break;
                }
            }
        });
    }

    public void corpImage(File file,int width,int height,OnImageSelectListener listener){
        this.mListener = listener;
        tempImage = FileUtils.createTmpFile(act);
        CropImageIntentBuilder cropImage = new CropImageIntentBuilder(width, height,width, height, Uri.fromFile(tempImage));
        cropImage.setSourceImage(Uri.fromFile(file));
        cropImage.setScale(true);
        Intent i = cropImage.getIntent(act);
        startActivityForResult(i, REQUEST_CORP);
    }

    public void corpImageAbsolute(File file,int width,int height,OnImageSelectListener listener){
        this.mListener = listener;
        tempImage = FileUtils.createTmpFile(act);
        CropImageIntentBuilder cropImage = new CropImageIntentBuilder(width, height,width, height, Uri.fromFile(tempImage));
        cropImage.setSourceImage(Uri.fromFile(file));
        Intent i = cropImage.getIntent(act);
        startActivityForResult(i, REQUEST_CORP);
    }

    public static Bitmap readImageWithSize(File file, int outWidth, int outHeight){
        return Utils.readBitmapAutoSize(file.getPath(), outWidth, outHeight);
    }


}
