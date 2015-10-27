package com.jude.imageprovider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jude.exgridview.ImagePieceView;
import com.jude.exgridview.PieceViewGroup;
import com.jude.library.imageprovider.ImageProvider;
import com.jude.library.imageprovider.OnImageSelectListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements OnImageSelectListener{

    private ImageProvider provider;
    private PieceViewGroup pieceViewGroup;
    private MaterialDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        provider = new ImageProvider(this);
        pieceViewGroup = (PieceViewGroup) findViewById(R.id.piece);
        pieceViewGroup.setOnAskViewListener(new PieceViewGroup.OnAskViewListener() {
            @Override
            public void onAddView() {
                showSelectDialog();
            }
        });
    }


    public void showSelectDialog(){
        new MaterialDialog.Builder(MainActivity.this)
                .title("选择图片来源")
                .items(new String[]{"相机","相册","相册(多张)","网络","裁剪"})
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        switch (i){
                            case 0:
                                provider.getImageFromCamera(MainActivity.this);
                                break;
                            case 1:
                                provider.getImageFromAlbum(MainActivity.this);
                                break;
                            case 2:
                                provider.getImageFromAlbum(MainActivity.this, 9);
                                break;
                            case 3:
                                provider.getImageFromNet(MainActivity.this);
                                break;
                            case 4:
                                //裁剪，用相册的图片做例子。
                                provider.getImageFromAlbum(new OnImageSelectListener() {
                                    @Override
                                    public void onImageSelect() {

                                    }

                                    @Override
                                    public void onImageLoaded(Uri uri) {
                                        //裁剪来源可以是本地的所有有效URI
                                        provider.corpImage(uri, 500, 500, new OnImageSelectListener() {
                                            @Override
                                            public void onImageSelect() {

                                            }

                                            @Override
                                            public void onImageLoaded(Uri uric) {
                                                addImage(uric);
                                            }

                                            @Override
                                            public void onError() {
                                            }
                                        });
                                    }

                                    @Override
                                    public void onError() {

                                    }
                                });
                                break;
                        }
                    }
                })
                .show();
    }


    @Override
    public void onImageSelect() {
        dialog = new MaterialDialog.Builder(MainActivity.this)
                .progress(true,100)
                .title("加载中")
                .content("请稍候")
                .cancelable(false)
                .show();
    }

    @Override
    public void onImageLoaded(Uri uri) {
        dialog.dismiss();
        addImage(uri);
        Log.i("Image", uri.getPath() + " File" + new File(uri.getPath()).exists());

    }

    @Override
    public void onError() {
        dialog.dismiss();
        Toast.makeText(this,"Load Error",Toast.LENGTH_SHORT).show();
    }

    public void addImage(Uri uri){
        ImagePieceView pieceView = new ImagePieceView(MainActivity.this);
        try {
            Log.i("Image", "Size:" + new FileInputStream(new File(uri.getPath())).available());
        } catch (IOException e) {
            Log.i("Image", "Error::"+e.getLocalizedMessage());
        }
        pieceView.setImageBitmap(ImageProvider.readImageWithSize(uri, 200, 200));
        pieceViewGroup.addView(pieceView);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        provider.onActivityResult(requestCode, resultCode, data);
    }



}
