package com.agreader.screen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.agreader.MasterActivity;
import com.agreader.R;
import com.agreader.utils.Config;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ProgressDialog pDialog;
    private EditText txtEmail, txtPassword;
    private Button buttonLogin;

    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_login);
        changeStatusBarColor();

        mAuth = FirebaseAuth.getInstance();
        txtEmail = (EditText) findViewById(R.id.email);
        txtPassword = (EditText) findViewById(R.id.password);

        buttonLogin = (Button) findViewById(R.id.login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null){
                    sendToHome();
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void checkValidation() {
        displayLoader();
        String getEmailId = txtEmail.getText().toString();
        String getPassword = txtPassword.getText().toString();
        Pattern p = Pattern.compile(Config.regEx);
        Matcher m = p.matcher(getEmailId);
        if (getEmailId.equals("") || getEmailId.length() == 0 || getPassword.equals("") || getPassword.length() == 0) {
            Toast.makeText(this, "Enter both credentials.", Toast.LENGTH_SHORT).show();
            pDialog.dismiss();
        } else if (!m.find()){
            Toast.makeText(this, "Your Email Id is Invalid.", Toast.LENGTH_SHORT).show();
            pDialog.dismiss();
        } else {
            mAuth.signInWithEmailAndPassword(getEmailId, getPassword)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            pDialog.dismiss();
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Your email account is not registered.", Toast.LENGTH_SHORT).show();
                            } else {
                                sendToHome();
                            }
                        }
                    });
        }

    }

    private void sendToHome(){
        Intent intent = new Intent(LoginActivity.this, MasterActivity.class);
        startActivity(intent);
        finish();
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Account Verification...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

}

