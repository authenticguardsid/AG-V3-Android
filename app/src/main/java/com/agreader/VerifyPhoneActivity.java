package com.agreader;

import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.agreader.screen.Dashboard;
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

    private EditText editText;
    private TextView tunggu,number;

    private PhoneAuthProvider.ForceResendingToken resendingToken;

    String phonenumber,code,yeay,namaa;

    String numberPhone = "";
    String name = "";
    String email = "";
    String gender = "";
    String age = "";
    String address = "";
    String gambar = "https://firebasestorage.googleapis.com/v0/b/ag-version-3.appspot.com/o/users%2Ficons8-male-user-100.png?alt=media&token=4c93ddec-e12e-429c-87e6-1d610531b4df";



    private static final String FORMAT = "%02d:%02d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifiy_phone);

        mAuth = FirebaseAuth.getInstance();


        editText = (EditText)findViewById(R.id.editTextCode);
        tunggu = (TextView)findViewById(R.id.tunggu);
        number = (TextView)findViewById(R.id.textView);

        phonenumber = getIntent().getStringExtra("phonenumber");

        Intent getData = getIntent();
        if (getData.getStringExtra("nama") != null){
            namaa = getData.getStringExtra("nama");
        }

        yeay = phonenumber.toString().trim();
        number.setText("Please type the verification code sent to \n "+phonenumber);
        sendVerificationCode(yeay);

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
                yeay,        // Phone number to verify
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

                            final FirebaseUser currentUser = mAuth.getCurrentUser();

                            final DatabaseReference dbf = FirebaseDatabase.getInstance().getReference("user").child(gabung.getUid());
                            final HashMap<String, Object> user= new HashMap<>();

                            dbf.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    User us = dataSnapshot.getValue(User.class);

                                    if (dataSnapshot.child("gambar").exists()){
                                        gambar = us.getGambar();
                                    }

                                    if (namaa != null){
                                        name = namaa;
                                    }else{
                                        if (dataSnapshot.child("name").exists()){
                                            name = us.getName();
                                        }
                                    }


                                    if (dataSnapshot.child("email").exists()){
                                        email = us.getEmail();
                                    }
                                    if (dataSnapshot.child("gender").exists()){
                                        gender = us.getGender();
                                    }
                                    if (dataSnapshot.child("age").exists()){
                                        age = us.getAge();
                                    }
                                    if (dataSnapshot.child("address").exists()){
                                        address = us.getAddress();
                                    }

                                    user.put("numberPhone",currentUser.getPhoneNumber());
                                    user.put("idPhone",currentUser.getUid());
                                    user.put("idEmail","");
                                    user.put("name",name);
                                    user.put("email",email);
                                    user.put("gender",gender);
                                    user.put("age",age);
                                    user.put("address",address);
                                    user.put("gambar",gambar);

                                    dbf.setValue(user);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            Intent intent = new Intent(VerifyPhoneActivity.this,MasterActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }else {
                            Toast.makeText(VerifyPhoneActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendVerificationCode (String number){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                yeay,
                60,
                TimeUnit.SECONDS,
                this,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            resendingToken = forceResendingToken;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            code = phoneAuthCredential.getSmsCode();
            Toast.makeText(VerifyPhoneActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
            if (code != null){
                editText.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    };
}
