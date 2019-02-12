package com.agreader.screen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.agreader.R;
import com.agreader.utils.ShowCamera;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ClaimProductActivity extends AppCompatActivity {

    private static int IMG_CAMERA = 2;
    final int REQUEST_CODE_CAMERA = 999;

    Camera camera;
    FrameLayout frameLayout;

    private Uri filepath;
    private Button btnShare;
    private ImageView imagePost, imageKlik;
    ShowCamera showCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_product);

        btnShare = (Button) findViewById(R.id.btn_share);
        //imagePost = (ImageView) findViewById(R.id.img_post);
        imageKlik = (ImageView) findViewById(R.id.img_klik);

        camera = Camera.open();
        frameLayout = (FrameLayout)findViewById(R.id.img_post);
        showCamera = new ShowCamera(this,camera);
        frameLayout.addView(showCamera);


        btnShare.setVisibility(View.GONE);
        imageKlik.setVisibility(View.VISIBLE);

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClaimProductActivity.this, ShareActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    Camera.PictureCallback mPictureCallBack = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File picture_files = getOutPutMediaFile();
            if (picture_files == null){
                return;
            }else {
                try {
                    FileOutputStream fos = new FileOutputStream(picture_files);
                    fos.write(data);
                    fos.close();

                    camera.startPreview();
                }  catch (IOException e){

                }
            }
        }
    };

    private File getOutPutMediaFile() {
        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)){
            return null;
        }else {
            File folder_gui = new File(Environment.getExternalStorageDirectory() + File.separator + "GUI");
            if (!folder_gui.exists()){
                folder_gui.mkdirs();
            }
            File outputFile = new File(folder_gui,"temp.jpg");
            return outputFile;
        }
    }

    public void foto(View view) {

        if (camera!= null){
            camera.takePicture(null,null,mPictureCallBack);
            btnShare.setVisibility(View.VISIBLE);
            imageKlik.setVisibility(View.GONE);

        }
    }

}
