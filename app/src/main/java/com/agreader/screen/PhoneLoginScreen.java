package com.agreader.screen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.agreader.R;
import com.agreader.VerifyPhoneActivity;

public class PhoneLoginScreen extends AppCompatActivity {

    EditText numberPhone;
    Button next;

    final int REQUEST_CODE_SMS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login_screen);

        numberPhone = (EditText)findViewById(R.id.numberPhone);
        next = (Button)findViewById(R.id.nextNumberPhone);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS}, REQUEST_CODE_SMS);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECEIVE_SMS}, REQUEST_CODE_SMS);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_SMS}, REQUEST_CODE_SMS);
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String number = "+62"+numberPhone.getText().toString();

                Log.i("Nomor " ,  number);

                if (numberPhone.getText().toString().substring(0,0).equals("0")){
                    numberPhone.setError("Invalid Number");
                    numberPhone.requestFocus();
                    return;
                }


                if (number.isEmpty()){
                    numberPhone.setError("Required");
                    numberPhone.requestFocus();
                    return;
                }else if (number.length() < 10){
                    numberPhone.setError("Enter a valid number");
                    numberPhone.requestFocus();
                    return;
                }

                Intent intent = new Intent(PhoneLoginScreen.this,VerifyPhoneActivity.class);
                intent.putExtra("phonenumber",number);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE_SMS){
            if(grantResults.length <0 && grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(), "You don't have permission to send sms!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
