package com.agreader.screen;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.agreader.MasterActivity;
import com.agreader.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class LoginScreen extends AppCompatActivity {

    LinearLayout buttonPhoneLogin,buttonemail;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    Button mRegister,buttonLogin;
    private static final int RC_SIGN=9001;
    private static final String TAG="Google Sign";
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient googleApiClient;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if (currentUser != null){
            startActivity(new Intent(LoginScreen.this,MasterActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        mRegister = (Button) findViewById(R.id.register);
        buttonLogin = (Button) findViewById(R.id.btnLogin);

        mRegister.setEnabled(false);
        buttonLogin.setEnabled(false);

        mRegister.setAlpha((float) 0.3);
        buttonLogin.setAlpha((float)0.3);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]
        googleApiClient = new GoogleApiClient.Builder(LoginScreen.this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LoginScreen.this, "You Have An Error", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mFirebaseAuth=FirebaseAuth.getInstance();


        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

            }
        });
        slidingUpPanelLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
            }
        });

        buttonPhoneLogin = (LinearLayout) findViewById(R.id.login_hp);
        buttonPhoneLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginScreen.this, PhoneLoginScreen.class);
                startActivity(i);
            }
        });

        buttonemail = (LinearLayout) findViewById(R.id.login_email);
        buttonemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInwithGoogle();
            }
        });


        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreen.this,RegisterScreen.class);
                startActivity(intent);
                Toast.makeText(LoginScreen.this, "Yeay", Toast.LENGTH_SHORT).show();
            }
        });




    }

   protected void signInwithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN);
    }

    public void syaratKlik(View view){
        boolean checked = ((CheckBox)view).isChecked();
        switch (view.getId()){
            case R.id.check:
                if (checked){
                    mRegister.setEnabled(true);
                    buttonLogin.setEnabled(true);

                    mRegister.setAlpha((float) 1);
                    buttonLogin.setAlpha((float) 1);
                }else {
                    mRegister.setEnabled(false);
                    buttonLogin.setEnabled(false);
                    mRegister.setAlpha((float) 0.3);
                    buttonLogin.setAlpha((float)0.3);
                }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RC_SIGN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()){
                GoogleSignInAccount account= result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }else {
                mFirebaseAuth.signOut();
            }
        }
    }



    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle: "+acct.getId());
        AuthCredential credential= GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
//                            jika sign succes update ui
                            Log.d(TAG, "onComplete: success");
                            FirebaseUser user=mFirebaseAuth.getCurrentUser();
                            startActivity(new Intent(LoginScreen.this,MasterActivity.class));
                            //updateUI(user);
                        }else {
                            Log.w(TAG, "onFailure: ", task.getException() );
                        }
                    }
                });
    }

   /* private void updateUI(FirebaseUser user) {
        Log.i(TAG, "sudah disini");
        Log.i(TAG, "updateUI: " + user);
        if (user != null) {
            Intent intent = new Intent(LoginScreen.this,MasterActivity.class);
            startActivity(intent);
            finish();
        }else  {
            return;
        }
    }
*/

}
