package com.example.zitech.cameratest;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Button bt;
    private int REQUEST_CODE_TAKE_PICTURE=1;
    private ImageView imageView;
    private Uri imagUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt= (Button) findViewById(R.id.bt_start);
        imageView= (ImageView) findViewById(R.id.img_view);
        final ClipImageViewGroup clipImageViewGroup= (ClipImageViewGroup) findViewById(R.id.clip_imag);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             imageView.setImageBitmap(clipImageViewGroup.getClipedBitmap());
            }
        });
    }

    private void startCaramer(){
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String f = System.currentTimeMillis()+".jpg";
        File file=new File(Environment.getExternalStoragePublicDirectory("")+"/"+"image/");

        if(!file.exists()) {
            file.mkdirs();
        }
        File imag=new File(file,f);
        imagUrl = Uri.fromFile(imag);
        Log.e("文件路径：：",imagUrl.getPath()+"//////////");
        openCameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imagUrl); //指定图片存放位置，指定后，在onActivityResult里得到的Data将为null
        startActivityForResult(openCameraIntent, REQUEST_CODE_TAKE_PICTURE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_EXTERNAL_STORAGE){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                startCaramer();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(imagUrl!=null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagUrl.getPath());
            imageView.setImageBitmap(bitmap);
        }
    }



//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putString("imagurl",imagUrl.getPath());
//    }

//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        String url= (String) savedInstanceState.get("imagurl");
//        Bitmap bitmap=BitmapFactory.decodeFile(url);
//        imageView.setImageBitmap(bitmap);
//    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
     private static String[] PERMISSIONS_STORAGE = {
      Manifest.permission.READ_EXTERNAL_STORAGE,
      Manifest.permission.WRITE_EXTERNAL_STORAGE,
      Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};

    private  void verifyStoragePermissions(Activity activity) {
                 // Check if we have write permission
                 int permission = ActivityCompat.checkSelfPermission(activity,
                                 Manifest.permission.WRITE_EXTERNAL_STORAGE);
                 if (permission != PackageManager.PERMISSION_GRANTED) {
                         // We don't have permission so prompt the user
                         ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                                         REQUEST_EXTERNAL_STORAGE);
                     }else{
                     startCaramer();
                 }
             }
}
