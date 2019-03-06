package com.agreader.screen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.agreader.R;
import com.agreader.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {

    private String verificationId;
    private FirebaseAuth mAuth;
    private FirebaseUser gabung;
    private ProgressDialog pDialog;

    private EditText editText;
    private TextView tunggu,number;

    private Button kirim;

    private PhoneAuthProvider.ForceResendingToken resendingToken;

    String phonenumber,code,yeay,namaa;

    String numberPhone = "";
    String name = "";
    String emailnya = "";
    String gender = "";
    String age = "";
    String address = "";
    String gambar = "https://firebasestorage.googleapis.com/v0/b/ag-version-3.appspot.com/o/users%2Fuser.png?alt=media&token=a07b3aa8-90d4-4322-8e1d-8f20b91e54b0";
    String totalPoint = "10000";
    String completeProfile = "false";
    String codebaru = "";


    private static final String FORMAT = "%02d:%02d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifiy_phone);

        mAuth = FirebaseAuth.getInstance();


        editText = (EditText)findViewById(R.id.editTextCode);
        tunggu = (TextView)findViewById(R.id.tunggu);
        number = (TextView)findViewById(R.id.textView);

        kirim = (Button)findViewById(R.id.kirim);

        phonenumber = getIntent().getStringExtra("phonenumber");

        Log.i("Nomor Auth",phonenumber);

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
                    verifyCode(codebaru);
                }
            }
        });

        number.setText("Please type the verification code sent to \n "+phonenumber);
        sendVerificationCode(phonenumber);

        countdownTime();

        changeStatusBarColor();

    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    private void countdownTime(){
        new CountDownTimer(60000, 1000) {

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
                        resendVerificationCode(phonenumber,resendingToken);
                        countdownTime();
                    }
                });
            }
        }.start();
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallBack,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }


    private void verifyCode(String code2){

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code2);
        signInWithCredential(credential);
    }



    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.d("LOL", "onComplete: success");
                            final FirebaseUser currentUser = mAuth.getCurrentUser();
                            final HashMap<String, Object> user= new HashMap<>();
                            name = currentUser.getDisplayName();
                            final DatabaseReference dbf = FirebaseDatabase.getInstance().getReference("user").child(currentUser.getUid());
                            dbf.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        Intent pindah = new Intent(VerifyPhoneActivity.this, MasterActivity.class);

                                        startActivity(pindah);
                                    }

                                    User us = dataSnapshot.getValue(User.class);

                                    gambar = "https://firebasestorage.googleapis.com/v0/b/ag-version-3.appspot.com/o/hadiah%2Fuser.png?alt=media&token=178bff40-3a10-4d6f-8544-c93d7fc2dcc1";

                                    user.put("numberPhone", phonenumber);
                                    user.put("idEmail", currentUser.getUid());
                                    user.put("idPhone", phonenumber);
                                    user.put("name", "");
                                    user.put("email", "");
                                    user.put("gender", "");
                                    user.put("age", "");
                                    user.put("address", "");
                                    user.put("gambar",gambar);
                                    user.put("totalPoint", "10000");
                                    user.put("completeProfile", "false");

                                    dbf.setValue(user);
                                    Intent pindah = new Intent(VerifyPhoneActivity.this, MasterActivity.class);
                                    startActivity(pindah);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }else {
                            Toast.makeText(VerifyPhoneActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendVerificationCode (String number){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                this,
                mCallBack
        );
        Log.i("Nomor Auth","Berhasil");
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            resendingToken = forceResendingToken;
            Log.i("Code","Terkirim");
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            code = phoneAuthCredential.getSmsCode();
            codebaru = code;
            if (code != null){
                editText.setText(code);
                signInWithCredential(phoneAuthCredential);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeAutoRetrievalTimeOut(String s) {
            super.onCodeAutoRetrievalTimeOut(s);
            Log.i("Code","Terikimr2");
            code = editText.getText().toString().trim();
            if (TextUtils.isEmpty(code)){
                Toast.makeText(VerifyPhoneActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
            } else {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(s,code);
                signInWithCredential(credential);
            }

        }
    };

    private void displayLoader() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Create Account...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }
}
