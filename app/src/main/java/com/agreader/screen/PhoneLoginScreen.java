package com.agreader.screen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.agreader.R;
import com.agreader.VerifyPhoneActivity;

public class PhoneLoginScreen extends AppCompatActivity {

    EditText numberPhone;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login_screen);

        numberPhone = (EditText)findViewById(R.id.numberPhone);
        next = (Button)findViewById(R.id.nextNumberPhone);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = numberPhone.getText().toString();

                if (numberPhone.getText().toString().substring(0,1).equals("0")){
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
}
