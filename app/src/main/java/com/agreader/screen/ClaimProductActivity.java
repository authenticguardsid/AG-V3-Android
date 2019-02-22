package com.agreader.screen;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.agreader.R;
import com.agreader.adapter.MyProductAdapter;
import com.agreader.model.ProductModel;
import com.agreader.utils.CustomItemClickListener;
import com.agreader.utils.DataRequest;
import com.agreader.utils.ShowCamera;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ClaimProductActivity extends AppCompatActivity {

    private static int IMG_CAMERA = 2;
    final int REQUEST_CODE_CAMERA = 999;

    Camera camera;
    FrameLayout frameLayout;

    private Uri filepath;
    private File picture_files;
    private Button btnShare;
    private ImageView imagePost, imageKlik;
    ShowCamera showCamera;
    private FirebaseUser firebaseUser;

    private String urlPost = "", code = "", token = "", gambar;
    private double longitude, latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_product);

        btnShare = (Button) findViewById(R.id.btn_share);
        //imagePost = (ImageView) findViewById(R.id.img_post);
        imageKlik = (ImageView) findViewById(R.id.img_klik);

        code = getIntent().getStringExtra("code");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    @Override
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        token = task.getResult().getToken();
                        Log.d("lol", "onCompleteBaru: " + token);
                        String result = "";
                        DataRequest.setUser(ClaimProductActivity.this,token);
                    }
                });

        token = DataRequest.getResultToken(getApplicationContext());

        Log.i("proses2",token);
        System.out.println("Raka"+token + "" + code);

        urlPost = "http://admin.authenticguards.com/api/claim_/" + code + "?token=" + token + "&appid=003";

        camera = Camera.open();
        frameLayout = (FrameLayout) findViewById(R.id.img_post);
        showCamera = new ShowCamera(this, camera);
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
            picture_files = getOutPutMediaFile();
            if (picture_files == null) {
                return;
            } else {
                try {
                    FileOutputStream fos = new FileOutputStream(picture_files);
                    fos.write(data);
                    fos.close();
                    Log.i("proses3",gambar);
                    gambar = picture_files.getName();
                    Log.i("proses4",picture_files+"");
                    camera.startPreview();
                } catch (IOException e) {

                }
            }
        }
    };

    private File getOutPutMediaFile() {
        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            return null;
        } else {
            File folder_gui = new File(Environment.getExternalStorageDirectory() + File.separator + "GUI");
            if (!folder_gui.exists()) {
                folder_gui.mkdirs();
            }
            File outputFile = new File(folder_gui, "temp.jpg");
            picture_files = outputFile;
            Log.i("proses",outputFile+"");
            return outputFile;
        }
    }

    public void foto(View view) {

        if (camera != null) {
            camera.takePicture(null, null, mPictureCallBack);
            btnShare.setVisibility(View.VISIBLE);
            imageKlik.setVisibility(View.GONE);
            //sendPost();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlPost, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        if (ActivityCompat.checkSelfPermission(ClaimProductActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ClaimProductActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }


                        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();

                        Log.i("proses5",longitude+"");
                        Log.i("proses6",latitude+"");

                        response.put("photo",picture_files);
                        response.put("loclong",longitude);
                        response.put("loclang",latitude);

                        Log.i("proses",picture_files+"");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
        }
    }

    private void sendPostt(){

    }

    private void sendPost() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlPost);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setUseCaches(false);


                    conn.connect();
                    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(ClaimProductActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ClaimProductActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }


                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();

                    /*InputStream inputStream = new FileInputStream(picture_files.getName());//You can get an inputStream using any IO API
                    byte[] bytes;
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    try {
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            output.write(buffer, 0, bytesRead);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    bytes = output.toByteArray();
                    String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);
*/
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("photo","");
                    jsonParam.put("loclang",latitude);
                    jsonParam.put("loclong",longitude);


                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();
                    Log.i("Proses","Berhasil");
                    conn.disconnect();
                }catch (Exception e){

                }
            }
        });
        thread.start();
    }

    private void uploadImage(File file){

    }

}
