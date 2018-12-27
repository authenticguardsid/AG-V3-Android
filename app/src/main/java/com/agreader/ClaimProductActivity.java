package com.agreader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class ClaimProductActivity extends AppCompatActivity {

    private static int IMG_CAMERA = 2;

    private Uri filepath;
    private Button btnShare;
    private ImageView imagePost, imageKlik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_product);

        btnShare = (Button) findViewById(R.id.btn_share);
        imagePost = (ImageView) findViewById(R.id.img_post);
        imageKlik = (ImageView) findViewById(R.id.img_klik);

        btnShare.setVisibility(View.GONE);
        imageKlik.setVisibility(View.VISIBLE);

        imageKlik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(ClaimProductActivity.this);
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClaimProductActivity.this, ShareActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    filepath = result.getUri();
                    imagePost.setImageURI(filepath);
                    btnShare.setVisibility(View.VISIBLE);
                    imageKlik.setVisibility(View.GONE);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }

            }
            else if (requestCode == IMG_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                imagePost.setImageBitmap(thumbnail);
                btnShare.setVisibility(View.VISIBLE);
                imageKlik.setVisibility(View.GONE);
            }
        }catch (Exception e){
            Log.d("ClaimProduct", "onActivityResult: "+e.toString());
            Toast.makeText(ClaimProductActivity.this, "Silahkan coba lagi", Toast.LENGTH_LONG).show();
        }
    }
}
