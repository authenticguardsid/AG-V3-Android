package com.agreader;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static com.soundcloud.android.crop.Crop.REQUEST_PICK;

public class EditProfileActivity extends AppCompatActivity {

    ImageView picture;
    EditText name,age,address,email,phonenumber;
    Button savEdit;

    final int REQUEST_CODE_GALLERY = 999;

    Intent CropIntent;

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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, REQUEST_CODE_GALLERY);
        }

        getData();

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
        });
        savEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSetting();
                Intent intent = new Intent(EditProfileActivity.this, MasterActivity.class);
                startActivity(intent);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            filepath = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(),bitmap);
                roundedBitmapDrawable.setCircular(true);
                //roundedBitmapDrawable.setCornerRadius(500f);
                picture.setImageDrawable(roundedBitmapDrawable);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void saveSetting(){
        final String namee = name.getText().toString();
        final String gendeer = spinner.getText().toString();
        final String agee = age.getText().toString();
        final String addresse = address.getText().toString();
        final String emaile = email.getText().toString();
        final String phoneNumbere = phonenumber.getText().toString();

        final StorageReference ref = storageReference.child("users/"+ UUID.randomUUID().toString());
        if (filepath == null){
            final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            final DatabaseReference dbf = FirebaseDatabase.getInstance().getReference("user").child(currentUser.getUid());
            dbf.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User usr = dataSnapshot.getValue(User.class);
                    final HashMap<String, Object> user= new HashMap<>();
                    user.put("name",namee);
                    user.put("gender",gendeer);
                    user.put("age",agee);
                    user.put("address",addresse);
                    user.put("email",emaile);
                    user.put("id",currentUser.getUid());
                    user.put("numberPhone",phoneNumbere);
                    dbf.setValue(user);

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
                                    final HashMap<String, Object> user= new HashMap<>();
                                    user.put("name",namee);
                                    user.put("gender",gendeer);
                                    user.put("age",agee);
                                    user.put("address",addresse);
                                    user.put("email",emaile);
                                    user.put("numberPhone",phoneNumbere);
                                    user.put("gambar",urlGambar);
                                    dbf.setValue(user);
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


    private void CropImage() {

        try{
            CropIntent = new Intent("com.android.camera.action.CROP");
            CropIntent.setDataAndType(uri,"image/*");
            CropIntent.putExtra("crop","true");
            CropIntent.putExtra("outputX",180);
            CropIntent.putExtra("outputY",180);
            CropIntent.putExtra("aspectX",3);
            CropIntent.putExtra("aspectY",4);
            CropIntent.putExtra("scaleUpIfNeeded",true);
            CropIntent.putExtra("return-data",true);

            startActivityForResult(CropIntent,2);
        }
        catch (ActivityNotFoundException ex)
        {

        }

    }


}