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
import android.widget.TextView;
import android.widget.Toast;

import com.agreader.R;
import com.agreader.model.User;
import com.agreader.utils.Config;
import com.crowdfire.cfalertdialog.CFAlertDialog;
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

import org.w3c.dom.Text;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ProgressDialog pDialog;
    private EditText txtEmail, txtPassword;
    FirebaseUser currentUser;
    private Button buttonLogin;
    private TextView forgetText;

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
        forgetText = (TextView) findViewById(R.id.forgot);
        forgetText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

        buttonLogin = (Button) findViewById(R.id.login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser fuser = mAuth.getCurrentUser();
        if (fuser != null) {
            if (!fuser.isEmailVerified()) {
                Intent intent = getIntent();
                if (intent.getStringExtra("titleName") != null) {
                    CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
                            .setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION)
                            .setTitle("Hi " + intent.getStringExtra("titleName"))
                            .setMessage("Please check your email, we send a verification email so you can log in to your account");
                    builder.show();
                }

            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
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
                                try {
                                    currentUser = mAuth.getCurrentUser();
                                    if (Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()) {
                                        try {
                                            DatabaseReference dbuser = FirebaseDatabase.getInstance().getReference("user").child(currentUser.getUid());
                                            dbuser.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    User user = dataSnapshot.getValue(User.class);
                                                    if (user.getOnverifiednumber().equals("false")) {
                                                        Intent intent = new Intent(LoginActivity.this, VerifyPhoneActivity.class);
                                                        intent.putExtra("number", user.getNumberPhone());
                                                        intent.putExtra("nameUser", user.getName());
                                                        intent.putExtra("emailnya", user.getEmail());
                                                        intent.putExtra("gender", user.getGender());
                                                        intent.putExtra("age", user.getAge());
                                                        intent.putExtra("address", user.getAddress());
                                                        intent.putExtra("gambar", user.getGambar());
                                                        intent.putExtra("totalPoint", user.getTotalPoint());
                                                        intent.putExtra("completeProfile", user.getCompleteProfile());
                                                        intent.putExtra("onverify", user.getOnverifiednumber());
                                                        startActivity(intent);
                                                    } else {
                                                        sendToHome();
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                        } catch (NullPointerException e) {

                                        }

                                    } else {
                                        Toast.makeText(LoginActivity.this, "Please Verified your email", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (NullPointerException e) {

                                }
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

