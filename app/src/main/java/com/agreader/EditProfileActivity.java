package com.agreader;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.agreader.fragment.ProfileFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static com.soundcloud.android.crop.Crop.REQUEST_PICK;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = "LOL";
    ImageView picture;
    EditText name,age,address,email,phonenumber;
    Button savEdit;
    String isComplete;

    private static int IMG_CAMERA = 2;

    private Uri filepath;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private MaterialBetterSpinner spinner;
    private final String [] genderSpinner = {"Male","Female"};

    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);



        picture = (ImageView)findViewById(R.id.fotoProfile);
        name = (EditText)findViewById(R.id.editTextNamaProfile);
        age = (EditText)findViewById(R.id.editTextUmurProfile);
        address = (EditText)findViewById(R.id.editTextLokasiProfile);
        email = (EditText)findViewById(R.id.editTextEmailProfile);
        phonenumber = (EditText)findViewById(R.id.editTextPhoneNumberProfile);
        savEdit = (Button)findViewById(R.id.saveEdit);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        spinner = (MaterialBetterSpinner) findViewById(R.id.genderProfile);
        ArrayAdapter<String> genderAdapter= new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, genderSpinner);
        spinner.setAdapter(genderAdapter);

        getData();

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(EditProfileActivity.this);
            }
        });
        savEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSetting();

            }
        });
    }

    private void getData(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference dbs = FirebaseDatabase.getInstance().getReference("user").child(currentUser.getUid());
        dbs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User us = dataSnapshot.getValue(User.class);
                name.setText(us.getName());
                spinner.setText(us.getGender());
                age.setText(us.getAge());
                address.setText(us.getAddress());
                email.setText(us.getEmail());
                if (!us.getEmail().isEmpty()){
                    email.setFocusable(false);
                }
                Picasso.get().load(us.getGambar()).into(picture);
                phonenumber.setText(us.getNumberPhone());
                if (!us.getNumberPhone().isEmpty()){
                    phonenumber.setFocusable(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                    picture.setImageURI(filepath);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }

            }
            else if (requestCode == IMG_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                picture.setImageBitmap(thumbnail);
            }
        }catch (Exception e){
            Log.d("ClaimProduct", "onActivityResult: "+e.toString());
            Toast.makeText(EditProfileActivity.this, "Silahkan coba lagi", Toast.LENGTH_LONG).show();
        }
    }

    private void saveSetting(){
        final String namee = name.getText().toString();
        final String gendeer = spinner.getText().toString();
        final String agee = age.getText().toString();
        final String addresse = address.getText().toString();
        final String emaile = email.getText().toString();
        final String phoneNumbere = phonenumber.getText().toString();
        final String point;

        final StorageReference ref = storageReference.child("users/"+ UUID.randomUUID().toString());
        if (filepath == null){
            final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            final DatabaseReference dbf = FirebaseDatabase.getInstance().getReference("user").child(currentUser.getUid());
            dbf.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User usr = dataSnapshot.getValue(User.class);
                    isComplete = usr.getCompleteProfile();
                    final HashMap<String, Object> user= new HashMap<>();
                    if(isComplete.equals("false")){
                        if(name.getText().equals("") || spinner.getText().equals("") || age.getText().equals("")|| phonenumber.equals("") || address.getText().equals(""))
                        {
                            Log.d(TAG, "iscomplete : kondisi profil belum lengkap");
                            user.put("name",namee);
                            user.put("gender",gendeer);
                            user.put("age",agee);
                            user.put("address",addresse);
                            user.put("email",emaile);
                            user.put("id",currentUser.getUid());
                            user.put("numberPhone",phoneNumbere);
                            user.put("totalPoint", usr.getTotalPoint());
                            user.put("completeProfile", "false");
                            dbf.setValue(user);
                            Toast.makeText(EditProfileActivity.this, "Profile tersimpan ,Lengkapi Profile dengan mendapatkan 5.000 point", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditProfileActivity.this, MasterActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Log.d("LOL", "iscomplete: " + usr.getCompleteProfile() );
                            Log.d("LOL", "iscomplete: ini kalau sudah lengkap" );
                            user.put("name",namee);
                            user.put("gender",gendeer);
                            user.put("age",agee);
                            user.put("address",addresse);
                            user.put("email",emaile);
                            user.put("id",currentUser.getUid());
                            user.put("numberPhone",phoneNumbere);
                            String pointText = usr.getTotalPoint();
                            int point = Integer.parseInt(pointText);
                            int total = point + 5000;
                            String totalString = Integer.toString(total);
                            user.put("totalPoint", totalString);
                            user.put("completeProfile", "true");
                            dbf.setValue(user);
                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(EditProfileActivity.this);
                            View mView = getLayoutInflater().inflate(R.layout.reward_popup,
                                    null);
                            Button reward;
                            reward = (Button)mView.findViewById(R.id.ok);
                            mBuilder.setView(mView);
                            final AlertDialog dialog = mBuilder.create();
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            reward.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(EditProfileActivity.this,MasterActivity.class);
                                    startActivity(intent);
                                }
                            });
                            dialog.show();
                        }
                    }else {
                        if(name.getText().equals("") || spinner.getText().equals("") || age.getText().equals("")|| phonenumber.equals("") || address.getText().equals("") ){
                            Log.d("LOL", "iscomplete: ini profil sudah pernah lengkap tapi tidak lengkap ");
                            user.put("name",namee);
                            user.put("gender",gendeer);
                            user.put("age",agee);
                            user.put("address",addresse);
                            user.put("email",emaile);
                            user.put("id",currentUser.getUid());
                            user.put("numberPhone",phoneNumbere);
                            user.put("totalPoint", usr.getTotalPoint());
                            user.put("completeProfile", "true");
                            dbf.setValue(user);
                            Toast.makeText(EditProfileActivity.this, "Profile Tersimpan Lengkapi Profil mu dapatkan hadia menarik !", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditProfileActivity.this, MasterActivity.class);
                            startActivity(intent);
                        }else{
                            Log.d("LOL", "iscomplete: ini kondisi dimana profil telah lengkap tapi sudah dapat reward "  );
                            user.put("name",namee);
                            user.put("gender",gendeer);
                            user.put("age",agee);
                            user.put("address",addresse);
                            user.put("email",emaile);
                            user.put("id",currentUser.getUid());
                            user.put("numberPhone",phoneNumbere);
                            user.put("totalPoint", usr.getTotalPoint());
                            user.put("completeProfile", "true");
                            dbf.setValue(user);
                            Toast.makeText(EditProfileActivity.this, "Profile Tersimpan", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditProfileActivity.this, MasterActivity.class);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else {
            UploadTask uploadTask = ref.putFile(filepath);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final  String urlGambar = uri.toString();
                            final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            final DatabaseReference dbf = FirebaseDatabase.getInstance().getReference("user").child(currentUser.getUid());
                            dbf.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    User usr = dataSnapshot.getValue(User.class);
                                    isComplete = usr.getCompleteProfile();
                                    final HashMap<String, Object> user= new HashMap<>();
                                    if(isComplete.equals("false")){
                                        if(name.getText().equals("") || spinner.getText().equals("") || age.getText().equals("")|| phonenumber.equals("") || address.getText().equals(""))
                                        {
                                            Log.d("LOL", "iscomplete: ini kondisi dimana profil belum lengkap dan belum dapat reward " );
                                            user.put("name",namee);
                                            user.put("gender",gendeer);
                                            user.put("age",agee);
                                            user.put("address",addresse);
                                            user.put("email",emaile);
                                            user.put("numberPhone",phoneNumbere);
                                            user.put("gambar",urlGambar);
                                            user.put("totalPoint", usr.getTotalPoint());
                                            user.put("completeProfile", "false");
                                            dbf.setValue(user);
                                            Toast.makeText(EditProfileActivity.this, "Profile tersimpan ,Lengkapi Profile dengan mendapatkan 5.000 point", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(EditProfileActivity.this, MasterActivity.class);
                                            startActivity(intent);
                                        }else{
                                            Log.d("LOL", "iscomplete: " + usr.getCompleteProfile() );
                                            user.put("name",namee);
                                            user.put("gender",gendeer);
                                            user.put("age",agee);
                                            user.put("address",addresse);
                                            user.put("email",emaile);
                                            user.put("numberPhone",phoneNumbere);
                                            user.put("gambar",urlGambar);
                                            String pointText = usr.getTotalPoint();
                                            int point = Integer.parseInt(pointText);
                                            int total = point + 5000;
                                            String totalString = Integer.toString(total);
                                            user.put("totalPoint", totalString);
                                            user.put("completeProfile", "true");
                                            dbf.setValue(user);
                                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(EditProfileActivity.this);
                                            View mView = getLayoutInflater().inflate(R.layout.reward_popup,
                                                    null);
                                            Button reward;
                                            reward = (Button)mView.findViewById(R.id.ok);
                                            mBuilder.setView(mView);
                                            final AlertDialog dialog = mBuilder.create();
                                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                            reward.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent intent = new Intent(EditProfileActivity.this,MasterActivity.class);
                                                    startActivity(intent);
                                                }
                                            });
                                            dialog.show();
                                        }
                                    }else{
                                        if(name.getText().equals("") || spinner.getText().equals("") || age.getText().equals("")|| phonenumber.equals("") || address.getText().equals("") ){
                                            user.put("name",namee);
                                            user.put("gender",gendeer);
                                            user.put("age",agee);
                                            user.put("address",addresse);
                                            user.put("email",emaile);
                                            user.put("id",currentUser.getUid());
                                            user.put("numberPhone",phoneNumbere);
                                            user.put("totalPoint", usr.getTotalPoint());
                                            user.put("completeProfile", "true");
                                            dbf.setValue(user);
                                            Toast.makeText(EditProfileActivity.this, "Profile Tersimpan", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(EditProfileActivity.this, MasterActivity.class);
                                            startActivity(intent);
                                        }else{
                                            Log.d("LOL", "iscomplete: ini kondisi terakir" );
                                            user.put("name",namee);
                                            user.put("gender",gendeer);
                                            user.put("age",agee);
                                            user.put("address",addresse);
                                            user.put("email",emaile);
                                            user.put("id",currentUser.getUid());
                                            user.put("numberPhone",phoneNumbere);
                                            user.put("totalPoint", usr.getTotalPoint());
                                            user.put("completeProfile", "true");
                                            dbf.setValue(user);
                                            Toast.makeText(EditProfileActivity.this, "Profile Tersimpan", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(EditProfileActivity.this, MasterActivity.class);
                                            startActivity(intent);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    });
                }
            });

        }
    }
}