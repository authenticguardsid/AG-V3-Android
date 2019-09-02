package com.agreader.screen;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.agreader.R;
import com.agreader.model.User;
import com.agreader.utils.DataRequest;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {


    FirebaseUser firebaseUser;
    private ProgressDialog pDialog;

    private EditText editText;
    private TextView tunggu,number;
    Dialog dialog;
    String numberPhone = "";
    String nameUser = "";
    String emailnya = "";
    String gender = "";
    String age = "";
    String address = "";
    String gambar = "";
    String totalPoint = "";
    String token = "";
    String filepath;
    String completeProfile = "";
    String onVerify = "";
    private Button kirim;
    Uri uriFilepath;
    String valid;
    String verify;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private PhoneAuthProvider.ForceResendingToken resendingToken;

    String phonenumber, code, yeay, namaa;



    private static final String FORMAT = "%02d:%02d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifiy_phone);
        Intent intent = getIntent();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        numberPhone = intent.getStringExtra("number");
        nameUser = intent.getStringExtra("nameUser");
        emailnya = intent.getStringExtra("emailnya");
        gender = intent.getStringExtra("gender");
        age = intent.getStringExtra("age");
        address = intent.getStringExtra("address");
        gambar = intent.getStringExtra("gambar");
        totalPoint = intent.getStringExtra("totalPoint");
        completeProfile = intent.getStringExtra("completeProfile");
        onVerify = intent.getStringExtra("onverify");

        if (intent.getStringExtra("filepath") != null) {
            filepath = intent.getStringExtra("filepath");

            if (filepath.equals("")) {
                uriFilepath = null;
            } else {
                uriFilepath = Uri.parse(filepath);
            }
        } else {
            uriFilepath = null;
        }




        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    @Override
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        token = task.getResult().getToken();
                        Log.d("lol", "onCompleteBaru: " + token);
                        String result = "";
                        DataRequest.setUser(getApplicationContext(), token);
                        sentOtp(token, numberPhone);
                    }
                });



        editText = (EditText)findViewById(R.id.editTextCode);
        tunggu = (TextView)findViewById(R.id.tunggu);
        number = (TextView)findViewById(R.id.textView);

        kirim = (Button)findViewById(R.id.kirim);



        Intent getData = getIntent();
        if (getData.getStringExtra("nama") != null){
            namaa = getData.getStringExtra("nama");
        }

       kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textfield = editText.getText().toString();
                if (TextUtils.isEmpty(textfield)) {
                    editText.setError("Required");
                    editText.setFocusable(true);
                    return;
                } else {
                    verifyOtp(token, editText.getText().toString(),verify);
                }
            }
        });

        number.setText("Please type the verification code sent to \n " + numberPhone);


        countdownTime();

        changeStatusBarColor();

    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    private void countdownTime(){
        new CountDownTimer(180000, 1000) {

            public void onTick(long millisUntilFinished) {
                tunggu.setText("Please Wait "+String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                tunggu.setText("Resend Token");
                tunggu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        countdownTime();
                        sentOtp(token, numberPhone);
                    }
                });
            }
        }.start();
    }

    public void sentOtp(String token, String number) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://admin.authenticguards.com/api/smsotp?token=" + token + "&appid=003&phone=" + number, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    verify = response.getString("verify");
                    Log.d("onVerify", "onResponse: " + code);
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

    public void verifyOtp(String token,String otp, String verify) {
        displayLoader();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://admin.authenticguards.com/api/Verifysms?token="+token+"&appid=003&code="+otp+"&otp="+verify, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    valid = response.getString("status");
                    Log.d("isvalid", "onResponse: " + valid + response.getString("status"));
                    if (valid.equals("Success")) {
                        pDialog.dismiss();
                        setData(numberPhone, emailnya, nameUser, gender, age, address);
                    } else {
                        pDialog.dismiss();
                        Toast.makeText(VerifyPhoneActivity.this, "Wrong Code", Toast.LENGTH_SHORT).show();

                    }
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

    @Override
    public void onBackPressed() {
    }


    public void setData(final String numberString, final String emailString, final String nameString, final String genderString, final String ageString, final String addressString) {
        final StorageReference ref = storageReference.child("users/" + UUID.randomUUID().toString());
        final String stringNumber = numberString;
        final String stringEmail = emailString;
        final String stringName = nameString;
        final String stringGender = genderString;
        final String stringAge = ageString;
        final String stringAddress = addressString;
        if (uriFilepath == null) {
            final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            final DatabaseReference dbf = FirebaseDatabase.getInstance().getReference("user").child(currentUser.getUid());
            displayLoader();
            dbf.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (completeProfile.equals("false")) {
                        if (!number.equals("") && !emailnya.equals("") && !age.equals("") && !nameUser.equals("")
                                && !address.equals("")) {
                            User us = dataSnapshot.getValue(User.class);
                            HashMap<String, Object> user = new HashMap<>();
                            String url = us.getGambar();
                            user.put("numberPhone", stringNumber);
                            user.put("name", stringName);
                            user.put("email", stringEmail);
                            user.put("gender", stringGender);
                            user.put("age", stringAge);
                            user.put("address", stringAddress);
                            user.put("gambar", url);
                            int reward = Integer.parseInt(totalPoint) + 5000;
                            user.put("totalPoint", Integer.toString(reward));
                            user.put("completeProfile", "true");
                            user.put("onverifiednumber", "true");
                            dbf.setValue(user);
                            pDialog.dismiss();
                            //intent reward
                            View viewthen = getLayoutInflater().inflate(R.layout.fullscreen_popup, null);
                            dialog = new Dialog(VerifyPhoneActivity.this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
                            dialog.setContentView(viewthen);
                            Button buttonhome;
                            buttonhome = viewthen.findViewById(R.id.butthome);
                            buttonhome.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(VerifyPhoneActivity.this, MasterActivity.class);
                                    startActivity(intent);
                                }
                            });
                            dialog.show();
                        } else {
                            User us = dataSnapshot.getValue(User.class);
                            HashMap<String, Object> user = new HashMap<>();
                            String url = us.getGambar();
                            user.put("numberPhone", numberString);
                            user.put("name", nameString);
                            user.put("email", emailString);
                            user.put("gender", genderString);
                            user.put("age", ageString);
                            user.put("address", addressString);
                            user.put("gambar", url);
                            user.put("totalPoint", totalPoint);
                            user.put("completeProfile", "false");
                            user.put("onverifiednumber", "true");
                            dbf.setValue(user);
                            pDialog.dismiss();
                            Toast.makeText(VerifyPhoneActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(VerifyPhoneActivity.this, MasterActivity.class);
                            startActivity(intent);
                        }
                    } else if (completeProfile.equals("true")) {
                        User us = dataSnapshot.getValue(User.class);
                        HashMap<String, Object> user = new HashMap<>();
                        String url = us.getGambar();
                        user.put("numberPhone", numberString);
                        user.put("name", nameString);
                        user.put("email", emailString);
                        user.put("gender", genderString);
                        user.put("age", ageString);
                        user.put("address", addressString);
                        user.put("gambar", url);
                        user.put("totalPoint", totalPoint);
                        user.put("completeProfile", "true");
                        user.put("onverifiednumber", "true");
                        dbf.setValue(user);
                        pDialog.dismiss();
                        Toast.makeText(VerifyPhoneActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(VerifyPhoneActivity.this, MasterActivity.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(VerifyPhoneActivity.this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            UploadTask uploadTask = ref.putFile(uriFilepath);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String urlGambar = uri.toString();
                            final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            final DatabaseReference dbf = FirebaseDatabase.getInstance().getReference("user").child(currentUser.getUid());
                            dbf.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    User usr = dataSnapshot.getValue(User.class);
                                    if (completeProfile.equals(false)) {
                                        if (number.equals("") && emailnya.equals("") && age.equals("") && nameUser.equals("")
                                                && address.equals("")) {
                                            User us = dataSnapshot.getValue(User.class);
                                            HashMap<String, Object> user = new HashMap<>();
                                            user.put("numberPhone", numberString);
                                            user.put("name", nameString);
                                            user.put("email", emailString);
                                            user.put("gender", genderString);
                                            user.put("age", ageString);
                                            user.put("address", addressString);
                                            user.put("gambar", urlGambar);
                                            int reward = Integer.parseInt(totalPoint) + 5000;
                                            user.put("totalPoint", Integer.toString(reward));
                                            user.put("completeProfile", true);
                                            user.put("onverifiednumber", "true");
                                            dbf.setValue(user);
                                            pDialog.dismiss();
                                            //intent reward
                                            View viewthen = getLayoutInflater().inflate(R.layout.fullscreen_popup, null);
                                            dialog = new Dialog(VerifyPhoneActivity.this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
                                            dialog.setContentView(viewthen);
                                            Button buttonhome;
                                            buttonhome = viewthen.findViewById(R.id.butthome);
                                            buttonhome.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent intent = new Intent(VerifyPhoneActivity.this, MasterActivity.class);
                                                    startActivity(intent);
                                                }
                                            });
                                            dialog.show();
                                        } else {
                                            User us = dataSnapshot.getValue(User.class);
                                            HashMap<String, Object> user = new HashMap<>();
                                            user.put("numberPhone", numberString);
                                            user.put("name", nameString);
                                            user.put("email", emailString);
                                            user.put("gender", genderString);
                                            user.put("age", ageString);
                                            user.put("address", addressString);
                                            user.put("gambar", urlGambar);
                                            user.put("totalPoint", totalPoint);
                                            user.put("completeProfile", "true");
                                            user.put("onverifiednumber", "true");
                                            dbf.setValue(user);
                                            pDialog.dismiss();
                                            Toast.makeText(VerifyPhoneActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(VerifyPhoneActivity.this, MasterActivity.class);
                                            startActivity(intent);
                                        }
                                    } else if (completeProfile.equals("true")) {
                                        User us = dataSnapshot.getValue(User.class);
                                        HashMap<String, Object> user = new HashMap<>();
                                        user.put("numberPhone", numberString);
                                        user.put("name", nameString);
                                        user.put("email", emailString);
                                        user.put("gender", genderString);
                                        user.put("age", ageString);
                                        user.put("address", addressString);
                                        user.put("gambar", urlGambar);
                                        user.put("totalPoint", totalPoint);
                                        user.put("completeProfile", "true");
                                        user.put("onverifiednumber", "true");
                                        dbf.setValue(user);
                                        pDialog.dismiss();
                                        Toast.makeText(VerifyPhoneActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(VerifyPhoneActivity.this, MasterActivity.class);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(VerifyPhoneActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                            .getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                }
            });
        }
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Menunggu Verifikasi...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }
}
