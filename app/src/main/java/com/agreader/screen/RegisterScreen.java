package com.agreader.screen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.agreader.MasterActivity;
import com.agreader.R;
import com.agreader.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class RegisterScreen extends AppCompatActivity {

    private EditText textEmail, textPassword, textConfirm,textName,textNumber;
    private ProgressDialog pDialog;
    private Button btnRegister;
    private FirebaseAuth mFirebaseAuth;
    String numberPhone = "";
    String name = "";
    String gender = "";
    String age = "";
    String address = "";
    String gambar;
    String totalPoint = "10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);
        mFirebaseAuth=FirebaseAuth.getInstance();
        textName = (EditText) findViewById(R.id.fullname);
        textNumber = (EditText) findViewById(R.id.numberPhone);
        textEmail = (EditText) findViewById(R.id.email);
        textPassword = (EditText) findViewById(R.id.password);
        textConfirm = (EditText) findViewById(R.id.confirmationPassword);
        btnRegister = (Button) findViewById(R.id.registerAkun);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = textEmail.getText().toString().trim();
                String password = textPassword.getText().toString().trim();
                String confirm_password = textConfirm.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                displayLoader();

                if(!confirm_password.equals(password)){
                    Toast.makeText(getApplicationContext(), "Password and Confirmation Password does'nt match!", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }else{
                    mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    pDialog.dismiss();
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(RegisterScreen.this, "Authentication failed : Account already exist", Toast.LENGTH_SHORT).show();
                                    } else {
                                        final FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
                                        final HashMap<String, Object> user= new HashMap<>();
                                        name = textName.getText().toString();
                                        numberPhone = textNumber.getText().toString();
                                        final DatabaseReference dbf = FirebaseDatabase.getInstance().getReference("user").child(currentUser.getUid());
                                        dbf.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                User us = dataSnapshot.getValue(User.class);

                                                if (dataSnapshot.child("gambar").exists()){
                                                    gambar = us.getGambar();
                                                }

                                                if (dataSnapshot.child("numberPhone").exists()){
                                                    numberPhone = us.getNumberPhone();
                                                }

                                                if (dataSnapshot.child("name").exists()){
                                                    name = us.getName();
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

                                                if (dataSnapshot.child("totalPoint").exists()){
                                                    totalPoint = us.getTotalPoint();
                                                }

                                                user.put("numberPhone",numberPhone);
                                                user.put("idEmail",currentUser.getUid());
                                                user.put("idPhone","");
                                                user.put("name",name);
                                                user.put("email",currentUser.getEmail());
                                                user.put("gender",gender);
                                                user.put("age",age);
                                                user.put("address",address);
                                                user.put("gambar",gambar);
                                                user.put("totalPoint",totalPoint);

                                                dbf.setValue(user);

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                        Intent pindah = new Intent(RegisterScreen.this,MasterActivity.class);
                                        pindah.putExtra("tambahPoint","100");
                                        startActivity(pindah);
                                    }
                                }
                            });
                }
            }
        });

    }

    private void displayLoader() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Create Account...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

}
