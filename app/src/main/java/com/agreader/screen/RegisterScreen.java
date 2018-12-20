package com.agreader.screen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.agreader.R;
import com.agreader.VerifyPhoneActivity;

public class RegisterScreen extends AppCompatActivity {

    EditText registerFullName,registerPhoneNumber;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        changeStatusBarColor();


        registerFullName = (EditText)findViewById(R.id.editRegisterName);
        registerPhoneNumber = (EditText)findViewById(R.id.editRegisterPhoneNumber);
        registerButton = (Button) findViewById(R.id.registerAkun);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateForm()){
                    return;
                }else {
                    Intent intent = new Intent(RegisterScreen.this,VerifyPhoneActivity.class);
                    intent.putExtra("phonenumber","+62"+registerPhoneNumber.getText().toString());
                    intent.putExtra("nama",registerFullName.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private boolean validateForm(){
        boolean result = true;
        if (TextUtils.isEmpty(registerFullName.getText().toString())){
            registerFullName.setError("Required");
            result = false;
        }else {
            registerFullName.setError(null);
        }

        if (registerPhoneNumber.getText().toString().substring(0,1).equals("0")){
            registerPhoneNumber.setError("Invalid Number");
            result = false;
        }

        if (TextUtils.isEmpty(registerPhoneNumber.getText().toString())){
            registerPhoneNumber.setError("Required");
            result = false;
        }else if (registerPhoneNumber.getText().toString().length() < 11){
            registerPhoneNumber.setError("enter a valid number");
            result = false;
        }else {
            registerPhoneNumber.setError(null);
        }

        return result;
    }
}
