package com.jude.imageprovider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.jude.library.imageprovider.ImageProvider;
import com.jude.library.imageprovider.OnImageSelectListener;

public class MainActivity extends AppCompatActivity implements OnImageSelectListener{

    private ImageProvider provider;
    private ProgressDialog dialog;
    private ImageView image;
    private Button btnCamera;
    private Button btnAlbum;
    private Button btnNet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        provider = new ImageProvider(this);
        image       = (ImageView) findViewById(R.id.image);
        btnCamera   = (Button) findViewById(R.id.camera);
        btnAlbum    = (Button) findViewById(R.id.album);
        btnNet      = (Button) findViewById(R.id.net);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                provider.getImageFromCamera(MainActivity.this);
            }
        });
        btnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                provider.getImageFromAlbum(MainActivity.this);
            }
        });
        btnNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                provider.getImageFromNet(MainActivity.this);
            }
        });
    }

    @Override
    public void onImageSelect() {
        dialog = new ProgressDialog(this);
        dialog.show();
    }

    @Override
    public void onImageLoaded(Uri uri) {
        dialog.dismiss();

        provider.corpImage(uri, 300, 300, new OnImageSelectListener() {
            @Override
            public void onImageSelect() {

            }

            @Override
            public void onImageLoaded(Uri uric) {
                image.setImageURI(uric);
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void onError() {
        Toast.makeText(this,"Load Error",Toast.LENGTH_SHORT).show();
        dialog.dismiss();
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
